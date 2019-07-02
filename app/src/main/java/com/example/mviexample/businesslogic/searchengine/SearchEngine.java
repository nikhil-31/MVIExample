package com.example.mviexample.businesslogic.searchengine;

import android.support.annotation.NonNull;
import com.example.mviexample.businesslogic.model.Product;
import com.example.mviexample.businesslogic.http.ProductBackendApiDecorator;
import io.reactivex.Observable;

import java.util.List;
import java.util.concurrent.TimeUnit;

public class SearchEngine {
    private final ProductBackendApiDecorator backend;

    public SearchEngine(ProductBackendApiDecorator productApi) {
        this.backend = productApi;
    }

    public Observable<List<Product>> searchFor(@NonNull String searchQueryText) {

        if (searchQueryText == null) {
            return Observable.error(new NullPointerException("SearchQueryText == null"));
        }

        if (searchQueryText.length() == 0) {
            return Observable.error(new IllegalArgumentException("SearchQueryTest is blank"));
        }

        return backend.getAllProducts()
                .delay(1000, TimeUnit.MILLISECONDS)
                .flatMap(Observable::fromIterable)
                .filter(product -> isProductMatchingSearchCriteria(product, searchQueryText))
                .toList()
                .toObservable();
    }

    /**
     * Filters those items that contains the search query text in name, description or category
     */
    private boolean isProductMatchingSearchCriteria(Product product, String searchQueryText) {
        String words[] = searchQueryText.split(" ");
        for (String w : words) {
            if (product.getName().contains(w)) return true;
            if (product.getDescription().contains(w)) return true;
            if (product.getCategory().contains(w)) return true;
        }
        return false;
    }
}
