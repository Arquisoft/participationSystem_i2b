Feature:
  The user should be able to see de details of a proposal and a list of comments.
  The comment will be ordered by date (default) or popularity (votes).

  Scenario: the whole information of the proposal is shown
    Given the test database is loaded
    And the user navigates to "localhost:9000"
    And user "prueba01@prueba.es" with password "dgM4BrQu8zHuXSk" is logged in
    When the user navigates into "Proposal 1" details
    Then the user should see "Proposal 1"'s information with author "Author 1" and supported by "5" users

  Scenario: the comments should be shown
    Given the test database is loaded
    And the user navigates to "localhost:9000"
    And user "prueba01@prueba.es" with password "dgM4BrQu8zHuXSk" is logged in
    When the user navigates into "Proposal 1" details
    Then the user should see "Proposal 1 seems quite good"

  Scenario: the comments should be ordered by date by default
    Given the test database is loaded
    And the user navigates to "localhost:9000"
    And user "prueba01@prueba.es" with password "dgM4BrQu8zHuXSk" is logged in
    When the user navigates into "Proposal 2" details
    Then the user should see comment "Shut up" before "Your test comment is shit"

  Scenario: the user should be able to order the comments by popularity
    Given the test database is loaded
    And the user navigates to "localhost:9000"
    And user "prueba01@prueba.es" with password "dgM4BrQu8zHuXSk" is logged in
    When the user navigates into "Proposal 2" details
    And the users orders the comments by popularity
    Then the user should see comment "This is a test comment" before "Shut up"