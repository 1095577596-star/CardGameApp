package com.cardgame;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class JinHuaRule implements GameRule {

    private static final int LEOPARD = 6;
    private static final int STRAIGHT_FLUSH = 5;
    private static final int FLUSH = 4;
    private static final int STRAIGHT = 3;
    private static final int PAIR = 2;
    private static final int HIGH_CARD = 1;

    @Override
    public String getName() {
        return "金花";
    }

    @Override
    public int getCardsPerPlayer() {
        return 3;
    }

    @Override
    public Object calculateHandRank(List<Card> cards) {
        if (cards.size() != 3) return new int[]{0, 0, 0, 0};

        List<Integer> values = new ArrayList<>();
        List<String> suits = new ArrayList<>();
        for (Card c : cards) {
            int v = c.getValue() == 1 ? 14 : c.getValue();
            values.add(v);
            suits.add(c.getSuit());
        }
        Collections.sort(values, Collections.reverseOrder());

        boolean isLeopard = values.get(0).equals(values.get(1)) && values.get(1).equals(values.get(2));
        boolean isFlush = suits.get(0).equals(suits.get(1)) && suits.get(1).equals(suits.get(2));
        boolean isStraight = (values.get(0) - values.get(1) == 1 && values.get(1) - values.get(2) == 1) ||
                            (values.get(0) == 14 && values.get(1) == 3 && values.get(2) == 2);
        boolean isPair = values.get(0).equals(values.get(1)) || values.get(1).equals(values.get(2)) || values.get(0).equals(values.get(2));

        int type;
        if (isLeopard) type = LEOPARD;
        else if (isStraight && isFlush) type = STRAIGHT_FLUSH;
        else if (isFlush) type = FLUSH;
        else if (isStraight) type = STRAIGHT;
        else if (isPair) type = PAIR;
        else type = HIGH_CARD;

        int pairValue = 0;
        if (isPair) {
            if (values.get(0).equals(values.get(1))) pairValue = values.get(0);
            else pairValue = values.get(1);
        }

        return new int[]{type, values.get(0), values.get(1), values.get(2), pairValue};
    }

    @Override
    public int compareHands(Object rank1, Object rank2) {
        int[] r1 = (int[]) rank1;
        int[] r2 = (int[]) rank2;

        if (r1[0] != r2[0]) {
            return Integer.compare(r1[0], r2[0]);
        }

        if (r1[0] == PAIR) {
            if (r1[4] != r2[4]) {
                return Integer.compare(r1[4], r2[4]);
            }
        }

        for (int i = 1; i <= 3; i++) {
            if (r1[i] != r2[i]) {
                return Integer.compare(r1[i], r2[i]);
            }
        }

        return 0;
    }
}
