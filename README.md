# How to Run the Application Locally

## Step 1: Set Up the Database
To run the application locally, you first need to create a PostgreSQL database. Execute the following command:

```sh
docker run --name postgres-0 -e POSTGRES_USER=postgres -e POSTGRES_PASSWORD=password -e POSTGRES_DB=online_shop_db -p 5432:5432 -d postgres:alpine
```

## Step 2: Build the Application Docker Image
Before running the application using Docker Compose, you need to generate a Docker image for the application. Use the following command:

```sh
mvn compile jib:dockerBuild
```

### Important:
Before executing the above command, modify the `pom.xml` file to replace `ivankus197` with your own Docker Hub username in the following section:

```xml
<to>
    <image>your-docker-username/${project.artifactId}:v1</image>
</to>
```

## Step 3: Update `docker-compose.yml`
After building the image, you also need to update the `docker-compose.yml` file. Locate the `online-shop` service and replace `ivankus197` with your Docker Hub username:

```yaml
online-shop:
    image: "your-docker-username/online-shop:v1"
    container_name: online-shop
```

## Step 4: Run the Application with Docker Compose
Once the changes are made, you can start the application using:

```sh
docker-compose up -d
```

## Step 5: Access API Documentation

To view the API documentation, open the following URL in your browser:

http://localhost:8080

You will be redirected to:

http://localhost:8080/swagger-ui/index.html#/

## Step 6: Test API with Postman

You can import the Online Shop Demo.postman_collection.json file into Postman and use it to test the API endpoints.

