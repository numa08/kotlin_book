package net.numa08.kotlinbook.chapter2.viewmodels;

import android.databinding.BaseObservable;
import android.databinding.Bindable;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;

import net.numa08.kotlinbook.chapter2.BR;
import net.numa08.kotlinbook.chapter2.models.ApplicationInformation;

public final class ApplicationInformationListRowViewModel extends BaseObservable {
    public interface OnClickListener {
        void onClick(ApplicationInformationListRowViewModel viewModel);
    }
    private ApplicationInformation applicationInformation;
    @Nullable
    private OnClickListener onClickListener;

    @Nullable
    public ApplicationInformation getApplicationInformation() {
        return applicationInformation;
    }

    @Nullable @Bindable
    public CharSequence getLabel() {
        if (applicationInformation == null) {
            return null;
        }
        return applicationInformation.getLabel();
    }

    @Nullable @Bindable
    public Drawable getIcon() {
        if (applicationInformation == null) {
            return null;
        }
        return applicationInformation.getIcon();
    }

    @Nullable @Bindable
    public CharSequence getDescription() {
        if (applicationInformation == null) {
            return null;
        }
        return applicationInformation.getDescription();
    }

    public void setOnClickListener(@Nullable OnClickListener onClickListener) {
        this.onClickListener = onClickListener;
    }

    public void onClick(@SuppressWarnings("UnusedParameters") View view) {
        if (onClickListener != null) {
            onClickListener.onClick(this);
        }
    }

    public void setApplicationInformation(@NonNull  ApplicationInformation applicationInformation) {
        this.applicationInformation = applicationInformation;
        notifyPropertyChanged(BR.label);
        notifyPropertyChanged(BR.icon);
        notifyPropertyChanged(BR.description);
    }
}
