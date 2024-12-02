FROM ubuntu:noble

# Install dependencies
RUN apt-get update && \
    apt-get install -y wget libgomp1 maven git build-essential cmake && \
    rm -rf /var/lib/apt/lists/*

# Setup JDK 22
RUN wget -O /tmp/jdk.tar.gz https://github.com/adoptium/temurin22-binaries/releases/download/jdk-22%2B36/OpenJDK22U-jdk_aarch64_linux_hotspot_22_36.tar.gz && \
    mkdir -p /usr/lib/jvm/jdk-22 && \
    tar -xzf /tmp/jdk.tar.gz -C /usr/lib/jvm/jdk-22 --strip-components=1 && \
    rm /tmp/jdk.tar.gz

# Set environment variables
ENV JAVA_HOME=/usr/lib/jvm/jdk-22
ENV PATH=$JAVA_HOME/bin:$PATH

# Verify JNI headers are in place
RUN test -d $JAVA_HOME/include && \
    test -d $JAVA_HOME/include/linux && \
    ls -la $JAVA_HOME/include/jni.h && \
    ls -la $JAVA_HOME/include/linux/jni_md.h

WORKDIR /app

CMD ["tail", "-f", "/dev/null"]
