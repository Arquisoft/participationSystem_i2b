Feature:
  The user should be able to log into the application.
  The username and password must match those stored in the database. The password will be sent directly to the server.

  Scenario: A user tries to login and succeeds
    Given the test database is loaded
    And the user navigates to "localhost:8090"
    When the user introduces username "prueba01@prueba.es" and password "dgM4BrQu8zHuXSk"
    Then the user successfully logs in

  Scenario: A user tries to login and fails
    Given the test database is loaded
    And the user navigates to "localhost:8090"
    When the user introduces username "prueba03@prueba.es" and password "dgM4BrQu8zHuXSk"
    Then the login fails

  Scenario: A user puts a wrong password
    Given the test database is loaded
    And the user navigates to "localhost:8090"
    When the user introduces username "prueba01@prueba.es" and password "wrongpassword"
    Then the login fails
