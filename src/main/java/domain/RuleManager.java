package domain;

public final class RuleManager {

    private static final int CAT_PAIR_SIZE = 2;
    private static final int CAT_TRIPLE_SIZE = 3;

    private static final String CANNOT_PLAY_DIRECTLY_KEY = "rule.play.cannotPlayDirectly";
    private static final String INVALID_TARGET_KEY = "rule.target.invalid";
    private static final String CAT_PAIR_NEED_TWO_KEY = "rule.catPair.needTwo";
    private static final String CAT_PAIR_FERAL_CANNOT_BE_BASE_KEY =
            "rule.catPair.feralCannotBeBaseType";
    private static final String CAT_PAIR_CLONE_CANNOT_BE_BASE_KEY =
            "rule.catPair.cloneCannotBeBaseType";
    private static final String CAT_TRIPLE_NEED_THREE_KEY = "rule.catTriple.needThree";
    private static final String CAT_TRIPLE_FERAL_CANNOT_BE_BASE_KEY =
            "rule.catTriple.feralCannotBeBaseType";
    private static final String CAT_TRIPLE_CLONE_CANNOT_BE_BASE_KEY =
            "rule.catTriple.cloneCannotBeBaseType";
    private static final String NOTHING_TO_NOPE_KEY = "rule.nope.nothingToCancel";
    private static final String NOTHING_TO_CLONE_KEY = "rule.clone.nothingToClone";
    private static final String CANNOT_CLONE_CLONE_CARD_KEY = "rule.clone.cannotCloneClone";
    private static final String INVALID_INSERT_INDEX_KEY = "rule.bury.invalidIndex";

    public void requirePlayable(CardType type) {
        if (type == CardType.DEFUSE || type == CardType.EXPLODING_KITTEN) {
            throw new IllegalArgumentException(CANNOT_PLAY_DIRECTLY_KEY);
        }
    }

    public void requireValidTarget(Player actor, Player target) {
        if (target.getPlayerId() == actor.getPlayerId() || !target.isAlive()) {
            throw new IllegalArgumentException(INVALID_TARGET_KEY);
        }
    }

    public void requireCatPair(Player actor, CardType cardType) {
        if (cardType == CardType.FERAL_CAT) {
            throw new IllegalStateException(CAT_PAIR_FERAL_CANNOT_BE_BASE_KEY);
        }
        if (cardType == CardType.CLONE) {
            throw new IllegalStateException(CAT_PAIR_CLONE_CANNOT_BE_BASE_KEY);
        }
        if (countOfType(actor, cardType) < CAT_PAIR_SIZE) {
            throw new IllegalStateException(CAT_PAIR_NEED_TWO_KEY);
        }
    }

    private static int countOfType(Player actor, CardType cardType) {
        int count = 0;
        int cloneCount = 0;

        for (Card card : actor.getHand()) {
            CardType currentType = card.getCardType();
            if (currentType == cardType
                    || (currentType == CardType.FERAL_CAT && cardType == CardType.CAT_CARDS)) {
                count++;
            } else if (currentType == CardType.CLONE) {
                cloneCount++;
            }
        }

        int effectiveClones = Math.min(cloneCount, 1);

        if (cardType == CardType.CAT_CARDS) {
            return count + effectiveClones;
        }

        return count;
    }

    public void requireCatTriple(Player actor, CardType selectedCard) {
        if (selectedCard == CardType.FERAL_CAT) {
            throw new IllegalStateException(CAT_TRIPLE_FERAL_CANNOT_BE_BASE_KEY);
        }
        if (selectedCard == CardType.CLONE) {
            throw new IllegalStateException(CAT_TRIPLE_CLONE_CANNOT_BE_BASE_KEY);
        }
        if (countOfType(actor, selectedCard) < CAT_TRIPLE_SIZE) {
            throw new IllegalStateException(CAT_TRIPLE_NEED_THREE_KEY);
        }
    }

    public void requireSomethingToNope(CardType lastPlayedCard) {
        if (lastPlayedCard == null) {
            throw new IllegalStateException(NOTHING_TO_NOPE_KEY);
        }
    }

    public void requireSomethingToClone(CardType lastPlayedCard) {
        if (lastPlayedCard == null) {
            throw new IllegalStateException(NOTHING_TO_CLONE_KEY);
        }
        if (lastPlayedCard == CardType.CLONE) {
            throw new IllegalStateException(CANNOT_CLONE_CLONE_CARD_KEY);
        }
    }

    public void requireValidInsertIndex(int index, int size) {
        if (index < 0 || index > size) {
            throw new IllegalStateException(INVALID_INSERT_INDEX_KEY);
        }
    }
}
