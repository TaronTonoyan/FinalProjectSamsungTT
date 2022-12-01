package com.samsung.finalprojectsamsungtt;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;

public class DBTickets {

    private static final String DATABASE_NAME = "tickets.db";
    private static final int DATABASE_VERSION = 1;
    private static final String TABLE_ACCOUNTS = "tableLogin";
    private static final String TABLE_FILMS = "tableFilm";
    private static final String TABLE_ORDERS = "tableOrder";

    private static final String COLUMN_ID = "id";
    private static final String COLUMN_EMAIL = "Email";
    private static final String COLUMN_PASSWORD = "Password";
    private static final String COLUMN_IS_ADMIN = "Is_Admin";
    private static final String COLUMN_FILM_NAME = "Name";
    private static final String COLUMN_SIZE = "Size";
    private static final String COLUMN_PRICE = "Price";
    private static final String COLUMN_CATEGORY = "Category";
    private static final String COLUMN_GENRE = "Genre";
    private static final String COLUMN_DESCRIPTION = "Description";
    private static final String COLUMN_FILM_DATE_AND_TIME = "Date_And_Time";
    private static final String COLUMN_ORDER_NAME = "Name";
    private static final String COLUMN_ORDER_DATE_AND_TIME = "Date_And_Time";
    private static final String COLUMN_PLACE = "Place";

    private static final int NUM_COLUMN_ID = 0;
    private static final int NUM_COLUMN_EMAIL = 1;
    private static final int NUM_COLUMN_PASSWORD = 2;
    private static final int NUM_COLUMN_IS_ADMIN = 3;
    private static final int NUM_COLUMN_FILM_NAME = 1;
    private static final int NUM_COLUMN_SIZE = 2;
    private static final int NUM_COLUMN_PRICE = 3;
    private static final int NUM_COLUMN_CATEGORY = 4;
    private static final int NUM_COLUMN_GENRE = 5;
    private static final int NUM_COLUMN_DESCRIPTION = 6;
    private static final int NUM_COLUMN_FILM_DATE_AND_TIME = 7;
    private static final int NUM_COLUMN_ORDER_NAME = 1;
    private static final int NUM_COLUMN_ORDER_DATE_AND_TIME = 2;
    private static final int NUM_COLUMN_PLACE = 3;

    private SQLiteDatabase db;

    public DBTickets (Context context) {
        OpenHelper openHelper = new OpenHelper(context);
        db = openHelper.getWritableDatabase();
    }

    public long insertAcc(String email, String password, int isAdmin) {
        ContentValues cv = new ContentValues();
        cv.put(COLUMN_EMAIL, email);
        cv.put(COLUMN_PASSWORD, password);
        cv.put(COLUMN_IS_ADMIN, isAdmin);
        return db.insert(TABLE_ACCOUNTS, null, cv);
    }

    public long insertFilm(String name, int size, double price, String category, String genre, String description, String dateAndTime) {
        ContentValues cv = new ContentValues();
        cv.put(COLUMN_FILM_NAME, name);
        cv.put(COLUMN_SIZE, size);
        cv.put(COLUMN_PRICE, price);
        cv.put(COLUMN_CATEGORY, category);
        cv.put(COLUMN_GENRE, genre);
        cv.put(COLUMN_DESCRIPTION, description);
        cv.put(COLUMN_FILM_DATE_AND_TIME, dateAndTime);
        return db.insert(TABLE_FILMS, null, cv);
    }

    public long insertOrder(String name, String dateAndTime, int place) {
        ContentValues cv = new ContentValues();
        cv.put(COLUMN_ORDER_NAME, name);
        cv.put(COLUMN_ORDER_DATE_AND_TIME, dateAndTime);
        cv.put(COLUMN_PLACE, place);
        return db.insert(TABLE_ORDERS, null, cv);
    }

    public int updateAcc(Account ld) {
        ContentValues cv = new ContentValues();
        cv.put(COLUMN_EMAIL, ld.getEmail());
        cv.put(COLUMN_PASSWORD, ld.getPassword());
        cv.put(COLUMN_IS_ADMIN, ld.getIsAdmin());

        return db.update(TABLE_ACCOUNTS, cv, COLUMN_ID + " = ?", new String[] {String.valueOf(ld.getId())});
    }

