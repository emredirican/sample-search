package com.kallavistudios.samplesearch.rest;


import com.kallavistudios.samplesearch.rest.response.SearchResult;

import retrofit.http.GET;
import retrofit.http.Query;
import rx.Observable;

public interface SearchService {

    @GET("customsearch/v1")
    Observable<SearchResult> search(@Query("key") String apiKey,
                                    @Query("cx") String searchEngineId,
                                    @Query("fields") String fields,
                                    @Query("prettyPrint") boolean prettyPrint,
                                    @Query("q") String keyword);
}
