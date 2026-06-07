package domain;

public final class RuleManager {

    private static final int CAT_PAIR_SIZE = 2;

    private static final String CANNOT_PLAY_DIRECTLY_KEY = "rule.play.cannotPlayDirectly";
    private static final String INVALID_TARGET_KEY = "rule.target.invalid";
    private static final String CAT_PAIR_NEED_TWO_KEY = "rule.catPair.needTwo";
    private static final String NOTHING_TO_NOPE_KEY = "rule.nope.nothingToCancel";

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
        if (countOfType(actor, cardType) < CAT_PAIR_SIZE) {
            throw new IllegalStateException(CAT_PAIR_NEED_TWO_KEY);
        }
    }

    private static int countOfType(Player actor, CardType cardType) {
        int count = 0;
        for (Card card : actor.getHand()) {
            if (card.getCardType() == cardType) {
                count++;
            }
        }
        return count;
    }

    public void requireSomethingToNope(CardType lastPlayedCard) {
        if (lastPlayedCard == null) {
            throw new IllegalStateException(NOTHING_TO_NOPE_KEY);
        }
    }
}
