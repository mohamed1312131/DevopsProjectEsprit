package com.example.devops.UITest;

import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

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
            // Open the page and log the action
            System.out.println("Navigating to http://localhost:8080/foyer");
            driver.get("http://localhost:8080/foyer");

            // Wait for the page title to be correct
            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
            wait.until(ExpectedConditions.titleIs("Manage Foyers"));

            // Log the title for debugging purposes
            String title = driver.getTitle();
            System.out.println("Page title: " + title);

            // Assert that the title is as expected
            assert title.equals("Manage Foyers") : "Page title mismatch";

            // Find and log the header text
            WebElement header = driver.findElement(By.tagName("h1"));
            String headerText = header.getText();
            System.out.println("Header text: " + headerText);

            // Assert that the header text is as expected
            assert headerText.equals("Foyer Management") : "Header text mismatch";
        } catch (Exception e) {
            // Log the exception if something goes wrong
            System.err.println("Test failed: " + e.getMessage());
            e.printStackTrace();
        } finally {
            // Always quit the driver at the end
            driver.quit();
        }
    }
}
