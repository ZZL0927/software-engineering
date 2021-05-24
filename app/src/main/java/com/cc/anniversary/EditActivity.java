package com.cc.anniversary;

import android.app.DatePickerDialog;
import android.database.sqlite.SQLiteConstraintException;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;

import com.cc.anniversary.db.CalendarEntity;
import com.cc.anniversary.tools.Tool;

import java.util.Calendar;

/*
 * 节日编辑页
 * */
public class EditActivity extends baseActivity {
    String tempData = null;
    private CalendarEntity calendarEntity = new CalendarEntity();
    private EditText description;
    private EditText name;
    private TextView date_picker_actions;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);

        /*initView*/
        date_picker_actions = findViewById(R.id.date_picker_actions);
        name = findViewById(R.id.name);
        description = findViewById(R.id.description);
        Button commit = findViewById(R.id.commit);

        Calendar c1 = Calendar.getInstance();

        date_picker_actions.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new DatePickerDialog(mContext, new DatePickerDialog.OnDateSetListener() {

                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int day) {
                        tempData = year + Tool.getString(month + 1, day);
                        Get(tempData);
                        date_picker_actions.setText("当前时间：" + tempData);
                    }
                }, c1.get(Calendar.YEAR), c1.get(Calendar.MONTH), c1.get(Calendar.DAY_OF_MONTH))
                        .show(); //后三个参数相当于初始化
            }
        });


        String flags = getIntent().getStringExtra("flags");
        if (flags != null) {
            Get(flags);
        }

        commit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (TextUtils.isEmpty(tempData)) {
                    return;
                }
                calendarEntity.setName(name.getText().toString())
                        .setDescription(description.getText().toString()).setTime(tempData);
                try {
                    long res = dao.insert(calendarEntity);
                    if (res == -1) {
                        showToast("新增失败！");
                    } else {
                        showToast("新增成功！");
                        finish();
                    }
                } catch (SQLiteConstraintException e) {
                    long res = dao.update(calendarEntity);
                    if (res > 0) {
                        showToast("更新成功！");
                        finish();
                    } else {
                        showToast("更新失败！");
                    }
                    e.printStackTrace();
                }

            }
        });

    }

    private void Get(String flags) {
        name.setText("");
        description.setText("");
        CalendarEntity infoByData = dao.getInfoByData(flags);

        if (infoByData != null) {
            calendarEntity = infoByData;
            tempData = infoByData.getTime();
            date_picker_actions.setText("当前时间：" + tempData);
            description.setText(infoByData.getDescription());
            name.setText(infoByData.getName());
        }
    }
}