package com.udacity.jwdnd.course1.cloudstorage.pages;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

public class LoginPage {
    @FindBy(id = "inputUsername")
    private WebElement username;

    @FindBy(id = "inputPassword")
    private WebElement password;

    @FindBy(id = "submit-button")
    private WebElement submitButton;

    @FindBy(id = "signup-link")
    private WebElement signup;

    public LoginPage(WebDriver driver) {
        PageFactory.initElements(driver, this);
    }

    public void fillOutLogin (String userName, String password) {
        inputUsername(userName);
        inputPassword(password);
        submitLogin();
    }

    public void inputUsername(String username) {
        this.username.clear();
        this.username.sendKeys(username);
    }

    public void inputPassword(String password) {
        this.password.clear();
        this.password.sendKeys(password);
    }

    public void submitLogin() {
        submitButton.click();
    }

    public void goToSignUp() {
        signup.click();
    }

}
