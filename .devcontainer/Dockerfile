# Base OS
FROM ubuntu:latest

# Metadata
LABEL maintainer="Kuentin"
LABEL description="Custom Java 18 image with Maven. Based on Ubuntu latest."

# Environment variables
ENV SDKMAN_DIR="/usr/local/sdkman"
ENV PATH=${SDKMAN_DIR}/bin:${SDKMAN_DIR}/candidates/maven/current/bin:${PATH}

# Setting working directory
WORKDIR /root

# Copying scripts
COPY library-scripts/maven-debian.sh /tmp/library-scripts/
COPY library-scripts/entrypoint.sh /tmp/library-scripts/

# Running setup commands
RUN apt update --fix-missing && \
    apt install -y openjdk-18-jre openjdk-18-jdk wget git && \
    bash /tmp/library-scripts/maven-debian.sh "latest" "${SDKMAN_DIR}" &&\
    rm -rf /var/lib/apt/lists/*
