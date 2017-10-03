package net.numa08.kotlinbook.chapter2.viewmodels;

import android.content.Context;
import android.databinding.BaseObservable;
import android.databinding.Bindable;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.format.DateUtils;
import android.view.View;

import net.numa08.kotlinbook.chapter2.BR;
import net.numa08.kotlinbook.chapter2.R;
import net.numa08.kotlinbook.chapter2.models.ApplicationInformation;
import net.numa08.kotlinbook.chapter2.models.ProcessInformation;
import net.numa08.kotlinbook.chapter2.view.TimerTextView;

public final class ApplicationInformationListRowViewModel extends BaseObservable {
    public interface OnClickListener {

        void onClick(ApplicationInformationListRowViewModel viewModel);
    }

    public interface Injector {
        Context context();
    }

    private final Injector injector;
    private ApplicationInformation applicationInformation;
    private ProcessInformation processInformation;

    public ApplicationInformationListRowViewModel(Injector injector) {
        this.injector = injector;
    }

    @Nullable
    private OnClickListener onClickListener;

    @Nullable
    public ApplicationInformation getApplicationInformation() {
        return applicationInformation;
    }

    @Nullable
    @Bindable
    public CharSequence getLabel() {
        if (applicationInformation == null) {
            return null;
        }
        return applicationInformation.getLabel();
    }

    @Nullable
    @Bindable
    public Drawable getIcon() {
        if (applicationInformation == null) {
            return null;
        }
        return applicationInformation.getIcon();
    }

    @Nullable
    @Bindable
    public CharSequence getDescription() {
        if (processInformation == null) {
            return null;
        }
        if (processInformation instanceof ProcessInformation.InActiveProcessInformation) {
            return injector.context().getString(R.string.description_inactive_row_view_nodel);
        }
        if (processInformation instanceof ProcessInformation.ActiveProcessInformation) {
            return injector.context().getString(R.string.description_active_row_view_model);
        }
        if (processInformation instanceof ProcessInformation.ActiveProcessInformationV21) {
            final CharSequence spanString = DateUtils.getRelativeTimeSpanString(((ProcessInformation.ActiveProcessInformationV21) processInformation).getLastStartupTime(), System.currentTimeMillis(), DateUtils.SECOND_IN_MILLIS);
            return injector.context().getString(R.string.description_active_row_v21_view_model, spanString);
        }
        throw new IllegalStateException("ここには来ない");
    }

    @Bindable
    public TimerTextView.TimerTask getTimerTask() {
        return new TimerTextView.TimerTask() {
            @Nullable
            @Override
            public CharSequence onExecute(@NonNull TimerTextView textView) {
                return getDescription();
            }
        };
    }

    public void setOnClickListener(@Nullable OnClickListener onClickListener) {
        this.onClickListener = onClickListener;
    }

    public void onClick(@SuppressWarnings("UnusedParameters") View view) {
        if (onClickListener != null) {
            onClickListener.onClick(this);
        }
    }

    public void setApplicationInformation(@NonNull ApplicationInformation applicationInformation) {
        this.applicationInformation = applicationInformation;
        notifyPropertyChanged(BR.label);
        notifyPropertyChanged(BR.icon);
    }

    public void setProcessInformation(ProcessInformation processInformation) {
        this.processInformation = processInformation;
        notifyPropertyChanged(BR.description);
    }
}
