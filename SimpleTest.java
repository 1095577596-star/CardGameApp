
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class SimpleTest {
    public static void main(String[] args) {
        System.out.println("========================================");
        System.out.println("      牌类游戏助手 - 简单测试版");
        System.out.println("========================================");
        System.out.println();

        System.out.println("测试1：40张宝子（7人）");
        testGame(new FortyCardRule(), 7);
        System.out.println();

        System.out.println("测试2：金花（7人）");
        testGame(new JinHuaRule(), 7);
        System.out.println();

        System.out.println("测试3：斗牛（7人）");
        testGame(new DouNiuRule(), 7);
        System.out.println();

        System.out.println("测试完成！");
    }

    private static void testGame(GameRule rule, int playerCount) {
        List&lt;PlayerHand&gt; hands = generateRandomHands(rule, playerCount);
        GameAnalyzer.AnalysisResult result = GameAnalyzer.analyze(hands);

        System.out.println("----------------------------------------");
        System.out.println("玩家手牌:");
        for (PlayerHand hand : hands) {
            System.out.println("  " + hand.getPlayerNumber() + "号: " + hand.getCards());
        }
        System.out.println("----------------------------------------");
        System.out.println(result.getDisplayText());
        System.out.println("----------------------------------------");
        System.out.println("【语音播报】: " + result.getSpeechText());
    }

    private static List&lt;PlayerHand&gt; generateRandomHands(GameRule rule, int playerCount) {
        List&lt;PlayerHand&gt; hands = new ArrayList&lt;&gt;();
        String[] suits = {"S", "H", "C", "D"};
        List&lt;Card&gt; deck = new ArrayList&lt;&gt;();

        for (int v = 1; v &lt;= 13; v++) {
            for (String s : suits) {
                deck.add(new Card(v, s));
            }
        }
        Collections.shuffle(deck);

        int cardIndex = 0;
        for (int i = 1; i &lt;= playerCount; i++) {
            List&lt;Card&gt; playerCards = new ArrayList&lt;&gt;();
            for (int j = 0; j &lt; rule.getCardsPerPlayer(); j++) {
                if (cardIndex &lt; deck.size()) {
                    playerCards.add(deck.get(cardIndex++));
                }
            }
            hands.add(new PlayerHand(i, playerCards, rule));
        }
        return hands;
    }
}

class Card {
    private int value;
    private String suit;

    public Card(int value, String suit) {
        this.value = value;
        this.suit = suit;
    }

    public int getValue() { return value; }
    public String getSuit() { return suit; }

    public int getPointValue() {
        if (value &gt;= 10) return 10;
        return value;
    }

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

interface GameRule {
    String getName();
    int getCardsPerPlayer();
    Object calculateHandRank(List&lt;Card&gt; cards);
    int compareHands(Object rank1, Object rank2);
}

class PlayerHand implements Comparable&lt;PlayerHand&gt; {
    private int playerNumber;
    private List&lt;Card&gt; cards;
    private GameRule gameRule;
    private Object handRank;

    public PlayerHand(int playerNumber, List&lt;Card&gt; cards, GameRule gameRule) {
        this.playerNumber = playerNumber;
        this.cards = cards;
        this.gameRule = gameRule;
        this.handRank = gameRule.calculateHandRank(cards);
    }

    public int getPlayerNumber() { return playerNumber; }
    public List&lt;Card&gt; getCards() { return cards; }

    public int compareTo(PlayerHand other) {
        return gameRule.compareHands(this.handRank, other.handRank);
    }

    public boolean isSameRank(PlayerHand other) {
        return gameRule.compareHands(this.handRank, other.handRank) == 0;
    }
}

class GameAnalyzer {
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
            String result = firstPlayer + " " + secondPlayer;
            if (isSame) {
                result += " 相同";
            }
            return result;
        }

        public String getDisplayText() {
            String result = "最大: " + firstPlayer + "号\n次大: " + secondPlayer + "号";
            if (isSame) {
                result += "\n牌型相同";
            }
            return result;
        }
    }

    public static AnalysisResult analyze(List&lt;PlayerHand&gt; hands) {
        if (hands.size() &lt; 2) return null;

        List&lt;PlayerHand&gt; sortedHands = new ArrayList&lt;&gt;(hands);
        Collections.sort(sortedHands, Collections.reverseOrder());

        PlayerHand first = sortedHands.get(0);
        PlayerHand second = sortedHands.get(1);
        boolean isSame = first.isSameRank(second);

        return new AnalysisResult(first.getPlayerNumber(), second.getPlayerNumber(), isSame);
    }
}

class FortyCardRule implements GameRule {
    public String getName() { return "40张宝子"; }
    public int getCardsPerPlayer() { return 2; }

