package com.cc.anniversary;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Looper;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.cc.anniversary.db.DAO;

public abstract class baseActivity extends AppCompatActivity {
    public Context mContext;
    public DAO dao;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = this;
        dao = new DAO(mContext);
    }


    public void showToast(String msg) {
        Toast.makeText(mContext, msg, Toast.LENGTH_SHORT).show();
    }

    public void showToastSync(String msg) {
        Looper.prepare();
        Toast.makeText(mContext, msg, Toast.LENGTH_SHORT).show();
        Looper.loop();
    }

    public void navigateTo(Class cls) {
        Intent in = new Intent(mContext, cls);
        startActivity(in);
    }

    public void navigateToWithFlag(Class cls, String flags) {
        Intent in = new Intent(mContext, cls);
        in.putExtra("flags", flags);
        startActivity(in);
    }

    protected void insertVal(String key, String val) {
        SharedPreferences sp = getSharedPreferences("kkk", MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putString(key, val);
        editor.apply();
    }


    protected String findByKey(String key) {
        SharedPreferences sp = getSharedPreferences("kkk", MODE_PRIVATE);
        return sp.getString(key, "");
    }

}
