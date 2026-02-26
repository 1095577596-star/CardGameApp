package com.cardgame;

import java.util.List;

public interface GameRule {
    String getName();
    int getCardsPerPlayer();
    Object calculateHandRank(List<Card> cards);
    int compareHands(Object rank1, Object rank2);
}
