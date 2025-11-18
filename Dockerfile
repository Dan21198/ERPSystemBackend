FROM maven:3.9-eclipse-temurin-21

WORKDIR /app

# Copy the Maven POM file first (for better caching)
COPY pom.xml .

# Copy the source code
COPY src ./src

# Build the application
RUN mvn package -DskipTests

# Run the jar file
ENTRYPOINT ["java","-jar","/app/target/RPR1-0.0.1-SNAPSHOT.jar"]