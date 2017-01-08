Newsbot
========
It's a telegram bot to send the last news of some sources.

Prerequisites:
* [Java SE 8](http://www.oracle.com/technetwork/java/javase/downloads/index.html)
* [Maven 3.3](http://maven.apache.org/install.html)
* [PostgreSQL 9.4](https://www.postgresql.org/download/)

### Installation
1. Clone the repository
2. Install the Maven dependencies
```sh
cd NewsBot
mvn install
```

### Usage
1. PostgreSQL configuration
2. Bot Configuration
3. Run the Newsbot
```sh
cd target
java -jar newsbot-0.0.1-SNAPSHOT-jar-with-dependencies
```

### PostgreSQL configuration
[TO DO]

### Bot Configuration
1. Create the telegram bot with help of the great [BotFather](https://core.telegram.org/bots#3-how-do-i-create-a-bot)
2. Create the file `config.properties` in the directory `target`
3. Insert these properties:
  * `token`: given by the BotFather
  * `admin`: the chat id of admin that will manage the bot
  * `name`: the bot's name created
  * `sources`: the urls separated by `;`

## License
MIT
