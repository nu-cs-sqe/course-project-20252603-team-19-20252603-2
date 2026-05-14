Feature: Keep Track of Turns
  In order to play the game,
  as a player,
  I want to know whose turn is happening.
  Scenario: Game start
    Given a deck of cards and player A's turn
    And player A plays turn
    When player A is finished
    Then turn goes to next player
