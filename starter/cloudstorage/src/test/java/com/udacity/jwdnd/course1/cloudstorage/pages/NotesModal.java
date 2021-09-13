package com.udacity.jwdnd.course1.cloudstorage.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class NotesModal {
    @FindBy(id = "note-title")
    WebElement title;

    @FindBy(id = "note-description")
    WebElement description;

    @FindBy(id = "note-submit")
    WebElement submitNote;

    public NotesModal(WebDriver driver) {
        PageFactory.initElements(driver, this);
    }

    public void enterNote(String title, String description,WebDriver driver) {
        WebDriverWait wait = new WebDriverWait(driver, 10);
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("note-title")));
        this.title.sendKeys(title);
        this.description.sendKeys(description);
        submitNote.click();
    }

    public void clearNotes(WebDriver driver) {
        WebDriverWait wait = new WebDriverWait(driver, 10);
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("note-title")));
        this.title.clear();
        this.description.clear();
    }
}
