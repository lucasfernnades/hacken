# **csv_api**

REST API to store csv files, with support for 3 different types of delimiters most commonly used today.

It also has the option to search using a specific value key.

## Stacks

- Java 17
- Spring
- Maven 3.9.6
- H2 Databse
- JPA
- Swagger
- Micrometer/Prometheus

OBS: The choice of Java 17 was due to having problems with Java 11 during development.
However, I explain here that I did not use any of the new features that version 17 offers unlike previous versions, I only used 17 because the JDK was already installed and I had no problems during the development and testing of the API.
