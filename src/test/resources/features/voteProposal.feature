Feature:
  The participants should be able to vote proposals.
  Each proposal should have a button. Each user should only be able to vote each proposal once.

  Scenario: a user votes a proposal
    Given the test database is loaded
    And the user navigates to "localhost:8090"
    And user "prueba01@prueba.es" with password "dgM4BrQu8zHuXSk" is logged in
    When the user clicks on the vote button of "Proposal 1"
    Then the amount of votes for proposal "Proposal 1" with initial value "5" is increased and "prueba01@prueba.es" is added to the votes list

  Scenario: a user votes a proposal twice
    Given the test database is loaded
    And the user navigates to "localhost:8090"
    And user "prueba01@prueba.es" with password "dgM4BrQu8zHuXSk" is logged in
    When the user clicks on the vote button of "Proposal 1"
    Then "Proposal 1" vote button is not visible

  Scenario: a user votes a proposal
    Given the test database is loaded
    And the user navigates to "localhost:8090"
    And user "prueba01@prueba.es" with password "dgM4BrQu8zHuXSk" is logged in
    When the user clicks on the vote button of "Proposal 1"
    Then a kafka voteProposal event is generated

