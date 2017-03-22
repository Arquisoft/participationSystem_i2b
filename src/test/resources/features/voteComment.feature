Feature:
  The participants should be able to vote comments
  Each comment should have a button. Each user should only be able to vote a comment once.

  Scenario: a user votes a comment
    Given the test database is loaded
    And a user logged
    And navigated to "proposal1" details
    When the user clicks on the comment's vote button
    Then votesAmount is increased and the userId is added to the votes list

  Scenario: a user votes a comment twice
    Given the test database is loaded
    And a user logged
    And navigated to "proposal1" details
    When the user clicks on the comment's vote button twice
    Then the comment's votesAmount is increased only once and the userId is added to the comment's votes list once

  Scenario: a user votes a comment
    Given the test database is loaded
    And a user logged
    And the user navigated to "proposal1" details
    When the user clicks on the comment's vote button
    Then a kafka voteComment event is generated