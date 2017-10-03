package net.numa08.kotlinbook.chapter2.repositories.impl

import android.annotation.TargetApi
import android.app.ActivityManager
import android.app.usage.UsageStatsManager
import android.content.Context
import android.os.Build
import android.os.Handler
import android.os.Looper
import net.numa08.kotlinbook.chapter2.models.ProcessInformation
import net.numa08.kotlinbook.chapter2.repositories.ProcessInformationRepository

class ProcessInformationRepositoryImpl(
        private val activityManager: ActivityManager
) : ProcessInformationRepository {

    companion object {
        @JvmStatic
        fun repository(context: Context): ProcessInformationRepository
            = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            ProcessInformationRepositoryImplV21(context.getSystemService("usagestats") as UsageStatsManager)
        } else {
            ProcessInformationRepositoryImpl(context.getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager)
        }
    }

    override fun findProcessInformationByName(name: String, cb: (ProcessInformation) -> Unit) {
        cb(findProcessInformationByName(name))
    }

    private fun findProcessInformationByName(name: String): ProcessInformation {
        activityManager.runningAppProcesses.find { it.processName == name } ?: return ProcessInformation.InActiveProcessInformation(name)
        return ProcessInformation.ActiveProcessInformation(name)
    }
}

@TargetApi(Build.VERSION_CODES.LOLLIPOP)
class ProcessInformationRepositoryImplV21(
        private val usageStatsManager: UsageStatsManager
): ProcessInformationRepository {
    override fun findProcessInformationByName(name: String, cb: (ProcessInformation) -> Unit) {
        val callbackHandler = Handler(Looper.getMainLooper())
        Thread{
            val info = findProcessInformationByName(name)
            callbackHandler.post {
                cb(info)
            }
        }.start()
    }

    private fun findProcessInformationByName(name: String): ProcessInformation {
        val usageStats = usageStatsManager.queryAndAggregateUsageStats(0, System.currentTimeMillis())[name] ?: return ProcessInformation.InActiveProcessInformation(name)
        return ProcessInformation.ActiveProcessInformationV21(name, usageStats.lastTimeUsed)
    }
}