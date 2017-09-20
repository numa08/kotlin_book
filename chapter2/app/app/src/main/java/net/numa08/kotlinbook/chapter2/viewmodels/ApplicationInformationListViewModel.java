package net.numa08.kotlinbook.chapter2.viewmodels;

import android.databinding.BaseObservable;
import android.databinding.Bindable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.VisibleForTesting;

import net.numa08.kotlinbook.chapter2.BR;
import net.numa08.kotlinbook.chapter2.adapters.ApplicationInformationListAdapter;
import net.numa08.kotlinbook.chapter2.databinding.ViewApplicationInformationListRowBinding;
import net.numa08.kotlinbook.chapter2.models.ApplicationInformation;
import net.numa08.kotlinbook.chapter2.repositories.ApplicationInformationRepository;

import java.util.List;

public final class ApplicationInformationListViewModel extends BaseObservable {
    public interface OnClickListener {
        void onClickApplicationInformation(ApplicationInformationListViewModel viewModel, int index, ViewApplicationInformationListRowBinding binding);
    }
    private final ApplicationInformationRepository applicationInformationRepository;
    private final ApplicationInformationListAdapter adapter = new ApplicationInformationListAdapter();
    @Nullable
    private OnClickListener onClickListener;

    private boolean isLoading;
    boolean isVisible;

    private List<ApplicationInformation> applicationInformationList;

    public ApplicationInformationListViewModel(ApplicationInformationRepository applicationInformationRepository) {
        this.applicationInformationRepository = applicationInformationRepository;
    }

    @Bindable
    public boolean isLoading() {
        return isLoading;
    }

    private void setLoading(boolean loading) {
        isLoading = loading;
        notifyPropertyChanged(BR.loading);
    }

    @Bindable @Nullable @VisibleForTesting
    List<ApplicationInformation> getApplicationInformationList() {
        return applicationInformationList;
    }

    public void setOnClickListener(@Nullable OnClickListener onClickListener) {
        this.onClickListener = onClickListener;
    }

    private void setApplicationInformationList(List<ApplicationInformation> applicationInformationList) {
        this.applicationInformationList = applicationInformationList;
        notifyPropertyChanged(BR.applicationInformationList);
        adapter.getInformationList().clear();
        adapter.getInformationList().addAll(applicationInformationList);
    }

    public ApplicationInformationListAdapter getAdapter() {
        return adapter;
    }

    public void onCreate() {
        isVisible = true;
        setLoading(true);
        adapter.setOnItemClickListener(new ApplicationInformationListAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int index, ViewApplicationInformationListRowBinding binding) {
                if (onClickListener != null) {
                    onClickListener.onClickApplicationInformation(ApplicationInformationListViewModel.this, index, binding);
                }
            }
        });
    }

    public void fetchApplication() {
        applicationInformationRepository.findAllApplications(new ApplicationInformationRepository.FindAllApplicationsCallback() {
            @Override
            public void onFindAllApplications(@NonNull List<ApplicationInformation> list) {
                setLoading(false);
                if (isVisible) {
                    setApplicationInformationList(list);
                }
            }
        });
    }

    public void onDestroy() {
        isVisible = false;
    }
}
