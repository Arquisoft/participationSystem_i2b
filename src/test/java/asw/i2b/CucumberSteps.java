package asw.i2b;

import asw.i2b.consumers.KafkaConsumer;
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
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.Date;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

import static junit.framework.TestCase.assertFalse;
import static junit.framework.TestCase.assertTrue;
import static junit.framework.TestCase.fail;
import static org.junit.Assert.assertEquals;
import org.openqa.selenium.support.ui.*;

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
    private static MongoCollection<Document> invalidWords = db.getCollection("invalidWords");

    @Autowired
    private KafkaConsumer kafkaConsumer;

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
        invalidWords.deleteMany(new BsonDocument());

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
                JSONArray comments = proposal.getJSONArray("comments");
                List<Document> commentsToInsert = new ArrayList<>();

                for(Object commentO :comments){
                    JSONObject comment = (JSONObject) commentO;
                    commentsToInsert.add(new Document()
                            .append("num", comment.getInt("num"))
                            .append("author", comment.getString("author"))
                            .append("created", Date.from(Instant.parse(comment.getString("created"))))
                            .append("votes", comment.getInt("votes"))
                            .append("votedUsernames", comment.getJSONArray("votedUsernames").toList())
                            .append("body", comment.getString("body"))
                    );
                }


                CucumberSteps.proposals.insertOne(new Document()
                        .append("_id", new ObjectId(proposal.getString("_id")))
                        .append("title", proposal.getString("title"))
                        .append("body", proposal.getString("body"))
                        .append("votes", proposal.getInt("votes"))
                        .append("votedUsernames", proposal.getJSONArray("votedUsernames").toList())
                        .append("comments", commentsToInsert)
                        .append("created", Date.from(Instant.parse(proposal.getString("created"))))
                        .append("minimalSupport", proposal.getInt("minimalSupport"))
                        .append("category", proposal.getString("category"))
                        .append("author", proposal.getString("author"))
                        .append("invalidWords", proposal.getJSONArray("invalidWords").toList())
                );
            });

            JSONArray categories = parseArray("testDatabase/categories.json");
            categories.forEach(categoryObject -> {
                JSONObject category = (JSONObject) categoryObject;
                CucumberSteps.categories.insertOne(new Document()
                        .append("_id", new ObjectId(category.getString("_id")))
                        .append("name", category.getString("name"))
                        .append("minimalSupport", category.getInt("minimalSupport"))
                );
            });

            JSONArray invalidWords = parseArray("testDatabase/invalidWords.json");
            invalidWords.forEach(
                    invalidWordObject -> {
                        JSONObject invalidWord = (JSONObject) invalidWordObject;
                        CucumberSteps.invalidWords.insertOne(
                                new Document()
                                    .append("word", invalidWord.getString("word"))
                        );

                    }
            );

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
                "//div[@id='proposalList']/div[div[@class='panel-heading']/a/text()='" + proposalTitle + "']//div[@id='form-vote']//button"
        ).click();
    }

    @Then("^the user should see \"([^\"]*)\" and \"([^\"]*)\"$")
    public void theUserShouldSeeAnd(String text1, String text2) throws Throwable {
        assertTrue(driver.findElementsByXPath("//*[contains(text(), '" + text1 + "')]").size() > 0);
        assertTrue(driver.findElementsByXPath("//*[contains(text(), '" + text2 + "')]").size() > 0);
    }

    @Then("^the user should see \"([^\"]*)\"$")
    public void theUserShouldSee(String text1) {
        WebDriverWait wait = new WebDriverWait(driver, 5);
        wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//*[contains(text(), '" + text1 + "')]")));
        assertTrue(driver.findElementsByXPath("//*[contains(text(), '" + text1 + "')]").size() > 0);
    }

    @Then("^the user should see proposal \"([^\"]*)\"$")
    public void theUserShouldSeeProposal(String proposal) throws Throwable {
        assertTrue(driver.findElementsByXPath("//*[contains(text(), '" + proposal + "')]").size() > 0);
    }

    @Then("^the user should see proposal \"([^\"]*)\" before \"([^\"]*)\"$")
    public void theUserShouldSeeProposalBefore(String text1, String text2) throws Throwable {
        List<WebElement> list = driver.findElementsByXPath("//div[@id='proposalList']/div/div[@class='panel-heading']/a");
        if(!isBefore(list, text1, text2))
            fail();
    }

    private boolean isBefore(List<WebElement> list, String text1, String text2){
        boolean foundFirst = false;
        for (WebElement we : list) {
            if (we.getText().contains(text1)) {
                foundFirst = true;
            } else if (we.getText().contains(text2)) {
                return foundFirst;
            }
        }
        return false;
    }

    @Then("^the user should see comment \"([^\"]*)\" before \"([^\"]*)\"$")
    public void theUserShouldSeeCommentBefore(String text1, String text2) throws Throwable {
        WebDriverWait wait = new WebDriverWait(driver, 5);
        wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//div[@id='comments-panel']/div[@class='panel-body']//div[@class='panel-body']/span")));
        List<WebElement> list = driver.findElementsByXPath("//div[@id='comments-panel']/div[@class='panel-body']//div[@class='panel-body']/span");
        if(!isBefore(list, text1, text2))
            fail();
    }

    @Then("^the user returns to login screen$")
    public void theUserReturnsToLoginScreen() throws Throwable {
        assertTrue(driver.getCurrentUrl().contains("localhost:9000/login?logout"));
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

    @Then("^the amount of votes for proposal \"([^\"]*)\" with initial value \"([^\"]*)\" is not increased$")
    public void theAmountOfVotesForProposalWithInitialValueIsNotIncreased(String proposalTitle, String initialVotesString) throws Throwable {
        int initialVotes = Integer.parseInt(initialVotesString);
        int votes = Integer.parseInt(
                driver.findElementByXPath(
                        "//div[@id='proposalList']/div[div[@class='panel-heading']/a/text()='" + proposalTitle + "']//div[contains(@id, 'votes-')]"
                ).getText()
        );
        assertEquals(initialVotes, votes);
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
        WebDriverWait wait = new WebDriverWait(driver, 5);
        WebElement modal = wait.until(ExpectedConditions.visibilityOf(driver.findElementById("createProposal")));

        Select select = new Select(modal.findElement(By.id("sel1")));
        select.selectByVisibleText(category);

        modal.findElement(By.id("title")).sendKeys(title);
        modal.findElement(By.id("body")).sendKeys(explanation);
        modal.findElement(By.id("sendCreateProposal")).click();
    }

    @When("^the user clicks on the comment's vote button with title \"([^\"]*)\"$")
    public void theUserClicksOnTheCommentVoteButton(String commentTitle) throws InterruptedException {
        WebDriverWait wait = new WebDriverWait(driver, 5);
        wait.until(ExpectedConditions.presenceOfElementLocated(
        By.xpath(
                ".//*[contains(text(),\"" + commentTitle.trim() + "\")]/../..//div[@id='form-vote']/form//button"
        ))).click();
    }

    @Then("^the amount of votes for comment \"([^\"]*)\" with initial value \"([^\"]*)\" is increased and \"([^\"]*)\" is added to the votes list$")
    public void theAmountOfVotesForCommentWithInitialValueIsIncreasedAndIsAddedToTheVotesList(String commentTitle, String initialVotesString, String user) throws Throwable {

        int initialVotes = Integer.parseInt(initialVotesString);
        int votes = Integer.parseInt(
                driver.findElementByXPath(
                        ".//*[contains(text(),\""+commentTitle.trim()+"\")]/../../div[@class=\"panel-heading\"]/div[@class=\"pull-right\"]"
                ).getText()
        );
        assertEquals(initialVotes + 1, votes);
    }

    @Then("^the amount of votes for comment \"([^\"]*)\" with initial value \"([^\"]*)\" is not increased$")
    public void theAmountOfVotesForCommentWithInitialValueIsNotIncreased(String commentTitle, String initialVotesString) throws Throwable {
        int initialVotes = Integer.parseInt(initialVotesString);
        int votes = Integer.parseInt(
                driver.findElementByXPath(
                        ".//*[contains(text(),\""+commentTitle.trim()+"\")]/../../div[@class=\"panel-heading\"]/div[@class=\"pull-right\"]"
                ).getText()
        );
        assertEquals(initialVotes, votes);
    }

    @Then("^the button to vote comment \"([^\"]*)\" is not present$")
    public void testButtonNotPresentForComment(String commentContent) throws Throwable {
        WebDriverWait wait = new WebDriverWait(driver, 5);
        String button = wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(
                ".//*[contains(text(),\""+commentContent+"\")]/../..//div[@id='form-vote']/form//button")))
                .getText();
        assertEquals("Unvote", button);
    }


    @And("^the user navigates into \"([^\"]*)\" details$")
    public void theUserNavigatesIntoDetails(String link) throws Throwable {
        WebDriverWait wait = new WebDriverWait(driver, 5);
        wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(
                ".//a[contains(text(),'"+ link.trim() +"')]"
        ))).click();
    }

    @When("^the user clicks on the create comment button$")
    public void theUserClicksOnTheCreateCommentButton() throws Throwable {
        WebDriverWait wait = new WebDriverWait(driver, 5);
        wait.until(ExpectedConditions.presenceOfElementLocated(By.id("openCreateProposalDialog"))).click();
    }

    @And("^the user fills and sends the comment with body \"([^\"]*)\"$")
    public void theUserFillsAndSendsTheCommentWithBody(String body) throws Throwable {
        WebDriverWait wait = new WebDriverWait(driver, 5);
        WebElement input = wait.until(ExpectedConditions.visibilityOf(driver.findElementById("body")));
        input.sendKeys(body);
        driver.findElement(By.xpath(".//*[@id='createComment']//div[@class=\"modal-footer\"]//button[1]")).click();
    }

    @Then("^a comment should appear at the bottom of the comment list with body \"([^\"]*)\"$")
    public void aCommentShouldAppearOnTheCommentListWithBody(String body) throws Throwable {
        String bodyInDoc = (
                driver.findElementByXPath(
                        ".//*[@class='comment'][last()]//div[@class=\"panel-body\"]"
                ).getText()
        );
        assertEquals(body, bodyInDoc);
    }

    @Then("^the user should see \"([^\"]*)\"'s information with author \"([^\"]*)\" and supported by \"([^\"]*)\" users$")
    public void theUserShouldSeeInformationWithAuthorAndSupportedBy(String proposal, String author, String supportedBy) {
        WebDriverWait wait = new WebDriverWait(driver, 5);
        wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(".//*[contains(text(), '" + proposal + "')]")));
        assertTrue(driver.findElementsByXPath(".//*[contains(text(), '" + proposal + "')]").size() > 0);
        assertTrue(driver.findElementsByXPath(".//*[contains(text(), 'Created by: " + author + "')]").size() > 0);
        assertTrue(driver.findElementsByXPath(".//*[contains(text(), 'Supported by: " + supportedBy + "')]").size() > 0);
    }

    @And("^the users orders the comments by popularity$")
    public void theUserOrdersTheCommentsByPopularity(){
        WebDriverWait wait = new WebDriverWait(driver, 5);
        wait.until(ExpectedConditions.presenceOfElementLocated(By.id("order-by-popularity"))).click();
    }

    @And("^a kafka createProposal event is generated for \"([^\"]*)\"$")
    public void aKafkaCreateProposalEventIsGenerated(String proposal) throws InterruptedException {
        assertTrue(kafkaConsumer.getCreateProposal().stream().anyMatch(elem -> elem.contains(proposal)));
    }

    @And("^a kafka createComment event is generated for \"([^\"]*)\"$")
    public void aKafkaCreateCommentEventIsGenerated(String comment) throws InterruptedException {
        assertTrue(kafkaConsumer.getCreateComment().stream().anyMatch(elem -> elem.contains(comment)));
    }

    @And("^the user navigates into admin settings$")
    public void theUserNavigatesIntoAdminSettings(){
        WebDriverWait wait = new WebDriverWait(driver, 5);
        wait.until(ExpectedConditions.presenceOfElementLocated(By.id("btn-settings"))).click();
    }

    @When("^the personnel member adds a new category \"([^\"]*)\" with minimal support of \"([^\"]*)\"$")
    public void thePersonnelMemberAddsANewCategoryWithMinimalSupportOf(String category, String minimalSupport){
        WebDriverWait wait = new WebDriverWait(driver, 5);
        WebElement form = wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//div[@id='manageCategories']/div[@class='panel-footer']/form")));
        form.findElement(By.xpath("./input[@type='text']")).sendKeys(category);
        form.findElement(By.xpath("./input[@type='number']")).sendKeys(minimalSupport);
        form.findElement(By.xpath(".//button")).click();
    }

    @Then("^the category \"([^\"]*)\" is added with \"([^\"]*)\" minimal support$")
    public void theCategoryIsAdded(String category, String minimalSupport){
        assertTrue(driver.findElements(By.xpath("//div[@id='manageCategories']//table//*[contains(text(), '" + category + "')]")).size() > 0);
        assertTrue(driver.findElements(By.xpath("//div[@id='manageCategories']//table//*[contains(text(), '" + minimalSupport + "')]")).size() > 0);
    }

    @When("^the personnel member deletes category \"([^\"]*)\"$")
    public void thePersonnelMemberDeletesCategory(String category){
        WebDriverWait wait = new WebDriverWait(driver, 5);
        wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//div[@id='manageCategories']//table/tbody//*[contains(text(), '"
                + category + "')]/..//form/a"))).click();
    }

    @Then("^the category \"([^\"]*)\" doesn't exist$")
    public void theCategoryDoesntExist(String category){
        assertFalse(driver.findElements(By.xpath("//div[@id='manageCategories']//table//*[contains(text(), '" + category + "')]")).size() > 0);
    }



}
