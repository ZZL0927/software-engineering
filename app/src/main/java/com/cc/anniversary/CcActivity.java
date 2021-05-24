package com.cc.anniversary;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.Notification;
import android.app.NotificationManager;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.cardview.widget.CardView;
import androidx.core.app.NotificationCompat;

import com.cc.anniversary.db.CalendarEntity;
import com.cc.anniversary.tools.Tool;
import com.haibin.calendarview.Calendar;
import com.haibin.calendarview.CalendarLayout;
import com.haibin.calendarview.CalendarView;
import com.haibin.calendarview.TrunkBranchAnnals;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

public class CcActivity extends baseActivity implements
        CalendarView.OnCalendarSelectListener,
        CalendarView.OnCalendarLongClickListener,
        CalendarView.OnMonthChangeListener,
        CalendarView.OnYearChangeListener,
        CalendarView.OnWeekChangeListener,
        CalendarView.OnViewChangeListener,
        CalendarView.OnCalendarInterceptListener,
        CalendarView.OnYearViewChangeListener {

    private TextView mTextMonthDay;

    private TextView mTextYear;
    private TextView TextGregorianFestival;
    private final ArrayList<String> Nameless = new ArrayList<>();
    private TextView mTextLunar;

    private TextView textview_first;
    private TextView textview_des;

    private CardView today;
    private CardView today1;
    private ListView listView;

    private CalendarView mCalendarView;

    private int mYear;
    private CalendarLayout mCalendarLayout;
    private String SelectTime;
    private List<CalendarEntity> list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cc);
        initView();

    }

    private void showInfo(CalendarEntity calendarEntity, int finalI) {
        /**
         *  创建通知栏管理工具
         */

        NotificationManager notificationManager = (NotificationManager) getSystemService
                (NOTIFICATION_SERVICE);

        /**
         *  实例化通知栏构造器
         */

        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(this);

        /**
         *  设置Builder
         */
        //设置标题
        mBuilder.setContentTitle("你有新日程" + calendarEntity.getTime())
                //设置内容
                .setContentText(calendarEntity.getName())
                //设置大图标
                .setLargeIcon(BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher))
                //设置小图标
                .setSmallIcon(R.mipmap.ic_launcher_round)
                //设置通知时间
                .setWhen(System.currentTimeMillis())
                //首次进入时显示效果
                .setTicker("我是测试内容")
                //设置通知方式，声音，震动，呼吸灯等效果，这里通知方式为声音
                .setDefaults(Notification.DEFAULT_SOUND);
        //发送通知请求
        notificationManager.notify(finalI, mBuilder.build());
    }

    @Override
    protected void onStart() {
        /*修改后 即时刷新*/
        initData();
        super.onStart();
    }

    @SuppressLint("SetTextI18n")
    protected void initView() {
        mTextMonthDay = findViewById(R.id.tv_month_day);
        mTextYear = findViewById(R.id.tv_year);
        mTextLunar = findViewById(R.id.tv_lunar);
        textview_first = findViewById(R.id.textview_first);
        textview_des = findViewById(R.id.textview_des);

        today = findViewById(R.id.today);
        today1 = findViewById(R.id.today1);

        mCalendarView = findViewById(R.id.calendarView);

        //mCalendarView.setRange(2018, 7, 1, 2019, 4, 28);


        /*详情按钮*/
        TextGregorianFestival = findViewById(R.id.text);
        TextGregorianFestival.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                navigateToWithFlag(WebActivity.class, TextGregorianFestival.getText().toString());
            }
        });

        listView = findViewById(R.id.search_lv_tips);
        mCalendarLayout = findViewById(R.id.calendarLayout);
        mCalendarView.setOnYearChangeListener(this);
        mCalendarView.setOnCalendarSelectListener(this);
        mCalendarView.setOnMonthChangeListener(this);
        mCalendarView.setOnCalendarLongClickListener(this, true);
        mCalendarView.setOnWeekChangeListener(this);
        mCalendarView.setOnYearViewChangeListener(this);

        //设置日期拦截事件，仅适用单选模式，当前无效
        mCalendarView.setOnCalendarInterceptListener(this);

        mCalendarView.setOnViewChangeListener(this);
        mTextYear.setText(String.valueOf(mCalendarView.getCurYear()));
        mYear = mCalendarView.getCurYear();
        mTextMonthDay.setText(mCalendarView.getCurMonth() + "月" + mCalendarView.getCurDay() + "日");
        mTextLunar.setText("今日");

        //悬浮框
        findViewById(R.id.fab).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                navigateToWithFlag(EditActivity.class, SelectTime);
            }
        });
    }

    @SuppressWarnings("unused")
    protected void initData() {
        /*搜索框 列表*/

        final ArrayAdapter adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, Nameless);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                String itemAtPosition = (String) parent.getItemAtPosition(position);
                String[] split = itemAtPosition.split("@");
                CalendarEntity calendarEntity = new CalendarEntity();
                calendarEntity.setTime(split[1]);
                mCalendarView.scrollToCalendar(calendarEntity.getYear(), calendarEntity.getMonth(), calendarEntity.getDay());

                System.out.println();
            }
        });
        SearchView search_button = findViewById(R.id.search_button);
        search_button.setSubmitButtonEnabled(false);
        // 设置搜索文本监听
        search_button.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            // 当搜索内容改变时触发该方法
            @Override
            public boolean onQueryTextSubmit(String query) {
//                Snackbar.make(search_button, "搜索内容===" + query, Snackbar.LENGTH_SHORT).show();

                //清除焦点，收软键盘
                //mSearchView.clearFocus();

                return false;
            }

            // 当点击搜索按钮时触发该方法
            @Override
            public boolean onQueryTextChange(String newText) {
                //如果newText长度不为0
                if (TextUtils.isEmpty(newText)) {
                    listView.setVisibility(View.GONE);
                    listView.clearTextFilter();
                } else {
                    listView.setVisibility(View.VISIBLE);
//                    listView.setFilterText(newText);
                    adapter.getFilter().filter(newText);//替换成本句后消失黑框！！！
                }
                return true;
            }
        });


        final int year = mCalendarView.getCurYear();
        final int month = mCalendarView.getCurMonth();
        final int curDay = mCalendarView.getCurDay();

        /*新增按钮*/
        findViewById(R.id.iv_more).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                navigateTo(EditActivity.class);
                new DatePickerDialog(mContext, new DatePickerDialog.OnDateSetListener() {

                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int day) {
                        mCalendarView.scrollToCalendar(year, month + 1, day);
                    }
                }, year, month - 1, curDay)
                        .show(); //后三个参数相当于初始化
            }
        });
        list = dao.getInfoList();

        Map<String, Calendar> map = new HashMap<>();
        for (int i = 0; i < list.size(); i++) {
            CalendarEntity calendarEntity = list.get(i);
            Nameless.add(calendarEntity.getName() + "@" + calendarEntity.getTime());
            int finalI = i;

            String time = calendarEntity.getTime();

            /*获取当前日程的年月日*/
            int dbMonth = calendarEntity.getMonth();
            int day = calendarEntity.getDay();
            int dbYear = calendarEntity.getYear();
            /*判断是否为未来几天*/
            if (year == dbYear && dbMonth == month && curDay <= day && curDay + 5 > day) {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        /*发送通知栏通知*/
                        showInfo(list.get(finalI), finalI);
                    }
                }, new Random().nextInt(100));
            }

            Calendar schemeCalendar = getSchemeCalendar(dbYear, dbMonth, day, 0xFF40db25, "办");
            Calendar.Scheme sch = new Calendar.Scheme();
            sch.setScheme(calendarEntity.getName());
            sch.setOther(calendarEntity.getDescription());
            schemeCalendar.addScheme(sch);
            map.put(schemeCalendar.toString(),
                    schemeCalendar);
        }
        //28560 数据量增长不会影响UI响应速度，请使用这个API替换
        mCalendarView.setSchemeDate(map);

        //可自行测试性能差距
        //mCalendarView.setSchemeDate(schemes);

    }


    private Calendar getSchemeCalendar(int year, int month, int day, int color, String text) {
        Calendar calendar = new Calendar();
        calendar.setYear(year);
        calendar.setMonth(month);
        calendar.setDay(day);
        calendar.setSchemeColor(color);//如果单独标记颜色、则会使用这个颜色
        calendar.setScheme(text);
        return calendar;
    }


    @Override
    public void onCalendarOutOfRange(Calendar calendar) {
        Toast.makeText(this, String.format("%s : OutOfRange", calendar), Toast.LENGTH_SHORT).show();
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onCalendarSelect(Calendar calendar, boolean isClick) {
        //Log.e("onDateSelected", "  -- " + calendar.getYear() + "  --  " + calendar.getMonth() + "  -- " + calendar.getDay());
        mTextLunar.setVisibility(View.VISIBLE);
        mTextYear.setVisibility(View.VISIBLE);
        mTextMonthDay.setText(calendar.getMonth() + "月" + calendar.getDay() + "日");
        mTextYear.setText(String.valueOf(calendar.getYear()));
        mTextLunar.setText(calendar.getLunar());
        mYear = calendar.getYear();
        if (isClick) {
            String res = Res(calendar);
            SelectTime = mYear + Tool.getString(calendar.getMonth(), calendar.getDay());
            if (!TextUtils.isEmpty(res)) {
                TextGregorianFestival.setText(res);
                today1.setVisibility(View.VISIBLE);
            } else {
                today1.setVisibility(View.GONE);
            }

            List<Calendar.Scheme> schemes = calendar.getSchemes();
            if (schemes != null && schemes.size() > 0) {
                today.setVisibility(View.VISIBLE);
                textview_first.setText(schemes.get(0).getScheme());
                textview_des.setText(schemes.get(0).getOther());
                today.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        navigateToWithFlag(InfoActivity.class, schemes.get(0).getOther());
                    }
                });
            } else {
                today.setVisibility(View.GONE);
            }
            Log.e("onDateSelected", "isClick" + schemes);
