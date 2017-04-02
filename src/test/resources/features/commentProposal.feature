Feature:
  The participants should be able to comment proposals.
  Each participant can create as many comments as wanted.

  Scenario:
    Given the test database is loaded
    And the user navigates to "localhost:8090"
    And user "user1" with password "user1" is logged in
    And the user navigates into "Proposal 1" details n
    When the user creates a comment
    Then a comment should appear on the comment list