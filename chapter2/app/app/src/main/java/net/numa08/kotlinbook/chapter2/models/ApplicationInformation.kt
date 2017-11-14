package net.numa08.kotlinbook.chapter2.models

import android.content.pm.ApplicationInfo
import android.graphics.drawable.Drawable

data class ApplicationInformation(
        val label: CharSequence,
        val icon: Drawable,
        val description: CharSequence?,
        val lightVibrantRGB: Int?,
        val vibrantRGB: Int?,
        val bodyTextColor: Int?,
        val titleTextColor: Int?,
        val packageName: String,
        val applicationInfo: ApplicationInfo)
