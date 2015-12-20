package com.kallavistudios.samplesearch.di.component;


import com.kallavistudios.samplesearch.SampleSearchApplication;
import com.kallavistudios.samplesearch.di.module.ActivityModule;
import com.kallavistudios.samplesearch.di.module.AppModule;
import com.kallavistudios.samplesearch.di.module.RestModule;

import javax.inject.Singleton;

import dagger.Component;

@Singleton
@Component(modules = {AppModule.class, RestModule.class})
public interface SampleSearchComponent {
    ActivityComponent plus(ActivityModule module);

    void inject(SampleSearchApplication application);

    final class Initializer {
        public static SampleSearchComponent init(SampleSearchApplication application) {
            return DaggerSampleSearchComponent.builder().appModule(new AppModule(application)).build();
        }

        private Initializer() {
        }
    }
}
