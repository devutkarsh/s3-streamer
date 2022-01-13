FROM openjdk:8-jdk-alpine
MAINTAINER Dev Utkarsh
ADD s3-streamer-0.0.1-SNAPSHOT.tar /
ENTRYPOINT ["/s3-streamer-0.0.1-SNAPSHOT/bin/s3-streamer"]
