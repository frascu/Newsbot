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
[TO DO]

### Bot Configuration
First of all you need a telegram bot you can create thanks to the great [BotFather](https://core.telegram.org/bots#3-how-do-i-create-a-bot).

Create the file `config.properties` in the directory `target`.
The file must have these properties:
* `token`: it is given by the BotFather
* `admin`: the chat id of admin that will manage the bot
* `name`: the bot's name created

### Run
Now, you can execute the file jar generated in the directory `target`.
```sh
cd target
java -jar newsbot-0.0.1-SNAPSHOT-jar-with-dependencies
```
