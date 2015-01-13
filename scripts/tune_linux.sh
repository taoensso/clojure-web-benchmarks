#!/bin/bash

tune_linux() {
    echo "Tuning Linux..."
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
					net.core.somaxconn=1024
					

    echo "If 'ulimit -n 500000' fails, we should modify /etc/security/limits.conf and re-login"
    ulimit -n 500000
    ulimit -S -n 500000
    ulimit -u 127581

    echo | tee -a $OUT
    echo "$(ulimit -a)" | tee -a $OUT
    echo "$(sysctl -a)" | tee -a $OUT
    echo | tee -a $OUT
}

tune_linux