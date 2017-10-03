package net.numa08.kotlinbook.chapter2.viewmodels;

import android.databinding.BaseObservable;
import android.databinding.Bindable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.VisibleForTesting;
import android.support.v4.util.Pair;

import net.numa08.kotlinbook.chapter2.BR;
import net.numa08.kotlinbook.chapter2.adapters.ApplicationInformationListAdapter;
import net.numa08.kotlinbook.chapter2.databinding.ViewApplicationInformationListRowBinding;
import net.numa08.kotlinbook.chapter2.models.ApplicationInformation;
import net.numa08.kotlinbook.chapter2.models.ProcessInformation;
import net.numa08.kotlinbook.chapter2.repositories.ApplicationInformationRepository;
import net.numa08.kotlinbook.chapter2.repositories.ProcessInformationRepository;

import java.util.ArrayList;
import java.util.List;

import kotlin.Unit;
import kotlin.jvm.functions.Function1;

public final class ApplicationInformationListViewModel extends BaseObservable {
    public interface OnClickListener {
        void onClickApplicationInformation(ApplicationInformationListViewModel viewModel, int index, ViewApplicationInformationListRowBinding binding);
    }

    private final ApplicationInformationRepository applicationInformationRepository;
    private final ProcessInformationRepository processInformationRepository;
    private final ApplicationInformationListAdapter adapter = new ApplicationInformationListAdapter();
    @Nullable
    private OnClickListener onClickListener;

    private boolean isLoading;
    private boolean isVisible;

    private List<Pair<ApplicationInformation, ProcessInformation>> applicationInformationList;

    public ApplicationInformationListViewModel(ApplicationInformationRepository applicationInformationRepository, ProcessInformationRepository processInformationRepository) {
        this.applicationInformationRepository = applicationInformationRepository;
        this.processInformationRepository = processInformationRepository;
    }

    @Bindable
    public boolean isLoading() {
        return isLoading;
    }

    public boolean isVisible() {
        return isVisible;
    }

    private void setLoading(boolean loading) {
        isLoading = loading;
        notifyPropertyChanged(BR.loading);
    }

    @Bindable
    @Nullable
    @VisibleForTesting
    List<Pair<ApplicationInformation, ProcessInformation>> getApplicationInformationList() {
        return applicationInformationList;
    }

    public void setOnClickListener(@Nullable OnClickListener onClickListener) {
        this.onClickListener = onClickListener;
    }

    private void setApplicationInformationList(List<Pair<ApplicationInformation, ProcessInformation>> applicationInformationList) {
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
            public void onFindAllApplications(@NonNull final List<ApplicationInformation> list) {
                recursiveFetchProcessInformation(new ArrayList<Pair<ApplicationInformation, ProcessInformation>>(), list);
            }
        });
    }

    private void recursiveFetchProcessInformation(final List<Pair<ApplicationInformation, ProcessInformation>> list, final List<ApplicationInformation> infoList) {
        if (infoList.isEmpty()) {
            setLoading(false);
            setApplicationInformationList(list);
            return;
        }
        final ApplicationInformation info = infoList.get(0);
        processInformationRepository.findProcessInformationByName(info.getPackageName(), new Function1<ProcessInformation, Unit>() {
            @Override
            public Unit invoke(ProcessInformation processInformation) {
                list.add(Pair.create(info, processInformation));
                final ArrayList<ApplicationInformation> copy = new ArrayList<>(infoList);
                copy.remove(0);
                recursiveFetchProcessInformation(list, copy);
                return Unit.INSTANCE;
            }
        });
    }

    public void onDestroy() {
        isVisible = false;
    }


}
