package net.josecortes.com.googlebooksapp;

import android.support.test.espresso.IdlingResource;

public class SearchIdlingResource implements IdlingResource {

    private ResourceCallback resourceCallback;
    private BookListActivity bookListActivity;

    public SearchIdlingResource(BookListActivity activity) {
        bookListActivity = activity;
    }

    @Override
    public String getName() {
        return SearchIdlingResource.class.getName();
    }

    @Override
    public boolean isIdleNow() {
        // the resource becomes idle when the progress has been dismissed
        if (bookListActivity.isPerformingSearch) {
            return false;
        } else {
            resourceCallback.onTransitionToIdle();
            return true;
        }
    }

    @Override
    public void registerIdleTransitionCallback(ResourceCallback resourceCallback) {
        this.resourceCallback = resourceCallback;
    }
}