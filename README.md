# kafka-fun template repo

This template repo provides a simple Hello web app accessing Kafka, based on Spring Boot and Spring Cloud Function.

It can be deployed as a standalone web app, as a Kubernetes Deployment and Service, or as a Knative Service.
It will connect to Kafka on `localhost:9092` unless you provide a `spring.kafka.bootstrap-servers` connection property for connecting to a different Kafka cluster.

## The code

> **NOTE**: The project is configured for Java 11, if you are using Java 8, then modify the `java.version` property in `pom.xml`.

The project contains the following:

```text
.
├── mvnw
├── mvnw.cmd
├── pom.xml
├── README.md
└── src
    ├── main
    │   ├── java
    │   │   └── com
    │   │       └── example
    │   │           └── hello
    │   │               └── KafkaFunApplication.java
    │   └── resources
    │       ├── application.properties
    │       ├── static
    │       └── templates
    └── test
        └── java
            └── com
                └── example
                    └── hello
                        └── KafkaFunApplicationTests.java

14 directories, 7 files
```

You can modify the source code using [Visual Studio Code](https://code.visualstudio.com/):

```bash
code .
```

The Function that is used by this app is located at `src/main/java/com/example/helloapp/KafkaFunApplication.java`

You can build the project using the provided Maven wrapper:

```bash
./mvnw clean package
```

## Standalone app with embedded Tomcat server and local Kafka

### Run Kafka locally using `docker-compose`

Create a `docker-compose.yaml` file with the following content:

```yaml
version: '3'

services:
  zookeeper:
    image: wurstmeister/zookeeper

  kafka:
    image: wurstmeister/kafka
    ports:
      - "9092:9092"
    environment:
      KAFKA_ADVERTISED_HOST_NAME: localhost
      KAFKA_ZOOKEEPER_CONNECT: zookeeper:2181
```

Start this local Kafka "cluster" using:

```sh
docker-compose up -d
```

### Run the sample app locally 

To run the app locally using the embedded Tomcat server you can run this command:

```bash
./mvnw spring-boot:run
```

> **NOTE**: The app will connect to Kafka on `localhost:9092` by default

You can access the function using `curl`:

```bash
curl -w'\n' -H 'Content-Type: text/plain' localhost:8080 -d "Fun"
```

You should get this reply from the function:

```text
Sent Fun to 'hello' topic
```

You should also see some logging from the app. It should be similar to this:

```text
2020-10-15 15:35:59.599  INFO 35542 --- [nio-8080-exec-1] o.a.kafka.common.utils.AppInfoParser     : Kafka version: 2.6.0
2020-10-15 15:35:59.599  INFO 35542 --- [nio-8080-exec-1] o.a.kafka.common.utils.AppInfoParser     : Kafka commitId: 62abe01bee039651
2020-10-15 15:35:59.599  INFO 35542 --- [nio-8080-exec-1] o.a.kafka.common.utils.AppInfoParser     : Kafka startTimeMs: 1602790559599
2020-10-15 15:35:59.605  INFO 35542 --- [ad | producer-1] org.apache.kafka.clients.Metadata        : [Producer clientId=producer-1] Cluster ID: jUcT517vRU2_6O6-pPYjxA
2020-10-15 15:35:59.629  INFO 35542 --- [nio-8080-exec-1] c.f.c.c.BeanFactoryAwareFunctionRegistry : Looking up function '' with acceptedOutputTypes: []
2020-10-15 15:35:59.644  INFO 35542 --- [mpleGroup-0-C-1] com.example.hello.KafkaFunApplication    : Received: Fun
```