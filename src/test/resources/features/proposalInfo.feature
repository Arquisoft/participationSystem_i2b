Feature:
  The user should be able to see de details of a proposal and a list of comments.
  The comment will be ordered by date (default) or popularity (votes).

  Scenario: the whole information of the proposal is shown
    Given the test database is loaded
    And the user navigates to "localhost:8090"
    And user "user1" with password "user1" is logged in
    When the user navigates into "Proposal 1" details n
    Then the user should see "Proposal 1"'s information

  Scenario: the comments should be shown
    Given the test database is loaded
    And the user navigates to "localhost:8090"
    And user "user1" with password "user1" is logged in
    When the user navigates into "Proposal 1" details n
    Then the user should see "comment1"

  Scenario: the comments should be ordered by date by default
    Given the test database is loaded
    And the user navigates to "localhost:8090"
    And user "user1" with password "user1" is logged in
    When the user navigates into "Proposal 1" details n
    Then the user should see "comment2" before "comment1"

  Scenario: the user should be able to order the comments by popularity
    Given the test database is loaded
    And the user navigates to "localhost:8090"
    And user "user1" with password "user1" is logged in
    When the user navigates into "Proposal 1" details n
    And the users orders the comment by popularity
    Then the user should see "comment1" before "comment2"
