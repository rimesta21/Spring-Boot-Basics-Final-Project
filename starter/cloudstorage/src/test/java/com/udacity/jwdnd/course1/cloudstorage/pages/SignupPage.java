package com.udacity.jwdnd.course1.cloudstorage.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.WebDriverWait;


public class SignupPage {
    @FindBy(id = "inputFirstName")
    private WebElement firstName;

    @FindBy(id = "inputLastName")
    private WebElement lastName;

    @FindBy(id = "inputUsername")
    private WebElement username;

    @FindBy(id = "inputPassword")
    private WebElement password;

    @FindBy(id = "submit-button")
    private WebElement submitButton;

    @FindBy(id = "login-link")
    private WebElement login;

    public SignupPage(WebDriver driver) {
        WebDriverWait wait = new WebDriverWait(driver, 10);
        wait.until(webDriver -> webDriver.findElement(By.id("inputFirstName")));
        PageFactory.initElements(driver, this);
    }

    public void fillOutSignup(String firstName, String lastName, String userName, String password) {
        inputFirstName(firstName);
        inputLastName(lastName);
        inputUsername(userName);
        inputPassword(password);
        submitSignup();
    }

    public void inputFirstName(String firstName) {
        this.firstName.clear();
        this.firstName.sendKeys(firstName);
    }

    public void inputLastName(String lastName) {
        this.lastName.clear();
        this.lastName.sendKeys(lastName);
    }

    public void inputUsername(String username) {
        this.username.clear();
        this.username.sendKeys(username);
    }

    public void inputPassword(String password) {
        this.password.clear();
        this.password.sendKeys(password);
    }

    public void submitSignup() {
        submitButton.click();
    }

    public void goToLogin(WebDriver driver) {
        WebDriverWait wait = new WebDriverWait(driver, 10);
        wait.until(webDriver -> webDriver.findElement(By.id("login-link")));
        login.click();
    }
}
