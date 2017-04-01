Feature:
  The participant should see a list of proposals in their home page. They should be ordered by popularity (votes).
  The proposals should be queried from the databased and shown in a list (pagination?).

  Scenario: Proposals are shown
    Given the test database is loaded
    And the user navigates to "localhost:8090"
    And user "prueba01@prueba.es" with password "dgM4BrQu8zHuXSk" is logged in
    When the user navigates to "localhost:8090/user/home"
    Then the user should see "Proposal 1" and "Proposal 2"

  Scenario: Proposals are ordered correctly
    Given the test database is loaded
    And the user navigates to "localhost:8090"
    And user "prueba01@prueba.es" with password "dgM4BrQu8zHuXSk" is logged in
    When the user navigates to "localhost:8090/user/home"
    Then the user should see proposal "Proposal 2" before "Proposal 1"