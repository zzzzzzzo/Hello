package com.example.hello;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;

public class FifthActivity extends AppCompatActivity {

    public final String TAG = "FifthActivity";

    EditText dollarText;
    EditText euroText;
    EditText wonText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fifth);

        //获得传递过来的数据
        Intent intent = getIntent();

        //获取数据（float类型）
        float dollar2 = intent.getFloatExtra("dollar_rate_key",0.0f);
        float euro2 = intent.getFloatExtra("euro_rate_key",0.0f);
        float won2 = intent.getFloatExtra("won_rate_key",0.0f);

        //获取控件
        dollarText = (EditText)findViewById(R.id.dollar_rate);
        euroText = (EditText)findViewById(R.id.euro_rate);
        wonText = (EditText)findViewById(R.id.won_rate);

        //显示数据到控件
        dollarText.setText(String.valueOf(dollar2));
        euroText.setText(String.valueOf(euro2));
        wonText.setText(String.valueOf(won2));

    }

    public void save(View btn) {

        //获取新的值
        float newDollar = Float.parseFloat(dollarText.getText().toString());
        float newEuro = Float.parseFloat(euroText.getText().toString());
        float newWon = Float.parseFloat(wonText.getText().toString());

        //保存到Bundle或放入到Extra
        Intent intent = getIntent();
        Bundle bd1 = new Bundle();
        bd1.putFloat("key_dollar",newDollar);
        bd1.putFloat("key_euro",newEuro);
        bd1.putFloat("key_won",newWon);
        intent.putExtras(bd1);
        setResult(2,intent);

        //返回到调用页面
        finish();


    }
}
