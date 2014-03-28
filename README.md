What?
=====

MariaDB is "a backward compatible, drop-in replacement of the MySQL(R) Database Server" :
* Homepage: http://mariadb.org
* FAQ: http://kb.askmonty.org/en/mariadb-faq
* Wikipedia: http://en.wikipedia.org/wiki/MariaDB

<<<<<<< master
MariaDB4j is a Java "launcher" for MariaDB, allowing to use it from Java without ANY installation / external dependencies.  Read again: You do NOT have to have MariaDB binaries installed on your system to use MariaDB4j!
=======
MariaDB4j/noEmbedded is a Java "launcher" for MariaDB, allowing to launch a separate instance from Java
>>>>>>> HEAD~3

How?
----
The system-installed MariaDB files are detected and then launched

1. Install the database with a particular configuration, using short-cut:

```java
DB db = DB.newEmbeddedDB(3306);
```

or

```
Configuration config = new Configuration();
config.detectFreePort();
DB db = DB.newEmbeddedDB(config);
```

2. (Optional) The data directory will, by default, be in a temporary directory too, and will automatically get scratched at every restart; this
is suitable for integration tests.  If you use MariaDB4j for something more permanent (maybe an all-in-one application package?),
then you can simply specify a more durable location of your data directory in the Configuration, like so:
```java
Configuration config = new Configuration();
config.setPort(3306);
// OR: config.detectFreePort(); // == config.setPort(0);
config.setDataDir("/home/theapp/db"); // just an example
DB db = DB.newEmbeddedDB(config);
```

3. Start the database
```java
db.start();
```

4. Use the database
```java
Connection conn = db.getConnection();
```
or:
Use your your favourite connection pool... business as usual.

4. If desired, load data from a SQL resource
```java
db.source("path/to/resource.sql");
```

Where from?
-----------

MariaDB4j is not in Maven central (yet; it could be if you asked for it...), 
so for now you (or your build server) have to build it yourself from
source. -- MariaDB4j's Maven coordinates are:

```xml
<groupId>ch.vorburger.mariaDB4j</groupId>
<artifactId>mariaDB4j</artifactId>
<version>1.0.0</version>
```

Why?
----

Being able to start a separate database instance is useful in one
specific scenario: running unit test cases against MySQL legacy
database schema that has too many implementation-specific details to
be emulated with pure Java databases such as H2, hsqldb (HyperSQL),
Derby / JavaDB. With this hack, it is possible to run database related
unit tests in parallel and without configuring access to the system
database.

It was initially developed for use in Mifos, the "Open Source Technology that accelerates Microfinance", see http://mifos.org.


Who?
----
* Michael Vorburger, February/March 2012.
* Michael Seaton, October 2013.
* Juhani Simola, March 2014.
* _YourNameHere, if you jump on-board..._

Contributions, patches, forks more than welcome - hack it, and add your name here! ;-)


[![Bitdeli Badge](https://d2weczhvl823v0.cloudfront.net/vorburger/mariadb4j/trend.png)](https://bitdeli.com/free "Bitdeli Badge")

