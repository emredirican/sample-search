package com.kallavistudios.samplesearch.di.component;

import com.kallavistudios.samplesearch.MainActivity;
import com.kallavistudios.samplesearch.di.PerActivity;
import com.kallavistudios.samplesearch.di.module.ActivityModule;

import dagger.Subcomponent;

@PerActivity
@Subcomponent(modules = ActivityModule.class)
public interface ActivityComponent {

    void inject(MainActivity activity);
}
