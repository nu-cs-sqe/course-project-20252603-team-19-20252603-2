# Project Use Cases Document

## Player Class

### Data Members
- `playerId`: int — immutable; assigned at construction.
- `hand`: `List<Card>`
- `isAlive`: boolean — true until this player draws an undefused Exploding Kitten.

### Methods
- `Player(int playerId)` — constructor.
- `getPlayerId(): int`
- `addCardToHand(Card)`
- `removeCardFromHand(int index): Card`
- `getHandSize(): int`
- `getCardAt(int index): Card`
- `getHand(): List<Card>` — returns a defensive copy of the hand (`new ArrayList<>(hand)`).
- `hasCard(CardType): boolean`
- `getIndexOfCard(CardType): int`
- `isAlive(): boolean`
- `markDead()`


---

## Card Class

### Data Members
- `cardType`: `CardType` — immutable; set at construction.

### Methods
- `Card(CardType cardType)` — constructor; rejects `null`.
- `getCardType(): CardType`


---

## CardType (enum, supporting Card)

### Values
- `EXPLODING_KITTEN`
- `DEFUSE`
- `ATTACK`
- `SHUFFLE`
- `SKIP`
- `SEE_THE_FUTURE`
- `NOPE`
- `CAT_CARDS`
- `FAVOR`
- `REVERSE`
- `TARGETED_ATTACK`
- `FERAL_CARD`
- `CLONE`
- `SUPER_SKIP`
- `BURY`
- `PERSONAL_ATTACK_3X`

---

## Deck Class

### Data Members
- `drawPile`: `List<Card>`
- `discardPile`: `List<Card>`

### Methods
- `Deck()` — constructor.
- `shuffle()`
- `drawTop(): Card`
- `peekTop(int n): List<Card>` — supports *See the Future*.
- `insertAt(Card, int index): boolean` — supports *Defuse* re-inserting an Exploding Kitten anywhere in the draw pile.
- `discard(Card)`
- `getSize(): int`
- `isEmpty(): boolean`

---

## GameEngine Class

### Data Members
- `numPlayers`: `int` — immutable; supplied at construction; must be in `[2, 5]`.
- `players`: `List<Player>` — one `Player` per id `0..numPlayers-1`.
- `deck`: `Deck` — draw and discard piles; rigged during setup with
  `(numPlayers - 1)` Exploding Kittens and `(6 - numPlayers)` remaining Defuses.
- `turnTracker`: `TurnTracker` — tracks whose turn it is.

### Methods
- `GameEngine(int numPlayers)` — constructor; throws `IllegalArgumentException` if
  `numPlayers` is outside `[2, 5]`. Performs the full setup: builds a deck with
  no Exploding Kittens and only `(6 - numPlayers)` Defuses initially, deals
  `1 Defuse + 4 random` cards to each player, inserts `(numPlayers - 1)`
  Exploding Kittens back into the draw pile, then shuffles.
- `getNumPlayers(): int`
- `getPlayer(int playerId): Player` — throws `IllegalArgumentException` if id
  is outside `[0, numPlayers)`.
- `getCurrentPlayerId(): int`
- `getDrawPileSize(): int`
- `isDeckEmpty(): boolean` — true when the draw pile has no cards left; the UI
  checks this before letting the current player draw.
- `getPlayerHand(int playerId): List<Card>` — defensive copy of the given
  player's hand (delegates to `Player.getHand()`); used by the UI to render a
  hand at game start and on each turn change.
- `drawCardForCurrentPlayer(): Card` — draws the top card of the draw pile,
  adds it to the current player's hand, and returns it. Throws
  `IllegalStateException` (`deck.emptyType`) if the draw pile is empty.
- `advanceToNextPlayer()` — hands the turn to the next living player (skips
  eliminated seats).
- `endTurnByDrawing()` — ends the current turn after a safe (non-kitten) draw:
  consumes one owed turn and, when none remain, advances to the next living
  player. The UI calls this after `drawCardForCurrentPlayer()` returns a
  non-kitten card, so Attack / Targeted Attack stacking is honoured.
- `getDiscardPile(): List<Card>` — defensive copy of the discard pile for the
  UI to render.
- `getDrawPile(): List<Card>` — live view of the draw pile (used by the UI to
  peek the top card before Bury resolution).
- `getForcedTurns(): int` — draws the current player still owes before the turn
  passes (1 normally; raised by Attack / Targeted Attack).
- `getLastPlayedCard(): CardType` — the most recently played card type (what a
  Nope may cancel); `null` before any card is played.
- `playFromHand(CardType type)` — validates the card is playable from the hand
  via `RuleManager.requirePlayable`, discards it from the current player's hand,
  and records it as the last played card; throws `IllegalStateException`
  (`gameEngine.play.notInHand`) when the card is absent; exposed publicly so
  unit tests can cover discard / playability paths used by every `play*` method.
- `playSkip()` — discards a SKIP from the current hand and ends one owed turn
  without drawing.
- `playShuffle()` — discards a SHUFFLE and shuffles the draw pile; same player
  continues.
- `playSeeTheFuture(): List<Card>` — discards a SEE_THE_FUTURE and returns the
  top up-to-3 cards; same player continues.
- `playReverse()` — discards a REVERSE, flips the turn direction, and ends one
  owed turn without drawing.
- `playAttack()` — discards an ATTACK and ends the current turn without drawing;
  the next living player owes `(2 + any stacked turns)` turns.
- `playTargetedAttack(int targetId)` — discards a TARGETED_ATTACK; like Attack
  but the chosen living opponent (not the neighbour) owes the turns.
- `playFavor(int targetId, int cardIndex)` — discards a FAVOR; the target gives
  the card at `cardIndex` to the current player; same player continues.
- `playCatPair(int targetId, List<CardType> selectedCards)` — discards the two
  cards in `selectedCards` (any matching pair; `CAT_CARDS` is the base type when
  any cat is selected; at most one `CLONE` may count toward the pair; `FERAL_CAT`
  and `CLONE` cannot be the base type) and steals one random card from the
  target; records the resolved base type as the last played card; same player
  continues.
- `playCatTriple(int targetId, List<CardType> selectedCards, CardType desiredCard)` —
  discards the three cards in `selectedCards` (same base-type / Clone / Feral Cat
  rules as the pair) and takes `desiredCard` from the target if the target has it
  (otherwise steals nothing); same player continues.
- `playClone(int targetId, int cardIndex): List<Card>` — discards CLONE, gives
  the cloner a copy of `lastPlayedCard`, then re-executes that card's effect
  (`targetId` / `cardIndex` required for Targeted Attack and Favor). Returns the
  See the Future peek list when cloning that card; empty list otherwise. Clears
  `lastPlayedCard`. Throws if nothing to clone or if the last card was CLONE.
- `playSuperSkip()` — discards SUPER_SKIP, advances to the next living player,
  and sets `forcedTurns` to 1.
- `playBury(int index)` — validates `index` against draw-pile size, discards
  BURY, draws the top card, reinserts it at `index`, and consumes one owed turn.
- `playPersonalAttack3X()` — discards PERSONAL_ATTACK_3X and adds 3 to
  `forcedTurns` (current player owes four draws before the turn passes); same
  player continues.
- `playNope(int noperId)` — the noper discards a NOPE to undo the last played
  card based on its type: SKIP/REVERSE return the turn to the player who played
  it (REVERSE also restores direction); ATTACK/TARGETED_ATTACK reduce the forced
  turns and return the turn to the attacker; SEE_THE_FUTURE shuffles the draw
  pile so the peek no longer applies. Other cards are a documented no-op (see
  README design choices). Clears `lastPlayedCard`.
