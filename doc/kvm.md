## kvm安装ubuntu

```bash
# 在host上安装软件
$ sudo apt-get update
$ sudo apt-get upgrade
$ sudo apt-get install qemu-kvm libvirt-bin virtinst bridge-utils cpu-checker virt-manager

# Verify kvm installation
$ kvm-ok
INFO: /dev/kvm exists
KVM acceleration can be used

# 配置network
$ sudo cp /etc/network/interfaces /etc/network/interfaces.backup #备份
$ sudo vi /etc/network/interfaces

$ sudo systemctl restart networking

# 新建os
$ sudo virt-install  --virt-type=kvm  --name slave1 --ram 16384  --vcpus=2 --os-type linux --os-variant=ubuntu16.04 --cdrom=/var/lib/libvirt/iso/ubuntu-16.04-server-amd64.iso  --disk path=/var/lib/libvirt/images/ubuntu16_1.img, size=80
```
