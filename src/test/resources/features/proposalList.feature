Feature:
  The participant should see a list of proposals in their home page. They should be ordered by popularity (votes).
  The proposals should be queried from the databased and shown in a list (pagination?).

  Scenario: Proposals are shown
    Given Selenium driver is loaded
    And the test database is loaded
    And the user navigates to "webpage"
    And user "user1" with password "user1" is logged in
    When the user visits the homepage
    Then the user should see "proposal2" and "proposal1"

  Scenario: Proposals are ordered correctly
    Given Selenium driver is loaded
    And the test database is loaded
    And the user navigates to "webpage"
    And user "user1" with password "user1" is logged in
    When the user visits the homepage
    Then the user should see "proposal2" before "proposal1"