- `defuseDrawnKitten(int reinsertIndex)` — after drawing an Exploding Kitten,
  discards a DEFUSE, reinserts the kitten at `reinsertIndex`, and ends the turn.
- `explodeCurrentPlayer()` — after drawing an Exploding Kitten with no Defuse,
  marks the current player dead, discards their remaining hand, and ends the
  turn (advancing to the next living player).
- `isGameOver(): boolean` — true when only one player is alive or the draw pile
  is exhausted.
- `countAlive(): int` — returns how many players are still alive; used by
  `isGameOver()`; exposed publicly so unit tests can assert the count directly.
- `getWinnerId(): int` — the winner's id; throws `IllegalStateException`
  (`gameEngine.notOver`) if the game is not over. Last player standing, or — on
  an exhausted pile — the living player with the most cards (ties: lowest id).

### UI turn flow (recommended call sequence)

For each turn the UI drives the engine as follows:

1. Show `getPlayerHand(getCurrentPlayerId())` and `getDiscardPile()`.
2. While the current player chooses to play cards, call the matching
   `play*` method (e.g. `playSkip`, `playAttack`, `playFavor`). Each returns
   after validating; catch `IllegalArgumentException` / `IllegalStateException`
   and show the message (resolve the key against the locale bundle).
   - `playSkip`, `playReverse`, `playAttack`, `playTargetedAttack`, `playSuperSkip`
     end the turn themselves — after them, go to step 5.
   - `playBury(int index)` consumes one owed turn (may or may not pass the turn,
     depending on Attack stacking); UI shows a depth slider before calling it.
   - `playPersonalAttack3X()` adds three owed draws; same player continues.
   - `playClone(targetId, cardIndex)` re-runs the last played card; modal target/card
     picks are required when cloning Targeted Attack, Favor, or See the Future.
3. When the player chooses to draw, check `isDeckEmpty()`; if empty the game is
   over (`isGameOver()`), go to step 6.
4. Call `drawCardForCurrentPlayer()`.
   - If the drawn card is `EXPLODING_KITTEN`: call `defuseDrawnKitten(index)`
     when the player has (and plays) a Defuse, otherwise `explodeCurrentPlayer()`.
     Both end the turn.
   - Otherwise call `endTurnByDrawing()` to end the turn (honours Attack
     stacking and skips eliminated players).
5. After any turn-ending action, check `isGameOver()`; if true resolve
   `getWinnerId()` → `AppModel.setWinnerPlayerName()` and navigate to `WinnerView`
   via `ScreenRouter.showWinner()`.
6. Otherwise repeat from step 1 for the new `getCurrentPlayerId()`.

A Nope may be played by any other player via `playNope(noperId)` immediately
after a card is played, before the next action.

---

## ActionController Class

Applies the deck/player-level effect of a played card. Stateless apart from an
injected `Random` (so the Cat-pair random steal is deterministic under test).
Turn-flow effects that change *whose* turn it is or *how many* turns are owed
(Skip, Attack, Targeted Attack) live in `GameEngine`, since they manipulate its
turn-state; `ActionController` only touches the deck and players' hands.

### Data Members
- `random`: `Random` — source of randomness for `stealRandomCard`.

### Methods
- `ActionController()` — production constructor; uses `new Random()`.
- `ActionController(Random random)` — package-private; injects a seeded
  `Random` for deterministic tests.
- `shuffleDeck(Deck deck)` — `deck.shuffle()` (Shuffle card).
- `peekTopThree(Deck deck): List<Card>` — top up to 3 cards (See the Future);
  returns fewer when the draw pile is smaller; no state change.
- `reverseDirection(TurnTracker turnTracker)` — `turnTracker.changeCurrentDirection()`
  (Reverse card).
- `giveCard(Player from, Player to, int cardIndex)` — Favor: removes the card at
  `cardIndex` from `from`'s hand and adds it to `to`'s hand.
- `stealRandomCard(Player from, Player to)` — Cat pair: moves one randomly
  chosen card from `from`'s hand to `to`'s hand; no-op if `from` has no cards.
- `stealDesiredCard(Player from, Player to, CardType desiredCard)` — Cat triple:
  moves the named `desiredCard` from `from` to `to` if `from` has it; otherwise
  a no-op.

---

## TurnTracker Class

### Data Members

- `numTotalPlayers`: `int`
- `currentPlayer`: `int`
- `currentDirection`: `int`

### Methods

- `getNumTotalPlayers()`: `int`
- `setNumTotalPlayers()`
- `getCurrentPlayer()`: `int`
- `getCurrentDirection()`: `int`
- `changeCurrentDirection()`
- `turnGoesToNextPlayer()`
- `turnSkipsNextPlayer()`


---

## RuleManager Class

Pure, stateless validation of whether a play is legal in the current state.
Each method throws (with an i18n key) when the play is illegal and returns
normally otherwise. No mutation; takes domain objects as parameters so it is
trivially unit-testable.

### Methods
- `requirePlayable(CardType type)` — throws `IllegalArgumentException`
  (`rule.play.cannotPlayDirectly`) if `type` is `DEFUSE` or `EXPLODING_KITTEN`
  (those are never played directly from hand).
- `requireValidTarget(Player actor, Player target)` — throws
  `IllegalArgumentException` (`rule.target.invalid`) if `target` is the actor
  themselves or is not alive.
- `requireCatPair(Player actor, CardType cardType)` — throws
  `IllegalStateException` if the pair is illegal: `rule.catPair.feralCannotBeBaseType`
  when `cardType` is `FERAL_CAT`, `rule.catPair.cloneCannotBeBaseType` when
  `cardType` is `CLONE`, or `rule.catPair.needTwo` when the actor holds fewer than
  two matching cards. Counting treats `FERAL_CAT` as a cat when the base type is
  `CAT_CARDS`, and allows at most one `CLONE` to stand in for a missing card.
- `requireCatTriple(Player actor, CardType selectedCard)` — throws
  `IllegalStateException` with the triple analogues of the pair keys above when
  the actor holds fewer than three matching cards (same Feral Cat / Clone counting
  rules).
- `requireSomethingToNope(CardType lastPlayedCard)` — throws
  `IllegalStateException` (`rule.nope.nothingToCancel`) if `lastPlayedCard` is
  `null`.
- `requireSomethingToClone(CardType lastPlayedCard)` — throws
  `IllegalStateException` (`rule.clone.nothingToClone`) if `lastPlayedCard` is
  `null`, or (`rule.clone.cannotCloneClone`) if it is `CLONE`.
- `requireValidInsertIndex(int index, int size)` — throws
  `IllegalStateException` (`rule.bury.invalidIndex`) when `index` is outside
  `[0, size]` (Defuse reinsert and Bury depth selection).

---

## UI Classes

## MainApp Class

Serves as the main entry point and bootstrap coordinator for the JavaFX application lifecycle. 
It instantiates the core data model, wires up all UI views with their respective controllers, 
injects a unified screen navigation router, manages global window sizing boundaries, and initializes 
the root JavaFX Stage.

### Data Members

- `mainWindowWidth`: `int` — default width for the application window (`1000`). 
- `mainWindowHeight`: `int` — default height for the application window (`800`). 
- `mainWindowMinWidth`: `int` — minimum allowed window width boundary to ensure UI layout stability (`600`). 
- `mainWindowMinHeight`: `int` — minimum allowed window height boundary to prevent UI component clipping (`500`).

