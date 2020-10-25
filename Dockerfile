FROM ubuntu:latest AS builder

ENV LANG C.UTF-8
ENV LANGUAGE C
ENV LC_ALL C.UTF-8

RUN apt-get update && apt-get install -y wget git xz-utils g++ zlib1g-dev build-essential
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


FROM ubuntu:20.04
RUN mkdir /zettelkasten && mkdir /zk
ENV LANG C.UTF-8
ENV LANGUAGE C
ENV LC_ALL C.UTF-8
COPY --from=builder /zettelkasten/zk/target/zk /zettelkasten/
COPY --from=builder /zettelkasten/zk/target/_zk /zettelkasten/
COPY --from=builder /xapian-bindings-1.4.17/java/built/libxapian_jni.so /zettelkasten/
COPY --from=builder /usr/local/lib/libxapian.so /usr/local/lib/
RUN echo base=/zk >> /zettelkasten/zk.conf
RUN ln -s /usr/local/lib/libxapian.so /usr/local/lib/libxapian.so.30 
RUN ln -s /usr/local/lib/libxapian.so /usr/local/lib/libxapian.so.30.10.3
RUN ldconfig

RUN apt-get update
RUN apt-get install -y ca-certificates
RUN apt-get install --no-install-recommends -y vim
RUN apt-get install --no-install-recommends -y git
RUN apt-get install --no-install-recommends -y jq
COPY src/main/resources/vim/.vimrc /root/.vimrc
RUN mkdir -p /root/.vim/UltiSnips
COPY src/main/resources/vim/all.snippets /root/.vim/UltiSnips/all.snippets
RUN git clone https://github.com/VundleVim/Vundle.vim.git ~/.vim/bundle/Vundle.vim && vim +PluginInstall +qall
