FROM amazoncorretto:21

COPY ./Test.java    .
COPY ./run-test.sh  .

# for VisualVM
# EXPOSE 9010
EXPOSE 9011
# EXPOSE 9012

ENTRYPOINT ["./run-test.sh"]