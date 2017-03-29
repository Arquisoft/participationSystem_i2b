package asw.i2b; /**
 * @author nokutu
 * @since 22/03/2017.
 */

import cucumber.api.CucumberOptions;
import cucumber.api.junit.Cucumber;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.runner.RunWith;

@RunWith(Cucumber.class)
@CucumberOptions(features = {"src/test/resources"})
public class CucumberTest {

    @BeforeClass
    public static void setUp() {
        CucumberSteps.setUp();
    }

    @AfterClass
    public static void tearDown() {
        CucumberSteps.tearDown();
    }
}