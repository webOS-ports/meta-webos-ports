# serial consoles are not very useful in VirtualBox emulator and generates lots of logs
# so let's diable them for LuneOS
SERIAL_CONSOLES:qemux86 = ""
SERIAL_CONSOLES:qemux86-64 = ""

# Also disable serial getty on devices where there is no serial console
SERIAL_CONSOLES:rosy = ""
SERIAL_CONSOLES:tissot = ""
