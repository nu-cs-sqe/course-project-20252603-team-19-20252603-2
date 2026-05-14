# Project Standards

These are the standards every contribution to this repository commits to. They are
the rubric the AI PR reviewer (`.github/workflows/ai-review.yml`) uses to evaluate
pull requests, and the same rubric a human reviewer should hold each PR to.

The goal is the **A-tier** of `project_grading.pdf`. Every section below is a
non-negotiable rule unless the rubric itself explicitly excludes a case (e.g.,
enums and GUI code are excluded from coverage requirements).

---

## 1. Use-cases doc as spec

`docs/use-cases/use-cases.md` is the team's binding contract for what the domain
classes look like. The AI treats it as the source of truth.

- Every class the team implements has a `## <Name> Class` section.
- Each class section lists its **public** data members under `### Data Members`
  and its **public** methods under `### Methods`.
- Method bullets are written as backticked Java signatures, e.g.
  `` - `getCardType(): CardType` `` or `` - `Card(CardType cardType)` — constructor; rejects null. ``.
- Enum sections (heading suffix `(enum, ...)`) are out of scope for BVA / coverage,
  per the rubric.
- Adding a method to a class section without producing the BVA file in the same
  PR (or a previously-merged PR) is a 🔴 violation.
- Removing or renaming a method without updating every reference (BVA file name,
  test name, source) is a 🔴 violation.

## 2. BVA discipline

The team commits to **one BVA analysis file per public method**, following
`docs/bva/bva-template.md`.

- Filename: `docs/bva/bva-<methodName>.md`.
- Constructors: `docs/bva/bva-<className>.md` (constructor's Java name is the
  class name).
- Required sections, in order:
  1. `## Method 1: \`\`\`public <signature>\`\`\`` heading.
  2. `### Step 1-3 Results` table with rows for Step 1 (domain),
     Step 2 (data type from BVA Catalog), Step 3 (concrete edge values).
  3. `### Step 4:` heading with `##### All-combination or each-choice: <decision>`
     and a Test Case table with columns: Test Case # | System under test |
     Expected output | Implemented?.
  4. `## Recall the 4 steps of BVA` footer (verbatim from the template).
- The **Implemented?** column starts as `no` and flips to `yes` (or
  `:white_check_mark:`) in the same commit that adds the green test for that
  case. Doc and code never drift.
- BVA test cases must include the null / empty / boundary edges of the input
  domain — that is the whole point of BVA. Skipping the null edge for a
  reference-typed input is a 🟡 suggestion (the AI should call it out).

The following is also an acceptable version of the BVA Analysis.
## Method under test: `methodName()`
- TC1.1: NAME OF THE TEST CASE
  - State of the system: 
  - Expected output: 
  - Implemented: yes/no

- TC1.2: NAME OF THE TEST CASE
  - State of the system: 
  - Expected output: 
  - Implemented: yes/no

## 3. TDD ordering in commits

The C-tier rubric requires "evidence of a TDD/BDD workflow ... in the GitHub
commit history." The team enforces this as a per-method commit pattern:

For every newly-introduced public method in a PR, the PR's commit history must
show, **in topological order**:

1. A commit that adds the BVA file (`bva-<methodName>.md` or
   `bva-<className>.md`).
2. A commit that adds the failing test in `src/test/java/`.
3. A commit that adds the implementation in `src/main/java/`.

Equality is fine — one commit doing two of these steps at once does not violate
ordering. Reverse order (impl committed before test, or test committed before
BVA) is a 🔴 violation.

