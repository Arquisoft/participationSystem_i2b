import com.esotericsoftware.minlog.Log;
import com.mongodb.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import cucumber.api.PendingException;
import cucumber.api.java.After;
import cucumber.api.java.en.And;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import org.apache.commons.lang3.SystemUtils;
import org.bson.BsonDocument;
import org.bson.Document;
import org.bson.types.ObjectId;
import org.json.JSONArray;
import org.json.JSONObject;
import org.openqa.selenium.By;
import org.openqa.selenium.firefox.FirefoxBinary;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxProfile;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.Date;
import java.time.Instant;
import java.util.logging.Level;

import static junit.framework.TestCase.assertTrue;
import static org.junit.Assert.assertEquals;

/**
 * @author nokutu
 * @since 22/03/2017.
 */
public class CucumberSteps {

    private static MongoClient mongoClient = new MongoClient("localhost", 27017);
    private static MongoDatabase db = mongoClient.getDatabase("aswdb");
    private static MongoCollection<Document> collection = db.getCollection("users");

    private static FirefoxDriver driver;

    public static void main(String args[]) {
        CucumberSteps steps = new CucumberSteps();
        try {
            steps.theTestDatabaseIsLoaded();
        } catch (Throwable throwable) {
            throwable.printStackTrace();
        }
    }

    public static void setUp() {
        FirefoxBinary ffBinary;
        if (SystemUtils.IS_OS_WINDOWS) {
            ffBinary = new FirefoxBinary(new File("FirefoxPortable\\FirefoxPortable.exe"));
        } else {
            ffBinary = new FirefoxBinary();
        }
        FirefoxProfile firefoxProfile = new FirefoxProfile();
        driver = new FirefoxDriver(ffBinary, firefoxProfile);
    }

    public static void tearDown() {
        driver.quit();
    }

    @After
    public void cleanCookies() {
        driver.manage().deleteAllCookies();
        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Given("^the test database is loaded$")
    public void theTestDatabaseIsLoaded() throws Throwable {
        // Write code here that turns the phrase above into concrete actions
        collection.deleteMany(new BsonDocument());
        try {
            BufferedReader br = new BufferedReader(new InputStreamReader(this.getClass().getResource("testDatabase/users.json").openStream()));
            String line;StringBuilder result = new StringBuilder();
            while ((line = br.readLine()) != null) {
                result.append(line);
            }
            JSONArray users = new JSONArray(result.toString());
            users.forEach(userObject -> {
                JSONObject user = (JSONObject) userObject;
                collection.insertOne(new Document()
                        .append("_id", new ObjectId(user.getString("_id")))
                        .append("firstName", user.getString("firstName"))
                        .append("lastName", user.getString("lastName"))
                        .append("email", user.getString("email"))
                        .append("address", user.getString("address"))
                        .append("nationality", user.getString("nationality"))
                        .append("userId", user.getString("userId"))
                        .append("dateOfBirth", Date.from(Instant.parse(user.getString("dateOfBirth"))))
                        .append("password", user.getString("password"))
                );
            });
        } catch (IOException e) {
            Log.error(e.getMessage(), e);
        }
    }

    @And("^the user navigates to \"([^\"]*)\"$")
    public void theUserNavigatesTo(String url) throws Throwable {
        driver.get(url);
    }

    @When("^the user introduces username \"([^\"]*)\" and password \"([^\"]*)\"$")
    public void theUserIntroducesUsernameAndPassword(String username, String password) throws Throwable {
        // Write code here that turns the phrase above into concrete actions
        driver.findElement(By.id("username")).clear();
        driver.findElement(By.id("username")).sendKeys(username);
        driver.findElement(By.id("password")).clear();
        driver.findElement(By.id("password")).sendKeys(password);
        driver.findElement(By.id("loginButton")).click();
    }

    @Then("^the login fails$")
    public void theLoginFails() throws Throwable {
        // Write code here that turns the phrase above into concrete actions
        assertTrue(driver.getCurrentUrl().contains("/login?error"));
    }

    @Then("^the user successfully logs in$")
    public void theUserSuccessfullyLogsIn() throws Throwable {
        assertTrue(driver.getCurrentUrl().contains("/user/home"));
    }

    @And("^user \"([^\"]*)\" with password \"([^\"]*)\" is logged in$")
    public void userWithPasswordIsLoggedIn(String username, String password) throws Throwable {
        theUserIntroducesUsernameAndPassword(username, password);
    }

    @When("^the personnel member clicks in \"([^\"]*)\"'s delete button$")
    public void thePersonnelMemberClicksInSDeleteButton(String proposalTitle) throws Throwable {
        // TODO click in parameter's proposal delete button
        // Write code here that turns the phrase above into concrete actions
        throw new PendingException();
    }

    @Then("^\"([^\"]*)\" is deleted$")
    public void isDeleted(String proposalTitle) throws Throwable {
        // TODO check that the given proposal is neither in the page nor in the database.
        // Write code here that turns the phrase above into concrete actions
        throw new PendingException();
    }

    @Then("^the user doesn't see \"([^\"]*)\"'s delete button$")
    public void theUserDoesnTSeeSDeleteButton(String proposalTitle) throws Throwable {
        // TODO check that there is no delete button in the given proposal
        // Write code here that turns the phrase above into concrete actions
        throw new PendingException();
    }

    @When("^the user clicks on the vote button of \"([^\"]*)\"$")
    public void theUserClicksOnTheVoteButtonOf(String proposalTitle) throws Throwable {
        // TODO click on the given proposal's vote button
        // Write code here that turns the phrase above into concrete actions
        throw new PendingException();
    }
}
