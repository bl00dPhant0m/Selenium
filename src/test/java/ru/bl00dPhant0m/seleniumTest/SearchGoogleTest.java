package ru.bl00dPhant0m.seleniumTest;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;


import java.io.File;
import java.io.IOException;

import java.nio.file.Files;


public class SearchGoogleTest {

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
    public void searchGoogle() {
        driver.get("https://www.google.com");
        WebElement webElement = driver.findElement(By.name("q"));

        String nameSearch = "java 2025";
        webElement.sendKeys(nameSearch);
        webElement.submit();

        try {
            Thread.sleep(1500);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        String title = driver.getTitle();
        Assertions.assertEquals("java 2025 - Поиск в Google", title);
        System.out.println(title);

        File screenshotAs = driver.getScreenshotAs(OutputType.FILE);
        File file = new File( nameSearch + " screen.png");

        try {
            Files.copy(screenshotAs.toPath(), file.toPath());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }



    }



}