### Methods

- `start(Stage primaryStage)` — Overridden JavaFX application lifecycle method. 
Configures the centralized `AppModel` and `JavaFxScreenRouter`, instantiates all 
screen views (`StartView`, `InstructionView`, `GameSetupView`, `GameView`, `WinnerView`), wires 
`StartController`, `InstructionController`, `GameSetupController`, `GameController`, and 
`WinnerController`, registers anonymous `ScreenRouter` logic to mutate the `Scene` root during 
navigation (including `showWinner()`), initializes the `Scene` on `StartView`, and brings the 
primary application window into view.
- `main(String[] args)` — Traditional static entry point; delegates execution straight
to the native JavaFX framework's `launch()` sequence.

---

## StartView Class

Represents the landing screen interface for the game. Extends `StackPane` and serves as a 
passive view that sets up the layout hierarchy for the main title components, descriptive labels, 
and navigation buttons. Supports runtime language toggling through an injected `ResourceBundle` 
and delegates user interactions via decoupled action hooks.

### Data Members
- `titleExploding`: `Text` — primary header element for game branding.
- `titleKittens`: `Text` — secondary header element for game branding.
- `subTitleCardGame`: `Text` — subtitle branding label.
- `subLine1`: `Text` — informational description label line 1.
- `subLine2`: `Text` — informational description label line 2.
- `startGameButton`: `Button` — user trigger to initiate the game path.
- `howToPlayButton`: `Button` — user trigger to display game rules.
- `languageButton`: `Button` — user trigger to toggle localization choices.
- `containerWidth`: `int` — constraint cap for main content stack layout ($500$).
- `buttonBoxWidth`: `int` — constraint cap for the navigation buttons stack layout ($320$).
- `containerSpacing`: `int` — vertical layout gap between central containers ($25$).
- `titleBoxSpacing`: `int` — tight layout overlap adjustment for title elements ($-10$).
- `descriptionBoxSpacing`: `int` — vertical layout gap for information texts ($10$).
- `buttonBoxSpacing`: `int` — vertical layout gap between buttons ($20$).

### Methods
- `StartView()` — view constructor; configures layouts, calls sub-component initialization 
  factory pipelines, and hooks up external styling sheets (`start-style.css`).
- `createTitle(): VBox` — helper factory; structures and styles the main game logo titles.
- `createDescription(): VBox` — helper factory; organizes the subtitle and rule description rows.
- `createButtons(): VBox` — helper factory; instantiates control buttons, stretches their scaling 
  layout properties, and loads CSS style hooks.
- `updateDisplay(ResourceBundle bundle)` — pulls real-time text mappings from properties files to 
  update UI elements dynamically for internationalization.
- `setOnStartGameAction(Runnable handler)` — hooks up the trigger handler callback for the game 
  initialization action pathway.
- `setOnHowToPlayAction(Runnable handler)` — hooks up the trigger handler callback for the instructional
  layout route.
- `setOnLanguageAction(Runnable handler)` — hooks up the trigger handler callback for altering application
  language profiles.

--- 

## InstructionView Class

Represents the rulebook and instructions screen interface for the game. 
Extends `StackPane` and serves as a passive view that renders a structured 
list of game rules inside a bounded, styled layout container. 
Dynamically populates localized rule titles and body text segments 
from an injected `ResourceBundle` at runtime, ensuring complete isolation
between layout components and application controllers.

### Data Members
- `instructionsTitle`: `Text` — header element displaying the screen's main title.
- `backButton`: `Button` — user trigger to navigate back to the previous screen path.
- `ruleSection`: `VBox` — container panel that vertically stacks all rule blocks.
- `ruleKeys`: `String[]` — constant identification array keys tracking internal instruction 
  text files (`ruleOne` through `ruleFour`).
- `instructionsContainerSpacing`: `int` — vertical gap spacing separating the main view components ($25$).
- `instructionsContainerHeight`: `int` — fixed target height dimension for the central outer container panel ($650$).
- `instructionsContainerWidth`: `int` — fixed target width dimension for the central outer container panel ($950$).
- `ruleSpacing`: `int` — vertical layout gap between an individual rule's title and its body description ($10$).
- `ruleSectionSpacing`: `int` — vertical layout layout gap separating distinct rule paragraphs ($30$).
- `ruleBodyWrapping`: `int` — target boundary width constraint used to force rule text lines to wrap cleanly ($850$).

### Methods
- `InstructionView()` — view constructor; configures dimension bounds, generates geometric visibility 
  clipping shapes, bundles header and paragraph elements into the main view hierarchy, and loads 
  external style sheets (`instruction-style.css`).
- `createTopBar(): BorderPane` — helper factory; isolates header formatting by anchoring the page title 
  text and navigation buttons along split layout axes.
- `createRule(String titleKey, String bodyKey, ResourceBundle bundle): VBox` — helper factory; instantiates 
  a standardized text segment cluster containing a styled title and automated paragraph wrapping blocks.
- `updateDisplay(ResourceBundle bundle)` — updates localized layout banners dynamically on demand and runs 
  a processing loop across `ruleKeys` to completely clear, rebuild, and append fresh translated text sequences.
- `setOnBackAction(Runnable handler)` — hooks up the trigger handler callback for the navigation regression 
  pathway.

---

## GameSetupView Class

Represents the pre-game setup screen interface where players choose the number of competitors and input their display names. Extends `StackPane` and acts as a passive view that manages dynamic rows of input fields based on numerical button selections. Exposes raw string collection hooks to controllers for game initialization and relies on an injected `ResourceBundle` for runtime localization updates.

### Data Members
- `textFields`: `List<TextField>` — active collection tracking the input elements where players type their names.
- `gameSetupContainer`: `VBox` — root structural container holding the layout elements.
- `playerNameSection`: `VBox` — contextual container panel that dynamically appends or removes player input blocks.
- `playerButtonSection`: `HBox` — horizontal bar housing the player count selector buttons.
- `gameSetupTitle`: `Text` — header element displaying the screen's main title banner.
- `totalPlayerLabel`: `Text` — subsection header label for player count selection.
- `whosePlayingHeading`: `Text` — subsection header label for the player name inputs.
- `backButton`: `Button` — user trigger to regress to the previous navigation view.
- `launchButton`: `Button` — user trigger to process configurations and boot the game match.
- `minPlayerCount`: `int` — minimum supported player boundary limit ($2$).
- `maxPlayerCount`: `int` — maximum supported player boundary limit ($5$).
- `gameSetupContainerSpacing`: `int` — layout gap spacing separating the primary panels ($15$).
- `gameSetupContainerHeight`: `int` — default static height constraint for the setup menu box ($700$).
- `gameSetupContainerWidth`: `int` — default static width constraint for the setup menu box ($600$).
- `gameSetupTranslateY`: `int` — visual alignment offset applied to all nested containers ($-15$).
- `playerSelectionSectionSpacing`: `int` — vertical layout gap inside the player count header row ($5$).
- `playerButtonSectionSpacing`: `int` — horizontal layout gap separating the numeric count toggles ($15$).
- `playerSectionSpacing`: `int` — vertical layout gap inside the name input section wrapper ($15$).
- `playerNameSectionSpacing`: `int` — vertical layout gap separating distinct player name rows ($15$).
- `playerNameSpacing`: `int` — layout gap separating a player row's label from its corresponding `TextField` ($5$).

### Methods
- `GameSetupView()` — view constructor; initializes layout dimensions, configures component visibility 
  clipping fields, loops children nodes to apply baseline Y-axis translation offsets, and binds external 
  stylesheets (`game-setup-style.css`).
