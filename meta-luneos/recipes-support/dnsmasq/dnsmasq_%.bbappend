# lxc and waydroid pull dnsmasq in for the instances lxc-net and
# waydroid-net.sh start on their own bridges (with --conf-file=/dev/null).
# Nothing on LuneOS needs the system-wide dnsmasq.service: connman's
# dnsproxy is the resolver on 127.0.0.1:53, and the two would fight over
# that port. Enabled, it also answered DNS on every interface, Wi-Fi and
# cellular included, because the except-interface= line in
# /etc/dnsmasq.d/lxc cancelled --local-service.
SYSTEMD_AUTO_ENABLE:${PN} = "disable"
