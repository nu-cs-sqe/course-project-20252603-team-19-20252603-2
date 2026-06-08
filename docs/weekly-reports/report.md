# Week 3 (04/13/2026-04/19/2026)
**Planning and Progress Tracking**:
1. [done]: Decided on project (Exploding Kittens)
2. [done]: Updated README.md (https://github.com/nu-cs-sqe/course-project-20252603-team-19-20252603-2/pull/3 and https://github.com/nu-cs-sqe/course-project-20252603-team-19-20252603-2/pull/1)


# Week 4 (04/20/2026-04/26/2026) 
**Planning and Progress Tracking**:
1. [done] Decided on project form: GUI application
2. [done] Decided the time slot to meet with instructor in week 5
3. [done] Kevin: Created the use-cases analysis document and revised it with structured per-class data members and methods (https://github.com/nu-cs-sqe/course-project-20252603-team-19-20252603-2/pull/5 and https://github.com/nu-cs-sqe/course-project-20252603-team-19-20252603-2/pull/7)
4. [done] Created issues on project board in Github repo

# Week 5 (04/27/2026-05/03/2026)
**Planning and Progress Tracking**:
1. [done] Kevin: Implemented `Card` class with full TDD discipline — `CardType` enum (7 base-game values), constructor with null rejection, `getCardType`. Per-test Red→Green commit pairs; all 9 BVA test cases pass (https://github.com/nu-cs-sqe/course-project-20252603-team-19-20252603-2/pull/20)
2. [done] Kevin: Authored BVA analysis for `Card` class at `docs/bva/bva-Card.md`, consolidated into one-file-per-class format with one section per public method (https://github.com/nu-cs-sqe/course-project-20252603-team-19-20252603-2/pull/20)
3. [done] Kevin: Built advisory AI PR reviewer workflow (DeepSeek `deepseek-v4-pro`) that posts a severity-tagged sticky comment on every PR against `docs/STANDARDS.md` (BVA discipline, TDD ordering, sole authorship, Clean Code, i18n) (https://github.com/nu-cs-sqe/course-project-20252603-team-19-20252603-2/pull/20)
4. [done] Kevin: Documented project standards rubric at `docs/STANDARDS.md`, added `docs/design/design-doc.md`, and rewrote `docs/use-cases/use-cases.md` into proper Actor / Preconditions / Main Flow / Alternate Flows / Postconditions format (https://github.com/nu-cs-sqe/course-project-20252603-team-19-20252603-2/pull/20)
5. [done] Kevin: Refactored `Card.java` exception messages to use i18n keys (`card.nullType`) instead of hard-coded English text, addressing 🔴 i18n violation flagged by AI reviewer (https://github.com/nu-cs-sqe/course-project-20252603-team-19-20252603-2/pull/20)
6. [done] Kevin: Added base i18n message bundles `src/main/resources/message_en.properties` (English) and `message_zh.properties` (Simplified Chinese) — satisfies the A-tier "≥ 2 locales must ship" requirement and provides lookup targets for the `card.nullType` key (https://github.com/nu-cs-sqe/course-project-20252603-team-19-20252603-2/pull/20)
7. [done] Kevin: Hardened the AI reviewer workflow — removed `max_tokens` cap (model was hitting it on synthetic merge-commit runs and returning empty content), added `reasoning_content` fallback, and added a `concurrency:` block to cancel stale parallel runs on the same PR (https://github.com/nu-cs-sqe/course-project-20252603-team-19-20252603-2/pull/20)
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
4. [done] Kevin: Refactored `Player` to match the design-doc API (`int playerId`, `List<Card>` hand, `getCardAt`/`getHand`/`getIndexOfCard`/`markDead`, all error strings i18n-keyed) and built the `GameEngine` setup phase (validate 2–5 players, deal 5-card hands, rig the deck with Exploding Kittens/Defuses). Per-method Red→Green TDD with BVA (https://github.com/nu-cs-sqe/course-project-20252603-team-19-20252603-2/pull/43)

# Week 8 (05/18/2026-05/24/2026)
**Planning and Progress Tracking**:
1. [done] Team: Read the Week-7 instructor code-review feedback and merged the feedback PR to close the loop (https://github.com/nu-cs-sqe/course-project-20252603-team-19-20252603-2/pull/44)

# Week 9 (05/25/2026-05/31/2026)
**Planning and Progress Tracking**:
1. [done] Vincent: Refactored `Deck.peekTop` to extract the three input-validation checks into a private helper, addressing the instructor's Week-6 "each function does one thing" feedback (https://github.com/nu-cs-sqe/course-project-20252603-team-19-20252603-2/pull/46)
2. [done] Kevin: Reviewed and merged Vincent's `peekTop` refactor PR (https://github.com/nu-cs-sqe/course-project-20252603-team-19-20252603-2/pull/46)

# Week 10 (06/01/2026-06/07/2026)
**Planning and Progress Tracking**:
1. [done] Kevin: Added UI-facing `GameEngine` methods for the UI developer — `isDeckEmpty`, `getPlayerHand` (defensive copy via new `Player.getHand`), `drawCardForCurrentPlayer`, `advanceToNextPlayer`; TDD + design-doc + BVA (https://github.com/nu-cs-sqe/course-project-20252603-team-19-20252603-2/pull/48)
2. [done] Kevin: Set up PIT mutation testing (`info.solidsoft.pitest`) wired into `./gradlew build`, mirroring the Code-Coverage lab; first run reports 95% test strength. TurnTracker `changeCurrentDirection` shows as the lone uncovered method — flagged for Allan (https://github.com/nu-cs-sqe/course-project-20252603-team-19-20252603-2/pull/47)
3. [done] Kevin: Finalized the playable game loop — new `REVERSE` + `TARGETED_ATTACK` card types dealt from the deck (size 63), `RuleManager` legality checks, `ActionController` effects (shuffle, see-the-future, reverse, favor, cat-pair steal with injected `Random`), all `GameEngine` play methods (skip/shuffle/see-the-future/reverse/attack/targeted-attack/favor/cat-pair/nope), explode-or-defuse resolution, forced-turn-aware `endTurnByDrawing`, `getDiscardPile`, and two win conditions (last player standing + draw-pile exhausted). Per-method Red→Green TDD; design-doc, BVA, and use-cases updated alongside (https://github.com/nu-cs-sqe/course-project-20252603-team-19-20252603-2/pull/51)
4. [done] Vincent: Implemented the Start, Instructions, GameSetup, CardView, and Game Screen for the UI following MVC (https://github.com/nu-cs-sqe/course-project-20252603-team-19-20252603-2/pull/49)
5. [done] Vincent: Implemented the Controllers and Models for each screen 
6. [done] Vincent: Implemented a navigation file to control switching between screens 
7. [done] Vincent: Conduct BVA/TDD for the methods in UI that needs it
8. [done] Vincent: Added the CSS files, card images, and a random selector method
9. [done] Vincent: Updated the i18n file to contain the test needed for the screens/view created and updated the design doc.
10. [done] Vincent: Connected the methods that Kevin implemented in GameEngine to have a semi functioning game
11. [done] Kevin: Closed the `TurnTracker` cyclomatic-coverage gap flagged in the Week-10 instructor review — added tests for the throw branches in `setNumTotalPlayers` (players < 2) and `setCurrentDirection` (direction not ±1), restored the missing `@Test` on `changeCurrentDirection_4TotalPlayers...`, and added BVA Methods 5–6 in `docs/bva/bva-TurnTracker.md` (https://github.com/nu-cs-sqe/course-project-20252603-team-19-20252603-2/pull/61)
12. [done] Kevin: Project-board management cleanup for the Week-10 instructor feedback — rewrote all existing board issues with detailed Owner/Goal/Scope/PR-link descriptions and added board entries for the previously-untracked work (TurnTracker, GameEngine setup, UI-facing methods, full game loop, PIT setup), each assigned to its contributor by commit history
13. [done] Vincent: Connected all the existing cards to the game, created a winner screen, and did some slight refactoring to the codebase (https://github.com/nu-cs-sqe/course-project-20252603-team-19-20252603-2/pull/53)