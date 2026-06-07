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

## Acknowledgements
REFERENCES, SOURCE OF HELP ETC
