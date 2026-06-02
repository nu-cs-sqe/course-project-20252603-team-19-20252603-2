# Game Loop Finalization — Design

Date: 2026-06-01
Branch: `feat/game-loop`

## Goal

Turn the existing setup-only `GameEngine` into a fully playable Exploding
Kittens loop: every current card type wired in, two new card types
(`REVERSE`, `TARGETED_ATTACK`), and a second win condition (draw pile
exhausted) on top of last-player-standing.

All player decisions arrive as **explicit method parameters** — no I/O in the
domain, so everything is deterministic and unit-testable. The UI layer (owned
by a teammate) calls these methods.

## Class responsibilities

- **`GameEngine`** — orchestrator and single entry point for a turn. Owns
  `players`, `deck`, `turnTracker`, and turn-state (`forcedTurns`,
  `lastPlayedCard`, pending exploding kitten). Exposes the turn API and the
  win-condition queries. Delegates legality to `RuleManager` and effects to
  `ActionController`.
- **`RuleManager`** — pure, stateless validation. Answers "is this play legal
  in this state?" and throws `IllegalStateException` / `IllegalArgumentException`
  with an i18n key when not. No mutation.
- **`ActionController`** — applies a validated card's effect by mutating game
  state. Takes an injected `Random` (package-private constructor) so the
  Cat-pair random steal is deterministic under test.

Per-play flow: `GameEngine.playCard*` → `RuleManager.validate*` (throws if
illegal) → `ActionController.apply*` (mutates) → `GameEngine` updates
turn/win state and records `lastPlayedCard`.

## Turn state

- `forcedTurns` (int, default 1) — draws the current player still owes. Attack
  and Targeted Attack set the *next/target* player's count (stacking: owed N →
  next owes N + 2).
- `lastPlayedCard` (CardType, nullable) — the only thing a Nope can cancel.
- `pendingKitten` (Card, nullable) — an exploding kitten just drawn, awaiting
  defuse-or-die resolution.

### A single turn

1. Current player plays zero or more cards (`playSkip`, `playAttack`, ...).
   Each play: validate → apply → record `lastPlayedCard`.
2. Player ends the turn by drawing (`drawCardForCurrentPlayer()`):
   - Exploding Kitten → set `pendingKitten`; caller resolves via
     `defuseAndReinsert(index)` or `resolveExplosionWithoutDefuse()`.
   - Otherwise the card stays in hand and the turn ends.
3. Decrement `forcedTurns`. If > 0, same player goes again. If 0,
   `advanceToNextPlayer()` (skipping dead players) and reset `forcedTurns`.

Skip and Attack and Reverse end the turn **without** drawing.

## Card-by-card behavior

| Card | Method | Effect |
|------|--------|--------|
| Skip | `playSkip()` | End turn, no draw; consume one forced turn. |
| Shuffle | `playShuffle()` | `deck.shuffle()`; same player continues. |
| See the Future | `playSeeTheFuture(): List<Card>` | Return `deck.peekTop(3)` copy (fewer if pile smaller); no state change. |
| Attack | `playAttack()` | End turn no-draw; next player owes current + 2 turns. |
| Reverse | `playReverse()` | `turnTracker.changeCurrentDirection()`; ends turn like Skip. |
| Favor | `playFavor(targetId, cardIndex)` | Target gives the chosen card to current player. |
| Targeted Attack | `playTargetedAttack(targetId)` | Like Attack, but `targetId` owes the turns. |
| Cat pair | `playCatPair(targetId)` | Requires 2 matching `CAT_CARDS`; steal a random card from target. |
| Nope | `playNope(noperId)` | Cancel `lastPlayedCard`'s effect (simplified, no reaction window). |
| Defuse | resolved on draw | `defuseAndReinsert(index)`. |

### RuleManager rejections

- Playing `DEFUSE` or `EXPLODING_KITTEN` directly.
- Favor / Targeted Attack / Cat pair with an invalid, dead, or self target.
- Cat pair without two matching `CAT_CARDS` in hand.
- Nope when `lastPlayedCard` is null (nothing to cancel).

## Explode / Defuse resolution

`drawCardForCurrentPlayer()` returns the drawn card. If it is an Exploding
Kitten, the engine stores it as `pendingKitten` and the caller resolves:

1. **No Defuse** → `resolveExplosionWithoutDefuse()` → `player.markDead()`,
   kitten to discard, advance to next living player.
2. **Has Defuse** → `defuseAndReinsert(index)` → remove one Defuse from hand to
   discard, `deck.insertAt(kitten, index)`, end turn, advance.

## Win conditions

Checked after each turn-ending event:

1. **Last player standing** — `countAlive() == 1` → that player wins.
2. **Draw pile exhausted** — `deck.isEmpty()` with >= 2 alive → winner is the
   living player with the **most cards in hand**, ties broken by **lowest
   player id**.

API: `isGameOver(): boolean`, `getWinnerId(): int` (throws `game.notOver` when
the game is not over). `advanceToNextPlayer()` skips dead players.

## New CardType values

`REVERSE`, `TARGETED_ATTACK` appended to the enum. `Deck` constructor gains
counts (4 REVERSE, 3 TARGETED_ATTACK) which shift the setup draw-pile
constants; recompute and update GameEngine setup tests/BVA.

## i18n keys (en + zh)

- `rule.play.cannotPlayDirectly`
- `rule.target.invalid`
- `rule.catPair.needTwo`
- `rule.nope.nothingToCancel`
- `game.notOver`

## Mocking

`ActionController` takes a `Random` via a package-private constructor;
production uses `new Random()`, tests inject a seeded `Random` for a
deterministic Cat-pair steal. No new build dependency.

## Implementation order (each step = doc/BVA + Red→Green commits)

1. Design doc (this file), `REVERSE` + `TARGETED_ATTACK` enum values, `Deck`
   counts + recomputed setup constants.
2. `RuleManager` validation methods (pure).
3. `ActionController` effects — non-interactive first (Skip, Shuffle, Reverse,
   See the Future, Attack), then interactive (Favor, Targeted Attack, Cat pair).
4. `GameEngine` turn-state, `playCard*` dispatch, explode/defuse resolution,
   living-player advance.
5. Win conditions (`isGameOver` / `getWinnerId`).
6. Nope (simplified) last.

Standards: per-method Red→Green commits, design-doc + BVA updated alongside
code, sole authorship, i18n keys (no hard-coded user strings), Clean Code.