- `createTopBar(): BorderPane` — helper factory; formats and partitions the screen title string and
  navigation back buttons along split layout axes.
- `createPlayerCountButton(int count, int selectedCount): Button` — helper factory; creates a numeric button 
  and binds its appropriate toggle styling classes (`selected` vs `unselected`) matching current selection counts.
- `updatePlayerCountButtons(int selectedCount, IntConsumer onClick)` — completely flushes, loops, and rebuilds 
  the active navigation selector button bank, injecting an functional consumer callback for state capture.
- `createPlayerSelectionSection(): VBox` — helper factory; bundles the player selection description label with 
  its child button row container.
- `createPlayerName(String playerNumber, ResourceBundle bundle): VBox` — helper factory; instantiates a specific
  player row box containing its unique input tracking label, a localized placeholder string field, and logs the generated 
  node straight into `textFields`.
- `updatePlayerNameSection(int selectedCount, ResourceBundle bundle)` — layout pipeline matrix; wipes out stale 
  player name blocks from memory and runs an iterative loop up to `selectedCount` to construct and hook up fresh input
  fields dynamically.
- `createPlayerSection(): VBox` — helper factory; groups the input prompt headers with the dynamic `playerNameSection`
  holder stack.
- `createLaunchButton(): Button` — helper factory; builds the start button trigger element and stretches it to inherit 
  maximum parent layout widths.
- `updateDisplay(ResourceBundle bundle)` — grabs real-time translation property keys to overwrite text banners 
  and menu prompt fields instantly on the fly.
- `updateSetupContainerHeight(int heightChange)` — utility macro; dynamically grows or shrinks the visual 
  container dimensions to keep alignment looking uniform when names are added or removed.
- `setOnBackAction(Runnable handler)` — hooks up the trigger handler callback for the navigation regression
  pathway.
- `setOnLaunchAction(Runnable handler)` — hooks up the trigger handler callback for compiling setup details 
  and executing the game launch.
- `getRawPlayerNameInputs(): List<String>` — scanner loop; parses the collection of `textFields` sequentially 
  to extract raw text data inputs, returning them as a clean string list back to the parent controller layer.

---

## GameView Class

Represents the primary tabletop gameplay screen interface where matches are conducted. 
Extends `StackPane` and serves as a comprehensive visual manager for opponent avatars, 
the draw deck, the live chatter logs, the discard pile stack, and the player's physical 
hand tracker. Manages an active multi-card selection state machine (`selectedHandCards`) 
restricted to homogeneous card types to support matching pair/triplet combo mechanics cleanly.

### Data Members
- `cardCountText`: `String` — localized string label text for card counts.
- `cardsText`: `String` — localized string label text for deck counts.
- `cardCollection`: `Map<CardType, String>` — internal map linking structural domain card types to asset layout paths.
- `cards`: `String[]` — string matrix indexing matching asset identifiers.
- `topBar`: `BorderPane` — header panel framing the main game title and utility controllers.
- `playerBar`: `HBox` — horizontal display grid housing opponent status modules.
- `gamePlaySection`: `HBox` — main central board layout framing the deck, chatter, and discard pile.
- `cardSection`: `HBox` — bottom dock formatting hand metrics, card tracks, and buttons.
- `playerHandSection`: `HBox` — dynamic row rendering the active cards held in the local player's hand.
- `handScrollContent`: `HBox` — centering wrapper around `playerHandSection`; `minWidth` binds to `max(viewport, hand width)` so few cards center in the bar.
- `handScrollPane`: `ScrollPane` — horizontal-only hand viewport (`.hand-cards-col-2`); pannable left/right, vertical scroll blocked, `vvalue` pinned to `0`.
- `modalOverlayScreen`: `VBox` — shared backdrop for See the Future, Targeted Attack, Favor, and double special-combo modals.
- `tripleComboOverlayScreen`: `VBox` — dedicated backdrop for the three-of-a-kind special combo (`.combo-three-overlay`).
- `defuseModal`: `DepthSliderModal` — shared depth-slider modal instance configured with
  Defuse styling/copy, a primary Defuse button, and the extra Explode button.
- `buryModal`: `DepthSliderModal` — shared depth-slider modal instance configured with
  Bury styling/copy and a primary Bury confirm button.
- `DepthSliderModal`: private static holder for the shared slider-modal nodes (`overlay`,
  `dialog`, `section`, `sliderBody`, `sliderInfo`, `slider`, title/subtitle/label texts,
  primary button, localized label text, and max slider index).
- `buryCard`: `VBox` — Bury-specific card preview slot inserted into `buryModal`.
- `modalDialogScreen`, `modalCardRow`, `modalCardScroll`, `modalPlayerButtons` — reusable modal chrome for card/target selection flows.
- `tripleComboCard`, `cardGuessComboBox`, `tripleTargetButtons` — triple-combo UI (title, `ComboBox<CardType>` guess, per-opponent target buttons).
- `feedContainer`: `VBox` — vertical logging module appending incoming event feeds.
- `discardPile`: `VBox` — container stack managing the visual properties of discarded card layouts.
- `scrollPane`: `ScrollPane` — container scrolling window wrapping around the live chatter engine.
- `discardPileSection`: `StackPane` — structural grouping pane superimposing discarded files.
- `logoText`: `Text` — game branding header.
- `deckTitleText`: `Text` — layout descriptor banner for the draw stack.
- `turnIndicatorText`: `Text` — state tracker displaying whose turn is active.
- `tableChatterTitle`: `Text` — section header for the scrolling feed panel.
- `discardPileFooterText`: `Text` — section description label underneath the discard deck.
- `deckCountLabel`: `Label` — numeric indicator rendering remaining deck sizes.
- `localHandLabel`: `Label` — tracker showing local player inventory dimensions.
- `quitButton`: `Button` — menu trigger to terminate active matches.
- `deck`: `Button` — underlying button engine managing interaction on the draw pile.
- `drawCard`: `Button` — supplementary action button facilitating player draw commands.
- `playCardButton`: `Button` — action trigger validating and processing selected hand combinations.
- `selectedHandCards`: `List<CardView>` — active tracking matrix collecting card nodes selected for play.
- `topBarRightSpacing`: `int` — layout gap spacing for header right items ($20$).
- `playerBarSpacing`: `int` — alignment padding separating opponent elements ($25$).
- `playerSpacing`: `int` — baseline gap separating opponent info components ($5$).
- `deckInfoSpacing`: `int` — layout separation space for deck badge metadata items ($20$).
- `drawDeckSpacing`: `int` — alignment spacing separating draw buttons from card shapes ($25$).
- `gamePlaySectionSpacing`: `int` — grand layout division separating center-felt anchors ($100$).
- `tableChatterInfoSpacing`: `int` — header spacing inside the chatter box ($5$).
- `feedContainerSpacing`: `int` — vertical row padding inside the live logger stream ($5$).
- `tableChatterSectionSpacing`: `int` — container layout padding across structural box parts ($10$).
- `playerEventLogSpacing`: `int` — horizontal tracking gap inside chatter row arrays ($8$).
- `discardCardWidth`: `int` — explicit scale boundary width for discarded cards ($175$).
- `discardCardHeight`: `int` — explicit scale boundary height for discarded cards ($260$).

### Methods
- `GameView()` — class constructor; instantiates lookup collections, initializes layout wrappers 
  into a centralized sequence tracker, binds style properties (`game-style.css`), and adds automated 
  snapping listeners onto scroll dimensions.
