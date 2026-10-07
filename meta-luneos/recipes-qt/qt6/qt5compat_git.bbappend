# Qt5Compat.GraphicalEffects ships its shaders prebuilt (shaders_ng/*.qsb), baked
# from sources that declare the uniform block as
#
#     layout(std140, binding = 0) uniform buf { mat4 qt_Matrix; float qt_Opacity; ... };
#
# spirv-cross turns that into a GLSL ES 1.00 uniform struct with a mat4 first
# member. The Adreno 3xx compiler (hammerhead-halium, via libhybris) reads every
# member that follows a mat4 in such a struct from the wrong place: qt_Opacity
# comes out as a matrix element, i.e. about 0, so OpacityMask, Desaturate,
# FastBlur, LinearGradient, ColorOverlay and the rest draw nothing. A struct
# with a vec4[4] matrix, which is what Qt's own shaders declare, is read
# correctly, so bake the shaders again that way.
DEPENDS += "qtshadertools-native"

do_configure:prepend() {
    cd ${S}/src/imports/graphicaleffects5/shaders_ng
    for f in *.frag *.vert; do
        sed -i \
            -e 's/^\( *\)mat4 qt_Matrix;/\1vec4 qt_Matrix[4];/' \
            -e 's/\bqt_Matrix \* qt_Vertex/mat4(qt_Matrix[0], qt_Matrix[1], qt_Matrix[2], qt_Matrix[3]) * qt_Vertex/' \
            $f
        case $f in
            *.vert) batchable="-b" ;;
            *)      batchable="" ;;
        esac
        # same arguments as compile.bat, minus -c (fxc only exists on Windows)
        qsb $batchable --glsl "150,120,100 es" --hlsl 50 --msl 12 -o $f.qsb $f
    done
    cd ${B}
}
