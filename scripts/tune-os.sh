#!/bin/bash

tune_osx() {
  echo "Applying OS tuning for: OS X..."
  echo
  sudo sysctl -w \
    kern.maxfiles=100000 \
    kern.maxfilesperproc=50000 \
    net.inet.ip.portrange.first=1024 \
    net.inet.ip.portrange.hifirst=1024 \
    net.inet.tcp.blackhole=2 \
    net.inet.udp.blackhole=1 \
    kern.ipc.somaxconn=30000 \
    kern.ipc.maxsockbuf=800000 \
    net.inet.tcp.msl=1000
  # net.inet.icmp.icmplim=250

  ulimit -n 50000
  ulimit -S -n 50000
  launchctl limit maxfiles 50000

  echo | tee -a $OUT
  echo "$(ulimit -a)" | tee -a $OUT
  echo "$(launchctl limit)" | tee -a $OUT
  echo | tee -a $OUT
}

tune_linux() {
  echo "Applying OS tuning for: Linux..."
  echo
  sudo sysctl -w \
    fs.file-max=999999 \
    net.ipv4.ip_forward=0 \
    net.ipv4.conf.default.rp_filter=1 \
    net.ipv4.conf.default.accept_source_route=0 \
    kernel.sysrq=0 \
    kernel.core_uses_pid=1 \
    net.ipv4.tcp_syncookies=1 \
    kernel.msgmnb=65536 \
    kernel.msgmax=65536 \
    kernel.shmmax=68719476736 \
    kernel.shmall=4294967296 \
    kernel.panic=1 \
    net.core.rmem_max=16777216 \
    net.core.wmem_max=16777216 \
    net.core.rmem_default=16777216 \
    net.core.wmem_default=16777216 \
    net.core.optmem_max=40960 \
    net.ipv4.tcp_rmem=4096 87380 16777216 \
    net.ipv4.tcp_wmem=4096 65536 16777216 \
    net.core.netdev_max_backlog=50000 \
    net.ipv4.tcp_max_syn_backlog=30000 \
    net.ipv4.tcp_max_tw_buckets=2000000 \
    net.ipv4.tcp_tw_reuse=1 \
    net.ipv4.tcp_fin_timeout=10 \
    net.core.somaxconn=1024 \
    net.nf_conntrack_max=655360

  echo "If ulimit reset fails, you may need to modify /etc/security/limits.conf and re-login"
  ulimit -n 500000
  ulimit -S -n 500000

  echo | tee -a $OUT
  echo "$(ulimit -a)" | tee -a $OUT
  echo "$(sysctl -a)" | tee -a $OUT
  echo | tee -a $OUT
}

# TODO Why has this been commented out? Don't we need this?
# if [[ $(/usr/bin/id -u) -ne 0 ]]; then
#     echo "ABORTING: Please run $0 as sudoer (necessary for OS tuning)"
#     exit 1
# fi

case "$1" in
  "osx") tune_osx ;;
  "linux") tune_linux ;;
  *) echo "Usage: $0 {osx|linux}"
     exit 1 ;;
esac

exit 0
