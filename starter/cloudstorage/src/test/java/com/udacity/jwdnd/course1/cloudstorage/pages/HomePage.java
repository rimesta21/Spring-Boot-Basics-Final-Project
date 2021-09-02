package com.udacity.jwdnd.course1.cloudstorage.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.util.List;

public class HomePage {
    @FindBy(id = "logout")
    private WebElement logout;

    @FindBy(id = "fileUpload")
    private WebElement fileUpload;

    @FindBy(id = "fileNames")
    private List<WebElement> files;

    @FindBy(id = "view-btn")
    private List <WebElement> viewBtn;

    public HomePage(WebDriver driver) {
        PageFactory.initElements(driver, this);
    }

    public void logout() {
        logout.click();
    }

    public void uploadFile(WebDriver driver, String filePath) {
        fileUpload.sendKeys(filePath);
        fileUpload.submit();
    }

    public String checkForXFile(int index) {
        return files.get(index).getText();
    }

    public void viewXFile(int index) {
        viewBtn.get(index).click();
    }
}
