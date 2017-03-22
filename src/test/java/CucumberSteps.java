import cucumber.api.PendingException;
import cucumber.api.java.en.And;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;

/**
 * @author nokutu
 * @since 22/03/2017.
 */
public class CucumberSteps {
    @Given("^the test database is loaded$")
    public void theTestDatabaseIsLoaded() throws Throwable {
        // TODO load a test database overwriting the old one
        // Write code here that turns the phrase above into concrete actions
        throw new PendingException();
    }

    @When("^the user introduces username \"([^\"]*)\" and password \"([^\"]*)\"$")
    public void theUserIntroducesUsernameAndPassword(String username, String password) throws Throwable {
        // TODO login
        // Write code here that turns the phrase above into concrete actions
        throw new PendingException();
    }

    @Then("^the login fails$")
    public void theLoginFails() throws Throwable {
        // TODO detect that we are still in the login page
        // Write code here that turns the phrase above into concrete actions
        throw new PendingException();
    }

    @Then("^the user successfully logs in$")
    public void theUserSuccessfullyLogsIn() throws Throwable {
        // TODO detect that we are in the main page
        // Write code here that turns the phrase above into concrete actions
        throw new PendingException();
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

    @Given("^Selenium driver is loaded$")
    public void seleniumDriverIsLoaded() throws Throwable {
        // TODO load selenium
        // Write code here that turns the phrase above into concrete actions
        throw new PendingException();
    }

    @And("^the user navigates to \"([^\"]*)\"$")
    public void theUserNavigatesTo(String url) throws Throwable {
        // TODO go to url
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
