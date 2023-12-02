Feature: Fetch rate by currency pair

  Scenario: Retrieving rate by currency pair scenario
    Given the storage has been cleared
    And the following currency pairs are available as JSON
   """
   [
  {
    "id": "1",
    "from": "USD",
    "to": "UAH",
    "rate": 0.2314
  },
  {
    "id": "2",
    "from": "EUR",
    "to": "UAH",
    "rate": 0.2314
  }
]
    """
    When the client requests for currency pairs with from "EUR", to "UAH"
    Then the client receives status code of 200
    And the user should receive the following single data as JSON
   """
  {
    "id": "2",
    "from": "EUR",
    "to": "UAH",
    "rate": 0.2314
  }
   """

  Scenario: Retrieving currency pair negative scenario, empty result
    Given the storage has been cleared
    When the client requests for currency pairs with from "USD", to "UAH"
    Then the client receives status code of 404

  Scenario: Retrieving currency pair negative scenario, incorrect param
    Given the storage has been cleared
    When the client requests for currency pairs with from "", to "UAH"
    Then the client receives status code of 400