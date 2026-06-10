package domain;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Arrays;
import org.junit.jupiter.api.Test;

/**
 * Integration tests that drive a real {@link GameEngine} through multi-step game
 * scenarios. Unlike the per-method unit tests, these exercise several
 * collaborators together with no mocks — the engine wires {@link Deck},
 * {@link Player}, {@link TurnTracker}, {@link RuleManager} and
 * {@link ActionController} — covering more than two main features end to end:
 * turn flow (Attack stacking, Skip, Nope undo), cross-player card transfer
 * (Favor, Cat pair), Exploding-Kitten resolution (Defuse), and the win
 * conditions (elimination).
 */
class GameFlowIntegrationTest {

    private static final int TWO_PLAYERS = 2;
    private static final int THREE_PLAYERS = 3;

    // Feature: turn flow — an Attack's two forced turns are worked off by a
    // Skip plus an end-of-turn draw before the turn moves on.
    @Test
    void attackThenSkipThenDraw_worksOffForcedTurnsAndAdvances() {
        GameEngine engine = new GameEngine(THREE_PLAYERS);
        give(engine, 0, CardType.ATTACK);

        engine.playAttack();
        assertEquals(1, engine.getCurrentPlayerId());
        assertEquals(2, engine.getForcedTurns());

        give(engine, 1, CardType.SKIP);
        engine.playSkip();
        assertEquals(1, engine.getCurrentPlayerId());
        assertEquals(1, engine.getForcedTurns());

        engine.endTurnByDrawing();
        assertEquals(2, engine.getCurrentPlayerId());
    }

    // Feature: turn flow + Nope — a Nope undoes the Attack and hands the turn
    // back to the attacker with the forced turns reduced.
    @Test
    void nopeUndoesAttack_returnsTurnToAttacker() {
        GameEngine engine = new GameEngine(TWO_PLAYERS);
        give(engine, 0, CardType.ATTACK);
        engine.playAttack();

        give(engine, 1, CardType.NOPE);
        engine.playNope(1);

        assertEquals(0, engine.getCurrentPlayerId());
        assertEquals(1, engine.getForcedTurns());
    }

    // Feature: cross-player card transfer — Favor moves a card from the target
    // to the current player, who keeps their turn.
    @Test
    void favor_movesCardFromTargetToCurrentPlayer() {
        GameEngine engine = new GameEngine(TWO_PLAYERS);
        give(engine, 0, CardType.FAVOR);
        int targetBefore = engine.getPlayerHand(1).size();
        int actorBefore = engine.getPlayerHand(0).size();

        engine.playFavor(1, 0);

        assertEquals(targetBefore - 1, engine.getPlayerHand(1).size());
        assertEquals(actorBefore, engine.getPlayerHand(0).size());
        assertEquals(0, engine.getCurrentPlayerId());
    }

    // Feature: cross-player card transfer — a Cat pair steals one card from the
    // target.
    @Test
    void catPair_stealsOneCardFromTarget() {
        GameEngine engine = new GameEngine(TWO_PLAYERS);
        give(engine, 0, CardType.CAT_CARDS);
        give(engine, 0, CardType.CAT_CARDS);
        int targetBefore = engine.getPlayerHand(1).size();

        engine.playCatPair(1, Arrays.asList(CardType.CAT_CARDS, CardType.CAT_CARDS));

        assertEquals(targetBefore - 1, engine.getPlayerHand(1).size());
        assertEquals(0, engine.getCurrentPlayerId());
    }

    // Feature: Exploding-Kitten resolution — drawing a kitten and defusing it
    // reinserts the kitten, keeps the player alive, and passes the turn.
    @Test
    void drawKittenThenDefuse_survivesReinsertsAndPassesTurn() {
        GameEngine engine = new GameEngine(TWO_PLAYERS);
        give(engine, 0, CardType.EXPLODING_KITTEN);
        final int pileBefore = engine.getDrawPileSize();

        engine.defuseDrawnKitten(0);

        assertTrue(engine.getPlayer(0).isAlive());
        assertEquals(-1, engine.getPlayer(0).getIndexOfCard(CardType.EXPLODING_KITTEN));
        assertEquals(pileBefore + 1, engine.getDrawPileSize());
        assertEquals(1, engine.getCurrentPlayerId());
    }

    // Feature: win condition — exploding without a Defuse eliminates the player
    // and ends the game with the last player standing as the winner.
    @Test
    void explodeWithoutDefuse_eliminatesPlayerAndDeclaresLastStandingWinner() {
        GameEngine engine = new GameEngine(TWO_PLAYERS);
        clear(engine, 0, CardType.DEFUSE);
        give(engine, 0, CardType.EXPLODING_KITTEN);

        engine.explodeCurrentPlayer();

        assertFalse(engine.getPlayer(0).isAlive());
        assertTrue(engine.isGameOver());
        assertEquals(1, engine.getWinnerId());
    }

    private void give(GameEngine engine, int playerId, CardType type) {
        engine.getPlayer(playerId).addCardToHand(new Card(type));
    }

    private void clear(GameEngine engine, int playerId, CardType type) {
        Player player = engine.getPlayer(playerId);
        int index = player.getIndexOfCard(type);
        while (index >= 0) {
            player.removeCardFromHand(index);
            index = player.getIndexOfCard(type);
        }
    }
}
