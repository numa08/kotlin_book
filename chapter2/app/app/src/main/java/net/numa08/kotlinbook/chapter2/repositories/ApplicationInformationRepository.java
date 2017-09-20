package net.numa08.kotlinbook.chapter2.repositories;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import net.numa08.kotlinbook.chapter2.models.ApplicationInformation;

import java.util.List;

public interface ApplicationInformationRepository {

    interface FindAllApplicationsCallback {
        void onFindAllApplications(@NonNull List<ApplicationInformation> list);
    }

    interface FindApplicationCallback {
        void onFindApplication(@Nullable ApplicationInformation information);
    }

    void findAllApplications(FindAllApplicationsCallback callback);
    void findApplicationByPackageName(String packageName, FindApplicationCallback callback);

}
