package com.udacity.jwdnd.course1.cloudstorage;

import com.udacity.jwdnd.course1.cloudstorage.pages.*;
import io.github.bonigarcia.wdm.WebDriverManager;
import org.junit.jupiter.api.*;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;

import java.io.File;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class CloudStorageApplicationTests {

	@LocalServerPort
	private int port;

	private WebDriver driver;

	@BeforeAll
	static void beforeAll() {
		WebDriverManager.chromedriver().setup();
	}

	@BeforeEach
	public void beforeEach() {
		this.driver = new ChromeDriver();
	}

	@AfterEach
	public void afterEach() {
		//TODO: delete the user created
		driver.get("http://localhost:" + this.port + "/home/deleteUser");
		if (this.driver != null) {
			driver.quit();
		}
	}

	@Test
	@Order(1)
	public void testFromSignUptoLogin() {
		driver.get("http://localhost:" + this.port + "/signup");
		SignupPage signup = new SignupPage(driver);
		signup.fillOutSignup("Rodrigo", "Mesta", "rimesta", "1234");;
		LoginPage login = new LoginPage(driver);
		login.fillOutLogin("rimesta", "1234");
		Assertions.assertEquals("Home", driver.getTitle());
	}

	@Test
	@Order(2)
	public void testExistingUsername() {
		driver.get("http://localhost:" + this.port + "/signup");
		SignupPage signup = new SignupPage(driver);
		signup.fillOutSignup("Rodrigo", "Mesta", "rimesta", "1234");
		WebElement errorMsg = driver.findElement(By.id("error-msg"));
		Assertions.assertTrue(errorMsg.isDisplayed());
		Assertions.assertEquals("The username already exists.", errorMsg.getText());
	}

	@Test
	@Order(3)
	public void testInvalidPassword() {
		driver.get("http://localhost:" + this.port + "/home");
		LoginPage login = new LoginPage(driver);
		login.fillOutLogin("rimesta", "1423");
		WebElement errorMsg = driver.findElement(By.id("error-msg"));
		Assertions.assertTrue(errorMsg.isDisplayed());
		Assertions.assertEquals("Invalid username or password",errorMsg.getText());
		Assertions.assertEquals("Login", driver.getTitle());
	}

	@Test
	@Order(4)
	public void testInvalidUsername() {
		driver.get("http://localhost:" + this.port + "/home");
		LoginPage login = new LoginPage(driver);
		login.fillOutLogin("rimetas", "1234");
		WebElement errorMsg = driver.findElement(By.id("error-msg"));
		Assertions.assertTrue(errorMsg.isDisplayed());
		Assertions.assertEquals("Invalid username or password",errorMsg.getText());
		Assertions.assertEquals("Login", driver.getTitle());
	}

	@Test
	@Order(5)
	public void testUnauthorizedHomeRequest() {
		driver.get("http://localhost:" + this.port + "/home");
		Assertions.assertEquals("Login", driver.getTitle());
	}

	@Test
	@Order(6)
	public void testFromLoginToHomeToLogOut() {
		driver.get("http://localhost:" + this.port + "/login");
		LoginPage login = new LoginPage(driver);
		login.fillOutLogin("rimesta", "1234");
		HomePage homepage = new HomePage(driver);
		WebDriverWait wait = new WebDriverWait(driver, 10);
		wait.until(webDriver -> webDriver.findElement(By.id("logout")));
		homepage.logout();
		Assertions.assertEquals("Login", driver.getTitle());
	}

	private void signIn() {
		driver.get("http://localhost:" + this.port + "/login");
		LoginPage login = new LoginPage(driver);
		login.fillOutLogin("rimesta", "1234");
	}

	@Test
	@Order(7)
	public void testUploadFile() {
		signIn();
		HomePage homepage = new HomePage(driver);
		homepage.uploadFile("C://Users/rimes/OneDrive/Pictures/In my Mind (3).jpg");
		ResultsPage resultPage = new ResultsPage(driver);
		resultPage.clickContSuccess();
		Assertions.assertEquals("In my Mind (3).jpg", homepage.checkForXFile(0));
	}

	@Test
	@Order(8)
	public void testViewDownloadFile() throws InterruptedException {
		signIn();
		HomePage homepage = new HomePage(driver);
		homepage.viewXFile(0);
		driver.switchTo().activeElement();
		ViewDeleteModal viewDeleteModal = new ViewDeleteModal(driver);
		viewDeleteModal.downloadFile(driver);
		Assertions.assertTrue(isFileDownloaded("C://Users/rimes/Downloads", "In my Mind (3).jpg"));
	}

	private boolean isFileDownloaded(String downloadPath, String fileName) throws InterruptedException {
		Thread.sleep(5000);
		File dir = new File(downloadPath);
		File[] dirContents = dir.listFiles();

		for (File dirContent : dirContents) {
			if (dirContent.getName().equals(fileName)) {
				// File has been found, it can now be deleted:
				dirContent.delete();
				return true;
			}
		}
		return false;
	}

	@Test
	@Order(9)
	public void testDeleteUploadedFile() {
		signIn();
		HomePage homepage = new HomePage(driver);
		Assertions.assertEquals("In my Mind (3).jpg", homepage.checkForXFile(0));
		homepage.deleteXFile(0);
		driver.switchTo().activeElement();
		ViewDeleteModal viewDeleteModal = new ViewDeleteModal(driver);
		viewDeleteModal.delete(driver);
		ResultsPage resultspage = new ResultsPage(driver);
		WebDriverWait wait = new WebDriverWait(driver, 10);
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("linkSuccess")));
		resultspage.clickContSuccess();
		Assertions.assertEquals("Example File Name.jpg", homepage.checkForXFile(0));
	}

	@Test
	@Order(10)
	public void testUploadCredential() {
		signIn();
		HomePage homepage = new HomePage(driver);
		homepage.goToAddCredential(driver);
		driver.switchTo().activeElement();
		CredentialModal credentialModal = new CredentialModal(driver);
		credentialModal.enterCredentials("google.com", "rimesta", "1234", driver);
		ResultsPage resultPage = new ResultsPage(driver);
		WebDriverWait wait = new WebDriverWait(driver, 10);
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("linkSuccess")));
		resultPage.clickContSuccess();
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("credentialUrl")));
		Assertions.assertEquals("google.com", homepage.checkForXUrl(0));
		Assertions.assertEquals("rimesta", homepage.checkForXUsername(0));
		Assertions.assertNotEquals("1234", homepage.checkForXPassword(0));
	}

	@Test
	@Order(11)
	public void testEditCredential() {
		signIn();
		HomePage homepage = new HomePage(driver);
		homepage.goToCredentialTab(driver);
		WebDriverWait wait = new WebDriverWait(driver, 10);
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("edit-btn-credential")));
		homepage.editXCredential(0);
		driver.switchTo().activeElement();
		CredentialModal credentialModal = new CredentialModal(driver);
		//checks that the original password was saved (wasn't checked in the test before its checked here)
		Assertions.assertEquals("1234", credentialModal.returnOriginalPassword(driver));
		credentialModal.clearCredentials(driver);
		credentialModal.enterCredentials("google1.com", "rimesta11", "4321", driver);
		ResultsPage resultPage = new ResultsPage(driver);
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("linkSuccess")));
		resultPage.clickContSuccess();
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("credentialUrl")));
		Assertions.assertEquals("google1.com", homepage.checkForXUrl(0));
		Assertions.assertEquals("rimesta11", homepage.checkForXUsername(0));
		homepage.editXCredential(0);
		driver.switchTo().activeElement();
		Assertions.assertEquals("4321", credentialModal.returnOriginalPassword(driver));
	}


	@Test
	@Order(12)
	public void deleteUploadedCredential() {
		signIn();
		HomePage homepage = new HomePage(driver);
		homepage.goToCredentialTab(driver);
		WebDriverWait wait = new WebDriverWait(driver, 10);
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("delete-btn-credential")));
		homepage.deleteXCredential(0);
		driver.switchTo().activeElement();
		//The only way this test works is if I submit the btn directly. Though the click does work when tested in person.
		WebElement btn = driver.findElement(By.id("delete-btn-modal"));
		btn.submit();
		ResultsPage resultPage = new ResultsPage(driver);
		resultPage.clickContSuccess();
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("credentialUrl")));
		Assertions.assertEquals("ExampleUrl.com", homepage.checkForXUrl(0));
	}


	@Test
	@Order(13)
	public void testUploadNote() {
		signIn();
		HomePage homepage = new HomePage(driver);
		homepage.goToAddNote(driver);
		driver.switchTo().activeElement();
		NotesModal notesModal = new NotesModal(driver);
		notesModal.enterNote("Test", "Hey hows it going?", driver);
		ResultsPage resultPage = new ResultsPage(driver);
		resultPage.clickContSuccess();
		WebDriverWait wait = new WebDriverWait(driver, 10);
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("noteTitle")));
		Assertions.assertEquals("Test", homepage.checkForXNoteTitle(0));
		Assertions.assertEquals("Hey hows it going?", homepage.checkForXNoteDescription(0));
	}

	@Test
	@Order(14)
	public void testEditNote() {
		signIn();
		HomePage homepage = new HomePage(driver);
		homepage.goToNoteTab(driver);
		WebDriverWait wait = new WebDriverWait(driver, 10);
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("edit-btn-note")));
		homepage.editXNote(0);
		driver.switchTo().activeElement();
		NotesModal notesModal = new NotesModal(driver);
		notesModal.clearNotes(driver);
		notesModal.enterNote("Test6", "This is a test.",  driver);
		ResultsPage resultPage = new ResultsPage(driver);
		resultPage.clickContSuccess();
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("noteTitle")));
		Assertions.assertEquals("Test6", homepage.checkForXNoteTitle(0));
		Assertions.assertEquals("This is a test.", homepage.checkForXNoteDescription(0));
	}

	@Test
	@Order(15)
	public void deleteUploadedNote() {
		signIn();
		HomePage homepage = new HomePage(driver);
		homepage.goToNoteTab(driver);
		WebDriverWait wait = new WebDriverWait(driver, 10);
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("delete-btn-note")));
		homepage.deleteXNote(0);
		driver.switchTo().activeElement();
		//The only way this test works is if I submit the btn directly. Though the click does work when tested in person.
		WebElement bttn = driver.findElement(By.id("delete-btn-modal"));
		bttn.submit();
		ResultsPage resultPage = new ResultsPage(driver);
		resultPage.clickContSuccess();
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("noteTitle")));
		Assertions.assertEquals("Example Note Title", homepage.checkForXNoteTitle(0));
	}

}