- `createTopBarLeft(): HBox` — helper factory; initializes and formats game branding text blocks.
- `createTurnIndication(): VBox` — helper factory; sets up styling blocks for game status monitors.
- `createQuitButton(): Button` — helper factory; constructs and styles the termination handler.
- `createTopBarRight(): HBox` — helper factory; joins game trackers with exit buttons inside a single layout cell.
- `createTopBar(): BorderPane` — layout splitter; aggregates left logo components and right tracking cells.
- `createPlayerAvatar(String playerName): VBox` — graphics helper; creates customized icon circles from 
  initial character letters.
- `createPlayerAvatarLabel(String playerName): Text` — graphics helper; structures standardized user
  tracking banners.
- `createPlayerAvatarCardCount(int cardCount): Label` — graphics helper; formats string trackers rendering 
  relative hands.
- `createPlayer(PlayerDisplayInfo opponent): VBox` — component engine; shapes a player module, applying 
  conditional tracking highlights if the target user possesses the active turn tracker.
- `createPlayerSection(): VBox` — alignment wrapper; centers and formats the opponent collection dashboard.
- `createDeckInfo(): VBox` — metadata factory; bundles descriptive strings with numeric pill labels.
- `createDeck(VBox infoOverlay): StackPane` — graphics matrix; piles layered visual shapes atop active 
  click surfaces to replicate a tactile card deck appearance.
- `createDrawCard(): Button` — action factory; creates the draw trigger framework.
- `createDrawDeck(): VBox` — aggregation loop; bundles geometric stack panels with secondary draw targets.
- `createTableChatterTitle(): Text` — element factory; builds localized header panels.
- `createTableChatterInfo(): VBox` — component engine; stacks header strings alongside divider separators.
- `createTableChatterView(): VBox` — layout generator; creates smooth scrolling wrappers over structural
  notification feeds.
- `createTablechatter(): StackPane` — module factory; wraps scrolling panes inside styled structural panels.
- `createDiscardPile(): StackPane` — module factory; frames discard backgrounds with footer descriptor bars.
- `createGamePlaySection(): HBox` — macro assembler; layouts the board surface, integrating draw piles, 
  feed trackers, and discard piles.
- `createHandLabelSection(): VBox` — component factory; configures local user hand title tracking rows.
- `createPlayerHandSection(): ScrollPane` — builds `handScrollPane` over `handScrollContent` + 
  `playerHandSection`; horizontal pan only, blocks vertical wheel/drag, recenters on hand refresh.
- `centerHandScrollInitially()` — after `updatePlayerCards`, sets `hvalue` to `0` when the hand fits 
  the viewport or `0.5` when it overflows (starts centered on wide hands).
- `createTripleComboOverlay(): VBox` — constructs the three-of-a-kind modal (`combo-three-card` CSS).
- `createPlayCardButton()` — element factory; generates main card triggers and forces initial fallback 
  disable constraints.
- `createPlayCardSection(): VBox` — alignment wrapper; maps out the layout area for execution buttons.
- `createCardSection(): HBox` — macro assembler; organizes layout panels for metrics, tracking fields, 
  and selection buttons.
- `updateDisplay(ResourceBundle bundle)` — reads translation key files to update static layout textual 
  headers dynamically on the fly.
- `showOpponents(List<PlayerDisplayInfo> opponents)` — layout flush loop; completely empties the visual 
  opponent tracks and maps down updated tracking profiles sequentially.
- `updateDeckCount(int count)` — updates string counts rendering remaining card inventories.
- `updateCardCount(int cardCount)` — wrapper utility; routes count details to refresh deck sizes uniformly.
- `updatePlayerTurn(ResourceBundle bundle, String player)` — string processor; builds and prints strings
  updating active turn fields.
- `updateHandCount(int handSize, String playerName)` — string processor; builds and prints local user 
  inventory lengths.
- `updateDrawCount(ResourceBundle bundle, int drawCount)` — updates the draw button label to
  `{gameView.drawCard} X{drawCount}` (forced-turn counter from `GameEngine.getForcedTurns()`).
- `updatePlayerCards(List<Card> hand)` — layout flush loop; resets user hand panels, maps cards, then 
  calls `centerHandScrollInitially()`.
- `showDemandFavorScreen()` / `hideDemandFavorScreen()` — shared modal for Favor target pick 
  (`.favor-request-box`).
- `showGrantFavorScreen()` / `hideGrantFavorScreen()` — shared modal for granting a card from the 
  target's hand (`.favor-grant-box` + `modalCardScroll`).
- `updateDemandFavorPlayers(List<PlayerDisplayInfo>, IntConsumer)` — populates living-opponent target 
  buttons for demand Favor.
- `updateGrantFavorSubTitle(ResourceBundle, String fromPlayer, String toPlayer)` — builds grant-step 
  subtitle text.
- `updateFavorCards(List<Card>, IntConsumer)` — renders opponent hand cards as clickable 
  `.favor-select-card` nodes in `modalCardRow`.
- `showDoubleSpecialComboScreen()` / `hideCatCardScreen()` — two-of-a-kind special combo modal.
- `showTripleSpecialComboScreen()` / `hideTripleSpecialComboScreen()` — shows/hides dedicated triple 
  combo overlay.
- `updateTripleComboScreen(ResourceBundle, List<PlayerDisplayInfo>, BiConsumer<Integer, CardType>)` — 
  fills triple combo UI: shared `catCardSpecialCombo` title/subtitle, `guessLabel`, `ComboBox` of all 
  `CardType` except `EXPLODING_KITTEN` (default `DEFUSE`), and gold target buttons per opponent.
- `showSeeTheFutureScreen()` / `hideSeeTheFutureScreen()` — See the Future peek modal.
- `updateSeeTheFutureCards(ResourceBundle, List<Card>)` — renders peeked cards in `modalCardRow`.
- `showTargetedAttackScreen()` / `hideTargetedAttackScreen()` — Targeted Attack victim picker.
- `updateTargetedAttackPlayers(List<PlayerDisplayInfo>, IntConsumer)` — opponent buttons for 
  Targeted Attack.
- `updateCatCardsPlayer(List<PlayerDisplayInfo>, IntConsumer)` — opponent buttons for double 
  special combo.
- `showDefuseScreen(ResourceBundle, int deckSize)` / `hideDefuseScreen()` — defuse slider overlay.
- `showBuryScreen(ResourceBundle, int deckSize)` / `hideBuryScreen()` — Bury slider overlay;
  `deckSize` is post-draw pile size minus one (same convention as defuse).
- `updateBuryCard(CardType card)` — renders the drawn top card in the Bury modal.
- `createDepthSliderModal(...)` — shared factory for Defuse/Bury modal chrome, including
  overlay, dialog, section, slider title, slider, slider labels, and primary button.
- `createDefuseModal()` / `createBuryModal()` — configure the shared depth modal with
  mode-specific CSS classes and add mode-specific content (Explode button or Bury card preview).
- `updateDepthSlider(...)` / `updateDepthSliderLabels(...)` / `showDepthModal(...)` /
  `hideDepthModal(...)` — shared depth-slider state and visibility helpers.
- `setOnSeeTheFutureDismissButton(Runnable)` / `setOnDefuseButton(IntConsumer)` / 
  `setOnExplodeButton(Runnable)` / `setOnBuryButton(IntConsumer)` — modal action hooks.
- `selectCard(CardView card)` — layout modifier; shifts visual item properties to tracking selections 
  and updates action button disable states.
