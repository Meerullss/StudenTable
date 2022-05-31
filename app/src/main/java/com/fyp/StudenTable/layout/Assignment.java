package com.fyp.StudenTable.layout;

public class Assignment {
    private String module, description, date;
    private int id, color;

    public Assignment() {}

    public Assignment(String module, String description, String date, int color) {
        this.module = module;
        this.description = description;
        this.date = date;
        this.color = color;
    }
    public String getModule() {
        return module;
    }

    public void setModule(String module) {
        this.module = module;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
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
