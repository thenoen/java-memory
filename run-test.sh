#!/bin/bash

function test {
    echo "running test ${1}"

    javac Test.java  && java -Xmx512m -XX:+UnlockDiagnosticVMOptions -XX:ZTenuringThreshold=10  Test 

}

test x