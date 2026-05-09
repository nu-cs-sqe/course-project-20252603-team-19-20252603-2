# Week 4 Project Feedback by PM/TA

**Dedicated PM/TA**: Utkarsh Narain (utkarshnarain2026@u.northwestern.edu)

## How to Read This Feedback
> [!NOTE]
> **Purpose.** This feedback focuses on your team's progress and collaboration. It is meant as guidance, not judgement.

> [!IMPORTANT]
> **Scope.** For the BVA and TDD items, the PM/TA evaluates only the `main` branch. Ongoing work in feature branches will be evaluated after it is merged. If you'd like early feedback on work in progress, please reach out to your PM/TA directly.

> [!TIP]
> **Mistakes are expected :).** As the instructor mentioned in class, early mistakes are part of the learning process. As long as your team addresses the issues after you get the feedback, your grade will not suffer from them.

## Checklist
Status:
- ✅: All done/Good job!
- ⚠️: Attention needed
- ❌: Significant issue found
- ➖: No basis to evaluate

### Software Process Quality
| #   | Item                                                                                                                                                         |  Status   | Reviewer Notes      | Source Instructions or Resources                                                  |
|-----|--------------------------------------------------------------------------------------------------------------------------------------------------------------|:---------:|---------------------|-----------------------------------------------------------------------------------|
| 1   | GitHub repository branch protection rules are fully set up so that people cannot push into main without a pull request approval. (needed for Letter Grade C) | ✅        | Branch protection enabled; direct pushes to `main` now require PR approval. | Canvas assignment Project: Setup, Project grading rubrics                         |
| 2   | Continuous Integration (CI) is fully set up from the beginning. (needed for Letter Grade B)                                                                  | ✅        | CI configured and running from project root. | Canvas assignment Project: Setup, Project grading rubrics                         |
| 3   | The team uses the project management board steadily and frequently, and the description of each task is detailed. (needed for Letter Grade B)                | ⚠️        | Board used but many entries lack clear user stories. Also assign the ones in To DO, can always change it up later | Week 4 Wednesday Lecture (Lecture 08), Canvas assignment Project: Week 4 Guidance |
| 3.1 | Every functionality-related work item on the management board includes a user story, and optionally one or more use cases.                                   | ⚠️        | Several work items lack explicit user stories; add them per feature. | Week 4 Wednesday Lecture (Lecture 08), Canvas assignment Project: Week 4 Guidance |
| 3.2 | The design is documented somewhere, either in the work item description, or in a separate design document.                                                   | ❌        | Design documentation is missing; add design docs to work items or create a separate design document. | Week 4 Wednesday Lecture (Lecture 08), Canvas assignment Project: Week 4 Guidance |
| 3.3 | Task assignments are documented clearly in the management board.                                                                                             | ✅        | Task ownership clearly documented on the board. | Week 4 Wednesday Lecture (Lecture 08), Canvas assignment Project: Week 4 Guidance |
| 4   | Each active feature branch has an open draft PR against main.                                                                                                | ⚠️        | Not all active feature branches have open draft PRs; ensure player-class-setup and player-initial-tests have draft PRs opened. | Week 4 Wednesday Lecture (Lecture 08)                                             |
| 5   | The team has a "definition of done" (BVA) fully documented for the part of the system that is done. (needed for Letter Grade D)                              | ⚠️        | BVA documented for Deck class but limited to basic boundary cases; add more methods (Card, Player classes) and deeper white-box boundary transitions. | Project grading rubrics                                                           |
| 6   | GitHub commit history demonstrates evidence of a TDD/BDD workflow for all the non-UI code. (needed for Letter Grade C)                                       | ❌        | No test-first workflow evident; test files are missing and commits show implementation before testing. Begin with unit tests using the BVA analysis as test cases. | Project grading rubrics                                                           |

### Planning & Progress Evaluation
| # | Item                                                                                                                                                         |  Status   | Reviewer Notes      | Source Instructions or Resources                                                  |
|---|--------------------------------------------------------------------------------------------------------------------------------------------------------------|:---------:|---------------------|-----------------------------------------------------------------------------------|
| 7 | The team documents every week's planning and progress evaluation professionally. (needed for Letter Grade B)                                                 | ✅        | Weekly planning and progress documented for Weeks 3–4 with clear task tracking. | Week 4 Wednesday Lecture (Lecture 08), Project grading rubrics                    |

### Progress & Collaboration
| #  | Item                                                                                                                                                             |  Status   | Reviewer Notes      | Source Instructions or Resources                                                  |
|----|------------------------------------------------------------------------------------------------------------------------------------------------------------------|:---------:|---------------------|-----------------------------------------------------------------------------------|
| 8  | README is updated properly, including the project name, contributors, and build status badge. In addition, the team should specify the GUI library if appliable. | ✅        | README updated with contributors and build status badge; GUI library noted where applicable. | Canvas assignment Project: Setup                                                  |
| 9  | Overall progress on "Game Setup Phase"                                                                                                                           | ⚠️        | Core domain classes (Card, Deck, Player) defined in active branches but not merged to `main` yet; use cases and requirements documented. | Canvas assignment Project: Week 4 Guidance                                        |
| 10 | Collaboration: Quality of discussion in PR reviews and work item comments on the board.                                                                          | ✅        | All good!           |                                                                                   |

## Additional Comments
Good progress so far, especially with planning and initial structure. Focus next on strengthening BVA coverage, adopting a clearer test-first workflow, and improving board detail. Once active work is merged into main, things should come together quickly. Keep going 👍