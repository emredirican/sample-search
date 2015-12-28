package com.kallavistudios.samplesearch;

import android.app.Activity;
import android.app.SearchManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Toast;
import butterknife.Bind;
import butterknife.ButterKnife;
import com.kallavistudios.samplesearch.data.DataManager;
import com.kallavistudios.samplesearch.di.component.ActivityComponent;
import com.kallavistudios.samplesearch.di.module.ActivityModule;
import com.kallavistudios.samplesearch.rest.SearchService;
import com.kallavistudios.samplesearch.rest.response.SearchResult;
import com.kallavistudios.samplesearch.rest.response.model.SearchResultItem;
import com.kallavistudios.samplesearch.ui.adapter.SearchResultAdapter;
import com.squareup.otto.Bus;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import javax.inject.Inject;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;
import timber.log.Timber;

public class MainActivity extends AppCompatActivity {

  private static final int RESULT_LIMIT = 100;
  @Bind(R.id.sv_main) SearchView searchView;
  @Bind(R.id.lv_main) RecyclerView listView;
  @Bind(R.id.lv_suggestions) ListView suggestionsListView;
  @Bind(R.id.layout_main_container) RelativeLayout mainViewContainer;

  @Inject SearchService searchService;
  @Inject Activity activityContext;
  @Inject Bus bus;
  @Inject DataManager dataManager;

  private SearchResultAdapter adapter;
  private ArrayAdapter<String> suggestionsAdapter;

  @Override
  protected void onStart() {
    super.onStart();
    bus.register(this);
  }

  @Override
  protected void onStop() {
    bus.unregister(this);
    super.onStop();
  }

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);
    ActivityComponent objectGraph = ((SampleSearchApplication) getApplication()).getObjectGraph()
        .plus(new ActivityModule(this));
    objectGraph.inject(this);
    ButterKnife.bind(this);
    Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
    setSupportActionBar(toolbar);

    initSearchView();
    initSuggestionsList();
    initSearchResultsList();
  }

  private void initSearchResultsList() {
    listView.setLayoutManager(new LinearLayoutManager(this));
    adapter = new SearchResultAdapter(Collections.<SearchResultItem>emptyList());
    listView.setAdapter(adapter);
  }

  private void initSuggestionsList() {
    suggestionsAdapter = new ArrayAdapter<>(activityContext, R.layout.item_suggestion_result,
        new ArrayList<String>());
    suggestionsListView.setAdapter(suggestionsAdapter);
    suggestionsListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
      @Override
      public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        search(suggestionsAdapter.getItem(position));
        suggestionsAdapter.clear();
      }
    });
  }

  private void initSearchView() {
    SearchManager searchManager = (SearchManager) getSystemService(SEARCH_SERVICE);
    searchView.setSubmitButtonEnabled(true);
    searchView.setQueryRefinementEnabled(true);
    searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
    searchView.setIconifiedByDefault(false);
    searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
      @Override
      public boolean onQueryTextSubmit(String query) {
        onSearchViewSubmit(query);
        return true;
      }

      @Override
      public boolean onQueryTextChange(String query) {
        return onSearchViewQueryTextChange(query);
      }
    });
  }

  private boolean onSearchViewQueryTextChange(String query) {
    adapter.setItems(Collections.<SearchResultItem>emptyList());
    if (query == null || query.length() < 2) {
      return false;
    }
    suggestionsAdapter.clear();
    //List<String> result = new ArrayList<>();
    //List<String> suggestions = dataManager.getSuggestions();
    //for (int i = 0, size = suggestions.size(); i < size; i++) {
    //    String suggestion = suggestions.get(i).replace("'", "");
    //    if (suggestion.startsWith(query)) {
    //        result.add(suggestions.get(i));
    //        if(result.size() >= RESULT_LIMIT){
    //            break;
    //        }
    //    }
    //}
    //suggestionsAdapter.addAll(result);
    dataManager.getSuggestedWords(query).subscribe(new Action1<List<String>>() {
      @Override
      public void call(List<String> strings) {
        List<String> result = strings;
        Collections.sort(result);
        if(strings.size() > RESULT_LIMIT){
          result = strings.subList(0, RESULT_LIMIT);
        }
        suggestionsAdapter.addAll(result);
      }
    });
    return true;
  }

  private void onSearchViewSubmit(String query) {
    adapter.setItems(Collections.<SearchResultItem>emptyList());
    suggestionsAdapter.clear();
    search(query);
  }

  private void search(String keyword) {
    searchService.search(getString(R.string.apikey), getString(R.string.seId),
        "items(title,link,snippet)", false, keyword)
        .observeOn(AndroidSchedulers.mainThread())
        .subscribeOn(Schedulers.io())
        .subscribe(new Subscriber<SearchResult>() {
          @Override
          public void onCompleted() {
          }

          @Override
          public void onError(Throwable e) {
            Timber.d(e.getMessage());
          }

          @Override
          public void onNext(SearchResult searchResult) {
            Timber.d(searchResult.toString());
            if (searchResult.getItems().isEmpty()) {
              Toast.makeText(activityContext, getString(R.string.no_result_found),
                  Toast.LENGTH_SHORT).show();
            }
            adapter.setItems(searchResult.getItems());
          }
        });
  }
}
