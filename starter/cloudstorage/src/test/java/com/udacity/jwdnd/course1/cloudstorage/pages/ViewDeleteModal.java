package com.udacity.jwdnd.course1.cloudstorage.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class ViewDeleteModal {
    @FindBy(id = "download-btn")
    private WebElement download;

    @FindBy(id = "delete-btn-modal")
    private WebElement delete;

    public ViewDeleteModal(WebDriver driver) {
        PageFactory.initElements(driver, this);
    }

    public void downloadFile(WebDriver driver) {
        WebDriverWait wait = new WebDriverWait(driver, 10);
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("download-btn")));
        download.click();}

    public void delete(WebDriver driver) {
        WebDriverWait wait = new WebDriverWait(driver, 10);
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("delete-btn-modal")));
        delete.click();
    }
}



