package asw.i2b;

import com.esotericsoftware.minlog.Log;
import com.mongodb.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
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
import org.junit.runner.RunWith;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxBinary;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxProfile;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.Date;
import java.time.Instant;
import java.util.List;

import static junit.framework.TestCase.assertTrue;
import static junit.framework.TestCase.fail;
import static org.junit.Assert.assertEquals;
import org.openqa.selenium.support.ui.Select;

/**
 * @author nokutu
 * @since 22/03/2017.
 */
@RunWith(SpringRunner.class)
@ContextConfiguration(classes = Application.class)
@SpringBootTest(classes = Application.class, webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
public class CucumberSteps {

    private static MongoClient mongoClient = new MongoClient("localhost", 27017);
    private static MongoDatabase db = mongoClient.getDatabase("aswdb");
    private static MongoCollection<Document> users = db.getCollection("users");
    private static MongoCollection<Document> proposals = db.getCollection("proposals");
    private static MongoCollection<Document> categories = db.getCollection("categories");

    private static FirefoxDriver driver;

    public static void setUp() {
        FirefoxBinary ffBinary;
        if (SystemUtils.IS_OS_WINDOWS) {
            ffBinary = new FirefoxBinary(new File("FirefoxWindows\\FirefoxPortable.exe"));
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
        users.deleteMany(new BsonDocument());
        proposals.deleteMany(new BsonDocument());
        categories.deleteMany(new BsonDocument());

        try {
            JSONArray users = parseArray("testDatabase/users.json");
            users.forEach(userObject -> {
                JSONObject user = (JSONObject) userObject;
                CucumberSteps.users.insertOne(new Document()
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

            JSONArray proposals = parseArray("testDatabase/proposals.json");
            proposals.forEach(proposalObject -> {
                JSONObject proposal = (JSONObject) proposalObject;
                CucumberSteps.proposals.insertOne(new Document()
                        .append("_id", new ObjectId(proposal.getString("_id")))
                        .append("title", proposal.getString("title"))
                        .append("body", proposal.getString("body"))
                        .append("votes", proposal.getInt("votes"))
                        .append("votedUsernames", proposal.getJSONArray("votedUsernames").toList())
                );
            });

            JSONArray categories = parseArray("testDatabase/categories.json");
            categories.forEach(categoryObject -> {
                JSONObject category = (JSONObject) categoryObject;
                CucumberSteps.categories.insertOne(new Document()
                        .append("_id", new ObjectId(category.getString("_id")))
                        .append("name", category.getString("name"))
                );
            });
        } catch (IOException e) {
            Log.error(e.getMessage(), e);
        }
    }

    private JSONArray parseArray(String name) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(this.getClass().getClassLoader().getResourceAsStream(name)));
        String line;
        StringBuilder result = new StringBuilder();
        while ((line = br.readLine()) != null) {
            result.append(line);
        }
        br.close();
        return new JSONArray(result.toString());
    }

    @And("^the user navigates to \"([^\"]*)\"$")
    public void theUserNavigatesTo(String url) throws Throwable {
        driver.get(url);
    }

    @When("^the user introduces username \"([^\"]*)\" and password \"([^\"]*)\"$")
    public void theUserIntroducesUsernameAndPassword(String username, String password) throws Throwable {
        driver.findElement(By.id("username")).clear();
        driver.findElement(By.id("username")).sendKeys(username);
        driver.findElement(By.id("password")).clear();
        driver.findElement(By.id("password")).sendKeys(password);
        driver.findElement(By.id("loginButton")).click();
    }

    @Then("^the login fails$")
    public void theLoginFails() throws Throwable {
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
        driver.findElementByXPath(
                "//div[@id='proposalList']/div[div[@class='panel-heading']/a/text()='" + proposalTitle + "']//button[text()='Delete']"
        ).click();
    }

    @Then("^the user doesn't see \"([^\"]*)\"'s delete button$")
    public void theUserDoesnTSeeSDeleteButton(String proposalTitle) throws Throwable {
        List<WebElement> elements = driver.findElementsByXPath(
                "//div[@id='proposalList']/div[div[@class='panel-heading']/a/text()='" + proposalTitle + "']//button[text()='Delete']"
        );
        assertEquals(0, elements.size());
    }

    @When("^the user clicks on the vote button of \"([^\"]*)\"$")
    public void theUserClicksOnTheVoteButtonOf(String proposalTitle) throws Throwable {
        driver.findElementByXPath(
                "//div[@id='proposalList']/div[div[@class='panel-heading']/a/text()='" + proposalTitle + "']//button[text()='Vote']"
        ).click();
    }

    @Then("^the user should see \"([^\"]*)\" and \"([^\"]*)\"$")
    public void theUserShouldSeeAnd(String text1, String text2) throws Throwable {
        assertTrue(driver.findElementsByXPath("//*[contains(text(), '" + text1 + "')]").size() > 0);
        assertTrue(driver.findElementsByXPath("//*[contains(text(), '" + text2 + "')]").size() > 0);
    }

    @Then("^the user should see proposal \"([^\"]*)\"$")
    public void theUserShouldSeeProposal(String proposal) throws Throwable {
        assertTrue(driver.findElementsByXPath("//*[contains(text(), '" + proposal + "')]").size() > 0);
    }

    @Then("^the user should see proposal \"([^\"]*)\" before \"([^\"]*)\"$")
    public void theUserShouldSeeBefore(String text1, String text2) throws Throwable {
        boolean foundFirst = false;
        for (WebElement we : driver.findElementsByXPath("//div[@id='proposalList']/div/div[@class='panel-heading']/a")) {
            if (we.getText().contains(text1)) {
                foundFirst = true;
            } else if (we.getText().contains(text2)) {
                if (foundFirst) {
                    return;
                } else {
                    fail();
                }
            }
        }
        fail();
    }

    @Then("^the user returns to login screen$")
    public void theUserReturnsToLoginScreen() throws Throwable {
        assertTrue(driver.getCurrentUrl().contains("localhost:8090/login?logout"));
    }

    @When("^the users clicks on the logout button$")
    public void theUsersClicksOnTheLogoutButton() throws Throwable {
        driver.findElementById("logout").click();
    }

    @Then("^the amount of votes for proposal \"([^\"]*)\" with initial value \"([^\"]*)\" is increased and \"([^\"]*)\" is added to the votes list$")
    public void theAmountOfVotesForProposalWithInitialValueIsIncreasedAndIsAddedToTheVotesList(String proposalTitle, String initialVotesString, String user) throws Throwable {
        int initialVotes = Integer.parseInt(initialVotesString);
        int votes = Integer.parseInt(
                driver.findElementByXPath(
                        "//div[@id='proposalList']/div[div[@class='panel-heading']/a/text()='" + proposalTitle + "']//div[contains(@id, 'votes-')]"
                ).getText()
        );
        assertEquals(initialVotes + 1, votes);
    }

    @Then("^\"([^\"]*)\" vote button is not visible$")
    public void voteButtonIsNotVisible(String proposalTitle) throws Throwable {
        List<WebElement> elements = driver.findElementsByXPath(
                "//div[@id='proposalList']/div[div[@class='panel-heading']/a/text()='" + proposalTitle + "']//button[text()='Vote']"
        );
        assertEquals(0, elements.size());
    }

    @Then("^the users doesn't see \"([^\"]*)\"$")
    public void theUsersDoesnTSee(String proposalTitle) throws Throwable {
        List<WebElement> elements = driver.findElementsByXPath(
                "//div[@id='proposalList']/div[div[@class='panel-heading']/a/text()='" + proposalTitle + "']"
        );
        assertEquals(0, elements.size());
    }

    @When("^the user clicks on the create proposal button$")
    public void theUserClicksOnTheCreateProposalButton() throws Throwable {
        driver.findElementById("openCreateProposalDialog").click();
    }



    @And("^the user fills and sends proposal creation form with category \"([^\"]*)\" title \"([^\"]*)\" and explanation \"([^\"]*)\"$")
    public void theUserFillsAndSendsProposalCreationFormWithCategoryTitleAndExplanation(String category, String title, String explanation) throws Throwable {
        WebElement modal = driver.findElementById("createProposal");
        Select select = new Select(modal.findElement(By.id("sel1")));
        select.selectByVisibleText(category);

        modal.findElement(By.id("title")).sendKeys(title);
        modal.findElement(By.id("body")).sendKeys(explanation);
        modal.findElement(By.id("sendCreateProposal")).click();
    }
}
