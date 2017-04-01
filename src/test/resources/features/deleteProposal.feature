Feature:
  The personnel from the council should be able to delete proposals

  Scenario: the personnel member deletes a proposal
    Given the test database is loaded
    And the user navigates to "localhost:8090"
    And user "admin@admin.com" with password "admin" is logged in
    When the personnel member clicks in "Proposal 1"'s delete button
    Then the users doesn't see "Proposal 1"

  Scenario: participants don't see the delete button
    Given the test database is loaded
    And the user navigates to "localhost:8090"
    And user "prueba01@prueba.es" with password "dgM4BrQu8zHuXSk" is logged in
    Then the user doesn't see "Proposal 1"'s delete button