package com.example.testingutil;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;

public class PostgreSQLTestContainer implements AutoCloseable {
    private static PostgreSQLTestContainer instance;
    private static boolean shutdownHookRegistered = false;
    private Process dockerComposeProcess;
    private final String jdbcUrl;
    private final String databaseName = "urlshortener";
    private final String username = "urlshortener";
    private final String password = "urlshortenerpassword";

    private PostgreSQLTestContainer() {
        this.jdbcUrl = "jdbc:postgresql://localhost:5432/" + databaseName;
    }

    public static synchronized PostgreSQLTestContainer getInstance() {
        if (instance == null) {
            instance = new PostgreSQLTestContainer();
            instance.startContainer();
            registerShutdownHook();
        }
        return instance;
    }

    private static synchronized void registerShutdownHook() {
        if (!shutdownHookRegistered) {
            Runtime.getRuntime().addShutdownHook(new Thread(() -> {
                PostgreSQLTestContainer.stop();
            }, "PostgreSQL-Container-Shutdown"));
            shutdownHookRegistered = true;
        }
    }

    public static synchronized void start() {
        getInstance();
    }

    private void startContainer() {
        if (!isRunning()) {
            try {
                // Use docker compose up to start containers from docker-compose.yml
                ProcessBuilder pb = new ProcessBuilder("docker", "compose", "up", "-d");
                pb.directory(new File("."));
                pb.inheritIO();
                dockerComposeProcess = pb.start();
                int exitCode = dockerComposeProcess.waitFor();
                if (exitCode != 0) {
                    throw new RuntimeException("docker compose up failed with exit code: " + exitCode);
                }
                // Wait for database to be ready
                waitForDatabaseReady();
                setSystemProperties();
            } catch (IOException | InterruptedException e) {
                throw new RuntimeException("Failed to start docker compose: " + e.getMessage(), e);
            }
        }
    }

    private void waitForDatabaseReady() throws InterruptedException {
        int maxAttempts = 30;
        int attempts = 0;
        while (attempts < maxAttempts) {
            try {
                ProcessBuilder pb = new ProcessBuilder(
                    "docker", "exec", "urlshortener_db", "pg_isready",
                    "-U", username, "-d", databaseName
                );
                Process process = pb.start();
                int exitCode = process.waitFor();
                if (exitCode == 0) {
                    System.out.println("Database is ready");
                    return;
                }
            } catch (IOException e) {
                // Continue retrying
            }
            attempts++;
            Thread.sleep(1000);
        }
        throw new RuntimeException("Database did not become ready within 30 seconds");
    }

    private boolean isRunning() {
        try {
            ProcessBuilder pb = new ProcessBuilder(
                "docker", "inspect", "-f", "{{.State.Running}}", "urlshortener_db"
            );
            Process process = pb.start();
            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            String line = reader.readLine();
            process.waitFor();
            return "true".equalsIgnoreCase(line);
        } catch (IOException | InterruptedException e) {
            return false;
        }
    }

    public static synchronized void stop() {
        if (instance != null) {
            instance.stopContainer();
            instance = null;
        }
    }

    private void stopContainer() {
        try {
            ProcessBuilder pb = new ProcessBuilder("docker", "compose", "down");
            pb.directory(new File("."));
            pb.inheritIO();
            Process process = pb.start();
            int exitCode = process.waitFor();
            if (exitCode != 0) {
                System.err.println("docker compose down failed with exit code: " + exitCode);
            }
        } catch (IOException | InterruptedException e) {
            System.err.println("Error stopping docker compose: " + e.getMessage());
        }
    }

    public static synchronized void reset() {
        stop();
        start();
    }

    @Override
    public void close() {
        stopContainer();
    }

    private void setSystemProperties() {
        System.setProperty("DB_URL", jdbcUrl);
        System.setProperty("DB_NAME", databaseName);
        System.setProperty("DB_USERNAME", username);
        System.setProperty("DB_PASSWORD", password);
    }

    public String getJdbcUrl() {
        return jdbcUrl;
    }

    public String getDatabaseName() {
        return databaseName;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }
}

