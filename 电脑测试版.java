
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Scanner;

public class 电脑测试版 {
    public static void main(String[] args) {
        System.out.println("========================================");
        System.out.println("      牌类游戏助手 - 电脑测试版");
        System.out.println("========================================");
        System.out.println();

        Scanner scanner = new Scanner(System.in);

        while (true) {
            System.out.println("请选择游戏：");
            System.out.println("1. 40张宝子");
            System.out.println("2. 金花");
            System.out.println("3. 斗牛");
            System.out.println("4. 退出");
            System.out.print("请输入选项 (1-4): ");

            String choice = scanner.nextLine();

            if (choice.equals("4")) {
                System.out.println("再见！");
                break;
            }

            GameRule rule = null;
            switch (choice) {
                case "1":
                    rule = new FortyCardRule();
                    break;
                case "2":
                    rule = new JinHuaRule();
                    break;
                case "3":
                    rule = new DouNiuRule();
                    break;
                default:
                    System.out.println("无效选项！");
                    continue;
            }

            System.out.println();
            System.out.print("请输入玩家人数 (1-10): ");
            int playerCount = Integer.parseInt(scanner.nextLine());

            System.out.println();
            System.out.println("正在生成随机牌局...");
            System.out.println();

            List&lt;PlayerHand&gt; hands = generateRandomHands(rule, playerCount);
            GameAnalyzer.AnalysisResult result = GameAnalyzer.analyze(hands);

            System.out.println("【" + rule.getName() + " - 测试结果");
            System.out.println("----------------------------------------");
            System.out.println("玩家手牌:");
            for (PlayerHand hand : hands) {
                System.out.println("  " + hand.getPlayerNumber() + "号: " + hand.getCards());
            }
            System.out.println("----------------------------------------");
            System.out.println(result.getDisplayText());
            System.out.println("----------------------------------------");
            System.out.println("【语音播报】: " + result.getSpeechText());
            System.out.println();
            System.out.println("按回车键继续...");
            scanner.nextLine();
            System.out.println();
        }
        scanner.close();
    }

    private static List&lt;PlayerHand&gt; generateRandomHands(GameRule rule, int playerCount) {
        List&lt;PlayerHand&gt; hands = new ArrayList&lt;&gt;();
        String[] suits = {"♠", "♥", "♣", "♦"};
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
    public Object getHandRank() { return handRank; }

    @Override
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
    @Override public String getName() { return "40张宝子"; }
    @Override public int getCardsPerPlayer() { return 2; }

    @Override
    public Object calculateHandRank(List&lt;Card&gt; cards) {
        if (cards.size() != 2) return new int[]{0, 0, 0};
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

    @Override public String getName() { return "金花"; }
    @Override public int getCardsPerPlayer() { return 3; }

    @Override
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

    @Override
    public int compareHands(Object rank1, Object rank2) {
        int[] r1 = (int[]) rank1;
        int[] r2 = (int[]) rank2;
        if (r1[0] != r2[0]) return Integer.compare(r1[0), r2[0]);
        if (r1[0] == PAIR &amp;&amp; r1[4] != r2[4]) return Integer.compare(r1[4), r2[4]);
        for (int i = 1; i &lt;= 3; i++) {
            if (r1[i] != r2[i]) return Integer.compare(r1[i), r2[i]);
        }
        return 0;
    }
}

class DouNiuRule implements GameRule {
    @Override public String getName() { return "斗牛"; }
    @Override public int getCardsPerPlayer() { return 5; }

    @Override
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

    protected int findNiu(List&lt;Card&gt; cards) {
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

    @Override
    public int compareHands(Object rank1, Object rank2) {
        int[] r1 = (int[]) rank1;
        int[] r2 = (int[]) rank2;
        if (r1[1] != r2[1]) return Integer.compare(r1[1), r2[1]);
        return Integer.compare(r1[2), r2[2));
    }
}

