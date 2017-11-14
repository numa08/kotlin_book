@file:Suppress("FunctionName")

package net.numa08.kotlinbook.chapter2.repositories.impl

import android.content.pm.ApplicationInfo
import android.content.pm.PackageManager
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.Drawable
import com.nhaarman.mockito_kotlin.any
import com.nhaarman.mockito_kotlin.mock
import org.hamcrest.CoreMatchers.nullValue
import org.hamcrest.core.Is.`is`
import org.junit.Assert.assertThat
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner


@RunWith(RobolectricTestRunner::class)
class ApplicationInformationRepositoryImplTest {

    @Test
    fun 例外が発生したときにはアプリケーションの情報を取得できない() {
        val packageManager = mock<PackageManager> {
            on(it.getApplicationInfo(any(), any()))
                    .thenThrow(PackageManager.NameNotFoundException())
        }
        val repository = ApplicationInformationRepositoryImpl(packageManager)
        val application = repository.
                findApplicationByPackageName("package")
        assertThat(application, `is`(nullValue()))
    }

    @Test
    fun アイコンがないアプリケーション情報の取得ができる() {
        val icon = ColorDrawable(0)
        val appInfo = mock<ApplicationInfo> {
            it.packageName = "package"
            on(it.loadLabel(any())).thenReturn("label")
            on(it.loadIcon(any())).thenReturn(icon)
            on(it.loadDescription(any())).thenReturn("description")
        }
        val applicationInformation =
                ApplicationInformationRepositoryImpl(mock { }).
                        convertApplicationInfo(appInfo)
        assertThat(applicationInformation.label,
                `is`("label" as CharSequence))
        assertThat(applicationInformation.icon,
                `is`(icon as Drawable))
        assertThat(applicationInformation.description,
                `is`("description" as CharSequence))
        assertThat(applicationInformation.lightVibrantRGB,
                `is`(0))
        assertThat(applicationInformation.vibrantRGB,
                `is`(0))
        assertThat(applicationInformation.bodyTextColor,
                `is`(0))
        assertThat(applicationInformation.titleTextColor,
                `is`(0))
        assertThat(applicationInformation.packageName,
                `is`("package"))
    }

    @Test
    fun flagがFLAG_SYSTEMのアプリケーション情報を取得しない() {
        val mockList = listOf<ApplicationInfo>(
                mock {
                    it.flags = ApplicationInfo.FLAG_SYSTEM
                    on(it.loadLabel(any())).thenReturn("label")
                    on(it.loadIcon(any())).thenReturn(ColorDrawable(0))
                },
                mock {
                    it.flags = ApplicationInfo.FLAG_ALLOW_BACKUP
                    it.packageName = "package1"
                    on(it.loadLabel(any())).thenReturn("label")
                    on(it.loadIcon(any())).thenReturn(ColorDrawable(0))
                },
                mock {
                    it.flags = ApplicationInfo.FLAG_ALLOW_BACKUP
                    it.packageName = "package2"
                    on(it.loadLabel(any())).thenReturn("label")
                    on(it.loadIcon(any())).thenReturn(ColorDrawable(0))
                }
        )
        val packageManager = mock<PackageManager> {

            on(it.getInstalledApplications(any())).thenReturn(mockList)
        }
        val applications =
                ApplicationInformationRepositoryImpl(packageManager)
                        .findAllApplications()
        assertThat(applications.size, `is`(2))
        assertThat(applications[0].packageName, `is`("package1"))
        assertThat(applications[1].packageName, `is`("package2"))
    }
}