package com.cardgame;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class DouNiuRule implements GameRule {

    protected static final int NORMAL = 0;

    @Override
    public String getName() {
        return "斗牛";
    }

    @Override
    public int getCardsPerPlayer() {
        return 5;
    }

    @Override
    public Object calculateHandRank(List<Card> cards) {
        if (cards.size() != 5) return new int[]{0, 0};

        List<Integer> values = new ArrayList<>();
        for (Card c : cards) {
            values.add(c.getValue() == 1 ? 14 : c.getValue());
        }
        Collections.sort(values, Collections.reverseOrder());

        int niu = findNiu(cards);
        int maxCard = values.get(0);

        return new int[]{getHandType(), niu, maxCard};
    }

    protected int getHandType() {
        return NORMAL;
    }

    protected int findNiu(List<Card> cards) {
        int[] points = new int[5];
        for (int i = 0; i < 5; i++) {
            points[i] = cards.get(i).getPointValue();
        }

        for (int i = 0; i < 5; i++) {
            for (int j = i + 1; j < 5; j++) {
                for (int k = j + 1; k < 5; k++) {
                    if ((points[i] + points[j] + points[k]) % 10 == 0) {
                        int sum = 0;
                        for (int m = 0; m < 5; m++) {
                            if (m != i && m != j && m != k) {
                                sum += points[m];
                            }
                        }
                        int niu = sum % 10;
                        return niu == 0 ? 10 : niu;
                    }
                }
            }
        }
        return 0;
    }

    @Override
    public int compareHands(Object rank1, Object rank2) {
        int[] r1 = (int[]) rank1;
        int[] r2 = (int[]) rank2;

        if (r1[0] != r2[0]) {
            return Integer.compare(r1[0], r2[0]);
        }

        if (r1[1] != r2[1]) {
            return Integer.compare(r1[1], r2[1]);
        }

        return Integer.compare(r1[2], r2[2]);
    }
}
