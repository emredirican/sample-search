package com.kallavistudios.samplesearch.data.provider;


import android.app.SearchManager;
import android.content.ContentProvider;
import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;
import android.provider.BaseColumns;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.kallavistudios.samplesearch.data.SuggestionsDatabase;
import com.kallavistudios.samplesearch.data.SuggestionsOpenHelper;

public class SearchSuggestionProvider extends ContentProvider {
    private static final String AUTHORITY = "com.kallavistudios.samplesearch.data.provider.SearchSuggestionProvider";
    public static final Uri CONTENT_URI = Uri.parse("content://" + AUTHORITY + "/suggestions");
    private SuggestionsDatabase suggestionsDb;

    @Override
    public boolean onCreate() {
//        this.suggestionsDb = new SuggestionsDatabase(getContext());
        return false;
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        String query = selectionArgs[0];
        if(query == null || query.length() < 2){
            return null;
        }
        String[] columns = new String[]{
                BaseColumns._ID,
                SuggestionsOpenHelper.KEY_WORD,
                SearchManager.SUGGEST_COLUMN_INTENT_DATA_ID
        };
        return suggestionsDb.getKeyWordMatches(query, columns);
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        return null;
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, ContentValues values) {
        return null;
    }

    @Override
    public int delete(@NonNull Uri uri, String selection, String[] selectionArgs) {
        return 0;
    }

    @Override
    public int update(@NonNull Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        return 0;
    }
}
