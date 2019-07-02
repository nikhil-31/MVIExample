package com.example.mviexample.view.search;

import android.support.annotation.NonNull;
import android.support.transition.TransitionManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;
import butterknife.BindInt;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import com.example.mviexample.R;
import com.example.mviexample.SampleApplication;
import com.example.mviexample.businesslogic.interactor.search.SearchViewState;
import com.example.mviexample.businesslogic.model.Product;
import com.example.mviexample.view.ui.viewholder.ProductViewHolder;
import com.hannesdorfmann.mosby3.mvi.MviActivity;
import com.jakewharton.rxbinding2.widget.RxSearchView;
import io.reactivex.Observable;

import java.util.List;
import java.util.concurrent.TimeUnit;

public class SearchActivity extends MviActivity<SearchView, SearchPresenter> implements SearchView {

    @BindView(R.id.searchView)
    android.widget.SearchView searchView;
    @BindView(R.id.container)
    ViewGroup container;
    @BindView(R.id.loadingView)
    View loadingView;
    @BindView(R.id.errorView)
    TextView errorView;
    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    @BindView(R.id.emptyView)
    View emptyView;
    @BindInt(R.integer.grid_span_size)
    int spanCount;

    private Unbinder unbinder;
    private SearchAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        unbinder = ButterKnife.bind(this);
        adapter = new SearchAdapter(LayoutInflater.from(this), product -> {
            Toast.makeText(this, "Product clicked", Toast.LENGTH_SHORT).show();
        });
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new GridLayoutManager(this, spanCount));

    }

    /**
     * This is the input of this screen
     * <p>
     * The user types in the intent. THis is
     *
     * @return
     */
    @Override
    public Observable<String> searchIntent() {
        return RxSearchView.queryTextChanges(searchView)
                .skip(2) // Because after screen orientation changes query Text will be resubmitted again
                .filter(queryString -> queryString.length() > 3 || queryString.length() == 0)
                .debounce(500, TimeUnit.MILLISECONDS)
                .distinctUntilChanged()
                .map(CharSequence::toString);
    }

    /**
     * This is the output of this screen
     *
     * @param viewState - viewState the current view state that should be displayed
     */
    @Override
    public void render(SearchViewState viewState) {
        if (viewState instanceof SearchViewState.SearchNotStartedYet) {
            renderSearchNotStarted();
        } else if (viewState instanceof SearchViewState.Loading) {
            renderLoading();
        } else if (viewState instanceof SearchViewState.SearchResult) {
            renderResult(((SearchViewState.SearchResult) viewState).getResult());
        } else if (viewState instanceof SearchViewState.EmptyResult) {
            renderEmptyResult();
        } else if (viewState instanceof SearchViewState.Error) {
            renderError();
        } else {
            throw new IllegalArgumentException("Don't know how to render viewState " + viewState);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unbinder.unbind();
    }

    @NonNull
    @Override
    public SearchPresenter createPresenter() {
        return SampleApplication.getDependencyInjection(this).newSearchPresenter();
    }

    private void renderResult(List<Product> result) {
        TransitionManager.beginDelayedTransition(container);
        recyclerView.setVisibility(View.VISIBLE);
        loadingView.setVisibility(View.GONE);
        emptyView.setVisibility(View.GONE);
        errorView.setVisibility(View.GONE);
        adapter.setProducts(result);
        adapter.notifyDataSetChanged();
    }

    private void renderSearchNotStarted() {
        TransitionManager.beginDelayedTransition(container);
        recyclerView.setVisibility(View.GONE);
        loadingView.setVisibility(View.GONE);
        errorView.setVisibility(View.GONE);
        emptyView.setVisibility(View.GONE);
    }

    private void renderLoading() {
        TransitionManager.beginDelayedTransition(container);
        recyclerView.setVisibility(View.GONE);
        loadingView.setVisibility(View.VISIBLE);
        errorView.setVisibility(View.GONE);
        emptyView.setVisibility(View.GONE);
    }

    private void renderError() {
        TransitionManager.beginDelayedTransition(container);
        recyclerView.setVisibility(View.GONE);
        loadingView.setVisibility(View.GONE);
        errorView.setVisibility(View.VISIBLE);
        emptyView.setVisibility(View.GONE);
    }

    private void renderEmptyResult() {
        TransitionManager.beginDelayedTransition(container);
        recyclerView.setVisibility(View.GONE);
        loadingView.setVisibility(View.GONE);
        errorView.setVisibility(View.GONE);
        emptyView.setVisibility(View.VISIBLE);
    }
}
