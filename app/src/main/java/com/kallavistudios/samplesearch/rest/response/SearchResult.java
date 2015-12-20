package com.kallavistudios.samplesearch.rest.response;


import com.kallavistudios.samplesearch.rest.response.model.SearchResultItem;

import java.util.Collections;
import java.util.List;

public class SearchResult {

    private List<SearchResultItem> items;

    public List<SearchResultItem> getItems() {
        if (items == null) {
            return Collections.emptyList();
        }
        return items;
    }
}
