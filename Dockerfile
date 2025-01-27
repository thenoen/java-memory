FROM amazoncorretto:21

COPY ./Test.java    .
COPY ./run-test.sh  .

# for VisualVM
EXPOSE 9010

ENTRYPOINT ["./run-test.sh", "$HOST_HOSTNAME"]