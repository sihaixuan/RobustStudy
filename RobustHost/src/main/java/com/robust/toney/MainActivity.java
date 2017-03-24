package com.robust.toney;

import android.app.Activity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    static Activity activity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        TextView textView = (TextView) findViewById(R.id.txt);
        MoneyBean bean = new MoneyBean();
        String content = "MoneyValue : " + bean.getMoneyValue() +",desc : " + MoneyBean.desc() ;
        textView.setText(content);
        activity = this;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

    }
}
