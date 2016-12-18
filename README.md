# Newsbot
It's a telegram bot to send the last news of a source.

Prerequisites:
* [Java SE 8](http://www.oracle.com/technetwork/java/javase/downloads/index.html)
* [Maven 3.3](http://maven.apache.org/install.html)
* [PostgreSQL 9.4](https://www.postgresql.org/download/)

### Maven
When you cloned this repository, execute these commands:
```sh
cd NewsBot
mvn install
```

### PostgreSQL configuration
[...]

### Bot Configuration
Create the file `config.properties` in the directory `target`.
The file must have these properties:
* `token`
* `admin`
* `name`

### Execution
Now, you can execute the file jar generated in the directory `target`.
```sh
cd target
java -jar newsbot-0.0.1-SNAPSHOT-jar-with-dependencies
```
