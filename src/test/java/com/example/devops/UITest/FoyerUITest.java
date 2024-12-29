package com.example.devops.UITest;

import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;

public class FoyerUITest {

    @Test
    public void testFoyerPage() {
        System.setProperty("webdriver.chrome.driver", "C:/Users/Ma9rouna/OneDrive/Bureau/DevOps/chromedriver-win64/chromedriver.exe");
        WebDriver driver = new ChromeDriver();

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
