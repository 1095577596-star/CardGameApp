package com.cardgame;

import android.Manifest;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.IBinder;
import android.speech.tts.TextToSpeech;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    private static final int PERMISSION_REQUEST_CODE = 100;
    private static final String PREFS_NAME = "CardGamePrefs";
    private static final String KEY_GAME = "selected_game";
    private static final String KEY_PLAYERS = "player_count";
    private static final String KEY_VOLUME = "volume";
    private static final String KEY_BRIGHTNESS = "brightness";
    private static final String KEY_DEAL_MODE = "deal_mode";

    private Spinner gameSpinner;
    private Spinner playerCountSpinner;
    private SeekBar volumeSeekBar;
    private SeekBar brightnessSeekBar;
    private Spinner dealModeSpinner;
    private Button startButton;
    private Button stopButton;
    private Button testButton;
    private TextView resultText;
    private TextView volumeText;
    private TextView brightnessText;

    private TextToSpeech textToSpeech;
    private SharedPreferences sharedPreferences;
    private boolean isRunning = false;
    private ForegroundService foregroundService;
    private boolean serviceBound = false;

    private List<GameRule> gameRules;
    private GameRule currentRule;
    private int currentPlayerCount = 7;
    private int currentVolume = 50;
    private int currentBrightness = 50;

    private ServiceConnection serviceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            ForegroundService.LocalBinder binder = (ForegroundService.LocalBinder) service;
            foregroundService = binder.getService();
            serviceBound = true;
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            serviceBound = false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initGameRules();
        initViews();
        loadPreferences();
        initTextToSpeech();
        checkPermissions();
        startForegroundService();
    }

    private void initGameRules() {
        gameRules = new ArrayList<>();
        gameRules.add(new FortyCardRule());
        gameRules.add(new LuZhouDaErRule());
        gameRules.add(new JinHuaRule());
        gameRules.add(new DouNiuRule());
        gameRules.add(new KanShunDouNiuRule());
    }

    private void initViews() {
        gameSpinner = findViewById(R.id.gameSpinner);
        playerCountSpinner = findViewById(R.id.playerCountSpinner);
        volumeSeekBar = findViewById(R.id.volumeSeekBar);
        brightnessSeekBar = findViewById(R.id.brightnessSeekBar);
        dealModeSpinner = findViewById(R.id.dealModeSpinner);
        startButton = findViewById(R.id.startButton);
        stopButton = findViewById(R.id.stopButton);
        testButton = findViewById(R.id.testButton);
        resultText = findViewById(R.id.resultText);
        volumeText = findViewById(R.id.volumeText);
        brightnessText = findViewById(R.id.brightnessText);

        setupGameSpinner();
        setupPlayerCountSpinner();
        setupDealModeSpinner();
        setupSeekBars();
        setupButtons();
    }

    private void setupGameSpinner() {
        List<String> gameNames = new ArrayList<>();
        for (GameRule rule : gameRules) {
            gameNames.add(rule.getName());
        }
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this,
            android.R.layout.simple_spinner_item, gameNames);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        gameSpinner.setAdapter(adapter);

        gameSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                currentRule = gameRules.get(position);
                savePreferences();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
    }

    private void setupPlayerCountSpinner() {
        List<String> playerCounts = new ArrayList<>();
        for (int i = 1; i <= 10; i++) {
            playerCounts.add(i + " 人");
        }
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this,
            android.R.layout.simple_spinner_item, playerCounts);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        playerCountSpinner.setAdapter(adapter);

        playerCountSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                currentPlayerCount = position + 1;
                savePreferences();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
    }

    private void setupDealModeSpinner() {
        List<String> dealModes = new ArrayList<>();
        dealModes.add("报最大和次大");
        dealModes.add("6报第一大和第二大");
        dealModes.add("只报最大");
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this,
            android.R.layout.simple_spinner_item, dealModes);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        dealModeSpinner.setAdapter(adapter);

        dealModeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                savePreferences();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
    }

    private void setupSeekBars() {
        volumeSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                currentVolume = progress;
                volumeText.setText(progress + "%");
                setVolume(progress);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                savePreferences();
            }
        });

        brightnessSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                currentBrightness = progress;
                brightnessText.setText(progress + "%");
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                savePreferences();
            }
        });
    }

    private void setupButtons() {
        startButton.setOnClickListener(v -> startAnalysis());
        stopButton.setOnClickListener(v -> stopAnalysis());
        testButton.setOnClickListener(v -> runTest());
    }

    private void initTextToSpeech() {
        textToSpeech = new TextToSpeech(this, status -> {
            if (status == TextToSpeech.SUCCESS) {
                int result = textToSpeech.setLanguage(Locale.CHINA);
                if (result == TextToSpeech.LANG_MISSING_DATA || result == TextToSpeech.LANG_NOT_SUPPORTED) {
                    Toast.makeText(this, "语音合成不支持中文", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void setVolume(int volume) {
        if (textToSpeech != null) {
        }
    }

    private void checkPermissions() {
        List<String> permissions = new ArrayList<>();
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            permissions.add(Manifest.permission.CAMERA);
        }
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED) {
            permissions.add(Manifest.permission.RECORD_AUDIO);
        }

        if (!permissions.isEmpty()) {
            ActivityCompat.requestPermissions(this, permissions.toArray(new String[0]), PERMISSION_REQUEST_CODE);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == PERMISSION_REQUEST_CODE) {
            for (int result : grantResults) {
                if (result != PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(this, "需要权限才能正常使用", Toast.LENGTH_SHORT).show();
                    return;
                }
            }
        }
    }

    private void startForegroundService() {
        Intent serviceIntent = new Intent(this, ForegroundService.class);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            startForegroundService(serviceIntent);
        } else {
            startService(serviceIntent);
        }
        bindService(serviceIntent, serviceConnection, Context.BIND_AUTO_CREATE);
    }

    private void startAnalysis() {
        if (currentRule == null) {
            Toast.makeText(this, "请先选择游戏", Toast.LENGTH_SHORT).show();
            return;
        }
        isRunning = true;
        startButton.setEnabled(false);
        stopButton.setEnabled(true);
        resultText.setText("分析中...");
        Toast.makeText(this, "已开始分析", Toast.LENGTH_SHORT).show();
    }

    private void stopAnalysis() {
        isRunning = false;
        startButton.setEnabled(true);
        stopButton.setEnabled(false);
        resultText.setText("已停止");
        Toast.makeText(this, "已停止分析", Toast.LENGTH_SHORT).show();
    }

    private void runTest() {
        if (currentRule == null) {
            Toast.makeText(this, "请先选择游戏", Toast.LENGTH_SHORT).show();
            return;
        }

        List<PlayerHand> hands = GameAnalyzer.generateRandomHands(currentRule, currentPlayerCount);
        GameAnalyzer.AnalysisResult result = GameAnalyzer.analyze(hands);

        if (result != null) {
            resultText.setText(result.getDisplayText());
            speak(result.getSpeechText());
        }
    }

    private void speak(String text) {
        if (textToSpeech != null) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                textToSpeech.speak(text, TextToSpeech.QUEUE_FLUSH, null, null);
            } else {
                textToSpeech.speak(text, TextToSpeech.QUEUE_FLUSH, null);
            }
        }
    }

    private void loadPreferences() {
        sharedPreferences = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        int gameIndex = sharedPreferences.getInt(KEY_GAME, 0);
        int playerIndex = sharedPreferences.getInt(KEY_PLAYERS, 6);
        currentVolume = sharedPreferences.getInt(KEY_VOLUME, 50);
        currentBrightness = sharedPreferences.getInt(KEY_BRIGHTNESS, 50);
        int dealModeIndex = sharedPreferences.getInt(KEY_DEAL_MODE, 0);

        gameSpinner.setSelection(gameIndex);
        playerCountSpinner.setSelection(playerIndex);
        volumeSeekBar.setProgress(currentVolume);
        brightnessSeekBar.setProgress(currentBrightness);
        dealModeSpinner.setSelection(dealModeIndex);
        volumeText.setText(currentVolume + "%");
        brightnessText.setText(currentBrightness + "%");
    }

    private void savePreferences() {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt(KEY_GAME, gameSpinner.getSelectedItemPosition());
        editor.putInt(KEY_PLAYERS, playerCountSpinner.getSelectedItemPosition());
        editor.putInt(KEY_VOLUME, currentVolume);
        editor.putInt(KEY_BRIGHTNESS, currentBrightness);
        editor.putInt(KEY_DEAL_MODE, dealModeSpinner.getSelectedItemPosition());
        editor.apply();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (textToSpeech != null) {
            textToSpeech.stop();
            textToSpeech.shutdown();
        }
        if (serviceBound) {
            unbindService(serviceConnection);
            serviceBound = false;
        }
    }
}
