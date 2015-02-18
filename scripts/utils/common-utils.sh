#!/bin/bash

echo_profiles() {
    echo "Available benching profiles:"
    echo "  1k-keepalive      - 32→1024 conns (keepalive)"
    echo "  1k-non-keepalive  - 32→1024 conns (non-keepalive)"
    echo "  60k-keepalive     - 10k→60k conns (keepalive)"
    echo "  60k-non-keepalive - 10k→60k conns (non-keepalive)"
}
