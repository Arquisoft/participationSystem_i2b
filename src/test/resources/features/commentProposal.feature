Feature:
  The participants should be able to comment proposals.
  Each participant can create as many comments as wanted.

  Scenario:
    Given Selenium driver is loaded
    And the test database is loaded
    And the user navigates to "webpage"
    And user "user1" with password "user1" is logged in
    And the user navigates into "proposal1" details
    When the user creates a comment
    Then a comment should appear on the comment list