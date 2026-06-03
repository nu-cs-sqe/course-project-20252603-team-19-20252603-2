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

## Main Class


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
- `getForcedTurns(): int` — draws the current player still owes before the turn
  passes (1 normally; raised by Attack / Targeted Attack).
- `getLastPlayedCard(): CardType` — the most recently played card type (what a
  Nope may cancel); `null` before any card is played.
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
- `playCatPair(int targetId)` — discards two CAT_CARDS and steals one random
  card from the target; same player continues.
- `playNope(int noperId)` — the noper discards a NOPE to cancel the last played
  card (simplified, no reaction window); clears `lastPlayedCard`.
- `defuseDrawnKitten(int reinsertIndex)` — after drawing an Exploding Kitten,
  discards a DEFUSE, reinserts the kitten at `reinsertIndex`, and ends the turn.
- `explodeCurrentPlayer()` — after drawing an Exploding Kitten with no Defuse,
  marks the current player dead, discards their remaining hand, and ends the
  turn (advancing to the next living player).
- `isGameOver(): boolean` — true when only one player is alive or the draw pile
  is exhausted.
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
   - `playSkip`, `playReverse`, `playAttack`, `playTargetedAttack` end the turn
     themselves — after them, go to step 5.
3. When the player chooses to draw, check `isDeckEmpty()`; if empty the game is
   over (`isGameOver()`), go to step 6.
4. Call `drawCardForCurrentPlayer()`.
   - If the drawn card is `EXPLODING_KITTEN`: call `defuseDrawnKitten(index)`
     when the player has (and plays) a Defuse, otherwise `explodeCurrentPlayer()`.
     Both end the turn.
   - Otherwise call `endTurnByDrawing()` to end the turn (honours Attack
     stacking and skips eliminated players).
5. After any turn-ending action, check `isGameOver()`; if true call
   `getWinnerId()` and show the End screen.
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
- `turnGoesToCurrentPlayerAgain()`
- `turnReversesDirection()`


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
- `requireCatPair(Player actor)` — throws `IllegalStateException`
  (`rule.catPair.needTwo`) if the actor holds fewer than two `CAT_CARDS`.
- `requireSomethingToNope(CardType lastPlayedCard)` — throws
  `IllegalStateException` (`rule.nope.nothingToCancel`) if `lastPlayedCard` is
  `null`.

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
screen views (`StartView`, `InstructionView`, `GameSetupView`, `GameView`), wires 
`StartController`, `InstructionController`, `GameSetupController`, and `GameController`, 
registers anonymous `ScreenRouter` logic to mutate the `Scene` root during navigation, 
and brings the primary application window into view.
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
- `createPlayerHandSection(): ScrollPane` — configuration wrapper; styles horizontal panning sliders over 
  active inventories.
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
- `updatePlayerCards(List<Card> hand)` — layout flush loop; resets user hand panels and runs structural
  loops to map and display cards.
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
- `cards`: `String[]` — canonical whitelist of supported card folder identity tags.
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
and an owned `GameModel`. Encapsulates draw, play, turn-advance, and full-board refresh 
logic while delegating all domain rules to `GameEngine` through the model layer.

### Data Members
- `model`: `GameModel` — per-match UI state facade wrapping `GameEngine` and display names.
- `refreshAction`: `Runnable` — synchronizes localized chrome, local hand, opponents, deck count, turn banner, and log/discard reset when a game is active.
- `startGameAction`: `Runnable` — boots the engine from `AppModel.getPlayerNames()` then runs `refreshAction`.

### Methods
- `GameController(GameView view, AppModel appModel, ScreenRouter router)` — instantiates `GameModel`, wires quit/draw/play handlers, and defines refresh/start pipelines.
- `startGame()` — executes `startGameAction`; invoked by navigation when entering the game screen.
- `refreshView()` — executes `refreshAction`; rebinds the board after locale changes or re-entry.

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

### Methods
- `toggleLanguage()` — flips `selectedLocale` between English and Simplified Chinese.
- `getResourceBundle(): ResourceBundle` — loads `message` properties for `selectedLocale`.
- `getSelectedLocale(): Locale` — returns the active locale token.
- `getNumberPlayer(): int` — delegates to `setupModel.getNumberPlayer()`.
- `setNumberPlayer(int playerCount)` — delegates to `setupModel.setNumberPlayer(playerCount)`.
- `getPlayerNames(): List<String>` — delegates to `setupModel.getPlayerNames()` (defensive copy).
- `capturePlayerNamesFromInputs(List<String> rawInputs, String defaultNamePrefix)` — delegates name normalization and validation to `setupModel`.

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
- `ableToDrawCard(): boolean` — delegates to `engine.isDeckEmpty()` (inverted guard semantics for UI draw eligibility).
- `drawCard(): Card` — delegates to `engine.drawCardForCurrentPlayer()`.
- `getDeckSize(): int` — delegates to `engine.getDrawPileSize()`.
- `removeCard(CardType cardType)` — locates and removes the first matching card from the local player's hand via the engine.
- `finishTurn()` — advances the engine turn tracker and syncs `localPlayerId` to `engine.getCurrentPlayerId()`.
- `getLocalHand(): List<Card>` — returns the local player's hand from the engine.
- `getLocalHandSize(): int` — returns local hand list size.
- `getLocalPlayerName(): String` — resolves the display name for `localPlayerId`.
- `resetPlayerId()` — resets `localPlayerId` to $0$ before a full UI refresh.
- `getOpponents(): List<PlayerDisplayInfo>` — maps every seated player into a display DTO for `GameView.showOpponents`.
- `toDisplayInfo(int playerId): PlayerDisplayInfo` — private mapper; bundles name, hand size, and current-turn flag.

---

## PlayerDisplayInfo Class

Represents an immutable snapshot of opponent (or seat) information required by `GameView` 
to render avatars, hand-size badges, and active-turn highlights. A pure data transfer 
object with no behavior beyond accessors.

### Data Members
- `name`: `String` — player display label.
- `handSize`: `int` — number of cards currently held (face-down count for opponents).
- `currentTurn`: `boolean` — whether this seat matches `GameEngine.getCurrentPlayerId()`.

### Methods
- `PlayerDisplayInfo(String name, int handSize, boolean currentTurn)` — constructs an immutable record.
- `getName(): String` — returns `name`.
- `getHandSize(): int` — returns `handSize`.
- `isCurrentTurn(): boolean` — returns `currentTurn`.

---

## ScreenRouter Interface

Represents the navigation contract exposed to controllers. Decouples UI event handlers 
from concrete JavaFX `Scene` root mutations implemented in `MainApp`.

### Methods
- `showStart()` — navigate to the landing screen.
- `showInstructions()` — navigate to the rules screen.
- `showGameSetup()` — navigate to the pre-game setup screen.
- `showGame()` — navigate to the active match screen.

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

---

