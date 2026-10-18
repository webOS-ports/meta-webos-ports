/*
 * The Android download's dashboard: one entry in the notification list that
 * says how far the first-start download has got, and stays current.
 *
 * A toast cannot do that - it can only be closed and posted again, and every
 * post pops a banner - but a dashboard is a window of the app's own: a
 * _WEBOS_WINDOW_TYPE_FLOATING window, which the card shell takes into the
 * notification list and keeps drawing (NotificationsMergedModel.qml).
 *
 * The progress comes from /run/waydroid-luneos/download.json, which "waydroid
 * init" rewrites on every update luna-downloadmgr sends (tools/helpers/luna.py
 * in the Waydroid patches). SAM starts QML apps with QML_XHR_ALLOW_FILE_READ,
 * so it is read with XMLHttpRequest.
 *
 * Tapping it relaunches Waydroid to ask where things stand, which is also
 * where the download can be stopped (waydroid-luneos-download). It goes away
 * by itself when the install is done or has failed - the launcher then says
 * why - and when the process doing it is gone, so that an init that was
 * killed outright cannot leave it standing at some percentage forever.
 */
import QtQuick 2.0

import Eos.Window 0.1
import LunaNext.Common 0.1
import LuneOS.Service 1.0

WebOSWindow {
    id: dashboard

    readonly property string stateFile: "file:///run/waydroid-luneos/download.json"

    property string phase: ""
    property real received: 0
    property real total: 0
    property int percent: 0
    property string image: ""
    // Polls in a row that found no state file, or no process behind it.
    property int misses: 0

    width: Settings.displayWidth
    height: Units.gu(7)
    windowType: "_WEBOS_WINDOW_TYPE_FLOATING"
    // qml-runner shows a root Item for us, but a root Window only on a
    // relaunch: on the first launch it stays unmapped and never reaches the
    // notification list unless it asks to be shown.
    visible: true
    color: "transparent"

    Component.onCompleted: {
        dashboard.setWindowProperty("LuneOS_window", "dashboard");
        dashboard.setWindowProperty("LuneOS_metrics", "units");
        dashboard.setWindowProperty("LuneOS_dashheight", 7);
        // The icon the shell shows for it in the minimized notification bar;
        // without it that is the generic one rather than Waydroid's.
        dashboard.setWindowProperty("LuneOS_icon", Qt.resolvedUrl("../icon.png").toString());
        poll();
    }

    // Sizes as the alert and the toasts give them: decimal, like upstream.
    function size(n) {
        if (n >= 1e9)
            return (n / 1e9).toFixed(1) + " GB";
        return Math.max(1, Math.round(n / 1e6)) + " MB";
    }

    function finish() {
        pollTimer.stop();
        Qt.quit();
    }

    // Whether the process that writes the state is still there. /proc is
    // read the same way as the state file; an empty answer means it is gone.
    function alive(pid, done) {
        var xhr = new XMLHttpRequest();
        xhr.onreadystatechange = function() {
            if (xhr.readyState === XMLHttpRequest.DONE)
                done(xhr.responseText.length > 0);
        };
        xhr.open("GET", "file:///proc/" + pid + "/cmdline");
        xhr.send();
    }

    function poll() {
        var xhr = new XMLHttpRequest();
        xhr.onreadystatechange = function() {
            if (xhr.readyState !== XMLHttpRequest.DONE)
                return;
            var s = null;
            try {
                s = JSON.parse(xhr.responseText);
            } catch (e) {
                s = null;
            }
            if (!s) {
                // Not written yet when this starts at the same moment as the
                // download; gone for good once a few polls in a row miss it.
                if (++misses > 10)
                    finish();
                return;
            }
            if (s.state === "done" || s.state === "failed") {
                finish();
                return;
            }
            dashboard.phase = s.state || "";
            received = s.received || 0;
            total = s.total || 0;
            percent = s.percent || 0;
            image = s.image || "";
            if (s.pid) {
                alive(s.pid, function(running) {
                    misses = running ? 0 : misses + 1;
                    if (misses > 3)
                        finish();
                });
            } else {
                misses = 0;
            }
        };
        xhr.open("GET", stateFile);
        xhr.send();
    }

    Timer {
        id: pollTimer
        interval: 1000
        repeat: true
        running: true
        onTriggered: dashboard.poll()
    }

    LunaService {
        id: service
        name: "org.webosports.app.waydroid.download"
    }

    // Laid out like the shell's own notification entries (NotificationItem):
    // the icon in a box of its own, the text in the box beside it.
    Rectangle {
        id: iconBox
        width: Units.gu(6)
        anchors { left: parent.left; top: parent.top; bottom: parent.bottom }
        color: "#393939"
        radius: 8

        Image {
            anchors.fill: parent
            anchors.margins: Units.gu(0.5)
            source: "../icon.png"
            fillMode: Image.PreserveAspectFit
            mipmap: true
        }
    }

    Rectangle {
        anchors {
            left: iconBox.right
            leftMargin: Units.gu(0.5)
            right: parent.right
            top: parent.top
            bottom: parent.bottom
        }
        color: "#393939"
        radius: 8

        Text {
            id: title
            anchors { left: parent.left; right: parent.right; top: parent.top }
            anchors { leftMargin: 10; rightMargin: 10; topMargin: 3 }
            font.bold: true
            font.pixelSize: FontUtils.sizeToPixels("medium")
            color: "white"
            elide: Text.ElideRight
            text: dashboard.phase === "extracting" ? "Installing Android"
                                                   : "Downloading Android"
        }

        Text {
            id: detail
            anchors { left: parent.left; right: parent.right; top: title.bottom }
            anchors { leftMargin: 10; rightMargin: 10 }
            font.pixelSize: FontUtils.sizeToPixels("small")
            color: "white"
            elide: Text.ElideRight
            text: {
                if (dashboard.phase === "extracting")
                    return "Unpacking the " + (dashboard.image || "Android") + " image";
                if (dashboard.total <= 0)
                    return "Starting";
                return dashboard.size(dashboard.received) + " of "
                        + dashboard.size(dashboard.total) + " · "
                        + dashboard.percent + "%";
            }
        }

        // The bar: the download as a whole, both images together.
        Rectangle {
            id: track
            anchors {
                left: parent.left; right: parent.right; bottom: parent.bottom
                leftMargin: 10; rightMargin: 10; bottomMargin: Units.gu(0.8)
            }
            height: Units.gu(0.6)
            radius: height / 2
            color: "#5a5a5a"

            Rectangle {
                anchors { left: parent.left; top: parent.top; bottom: parent.bottom }
                width: parent.width * Math.min(100, Math.max(0, dashboard.percent)) / 100
                radius: parent.radius
                color: "#7ec03d"
                Behavior on width { NumberAnimation { duration: 400 } }
            }
        }
    }

    MouseArea {
        anchors.fill: parent
        onClicked: service.call("luna://com.webos.applicationManager/launch",
                                JSON.stringify({"id": "Waydroid",
                                                "params": {"download": "status"}}),
                                undefined, undefined)
    }
}
