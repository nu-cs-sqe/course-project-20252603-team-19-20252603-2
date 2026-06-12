![Gradle Build](https://github.com/nu-cs-sqe/course-project-20252603-team-19-20252603-2/actions/workflows/main.yml/badge.svg)
# Exploding Kittens

## Contributors
- Mahnum Somji
- Kevin Yan
- Vincent Tang
- Allan Jiang

## Dependencies
- JDK 11
- JUnit 5.10
- Gradle 8.10

## Design Choices

### Nope card (undo semantics)
A Nope cancels the **last played card** by undoing its effect, dispatched on the
card type:

- **Skip / Reverse** — the turn returns to the player who played the card
  (Reverse also flips the playing direction back), and forced turns reset to 1.
- **Attack / Targeted Attack** — the forced-turn count is reduced by the amount
  the attack added (e.g. 2 → 1, 4 → 2) and the turn returns to the attacker
  (tracked via the last-player id).
- **See the Future** — the peek cannot be "un-seen", so instead the draw pile is
  shuffled, invalidating the three cards the player saw.
- **Shuffle** — intentionally **not** noped: a Nope would itself shuffle to
  "undo", which is indistinguishable from the original Shuffle, so there is no
  observable effect to revert.
- **Other cards (Favor, Cat pair/triple, etc.)** — no automatic revert; the
  effect stands. There is no clean, unambiguous way to roll these back, so they
  are a deliberate no-op.
- **Nope working for cards played more than 1 turn ago** — the nope card as of now
  will still undo an action even if the card was played a while back and no new cards played.
  There isn't a clean way to avoid this as we can't reset after drawing or else nope won't work properly
  Additionally, if we try to use the last player id as reference there will still be some difficulties with
  resetting the last played card as players explode and the order gets mixed up. There is no clean way that we
  can come up with to tackle this.

### Skip card/Reverse card
Both skip and reverse ends the turns for the player when played but our understand of it is that
it should only end one turn instead of all of the turns if the users gets attacked and has to draw 
more than once

### Clone card
For the clone card we didn't do it for nope as it requires the last card played and there is no clean
way to undo what a nope has undo already. Additionally, due to the fact that clone cards so so powerful
we decided to clone allow cloning bury as the function is really similar to see the future and should be
used on other cards like clone attacks or favor. 

Additonally, we won't allow the user to play clone then cat card unless it played together as you cannot
play a single cat card therefore you must always play them together. You also can't play clone cards with 
other cards to form a special combo other than cat card. You cannot play two clone cards as a special combo
based on the rules for the card as it violates playing a clone card on top of another one.

### Feral Cat 
You cannot play it alone or use it for a special combo of 2 or 3. You must play it with cat cards as that's
our understanding of the card.

## Acknowledgements / Exceptions
Deepseek API for the pull request check, Google Gemini for the UI prototype design, Cursor, Exploding Kittens Wiki
for the card images. 

For the mutation test there is one mutant that can't be killed. The mutant for forcedTurnsAfterUndoingAttack where
we switch the < to <= cannot be killed due to the normal forced turns and the turns added by attack being 1 as the lowest
which is also the normal forced turns. This causes the turns returned to be identical regardless of using < or <=. 
For example, if reduce is 1 then both using < and <= would give us 1. If it's not less than the normal forced turn so
we return 1. If it's equal to the normal forced turns then we also return 1. This causes it to be impossible to truly 
kill the mutant. Using < or <= will always return the same value causing it to be impossible to kill.

The advanceToNextLivingPlayer_skipsDeadPlayer test kills the mutation for advanceToNextLivingPlayer specifically 
the negating the condition. However, there is a test or some test in GameEngineTest that causes a timed_out which
won't allow the mutant to be killed unless you only keep the advanceToNextLivingPlayer_skipsDeadPlayer test.

Due to many methods in the model classes are setter, getter, or calling methods from already fully tested classes. 
We decided to not do more mutant test for those and focus more on the classes that actually handle the game logic.

## Notes
When playing the favor card there is a chance that the cards won't show up properly. We tried fixing it
but recreating the bug is almost impossible and completely random. There isn't a series of step that we 
could do to replicate the bug. There is also no way to check if it's actually fixed as whether or not it
occurs is basically random, so there is no good way to fully check that it's fixed of not.