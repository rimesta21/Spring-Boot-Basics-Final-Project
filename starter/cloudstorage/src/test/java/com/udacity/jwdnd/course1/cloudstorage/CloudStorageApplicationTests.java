package com.udacity.jwdnd.course1.cloudstorage;

import com.udacity.jwdnd.course1.cloudstorage.pages.HomePage;
import com.udacity.jwdnd.course1.cloudstorage.pages.LoginPage;
import com.udacity.jwdnd.course1.cloudstorage.pages.ResultsPage;
import com.udacity.jwdnd.course1.cloudstorage.pages.SignupPage;
import io.github.bonigarcia.wdm.WebDriverManager;
import org.junit.jupiter.api.*;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;

import java.io.File;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
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
		if (this.driver != null) {
			driver.quit();
		}
	}

	@Test
	public void getLoginPage() {
		driver.get("http://localhost:" + this.port + "/login");
		Assertions.assertEquals("Login", driver.getTitle());
	}

	@Test
	public void testFromSignUptoLogin() {
		driver.get("http://localhost:" + this.port + "/signup");
		SignupPage signup = new SignupPage(driver);
		signup.fillOutSignup("Rodrigo", "Mesta", "rimesta", "1234");
		signup.goToLogin(driver);
		LoginPage login = new LoginPage(driver);
		login.fillOutLogin("rimesta", "1234");
		Assertions.assertEquals("Home", driver.getTitle());
	}

	@Test
	public void testExistingUsername() {
		driver.get("http://localhost:" + this.port + "/signup");
		SignupPage signup = new SignupPage(driver);
		signup.fillOutSignup("Rodrigo", "Mesta", "rimesta", "1234");
		signup.fillOutSignup("Rodrigo", "Mesta", "rimesta", "1234");
		WebElement errorMsg = driver.findElement(By.id("error-msg"));
		Assertions.assertTrue(errorMsg.isDisplayed());
		Assertions.assertEquals("The username already exists.",errorMsg.getText());

	}

	@Test
	public void testInvalidPassword() {
		driver.get("http://localhost:" + this.port + "/signup");
		SignupPage signup = new SignupPage(driver);
		signup.fillOutSignup("Rodrigo", "Mesta", "rimesta", "1234");
		signup.goToLogin(driver);
		LoginPage login = new LoginPage(driver);
		login.fillOutLogin("rimesta", "1423");
		WebElement errorMsg = driver.findElement(By.id("error-msg"));
		Assertions.assertTrue(errorMsg.isDisplayed());
		Assertions.assertEquals("Invalid username or password",errorMsg.getText());
		Assertions.assertEquals("Login", driver.getTitle());
	}

	@Test
	public void testInvalidUsername() {
		driver.get("http://localhost:" + this.port + "/signup");
		SignupPage signup = new SignupPage(driver);
		signup.fillOutSignup("Rodrigo", "Mesta", "rimesta", "1234");
		signup.goToLogin(driver);
		LoginPage login = new LoginPage(driver);
		login.fillOutLogin("rimetas", "1423");
		WebElement errorMsg = driver.findElement(By.id("error-msg"));
		Assertions.assertTrue(errorMsg.isDisplayed());
		Assertions.assertEquals("Invalid username or password",errorMsg.getText());
		Assertions.assertEquals("Login", driver.getTitle());
	}

	@Test
	public void testUnauthorizedHomeRequest() {
		driver.get("http://localhost:" + this.port + "/home");
		Assertions.assertEquals("Login", driver.getTitle());
	}

	@Test
	public void testFromLoginToSignupToHomeToLogOut() {
		driver.get("http://localhost:" + this.port + "/login");
		LoginPage login = new LoginPage(driver);
		login.goToSignUp();
		SignupPage signup = new SignupPage(driver);
		signup.fillOutSignup("Rodrigo", "Mesta", "rimesta", "1234");
		signup.goToLogin(driver);
		login.fillOutLogin("rimesta", "1234");
		HomePage homepage = new HomePage(driver);
		WebDriverWait wait = new WebDriverWait(driver, 10);
		wait.until(webDriver -> webDriver.findElement(By.id("logout")));
		homepage.logout();
		Assertions.assertEquals("Login", driver.getTitle());
	}

	private void signupAndSignIn() {
		driver.get("http://localhost:" + this.port + "/login");
		LoginPage login = new LoginPage(driver);
		login.goToSignUp();
		SignupPage signup = new SignupPage(driver);
		signup.fillOutSignup("Rodrigo", "Mesta", "rimesta", "1234");
		signup.goToLogin(driver);
		login.fillOutLogin("rimesta", "1234");
	}

	@Test
	public void testUploadFile() {
		signupAndSignIn();
		HomePage homepage = new HomePage(driver);
		homepage.uploadFile(driver, "C://Users/rimes/OneDrive/Desktop/Java Practice/test.txt");
		ResultsPage resultPage = new ResultsPage(driver);
		resultPage.clickContSuccess();
		Assertions.assertEquals("test.txt", homepage.checkForXFile(0));
	}

	@Test
	public void testViewDownloadFile() {
		signupAndSignIn();
		uploadFile();
		HomePage homepage = new HomePage(driver);
		homepage.viewXFile(0);
		ResultsPage resultPage = new ResultsPage(driver);
		resultPage.downloadFile(driver);
		Assertions.assertTrue(isFileDownloaded("C://Users/rimes/Downloads", "In my Mind (3).jpg"));
	}

	private void uploadFile() {
		HomePage homepage = new HomePage(driver);
		homepage.uploadFile(driver, "C://Users/rimes/OneDrive/Pictures/In my Mind (3).jpg");
		ResultsPage resultPage = new ResultsPage(driver);
		resultPage.clickContSuccess();
	}

	private boolean isFileDownloaded(String downloadPath, String fileName) {
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
	public void testDeleteUploadedFile() {
		signupAndSignIn();
		uploadFile();
		HomePage homepage = new HomePage(driver);
		Assertions.assertEquals("In my Mind (3).jpg", homepage.checkForXFile(0));
		homepage.deleteXFile(0);
		ResultsPage resultspage = new ResultsPage(driver);
		resultspage.deleteFile(driver);
		resultspage.clickContSuccess();
		Assertions.assertEquals("ExampleFile.txt", homepage.checkForXFile(0));
	}


}
