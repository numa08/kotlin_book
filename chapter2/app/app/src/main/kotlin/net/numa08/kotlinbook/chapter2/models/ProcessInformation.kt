package net.numa08.kotlinbook.chapter2.models

import android.annotation.TargetApi
import android.os.Build


sealed class ProcessInformation {

    abstract val packageName: String

    data class InActiveProcessInformation(
            override val packageName: String
    ) : ProcessInformation()

    data class ActiveProcessInformation(
            override val packageName: String
    ) : ProcessInformation()

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    data class ActiveProcessInformationV21(
            override val packageName: String,
            val lastStartupTime: Long
    ) : ProcessInformation()
}