//            Toast.makeText(this, getCalendarText(calendar), Toast.LENGTH_SHORT).show();
        }
//        Log.e("lunar "," --  " + calendar.getLunarCalendar().toString() + "\n" +
//        "  --  " + calendar.getLunarCalendar().getYear());
        Log.e("onDateSelected", "  -- " + calendar.getYear() +
                "  --  " + calendar.getMonth() +
                "  -- " + calendar.getDay() +
                "  --  " + isClick + "  --   " + calendar.getScheme());
        Log.e("onDateSelected", "  " + mCalendarView.getSelectedCalendar().getScheme() +
                "  --  " + mCalendarView.getSelectedCalendar().isCurrentDay());
        Log.e("干支年纪 ： ", " -- " + TrunkBranchAnnals.getTrunkBranchYear(calendar.getLunarCalendar().getYear()));
    }

    private String Res(Calendar calendar) {
        String gregorianFestival = calendar.getGregorianFestival();
        if (TextUtils.isEmpty(gregorianFestival)) {
            String traditionFestival = calendar.getTraditionFestival();
            if (TextUtils.isEmpty(gregorianFestival)) {
                String solarTerm = calendar.getSolarTerm();
                if (TextUtils.isEmpty(solarTerm)) {
                    return "";
                } else {
                    return solarTerm;
                }
            } else {
                return traditionFestival;
            }
        } else {
            return gregorianFestival;
        }
    }

    @Override
    public void onCalendarLongClickOutOfRange(Calendar calendar) {
        Toast.makeText(this, String.format("%s : LongClickOutOfRange", calendar), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onCalendarLongClick(Calendar calendar) {
        Toast.makeText(this, "长按不选择日期\n" + getCalendarText(calendar), Toast.LENGTH_SHORT).show();
    }

    private static String getCalendarText(Calendar calendar) {
        return String.format("新历%s \n 农历%s \n 公历节日：%s \n 农历节日：%s \n 节气：%s \n 是否闰月：%s",
                calendar.getMonth() + "月" + calendar.getDay() + "日",
                calendar.getLunarCalendar().getMonth() + "月" + calendar.getLunarCalendar().getDay() + "日",
                TextUtils.isEmpty(calendar.getGregorianFestival()) ? "无" : calendar.getGregorianFestival(),
                TextUtils.isEmpty(calendar.getTraditionFestival()) ? "无" : calendar.getTraditionFestival(),
                TextUtils.isEmpty(calendar.getSolarTerm()) ? "无" : calendar.getSolarTerm(),
                calendar.getLeapMonth() == 0 ? "否" : String.format("闰%s月", calendar.getLeapMonth()));
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onMonthChange(int year, int month) {
        Log.e("onMonthChange", "  -- " + year + "  --  " + month);
        Calendar calendar = mCalendarView.getSelectedCalendar();
        mTextLunar.setVisibility(View.VISIBLE);
        mTextYear.setVisibility(View.VISIBLE);
        mTextMonthDay.setText(calendar.getMonth() + "月" + calendar.getDay() + "日");
        mTextYear.setText(String.valueOf(calendar.getYear()));
        mTextLunar.setText(calendar.getLunar());
        mYear = calendar.getYear();
    }

    @Override
    public void onViewChange(boolean isMonthView) {
        Log.e("onViewChange", "  ---  " + (isMonthView ? "月视图" : "周视图"));
    }


    @Override
    public void onWeekChange(List<Calendar> weekCalendars) {
        for (Calendar calendar : weekCalendars) {
            Log.e("onWeekChange", calendar.toString());
        }
    }

    @Override
    public void onYearViewChange(boolean isClose) {
        Log.e("onYearViewChange", "年视图 -- " + (isClose ? "关闭" : "打开"));
    }

    /**
     * 屏蔽某些不可点击的日期，可根据自己的业务自行修改
     *
     * @param calendar calendar
     * @return 是否屏蔽某些不可点击的日期，MonthView和WeekView有类似的API可调用
     */
    @Override
    public boolean onCalendarIntercept(Calendar calendar) {
        Log.e("onCalendarIntercept", calendar.toString());
        int day = calendar.getDay();
        return day == 1 || day == 3 || day == 6 || day == 11 || day == 12 || day == 15 || day == 20 || day == 26;
    }

    @Override
    public void onCalendarInterceptClick(Calendar calendar, boolean isClick) {
        Toast.makeText(this, calendar.toString() + "拦截不可点击", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onYearChange(int year) {
        mTextMonthDay.setText(String.valueOf(year));
        Log.e("onYearChange", " 年份变化 " + year);
    }
}
