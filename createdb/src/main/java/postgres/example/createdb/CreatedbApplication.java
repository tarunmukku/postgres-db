package postgres.example.createdb;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import org.slf4j.Logger;

import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class CreatedbApplication {

	public static void main(String[] args) {
        Logger logger = LoggerFactory.getLogger(CreatedbApplication.class);
        Connection connection = null;
        Statement statement = null;
        try {
            logger.debug("Creating database if not exist...");
            connection = DriverManager.getConnection("jdbc:postgresql://localhost:5432/", "postgres", "password");
            statement = connection.createStatement();
            statement.executeQuery("SELECT count(*) FROM pg_database WHERE datname = 'healenium'");
            ResultSet resultSet = statement.getResultSet();
            resultSet.next();
            int count = resultSet.getInt(1);

            if (count <= 0) {
                statement.executeUpdate("CREATE DATABASE healenium");
                logger.debug("Database created.");
            } else {
                logger.debug("Database already exist.");
            }
        } catch (SQLException e) {
            logger.error(e.toString());
        } finally {
            try {
                if (statement != null) {
                    statement.close();
                    logger.debug("Closed Statement.");
                }
                if (connection != null) {
                    logger.debug("Closed Connection.");
                    connection.close();
                }
            } catch (SQLException e) {
                logger.error(e.toString());
            }
        }
        SpringApplication.run(CreatedbApplication.class, args);
    }

}
