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
    void requireCatPair_twoOfNonCatType_returnsNormally() {
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
    void requireCatPair_oneCatCardAndOneFeralCatCard_returnsNormally() {
        Player actor = new Player(0);
        actor.addCardToHand(new Card(CardType.CAT_CARDS));
        actor.addCardToHand(new Card(CardType.FERAL_CAT));
        assertDoesNotThrow(() -> ruleManager.requireCatPair(actor, CardType.CAT_CARDS));
    }

    @Test
    void requireCatPair_oneAttackAndOneFeralCatCard_throwsIllegalStateException() {
        Player actor = new Player(0);
        actor.addCardToHand(new Card(CardType.ATTACK));
        actor.addCardToHand(new Card(CardType.FERAL_CAT));
        IllegalStateException ex = assertThrows(
                IllegalStateException.class,
                () -> ruleManager.requireCatPair(actor, CardType.ATTACK));
        assertEquals("rule.catPair.needTwo", ex.getMessage());
    }

    @Test
    void requireCatPair_twoFeralCatCard_throwsIllegalStateException() {
        Player actor = new Player(0);
        actor.addCardToHand(new Card(CardType.FERAL_CAT));
        actor.addCardToHand(new Card(CardType.FERAL_CAT));
        IllegalStateException ex = assertThrows(
                IllegalStateException.class,
                () -> ruleManager.requireCatPair(actor, CardType.FERAL_CAT));
        assertEquals("rule.catPair.feralCannotBeBaseType", ex.getMessage());
    }

    @Test
    void requireCatPair_oneCatCardOneCloneCard_returnsNormally() {
        Player actor = new Player(0);
        actor.addCardToHand(new Card(CardType.CAT_CARDS));
        actor.addCardToHand(new Card(CardType.CLONE));
        assertDoesNotThrow(() -> ruleManager.requireCatPair(actor, CardType.CAT_CARDS));
    }

    @Test
    void requireCatPair_oneAttackCardoneCloneCard_throwsIllegalStateException() {
        Player actor = new Player(0);
        actor.addCardToHand(new Card(CardType.ATTACK));
        actor.addCardToHand(new Card(CardType.CLONE));
        IllegalStateException ex = assertThrows(
                IllegalStateException.class,
                () -> ruleManager.requireCatPair(actor, CardType.ATTACK));
        assertEquals("rule.catPair.needTwo", ex.getMessage());
    }

    @Test
    void requireCatPair_twoCloneCard_throwsIllegalStateException() {
        Player actor = new Player(0);
        actor.addCardToHand(new Card(CardType.CLONE));
        actor.addCardToHand(new Card(CardType.CLONE));
        IllegalStateException ex = assertThrows(
                IllegalStateException.class,
                () -> ruleManager.requireCatPair(actor, CardType.CLONE));
        assertEquals("rule.catPair.cloneCannotBeBaseType", ex.getMessage());
    }

    @Test
    void requireCatTriple_threeOfType_returnsNormally() {
        Player actor = new Player(0);
        actor.addCardToHand(new Card(CardType.CAT_CARDS));
        actor.addCardToHand(new Card(CardType.CAT_CARDS));
        actor.addCardToHand(new Card(CardType.CAT_CARDS));
        assertDoesNotThrow(() -> ruleManager.requireCatTriple(actor, CardType.CAT_CARDS));
    }

    @Test
    void requireCatTriple_twoOfType_throwsIllegalStateException() {
        Player actor = new Player(0);
        actor.addCardToHand(new Card(CardType.CAT_CARDS));
        actor.addCardToHand(new Card(CardType.CAT_CARDS));
        IllegalStateException ex = assertThrows(
                IllegalStateException.class,
                () -> ruleManager.requireCatTriple(actor, CardType.CAT_CARDS));
        assertEquals("rule.catTriple.needThree", ex.getMessage());
    }

    @Test
    void requireCatTriple_twoCatCardsAndOneFeralCat_returnsNormally() {
        Player actor = new Player(0);
        actor.addCardToHand(new Card(CardType.CAT_CARDS));
        actor.addCardToHand(new Card(CardType.CAT_CARDS));
        actor.addCardToHand(new Card(CardType.FERAL_CAT));
        assertDoesNotThrow(() -> ruleManager.requireCatTriple(actor, CardType.CAT_CARDS));
    }

    @Test
    void requireCatTriple_oneCatCardsAndTwoFeralCat_returnsNormally() {
        Player actor = new Player(0);
        actor.addCardToHand(new Card(CardType.CAT_CARDS));
        actor.addCardToHand(new Card(CardType.FERAL_CAT));
        actor.addCardToHand(new Card(CardType.FERAL_CAT));
        assertDoesNotThrow(() -> ruleManager.requireCatTriple(actor, CardType.CAT_CARDS));
    }

    @Test
    void requireCatTriple_threeFeralCat_throwsIllegalStateException() {
        Player actor = new Player(0);
        actor.addCardToHand(new Card(CardType.FERAL_CAT));
        actor.addCardToHand(new Card(CardType.FERAL_CAT));
        actor.addCardToHand(new Card(CardType.FERAL_CAT));
        IllegalStateException ex = assertThrows(
                IllegalStateException.class,
                () -> ruleManager.requireCatTriple(actor, CardType.FERAL_CAT));
        assertEquals("rule.catTriple.feralCannotBeBaseType", ex.getMessage());
    }

    @Test
    void requireCatTriple_twoCatCardAndOneClone_returnsNormally() {
        Player actor = new Player(0);
        actor.addCardToHand(new Card(CardType.CAT_CARDS));
        actor.addCardToHand(new Card(CardType.CAT_CARDS));
        actor.addCardToHand(new Card(CardType.CLONE));
        assertDoesNotThrow(() -> ruleManager.requireCatTriple(actor, CardType.CAT_CARDS));
    }

    @Test
    void requireCatTriple_oneCatCardAndTwoClone_throwsIllegalStateException() {
        Player actor = new Player(0);
        actor.addCardToHand(new Card(CardType.CAT_CARDS));
        actor.addCardToHand(new Card(CardType.CLONE));
        actor.addCardToHand(new Card(CardType.CLONE));
        IllegalStateException ex = assertThrows(
                IllegalStateException.class,
                () -> ruleManager.requireCatTriple(actor, CardType.CAT_CARDS));
        assertEquals("rule.catTriple.needThree", ex.getMessage());
    }

    @Test
    void requireCatTriple_threeClone_throwsIllegalStateException() {
        Player actor = new Player(0);
        actor.addCardToHand(new Card(CardType.CLONE));
        actor.addCardToHand(new Card(CardType.CLONE));
        actor.addCardToHand(new Card(CardType.CLONE));
        IllegalStateException ex = assertThrows(
                IllegalStateException.class,
                () -> ruleManager.requireCatTriple(actor, CardType.CLONE));
        assertEquals("rule.catTriple.cloneCannotBeBaseType", ex.getMessage());
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

    @Test
    void requireSomethingToClone_nonNullLastCard_returnsNormally() {
        assertDoesNotThrow(() -> ruleManager.requireSomethingToClone(CardType.ATTACK));
    }

    @Test
    void requireSomethingToClone_null_throwsIllegalStateException() {
        IllegalStateException ex = assertThrows(
                IllegalStateException.class,
                () -> ruleManager.requireSomethingToClone(null));
        assertEquals("rule.clone.nothingToClone", ex.getMessage());
    }

    @Test
    void requireSomethingToClone_cloneCard_throwsIllegalStateException() {
        IllegalStateException ex = assertThrows(
                IllegalStateException.class,
                () -> ruleManager.requireSomethingToClone(CardType.CLONE));
        assertEquals("rule.clone.cannotCloneClone", ex.getMessage());
    }

    @Test
    void requireValidInsertIndex_indexOfNegativeOneAndSize61_throwsIllegalStateException() {
        final int index = -1;
        final int size = 61;
        IllegalStateException ex = assertThrows(
                IllegalStateException.class,
                () -> ruleManager.requireValidInsertIndex(index, size)
        );
        assertEquals("rule.bury.invalidIndex", ex.getMessage());
    }

    @Test
    void requireValidInsertIndex_indexOfZeroAndSize61_returnsNormally() {
        final int index = 0;
        final int size = 61;
        assertDoesNotThrow(() -> ruleManager.requireValidInsertIndex(index, size));
    }

    @Test
    void requireValidInsertIndex_indexOf30AndSize61_returnsNormally() {
        final int index = 30;
        final int size = 61;
        assertDoesNotThrow(() -> ruleManager.requireValidInsertIndex(index, size));
    }

    @Test
    void requireValidInsertIndex_indexOf61AndSize61_returnsNormally() {
        final int index = 61;
        final int size = 61;
        assertDoesNotThrow(() -> ruleManager.requireValidInsertIndex(index, size));
    }

    @Test
    void requireValidInsertIndex_indexOf62AndSize61_throwsIllegalStateException() {
        final int index = 62;
        final int size = 61;
        IllegalStateException ex = assertThrows(
                IllegalStateException.class,
                () -> ruleManager.requireValidInsertIndex(index, size)
        );
        assertEquals("rule.bury.invalidIndex", ex.getMessage());
    }

    @Test
    void requireValidInsertIndex_indexOf100AndSize61_throwsIllegalStateException() {
        final int index = 100;
        final int size = 61;
        IllegalStateException ex = assertThrows(
                IllegalStateException.class,
                () -> ruleManager.requireValidInsertIndex(index, size)
        );
        assertEquals("rule.bury.invalidIndex", ex.getMessage());
    }

    @Test
    void requireValidInsertIndex_indexOf0AndSize0_returnsNormally() {
        final int index = 0;
        final int size = 0;
        assertDoesNotThrow(() -> ruleManager.requireValidInsertIndex(index, size));
    }

    @Test
    void requireValidInsertIndex_indexOf1AndSize0_throwsIllegalStateException() {
        final int index = 1;
        final int size = 0;
        IllegalStateException ex = assertThrows(
                IllegalStateException.class,
                () -> ruleManager.requireValidInsertIndex(index, size)
        );
        assertEquals("rule.bury.invalidIndex", ex.getMessage());
    }

    @Test
    void requireValidInsertIndex_indexOf1AndSize1_returnsNormally() {
        final int index = 1;
        final int size = 1;
        assertDoesNotThrow(() -> ruleManager.requireValidInsertIndex(index, size));
    }
}
