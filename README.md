## Simple Book Demo
### Introduction
This the simple book application that I developed using Quarkus
This application will interact the standalone H2.

### Getting Start
####Configure the database connectivity
You should define some database configuration inside the application.properties
```properties
quarkus.datasource.jdbc.url={Please put the H2 JDBC connection string in here}
quarkus.datasource.username={Please put the DB username}
quarkus.datasource.password={Please put the DB password}
```

If you prefer another DB instead of H2, you need add the dependency for database support
#####MySQL
###### build.gradle
```groovy
dependencies {
    implementation("${quarkusGroup}:quarkus-jdbc-mysql")
}
```

#####PostgreSQL
###### build.gradle
```groovy
dependencies {
    implementation("${quarkusGroup}:quarkus-jdbc-postgresql")
}
```


#### How to build the container
To run build the container image, you can run the command below.
```shell
docker build -f Dockerfile.jvm -t simple-book-demo .
```