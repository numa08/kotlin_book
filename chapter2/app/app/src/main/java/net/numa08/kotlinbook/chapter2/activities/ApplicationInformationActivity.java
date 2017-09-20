package net.numa08.kotlinbook.chapter2.activities;

import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.databinding.Observable;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.view.MenuItem;
import android.view.Window;
import android.view.WindowManager;

import net.numa08.kotlinbook.chapter2.BR;
import net.numa08.kotlinbook.chapter2.R;
import net.numa08.kotlinbook.chapter2.app.Chapter2Application;
import net.numa08.kotlinbook.chapter2.databinding.ActivityApplicationInformationBinding;
import net.numa08.kotlinbook.chapter2.models.ApplicationInformation;
import net.numa08.kotlinbook.chapter2.util.ColorUtil;
import net.numa08.kotlinbook.chapter2.viewmodels.ApplicationInformationViewModel;

public final class ApplicationInformationActivity extends AppCompatActivity implements ApplicationInformationViewModel.Injector {

    public static final String ARG_APPLICATION_INFORMATION_PACKAGE = "application_information_package";

    public static Intent getIntent(@NonNull Context context,@NonNull ApplicationInformation applicationInformation) {
        final Intent intent = new Intent(context, ApplicationInformationActivity.class);
        intent.putExtra(ARG_APPLICATION_INFORMATION_PACKAGE, applicationInformation.getPackageName());
        return intent;
    }

    ActivityApplicationInformationBinding binding;

    @Override
    public Context getContext() {
        return this;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_application_information);
        setSupportActionBar(binding.toolbar);
        //noinspection ConstantConditions
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        binding.setViewModel(new ApplicationInformationViewModel(
                this,
                ((Chapter2Application) getApplication()).getApplicationInformationRepository()
        ));
        binding.getViewModel().onCreate();
        final String packageName = getIntent().getStringExtra(ARG_APPLICATION_INFORMATION_PACKAGE);
        binding.list.setAdapter(binding.getViewModel().getAdapter());
        binding.getViewModel().addOnPropertyChangedCallback(new Observable.OnPropertyChangedCallback() {
            @Override
            public void onPropertyChanged(Observable sender, int propertyId) {
                if (propertyId != BR.applicationInformation) {
                    return;
                }
                final ApplicationInformation applicationInformation = binding.getViewModel().getApplicationInformation();
                if (applicationInformation == null) {
                    return;
                }
                final ActionBar actionBar = getSupportActionBar();
                if (actionBar == null) {
                    return;
                }
                actionBar.setTitle(applicationInformation.getLabel());
                if (applicationInformation.getVibrantRGB() != 0) {
                    actionBar.setBackgroundDrawable(new ColorDrawable(applicationInformation.getVibrantRGB()));
                }
                if (applicationInformation.getBodyTextColor() != 0) {
                    final SpannableString spannableString = new SpannableString(actionBar.getTitle());
                    spannableString.setSpan(
                            new ForegroundColorSpan(applicationInformation.getBodyTextColor()),
                            0,
                            spannableString.length(),
                            Spannable.SPAN_INCLUSIVE_INCLUSIVE
                    );
                    actionBar.setTitle(spannableString);
                }
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP && applicationInformation.getVibrantRGB() != 0) {
                    final Window window = getWindow();
                    window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
                    window.setStatusBarColor(ColorUtil.darken(applicationInformation.getVibrantRGB(), 12));
                }
            }
        });
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
            binding.getViewModel().fetchApplication(packageName);
        }
    }

    @Override
    public void onEnterAnimationComplete() {
        super.onEnterAnimationComplete();
        final String packageName = getIntent().getStringExtra(ARG_APPLICATION_INFORMATION_PACKAGE);
        binding.getViewModel().fetchApplication(packageName);
    }

    @Override
    protected void onDestroy() {
        binding.getViewModel().onDestroy();
        super.onDestroy();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            supportFinishAfterTransition();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
