FROM ubuntu:latest AS builder

RUN apt-get update && apt-get install -y wget git xz-utils g++ zlib1g-dev build-essential
RUN wget https://github.com/graalvm/graalvm-ce-builds/releases/download/vm-20.2.0/graalvm-ce-java11-linux-amd64-20.2.0.tar.gz && tar -xzf graalvm-ce-java11-linux-amd64-20.2.0.tar.gz
ENV PATH=/graalvm-ce-java11-20.2.0/bin:$PATH
ENV JAVA_HOME=/graalvm-ce-java11-20.2.0

RUN gu install native-image && apt-get install -y maven

RUN wget https://oligarchy.co.uk/xapian/1.4.17/xapian-core-1.4.17.tar.xz && tar -xf xapian-core-1.4.17.tar.xz && cd xapian-core-1.4.17 && ./configure && make && make install
RUN wget https://oligarchy.co.uk/xapian/1.4.17/xapian-bindings-1.4.17.tar.xz && tar -xf xapian-bindings-1.4.17.tar.xz && cd xapian-bindings-1.4.17 && ./configure && make && make install
RUN mkdir -p ~/.local/lib && cp /xapian-bindings-1.4.17/java/built/libxapian_jni.so ~/.local/lib/ 
RUN mvn install:install-file -Dfile=/xapian-bindings-1.4.17/java/built/xapian.jar -DgroupId=org.xapian -DartifactId=xapian -Dversion=1.4.17 -Dpackaging=jar

ENV LD_LIBRARY_PATH=/xapian-bindings-1.4.17/java/built:/usr/local/lib

RUN git clone https://github.com/frankdressel/zettelkasten.git && cd zettelkasten/zk && git checkout dockerbuild && mvn clean package install


FROM alpine:latest

RUN mkdir /zettelkasten
RUN apk add xapian-core=1.4.17-r0
COPY --from=builder /zettelkasten/zk/target/zk /zettelkasten
COPY --from=builder /zettelkasten/zk/target/_zk /zettelkasten
COPY --from=builder /zettelkasten/zk/target/lib /zettelkasten/lib
