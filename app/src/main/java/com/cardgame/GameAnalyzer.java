package com.cardgame;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class GameAnalyzer {

    public static class AnalysisResult {
        public int firstPlayer;
        public int secondPlayer;
        public boolean isSame;

        public AnalysisResult(int first, int second, boolean same) {
            this.firstPlayer = first;
            this.secondPlayer = second;
            this.isSame = same;
        }

        public String getSpeechText() {
            StringBuilder sb = new StringBuilder();
            sb.append(firstPlayer).append(" ").append(secondPlayer);
            if (isSame) {
                sb.append(" 相同");
            }
            return sb.toString();
        }

        public String getDisplayText() {
            StringBuilder sb = new StringBuilder();
            sb.append("最大: ").append(firstPlayer).append("号\n");
            sb.append("次大: ").append(secondPlayer).append("号");
            if (isSame) {
                sb.append("\n牌型相同");
            }
            return sb.toString();
        }
    }

    public static AnalysisResult analyze(List<PlayerHand> hands) {
        if (hands.size() < 2) {
            return null;
        }

        List<PlayerHand> sortedHands = new ArrayList<>(hands);
        Collections.sort(sortedHands, Collections.reverseOrder());

        PlayerHand first = sortedHands.get(0);
        PlayerHand second = sortedHands.get(1);

        boolean isSame = first.isSameRank(second);

        return new AnalysisResult(first.getPlayerNumber(), second.getPlayerNumber(), isSame);
    }

    public static List<PlayerHand> generateRandomHands(GameRule rule, int playerCount) {
        List<PlayerHand> hands = new ArrayList<>();
        String[] suits = {"♠", "♥", "♣", "♦"};
        List<Card> deck = new ArrayList<>();

        for (int v = 1; v <= 13; v++) {
            for (String s : suits) {
                deck.add(new Card(v, s));
            }
        }
        Collections.shuffle(deck);

        int cardIndex = 0;
        for (int i = 1; i <= playerCount; i++) {
            List<Card> playerCards = new ArrayList<>();
            for (int j = 0; j < rule.getCardsPerPlayer(); j++) {
                if (cardIndex < deck.size()) {
                    playerCards.add(deck.get(cardIndex++));
                }
            }
            hands.add(new PlayerHand(i, playerCards, rule));
        }

        return hands;
    }
}
