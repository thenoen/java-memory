#!/bin/bash

docker build -t java-memory .
docker run \
    -p 9011:9011 \
    -it \
    --rm \
    -e HOST_HOSTNAME=$HOSTNAME \
    --memory 512m \
    --memory-swap 512m \
    java-memory