    public int updateFilm(Film ld) {
        ContentValues cv = new ContentValues();
        cv.put(COLUMN_FILM_NAME, ld.getName());
        cv.put(COLUMN_SIZE, ld.getSize());
        cv.put(COLUMN_PRICE, ld.getPrice());
        cv.put(COLUMN_CATEGORY, ld.getCategory());
        cv.put(COLUMN_GENRE, ld.getGenre());
        cv.put(COLUMN_DESCRIPTION, ld.getDescription());
        cv.put(COLUMN_FILM_DATE_AND_TIME, ld.getDateAndTime());

        return db.update(TABLE_FILMS, cv, COLUMN_ID + " = ?", new String[] {String.valueOf(ld.getId())});
    }

    public int updateOrder(Order ld) {
        ContentValues cv = new ContentValues();
        cv.put(COLUMN_ORDER_NAME, ld.getName());
        cv.put(COLUMN_ORDER_DATE_AND_TIME, ld.getDateAndTime());
        cv.put(COLUMN_PLACE, ld.getPlace());

        return db.update(TABLE_ORDERS, cv, COLUMN_ID + " = ?", new String[] {String.valueOf(ld.getId())});
    }

    public void deleteAcc(long id) {
        db.delete(TABLE_ACCOUNTS, COLUMN_ID + " = ?", new String[] {String.valueOf(id)});
    }

    public void deleteFilm(long id) {
        db.delete(TABLE_FILMS, COLUMN_ID + " = ?", new String[] {String.valueOf(id)});
    }

    public void deleteOrder(long id) {
        db.delete(TABLE_ORDERS, COLUMN_ID + " = ?", new String[] {String.valueOf(id)});
    }

    public void deleteAllAccounts() {
        db.delete(TABLE_ACCOUNTS, null, null);
    }

    public void deleteAllFilms() {
        db.delete(TABLE_FILMS, null, null);
    }

    public void deleteAllOrders() {
        db.delete(TABLE_ORDERS, null, null);
    }

    public Account selectAcc(long id) {
        Cursor cursor = db.query(TABLE_ACCOUNTS, null, COLUMN_ID + " = ?", new String[] {String.valueOf(id)}, null, null, null);

        cursor.moveToFirst();
        String email = cursor.getString(NUM_COLUMN_EMAIL);
        String password = cursor.getString(NUM_COLUMN_PASSWORD);
        int isAdmin = cursor.getInt(NUM_COLUMN_IS_ADMIN);
        return new Account(id, email, password, isAdmin);
    }

    public Film selectFilm(long id) {
        Cursor cursor = db.query(TABLE_FILMS, null, COLUMN_ID + " = ?", new String[] {String.valueOf(id)}, null, null, null);

        cursor.moveToFirst();
        String name = cursor.getString(NUM_COLUMN_FILM_NAME);
        int size = cursor.getInt(NUM_COLUMN_SIZE);
        double price = cursor.getDouble(NUM_COLUMN_PRICE);
        String category = cursor.getString(NUM_COLUMN_CATEGORY);
        String genre = cursor.getString(NUM_COLUMN_GENRE);
        String description = cursor.getString(NUM_COLUMN_DESCRIPTION);
        String dateAndTime = cursor.getString(NUM_COLUMN_FILM_DATE_AND_TIME);
        return new Film(id, name, size, price, category, genre, description, dateAndTime);
    }

    public Order selectOrder(long id) {
        Cursor cursor = db.query(TABLE_ORDERS, null, COLUMN_ID + " = ?", new String[] {String.valueOf(id)}, null, null, null);

        cursor.moveToFirst();
        String name = cursor.getString(NUM_COLUMN_ORDER_NAME);
        String dateAndTime = cursor.getString(NUM_COLUMN_ORDER_DATE_AND_TIME);
        int place = cursor.getInt(NUM_COLUMN_PLACE);
        return new Order(id, name, dateAndTime, place);
    }

