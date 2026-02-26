package com.cardgame;

import java.util.List;

public class LuZhouDaErRule implements GameRule {

    private static final String[] CARD_NAMES = {
        "壹", "贰", "叁", "肆", "伍", "陆", "柒", "捌", "玖", "拾", "大", "小"
    };

    @Override
    public String getName() {
        return "泸州大贰";
    }

    @Override
    public int getCardsPerPlayer() {
        return 1;
    }

    @Override
    public Object calculateHandRank(List<Card> cards) {
        if (cards.isEmpty()) return 0;
        int value = cards.get(0).getValue();
        return value;
    }

    @Override
    public int compareHands(Object rank1, Object rank2) {
        int r1 = (int) rank1;
        int r2 = (int) rank2;
        return Integer.compare(r1, r2);
    }

    public static String getCardName(int index) {
        if (index >= 0 && index < CARD_NAMES.length) {
            return CARD_NAMES[index];
        }
        return String.valueOf(index + 1);
    }
}
