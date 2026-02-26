package com.cardgame;

import java.util.List;

public class PlayerHand implements Comparable<PlayerHand> {
    private int playerNumber;
    private List<Card> cards;
    private GameRule gameRule;
    private Object handRank;

    public PlayerHand(int playerNumber, List<Card> cards, GameRule gameRule) {
        this.playerNumber = playerNumber;
        this.cards = cards;
        this.gameRule = gameRule;
        this.handRank = gameRule.calculateHandRank(cards);
    }

    public int getPlayerNumber() {
        return playerNumber;
    }

    public List<Card> getCards() {
        return cards;
    }

    public Object getHandRank() {
        return handRank;
    }

    @Override
    public int compareTo(PlayerHand other) {
        return gameRule.compareHands(this.handRank, other.handRank);
    }

    public boolean isSameRank(PlayerHand other) {
        return gameRule.compareHands(this.handRank, other.handRank) == 0;
    }
}
