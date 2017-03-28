Feature:
  The participant should see a list of proposals in their home page. They should be ordered by popularity (votes).
  The proposals should be queried from the databased and shown in a list (pagination?).

  Scenario: Proposals are shown
    Given the test database is loaded
    And the user navigates to "localhost:8090"
    And user "user1" with password "user1" is logged in
    When the user visits the homepage
    Then the user should see "proposal2" and "proposal1"

  Scenario: Proposals are ordered correctly
    Given the test database is loaded
    And the user navigates to "localhost:8090"
    And user "user1" with password "user1" is logged in
    When the user visits the homepage
    Then the user should see "proposal2" before "proposal1"