package net.numa08.kotlinbook.chapter2.activities;

import android.content.DialogInterface;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.widget.LinearLayout;

import net.numa08.kotlinbook.chapter2.R;
import net.numa08.kotlinbook.chapter2.app.Chapter2Application;
import net.numa08.kotlinbook.chapter2.databinding.ActivityApplicationInformationListBinding;
import net.numa08.kotlinbook.chapter2.databinding.ViewApplicationInformationListRowBinding;
import net.numa08.kotlinbook.chapter2.models.ApplicationInformation;
import net.numa08.kotlinbook.chapter2.util.ContextExtensions;
import net.numa08.kotlinbook.chapter2.viewmodels.ApplicationInformationListViewModel;

public final class ApplicationInformationListActivity extends AppCompatActivity {

    private ActivityApplicationInformationListBinding binding;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_application_information_list);
        binding.setViewModel(
                new ApplicationInformationListViewModel(
                        ((Chapter2Application) getApplication()).getApplicationInformationRepository(),
                        ((Chapter2Application) getApplication()).getProcessInformationRepository())
        );
        binding.getViewModel().onCreate();
        setSupportActionBar(binding.toolbar);
        //noinspection ConstantConditions
        getSupportActionBar().setTitle(R.string.app_name);
        binding.list.setAdapter(binding.getViewModel().getAdapter());
        binding.list.addItemDecoration(new DividerItemDecoration(this, LinearLayout.VERTICAL));
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            if (!ContextExtensions.isAllowedUsageStatsAccess(this)) {
                new AlertDialog.Builder(this)
                        .setTitle(R.string.dialog_usage_stats_access_title)
                        .setMessage(R.string.dialog_usage_stats_access_message)
                        .setPositiveButton(R.string.dialog_usage_stats_access_positive, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                                startActivity(new Intent("android.settings.USAGE_ACCESS_SETTINGS"));
                                finish();
                            }
                        }).setCancelable(false).create().show();
            } else {
                binding.getViewModel().fetchApplication();
            }
        } else {
            binding.getViewModel().fetchApplication();
        }
        binding.getViewModel().setOnClickListener(new ApplicationInformationListViewModel.OnClickListener() {

            @Override
            public void onClickApplicationInformation(ApplicationInformationListViewModel viewModel, int index, ViewApplicationInformationListRowBinding binding) {
                final ApplicationInformation applicationInformation = binding.getViewModel().getApplicationInformation();
                if (applicationInformation == null) {
                    return;
                }
                final Intent intent = ApplicationInformationActivity.getIntent(ApplicationInformationListActivity.this, applicationInformation);
                startActivity(intent,
                        ActivityOptionsCompat.makeSceneTransitionAnimation(
                                ApplicationInformationListActivity.this,
                                binding.icon,
                                getString(R.string.transition_name_icon)
                        ).toBundle());
            }
        });
    }

    @Override
    protected void onDestroy() {
        binding.getViewModel().onDestroy();
        super.onDestroy();
    }
}
