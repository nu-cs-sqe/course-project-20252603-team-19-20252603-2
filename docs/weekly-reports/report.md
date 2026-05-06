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

# Week X (XX/XX/2026-XX/XX/2026) TEMPLATE (You can change the format to whatever the team likes better)
**Planning and Progress Tracking**:
1. [done] Person: Task (Links to PR)
2. [not started] Person: Task (Links to PR)
3. [80% done] Person: Task (Links to PR)
