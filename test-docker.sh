#!/bin/bash

docker build -t java-memory .
docker run -p 9010:9010 \
    -it \
    --rm \
    -e HOST_HOSTNAME=$HOSTNAME \
    --memory 2048m \
    java-memory
