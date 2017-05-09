Feature:
  The participants should be able to comment proposals.
  Each participant can create as many comments as wanted.

  Scenario:
    Given the test database is loaded
    And the user navigates to "localhost:9000"
    And user "prueba01@prueba.es" with password "dgM4BrQu8zHuXSk" is logged in
    And the user navigates into "Proposal 1" details
    When the user clicks on the create comment button
    And the user fills and sends the comment with body "test comment"
    Then a comment should appear at the bottom of the comment list with body "test comment"
    And a kafka createComment event is generated for "test comment"