#!/bin/sh


misc_load ()
{
    module="faulty"
    device="faulty"
    mode="664"
    group="staff"

    insmod ./$module.ko || return 1
    major=$(awk "\$2==\"$module\" {print \$1}" /proc/devices)
    if [ ! -z ${major} ]; then
        echo "Remove any existing /dev node for /dev/${device}"
        rm -f /dev/${device}
        echo "Add a node for our device at /dev/${device} using mknod"
        mknod /dev/${device} c $major 0
        echo "Change group owner to ${group}"
        chgrp $group /dev/${device}
        echo "Change access mode to ${mode}"
        chmod $mode  /dev/${device}
    else
        echo "No device found in /proc/devices for driver ${module} (this driver may not allocate a device)"
    fi
}

misc_unload()
{
    module="faulty"
    device="faulty"

    rmmod $(module) || return 1
    rm -f /dev/$(device)
}

cd /lib/modules/$(uname -r)/extra
depmod
case "$1" in
    start)
        misc_load
        insmod ./hello.ko
        ;;
    stop)
        misc_unload
        rmmod hello
        ;;
    *)
        echo "Usage: $0 {start | stop}"
        exit 1
esac

exit 0