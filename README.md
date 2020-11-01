# Zettelkasten

This is an implementtion of the [zettelkasten knowledge management method](https://en.wikipedia.org/wiki/Zettelkasten). It aims to be as flexible to the users need as possible and makes minimal assumptions about formatting. Cards are created only with a title, a description and a list of links; each in plain text. The implementation is designed as cli and comes with a simple [vim](https://www.vim.org/) integration within a [docker](https://www.docker.com/) container.

## Demo

### Use Vim to create new cards easily and link them

<figure>
  <img src="/images/create.gif" width="640" height="386"/>
  <figcaption>Open the <a href="https://github.com/SirVer/ultisnips">UltiSnips</a> template by typing &lt;kat-Tab&gt; in insert mode. Navigate between the title, body and links with &lt;Ctrl-b&gt; and &lt;Ctrl-z&gt;. You can save in normal mode with the shortcut &lt;;ka&gt;</figcaption>
</figure>

### Use Vim to list cards and get selected ones

<figure>
  <img src="/images/listget.gif" width="640" height="386"/>
  <figcaption>Type &lt;;kl&gt; in normal mode to get an alphabetically sorted list of card titles. Get the one where the cursor is located with &lt;;kg&gt; in normal mode.</figcaption>
</figure>

### Use Vim to do a wildcard search and get selected search results

<figure>
  <img src="/images/searchget.gif" width="640" height="386"/>
  <figcaption>Type &lt;:Zks&gt; followed by the search term (separated with a space) in normal mode. If the search term contains spaces, escape the whole term with (double) quotes. Get the one where the cursor is located with &lt;;kg&gt; in normal mode.</figcaption>
</figure>

## Requirements

### Container based version (preferred for usage)

Something able to deal with Dockerfiles ([docker](https://www.docker.com/), [podman](https://podman.io/),...

### Non-container based version (preferred for development)

#### Xapian

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

#### Xapian bindings

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

#### Graal (optional)

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

#### jq

```
sudo apt-get install jq
```


## Build

### Container based version (preferred for usage)

```
docker build -t zettelkasten .
```

### Non-container based version (preferred for development)

```
mvn clean package
```

Optionally for the graal image run:

```
mvn clean package install
```

## Run

### Run docker image

```
docker run --rm -it -v <absolute path to the directory, where the data should be stored>:/zk zettelkasten vim
```

### Run jar file

Adadpt the following command with your paths:

```
java -Djava.library.path=<path to your libxapian_jni.so> -cp "<path to your home>/.m2/repository/info/picocli/picocli/4.5.0/picocli-4.5.0.jar:<path to your home>/.m2/repository/com/google/code/gson/gson/2.8.6/gson-2.8.6.jar:<path to your home>/.m2/repository/org/xapian/xapian/1.4.17/xapian-1.4.17.jar:target/zk-1.0-SNAPSHOT.jar" de.moduliertersingvogel.zk.ZK add """{"title": "Test", "text": "Hallo Welt", "links": []}"""
```

### Run graal image (optional)

```
zk add """{"title": "Test", "text": "Hallo Welt", "links": []}"""
```

## Usage

### Vim in docker

In the vim editor provided by the docker image, the following shortcuts can be used:

- _;kl_: Normal mode: Lists the title sof all available cards
- _;kt_: Normal mode: Opens a new buffer with an empty card template
- _;ka_: Normal mode: Adds the text of the current buffer as card (if possible) or updates the card with the same title
- _;kg_: Get the card with the title equal to the current line. Useful in combination with _;kl_.

The following commands are available:

- _:Zks <arg>_: Search the term provided as _arg_. In case the term contains spaces, the whole term needs to be escaped with quotes. Wildcard search is possible with *. Title, text and links of a card are searched.
- _:Zkd <arg>_: Delete the card with the title _arg_. In case the title contains spaces, the whole term needs to be escaped with quotes.

### Cli

### Generate an empty template

```
zk template
```

### Add a new entry

```
zk add """<json with escaped quotes>"""
```

### Search

Please escape search terms including spaces with quotes.

```
zk search <searchterm, use * as wildcard>
```

### Retrieve

Please escape terms including spaces with quotes.

```
zk get <title>
```

### Get list of existing entries (alphabetically sortes)

```
zk list
```

### Delete an entry

Please escape terms including spaces with quotes.

```
zk delete <title>
```
