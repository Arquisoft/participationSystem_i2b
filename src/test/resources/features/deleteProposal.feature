Feature:
  The personnel from the council should be able to delete proposals

  Scenario: the personnel member deletes a proposal
    Given the test database is loaded
    And the user navigates to "localhost:8090"
    And user "personnel1" with password "personnel1" is logged in
    When the personnel member clicks in "proposal1"'s delete button
    Then "proposal1" is deleted

  Scenario: participants don't see the delete button
    Given the test database is loaded
    And the user navigates to "localhost:8090"
    And user "user1" with password "user1" is logged in
    Then the user doesn't see "proposal1"'s delete button