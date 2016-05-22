package com.abstractcoders.mostafa.foodies.Model;

import java.io.Serializable;

/**
 * Created by hirsim2 on 15/03/16.
 */
public class Note implements Serializable {

   /* CREATE TABLE Notes(NoteID INTEGER PRIMARY KEY AUTOINCREMENT, NoteTitle TEXT, UserID TEXT,
   PlaceID TEXT, PlaceName TEXT, Content TEXT FOREIGN KEY(PlaceID) REFERENCES Users(PlaceID))";
*/
    private String userID;
    private String noteID;
    private String noteTitle;
    private String noteContent;
    private String placeID;
    private String placeName;

    public Note(String userID, String noteID, String noteTitle, String noteContent, String placeID, String placeName) {
        this.userID = userID;
        this.noteID = noteID;
        this.noteTitle = noteTitle;
        this.noteContent = noteContent;
        this.placeID = placeID;
        this.placeName = placeName;
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public String getNoteID() {
        return noteID;
    }

    public void setNoteID(String noteID) {
        this.noteID = noteID;
    }

    public String getNoteTitle() {
        return noteTitle;
    }

    public void setNoteTitle(String noteTitle) {
        this.noteTitle = noteTitle;
    }

    public String getNoteContent() {
        return noteContent;
    }

    public void setNoteContent(String noteContent) {
        this.noteContent = noteContent;
    }

    public String getPlaceID() {
        return placeID;
    }

    public void setPlaceID(String placeID) {
        this.placeID = placeID;
    }

    public String getPlaceName() {
        return placeName;
    }

    public void setPlaceName(String placeName) {
        this.placeName = placeName;
    }
}
