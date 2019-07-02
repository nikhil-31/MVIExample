package com.example.mviexample.businesslogic.interactor.search;

import com.example.mviexample.businesslogic.model.Product;

import java.util.List;

public interface SearchViewState {


    final class SearchNotStartedYet implements SearchViewState {
    }

    final class Loading implements SearchViewState {

    }


    final class EmptyResult implements SearchViewState {
        private final String searchQueryText;

        public EmptyResult(String searchQueryText) {
            this.searchQueryText = searchQueryText;
        }

        public String getSearchQueryText() {
            return searchQueryText;
        }
    }


    final class SearchResult implements SearchViewState {
        private final String searchQueryText;
        private final List<Product> result;

        public SearchResult(String searchQueryText, List<Product> result) {
            this.searchQueryText = searchQueryText;
            this.result = result;
        }

        public String getSearchQueryText() {
            return searchQueryText;
        }

        public List<Product> getResult() {
            return result;
        }
    }


    final class Error implements SearchViewState {
        private final String searchQueryText;
        private final Throwable error;

        public Error(String searchQueryText, Throwable error) {
            this.searchQueryText = searchQueryText;
            this.error = error;
        }

        public String getSearchQueryText() {
            return searchQueryText;
        }

        public Throwable getError() {
            return error;
        }
    }


}
