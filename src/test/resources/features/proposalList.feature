Feature:
  The participant should see a list of proposals in their home page. They should be ordered by popularity (votes).
  The proposals should be queried from the databased and shown in a list (pagination?).

  Scenario: Proposals are shown
    Given the test database is loaded
    And a user logged in
    When the user visits the homepage
    Then the user should see both proposals

  Scenario: Proposals are ordered correctly
    Given the test database is loaded
    And a user logged in
    When the user visits the homepage
    Then the user should see "proposal2" before "proposal1"