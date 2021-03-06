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
         *  ???????????????????????????
         */

        NotificationManager notificationManager = (NotificationManager) getSystemService
                (NOTIFICATION_SERVICE);

        /**
         *  ???????????????????????????
         */

        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(this);

        /**
         *  ??????Builder
         */
        //????????????
        mBuilder.setContentTitle("???????????????" + calendarEntity.getTime())
                //????????????
                .setContentText(calendarEntity.getName())
                //???????????????
                .setLargeIcon(BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher))
                //???????????????
                .setSmallIcon(R.mipmap.ic_launcher_round)
                //??????????????????
                .setWhen(System.currentTimeMillis())
                //???????????????????????????
                .setTicker("??????????????????")
                //???????????????????????????????????????????????????????????????????????????????????????
                .setDefaults(Notification.DEFAULT_SOUND);
        //??????????????????
        notificationManager.notify(finalI, mBuilder.build());
    }

    @Override
    protected void onStart() {
        /*????????? ????????????*/
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


        /*????????????*/
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

        //???????????????????????????????????????????????????????????????
        mCalendarView.setOnCalendarInterceptListener(this);

        mCalendarView.setOnViewChangeListener(this);
        mTextYear.setText(String.valueOf(mCalendarView.getCurYear()));
        mYear = mCalendarView.getCurYear();
        mTextMonthDay.setText(mCalendarView.getCurMonth() + "???" + mCalendarView.getCurDay() + "???");
        mTextLunar.setText("??????");

        //?????????
        findViewById(R.id.fab).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                navigateToWithFlag(EditActivity.class, SelectTime);
            }
        });
    }

    @SuppressWarnings("unused")
    protected void initData() {
        /*????????? ??????*/

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
        // ????????????????????????
        search_button.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            // ???????????????????????????????????????
            @Override
            public boolean onQueryTextSubmit(String query) {
//                Snackbar.make(search_button, "????????????===" + query, Snackbar.LENGTH_SHORT).show();

                //???????????????????????????
                //mSearchView.clearFocus();

                return false;
            }

            // ???????????????????????????????????????
            @Override
            public boolean onQueryTextChange(String newText) {
                //??????newText????????????0
                if (TextUtils.isEmpty(newText)) {
                    listView.setVisibility(View.GONE);
                    listView.clearTextFilter();
                } else {
                    listView.setVisibility(View.VISIBLE);
//                    listView.setFilterText(newText);
                    adapter.getFilter().filter(newText);//???????????????????????????????????????
                }
                return true;
            }
        });


        final int year = mCalendarView.getCurYear();
        final int month = mCalendarView.getCurMonth();
        final int curDay = mCalendarView.getCurDay();

        /*????????????*/
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
                        .show(); //?????????????????????????????????
            }
        });
        list = dao.getInfoList();

        Map<String, Calendar> map = new HashMap<>();
        for (int i = 0; i < list.size(); i++) {
            CalendarEntity calendarEntity = list.get(i);
            Nameless.add(calendarEntity.getName() + "@" + calendarEntity.getTime());
            int finalI = i;

            String time = calendarEntity.getTime();

            /*??????????????????????????????*/
            int dbMonth = calendarEntity.getMonth();
            int day = calendarEntity.getDay();
            int dbYear = calendarEntity.getYear();
            /*???????????????????????????*/
            if (year == dbYear && dbMonth == month && curDay <= day && curDay + 5 > day) {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        /*?????????????????????*/
                        showInfo(list.get(finalI), finalI);
                    }
                }, new Random().nextInt(100));
            }

            Calendar schemeCalendar = getSchemeCalendar(dbYear, dbMonth, day, 0xFF40db25, "???");
            Calendar.Scheme sch = new Calendar.Scheme();
            sch.setScheme(calendarEntity.getName());
            sch.setOther(calendarEntity.getDescription());
            schemeCalendar.addScheme(sch);
            map.put(schemeCalendar.toString(),
                    schemeCalendar);
        }
        //28560 ???????????????????????????UI??????????????????????????????API??????
        mCalendarView.setSchemeDate(map);

        //???????????????????????????
        //mCalendarView.setSchemeDate(schemes);

    }


    private Calendar getSchemeCalendar(int year, int month, int day, int color, String text) {
        Calendar calendar = new Calendar();
        calendar.setYear(year);
        calendar.setMonth(month);
        calendar.setDay(day);
        calendar.setSchemeColor(color);//???????????????????????????????????????????????????
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
        mTextMonthDay.setText(calendar.getMonth() + "???" + calendar.getDay() + "???");
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
        Log.e("???????????? ??? ", " -- " + TrunkBranchAnnals.getTrunkBranchYear(calendar.getLunarCalendar().getYear()));
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
        Toast.makeText(this, "?????????????????????\n" + getCalendarText(calendar), Toast.LENGTH_SHORT).show();
    }

    private static String getCalendarText(Calendar calendar) {
        return String.format("??????%s \n ??????%s \n ???????????????%s \n ???????????????%s \n ?????????%s \n ???????????????%s",
                calendar.getMonth() + "???" + calendar.getDay() + "???",
                calendar.getLunarCalendar().getMonth() + "???" + calendar.getLunarCalendar().getDay() + "???",
                TextUtils.isEmpty(calendar.getGregorianFestival()) ? "???" : calendar.getGregorianFestival(),
                TextUtils.isEmpty(calendar.getTraditionFestival()) ? "???" : calendar.getTraditionFestival(),
                TextUtils.isEmpty(calendar.getSolarTerm()) ? "???" : calendar.getSolarTerm(),
                calendar.getLeapMonth() == 0 ? "???" : String.format("???%s???", calendar.getLeapMonth()));
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onMonthChange(int year, int month) {
        Log.e("onMonthChange", "  -- " + year + "  --  " + month);
        Calendar calendar = mCalendarView.getSelectedCalendar();
        mTextLunar.setVisibility(View.VISIBLE);
        mTextYear.setVisibility(View.VISIBLE);
        mTextMonthDay.setText(calendar.getMonth() + "???" + calendar.getDay() + "???");
        mTextYear.setText(String.valueOf(calendar.getYear()));
        mTextLunar.setText(calendar.getLunar());
        mYear = calendar.getYear();
    }

    @Override
    public void onViewChange(boolean isMonthView) {
        Log.e("onViewChange", "  ---  " + (isMonthView ? "?????????" : "?????????"));
    }


    @Override
    public void onWeekChange(List<Calendar> weekCalendars) {
        for (Calendar calendar : weekCalendars) {
            Log.e("onWeekChange", calendar.toString());
        }
    }

    @Override
    public void onYearViewChange(boolean isClose) {
        Log.e("onYearViewChange", "????????? -- " + (isClose ? "??????" : "??????"));
    }

    /**
     * ????????????????????????????????????????????????????????????????????????
     *
     * @param calendar calendar
     * @return ??????????????????????????????????????????MonthView???WeekView????????????API?????????
     */
    @Override
    public boolean onCalendarIntercept(Calendar calendar) {
        Log.e("onCalendarIntercept", calendar.toString());
        int day = calendar.getDay();
        return day == 1 || day == 3 || day == 6 || day == 11 || day == 12 || day == 15 || day == 20 || day == 26;
    }

    @Override
    public void onCalendarInterceptClick(Calendar calendar, boolean isClick) {
        Toast.makeText(this, calendar.toString() + "??????????????????", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onYearChange(int year) {
        mTextMonthDay.setText(String.valueOf(year));
        Log.e("onYearChange", " ???????????? " + year);
    }
}
