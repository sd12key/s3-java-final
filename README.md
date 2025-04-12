
# GymApp

A Java application for gym management built with Maven.

## Prerequisites

- **Java 21 JDK** ([Download](https://www.oracle.com/java/technologies/javase/jdk21-archive-downloads.html))
- **Maven** ([Download](https://maven.apache.org/download.cgi))
- **PostgreSQL** (optional, if using database features)

## Installation

### 1. Install Java 21
Ensure you have Java 21 JDK installed:
```bash
java -version
```

### 2. Install Maven
#### Windows:
1. Download from [Maven website](https://maven.apache.org/download.cgi)
2. Unzip and add to PATH

#### macOS/Linux:
```bash
# macOS (Homebrew)
brew install maven

# Linux (Debian/Ubuntu)
sudo apt install maven
```

Verify installation:
```bash
mvn -version
```

## Building and Running

### Quick Start (Development)
```bash
# Clone the repository
git clone https://github.com/yourusername/GymApp.git
cd GymApp

# Run the application
mvn exec:java
```

### Build Options

| Command | Description |
|---------|-------------|
| `mvn exec:java` | Run the application normally |
| `mvn exec:java@drop-run` | Run with `--drop` argument (drops tables) |
| `mvn exec:java@drop-init-run` | Run with `--drop --init` arguments |

### Production Build (Fat JAR)
```bash
# Build the executable JAR (includes all dependencies)
mvn clean package

# Run the built JAR
java -jar target/GymApp.jar

# With arguments:
java -jar target/GymApp.jar --drop
java -jar target/GymApp.jar --drop --init
```

## Project Structure

```
GymApp/
├── src/                 # Source files
├── target/              # Build output (JAR files here)
│   └── GymApp.jar       # Main executable JAR
├── pom.xml              # Maven configuration
└── README.md            # This file
```

## Database Configuration

If using PostgreSQL:
1. Install PostgreSQL
2. Create database and user
3. Configure connection in application properties

## Troubleshooting

- **Java version issues**: Ensure JDK 21 is installed
- **Dependency problems**: Try `mvn clean install`
- **Database errors**: Verify PostgreSQL is running and credentials are correct
