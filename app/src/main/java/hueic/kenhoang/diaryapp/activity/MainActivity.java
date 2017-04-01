package hueic.kenhoang.diaryapp.activity;

import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.support.v7.widget.Toolbar;

import java.util.ArrayList;

import hueic.kenhoang.diaryapp.R;
import hueic.kenhoang.diaryapp.controller.NodeAdapter;
import hueic.kenhoang.diaryapp.controller.NoteDataSource;
import hueic.kenhoang.diaryapp.model.NoteModel;

public class MainActivity extends AppCompatActivity {
    private Toolbar toolbar;
    private ListView lvListNote;
    private TextView tvNoContent;
    private ProgressBar proLoading;
    private NoteDataSource dataSource;
    private ArrayList<NoteModel> arrNotes = new ArrayList<>();
    NodeAdapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        addControls();
        addEvents();
    }

    private void addEvents() {
    }

    private void addControls() {
        //get action bar
        toolbar = (Toolbar) findViewById(R.id.Toolbar1);
        toolbar.setTitle("My Diary");
        setSupportActionBar(toolbar);
        //findviewbyid
        lvListNote = (ListView) findViewById(R.id.lvListNote);
        tvNoContent = (TextView) findViewById(R.id.tvNoContent);
        proLoading = (ProgressBar) findViewById(R.id.pb1);
        // create data source
        dataSource = new NoteDataSource(this);
        dataSource.open();
        //read data from DB
        viewAllNotes();

    }
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);

            //update UI - display notes on listview
            if (arrNotes  != null && arrNotes.size() > 0)
            {
                proLoading.setVisibility(View.INVISIBLE);
                tvNoContent.setVisibility(View.INVISIBLE);
                lvListNote.setVisibility(View.VISIBLE);
                //view all note to list view
                adapter = new NodeAdapter(MainActivity.this, arrNotes);
                lvListNote.setAdapter(adapter);
            }
            else {
                proLoading.setVisibility(View.INVISIBLE);
                lvListNote.setVisibility(View.INVISIBLE);
                tvNoContent.setVisibility(View.VISIBLE);
            }
        }
    };
    public void viewAllNotes() {
        // create new thread to get all notes in background task
        new Thread(new Runnable() {
            @Override
            public void run() {
                //read all notes form DB
                arrNotes = dataSource.getAllNotes();
                handler.sendEmptyMessage(0);
            }
        }).start();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.action_bar, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.newNote:
                Intent intent = new Intent(this, NewNoteActivity.class);
                startActivity(intent);
                break;
            case R.id.setting:
                break;
            default:
                //If we got here, the user's action was not recoginized
                //Invoke the superclass to handle it
                return super.onOptionsItemSelected(item);
        }
        return true;
    }
}
