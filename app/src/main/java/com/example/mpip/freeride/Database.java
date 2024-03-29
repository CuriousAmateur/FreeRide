package com.example.mpip.freeride;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.strictmode.SqliteObjectLeakedViolation;
import android.util.Log;
import android.widget.Toast;

import java.net.URI;
import java.util.ArrayList;

import static android.support.constraint.Constraints.TAG;

public class Database extends SQLiteOpenHelper
{

    public static String db_Name = "database";

    //Creating table names
    private static final String table_login = "Login";
    private static final String table_offer = "Offer";
    private static final String table_images = "Images";
    private static final String table_dates = "Dates";
    private static final String table_rented = "Rented";
    private static final String table_categories = "Categories";

    //Creating table_login columns
    private static final String id_l = "idL";
    private static final String email = "Email";
    private static final String pass = "Password";
    private static final String ime = "Ime";
    private static final String prezime = "Prezime";

    private static final String create_table_login = "CREATE TABLE "
            + table_login + "(" + id_l + " INTEGER PRIMARY KEY AUTOINCREMENT, "
            + email + " TEXT, " + pass + " TEXT, " + ime + " TEXT, "
            + prezime + " TEXT);";

    //Creating table_offer columns
    private static final String id_O = "idO";
    private static final String langitude = "Langitude";
    private static final String longitude = "Longitude";
    private static final String price = "Price";
    private static final String id_k = "idK";

    private static final String create_table_offer = "CREATE TABLE "
            + table_offer + "(" + id_O + " INTEGER PRIMARY KEY AUTOINCREMENT,"
            + id_l + " INTEGER, " + langitude + " TEXT, " + longitude + " TEXT, "
            + price + " INTEGER, " + id_k + " INTEGER);";

    //Creating table_images columns
    private static final String id = "id";
    private static final String path_to_image = "PathToImage";

    private static final String create_table_images = "CREATE TABLE "
            + table_images + "(" + id + " INTEGER PRIMARY KEY AUTOINCREMENT, " + id_O + " INTEGER, "
            + path_to_image + " TEXT);";

    //Crating table_dates columns
    private static final String id_d = "idD";
    private static final String date_from = "DateFrom";
    private static final String date_to = "DateTo";

    private static final String create_table_dates = "CREATE TABLE "
            + table_dates + "(" + id_d + " INTEGER, "
            + id_O + " INTEGER, " + date_from + " TEXT, "
            + date_to + " TEXT);";

    //Creating table_rented columns
    private static final String id_z = "idZ";
    private static final String rented_from = "RentedFrom";
    private static final String rented_to = "RentedTo";

    private static final String create_table_rented = "CREATE TABLE "
            + table_rented + "(" + id_z + " INTEGER PRIMARY KEY AUTOINCREMENT, "
            + id_O + " INTEGER, " + rented_from + " TEXT, "
            + rented_to + " TEXT);";

    //Creating table_categories columns
    private static final String category = "Category";

    private static final String create_table_categories = "CREATE TABLE "
            + table_categories + "(" + id_k + " INTEGER PRIMARY KEY AUTOINCREMENT, "
            + category + " TEXT);";

    public Database(Context context)
    {
        super(context, db_Name + ".db", null, 1);
        Log.d("table", create_table_login);
    }

