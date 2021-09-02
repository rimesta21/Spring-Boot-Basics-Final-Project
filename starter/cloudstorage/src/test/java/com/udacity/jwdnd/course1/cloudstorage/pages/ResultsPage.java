package com.udacity.jwdnd.course1.cloudstorage.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.WebDriverWait;

public class ResultsPage {
    @FindBy(id = "linkSuccess")
    private WebElement success;

    @FindBy(id = "linkFailed")
    private WebElement failed;

    @FindBy(id = "download-btn")
    private WebElement download;

    public ResultsPage(WebDriver driver) {
        PageFactory.initElements(driver, this);
    }

    public void clickContSuccess() {
        success.click();
    }

    public void clickContFailure() {
        failed.click();
    }

    public void downloadFile(WebDriver driver) {
        WebDriverWait wait = new WebDriverWait(driver, 10);
        wait.until(webDriver -> webDriver.findElement(By.id("download-btn")));
        download.click();}
}
