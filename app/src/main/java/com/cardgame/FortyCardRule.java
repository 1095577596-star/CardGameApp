package com.cardgame;

import java.util.Collections;
import java.util.List;

public class FortyCardRule implements GameRule {

    @Override
    public String getName() {
        return "40张宝子";
    }

    @Override
    public int getCardsPerPlayer() {
        return 2;
    }

    @Override
    public Object calculateHandRank(List<Card> cards) {
        if (cards.size() != 2) return new int[]{0, 0};

        Card c1 = cards.get(0);
        Card c2 = cards.get(1);

        boolean isPair = c1.getValue() == c2.getValue();
        int pairValue = isPair ? (c1.getValue() == 1 ? 14 : c1.getValue()) : 0;

        int pointSum = (c1.getPointValue() + c2.getPointValue()) % 10;

        return new int[]{isPair ? 1 : 0, pairValue, pointSum};
    }

    @Override
    public int compareHands(Object rank1, Object rank2) {
        int[] r1 = (int[]) rank1;
        int[] r2 = (int[]) rank2;

        if (r1[0] != r2[0]) {
            return Integer.compare(r1[0], r2[0]);
        }

        if (r1[0] == 1) {
            return Integer.compare(r1[1], r2[1]);
        }

        return Integer.compare(r1[2], r2[2]);
    }
}
