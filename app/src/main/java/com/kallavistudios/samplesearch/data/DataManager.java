package com.kallavistudios.samplesearch.data;

import android.support.annotation.NonNull;
import com.kallavistudios.samplesearch.R;
import com.kallavistudios.samplesearch.SampleSearchApplication;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

public class DataManager {


    private final SampleSearchApplication application;
    private List<String> suggestions = new ArrayList<>();
  private volatile Set<String> suggestionSet = new HashSet<>();

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

    public Observable<List<String>> getSuggestedWords(String query){
        if(suggestionSet.isEmpty()){
          return getSuggestedWordsFromDisk(query);
        }else{
          final List<String> result = new ArrayList<>();
          for (String keyword : suggestionSet){
            if(keyword.startsWith(query)){
              result.add(keyword);
            }
          }
          if(result.isEmpty()){
            return getSuggestedWordsFromDisk(query)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io());
          }else{
            return Observable.just(result);
          }
        }
    }

  @NonNull
  private Observable<List<String>> getSuggestedWordsFromDisk(String query) {
    final Observable<List<String>> result = getSuggestionsMatching(query)
        .observeOn(AndroidSchedulers.mainThread())
        .subscribeOn(Schedulers.io());
    result.subscribe(new Action1<List<String>>() {
      @Override
      public void call(List<String> result) {
        for(int i = 0 , size = result.size(); i < size; i++){
          suggestionSet.add(result.get(i));
        }
      }
    });
    return result;
  }

  private Observable<List<String>> getSuggestionsMatching(String query){
        List<String> result = new ArrayList<>();
        try {
            InputStream inputStream = application.getResources().openRawResource(R.raw.suggestions);
            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
            String keyword = reader.readLine();
            while (keyword != null) {
                if(keyword.startsWith(query)) {
                    result.add(keyword);
                }
                keyword = reader.readLine();
            }
            reader.close();
        } catch (IOException ignore) {

        }
        return Observable.just(result);
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
