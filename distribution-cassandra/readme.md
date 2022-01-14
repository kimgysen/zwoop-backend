
Install Cassandra jars locally 

```
mvn install:install-file -Dfile=/Users/kimgysen/Documents/projects/zwoop-backend/distribution-cassandra/src/main/resources/db/lib/CassandraJDBC42.jar -DgroupId=com.simba.cassandra -DartifactId=SimbaCassandraJDBC42 -Dversion=2.0.12.1013 -Dpackaging=jar -DlocalRepositoryPath=$HOME/.m2/repository
```

```
mvn install:install-file -Dfile=/Users/kimgysen/Documents/projects/zwoop-backend/distribution-cassandra/src/main/resources/db/lib/liquibase-cassandra-4.6.2.jar -DgroupId=org.liquibase.ext -DartifactId=liquibase-cassandra -Dversion=4.6.2 -Dpackaging=jar -DlocalRepositoryPath=$HOME/.m2/repository -Dpom=/liquibase-cassandra-4.6.2.pom
```

Create keyspace: 

```
CREATE KEYSPACE zwoop_chat
WITH REPLICATION = {
    'class' : 'SimpleStrategy',
    'replication_factor' : 1
};
```

```
USE zwoop_chat 
CREATE TABLE IF NOT EXISTS chatroom_messages (
    chatRoomId text,
    date timestamp,
    fromUserId text,
    fromUserNickName text,
    fromUserAvatar text,
    message text,
    PRIMARY KEY ((chatRoomId), date) 
) WITH CLUSTERING ORDER BY (date DESC);
```
```
USE zwoop_chat
CREATE TABLE IF NOT EXISTS private_messages (
    postId text,
    userId text,
    partnerId text,
    date timestamp,
    fromUserId text,
    fromNickName text,
    fromAvatar text,
    toUserId text,
    toNickName text,
    toAvatar text,
    message text,
    PRIMARY KEY ((postId, userId, partnerId), date)
) WITH CLUSTERING ORDER BY (date DESC);
```
```
USE zwoop_chat
CREATE TABLE IF NOT EXISTS inbox_items (
    postId text,
    userId text,
    partnerId text,
    fromUserId text,
    fromNickName text,
    fromAvatar text,
    toUserId text,
    toNickName text,
    toAvatar text,
    unread int static,
    lastMessage text,
    lastMessageDate timestamp,
    PRIMARY KEY ((postId, userId), lastMessageDate)
) WITH CLUSTERING ORDER BY (lastMessageDate DESC);
```
