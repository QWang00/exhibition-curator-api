# Exhibition Curator

Exhibition Curator is a Spring Boot-based API service designed to help art enthusiasts discover digital artworks 
and curate personalized collections.

## Getting Started

The following instructions will help you to run this project on your local machine for development
and testing purposes:

1. Clone the repository:

```
    git clone https://github.com/QWang00/exhibition-curator-api.git
```

2. Navigate to the project directory:

```
    cd exhibition-curator-api
```

3. Build the project:

```shell
    mvn clean install
```

4. Run the application:

```shell
    mvn spring-boot:run
```

5. Visit http://localhost:8080/api/v1
6. To test the application:

```shell
    mvn test
```

## API Documentation

[http://localhost:8080/swagger-ui/index.html](http://localhost:8080/swagger-ui/index.html)

## Configuration

1. Create a .env file in the root of the project
2. Request your API key from Harvard Art Museum using the following link:
https://docs.google.com/forms/d/e/1FAIpQLSfkmEBqH76HLMMiCC-GPPnhcvHC9aJS86E32dOd0Z8MpY2rvQ/viewform
3. In the .env file assign your API to the harvard_museum_api_key

## Built With

- Spring Boot: Java-based framework for building stand-alone, production-grade Spring-based
  Applications.
- Maven: Build automation tool used primarily for Java projects.
- Harvard Art Museums API: Provides data about artworks in the Harvard Art Museums collection.
- Cleveland Museum of Art API: Provides data about artworks in the Cleveland Museum of Art collection.
- H2: Lightweight, in-memory database used for development and testing purposes.
- Lombok: Java library that reduces boilerplate code by automatically generating getters, setters,
  constructors, and more.
- Swagger: API documentation tool that helps design, build, document, and consume RESTful web
  services.


## Acknowledgments

[Northcoders](https://northcoders.com/)  
