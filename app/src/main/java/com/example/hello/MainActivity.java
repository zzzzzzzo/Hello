package com.example.hello;

import android.app.assist.AssistStructure;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    TextView out;
    EditText edit;

    @Override
    protected void onCreate(Bundle savedInstanceState)  {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        out = (TextView)findViewById(R.id.txtout);
        edit = (EditText)findViewById(R.id.inp);

        Button btn = (Button)findViewById(R.id.btn);

        btn.setOnClickListener(this);

        //btn.setOnClickListener(new View.OnClickListener() {
        //    @Override
        //    public void onClick(View v) {
        //        Log.i("main","1");

        //        String str = edit.getText().toString();
        //        double number1 = Double.parseDouble(str);
        //        double number2 = number1*1.8+32;
        //        out.setText("结果为：" + number2);

        //    }
        // });
    }


    @Override
    public void onClick(View v) {
        Log.i("main","1");

        String str = edit.getText().toString();
        double number1 = Double.parseDouble(str);
        double number2 = number1*1.8+32;
        out.setText("结果为：" + number2);

    }


    //public void btnClick(View v) {
    //    Log.i("main","1");

    //    String str = edit.getText().toString();
    //    double number1 = Double.parseDouble(str);
    //    double number2 = number1*1.8+32;
    //    out.setText("结果为：" + number2);
    //}



}