    public ArrayList<Account> selectAllAccounts() {
        Cursor cursor = db.query(TABLE_ACCOUNTS, null, null, null, null, null, null);

        ArrayList<Account> arr = new ArrayList<Account>();
        cursor.moveToFirst();
        if (!cursor.isAfterLast()) {
            do {
                long id = cursor.getLong(NUM_COLUMN_ID);
                String email = cursor.getString(NUM_COLUMN_EMAIL);
                String password = cursor.getString(NUM_COLUMN_PASSWORD);
                int isAdmin = cursor.getInt(NUM_COLUMN_IS_ADMIN);
                arr.add(new Account(id, email, password, isAdmin));
            } while (cursor.moveToNext());
        }
        return arr;
    }

    public ArrayList<Film> selectAllFilms() {
        Cursor cursor = db.query(TABLE_FILMS, null, null, null, null, null, null);

        ArrayList<Film> arr = new ArrayList<Film>();
        cursor.moveToFirst();
        if (!cursor.isAfterLast()) {
            do {
                long id = cursor.getLong(NUM_COLUMN_ID);
                String name = cursor.getString(NUM_COLUMN_FILM_NAME);
                int size = cursor.getInt(NUM_COLUMN_SIZE);
                double price = cursor.getDouble(NUM_COLUMN_PRICE);
                String category = cursor.getString(NUM_COLUMN_CATEGORY);
                String genre = cursor.getString(NUM_COLUMN_GENRE);
                String description = cursor.getString(NUM_COLUMN_DESCRIPTION);
                String dateAndTime = cursor.getString(NUM_COLUMN_FILM_DATE_AND_TIME);
                arr.add(new Film(id, name, size, price, category, genre, description, dateAndTime));
            } while (cursor.moveToNext());
        }
        return arr;
    }

    public ArrayList<Order> selectAllOrders() {
        Cursor cursor = db.query(TABLE_ORDERS, null, null, null, null, null, null);

        ArrayList<Order> arr = new ArrayList<Order>();
        cursor.moveToFirst();
        if (!cursor.isAfterLast()) {
            do {
                long id = cursor.getLong(NUM_COLUMN_ID);
                String name = cursor.getString(NUM_COLUMN_ORDER_NAME);
                String dateAndTime = cursor.getString(NUM_COLUMN_ORDER_DATE_AND_TIME);
                int place = cursor.getInt(NUM_COLUMN_PLACE);
                arr.add(new Order(id, name, dateAndTime, place));
            } while (cursor.moveToNext());
        }
        return arr;
    }

    private class OpenHelper extends SQLiteOpenHelper {

        OpenHelper(Context context) {
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
        }
        @Override
        public void onCreate(SQLiteDatabase db) {
            String queryAcc = "CREATE TABLE " + TABLE_ACCOUNTS + " (" +
                    COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    COLUMN_EMAIL + " TEXT, " +
                    COLUMN_PASSWORD + " TEXT, " +
                    COLUMN_IS_ADMIN + " INTEGER);";
            String queryFilm = "CREATE TABLE " + TABLE_FILMS + " (" +
                    COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    COLUMN_FILM_NAME + " TEXT, " +
                    COLUMN_SIZE + " INTEGER, " +
                    COLUMN_PRICE + " REAL, " +
                    COLUMN_CATEGORY + " TEXT, " +
                    COLUMN_GENRE + " TEXT, " +
                    COLUMN_DESCRIPTION + " TEXT, " +
                    COLUMN_FILM_DATE_AND_TIME + " TEXT);";
            String queryOrder = "CREATE TABLE " + TABLE_ORDERS + " (" +
                    COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    COLUMN_ORDER_NAME + " TEXT, " +
                    COLUMN_ORDER_DATE_AND_TIME + " TEXT, " +
                    COLUMN_PLACE + " INTEGER);";
            db.execSQL(queryAcc);
            db.execSQL(queryFilm);
            db.execSQL(queryOrder);
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_ACCOUNTS);
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_FILMS);
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_ORDERS);
            onCreate(db);
        }
    }

}
