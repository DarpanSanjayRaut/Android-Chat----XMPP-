package com.pleximus.pet_app.ui.splash.core.presenter;

import com.pleximus.pet_app.ui.splash.core.view.ISplashView;

/**
 * Created by pleximus on 03/05/17.
 */

public class SplashScreenImpl implements ISplashPresenter {


    private ISplashView iSplashView;

    public SplashScreenImpl(ISplashView iSplashView) {
        this.iSplashView = iSplashView;
    }

    @Override
    public void onActivityLoad() {
        iSplashView.launchAppropriate();
    }
}
