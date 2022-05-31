package com.fyp.StudenTable.layout;


public class Exam {
    private String module, lecturer, time, date, room;
    private int id, color;

    public Exam() {}

    public Exam(String module, String lecturer, String time, String date, String room, int color) {
        this.module = module;
        this.lecturer = lecturer;
        this.time = time;
        this.date = date;
        this.room = room;
        this.color = color;
    }

    public String getModule() {
        return module;
    }

    public void setModule(String module) {
        this.module = module;
    }

    public String getLecturer() {
        return lecturer;
    }

    public void setLecturer(String lecturer) {
        this.lecturer = lecturer;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getRoom() {
        return room;
    }

    public void setRoom(String room) {
        this.room = room;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getColor() {
        return color;
    }

    public void setColor(int color) {
        this.color = color;
    }
}
