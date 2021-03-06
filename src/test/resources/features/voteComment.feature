Feature:
  The participants should be able to vote comments
  Each comment should have a button. Each user should only be able to vote a comment once.

  Scenario: a user votes a comment
    Given the test database is loaded
    And the user navigates to "localhost:9000"
    And user "prueba01@prueba.es" with password "dgM4BrQu8zHuXSk" is logged in
    And the user navigates into "Proposal 1" details
    When the user clicks on the comment's vote button with title "Proposal 1 seems quite good"
    Then the amount of votes for comment "Proposal 1 seems quite good" with initial value "7" is increased and "prueba01@prueba.es" is added to the votes list

  Scenario: a user votes a comment twice
    Given the test database is loaded
    And the user navigates to "localhost:9000"
    And user "prueba01@prueba.es" with password "dgM4BrQu8zHuXSk" is logged in
    And the user navigates into "Proposal 1" details
    When the user clicks on the comment's vote button with title "Proposal 1 seems quite good"
    Then the button to vote comment "Proposal 1 seems quite good" is not present

  Scenario: a user votes and then unvotes a comment
    Given the test database is loaded
    And the user navigates to "localhost:9000"
    And user "prueba01@prueba.es" with password "dgM4BrQu8zHuXSk" is logged in
    And the user navigates into "Proposal 1" details
    When the user clicks on the comment's vote button with title "Proposal 1 seems quite good"
    And the user clicks on the comment's vote button with title "Proposal 1 seems quite good"
    Then the amount of votes for comment "Proposal 1 seems quite good" with initial value "7" is not increased