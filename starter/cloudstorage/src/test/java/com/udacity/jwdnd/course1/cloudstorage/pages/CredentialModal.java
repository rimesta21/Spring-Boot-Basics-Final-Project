package com.udacity.jwdnd.course1.cloudstorage.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class CredentialModal {
    @FindBy(id = "credential-url")
    WebElement url;

    @FindBy(id = "credential-username")
    WebElement username;

    @FindBy(id = "credential-password")
    WebElement password;

    @FindBy(id = "credential-submit")
    WebElement submitCredential;

    public CredentialModal(WebDriver driver) {
        PageFactory.initElements(driver, this);
    }

    public void enterCredentials(String url, String username, String password, WebDriver driver) {
        WebDriverWait wait = new WebDriverWait(driver, 10);
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("credential-url")));
        this.url.sendKeys(url);
        this.username.sendKeys(username);
        this.password.sendKeys(password);
        submitCredential.click();
    }

    public void clearCredentials(WebDriver driver) {
        WebDriverWait wait = new WebDriverWait(driver, 10);
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("credential-url")));
        this.url.clear();
        this.username.clear();
        this.password.clear();
    }

    public String returnOriginalPassword(WebDriver driver) {
        WebDriverWait wait = new WebDriverWait(driver, 10);
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("credential-password")));
        return password.getAttribute("value");
    }

}
