/*
 * Copyright 2016 Hannes Dorfmann.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */

package com.example.mviexample.dependencyinjection;


import com.example.mviexample.businesslogic.http.ProductBackendApi;
import com.example.mviexample.businesslogic.http.ProductBackendApiDecorator;
import com.example.mviexample.businesslogic.interactor.search.SearchInteractor;
import com.example.mviexample.businesslogic.searchengine.SearchEngine;
import com.example.mviexample.view.search.SearchPresenter;
import io.reactivex.Observable;
import io.reactivex.subjects.PublishSubject;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.moshi.MoshiConverterFactory;

/**
 * This is just a very simple example that creates dependency injection.
 * In a real project you might would like to use dagger.
 *
 * @author Hannes Dorfmann
 */
public class DependencyInjection {

//  @SuppressFBWarnings(value = "MS_SHOULD_BE_FINAL", justification = "Changeable for unit testing")
  public static String BASE_URL = "https://raw.githubusercontent.com";
  public static final String BASE_URL_BRANCH = "master";
  public static final String BASE_IMAGE_URL = BASE_URL
      + "/sockeqwe/mosby/"
      + DependencyInjection.BASE_URL_BRANCH
      + "/sample-mvi/server/images/";

  // Don't do this in your real app
  private final PublishSubject<Boolean> clearSelectionRelay = PublishSubject.create();
  private final PublishSubject<Boolean> deleteSelectionRelay = PublishSubject.create();
  //
  // Some singletons
  //
  private final HttpLoggingInterceptor httpLogger = new HttpLoggingInterceptor();
  private final Retrofit retrofit = new Retrofit.Builder().baseUrl(BASE_URL)
      .client(new OkHttpClient.Builder().addInterceptor(httpLogger).build())
      .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
      .addConverterFactory(MoshiConverterFactory.create())
      .build();

  private final ProductBackendApi backendApi = retrofit.create(ProductBackendApi.class);
  private final ProductBackendApiDecorator backendApiDecorator =
      new ProductBackendApiDecorator(backendApi);

//  private final MainMenuPresenter mainMenuPresenter = new MainMenuPresenter(backendApiDecorator);
//  private final ShoppingCart shoppingCart = new ShoppingCart();
//  private final ShoppingCartOverviewPresenter shoppingCartPresenter =
//      new ShoppingCartOverviewPresenter(shoppingCart, deleteSelectionRelay, clearSelectionRelay);


  private SearchEngine newSearchEngine() {
    return new SearchEngine(backendApiDecorator);
  }

  private SearchInteractor newSearchInteractor() {
    return new SearchInteractor(newSearchEngine());
  }

//  PagingFeedLoader newPagingFeedLoader() {
//    return new PagingFeedLoader(backendApiDecorator);
//  }
//
//  GroupedPagedFeedLoader newGroupedPagedFeedLoader() {
//    return new GroupedPagedFeedLoader(newPagingFeedLoader());
//  }
//
//  HomeFeedLoader newHomeFeedLoader() {
//    return new HomeFeedLoader(newGroupedPagedFeedLoader(), backendApiDecorator);
//  }
//
  public SearchPresenter newSearchPresenter() {
    return new SearchPresenter(newSearchInteractor());
  }

//  public HomePresenter newHomePresenter() {
//    return new HomePresenter(newHomeFeedLoader());
//  }
//
//  /**
//   * This is a singleton
//   */
//  public MainMenuPresenter getMainMenuPresenter() {
//    return mainMenuPresenter;
//  }
//
//  public CategoryPresenter newCategoryPresenter() {
//    return new CategoryPresenter(backendApiDecorator);
//  }
//
//  public ProductDetailsPresenter newProductDetailsPresenter() {
//    return new ProductDetailsPresenter(new DetailsInteractor(backendApiDecorator, shoppingCart));
//  }
//
//  /**
//   * This is a singleton
//   */
//  public ShoppingCartOverviewPresenter getShoppingCartPresenter() {
//    return shoppingCartPresenter;
//  }
//
//  public ShoppingCartLabelPresenter newShoppingCartLabelPresenter() {
//    return new ShoppingCartLabelPresenter(shoppingCart);
//  }
//
//  public CheckoutButtonPresenter newCheckoutButtonPresenter() {
//    return new CheckoutButtonPresenter(shoppingCart);
//  }
//
//  public SelectedCountToolbarPresenter newSelectedCountToolbarPresenter() {
//
//    Observable<Integer> selectedItemCountObservable =
//        shoppingCartPresenter.getViewStateObservable().map(items -> {
//          int selected = 0;
//          for (ShoppingCartOverviewItem item : items) {
//            if (item.isSelected()) selected++;
//          }
//          return selected;
//        });
//
//    return new SelectedCountToolbarPresenter(selectedItemCountObservable, clearSelectionRelay,
//        deleteSelectionRelay);
//  }

  public PublishSubject<Boolean> getClearSelectionRelay() {
    return clearSelectionRelay;
  }
}
