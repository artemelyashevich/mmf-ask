# MMFASK Web API

This project is simulating a stackoverflow system using the Spring
framework.

## Project Setup

To run this project, follow these steps:

1. Clone the repository.
2. Build and run the project using docker-compose.

## Main Web API Functionality

### Post Information

1. Retrieve a list of all posts.
2. Get a specific post by its ID.
3. Add an image to post by its ID.
4. Add a new post.
5. Update information about an existing post.
6. Delete a post.

### Category Information

1. Retrieve a list of all categories.
2. Get a specific category by its ID.
3. Add a new category.
4. Update information about an existing category.
5. Delete a category.

### Programming Language

1. Retrieve a list of all programming languages.
2. Get a specific programming language by its ID.
3. Add a new programming language.
4. Update information about an existing programming language.
5. Delete a programming language.

### User

1. Retrieve a list of all users.
2. Get a specific user by its ID.
3. Add an image to user by its ID.

### Authentication / Authorization

1. Register new user.
2. Set activation code to email to register.
3. Login user.
4. Reset password.
5. Set activation code to email to reset password.

### Commentaries

1. Retrieve a list of all comments by user-post id.
2. Get a specific commentary by its ID.
3. Add a new commentary.
4. Update information about an existing commentary.
5. Delete a commentary.

### Favorites

1. Retrieve a list of all favorites by user email.
2. Retrieve a list of all favorites.
3. Add a new post to favorites.
4. Update information about an existing favorites.
5. Delete a favorites.

## Technologies Used

1. **Spring Boot 3**
2. **Java 17**
3. **ORM**: **Spring Data MongoDB**
4. **MongoDB**
5. **OpenAPI Swagger** for open-api docs
6. **Lombok**
7. **Spring Security** for authentication
8. **JWT**
9. **EmailSender** for activation code sending
10. **Thymeleaf** for receiving views
11. **Docker** for containerization and deployment

### How to start application?

1) Clone project

```
git clone https://github.com/artemelyashevich/library-api.git
```

2) Build project
```
mvn clean && mvn install
```

3) Up docker-compose file
```
docker-compose build && docker-compose up -d 
```