Feature:
  The participants should be able to create proposals.

  Scenario:
    Given the test database is loaded
    And the user navigates to "localhost:8090"
    And user "prueba01@prueba.es" with password "dgM4BrQu8zHuXSk" is logged in
    When the user clicks on the create proposal button
    And the user fills and sends proposal creation form with category "Category 2" title "Testerino" and explanation "Hey, this is a dumb proposal"
    Then the user should see proposal "Testerino"
    And a kafka createProposal event is generated for "Testerino"
