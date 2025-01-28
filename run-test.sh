#!/bin/bash



function test {
    echo "running test ${1}"
    echo $HOST_HOSTNAME

    memory=512

    javac Test.java && java \
        -Xms${memory}m \
        -Xmx${memory}m \
        -XX:+UnlockDiagnosticVMOptions \
        -XX:ZTenuringThreshold=10 \
        -Dcom.sun.management.jmxremote=true \
        -Dcom.sun.management.jmxremote.local.only=false \
        -Dcom.sun.management.jmxremote.authenticate=false \
        -Dcom.sun.management.jmxremote.ssl=false \
        -Dcom.sun.management.jmxremote.port=9011 \
        -Dcom.sun.management.jmxremote.rmi.port=9011 \
        -Djava.rmi.server.hostname=$HOST_HOSTNAME \
        Test 

#                -Dcom.sun.management.jmxremote.local.only=false \
}


test