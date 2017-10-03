package net.numa08.kotlinbook.chapter2.app;

import android.app.Application;

import net.numa08.kotlinbook.chapter2.repositories.ApplicationInformationRepository;
import net.numa08.kotlinbook.chapter2.repositories.ProcessInformationRepository;
import net.numa08.kotlinbook.chapter2.repositories.impl.ApplicationInformationRepositoryImpl;
import net.numa08.kotlinbook.chapter2.repositories.impl.ProcessInformationRepositoryImpl;

public final class Chapter2Application extends Application {

    private ApplicationInformationRepository applicationInformationRepository;
    private ProcessInformationRepository processInformationRepository;

    public ApplicationInformationRepository getApplicationInformationRepository() {
        return applicationInformationRepository;
    }

    public ProcessInformationRepository getProcessInformationRepository() {
        return processInformationRepository;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        processInformationRepository = ProcessInformationRepositoryImpl.repository(this);
        applicationInformationRepository = new ApplicationInformationRepositoryImpl(getPackageManager());
    }
}