- `deselectCard(CardView card)` — layout modifier; resets targeted item style definitions back to 
  standard hand classes.
- `clearSelection()` — state clearing loop; completely strips active selections, sweeps state lists,
  and disables trigger buttons.
- `handleCardSelection(CardView playerCard)` — core event decision manager; checks for targeted item 
  removals, verifies group typing alignments, and updates active card states without accidental logic fall-through.
- `addPlayerCard(Card card)` — structural mapper; extracts asset properties to build fresh layout tracks 
  and registers interactive click listeners.
- `clearLog()` — feed routine; completely wipes the live logging stream.
- `addLog(String message)` — feed routine; builds, styles, and appends a fresh event row tracking standard 
  match details.
- `setOnQuitAction(Runnable handler)` — hooks up structural callback triggers managing match terminations.
- `clearDiscardCard()` — visual flush; completely empties the discard layout wrapper.
- `addCardToDiscardPile(CardView card)` — layout overrides; recalculates dimensions, handles layout 
  transformations, modifies tracking boundaries, and appends the card directly into discard arrays.
- `removeCardFromHand()` — inventory tracking sweep; loops and removes active item sets from UI trees
  before purging reference memory collections.
- `setOnDrawAction(Runnable handler)` — maps action callbacks onto interaction points across the draw deck.
- `setOnPlayButtonAction(Consumer<List<CardView>> handler)` — injects action callbacks to pass collection 
  clusters to the tracking execution layers.

--- 

## CardView Class

Represents the physical, visual instantiation of an individual game card within the user interface layers.
Extends `StackPane` and acts as a passive component responsible for parsing card identifiers, 
performing randomized asset path retrievals via `CardServices`, and masking incoming raster 
images into a standardized container. Encapsulates fixed geometric dimensions, rendering clip 
frames for rounded aesthetic profiles, and explicit type lookups to ensure safe tracking across 
multi-selection UI operations.

### Data Members
- `imageCountDict`: `Map<String, Integer>` — internal dictionary mapping card identity tags to their 
  total corresponding available variations.
- `cardNameToType`: `Map<String, CardType>` — internal translation map resolving raw text strings into domain-level `CardType` enumerations.
- `imageWidth`: `int` — strict layout width constraint assigned to the rendered graphics layer ($120$).
- `imageHeight`: `int` — strict layout height constraint assigned to the rendered graphics layer ($160$).
- `cardCorner`: `int` — rounding geometric arc constraint applied to the bounding visibility clipping mask ($12$).
- `cardName`: `String` — raw textual name identifier for the specific card instance.
- `cardType`: `CardType` — structural domain type token linked with the card's action rules.

### Methods
- `CardView(String card)` — constructor; resolves raw string entries into formal types, requests 
  deterministic randomized paths from internal assets, loads raw input streams into an optimized 
  asynchronous JavaFX `Image`, binds dimensions, constructs a structural rounded `Rectangle` clip path mask, 
  and registers styling classes.
- `getCardName(): String` — data getter; reveals the card instance's raw string identity tag.
- `getCardName(ResourceBundle bundle): String` — localized display name via `cardView.{cardName}` keys.
- `getCardType(): CardType` — data getter; reveals the card instance's structural domain rule type token.

---

## CardServices Class

Represents a stateless utility layer responsible for constructing deterministic, 
classpath-relative image asset paths for card face artwork. Validates incoming 
card identity strings against a fixed catalog before synthesizing randomized file 
names via an injected `Random` instance and a caller-supplied per-type file count. 
Consumed exclusively by `CardView` during visual instantiation.

### Data Members
- `rootPath`: `String` — base classpath prefix for all card asset directories (`/assets/`).
- `cards`: `String[]` — canonical whitelist of supported card folder identity tags
  (includes expansion cards: `Clone`, `SuperSkip`, `Bury`, `PersonalAttack3X`, `FeralCat`).
- `invalidCardName`: `String` — i18n message key emitted when validation rejects an unknown card name (`cardServices.cardDoesNotExist`).

### Methods
- `validateCardName(String cardName)` — private guard; throws `IllegalArgumentException` when the supplied name is absent from the internal catalog.
- `getRandomCardImage(Random random, String cardName, int fileCount): String` — public path builder; validates the card name, selects a zero-based random index bounded by `fileCount`, and returns a classpath path of the form `/assets/{cardName}/{cardName}{n}.png`.

---

## StartController Class

Represents the controller coordinating events between `StartView` and cross-screen 
shared state. Acts as passive glue: registers navigation and localization callbacks 
on the landing view without owning layout nodes or domain rules.

### Data Members
- None (constructor-scoped wiring only; dependencies remain local parameters).

### Methods
- `StartController(StartView view, AppModel appModel, ScreenRouter router)` — wires `setOnStartGameAction` to `router.showGameSetup`, `setOnHowToPlayAction` to `router.showInstructions`, `setOnLanguageAction` to toggle locale via `appModel` and refresh view text, then runs an initial `updateDisplay` with the active `ResourceBundle`.

---

## InstructionController Class

Represents the controller coordinating events between `InstructionView` and navigation. 
Maintains a reusable refresh pipeline so rule text rebinds whenever the instructions 
screen is shown or the active locale changes.

### Data Members
- `refreshAction`: `Runnable` — encapsulated callback that pushes the current `AppModel` `ResourceBundle` into `InstructionView.updateDisplay`.

### Methods
- `InstructionController(InstructionView view, AppModel appModel, ScreenRouter router)` — assigns `refreshAction`, hooks `setOnBackAction` to `router.showStart`, and runs an initial refresh.
- `refreshView()` — re-executes `refreshAction`; invoked by `MainApp` before displaying the instructions root.

---

## GameSetupController Class

Represents the controller coordinating events between `GameSetupView`, `AppModel`, and 
screen navigation during pre-match configuration. Owns setup-screen refresh and launch 
pipelines without storing references to mutable view or model instances as fields 
(beyond `Runnable` actions).

### Data Members
- `HEIGHT_CHANGE_PER_PLAYER`: `int` — layout delta applied per player-count change when resizing the setup container ($75$).
- `refreshAction`: `Runnable` — rebuilds localized labels, player-count buttons, and dynamic name rows from `AppModel` state.
- `launchAction`: `Runnable` — captures raw name inputs, normalizes them through `AppModel.capturePlayerNamesFromInputs`, and navigates to the game screen.

### Methods
- `GameSetupController(GameSetupView view, AppModel appModel, ScreenRouter router)` — constructs refresh and launch actions, registers back/launch handlers, and runs initial refresh.
- `refreshView()` — re-executes `refreshAction`; invoked when entering the setup screen or after player-count selection changes.

---

## GameController Class

Represents the controller coordinating the active match between `GameView`, `AppModel`, 
and an owned `GameModel`. Encapsulates draw, play, modal card flows, defuse/explode 
resolution, game-over navigation, and full-board refresh while delegating domain rules 
to `GameEngine` through the model layer.

### Data Members
- `model`: `GameModel` — per-match UI state facade wrapping `GameEngine` and display names.
- `refreshAction`: `Runnable` — synchronizes localized chrome, local hand, opponents, deck count, turn banner, forced-turn count, and log/discard reset when a game is active.
- `startGameAction`: `Runnable` — boots the engine from `AppModel.getPlayerNames()` then runs `refreshAction`.
- `CAT_PAIR_SIZE`: `int` — hand selection count ($2$) that routes to double special combo.
- `CAT_TRIPLE_SIZE`: `int` — hand selection count ($3$) that routes to triple special combo.

