package com.kallavistudios.samplesearch.data;


import android.app.SearchManager;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteQueryBuilder;
import android.provider.BaseColumns;

import com.squareup.otto.Bus;

import java.util.HashMap;

public class SuggestionsDatabase {

    private static HashMap<String, String> buildColumnMap() {
        HashMap<String, String> map = new HashMap<>();
        map.put(SuggestionsOpenHelper.KEY_WORD, SuggestionsOpenHelper.KEY_WORD);
        map.put(BaseColumns._ID, "rowid AS " +
                BaseColumns._ID);
        map.put(SearchManager.SUGGEST_COLUMN_INTENT_DATA_ID, "rowid AS " +
                SearchManager.SUGGEST_COLUMN_INTENT_DATA_ID);
        map.put(SearchManager.SUGGEST_COLUMN_SHORTCUT_ID, "rowid AS " +
                SearchManager.SUGGEST_COLUMN_SHORTCUT_ID);
        return map;
    }

    private final SuggestionsOpenHelper suggestionsOpenHelper;
    private static final HashMap<String, String> columnMap = buildColumnMap();

    public SuggestionsDatabase(Context context, Bus bus) {
        suggestionsOpenHelper = new SuggestionsOpenHelper(context, bus);
    }

    public Cursor getKeyWordMatches(String query, String columns[]) {
        String selection = SuggestionsOpenHelper.KEY_WORD + " MATCH ?";
        String[] selectionArgs = new String[]{query + "*"};

        return query(selection, selectionArgs, columns);
    }

    private Cursor query(String selection, String[] selectionArgs, String[] columns) {
        SQLiteQueryBuilder builder = new SQLiteQueryBuilder();
        builder.setTables(SuggestionsOpenHelper.FTS_VIRTUAL_TABLE);
        builder.setProjectionMap(columnMap);
        Cursor cursor = builder.query(suggestionsOpenHelper.getReadableDatabase(), columns, selection, selectionArgs, null, null, null);
        if (cursor == null) {
            return null;
        } else if (!cursor.moveToFirst()) {
            cursor.close();
            return null;
        }
        return cursor;
    }
}


