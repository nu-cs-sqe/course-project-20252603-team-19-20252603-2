# Week 3 (04/13/2026-04/19/2026)
**Planning and Progress Tracking**:
1. [done]: Decided on project (Exploding Kittens)
2. [done]: Updated README.md (https://github.com/nu-cs-sqe/course-project-20252603-team-19-20252603-2/pull/3 and https://github.com/nu-cs-sqe/course-project-20252603-team-19-20252603-2/pull/1)


# Week 4 (04/20/2026-04/26/2026) 
**Planning and Progress Tracking**:
1. [done] Decided on project form: GUI application
2. [done] Decided the time slot to meet with instructor in week 5
3. [Working] Created use cases analysis document
4. [done] Created issues on project board in Github repo

# Week 5 (04/27/2026-05/03/2026)
**Planning and Progress Tracking**:
1. [done] Kevin: Implemented `Card` class with full TDD discipline — `CardType` enum (7 base-game values), constructor with null rejection, `getCardType`. Per-test Red→Green commit pairs; all 9 BVA test cases pass (https://github.com/nu-cs-sqe/course-project-20252603-team-19-20252603-2/pull/20)
2. [done] Kevin: Authored BVA analysis for `Card` class at `docs/bva/bva-Card.md`, consolidated into one-file-per-class format with one section per public method
3. [done] Kevin: Built advisory AI PR reviewer workflow (DeepSeek `deepseek-v4-pro`) that posts a severity-tagged sticky comment on every PR against `docs/STANDARDS.md` (BVA discipline, TDD ordering, sole authorship, Clean Code, i18n)
4. [done] Kevin: Documented project standards rubric at `docs/STANDARDS.md`, added `docs/design/design-doc.md`, and rewrote `docs/use-cases/use-cases.md` into proper Actor / Preconditions / Main Flow / Alternate Flows / Postconditions format
5. [done] Kevin: Refactored `Card.java` exception messages to use i18n keys (`card.nullType`) instead of hard-coded English text, addressing 🔴 i18n violation flagged by AI reviewer (Red→Green pair on `feat/jixin-card`)
6. [done] Kevin: Added base i18n message bundles `src/main/resources/message_en.properties` (English) and `message_zh.properties` (Simplified Chinese) — satisfies the A-tier "≥ 2 locales must ship" requirement and provides lookup targets for the `card.nullType` key
7. [done] Kevin: Hardened the AI reviewer workflow — removed `max_tokens` cap (model was hitting it on synthetic merge-commit runs and returning empty content), added `reasoning_content` fallback, and added a `concurrency:` block to cancel stale parallel runs on the same PR
8. [done] Mahnum: Opened draft PRs for `player-class-setup` and `player-initial-tests` branches
9. [done] Mahnum: Created `Player.java` stub and `docs/bva/bva-player` with BVA analysis covering constructor, `addCard`, `removeCard`, `getHandSize`, `hasCard`, and `isAlive`/`setAlive` boundary cases

# Week 6 (05/04/2026-05/10/2026)
**Planning and Progress Tracking**:
1. [done] Kevin: Added `CAT_CARDS` to the `CardType` enum and propagated to all dependent docs (`docs/design/design-doc.md` CardType list, `docs/bva/bva-Card.md` Method 2 Step 3 + new TC8). The parameterized `getCardType` test auto-picked up `CAT_CARDS` via `@EnumSource`; all 9 tests pass (https://github.com/nu-cs-sqe/course-project-20252603-team-19-20252603-2/pull/24)
2. [done] Kevin: Added `FAVOR` to the `CardType` enum and propagated to `docs/design/design-doc.md` and `docs/bva/bva-Card.md` (Method 2 Step 3 + new TC9). All 10 tests pass via `@EnumSource` auto-pickup (https://github.com/nu-cs-sqe/course-project-20252603-team-19-20252603-2/pull/24)
3. [done] Kevin: Set up Checkstyle (Google Java Style 10.18.2 base + project overrides for tab indent, no-Javadoc-required, `MagicNumber`, severity=error) and SpotBugs (default effort + confidence, HTML reports) as strict-mode quality gates wired into `./gradlew build`. PR's CI fails until each owner refactors their files on their own branch (https://github.com/nu-cs-sqe/course-project-20252603-team-19-20252603-2/pull/25)
4. [done] Kevin: Refactored `Card.java` (marked `final` to close SpotBugs `CT_CONSTRUCTOR_THROW` finalizer-attack vector) and `CardTest.java` (moved static imports above non-static to satisfy Checkstyle `CustomImportOrder` `STATIC###THIRD_PARTY_PACKAGE`) so the `Card` source files pass the strict linters from PR #25 (https://github.com/nu-cs-sqe/course-project-20252603-team-19-20252603-2/pull/27)
5. [done] Vincent: Implemented `Deck` class following TDD workflow. Implemented the Deck constructor, drawTop, shuffle, peekTop, and discard. All test cases passed. (https://github.com/nu-cs-sqe/course-project-20252603-team-19-20252603-2/pull/26)
6. [done] Vincent: Conducted BVA analysis for `Deck` class in `docs/bva/bva-Deck.md` for Deck constructor, drawTop, shuffle, peekTop, and discard and followed the team standards for the format.
7. [done] Vincent: Refactored `Deck.java` and `DeckTest.java` to follow checkstyle. Fixed variable names, importing, and removed all the magical numbers. (https://github.com/nu-cs-sqe/course-project-20252603-team-19-20252603-2/pull/29)


# Week 7 (05/11/2026-05/17/2026)
**Planning and Progress Tracking**:
1. [done] Allan: added methods in Deck class (https://github.com/nu-cs-sqe/course-project-20252603-team-19-20252603-2/pull/37)
2. [done] Allan: started game implementation (https://github.com/nu-cs-sqe/course-project-20252603-team-19-20252603-2/pull/39)
3. [in progress] Allan: started TurnTracker class

# Week 10 (06/01/2026-06/07/2026)
**Planning and Progress Tracking**:
1. [done] Kevin: Refactored `Player` to match the design-doc API (`int playerId`, `List<Card>` hand, `getCardAt`/`getHand`/`getIndexOfCard`/`markDead`, all error strings i18n-keyed) and built the `GameEngine` setup phase (validate 2–5 players, deal 5-card hands, rig the deck with Exploding Kittens/Defuses). Per-method Red→Green TDD with BVA (https://github.com/nu-cs-sqe/course-project-20252603-team-19-20252603-2/pull/43)
2. [done] Kevin: Added UI-facing `GameEngine` methods for the UI developer — `isDeckEmpty`, `getPlayerHand` (defensive copy via new `Player.getHand`), `drawCardForCurrentPlayer`, `advanceToNextPlayer`; TDD + design-doc + BVA (https://github.com/nu-cs-sqe/course-project-20252603-team-19-20252603-2/pull/48)
3. [done] Kevin: Set up PIT mutation testing (`info.solidsoft.pitest`) wired into `./gradlew build`, mirroring the Code-Coverage lab; first run reports 95% test strength (branch `setup/pitest`). TurnTracker `changeCurrentDirection` shows as the lone uncovered method — flagged for Allan
4. [done] Kevin: Finalized the playable game loop on `feat/game-loop` — new `REVERSE` + `TARGETED_ATTACK` card types dealt from the deck (size 63), `RuleManager` legality checks, `ActionController` effects (shuffle, see-the-future, reverse, favor, cat-pair steal with injected `Random`), all `GameEngine` play methods (skip/shuffle/see-the-future/reverse/attack/targeted-attack/favor/cat-pair/nope), explode-or-defuse resolution, and two win conditions (last player standing + draw-pile exhausted). Per-method Red→Green TDD; design-doc, BVA, and use-cases updated alongside
