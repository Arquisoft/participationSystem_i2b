Feature:
  The participants should be able to vote proposals.
  Each proposal should have a button. Each user should only be able to vote each proposal once.

  Scenario: a user votes a proposal
    Given Selenium driver is loaded
    And the test database is loaded
    And the user navigates to "webpage"
    And user "user1" with password "user1" is logged in
    When the user clicks on the vote button of "proposal1"
    Then votesAmount is increased and the userId is added to the votes list

  Scenario: a user votes a proposal twice
    Given Selenium driver is loaded
    And the test database is loaded
    And the user navigates to "webpage"
    And user "user1" with password "user1" is logged in
    When the user clicks on the vote button of "proposal1"
    Then "proposal1"'s button is not visible

  Scenario: a user votes a proposal
    Given Selenium driver is loaded
    And the test database is loaded
    And the user navigates to "webpage"
    And user "user1" with password "user1" is logged in
    When the user clicks on the vote button of "proposal1"
    Then a kafka voteProposal event is generated

