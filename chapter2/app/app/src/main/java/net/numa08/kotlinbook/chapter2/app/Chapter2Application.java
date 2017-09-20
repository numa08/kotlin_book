package net.numa08.kotlinbook.chapter2.app;

import android.app.Application;

import net.numa08.kotlinbook.chapter2.repositories.ApplicationInformationRepository;
import net.numa08.kotlinbook.chapter2.repositories.impl.ApplicationInformationRepositoryImpl;

public final class Chapter2Application extends Application {

    private ApplicationInformationRepository applicationInformationRepository;

    public ApplicationInformationRepository getApplicationInformationRepository() {
        return applicationInformationRepository;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        applicationInformationRepository = new ApplicationInformationRepositoryImpl(getPackageManager());
    }
}
