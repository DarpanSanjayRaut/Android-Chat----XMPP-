package com.pleximus.pet_app.ui.base;

import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;

/**
 * Created by pleximus on 24/04/17.
 */

public abstract class BasePresenter implements Presenter {

    private CompositeDisposable compositeDisposable;

    @Override
    public void onCreate() {
        configureSubscription();
    }

    @Override
    public void onPause() {

    }

    @Override
    public void onResume() {
        configureSubscription();
    }

    /**
     * Initialise CompositeSubscription
     *
     * @return
     */
    private CompositeDisposable configureSubscription() {
        if (compositeDisposable == null || compositeDisposable.isDisposed()) {
            compositeDisposable = new CompositeDisposable();
        }
        return compositeDisposable;
    }

    @Override
    public void onDestroy() {
        unSubscribeAll();
    }

    /**
     * Method to UnSubscribe from all the subscriptions
     */
    protected void unSubscribeAll() {
        if (compositeDisposable != null) {
            compositeDisposable.dispose();
            compositeDisposable.clear();
            // mCompositeSubscription = null;
        }
    }

    /**
     * Method to add Observables to Disposable
     *
     * @param disposable
     */
    protected void addDisposable(Disposable disposable) {
        configureSubscription().add(disposable);
    }

}
