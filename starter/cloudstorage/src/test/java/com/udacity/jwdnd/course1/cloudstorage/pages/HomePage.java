package com.udacity.jwdnd.course1.cloudstorage.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.util.List;

public class HomePage {
    @FindBy(id = "logout")
    private WebElement logout;

    @FindBy(id = "fileUpload")
    private WebElement fileUpload;

    @FindBy(id = "fileNames")
    private List<WebElement> files;

    @FindBy(id = "view-btn-file")
    private List <WebElement> viewBtnFile;

    @FindBy(id = "delete-btn-file")
    private List<WebElement> deleteFile;

    @FindBy(id = "edit-btn-credential")
    private List <WebElement> editBtnCredential;

    @FindBy(id = "delete-btn-credential")
    private List<WebElement> deleteCredential;

    @FindBy(id = "nav-credentials-tab")
    private WebElement credentialsTab;

    @FindBy(id = "add-credential")
    private WebElement addCredential;

    @FindBy(id = "credentialUrl")
    private List<WebElement> credentialUrl;

    @FindBy(id = "credentialUsername")
    private List<WebElement> credentialUsername;

    @FindBy(id = "credentialPassword")
    private List<WebElement> credentialPassword;

    @FindBy(id = "add-note")
    private WebElement addNote;

    @FindBy(id = "nav-notes-tab")
    private WebElement notesTab;

    @FindBy(id = "noteTitle")
    private List<WebElement> noteTitle;

    @FindBy(id = "noteDescription")
    private List<WebElement> noteDescription;

    @FindBy(id = "edit-btn-note")
    private List<WebElement> editNote;

    @FindBy(id = "delete-btn-note")
    private List<WebElement> deleteNote;

    public HomePage(WebDriver driver) {
        PageFactory.initElements(driver, this);
    }

    public void logout() {
        logout.click();
    }

    public void uploadFile(String filePath) {
        fileUpload.sendKeys(filePath);
        fileUpload.submit();
    }

    public String checkForXFile(int index) {
        return files.get(index).getText();
    }

    public void viewXFile(int index) {
        viewBtnFile.get(index).click();
    }

    public void deleteXFile(int index) {
        deleteFile.get(index).click();
    }

    public void goToAddCredential(WebDriver driver) {
        WebDriverWait wait = new WebDriverWait(driver, 10);
        goToCredentialTab(driver);
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("add-credential")));
        addCredential.click();
    }

    public void goToCredentialTab(WebDriver driver) {
        WebDriverWait wait = new WebDriverWait(driver, 10);
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("nav-credentials-tab")));
        credentialsTab.click();
    }

    public void editXCredential(int index) {
        editBtnCredential.get(index).click();
    }

    public void deleteXCredential(int index) {
        deleteCredential.get(index).click();
    }

    public String checkForXUrl(int index) {
        return credentialUrl.get(index).getText();
    }

    public String checkForXUsername(int index) {
        return credentialUsername.get(index).getText();
    }

    public String checkForXPassword(int index) {
        return credentialPassword.get(index).getText();
    }

    public void goToAddNote(WebDriver driver) {
        WebDriverWait wait = new WebDriverWait(driver, 10);
        goToNoteTab(driver);
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("add-note")));
        addNote.click();
    }

    public void goToNoteTab(WebDriver driver) {
        WebDriverWait wait = new WebDriverWait(driver, 10);
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("nav-notes-tab")));
        notesTab.click();
    }

    public String checkForXNoteTitle(int index) {
        return noteTitle.get(index).getText();
    }

    public String checkForXNoteDescription(int index) {
        return noteDescription.get(index).getText();
    }

    public void editXNote(int index) {
        editNote.get(index).click();
    }

    public void deleteXNote(int index) {
        deleteNote.get(index).click();
    }
}
