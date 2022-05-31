package com.fyp.StudenTable.misc;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.fyp.StudenTable.layout.Assignment;
import com.fyp.StudenTable.layout.Exam;
import com.fyp.StudenTable.layout.Lecturer;
import com.fyp.StudenTable.layout.Notepad;
import com.fyp.StudenTable.layout.Week;

import java.util.ArrayList;


public class DbSupport extends SQLiteOpenHelper{

    private static final int DB_VERSION = 6;
    private static final String DB_NAME = "timetabledb";
    private static final String TIMETABLE = "timetable";
    private static final String WEEK_ID = "id";
    private static final String WEEK_MODULE = "subject";
    private static final String WEEK_FRAGMENT = "fragment";
    private static final String WEEK_LECTURER = "teacher";
    private static final String WEEK_ROOM = "room";
    private static final String WEEK_FROM_TIME = "fromtime";
    private static final String WEEK_TO_TIME = "totime";
    private static final String WEEK_COLOR = "color";

    private static final String ASSIGNMENTS = "homeworks";
    private static final String ASSIGNMENTS_ID  = "id";
    private static final String ASSIGNMENTS_MODULE = "subject";
    private static final String ASSIGNMENTS_DESCRIPTION = "description";
    private static final String ASSIGNMENTS_DATE = "date";
    private static final String ASSIGNMENTS_COLOR = "color";

    private static final String NOTEPAD = "notes";
    private static final String NOTEPAD_ID = "id";
    private static final String NOTEPAD_TITLE = "title";
    private static final String NOTEPAD_TEXT = "text";
    private static final String NOTEPAD_COLOR = "color";

    private static final String LECTURERS = "teachers";
    private static final String LECTURERS_ID = "id";
    private static final String LECTURERS_NAME = "name";
    private static final String LECTURERS_POST = "post";
    private static final String LECTURERS_PHONE_NUMBER = "phonenumber";
    private static final String LECTURERS_EMAIL = "email";
    private static final String LECTURERS_COLOR = "color";

    private static final String EXAMS = "exams";
    private static final String EXAMS_ID = "id";
    private static final String EXAMS_MODULE = "subject";
    private static final String EXAMS_LECTURER = "teacher";
    private static final String EXAMS_ROOM = "room";
    private static final String EXAMS_DATE = "date";
    private static final String EXAMS_TIME = "time";
    private static final String EXAMS_COLOR = "color";


    public DbSupport(Context context){
        super(context , DB_NAME, null, DB_VERSION);
    }

     public void onCreate(SQLiteDatabase db) {
        String CREATE_TIMETABLE = "CREATE TABLE " + TIMETABLE + "("
                + WEEK_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + WEEK_MODULE + " TEXT,"
                + WEEK_FRAGMENT + " TEXT,"
                + WEEK_LECTURER + " TEXT,"
                + WEEK_ROOM + " TEXT,"
                + WEEK_FROM_TIME + " TEXT,"
                + WEEK_TO_TIME + " TEXT,"
                + WEEK_COLOR + " INTEGER" +  ")";

        String CREATE_ASSIGNMENTS = "CREATE TABLE " + ASSIGNMENTS + "("
                + ASSIGNMENTS_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + ASSIGNMENTS_MODULE + " TEXT,"
                + ASSIGNMENTS_DESCRIPTION + " TEXT,"
                + ASSIGNMENTS_DATE + " TEXT,"
                + ASSIGNMENTS_COLOR + " INTEGER" + ")";

        String CREATE_NOTEPAD = "CREATE TABLE " + NOTEPAD + "("
                + NOTEPAD_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + NOTEPAD_TITLE + " TEXT,"
                + NOTEPAD_TEXT + " TEXT,"
                + NOTEPAD_COLOR + " INTEGER" + ")";

        String CREATE_LECTURERS = "CREATE TABLE " + LECTURERS + "("
                + LECTURERS_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + LECTURERS_NAME + " TEXT,"
                + LECTURERS_POST + " TEXT,"
                + LECTURERS_PHONE_NUMBER + " TEXT,"
                + LECTURERS_EMAIL + " TEXT,"
                + LECTURERS_COLOR + " INTEGER" + ")";

        String CREATE_EXAMS = "CREATE TABLE " + EXAMS + "("
                + EXAMS_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + EXAMS_MODULE + " TEXT,"
                + EXAMS_LECTURER + " TEXT,"
                + EXAMS_ROOM + " TEXT,"
                + EXAMS_DATE + " TEXT,"
                + EXAMS_TIME + " TEXT,"
                + EXAMS_COLOR + " INTEGER" + ")";

        db.execSQL(CREATE_TIMETABLE);
        db.execSQL(CREATE_ASSIGNMENTS);
        db.execSQL(CREATE_NOTEPAD);
        db.execSQL(CREATE_LECTURERS);
        db.execSQL(CREATE_EXAMS);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        switch (oldVersion) {
            case 1:
                db.execSQL("DROP TABLE IF EXISTS " + TIMETABLE);

            case 2:
                db.execSQL("DROP TABLE IF EXISTS " + ASSIGNMENTS);

            case 3:
                db.execSQL("DROP TABLE IF EXISTS " + NOTEPAD);

            case 4:
                db.execSQL("DROP TABLE IF EXISTS " + LECTURERS);

            case 5:
                db.execSQL("DROP TABLE IF EXISTS " + EXAMS);
                break;
        }
        onCreate(db);
    }

