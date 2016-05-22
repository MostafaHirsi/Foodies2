package com.abstractcoders.mostafa.foodies;

import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.abstractcoders.mostafa.foodies.Handlers.DatabaseHelper;
import com.abstractcoders.mostafa.foodies.Model.Eatery;
import com.abstractcoders.mostafa.foodies.Model.Note;

import org.w3c.dom.Text;

public class NoteActivity extends AppCompatActivity {

    EditText titleText, contentText;
    TextView placeTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note_view);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        final Note selectedNote = (Note) getIntent().getSerializableExtra("SelectedNote");
        placeTitle = (TextView) findViewById(R.id.eateryName);
        placeTitle.setText(selectedNote.getPlaceName());
        titleText = (EditText) findViewById(R.id.title_EditText);
        titleText.setTextColor(Color.BLACK);
        if (selectedNote.getNoteTitle().equals(""))
        {
            titleText.setText(selectedNote.getNoteTitle());
        }
        contentText = (EditText) findViewById(R.id.content_EditText);
        contentText.setTextColor(Color.BLACK);
        contentText.setText(selectedNote.getNoteContent());
        setSupportActionBar(toolbar);
        final DatabaseHelper db = new DatabaseHelper(this);
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.saveNoteFab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectedNote.setNoteTitle(titleText.getText().toString());
                selectedNote.setNoteContent(contentText.getText().toString());
                if(!selectedNote.getNoteID().equals("")) {
                    db.updateNote(selectedNote);
                    Toast.makeText(getBaseContext(), "Note saved", Toast.LENGTH_SHORT).show();
                }else
                {
                    db.insertNote(selectedNote);
                    Toast.makeText(getBaseContext(), "Note saved", Toast.LENGTH_SHORT).show();
                }
            }
        });
        getSupportActionBar().setTitle("");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public void onBackPressed() {
        finish();
        super.onBackPressed();
    }
}
