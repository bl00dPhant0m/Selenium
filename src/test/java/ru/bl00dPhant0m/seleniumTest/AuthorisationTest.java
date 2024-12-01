package ru.bl00dPhant0m.seleniumTest;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.List;

public class AuthorisationTest {
    ChromeDriver driver;

    @BeforeEach
    void setUp() {
        driver = new ChromeDriver();

    }

    @AfterEach
    void tearDown() {
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        driver.quit();
    }

    @Test
    public void testAuthorisation() {



        Path pathDir = Path.of("C:\\Users\\Terekhov\\IdeaProjects\\SeleniumLesson\\src\\test\\resources\\authScreen\\");

        if (!pathDir.toFile().exists()) {
            try {
                Files.createDirectory(pathDir);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }

        driver.get("https://the-internet.herokuapp.com/login");

        driver.findElement(By.name("username")).sendKeys("tomsmith");
        driver.findElement(By.name("password")).sendKeys("SuperSecretPassword!");

        File screenshotAsLoginAndPass = driver.getScreenshotAs(OutputType.FILE);

        Path pathLoginAndPassword = Path.of(pathDir.toAbsolutePath().toString(),"LoginAndPassWrite.png");
        //System.out.println(file.toAbsolutePath());

        try {
            Files.copy(screenshotAsLoginAndPass.toPath(), pathLoginAndPassword);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        driver.findElements(By.className("radius")).get(0).click();




        File screenshotAs = driver.getScreenshotAs(OutputType.FILE);
        WebElement flashSuccess = driver.findElement(By.cssSelector("div.flash.success"));
        Path pathAufCheck = Path.of(pathDir.toAbsolutePath().toString(),"AufCheck.png");

        if (flashSuccess.isDisplayed()) {
            try {
                Files.copy(screenshotAs.toPath(), pathAufCheck);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }


    }

    @Test
    public void testAuthorisationWithDifferentData() {
        Path dataPath = Path.of("C:\\Users\\Terekhov\\IdeaProjects\\SeleniumLesson\\src\\test\\resources\\Data\\UsernamesAndPasswords");
        Path resultDir = Path.of("C:\\Users\\Terekhov\\IdeaProjects\\SeleniumLesson\\src\\test\\resources\\authTestData\\");

        if (!resultDir.toFile().exists()) {
            try {
                Files.createDirectory(resultDir);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }

        Path successPath = Path.of(resultDir.toAbsolutePath().toString(),"success.txt");
        Path failedPath = Path.of(resultDir.toAbsolutePath().toString(),"failed.txt");

        List<String> successUsers = new ArrayList<>();
        List<String> failedUsers = new ArrayList<>();

        String username;
        String password;

        try {
            List<String> data = Files.readAllLines(dataPath);
            for (String dataOfUser : data) {

                String[] usernameAndPassword = dataOfUser.split(":");
                if (usernameAndPassword.length > 2){
                    continue;
                }
                username = usernameAndPassword[0].trim();
                password = usernameAndPassword[1].trim();

                driver.get("https://the-internet.herokuapp.com/login");

                driver.findElement(By.name("username")).sendKeys(username);
                driver.findElement(By.name("password")).sendKeys(password);
                driver.findElements(By.className("radius")).get(0).click();



                List<WebElement> flashSuccess = driver.findElements(By.cssSelector("div.flash.success"));

                if (!flashSuccess.isEmpty()) {
                    successUsers.add(dataOfUser);
                }else {
                    failedUsers.add(dataOfUser);
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        try {
            Files.write(successPath, successUsers, StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING);
            Files.write(failedPath, failedUsers, StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }


    }

}