    @Override
    public void onCreate(SQLiteDatabase db)
    {
        db.execSQL(create_table_login);
        db.execSQL(create_table_offer);
        db.execSQL(create_table_images);
        db.execSQL(create_table_dates);
        db.execSQL(create_table_rented);
        db.execSQL(create_table_categories);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
    {
        db.execSQL("DROP TABLE IF EXISTS '" + table_login + "'");
        db.execSQL("DROP TABLE IF EXISTS '" + table_offer + "'");
        db.execSQL("DROP TABLE IF EXISTS '" + table_images + "'");
        db.execSQL("DROP TABLE IF EXISTS '" + table_dates + "'");
        db.execSQL("DROP TABLE IF EXISTS '" + table_rented + "'");
        db.execSQL("DROP TABLE IF EXISTS '" + table_categories + "'");
        onCreate(db);
    }


    public void addCategories()
    {
        SQLiteDatabase db = this.getWritableDatabase();

        db.delete(table_categories, null, null);

        ContentValues values = new ContentValues();
        values.put(category, "Mountain Bike");
        db.insert(table_categories, null, values);

        values.put(category, "Everyday");
        db.insert(table_categories, null, values);

        values.put(category, "Sports");
        db.insert(table_categories, null, values);

    }

    public ArrayList getCategories()
    {
        ArrayList<String> list = new ArrayList<>();

        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.rawQuery("Select * from " + table_categories, null);
        cursor.moveToFirst();

        if(cursor != null)
        {
            while(cursor.isAfterLast() == false)
            {
                list.add(cursor.getString(cursor.getColumnIndex(category)));
                cursor.moveToNext();
            }
        }
        return list;

    }

    public boolean checkLogin(String email, String pass)
    {
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.rawQuery("Select * from Login where Email" + "='" + email + "'" +
                " and Password" + "='" + pass + "'", null);

        boolean exists = (cursor.getCount() > 0);
        return exists;
    }

    public int getLoginID(String email, String pass)
    {
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.rawQuery("Select * from Login where Email" + "='" + email + "'" +
                " and Password" + "='" + pass + "'", null);

        cursor.moveToFirst();
        int id = cursor.getInt(cursor.getColumnIndex(id_l));
        return id;
    }

    public int getCategoryID(String categori)
    {
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.rawQuery("Select * from " + table_categories + " where "
                + category + "='" + categori + "'", null);

        cursor.moveToFirst();
        String key = cursor.getString(cursor.getColumnIndex(id_k));

        int id = Integer.parseInt(key);
        return id;
    }

    public String getCategoryByID(int id)
    {
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.rawQuery("Select * from Categories where idK" + "'=" + id + "'", null);

        return cursor.getString(cursor.getColumnIndex(category));
    }

    public boolean checkMail(String email)
    {
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.rawQuery("Select * from Login where Email" + "='" + email + "'", null);

        boolean exists = (cursor.getCount() > 0);
        return exists;
    }

    public boolean insertUser(String password, String Email, String name, String surname)
    {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(email, Email);
        values.put(pass, password);
        values.put(ime, name);
        values.put(prezime, surname);

        long result = db.insert(table_login, null, values);

        if(result == -1)
            return false;
        else
            return true;
    }

    public boolean insertOffer(int idL, double lang, double longi, int cena, int idK)
    {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(id_l, idL);
        values.put(langitude, lang);
        values.put(longitude, longi);
        values.put(price, cena);
        values.put(id_k, idK);

        long result = db.insert(table_offer, null, values);

        if(result == -1)
            return false;
        else
            return true;
    }

    public boolean updateOffer(int idO, int idL, double lang, double longi, int cena, int idK)
    {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();

        values.put(id_l, idL);
        values.put(langitude, lang);
        values.put(longitude, longi);
        values.put(price, cena);
        values.put(id_k, idK);

        int result = db.update(table_offer, values, id_O + "=" + idO, null);

        return result > 0;
    }

    public boolean createEmptyOffer()
    {
        SQLiteDatabase db = this.getWritableDatabase();

        //ContentValues values = new ContentValues();

        long result = db.insert(table_offer, id_O, null);

        if(result == -1)
            return false;
        else
            return true;

    }

    public int getEmptyOfferID()
    {
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.rawQuery("Select * from Offer where Price is null", null);

        String key = "";

        if(cursor != null)
        {
            if (cursor.moveToFirst())
                key = cursor.getString(cursor.getColumnIndex(id_O));
            cursor.close();
        }

        int id = Integer.parseInt(key);
        return id;
    }

    public Cursor getAllOffers()
    {
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.rawQuery("Select * from Offer", null);
        return cursor;
    }

    public boolean insertImage(int idO, String uri)
    {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(id_O, idO);
        values.put(path_to_image, uri);

        long result = db.insert(table_images, null, values);

        if(result == -1)
            return false;
        else
            return true;
    }

    public boolean createDates(int idO, String from, String to)
    {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(date_from, from);
        values.put(date_to, to);
        values.put(id_O, idO);

        long result = db.insert(table_dates, null, values);

        if(result == -1)
            return false;
        else
            return true;
    }
    public Cursor getDates()
    {
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.rawQuery("Select * from Dates", null);

        return cursor;
    }

    public Cursor getImages()
    {
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.rawQuery("Select * from Images", null);

        return cursor;
    }
}
