package com.example.mviexample.view.search;

import com.example.mviexample.businesslogic.interactor.search.SearchInteractor;
import com.example.mviexample.businesslogic.interactor.search.SearchViewState;
import com.hannesdorfmann.mosby3.mvi.MviBasePresenter;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;


public class SearchPresenter extends MviBasePresenter<SearchView, SearchViewState> {
    private final SearchInteractor searchInteractor;

    public SearchPresenter(SearchInteractor interactor) {
        super(new SearchViewState.SearchNotStartedYet());
        this.searchInteractor = interactor;
    }


    @Override
    protected void bindIntents() {
        Observable<SearchViewState> search =
                intent(SearchView::searchIntent)
                        .switchMap(searchInteractor::search)  // This line talks to the business logic
                        .observeOn(AndroidSchedulers.mainThread());

        subscribeViewState(search, SearchView::render);

    }
}
