package com.cc.anniversary.db;

public class CalendarEntity {
    private String id;
    private String time;
    private String name;
    public final static String NAME = "name";
    public final static String TIME = "time";
    public final static String DESCRIPTION = "description";
    private String description;

    public String getTime() {
        return time;
    }

    public String getId() {
        return id;
    }

    public CalendarEntity setId(String id) {
        this.id = id;
        return this;
    }


    @Override
    public String toString() {
        return "CalendarEntity{" +
                "id='" + id + '\'' +
                ", time='" + time + '\'' +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                '}';
    }

    public CalendarEntity setTime(String time) {
        this.time = time;
        return this;
    }

    public String getName() {
        return name;
    }

    public CalendarEntity setName(String name) {
        this.name = name;
        return this;
    }

    public String getDescription() {
        return description;
    }

    public CalendarEntity setDescription(String description) {
        this.description = description;
        return this;
    }

    public int getMonth() {
        return Integer.parseInt(time.substring(4, 6));
    }

    public int getDay() {
        return Integer.parseInt(time.substring(6));
    }

    public int getYear() {
        return Integer.parseInt(time.substring(0, 4));
    }
}
