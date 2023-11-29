package it.uniroma3.dis;

import it.uniroma3.dis.utils.Constants;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@SpringBootApplication
@Slf4j
public class DataIntegrationStorageApplication implements ApplicationRunner {

    public static void main(String[] args) {
        SpringApplication.run(DataIntegrationStorageApplication.class, args);
    }

    @Override
    public void run(ApplicationArguments args) throws Exception {
        String directoryPath = Constants.DATA_DIRECTORY;

        createDirectory(directoryPath);
    }

    private void createDirectory(String directoryPath) {
        Path path = Paths.get(directoryPath);

        if (Files.notExists(path)) {
            try {
                Files.createDirectories(path);
                log.info("Directory created: {}", directoryPath);
            } catch (Exception e) {
                log.error("Error creating directory: {}", e.getMessage());
            }
        }
    }

}
