Feature: Fetch all currency pairs

  Scenario: Retrieving all currency pairs positive scenario
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
    "from": "USD",
    "to": "UAH",
    "rate": 0.2314
  }
]
    """
    When the client requests for currency pairs
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
  "id": "2",
    "from": "USD",
    "to": "UAH",
    "rate": 0.2314
  }
]
    """

  Scenario: Retrieving all currency pairs negative scenario, empty storage
    Given the storage has been cleared
    When the client requests for currency pairs
    Then the client receives status code of 404