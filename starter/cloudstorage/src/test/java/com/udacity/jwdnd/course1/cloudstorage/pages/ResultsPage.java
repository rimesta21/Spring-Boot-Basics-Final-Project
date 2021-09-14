package com.udacity.jwdnd.course1.cloudstorage.pages;


import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;


public class ResultsPage {
    @FindBy(id = "linkSuccess")
    private WebElement success;

    @FindBy(id = "linkFailed")
    private WebElement failed;

    public ResultsPage(WebDriver driver) {
        PageFactory.initElements(driver, this);
    }

    public void clickContSuccess() {
        success.click();
    }

    public void clickContFailure() {
        failed.click();
    }

}
