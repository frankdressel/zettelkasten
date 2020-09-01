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

tbd

## Builld

```
mvn clean package
```

Optionally for the graal image run:

```
mvn clean install
```

## Run jar file

java -Djava.library.path=. -cp "~/.m2/repository/info/picocli/picocli/4.5.0/picocli-4.5.0.jar:~/.m2/repository/com/google/code/gson/gson/2.8.6/gson-2.8.6.jar:~/.m2/repository/org/xapian/xapian/1.4.17/xapian-1.4.17.jar:target/zk-1.0-SNAPSHOT.jar" de.moduliertersingvogel.zk.ZK add tags=test title="Test Test" """Hallo Welt"""

## Run graal image (optional)

zk add tags=test title="Test Test" """Hallo Welt"""

## Use xapian-delve to inspect the indexed documents

```
xapian-delve -r 1 -d zk.xapian
```

## Usage

tbd
