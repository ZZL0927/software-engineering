package com.cc.anniversary;

import android.os.Bundle;
import android.widget.TextView;

/*
 * 节日详情页
 * */
public class InfoActivity extends baseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info);
        TextView info = findViewById(R.id.info);
        String flags = getIntent().getStringExtra("flags");
        info.setText(flags);

    }
}