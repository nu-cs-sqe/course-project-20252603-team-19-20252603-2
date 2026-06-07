package domain;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;

class RuleManagerTest {

    private final RuleManager ruleManager = new RuleManager();

    @Test
    void requirePlayable_normalCard_returnsNormally() {
        assertDoesNotThrow(() -> ruleManager.requirePlayable(CardType.SKIP));
    }

    @Test
    void requirePlayable_defuse_throwsIllegalArgumentException() {
        IllegalArgumentException ex = assertThrows(
                IllegalArgumentException.class,
                () -> ruleManager.requirePlayable(CardType.DEFUSE));
        assertEquals("rule.play.cannotPlayDirectly", ex.getMessage());
    }

    @Test
    void requirePlayable_explodingKitten_throwsIllegalArgumentException() {
        IllegalArgumentException ex = assertThrows(
                IllegalArgumentException.class,
                () -> ruleManager.requirePlayable(CardType.EXPLODING_KITTEN));
        assertEquals("rule.play.cannotPlayDirectly", ex.getMessage());
    }

    @Test
    void requireValidTarget_distinctLivingTarget_returnsNormally() {
        Player actor = new Player(0);
        Player target = new Player(1);
        assertDoesNotThrow(() -> ruleManager.requireValidTarget(actor, target));
    }

    @Test
    void requireValidTarget_self_throwsIllegalArgumentException() {
        Player actor = new Player(0);
        IllegalArgumentException ex = assertThrows(
                IllegalArgumentException.class,
                () -> ruleManager.requireValidTarget(actor, actor));
        assertEquals("rule.target.invalid", ex.getMessage());
    }

    @Test
    void requireValidTarget_deadTarget_throwsIllegalArgumentException() {
        Player actor = new Player(0);
        Player target = new Player(1);
        target.markDead();
        IllegalArgumentException ex = assertThrows(
                IllegalArgumentException.class,
                () -> ruleManager.requireValidTarget(actor, target));
        assertEquals("rule.target.invalid", ex.getMessage());
    }

    @Test
    void requireCatPair_twoCatCards_returnsNormally() {
        Player actor = new Player(0);
        actor.addCardToHand(new Card(CardType.CAT_CARDS));
        actor.addCardToHand(new Card(CardType.CAT_CARDS));
        assertDoesNotThrow(() -> ruleManager.requireCatPair(actor, CardType.CAT_CARDS));
    }

    @Test
    void requireCatPair_noCatCards_throwsIllegalStateException() {
        Player actor = new Player(0);
        IllegalStateException ex = assertThrows(
                IllegalStateException.class,
                () -> ruleManager.requireCatPair(actor, CardType.CAT_CARDS));
        assertEquals("rule.catPair.needTwo", ex.getMessage());
    }

    @Test
    void requireCatPair_oneCatCard_throwsIllegalStateException() {
        Player actor = new Player(0);
        actor.addCardToHand(new Card(CardType.CAT_CARDS));
        IllegalStateException ex = assertThrows(
                IllegalStateException.class,
                () -> ruleManager.requireCatPair(actor, CardType.CAT_CARDS));
        assertEquals("rule.catPair.needTwo", ex.getMessage());
    }

    @Test
    void requireCatPair_threeCatCards_returnsNormally() {
        Player actor = new Player(0);
        actor.addCardToHand(new Card(CardType.CAT_CARDS));
        actor.addCardToHand(new Card(CardType.CAT_CARDS));
        actor.addCardToHand(new Card(CardType.CAT_CARDS));
        assertDoesNotThrow(() -> ruleManager.requireCatPair(actor, CardType.CAT_CARDS));
    }

    @Test
    void requireCatPair_twoOfANonCatType_returnsNormally() {
        Player actor = new Player(0);
        actor.addCardToHand(new Card(CardType.ATTACK));
        actor.addCardToHand(new Card(CardType.ATTACK));
        assertDoesNotThrow(() -> ruleManager.requireCatPair(actor, CardType.ATTACK));
    }

    @Test
    void requireCatPair_onlyOneOfSelectedType_throwsIllegalStateException() {
        Player actor = new Player(0);
        actor.addCardToHand(new Card(CardType.ATTACK));
        actor.addCardToHand(new Card(CardType.CAT_CARDS));
        IllegalStateException ex = assertThrows(
                IllegalStateException.class,
                () -> ruleManager.requireCatPair(actor, CardType.ATTACK));
        assertEquals("rule.catPair.needTwo", ex.getMessage());
    }

    @Test
    void requireSomethingToNope_nonNullLastCard_returnsNormally() {
        assertDoesNotThrow(() -> ruleManager.requireSomethingToNope(CardType.ATTACK));
    }

    @Test
    void requireSomethingToNope_null_throwsIllegalStateException() {
        IllegalStateException ex = assertThrows(
                IllegalStateException.class,
                () -> ruleManager.requireSomethingToNope(null));
        assertEquals("rule.nope.nothingToCancel", ex.getMessage());
    }
}