    /**
     * Methods for Week fragments
     **/
    public void insertWeek(Week week){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(WEEK_MODULE, week.getModule());
        contentValues.put(WEEK_FRAGMENT, week.getFragment());
        contentValues.put(WEEK_LECTURER, week.getLecturer());
        contentValues.put(WEEK_ROOM, week.getRoom());
        contentValues.put(WEEK_FROM_TIME, week.getFromTime());
        contentValues.put(WEEK_TO_TIME, week.getToTime());
        contentValues.put(WEEK_COLOR, week.getColor());
        db.insert(TIMETABLE,null, contentValues);
        db.update(TIMETABLE, contentValues, WEEK_FRAGMENT, null);
        db.close();
    }

    public void deleteWeekById(Week week) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TIMETABLE, WEEK_ID + " = ? ", new String[]{String.valueOf(week.getId())});
        db.close();
    }

    public void updateWeek(Week week) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(WEEK_MODULE, week.getModule());
        contentValues.put(WEEK_LECTURER, week.getLecturer());
        contentValues.put(WEEK_ROOM, week.getRoom());
        contentValues.put(WEEK_FROM_TIME,week.getFromTime());
        contentValues.put(WEEK_TO_TIME, week.getToTime());
        contentValues.put(WEEK_COLOR, week.getColor());
        db.update(TIMETABLE, contentValues, WEEK_ID + " = " + week.getId(), null);
        db.close();
    }

    public ArrayList<Week> getWeek(String fragment){
        SQLiteDatabase db = this.getWritableDatabase();

        ArrayList<Week> weeklist = new ArrayList<>();
        Week week;
        Cursor cursor = db.rawQuery("SELECT * FROM ( SELECT * FROM "+TIMETABLE+" ORDER BY " + WEEK_FROM_TIME + " ) WHERE "+ WEEK_FRAGMENT +" LIKE '"+fragment+"%'",null);
        while (cursor.moveToNext()){
            week = new Week();
            week.setId(cursor.getInt(cursor.getColumnIndex(WEEK_ID)));
            week.setModule(cursor.getString(cursor.getColumnIndex(WEEK_MODULE)));
            week.setLecturer(cursor.getString(cursor.getColumnIndex(WEEK_LECTURER)));
            week.setRoom(cursor.getString(cursor.getColumnIndex(WEEK_ROOM)));
            week.setFromTime(cursor.getString(cursor.getColumnIndex(WEEK_FROM_TIME)));
            week.setToTime(cursor.getString(cursor.getColumnIndex(WEEK_TO_TIME)));
            week.setColor(cursor.getInt(cursor.getColumnIndex(WEEK_COLOR)));
            weeklist.add(week);
        }
        return  weeklist;
    }

    /**
     * Methods for Homeworks activity
     **/
    public void insertAssignment(Assignment assignment) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(ASSIGNMENTS_MODULE, assignment.getModule());
        contentValues.put(ASSIGNMENTS_DESCRIPTION, assignment.getDescription());
        contentValues.put(ASSIGNMENTS_DATE, assignment.getDate());
        contentValues.put(ASSIGNMENTS_COLOR, assignment.getColor());
        db.insert(ASSIGNMENTS,null, contentValues);
        db.close();
    }

    public void updateAssignment(Assignment assignment) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(ASSIGNMENTS_MODULE, assignment.getModule());
        contentValues.put(ASSIGNMENTS_DESCRIPTION, assignment.getDescription());
        contentValues.put(ASSIGNMENTS_DATE, assignment.getDate());
        contentValues.put(ASSIGNMENTS_COLOR, assignment.getColor());
        db.update(ASSIGNMENTS, contentValues, ASSIGNMENTS_ID + " = " + assignment.getId(), null);
        db.close();
    }

    public void deleteAssignmentById(Assignment assignment) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(ASSIGNMENTS,ASSIGNMENTS_ID + " = ? ", new String[]{String.valueOf(assignment.getId())});
        db.close();
    }


    public ArrayList<Assignment> getAssignment() {
        SQLiteDatabase db = this.getWritableDatabase();
        ArrayList<Assignment> homelist = new ArrayList<>();
        Assignment assignment;
        Cursor cursor = db.rawQuery("SELECT * FROM "+ ASSIGNMENTS + " ORDER BY datetime(" + ASSIGNMENTS_DATE + ") ASC",null);
        while (cursor.moveToNext()){
            assignment = new Assignment();
            assignment.setId(cursor.getInt(cursor.getColumnIndex(ASSIGNMENTS_ID)));
            assignment.setModule(cursor.getString(cursor.getColumnIndex(ASSIGNMENTS_MODULE)));
            assignment.setDescription(cursor.getString(cursor.getColumnIndex(ASSIGNMENTS_DESCRIPTION)));
            assignment.setDate(cursor.getString(cursor.getColumnIndex(ASSIGNMENTS_DATE)));
            assignment.setColor(cursor.getInt(cursor.getColumnIndex(ASSIGNMENTS_COLOR)));
            homelist.add(assignment);
        }
        cursor.close();
        db.close();
        return  homelist;
    }

    /**
     * Methods for Notes activity
     **/
    public void insertNotepad(Notepad notepad) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(NOTEPAD_TITLE, notepad.getTitle());
        contentValues.put(NOTEPAD_TEXT, notepad.getText());
        contentValues.put(NOTEPAD_COLOR, notepad.getColor());
        db.insert(NOTEPAD, null, contentValues);
        db.close();
    }

    public void updateNotepad(Notepad notepad)  {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(NOTEPAD_TITLE, notepad.getTitle());
        contentValues.put(NOTEPAD_TEXT, notepad.getText());
        contentValues.put(NOTEPAD_COLOR, notepad.getColor());
        db.update(NOTEPAD, contentValues, NOTEPAD_ID + " = " + notepad.getId(), null);
        db.close();
    }

    public void deleteNotepadById(Notepad notepad) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(NOTEPAD, NOTEPAD_ID + " =? ", new String[] {String.valueOf(notepad.getId())});
        db.close();
    }

    public ArrayList<Notepad> getNotepad() {
        SQLiteDatabase db = this.getWritableDatabase();
        ArrayList<Notepad> notelist = new ArrayList<>();
        Notepad notepad;
        Cursor cursor = db.rawQuery("SELECT * FROM " + NOTEPAD, null);
        while (cursor.moveToNext()) {
            notepad = new Notepad();
            notepad.setId(cursor.getInt(cursor.getColumnIndex(NOTEPAD_ID)));
            notepad.setTitle(cursor.getString(cursor.getColumnIndex(NOTEPAD_TITLE)));
            notepad.setText(cursor.getString(cursor.getColumnIndex(NOTEPAD_TEXT)));
            notepad.setColor(cursor.getInt(cursor.getColumnIndex(NOTEPAD_COLOR)));
            notelist.add(notepad);
        }
        cursor.close();
        db.close();
        return notelist;
    }

    /**
     * Methods for Teachers activity
     **/
    public void insertLecturer(Lecturer lecturer) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(LECTURERS_NAME, lecturer.getName());
        contentValues.put(LECTURERS_POST, lecturer.getPost());
        contentValues.put(LECTURERS_PHONE_NUMBER, lecturer.getPhonenumber());
        contentValues.put(LECTURERS_EMAIL, lecturer.getEmail());
        contentValues.put(LECTURERS_COLOR, lecturer.getColor());
        db.insert(LECTURERS, null, contentValues);
        db.close();
    }

    public void updateLecturer(Lecturer lecturer) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(LECTURERS_NAME, lecturer.getName());
        contentValues.put(LECTURERS_POST, lecturer.getPost());
        contentValues.put(LECTURERS_PHONE_NUMBER, lecturer.getPhonenumber());
        contentValues.put(LECTURERS_EMAIL, lecturer.getEmail());
        contentValues.put(LECTURERS_COLOR, lecturer.getColor());
        db.update(LECTURERS, contentValues, LECTURERS_ID + " = " + lecturer.getId(), null);
        db.close();
    }

    public void deleteLecturerById(Lecturer lecturer) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(LECTURERS, LECTURERS_ID + " =? ", new String[] {String.valueOf(lecturer.getId())});
        db.close();
    }

    public ArrayList<Lecturer> getLecturer() {
        SQLiteDatabase db = this.getWritableDatabase();
        ArrayList<Lecturer> lecturerlist = new ArrayList<>();
        Lecturer lecturer;
        Cursor cursor = db.rawQuery("SELECT * FROM " + LECTURERS, null);
        while (cursor.moveToNext()) {
            lecturer = new Lecturer();
            lecturer.setId(cursor.getInt(cursor.getColumnIndex(LECTURERS_ID)));
            lecturer.setName(cursor.getString(cursor.getColumnIndex(LECTURERS_NAME)));
            lecturer.setPost(cursor.getString(cursor.getColumnIndex(LECTURERS_POST)));
            lecturer.setPhonenumber(cursor.getString(cursor.getColumnIndex(LECTURERS_PHONE_NUMBER)));
            lecturer.setEmail(cursor.getString(cursor.getColumnIndex(LECTURERS_EMAIL)));
            lecturer.setColor(cursor.getInt(cursor.getColumnIndex(LECTURERS_COLOR)));
            lecturerlist.add(lecturer);
        }
        cursor.close();
        db.close();
        return lecturerlist;
    }

    /**
     * Methods for Exams activity
     **/
    public void insertExam(Exam exam) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(EXAMS_MODULE, exam.getModule());
        contentValues.put(EXAMS_LECTURER, exam.getLecturer());
        contentValues.put(EXAMS_ROOM, exam.getRoom());
        contentValues.put(EXAMS_DATE, exam.getDate());
        contentValues.put(EXAMS_TIME, exam.getTime());
        contentValues.put(EXAMS_COLOR, exam.getColor());
        db.insert(EXAMS, null, contentValues);
        db.close();
    }

    public void updateExam(Exam exam) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(EXAMS_MODULE, exam.getModule());
        contentValues.put(EXAMS_LECTURER, exam.getLecturer());
        contentValues.put(EXAMS_ROOM, exam.getRoom());
        contentValues.put(EXAMS_DATE, exam.getDate());
        contentValues.put(EXAMS_TIME, exam.getTime());
        contentValues.put(EXAMS_COLOR, exam.getColor());
        db.update(EXAMS, contentValues, EXAMS_ID + " = " + exam.getId(), null);
        db.close();
    }

    public void deleteExamById(Exam exam) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(EXAMS, EXAMS_ID + " =? ", new String[] {String.valueOf(exam.getId())});
        db.close();
    }

    public ArrayList<Exam> getExam() {
        SQLiteDatabase db = this.getWritableDatabase();
        ArrayList<Exam> examslist = new ArrayList<>();
        Exam exam;
        Cursor cursor = db.rawQuery("SELECT * FROM " + EXAMS, null);
        while (cursor.moveToNext()) {
            exam = new Exam();
            exam.setId(cursor.getInt(cursor.getColumnIndex(EXAMS_ID)));
            exam.setModule(cursor.getString(cursor.getColumnIndex(EXAMS_MODULE)));
            exam.setLecturer(cursor.getString(cursor.getColumnIndex(EXAMS_LECTURER)));
            exam.setRoom(cursor.getString(cursor.getColumnIndex(EXAMS_ROOM)));
            exam.setDate(cursor.getString(cursor.getColumnIndex(EXAMS_DATE)));
            exam.setTime(cursor.getString(cursor.getColumnIndex(EXAMS_TIME)));
            exam.setColor(cursor.getInt(cursor.getColumnIndex(EXAMS_COLOR)));
            examslist.add(exam);
        }
        cursor.close();
        db.close();
        return examslist;
    }
}
