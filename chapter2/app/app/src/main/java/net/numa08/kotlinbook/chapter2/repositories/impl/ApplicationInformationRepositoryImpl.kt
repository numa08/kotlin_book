package net.numa08.kotlinbook.chapter2.repositories.impl

import android.annotation.SuppressLint
import android.content.pm.ApplicationInfo
import android.content.pm.PackageManager
import android.graphics.drawable.BitmapDrawable
import android.os.Handler
import android.os.Looper
import android.support.annotation.VisibleForTesting
import android.support.v7.graphics.Palette
import net.numa08.kotlinbook.chapter2.models.ApplicationInformation
import net.numa08.kotlinbook.chapter2.repositories.ApplicationInformationRepository
import java.util.concurrent.Callable
import java.util.concurrent.ExecutionException
import java.util.concurrent.Executors

class ApplicationInformationRepositoryImpl(private val packageManager: PackageManager) : ApplicationInformationRepository {

    @SuppressLint("VisibleForTests")
    override fun findAllApplications(cb: ((List<ApplicationInformation>) -> Unit)) {
        val callbackHandler = Handler(Looper.myLooper())
        Thread(Runnable {
            val applications = findAllApplications()
            callbackHandler.post { cb(applications) }
        }).start()

    }

    @SuppressLint("VisibleForTests")
    override fun findApplicationByPackageName(packageName: String, cb: (ApplicationInformation?) -> Unit) {
        val callbackHandler = Handler(Looper.myLooper())
        Thread(Runnable {
            val applicationInformation = findApplicationByPackageName(packageName)
            callbackHandler.post { cb(applicationInformation) }
        }).start()
    }

    @VisibleForTesting
    fun findApplicationByPackageName(packageName: String): ApplicationInformation? {
        return try {
            val applicationInfo = packageManager.getApplicationInfo(packageName, 0)
            convertApplicationInfo(applicationInfo)
        } catch (e: PackageManager.NameNotFoundException) {
            e.printStackTrace()
            null
        }
    }

    @VisibleForTesting
    fun findAllApplications(): List<ApplicationInformation> {
        val exec = Executors.newFixedThreadPool(5)
        val apps =
                packageManager.getInstalledApplications(0)
                        .filter { it.flags and ApplicationInfo.FLAG_SYSTEM == 0 }
                        .map { exec.submit(Callable { convertApplicationInfo(it) }) }
                        .mapNotNull { task ->
                            try {
                                task.get()
                            } catch (e: InterruptedException) {
                                e.printStackTrace()
                                null
                            } catch (e: ExecutionException) {
                                e.printStackTrace()
                                null
                            }
                        }
        exec.shutdown()
        return apps
    }

    @VisibleForTesting
    fun convertApplicationInfo(appInfo: ApplicationInfo): ApplicationInformation {
        val label = appInfo.loadLabel(packageManager)
        val icon = appInfo.loadIcon(packageManager)
        val description = appInfo.loadDescription(packageManager)
        val lightVibrantRGB: Int?
        val vibrantRGB: Int?
        val bodyTextColor: Int?
        val titleTextColor: Int?
        if (icon is BitmapDrawable) {
            val bitmap = icon.bitmap
            val palette = Palette.Builder(bitmap).generate()
            val lightVibrant = palette.lightVibrantSwatch
            lightVibrantRGB = lightVibrant?.rgb
            bodyTextColor = lightVibrant?.bodyTextColor
            titleTextColor = lightVibrant?.titleTextColor
            val vibrantSwatch = palette.vibrantSwatch
            vibrantRGB = vibrantSwatch?.rgb
        } else {
            titleTextColor = null
            bodyTextColor = titleTextColor
            vibrantRGB = bodyTextColor
            lightVibrantRGB = vibrantRGB
        }
        return ApplicationInformation(
                label,
                icon,
                description,
                lightVibrantRGB,
                vibrantRGB,
                bodyTextColor,
                titleTextColor,
                appInfo.packageName,
                appInfo)
    }
}
