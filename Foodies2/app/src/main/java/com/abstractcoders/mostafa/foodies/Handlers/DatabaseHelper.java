package com.abstractcoders.mostafa.foodies.Handlers;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseErrorHandler;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.abstractcoders.mostafa.foodies.Model.Favourite;
import com.abstractcoders.mostafa.foodies.Model.Note;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Mostafa on 28/02/2016.
 */
public class DatabaseHelper extends SQLiteOpenHelper {

    String viewUsers = "viewUsers";

    public DatabaseHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    public DatabaseHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version, DatabaseErrorHandler errorHandler) {
        super(context, name, factory, version, errorHandler);
    }

    public DatabaseHelper(Context context) {
        super(context, "FoodiesDB", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String sql = "CREATE TABLE Users(UserID INTEGER PRIMARY KEY AUTOINCREMENT, UserName TEXT, FirstName TEXT, " +
                "Password TEXT, LastName TEXT, EmailAddress TEXT ,Address TEXT, Photo BLOB)";
        db.execSQL(sql);

        sql  = "CREATE VIEW " + viewUsers + " AS SELECT * FROM Users";
        db.execSQL(sql);

        sql = "CREATE TABLE Favourites(FavouriteID INTEGER PRIMARY KEY AUTOINCREMENT, UserID TEXT, PlaceID TEXT," +
                " PlaceName TEXT, Rating FLOAT, PhotoDrawable TEXT, Latitude REAL, Longitude REAL, FOREIGN KEY(UserID) REFERENCES Users(UserID))";
        db.execSQL(sql);

        sql = "CREATE TABLE Notes(NoteID INTEGER PRIMARY KEY AUTOINCREMENT, NoteTitle TEXT, UserID TEXT, PlaceID TEXT, " +
                "PlaceName TEXT, Content TEXT, FOREIGN KEY(UserID) REFERENCES Users(UserID), FOREIGN KEY(PlaceID) REFERENCES Favourites(PlaceID))";
        db.execSQL(sql);


    }

    public boolean retrieveFavourite(int userID, String placeID)
    {
        SQLiteDatabase db = this.getReadableDatabase();
        String[] dbColumns = {"FavouriteID", "UserID", "PlaceID"};
        // Define 'where' part of query.
        String selection = "";
        // Specify arguments in placeholder order.
        String[] selectionArgs = {placeID, Integer.toString(userID)};
        Cursor cursor = db.query("Favourites", dbColumns,"PlaceID=? and UserID=?",selectionArgs,null,null,null);
        boolean found = cursor.moveToFirst();
        return found;
    }


    public Cursor retrieveFavourites(int userID)
    {
        SQLiteDatabase db = this.getReadableDatabase();
        String[] dbColumns = {"FavouriteID", "UserID", "PlaceID", "PlaceName", "Rating", "PhotoDrawable", "Latitude", "Longitude"};
        // Define 'where' part of query.
        String selection = "";
        // Specify arguments in placeholder order.
        String[] selectionArgs = {Integer.toString(userID)};
        Cursor cursor = db.query("Favourites", dbColumns, "UserID=?", selectionArgs, null, null, "FavouriteID DESC");
        return cursor;
    }

    public List<Note> retrieveNotes(int userID)
    {
        List<Note> notes = new ArrayList<Note>();
        SQLiteDatabase db = this.getReadableDatabase();
        String[] dbColumns = {"NoteID", "NoteTitle", "UserID", "PlaceID", "PlaceName", "Content"};
        // Define 'where' part of query.
        String selection = "";
        // Specify arguments in placeholder order.
        String[] selectionArgs = {Integer.toString(userID)};
        Cursor cursor = db.query("Notes", dbColumns, "UserID=?", selectionArgs, null, null, null);
        while(cursor.moveToNext())
        {
            String noteUserID = cursor.getString(cursor.getColumnIndex("UserID"));
            String noteID = cursor.getString(cursor.getColumnIndex("NoteID"));
            String noteTitle = cursor.getString(cursor.getColumnIndex("NoteTitle"));
            String placeID = cursor.getString(cursor.getColumnIndex("PlaceID"));
            String placeName = cursor.getString(cursor.getColumnIndex("PlaceName"));
            String content = cursor.getString(cursor.getColumnIndex("Content"));
            Note note = new Note(noteUserID,noteID,noteTitle,content,placeID,placeName);
            notes.add(note);
        }
        return notes;
    }

    public void updateNote(Note note)
    {
        SQLiteDatabase db = this.getReadableDatabase();
        if(note != null) {
            ContentValues cv = new ContentValues();
            cv.put("NoteID", note.getNoteID());
            cv.put("NoteTitle",note.getNoteTitle());
            cv.put("UserID", note.getUserID());
            cv.put("PlaceID", note.getPlaceID());
            cv.put("PlaceName", note.getPlaceName());
            cv.put("Content", note.getNoteContent());
            String where = "NoteID=?";
            String[] whereArgs = new String[] {note.getNoteID()};
            db.update("Notes",cv,where, whereArgs);
            db.close();
        }
    }




    public void insertNote(Note note)
    {
        SQLiteDatabase db = this.getReadableDatabase();
        if(note != null)
        {
            ContentValues cv = new ContentValues();
            cv.put("NoteTitle",note.getNoteTitle());
            cv.put("UserID", note.getUserID());
            cv.put("PlaceID", note.getPlaceID());
            cv.put("PlaceName", note.getPlaceName());
            cv.put("Content", note.getNoteContent());
            long i = db.insert("Notes", "NoteID", cv);
            db.close();
        }
    }


    public void insertExampleNote() {
        SQLiteDatabase db = this.getReadableDatabase();
        ContentValues cv = new ContentValues();
        cv.put("NoteTitle", "An example title");
        cv.put("UserID", "1");
        cv.put("PlaceID", "2");
        cv.put("PlaceName", "Caspian");
        cv.put("Content", "Caspian Melt");
        long i = db.insert("Notes", "NoteID", cv);
        db.close();
    }


    public Favourite insertFavourite(String placeID, int UserID, String placeName, String photoDrawable, float rating, double latitude, double longitude)
    {
        if(!placeID.equals("") && UserID != 0) {
            if (!retrieveFavourite(UserID, placeID)) {
                SQLiteDatabase db = this.getWritableDatabase();

                ContentValues cv = new ContentValues();
                cv.put("PlaceID", placeID);
                cv.put("UserID", UserID);
                cv.put("PlaceName",placeName);
                cv.put("Rating",rating);
                cv.put("PhotoDrawable",photoDrawable);
                cv.put("Latitude", latitude);
                cv.put("Longitude", longitude);
                long i = db.insert("Favourites", "FavouriteID", cv);
                db.close();
                Favourite favourite = new Favourite(Long.toString(i),Integer.toString(UserID),
                        placeID,placeName,photoDrawable,rating,latitude,longitude);
                return favourite;
            }else
            {
                return null;
            }
        }else {
            return null;
        }
    }

    public void insertUser(String userName, String firstName, String secondName, String address, String password, byte[] photo) {
        if (userName.length() != 0 && firstName.length() != 0 && secondName.length() != 0 && address.length() != 0) {
            SQLiteDatabase db = this.getWritableDatabase();
            ContentValues cv = new ContentValues();
            cv.put("UserName", userName);
            cv.put("FirstName", firstName);
            cv.put("LastName", secondName);
            cv.put("Password", password);
            cv.put("EmailAddress", address);
            cv.put("Photo", photo);
            long i = db.insert("Users", "UserName", cv);
            db.close();
        }
    }

    public void insertExampleUser() {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put("UserName", "hirsim2");
        cv.put("FirstName", "Mostafa");
        cv.put("LastName", "Hirsi");
        cv.put("Password", "blabla");
        cv.put("Address", "49 Mead Crescent");
        long i = db.insert("Users", "UserName", cv);
        db.close();
    }

    public void insertExampleFavourite() {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put("PlaceID", "ChIJjRuIiTiuEmsRCHhYnrWiSok");
        cv.put("UserID", "0");
        cv.put("PlaceName","Sydney Showboats");
        cv.put("Rating","4.3");
        cv.put("PhotoDrawable","CmRdAAAAV5W08FNpUNmznRckZWNljawq7k0kaNGewcM4fXNkJ_wass6PhvD0PM0H3GZTuEEtOidYI2KSfpSa1bwhBhmr6WpzvZImvO7b8KE8jRnt-gSEDXxJh2Uudp0csZLQ9rNzEhAz-5e5WltC8DvDZoG9AhLRGhQTwANOB6L6TzU1nbQKYyZhtDUciQ");
        cv.put("Latitude", "-33.86755700000001");
        cv.put("Longitude", "151.201527");
        long i = db.insert("Favourites", "FavouriteID", cv);
        db.close();
    }

    public void removeFavourite(String FavouriteID)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        String[] selectionArgs = {FavouriteID};
        int i = db.delete("Favourites", "FavouriteID=?", selectionArgs);
    }

    public void deleteNote(String noteId)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        String[] selectionArgs = {noteId};
        int i = db.delete("Notes", "NoteID=?", selectionArgs);
    }

    public Cursor getUser(String userName, String password) {
        SQLiteDatabase db = this.getReadableDatabase();
        String[] dbColumns = {"UserID", "UserName", "FirstName", "LastName", "Password", "EmailAddress"};
        // Specify arguments in placeholder order.
        String[] selectionArgs = {userName, password};
        Cursor cursor = db.query("Users", dbColumns,"UserName=? and Password=?",selectionArgs,null,null,null);
        return cursor;
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
