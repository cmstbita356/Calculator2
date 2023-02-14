package com.example.calculator;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.material.button.MaterialButton;

public class HistoryActivity extends AppCompatActivity {
    private static final String KEY_HISTORY = "history";
    String textHis;
    int historyCode = 1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);

        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        textHis = bundle.getString(KEY_HISTORY);
        TextView txv = findViewById(R.id.history);
        txv.setText(textHis);

        Button clearHistory = findViewById(R.id.clearHistory);
        clearHistory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                textHis = "";
                txv.setText(textHis);
                historyCode = 0;
            }
        });

        Button btnReturn = findViewById(R.id.returnToCal);
        btnReturn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent resultIntent = new Intent();
                resultIntent.putExtra("result", "Đã trở về Calculator");
                setResult(RESULT_OK, resultIntent);
                finish();
            }
        });

    }
}