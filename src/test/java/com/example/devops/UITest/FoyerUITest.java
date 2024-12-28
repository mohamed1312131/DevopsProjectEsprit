package com.example.devops.UITest;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.testng.annotations.Test;

public class FoyerUITest {

    @Test
    public void testFoyerPage() {
        // Set up WebDriver
        System.setProperty("webdriver.chrome.driver", "C:/Users/Ma9rouna/OneDrive/Bureau/DevOps/chromedriver-win64");
        WebDriver driver = new ChromeDriver();

        // Navigate to the UI
        try {
            // Navigate to the UI
            driver.get("http://localhost:8080/foyer");

            // Validate page title
            String title = driver.getTitle();
            assert title.equals("Manage Foyers") : "Page title does not match";

            // Validate an element on the page (example: check for a header)
            WebElement header = driver.findElement(By.tagName("h1"));
            assert header.getText().equals("Welcome to the Foyer Management System") : "Header text does not match";

        } finally {
            // Ensure the browser is closed
            driver.quit();
        }}
}
