package com.example.devops.UITest;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.testng.annotations.Test;

public class FoyerUITest {

    @Test
    public void testFoyerPage() {
        // Set up WebDriver
        System.setProperty("webdriver.chrome.driver", "C:/Users/Ma9rouna/OneDrive/Bureau/DevOps/chromedriver-win64");
        WebDriver driver = new ChromeDriver();

        // Navigate to the UI
        driver.get("http://localhost:8080/foyer");

        // Perform assertions (example)
        String title = driver.getTitle();
        assert title.equals("Manage Foyers");

        // Close the browser
        driver.quit();
    }
}
