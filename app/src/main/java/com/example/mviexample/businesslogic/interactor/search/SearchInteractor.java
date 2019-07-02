package com.example.mviexample.businesslogic.interactor.search;

import com.example.mviexample.businesslogic.searchengine.SearchEngine;
import io.reactivex.Observable;


/**
 * Interacts with the search engine to search for items
 */
public class SearchInteractor {

    private final SearchEngine searchEngine;


    public SearchInteractor(SearchEngine searchEngine) {
        this.searchEngine = searchEngine;
    }

    /**
     * Searching for items
     */
    public Observable<SearchViewState> search(String searchString) {
        if (searchString.isEmpty()) {
            return Observable.just(new SearchViewState.SearchNotStartedYet());
        }

        return searchEngine.searchFor(searchString)
                .map(products -> {
                    if (products.isEmpty()) {
                        return new SearchViewState.EmptyResult(searchString);
                    } else {
                        return new SearchViewState.SearchResult(searchString, products);
                    }
                })
                .startWith(new SearchViewState.Loading())
                .onErrorReturn(error -> new SearchViewState.Error(searchString, error));

    }


}
