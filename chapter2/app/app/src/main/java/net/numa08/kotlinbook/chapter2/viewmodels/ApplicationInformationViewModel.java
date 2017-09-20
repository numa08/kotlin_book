package net.numa08.kotlinbook.chapter2.viewmodels;

import android.content.Context;
import android.databinding.BaseObservable;
import android.databinding.Bindable;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;

import net.numa08.kotlinbook.chapter2.BR;
import net.numa08.kotlinbook.chapter2.models.ApplicationInformation;
import net.numa08.kotlinbook.chapter2.repositories.ApplicationInformationRepository;

import java.util.ArrayList;

public final class ApplicationInformationViewModel extends BaseObservable {

    public interface Injector {
        Context getContext();
    }
    private final ApplicationInformationRepository applicationInformationRepository;

    @Nullable
    private ApplicationInformation applicationInformation;
    final private ArrayAdapter<String> adapter;
    private boolean isVisible;

    public ApplicationInformationViewModel(Injector injector, ApplicationInformationRepository applicationInformationRepository) {
        this.adapter = new ArrayAdapter<>(injector.getContext(), android.R.layout.simple_list_item_1, android.R.id.text1);
        this.applicationInformationRepository = applicationInformationRepository;
    }

    @Nullable @Bindable
    public ApplicationInformation getApplicationInformation() {
        return applicationInformation;
    }

    @Nullable @Bindable
    public Drawable getIcon() {
        if (applicationInformation == null) {
            return null;
        }
        return applicationInformation.getIcon();
    }

    private void setApplicationInformation(@NonNull final ApplicationInformation applicationInformation) {
        this.applicationInformation = applicationInformation;
        notifyPropertyChanged(BR.icon);
        notifyPropertyChanged(BR.applicationInformation);
        final ArrayList<String> list = new ArrayList<String>() {{
            if (applicationInformation.getApplicationInfo().className != null) {
                add(applicationInformation.getApplicationInfo().className);
            }
            if (applicationInformation.getApplicationInfo().backupAgentName != null) {
                add(applicationInformation.getApplicationInfo().backupAgentName);
            }
            if (applicationInformation.getApplicationInfo().dataDir != null) {
                add(applicationInformation.getApplicationInfo().dataDir);
            }
            if (applicationInformation.getApplicationInfo().manageSpaceActivityName != null) {
                add(applicationInformation.getApplicationInfo().manageSpaceActivityName);
            }
            if (applicationInformation.getApplicationInfo().nativeLibraryDir != null) {
                add(applicationInformation.getApplicationInfo().nativeLibraryDir);
            }
            if (applicationInformation.getApplicationInfo().permission != null) {
                add(applicationInformation.getApplicationInfo().permission);
            }
            if (applicationInformation.getApplicationInfo().publicSourceDir != null) {
                add(applicationInformation.getApplicationInfo().publicSourceDir);
            }
            if (applicationInformation.getApplicationInfo().sourceDir != null) {
                add(applicationInformation.getApplicationInfo().sourceDir);
            }
            if (applicationInformation.getApplicationInfo().taskAffinity != null) {
                add(applicationInformation.getApplicationInfo().taskAffinity);
            }
        }};
        adapter.addAll(list);
    }

    public BaseAdapter getAdapter() {
        return adapter;
    }

    public void onCreate() {
        isVisible = true;
    }

    public void onDestroy() {
        isVisible = false;
    }

    public void fetchApplication(String packageName) {
        applicationInformationRepository.findApplicationByPackageName(packageName, new ApplicationInformationRepository.FindApplicationCallback() {
            @Override
            public void onFindApplication(@Nullable ApplicationInformation information) {
                if (isVisible && information != null) {
                    setApplicationInformation(information);
                }
            }
        });
    }
}
