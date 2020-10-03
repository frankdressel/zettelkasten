FROM ubuntu:latest AS builder

RUN apt-get update && apt-get install -y wget git xz-utils g++ zlib1g-dev build-essential musl musl-tools libz-dev
RUN wget https://github.com/graalvm/graalvm-ce-builds/releases/download/vm-20.2.0/graalvm-ce-java11-linux-amd64-20.2.0.tar.gz && tar -xzf graalvm-ce-java11-linux-amd64-20.2.0.tar.gz
ENV PATH=/graalvm-ce-java11-20.2.0/bin:$PATH
ENV JAVA_HOME=/graalvm-ce-java11-20.2.0

RUN gu install native-image && apt-get install -y maven

RUN wget https://oligarchy.co.uk/xapian/1.4.17/xapian-core-1.4.17.tar.xz && tar -xf xapian-core-1.4.17.tar.xz && cd xapian-core-1.4.17 && ./configure && make && make install
RUN wget https://oligarchy.co.uk/xapian/1.4.17/xapian-bindings-1.4.17.tar.xz && tar -xf xapian-bindings-1.4.17.tar.xz && cd xapian-bindings-1.4.17 && ./configure --with-java && make && make install
RUN cp /xapian-bindings-1.4.17/java/built/libxapian_jni.so /usr/local/lib
RUN mvn install:install-file -Dfile=/xapian-bindings-1.4.17/java/built/xapian.jar -DgroupId=org.xapian -DartifactId=xapian -Dversion=1.4.17 -Dpackaging=jar

ENV LD_LIBRARY_PATH=/usr/local/lib

RUN mkdir -p /zettelkasten/zk
COPY . /zettelkasten/zk/
RUN cd /zettelkasten/zk && mvn clean package install


FROM ubuntu:latest
RUN mkdir /zettelkasten
COPY --from=builder /zettelkasten/zk/target/zk /zettelkasten/
COPY --from=builder /zettelkasten/zk/target/_zk /zettelkasten/
COPY --from=builder /xapian-bindings-1.4.17/java/built/libxapian_jni.so /zettelkasten/
COPY --from=builder /usr/local/lib/libxapian.so /usr/local/lib/
RUN echo base=/zk >> /zettelkasten/zk.conf
RUN ln -s /usr/local/lib/libxapian.so /usr/local/lib/libxapian.so.30 
RUN ln -s /usr/local/lib/libxapian.so /usr/local/lib/libxapian.so.30.10.3
RUN ldconfig
