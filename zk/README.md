wget https://oligarchy.co.uk/xapian/1.4.17/xapian-core-1.4.17.tar.xz -O xapian-core-1.4.17.tar.xz
tar -xf xapian-core-1.4.17.tar.xz
cd xapian-core-1.4.17
./configure
make
sudo make install
cd ..
wget https://oligarchy.co.uk/xapian/1.4.17/xapian-bindings-1.4.17.tar.xz -O xapian-bindings-1.4.17.tar.xz
tar -xf xapian-bindings-1.4.17.tar.xz
cd xapian-bindings-1.4.17
./configure
make
sudo make install

sudo ldconfig

mvn install:install-file -Dfile=<path-to-file> -DgroupId=org.xapian -DartifactId=xapian -Dversion=1.4.17 -Dpackaging=jar

mvn clean compile package

java -Djava.library.path=. -cp "~/.m2/repository/info/picocli/picocli/4.5.0/picocli-4.5.0.jar:~/.m2/repository/org/xapian/xapian/1.4.17/xapian-1.4.17.jar:target/zk-1.0-SNAPSHOT.jar" de.moduliertersingvogel.zk.ZK add tags=test title="Test Test" """Hallo Welt"""

xapian-delve -r 1 -d zk.xapian
