package com.kallavistudios.samplesearch.data;


import com.kallavistudios.samplesearch.R;
import com.kallavistudios.samplesearch.SampleSearchApplication;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

public class DataManager {

    private final SampleSearchApplication application;
    private List<String> suggestions = new ArrayList<>();

    public DataManager(SampleSearchApplication application) {
        this.application = application;
    }

    public List<String> getSuggestions() {
        if (suggestions.isEmpty()) {
            populateSuggestionsTable()
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribeOn(Schedulers.io())
                    .subscribe(new Action1<List<String>>() {
                        @Override
                        public void call(List<String> result) {
                            suggestions = result;
                        }
                    });
        }
        return suggestions;
    }

    private Observable<List<String>> populateSuggestionsTable() {
        List<String> result = new ArrayList<>();
        try {
            InputStream inputStream = application.getResources().openRawResource(R.raw.suggestions);
            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
            String keyword = reader.readLine();
            while (keyword != null) {
                result.add(keyword);
                keyword = reader.readLine();
            }
            reader.close();
        } catch (IOException ignore) {

        }
        return Observable.just(result);
    }
}
