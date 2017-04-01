package hueic.kenhoang.diaryapp.activity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.BufferedInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

import hueic.kenhoang.diaryapp.R;
import hueic.kenhoang.diaryapp.controller.NoteDataSource;
import hueic.kenhoang.diaryapp.util.Config;
import hueic.kenhoang.diaryapp.util.Util;

public class NewNoteActivity extends AppCompatActivity {
    //constants
    public final int PICK_PHOTO_FOR_NOTE = 0;

    private Toolbar toolbar;
    private ImageView imgAttach;
    private Bitmap bmpAttach;
    private EditText edTitle;
    private EditText edContent;

    private NoteDataSource dataSource;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_note);
        //findview
        addControls();
        //create data source
        dataSource = new NoteDataSource(this);
        dataSource.open();
    }

    private void addControls() {
        edContent = (EditText) findViewById(R.id.edContent);
        edTitle = (EditText) findViewById(R.id.edTitle);
        imgAttach = (ImageView) this.findViewById(R.id.imgAttach);
        //get Action bar
        toolbar = (Toolbar) findViewById(R.id.myToolbar);
        toolbar.setTitle("New Note");
        setSupportActionBar(toolbar);
        //get image attach
        imgAttach = (ImageView) findViewById(R.id.imgAttach);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.add_note, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.saveNote:
                handleSaveNote();
                break;
            case R.id.attach:
                pickImage();
                break;
            default:
            return super.onOptionsItemSelected(item);
        }
        return true;
    }

    private void pickImage() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_PHOTO_FOR_NOTE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_PHOTO_FOR_NOTE && resultCode == Activity.RESULT_OK) {
            if (data == null) {
                //Display an error
                Toast.makeText(this, "There is no selected photo", Toast.LENGTH_SHORT).show();
                return;
            }
            try {
                //get image from result
                InputStream inputStream = this.getContentResolver().openInputStream(data.getData());
                BufferedInputStream bufferedInputStream = new BufferedInputStream(inputStream);
                bmpAttach = BitmapFactory.decodeStream(bufferedInputStream);
                //show image in screen
                imgAttach.setImageBitmap(bmpAttach);
                //imgAttach.setImageResource(R.drawable.attach);
                imgAttach.setVisibility(View.VISIBLE);
            }
            catch (FileNotFoundException ex) {
                ex.printStackTrace();
            }
        }
    }

    private void handleSaveNote() {
        if (edTitle.getText().toString().trim().length() <= 0) {
            Toast.makeText(this, "Please input title of note!", Toast.LENGTH_SHORT).show();
            return;
        }
        if (edContent.getText().toString().trim().length() <= 0) {
            Toast.makeText(this, "Please input content of note!", Toast.LENGTH_SHORT).show();
            return;
        }

        String dateTime = Util.getCurrentDateTime();
        String imgName = Util.convertStringDatetimeToFileName(dateTime) + ".png";
        //save not to SQLite
        dataSource.addNewNote(edTitle.getText().toString(), edContent.getText().toString(), imgName, dateTime);
        //save image to SDCard
        if (bmpAttach != null) {
            Util.saveImageToSDCard(bmpAttach, Config.FOLDER_IMAGES, imgName);
        }
        this.finish();
        Toast.makeText(this, "Add new note successfully", Toast.LENGTH_LONG).show();

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        dataSource.close();
    }
}
