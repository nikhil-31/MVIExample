package com.example.mviexample.view.search;

import com.example.mviexample.businesslogic.interactor.search.SearchViewState;
import com.hannesdorfmann.mosby3.mvp.MvpView;
import io.reactivex.Observable;
import org.jetbrains.annotations.NotNull;

public interface SearchView extends MvpView {


    /**
     * The search intent
     *
     * @return - An observable emitting the search query text
     */
    @NotNull
    Observable<String> searchIntent();

    /**
     * Renders the view
     *
     * @param viewState - viewState the current view state that should be displayed
     */
    void render(SearchViewState viewState);


}
