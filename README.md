# Blog Application

This is a Blog Application built with Spring Boot. It includes features for user registration, login, posting content, and following other users.

## Table of Contents

- [Features](#features)
- [Installation](#installation)
- [Usage](#usage)
- [Technologies](#technologies)
- 
## Features

- User registration with roles (Student, Teacher)
- User login and authentication
- Posting content
- Following and unfollowing users
- Commenting on posts
- User profile management
- Password and email validation

## Installation

To get started with the Blog Application, follow these steps:

1. **Clone the repository**:
   ```sh
   git clone https://FlyByN1ght/blogDiplom.git
   cd blogDiplom
   blogDiplom/
2. **Configure the database**:
   
Set up database (i use PostgreSQL).
The database rises with the application.

## Usage

Once the application is running, you can access it at [http://localhost:8080](http://localhost:8080).

## Endpoints

- `/registration` - User registration
- `/login` - User login
- `/posts` - View posts
- `/profile` - View and edit user profile
- `/follow` - Follow/unfollow users
- `/comments` - Comment on posts

## Example

To register a new user, navigate to [http://localhost:8080/registration](http://localhost:8080/registration) and fill out the form.

## Technologies

- **Java 17**
- **Spring Boot**
- **Spring Security**
- **Spring Data JPA**
- **Hibernate**
- **MySQL / PostgreSQL**
- **Thymeleaf**
- **Maven**
  

