package com.udacity.jwdnd.course1.cloudstorage;

import com.udacity.jwdnd.course1.cloudstorage.pages.HomePage;
import com.udacity.jwdnd.course1.cloudstorage.pages.LoginPage;
import com.udacity.jwdnd.course1.cloudstorage.pages.SignupPage;
import io.github.bonigarcia.wdm.WebDriverManager;
import org.junit.jupiter.api.*;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;

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
		login.fillOutLogin("rimesta", "1423");
		HomePage homepage = new HomePage(driver);
		homepage.logout();
		Assertions.assertEquals("Login", driver.getTitle());
	}


}