    public Object calculateHandRank(List&lt;Card&gt; cards) {
        if (cards.size() != 2) return new int[]{0, 0, 0};
        Card c1 = cards.get(0);
        Card c2 = cards.get(1);
        boolean isPair = c1.getValue() == c2.getValue();
        int pairValue = isPair ? (c1.getValue() == 1 ? 14 : c1.getValue()) : 0;
        int pointSum = (c1.getPointValue() + c2.getPointValue()) % 10;
        return new int[]{isPair ? 1 : 0, pairValue, pointSum};
    }

    public int compareHands(Object rank1, Object rank2) {
        int[] r1 = (int[]) rank1;
        int[] r2 = (int[]) rank2;
        if (r1[0] != r2[0]) return Integer.compare(r1[0], r2[0]);
        if (r1[0] == 1) return Integer.compare(r1[1], r2[1]);
        return Integer.compare(r1[2], r2[2]);
    }
}

class JinHuaRule implements GameRule {
    private static final int LEOPARD = 6;
    private static final int STRAIGHT_FLUSH = 5;
    private static final int FLUSH = 4;
    private static final int STRAIGHT = 3;
    private static final int PAIR = 2;
    private static final int HIGH_CARD = 1;

    public String getName() { return "金花"; }
    public int getCardsPerPlayer() { return 3; }

    public Object calculateHandRank(List&lt;Card&gt; cards) {
        if (cards.size() != 3) return new int[]{0, 0, 0, 0, 0};
        List&lt;Integer&gt; values = new ArrayList&lt;&gt;();
        List&lt;String&gt; suits = new ArrayList&lt;&gt;();
        for (Card c : cards) {
            int v = c.getValue() == 1 ? 14 : c.getValue();
            values.add(v);
            suits.add(c.getSuit());
        }
        Collections.sort(values, Collections.reverseOrder());

        boolean isLeopard = values.get(0).equals(values.get(1)) &amp;&amp; values.get(1).equals(values.get(2));
        boolean isFlush = suits.get(0).equals(suits.get(1)) &amp;&amp; suits.get(1).equals(suits.get(2));
        boolean isStraight = (values.get(0) - values.get(1) == 1 &amp;&amp; values.get(1) - values.get(2) == 1) ||
                            (values.get(0) == 14 &amp;&amp; values.get(1) == 3 &amp;&amp; values.get(2) == 2);
        boolean isPair = values.get(0).equals(values.get(1)) || values.get(1).equals(values.get(2)) || values.get(0).equals(values.get(2));

        int type;
        if (isLeopard) type = LEOPARD;
        else if (isStraight &amp;&amp; isFlush) type = STRAIGHT_FLUSH;
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

    public int compareHands(Object rank1, Object rank2) {
        int[] r1 = (int[]) rank1;
        int[] r2 = (int[]) rank2;
        if (r1[0] != r2[0]) return Integer.compare(r1[0], r2[0]);
        if (r1[0] == PAIR &amp;&amp; r1[4] != r2[4]) return Integer.compare(r1[4], r2[4]);
        for (int i = 1; i &lt;= 3; i++) {
            if (r1[i] != r2[i]) return Integer.compare(r1[i], r2[i]);
        }
        return 0;
    }
}

class DouNiuRule implements GameRule {
    public String getName() { return "斗牛"; }
    public int getCardsPerPlayer() { return 5; }

    public Object calculateHandRank(List&lt;Card&gt; cards) {
        if (cards.size() != 5) return new int[]{0, 0, 0};
        List&lt;Integer&gt; values = new ArrayList&lt;&gt;();
        for (Card c : cards) {
            values.add(c.getValue() == 1 ? 14 : c.getValue());
        }
        Collections.sort(values, Collections.reverseOrder());
        int niu = findNiu(cards);
        int maxCard = values.get(0);
        return new int[]{0, niu, maxCard};
    }

    private int findNiu(List&lt;Card&gt; cards) {
        int[] points = new int[5];
        for (int i = 0; i &lt; 5; i++) {
            points[i] = cards.get(i).getPointValue();
        }
        for (int i = 0; i &lt; 5; i++) {
            for (int j = i + 1; j &lt; 5; j++) {
                for (int k = j + 1; k &lt; 5; k++) {
                    if ((points[i] + points[j] + points[k]) % 10 == 0) {
                        int sum = 0;
                        for (int m = 0; m &lt; 5; m++) {
                            if (m != i &amp;&amp; m != j &amp;&amp; m != k) {
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

    public int compareHands(Object rank1, Object rank2) {
        int[] r1 = (int[]) rank1;
        int[] r2 = (int[]) rank2;
        if (r1[1] != r2[1]) return Integer.compare(r1[1], r2[1]);
        return Integer.compare(r1[2], r2[2]);
    }
}