### Methods
- `GameController(GameView view, AppModel appModel, ScreenRouter router)` — instantiates `GameModel`, wires quit/draw/play/defuse/explode/bury/see-the-future-dismiss handlers, and defines refresh/start pipelines.
- `startGame()` — executes `startGameAction`; invoked by `MainApp` when navigating to the game screen (boots engine and runs `refreshAction`).
- `handleGameOver(AppModel, ScreenRouter)` — when `model.isGameOver()`, stores winner display name in `AppModel` and calls `router.showWinner()`.
- `handleExplodingKitten(GameView, AppModel)` — on drawn `EXPLODING_KITTEN`: opens defuse slider when local seat holds Defuse, otherwise calls `model.explodeCurrentPlayer()`.
- `playCard(List<CardView>, GameView, AppModel)` — dispatches by selection size and `CardType`; early-returns for two- or three-card cat combos before generic single-card switch.
- `playCatPair` / `playCatTriple` — show double/triple combo modals, discard selected cards on confirm, delegate to `model.playCatPair` / `model.playCatTriple`, then `refreshAfterPlay`.
- `playFavor` — two-step modal: demand target (`livingOpponents()`), then grant step showing `model.getSelectedHand(targetId)` for card pick.
- `playTargetedAttack` — victim picker over living opponents; discards and refreshes on confirm.
- `playSeeTheFuture` — discards card, peeks top three via `model.playSeeTheFuture()`, opens peek modal.
- `playSkip` / `playReverse` / `playAttack` / `playShuffle` / `playNope` — discard then delegate to matching `GameModel.play*` method.
- `playClone` — branches on `model.getLastPlayedCard()`: Targeted Attack and Favor reopen their
  target/card modals; See the Future opens the peek modal with the cloned peek; other types resolve
  immediately. Always discards the Clone card afterward.
- `playSuperSkip` / `playPersonalAttack3X` — discard and delegate to `GameModel`.
- `playBury` — peeks top draw-pile card via `model.peekTopCardForBury()`, opens Bury slider; confirm
  handled by `setOnBuryButton` (calls `model.playBury`, hides overlay, refreshes, discards Bury card).
- `livingOpponents()` — filters `model.getOpponents()` to seats where `playerId != localPlayerId` and `isAlive`.
- `refreshAfterPlay(GameView, AppModel)` — syncs hand, deck count, forced turns, turn banner, and opponent row after a play resolves.
- `discardCard` / `computeLog` — moves played `CardView` nodes to discard pile and formats play log lines.

---

## AppModel Class

Represents the application-wide presentation model spanning multiple screens. Centralizes 
locale selection, `ResourceBundle` resolution, and delegation to `GameSetupModel` for 
pre-game player metadata. Shared by every controller that requires i18n or setup data.

### Data Members
- `BUNDLE_BASE_NAME`: `String` — properties bundle base name (`message`).
- `ENGLISH`: `Locale` — default locale constant (`Locale.ENGLISH`).
- `CHINESE`: `Locale` — alternate locale constant (`Locale.SIMPLIFIED_CHINESE`).
- `selectedLocale`: `Locale` — active user-facing locale; toggled at runtime.
- `setupModel`: `GameSetupModel` — encapsulated setup-state store for player count and captured names.
- `winnerPlayerName`: `String` — display name of match winner; set on game over, read by `WinnerController`.

### Methods
- `toggleLanguage()` — flips `selectedLocale` between English and Simplified Chinese.
- `getResourceBundle(): ResourceBundle` — loads `message` properties for `selectedLocale`.
- `getSelectedLocale(): Locale` — returns the active locale token.
- `getNumberPlayer(): int` — delegates to `setupModel.getNumberPlayer()`.
- `setNumberPlayer(int playerCount)` — delegates to `setupModel.setNumberPlayer(playerCount)`.
- `getPlayerNames(): List<String>` — delegates to `setupModel.getPlayerNames()` (defensive copy).
- `capturePlayerNamesFromInputs(List<String> rawInputs, String defaultNamePrefix)` — delegates name normalization and validation to `setupModel`.
- `setWinnerPlayerName(String)` / `getWinnerPlayerName(): String` — store and retrieve the winner label for the end screen.

---

## GameSetupModel Class

Represents pre-game configuration state: selected competitor count and finalized display 
names. Enforces list-size boundaries on captured inputs and applies trim/default-name rules 
before persisting player labels for match initialization.

### Data Members
- `numberPlayer`: `int` — currently selected player count for the upcoming match.
- `playerNames`: `List<String>` — stored display names after capture normalization.
- `initialPlayerCount`: `int` — default count assigned at construction ($4$).
- `minPlayerCount`: `int` — minimum allowed captured input list size ($2$).
- `maxPlayerCount`: `int` — maximum allowed captured input list size ($5$).
- `TOO_FEW_PLAYERS`: `String` — exception message key when input list is too short.
- `TOO_MANY_PLAYERS`: `String` — exception message key when input list is too long.

### Methods
- `GameSetupModel()` — initializes `numberPlayer` to `initialPlayerCount` and an empty name list.
- `checkPlayerNameInputs(List<String> rawInputs)` — private validator; throws `IllegalArgumentException` when list size violates min/max bounds.
- `capturePlayerNamesFromInputs(List<String> rawInputs, String defaultNamePrefix)` — validates size, trims each entry, substitutes `{prefix} {n}` for blanks, and stores the result.
- `getPlayerNames(): List<String>` — returns a defensive copy of stored names.
- `setNumberPlayer(int playerCount)` — assigns `numberPlayer`.
- `getNumberPlayer(): int` — returns `numberPlayer`.

---

## GameModel Class

Represents in-match UI state for the local human player and opponent display projections. 
Owns a `GameEngine` instance and player name list after `startGame`; exposes draw, play, 
turn tracking, and hand queries without embedding JavaFX dependencies.

### Data Members
- `localPlayerId`: `int` — index of the human-controlled seat; updated when turns advance ($0$ at match start).
- `engine`: `GameEngine` — domain match engine; `null` until `startGame` completes.
- `playerNames`: `List<String>` — defensive copy of display names aligned with engine player indices.

### Methods
- `startGame(List<String> playerNames)` — copies names and constructs `GameEngine` for `playerNames.size()`.
- `isGameStarted(): boolean` — reports whether `engine` has been initialized.
- `isDeckEmpty(): boolean` — delegates to `engine.isDeckEmpty()` for UI draw guards.
- `drawCard(): Card` — delegates to `engine.drawCardForCurrentPlayer()`.
- `getDeckSize(): int` — delegates to `engine.getDrawPileSize()`.
- `endTurnByDrawing()` — delegates to `engine.endTurnByDrawing()` and syncs `localPlayerId`.
- `playSkip()` / `playReverse()` / `playAttack()` / `playShuffle()` — action delegates; each syncs `localPlayerId` after engine call.
- `playSeeTheFuture(): List<Card>` — delegates peek to engine.
- `playTargetedAttack(int targetId)` / `playFavor(int targetId, int cardIndex)` / `playNope()` — targeted and reactive play delegates.
- `playCatPair(int targetId, List<CardType> selectedCards)` /
  `playCatTriple(int targetId, List<CardType> selectedCards, CardType desired)` — special-combo steals.
- `playClone(int targetId, int cardIndex): List<Card>` / `playSuperSkip()` /
  `playPersonalAttack3X()` / `playBury(int index)` — expansion-card delegates; each syncs
  `localPlayerId` after the engine call.
