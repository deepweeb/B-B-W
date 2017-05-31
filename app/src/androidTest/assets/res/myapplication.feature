Feature: test

    Scenario: Ask for name and Iban
        Given I have my app configured and I am asked for my name and Iban
        When I fill in my name
        And I fill in my Iban
        Then I should go to the main page


