@file:Suppress("FunctionName")

package net.numa08.kotlinbook.chapter2.viewmodels

import android.content.Context
import android.content.pm.ApplicationInfo
import android.graphics.drawable.ColorDrawable
import com.nhaarman.mockito_kotlin.any
import com.nhaarman.mockito_kotlin.mock
import kotlinx.coroutines.experimental.async
import net.numa08.kotlinbook.chapter2.models.ApplicationInformation
import net.numa08.kotlinbook.chapter2.repositories.ApplicationInformationRepository
import org.hamcrest.core.Is.`is`
import org.junit.Assert.assertThat
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.RuntimeEnvironment


@RunWith(RobolectricTestRunner::class)
class ApplicationInformationViewModelTest {

    @Test
    fun isVisibleプロパティが遷移すること() {
        val viewModel = ApplicationInformationViewModel(mockInjector(), mock())
        assertThat("初期状態では画面に表示されていない扱いとする", viewModel.isVisible, `is`(false))
        viewModel.onCreate()
        assertThat("画面表示語は表示状態となる", viewModel.isVisible, `is`(true))
        viewModel.onDestroy()
        assertThat("画面が終了したあとは非表示状態となる", viewModel.isVisible, `is`(false))

    }

    @Test
    fun データが正しく読み込まれること() {
        val applicationInformation = ApplicationInformation(
                "label 1",
                ColorDrawable(0),
                "description",
                0,
                0,
                0,
                0,
                "package1",
                ApplicationInfo()
        )

        val mockApplicationInformationRepository = mock<ApplicationInformationRepository> {
            on { findApplicationByPackageNameAsync(any()) }.thenReturn(
                    async {
                        return@async applicationInformation
                    }
            )
        }

        val viewModel = ApplicationInformationViewModel(mockInjector(), mockApplicationInformationRepository)
        viewModel.onCreate()
        viewModel.fetchApplication("")
        assertThat("ロードされたアプリケーションの情報が取得できている", viewModel.applicationInformation, `is`(applicationInformation))
        assertThat("画像を取得できている", viewModel.icon, `is`(applicationInformation.icon))

    }

    private fun mockInjector(): ApplicationInformationViewModel.Injector =
            object : ApplicationInformationViewModel.Injector {
                override val context: Context
                    get() = RuntimeEnvironment.application

            }

}