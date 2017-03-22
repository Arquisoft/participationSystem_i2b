Feature:
  The user should be able to see de details of a proposal and a list of comments.
  The comment will be ordered by date (default) or popularity (votes).

  Scenario: the whole information of the proposal is shown
    Given Selenium driver is loaded
    And the test database is loaded
    And the user navigates to "webpage"
    And user "user1" with password "user1" is logged in
    When the user navigates into "proposal1" details
    Then the user should see "proposal1"'s information

  Scenario: the comments should be shown
    Given Selenium driver is loaded
    And the test database is loaded
    And the user navigates to "webpage"
    And user "user1" with password "user1" is logged in
    When the user navigates into "proposal1" details
    Then the user should see "comment1"

  Scenario: the comments should be ordered by date by default
    Given Selenium driver is loaded
    And the test database is loaded
    And the user navigates to "webpage"
    And user "user1" with password "user1" is logged in
    When the user navigates into "proposal1" details
    Then the user should see "comment2" before "comment1"

  Scenario: the user should be able to order the comments by popularity
    Given Selenium driver is loaded
    And the test database is loaded
    And the user navigates to "webpage"
    And user "user1" with password "user1" is logged in
    When the user navigates into "proposal1" details
    And the users orders the comment by popularity
    Then the user should see "comment1" before "comment2"
