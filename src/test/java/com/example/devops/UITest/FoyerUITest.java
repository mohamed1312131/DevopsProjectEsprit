package com.example.devops.UITest;

import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

public class FoyerUITest {

    @Test
    public void testFoyerPage() {
        String driverPath = System.getenv("CHROME_DRIVER_PATH");
        if (driverPath == null || driverPath.isEmpty()) {
            throw new IllegalStateException("CHROME_DRIVER_PATH environment variable is not set");
        }
        System.setProperty("webdriver.chrome.driver", driverPath);

        // Configure ChromeOptions for headless mode
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--headless", "--no-sandbox", "--disable-dev-shm-usage");

        WebDriver driver = new ChromeDriver(options);
        try {
            driver.get("http://localhost:8080/foyer");
            String title = driver.getTitle();
            assert title.equals("Manage Foyers") : "Page title mismatch";

            WebElement header = driver.findElement(By.tagName("h1"));
            assert header.getText().equals("Foyer Management") : "Header text mismatch";
        } finally {
            driver.quit();
        }
    }
}