A PR that squashed its commits at the end may legitimately have followed TDD
during development; the AI should call it out as 🟡 ("ordering not visible in
history; verify TDD was followed in the original branch") rather than 🔴.

## 4. Sole authorship

Every commit must be authored by exactly one person — the PR author.

- No `Co-Authored-By:` trailers (AI assistants, pair-programming partners, or
  otherwise) are allowed in commit message bodies. CS 380 grades individual
  contribution from commit history, and co-authorship distorts that signal.
- Author and Committer must match (`%an == %cn`, `%ae == %ce`). Mismatch usually
  indicates a rebase that retained someone else's authorship — fix with
  `git commit --amend --reset-author` before pushing.
- A 🔴 violation otherwise.

## 5. Substantive commits

- No empty commits (`git commit --allow-empty`).
- No whitespace-only or rename-only commits (excluding `.gitkeep` cleanups,
  which are fine because they remove a placeholder).
- Each commit's diff must reflect a real, identifiable step of the work.
- Commit message *wording* is free — the team has explicitly chosen not to
  enforce Conventional Commits or any prefix scheme. Whatever the author writes
  is acceptable as long as the commit content is substantive.

## 6. Clean Code

The A-tier rubric demands "code fully satisfying Clean Code standards."

- **Names:** descriptive, intention-revealing, no abbreviations except
  domain-standard ones (`id`, `idx`, `i` for loop counters in tight scope).
- **Functions:** small (target ≤ 20 lines), one responsibility, no flag
  parameters (`boolean isFoo` controlling two paths is a 🟡 — split the
  function instead).
- **Magic numbers:** any literal other than `0`, `1`, `-1`, `true`, `false`,
  `""`, or `null` must be a `private static final` constant with a meaningful
  name. (Example from this repo: the example example-kitten repo uses
  `final int maxDeckSize = 42;`.)
- **Comments:** only when the *why* is non-obvious. Don't restate what the code
  does. Don't reference task or PR numbers ("added for #42" rots).
- **Line length:** ≤ 100 characters.
- **Indentation:** tabs (matching the example repo's checkstyle config).
- **No commented-out code.** If it's not in use, delete it. Git remembers.
- **Exception messages:** extract as `private static final String` constants
  with descriptive names; pattern proven in
  `/tmp/example-kitten/src/main/java/domain/game/Player.java`.

## 7. i18n requirement (A-tier)

The rubric: "code supports adding new locales without changing existing code."

- All user-facing strings (game prompts, card names, error text shown to the
  player) must be looked up from
  `src/main/resources/message_<locale>.properties`. Hard-coding any
  user-facing string in Java is a 🔴 violation.
- Bundles must be loaded via `ResourceBundle.getBundle("message", locale)` —
  the standard idiom — so adding a new locale is purely a `.properties` file
  addition, no Java edits.
- At least 2 locales must ship by the time the team submits.
- Domain code (`Card`, `Deck`, `Player`, `Game`) does not store
  human-readable text — that's a UI concern, lookup-keyed by `CardType` or
  similar.

## 8. Testing conventions

- JUnit 5 (`@Test`, `@ParameterizedTest`, `@EnumSource`, `@ValueSource`).
- One test class per source class, mirroring package and named `<Class>Test`
  (e.g., `domain.Card` ↔ `domain.CardTest`).
- Exception cases use `assertThrows(SomeException.class, () -> { ... })` and
  also assert on the exception's message.
- Test method names are descriptive English clauses (matching the example
  repo's convention: `addNonDefuseToPlayerWhenShouldBeDefuse`,
  `getIndexOfCardSecond`).
- Method names can also be in the format methodName_stateUnderTest_expectedBehavior because this is what we learned in class.
- Tests do not share mutable state (no `static` test fixtures).
- BVA test cases in the doc should map 1:1 to test methods (or to rows in a
  parameterized test); a mismatch is a 🟡 suggestion.

---

## How to read AI suggestions

The AI reviewer posts a single sticky comment on each PR
(identified by `<!-- ai-reviewer:sticky -->`). Each finding carries a severity
icon:

| Icon | Meaning | What to do |
|------|---------|------------|
| 🔴 | **Likely violation** of a standard above. | Address before merge. If you disagree, push back in a reply — the AI is not infallible. |
| 🟡 | **Suggestion** — possible improvement, judgment call. | Consider it. Skip if the cost outweighs the benefit. |
| ✅ | **Looks good** — affirmation that a standard is met. | Keep doing this. Deliberate-good-behavior reinforcement is intentional. |
| 🟠 | **AI infrastructure issue** (API down, output malformed, PR too large to fully review). | Self-check against this doc; the workflow does not block on AI errors. |

The comment is **never a required status check** — the PR remains mergeable
regardless of what the AI says. The only hard merge gate is `./gradlew build`
in `main.yml`.
