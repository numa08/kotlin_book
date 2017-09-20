package net.numa08.kotlinbook.chapter2.repositories.impl;

import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.graphics.Palette;

import net.numa08.kotlinbook.chapter2.models.ApplicationInformation;
import net.numa08.kotlinbook.chapter2.repositories.ApplicationInformationRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class ApplicationInformationRepositoryImpl implements ApplicationInformationRepository {

    private final PackageManager packageManager;

    public ApplicationInformationRepositoryImpl(PackageManager packageManager) {
        this.packageManager = packageManager;
    }

    @Override
    public void findAllApplications(final FindAllApplicationsCallback callback) {
        final Handler callbackHandler = new Handler(Looper.myLooper());
        new Thread(new Runnable() {
            @Override
            public void run() {
                final List<ApplicationInformation> applications = findAllApplications();
                callbackHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        callback.onFindAllApplications(applications);
                    }
                });
            }
        }).start();

    }

    @Override
    public void findApplicationByPackageName(final String packageName, final FindApplicationCallback callback) {
        final Handler callbackHandler = new Handler(Looper.myLooper());
        new Thread(new Runnable() {
            @Override
            public void run() {
                final ApplicationInformation applicationInformation = findApplicationByPackageName(packageName);
                callbackHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        callback.onFindApplication(applicationInformation);
                    }
                });
            }
        }).start();
    }

    @Nullable
    private ApplicationInformation findApplicationByPackageName(String packgeName) {
        try {
            final ApplicationInfo applicationInfo = packageManager.getApplicationInfo(packgeName, 0);
            return convertApplicationInfo(applicationInfo);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
            return null;
        }
    }

    private List<ApplicationInformation> findAllApplications() {
        final ExecutorService exec = Executors.newFixedThreadPool(5);
        final List<Future<ApplicationInformation>> tasks = new ArrayList<>();
        final List<ApplicationInformation> apps = new ArrayList<>();
        for (final ApplicationInfo appInfo :
                packageManager.getInstalledApplications(0)) {
            if ((appInfo.flags & ApplicationInfo.FLAG_SYSTEM) != 0) {
                continue;
            }
            final Future<ApplicationInformation> task = exec.submit(new Callable<ApplicationInformation>() {

                @Override
                public ApplicationInformation call() throws Exception {
                    return convertApplicationInfo(appInfo);
                }
            });
            tasks.add(task);
        }

        for (Future<ApplicationInformation> task :
                tasks) {
            //noinspection TryWithIdenticalCatches
            try {
                apps.add(task.get());
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                e.printStackTrace();
            }
        }
        exec.shutdown();
        return apps;
    }

    @NonNull
    private ApplicationInformation convertApplicationInfo(ApplicationInfo appInfo) {
        final CharSequence label = appInfo.loadLabel(packageManager);
        final Drawable icon = appInfo.loadIcon(packageManager);
        final CharSequence description = appInfo.loadDescription(packageManager);
        final int lightVibrantRGB;
        final int vibrantRGB;
        final int bodyTextColor;
        final int titleTextColor;
        if (icon instanceof BitmapDrawable) {
            final Bitmap bitmap = ((BitmapDrawable) icon).getBitmap();
            final Palette palette = new Palette.Builder(bitmap).generate();
            final Palette.Swatch lightVibrant = palette.getLightVibrantSwatch();
            if (lightVibrant != null) {
                lightVibrantRGB = lightVibrant.getRgb();
                bodyTextColor = lightVibrant.getBodyTextColor();
                titleTextColor = lightVibrant.getTitleTextColor();
            } else {
                lightVibrantRGB = bodyTextColor = titleTextColor = 0;
            }
            final Palette.Swatch vibrantSwatch = palette.getVibrantSwatch();
            if (vibrantSwatch != null) {
                vibrantRGB = vibrantSwatch.getRgb();
            } else {
                vibrantRGB = 0;
            }
        } else {
            lightVibrantRGB = vibrantRGB = bodyTextColor = titleTextColor = 0;
        }
        return new ApplicationInformation(
                label,
                icon,
                description,
                lightVibrantRGB,
                vibrantRGB,
                bodyTextColor,
                titleTextColor,
                appInfo.packageName,
                appInfo);
    }
}
