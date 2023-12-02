Feature: Fetch all currency pairs by base

  Scenario: Retrieving all currency pairs by base positive scenario
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
  },
   {
    "id": "3",
    "from": "USD",
    "to": "EUR",
    "rate": 0.2314
  },
  {
    "id": "4",
    "from": "EUR",
    "to": "USD",
    "rate": 0.2314
  }
]
    """
    When the client requests for currency pairs with base "USD"
    Then the client receives status code of 200
    And the user should receive the following data as JSON
    """
    [
  {
    "id": "1",
    "from": "USD",
    "to": "UAH",
    "rate": 0.2314
  },
  {
  "id": "3",
    "from": "USD",
    "to": "EUR",
    "rate": 0.2314
  }
]
    """

  Scenario: Retrieving all currency pairs negative scenario, empty storage
    Given the storage has been cleared
    When the client requests for currency pairs with base "CAD"
    Then the client receives status code of 404

  Scenario: Retrieving all currency pairs negative scenario, incorrect param
    Given the storage has been cleared
    When the client requests for currency pairs with base "null"
    Then the client receives status code of 404