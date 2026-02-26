package com.cardgame;

public class Card implements Comparable<Card> {
    private int value;
    private String suit;

    public Card(int value, String suit) {
        this.value = value;
        this.suit = suit;
    }

    public int getValue() {
        return value;
    }

    public String getSuit() {
        return suit;
    }

    public int getPointValue() {
        if (value >= 10) return 10;
        return value;
    }

    @Override
    public int compareTo(Card other) {
        int thisRank = (this.value == 1) ? 14 : this.value;
        int otherRank = (other.value == 1) ? 14 : other.value;
        return Integer.compare(otherRank, thisRank);
    }

    @Override
    public String toString() {
        String valStr;
        if (value == 1) valStr = "A";
        else if (value == 11) valStr = "J";
        else if (value == 12) valStr = "Q";
        else if (value == 13) valStr = "K";
        else valStr = String.valueOf(value);
        return valStr + suit;
    }
}
