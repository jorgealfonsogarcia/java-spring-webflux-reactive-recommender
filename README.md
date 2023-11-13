# 🎬 Movie API Project

## Description

🔍 This project is a Spring WebFlux application providing a RESTful API for movie-related operations. It allows users to search for movies by various criteria, get details about specific movies, and retrieve lists of movie genres.

## Features

- 📅 Search movies by a range of release years.
- 🌟 Retrieve movie details including title, description, and popularity.
- 🎭 List all movie genres available.

## Getting Started

### Prerequisites

- Java 21 or later.
- Maven for building and managing the project.

### Installation

1. Clone the repository:
    ```bash
    git clone https://your-repository-url.git
    ```
2. Navigate to the project directory:
    ```bash
    cd movie-api
    ```
3. Build the project:
    ```bash
   mvn clean install
    ```
4. Run the application:
    ```bash
    mvn spring-boot:run
     ```

## Usage
Once the application is running, you can access the following endpoints:

* `GET /movies/search`: Search for movies.
* `GET /movies/genres/{language}`: Get a list of movie genres.

📖 Swagger documentation is available at http://localhost:8080/swagger-ui.html for detailed API usage.

## Contributing
🤝 Contributions to the project are welcome! Please follow these steps:

1. Fork the repository.
2. Create a new branch (`git checkout -b feature/AmazingFeature`).
3. Make your changes.
4. Commit your changes (`git commit -m 'Add some AmazingFeature'`).
5. Push to the branch (`git push origin feature/AmazingFeature`).
6. Open a pull request.

## License
Distributed under the MIT License. See LICENSE for more information.

## Contact
📧 Jorge Garcia - mr-george@georgethepenguin.dev 

Project Link: https://github.com/jorgealfonsogarcia/java-spring-webflux-reactive-recommender