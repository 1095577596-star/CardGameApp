package com.cardgame;

import android.app.Activity;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

public class SimpleTestActivity extends Activity {

    private TextView resultText;
    private Button testButton;
    private TextToSpeech textToSpeech;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        LinearLayout layout = new LinearLayout(this);
        layout.setOrientation(LinearLayout.VERTICAL);
        layout.setPadding(32, 32, 32, 32);

        TextView title = new TextView(this);
        title.setText("牌类游戏助手 - 测试版");
        title.setTextSize(24);
        title.setPadding(0, 0, 0, 32);
        layout.addView(title);

        resultText = new TextView(this);
        resultText.setText("点击下方按钮测试");
        resultText.setTextSize(18);
        resultText.setPadding(0, 0, 0, 32);
        layout.addView(resultText);

        testButton = new Button(this);
        testButton.setText("测试播报");
        testButton.setOnClickListener(v -> runTest());
        layout.addView(testButton);

        setContentView(layout);

        initTextToSpeech();
    }

    private void initTextToSpeech() {
        textToSpeech = new TextToSpeech(this, status -> {
            if (status == TextToSpeech.SUCCESS) {
                textToSpeech.setLanguage(Locale.CHINA);
            }
        });
    }

    private void runTest() {
        int playerCount = 7;
        List<TestPlayer> players = new ArrayList<>();

        for (int i = 1; i <= playerCount; i++) {
            int rank = (int) (Math.random() * 100);
            players.add(new TestPlayer(i, rank));
        }

        Collections.sort(players, (a, b) -> Integer.compare(b.rank, a.rank));

        TestPlayer first = players.get(0);
        TestPlayer second = players.get(1);
        boolean isSame = Math.abs(first.rank - second.rank) < 5;

        StringBuilder display = new StringBuilder();
        display.append("最大: ").append(first.number).append("号\n");
        display.append("次大: ").append(second.number).append("号");
        if (isSame) {
            display.append("\n牌型相同");
        }

        resultText.setText(display.toString());

        StringBuilder speech = new StringBuilder();
        speech.append(first.number).append(" ").append(second.number);
        if (isSame) {
            speech.append(" 相同");
        }

        speak(speech.toString());
        Toast.makeText(this, "播报: " + speech.toString(), Toast.LENGTH_SHORT).show();
    }

    private void speak(String text) {
        if (textToSpeech != null) {
            textToSpeech.speak(text, TextToSpeech.QUEUE_FLUSH, null, null);
        }
    }

    private static class TestPlayer {
        int number;
        int rank;

        TestPlayer(int number, int rank) {
            this.number = number;
            this.rank = rank;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (textToSpeech != null) {
            textToSpeech.shutdown();
        }
    }
}
