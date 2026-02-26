package com.cardgame;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class KanShunDouNiuRule extends DouNiuRule {

    private static final int KAN = 2;
    private static final int SHUN = 1;

    @Override
    public String getName() {
        return "坎顺斗牛";
    }

    @Override
    protected int getHandType() {
        return NORMAL;
    }

    @Override
    public Object calculateHandRank(List<Card> cards) {
        if (cards.size() != 5) return new int[]{0, 0, 0};

        List<Integer> values = new ArrayList<>();
        for (Card c : cards) {
            int v = c.getValue() == 1 ? 14 : c.getValue();
            values.add(v);
        }
        Collections.sort(values);

        int type = NORMAL;

        if (isKan(values)) {
            type = KAN;
        } else if (isShun(values)) {
            type = SHUN;
        }

        int niu = findNiu(cards);
        int maxCard = values.get(values.size() - 1);

        return new int[]{type, niu, maxCard};
    }

    private boolean isKan(List<Integer> values) {
        for (int i = 0; i <= 2; i++) {
            if (values.get(i).equals(values.get(i + 1)) && values.get(i + 1).equals(values.get(i + 2))) {
                return true;
            }
        }
        return false;
    }

    private boolean isShun(List<Integer> values) {
        List<Integer> sorted = new ArrayList<>(values);
        Collections.sort(sorted);

        boolean normalShun = true;
        for (int i = 0; i < 4; i++) {
            if (sorted.get(i + 1) - sorted.get(i) != 1) {
                normalShun = false;
                break;
            }
        }
        if (normalShun) return true;

        if (sorted.get(0) == 2 && sorted.get(1) == 3 && sorted.get(2) == 4 && sorted.get(3) == 5 && sorted.get(4) == 14) {
            return true;
        }

        return false;
    }
}
