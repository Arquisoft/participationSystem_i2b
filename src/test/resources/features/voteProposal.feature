Feature:
  The participants should be able to vote proposals.
  Each proposal should have a button. Each user should only be able to vote each proposal once.

  Scenario: a user votes a proposal
    Given the test database is loaded
    And a user logged in
    When the user clicks on the vote button of "proposal1"
    Then votesAmount is increased and the userId is added to the votes list

  Scenario: a user votes a proposal twice
    Given the test database is loaded
    And a user logged in
    When the user clicks on the vote button of "proposal1" twice
    Then votesAmount is increased only once and the userId is added to the votes list once

  Scenario: a user votes a proposal
    Given the test database is loaded
    And a user logged in
    When the user clicks on the vote button of "proposal1"
    Then a kafka voteProposal event is generated

