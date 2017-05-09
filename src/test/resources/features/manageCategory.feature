Feature:
  The personnel from the council should be able to manage categories

  Scenario: the personnel member adds a new category
    Given the test database is loaded
    And the user navigates to "localhost:9000"
    And user "admin@admin.com" with password "admin" is logged in
    And the user navigates into admin settings
    When the personnel member adds a new category "Test category" with minimal support of "50"
    Then the category "Test category" is added with "50" minimal support

  Scenario: the personnel member deletes a category
    Given the test database is loaded
    And the user navigates to "localhost:9000"
    And user "admin@admin.com" with password "admin" is logged in
    And the user navigates into admin settings
    When the personnel member adds a new category "Test category" with minimal support of "50"
    And the personnel member deletes category "Test category"
    Then the category "Test category" doesn't exist