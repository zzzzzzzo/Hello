package com.example.hello;

import android.os.PersistableBundle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class ThirdActivity extends AppCompatActivity {
    TextView scoreA;
    TextView scoreB;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_third);

        scoreA = (TextView)findViewById(R.id.scoreA);
        scoreB = (TextView)findViewById(R.id.scoreB);
    }

    @Override
    public void onSaveInstanceState(Bundle outState, PersistableBundle outPersistentState) {
        super.onSaveInstanceState(outState, outPersistentState);
        String scorea = scoreA.getText().toString();
        String scoreb = scoreB.getText().toString();

        outState.putString("teama_score",scorea);
        outState.putString("teamb_score",scoreb);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        String scorea = savedInstanceState.getString("teama_score");
        String scoreb = savedInstanceState.getString("teamb_score");

        scoreA .setText(scorea);
        scoreB .setText(scoreb);
    }

    public void btnAdd1(View btn) {
        if (btn.getId() == R.id.btn1A) {
            showScoreA(1);
        } else {
            showScoreB(1);
        }
    }

    public void btnAdd2(View btn) {
        if (btn.getId() == R.id.btn2A) {
            showScoreA(2);
        } else {
            showScoreB(2);
        }
    }

    public void btnAdd3(View btn) {
        if (btn.getId() == R.id.btn3A) {
            showScoreA(3);
        } else {
            showScoreB(3);
        }
    }

    public void btnReset(View btn) {
        scoreA.setText("0");
        scoreB.setText("0");
    }

    private void showScoreA(int inc) {
        String oldSorse = (String) scoreA.getText();
        int newScore = Integer.parseInt(oldSorse) + inc;
        scoreA.setText("" + newScore);
    }

    private void showScoreB(int inc) {
        String oldSorse = (String) scoreB.getText();
        int newScore = Integer.parseInt(oldSorse) + inc;
        scoreB.setText("" + newScore);
    }
}