- `getLastPlayedCard(): CardType` — delegates to engine (Clone eligibility).
- `peekTopCardForBury(): CardType` — reads the top draw-pile card type before Bury reinsert.
- `defuseExplodingKitten(int reinsertIndex)` / `explodeCurrentPlayer()` — Exploding Kitten resolution paths.
- `isGameOver(): boolean` / `getWinnerId(): int` — end-state queries from engine.
- `getForcedTurns(): int` — remaining forced draws for the local UI turn banner.
- `getLocalPlayerId(): int` — current human seat index tracked for modal filtering.
- `currentPlayerHasDefuse(): boolean` — whether the engine's current player holds Defuse (draw-time check).
- `getPlayerName(int playerId): String` — resolves a seat's display label.
- `getSelectedHand(int playerId): List<Card>` — full hand of any seat (Favor grant step).
- `getLocalHand(): List<Card>` — returns the local player's hand from the engine.
- `getLocalHandSize(): int` — returns local hand list size.
- `getLocalPlayerName(): String` — resolves the display name for `localPlayerId`.
- `resetPlayerId()` — resets `localPlayerId` to $0$ before a full UI refresh.
- `getOpponents(): List<PlayerDisplayInfo>` — maps every seated player into a display DTO for `GameView.showOpponents`.
- `toDisplayInfo(int playerId): PlayerDisplayInfo` — private mapper; bundles name, hand size, `playerId`, current-turn flag, and alive state.

---

## PlayerDisplayInfo Class

Represents a per-seat snapshot for `GameView` opponent rows and modal target pickers. 
Mutable turn/alive flags; name, hand size, and `playerId` fixed at construction.

### Data Members
- `name`: `String` — player display label.
- `handSize`: `int` — number of cards currently held (face-down count for opponents).
- `playerId`: `int` — engine seat index; used by `GameController.livingOpponents()` and modal callbacks.
- `currentTurn`: `boolean` — whether this seat matches `GameEngine.getCurrentPlayerId()`.
- `alive`: `boolean` — false after the player explodes without Defuse.

### Methods
- `PlayerDisplayInfo(String name, int handSize, int playerId)` — constructs a seat record (turn and alive default to false-turn / alive).
- `getName(): String` / `getHandSize(): int` / `getPlayerId(): int` — accessors.
- `isCurrentTurn(): boolean` / `setCurrentTurn(boolean)` — turn highlight state.
- `isAlive(): boolean` / `setAlive(boolean)` — elimination state for modal filtering.

---

## WinnerView Class

Represents the post-match overlay shown when exactly one player remains. Trophy icon, 
winner name line, description copy, and Play Again / Main Menu actions. Loads 
`winner-style.css`.

### Data Members
- `winnerTitle`, `winnerText`, `winnerDescription`: `Text` — header, dynamic winner line, and body copy.
- `playAgainButton`, `mainMenuButton`: `Button` — navigation triggers.
- Trophy layout constants (`trophyImageSize`, clip dimensions, `winnerSectionSpacing`, etc.).

### Methods
- `WinnerView()` — builds centered `win-overlay` stack with `win-modal-card` dialog.
- `updateDisplay(ResourceBundle)` — binds `winner.title`, `winner.description`, button labels.
- `updateWinner(ResourceBundle, String player)` — sets `winnerText` to `{player} {winner.subtitle}`.
- `setOnPlayAgainAction(Runnable)` / `setOnMainMenuAction(Runnable)` — wire controller navigation.

---

## WinnerController Class

Represents the controller binding `WinnerView` to `AppModel` winner state and 
`ScreenRouter` post-game navigation.

### Data Members
- `refreshView`: `Runnable` — reloads bundle strings and winner name from `AppModel`.

### Methods
- `WinnerController(WinnerView, AppModel, ScreenRouter)` — defines `refreshView`; Play Again → `showGame`, Main Menu → `showStart`.
- `refreshView()` — runs `refreshView` runnable (invoked when entering winner screen).

---

## ScreenRouter Interface

Represents the navigation contract exposed to controllers. Decouples UI event handlers 
from concrete JavaFX `Scene` root mutations implemented in `MainApp`.

### Methods
- `showStart()` — navigate to the landing screen.
- `showInstructions()` — navigate to the rules screen.
- `showGameSetup()` — navigate to the pre-game setup screen.
- `showGame()` — navigate to the active match screen.
- `showWinner()` — navigate to the post-match winner screen.

---

## JavaFxScreenRouter Class

Represents a delegating adapter that implements `ScreenRouter` and forwards each navigation 
call to a runtime-configured handler. Allows `MainApp` to register anonymous navigation 
logic after all views and controllers are constructed.

### Data Members
- `navigation`: `ScreenRouter` — delegate target assigned via `configureNavigation`.

### Methods
- `configureNavigation(ScreenRouter navigation)` — stores the concrete navigation implementation.
- `showStart()` — forwards to `navigation.showStart()`.
- `showInstructions()` — forwards to `navigation.showInstructions()`.
- `showGameSetup()` — forwards to `navigation.showGameSetup()`.
- `showGame()` — forwards to `navigation.showGame()`.
- `showWinner()` — forwards to `navigation.showWinner()`.

---

## UI Stylesheets (game and winner)

Key `game-style.css` hooks added or relied on by recent UI work:

- `.hand-scroll-content` — top/bottom padding so hand-card hover lift and selection ring are not clipped by the horizontal `ScrollPane`.
- `.hand-cards-col-2` — fixed hand viewport height ($200$px); horizontal scroll only (enforced in `GameView.createPlayerHandSection`).
- `.combo-three-overlay`, `.combo-three-card`, `.combo-three-*` — dedicated triple special-combo modal chrome (separate from shared `modalOverlayScreen`).
- `.favor-request-box` / `.favor-grant-box` — two-step Favor modals on the shared overlay; grant step uses `modalCardScroll` for opponent hand cards.
- `.defuse-overlay`, `.defuse-modal-card`, `.defuse-section`, `.btn-defuse` — Defuse-specific
  skin applied to the shared depth-slider modal.
- `.bury-overlay`, `.bury-modal`, `.bury-section`, `.bury-title`, `.bury-subtitle`, `.bury-button`,
  `.bury-stat-text`, `.bury-stat-text-chosen` — Bury-specific skin applied to the shared
  depth-slider modal.

`winner-style.css` styles `WinnerView` (`win-overlay`, `win-modal-card`, `win-header`, `btn-play-again`, etc.).

### i18n keys (message bundles)

- `catCardSpecialCombo.title`, `.subtitle`, `.guessLabel`, `.targetPrefix` — double/triple combo modals (`message_en.properties`, `message_zh.properties`).
- `defuse.title`, `.sliderLabel`, `.topLabel`, `.currentLabel`, `.bottomLabel`, `.defuseButton`, `.explodeButton` — defuse overlay copy.
- `bury.title`, `.subtitle`, `.sliderLabel`, `.topLabel`, `.currentLabel`, `.bottomLabel`, `.buryButton` — Bury overlay copy.
- `rule.clone.nothingToClone`, `rule.clone.cannotCloneClone`, `rule.bury.invalidIndex` — Clone / Bury validation messages.
- `rule.catPair.feralCannotBeBaseType`, `rule.catPair.cloneCannotBeBaseType`, and triple analogues — special-combo validation.
- `gameView.drawCard` — draw-button forced-turn label prefix.
- `winner.title`, `.subtitle`, `.description`, `.playAgain`, `.mainMenu` — winner screen copy.

---
