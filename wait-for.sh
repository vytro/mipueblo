#!/bin/bash
# wait-for.sh

#set -e
#
#host="$1"
#shift
#cmd="$@"
#
#until echo > /dev/tcp/$host/3306; do
#  >&2 echo "MySQL is unavailable - sleeping"
#  sleep 1
#done
#
#>&2 echo "MySQL is up - executing command"
#exec $cmd


##version 2
##!/bin/bash
## wait-for.sh
#
#set -e
#
#host="$1"
#shift
#cmd="$@"
#
#until echo > /dev/tcp/$host/3306; do
#  >&2 echo "MySQL is unavailable - sleeping"
#  sleep 1
#done
#
#>&2 echo "MySQL is up - executing command"
#exec $cmd