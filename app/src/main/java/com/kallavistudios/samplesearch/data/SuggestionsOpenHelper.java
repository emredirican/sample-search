package com.kallavistudios.samplesearch.data;

import android.app.SearchManager;
import android.content.ContentValues;
import android.content.Context;
import android.content.res.Resources;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.AsyncTask;

import com.kallavistudios.samplesearch.R;
import com.squareup.otto.Bus;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;


public class SuggestionsOpenHelper extends SQLiteOpenHelper {
    public static final String FTS_VIRTUAL_TABLE = "FTSsuggestions";
    public static final String KEY_WORD = SearchManager.SUGGEST_COLUMN_TEXT_1;
    public static final String FTS_TABLE_CREATE =
            "CREATE VIRTUAL TABLE " + FTS_VIRTUAL_TABLE
                    + " USING fts3(" + KEY_WORD + ")";
    private final Context context;
    private final Bus bus;
    private SQLiteDatabase database;


    SuggestionsOpenHelper(Context context, Bus bus) {
        super(context, "suggestions", null, 1);
        this.context = context;
        this.bus = bus;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        this.database = db;
        this.database.execSQL(FTS_TABLE_CREATE);
//        loadSuggestions();
    }


    private void loadSuggestions() {
        AsyncTask.THREAD_POOL_EXECUTOR.execute(new Runnable() {
            @Override
            public void run() {
                try {
                    loadKeyWords();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        });
    }

    private void loadKeyWords() throws IOException {
        Resources res = context.getResources();
        InputStream inputStream = res.openRawResource(R.raw.suggestions);
        BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
        try {
            String keyword = reader.readLine();
            while (keyword != null) {
                ContentValues initialValues = new ContentValues();
                initialValues.put(KEY_WORD, keyword);
                database.insert(FTS_VIRTUAL_TABLE, null, initialValues);
                keyword = reader.readLine();
            }
        } finally {
            reader.close();
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + FTS_VIRTUAL_TABLE);
        onCreate(db);
    }
}
