# Zettelkasten

## Requirements

### Xapian

(Xapian)[https://xapian.org/] is needed for the full text search and indexing. The process of installation (tested with ubuntu 20.04) is essentially:

```
wget https://oligarchy.co.uk/xapian/1.4.17/xapian-core-1.4.17.tar.xz -O xapian-core-1.4.17.tar.xz
tar -xf xapian-core-1.4.17.tar.xz
cd xapian-core-1.4.17
./configure
make
sudo make install
sudo ldconfig
```

### Xapian bindings

If you want to install zettelkasten from source, you need the java bindings for the xapian api. The process of installation (tested with ubuntu 20.04) is essentially:

```
wget https://oligarchy.co.uk/xapian/1.4.17/xapian-bindings-1.4.17.tar.xz -O xapian-bindings-1.4.17.tar.xz
tar -xf xapian-bindings-1.4.17.tar.xz
cd xapian-bindings-1.4.17
./configure
make
sudo make install
```

The build of the java bindings creates two required files in the built folder: _libxapian_jni.so_ and _xapian.jar_. The first one need to be added to java library path and the last one needs to be imported into local maven repository with:

```
export LD_LIBRARY_PATH=$LD_LIBRARY_PATH:<path-to-libxapian_jni.so>
```

```
mvn install:install-file -Dfile=<path-to-xapian.jar> -DgroupId=org.xapian -DartifactId=xapian -Dversion=1.4.17 -Dpackaging=jar
```

### Graal (optional)

Install the graalvm (change the versions below according to your needs):

```
wget https://github.com/graalvm/graalvm-ce-builds/releases/download/vm-20.2.0/graalvm-ce-java11-linux-amd64-20.2.0.tar.gz
tar -xzf graalvm-ce-java11-linux-amd64-20.2.0.tar.gz
export PATH=<path to GraalVM>/bin:$PATH
export JAVA_HOME=<path to GraalVM>
```

You need the native image component to build images. Install it with:

```
gu install native-image
```

### jq

```
sudo apt-get install jq
```


## Builld

```
mvn clean package
```

Optionally for the graal image run:

```
mvn clean package install
```

## Run jar file

Adadpt the following command with your paths:

```
java -Djava.library.path=. -cp "<path to your home>/.m2/repository/info/picocli/picocli/4.5.0/picocli-4.5.0.jar:<path to your home>/.m2/repository/com/google/code/gson/gson/2.8.6/gson-2.8.6.jar:<path to your home>/.m2/repository/org/xapian/xapian/1.4.17/xapian-1.4.17.jar:target/zk-1.0-SNAPSHOT.jar" de.moduliertersingvogel.zk.ZK add """{"title": "Test", "text": "Hallo Welt", "links": []}"""
```

## Run graal image (optional)

zk add """{"title": "Test", "text": "Hallo Welt", "links": []}"""

## Use xapian-delve to inspect the indexed documents

```
xapian-delve -r 1 -d zk.xapian
```

## Usage

### Generate an empty template

```
zk template
```

### Add a new entry

```
zk add """<json with escaped quotes>"""
```

### Search

```
zk search <searchterm, use * as wildcard>
```

### Retrieve

```
zk get <title>
```
