@file:Suppress("FunctionName")

package net.numa08.kotlinbook.chapter2.viewmodels

import android.content.pm.ApplicationInfo
import android.graphics.drawable.ColorDrawable
import com.nhaarman.mockito_kotlin.any
import com.nhaarman.mockito_kotlin.mock
import net.numa08.kotlinbook.chapter2.models.ApplicationInformation
import net.numa08.kotlinbook.chapter2.models.ProcessInformation
import net.numa08.kotlinbook.chapter2.repositories.ApplicationInformationRepository
import net.numa08.kotlinbook.chapter2.repositories.ProcessInformationRepository
import org.hamcrest.core.Is.`is`
import org.junit.Assert.assertThat
import org.junit.Assert.fail
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner

@RunWith(RobolectricTestRunner::class)
class ApplicationInformationListViewModelTest {

    @Test
    fun データが正しく読み込まれること() {
        val mockApplicationInformationRepository = mock<ApplicationInformationRepository> {
            on { findAllApplications(any()) }.then {
                val cb = it.getArgument<((List<ApplicationInformation>) -> Unit)>(0)
                cb(
                        listOf(
                                ApplicationInformation(
                                        "label 1",
                                        ColorDrawable(0),
                                        "description",
                                        0,
                                        0,
                                        0,
                                        0,
                                        "package1",
                                        ApplicationInfo()
                                ),
                                ApplicationInformation(
                                        "label 2",
                                        ColorDrawable(0),
                                        "description",
                                        0,
                                        0,
                                        0,
                                        0,
                                        "package2",
                                        ApplicationInfo()
                                ),
                                ApplicationInformation(
                                        "label 3",
                                        ColorDrawable(0),
                                        "description",
                                        0,
                                        0,
                                        0,
                                        0,
                                        "package3",
                                        ApplicationInfo()
                                )

                        )
                )
            }
        }

        val mockProcessInformationRepository = mock<ProcessInformationRepository> {
            on { findProcessInformationByName(any(), any()) }.then {
                val name = it.getArgument<String>(0)
                val cb = it.getArgument<(ProcessInformation) -> Unit>(1)
                cb(ProcessInformation.InActiveProcessInformation(name))
            }
        }
        val viewModel = ApplicationInformationListViewModel(mockApplicationInformationRepository, mockProcessInformationRepository)
        viewModel.onCreate()
        viewModel.fetchApplication()
        assertThat("3件の情報が取得できていること", viewModel.applicationInformationList?.size, `is`(3))
        assertThat("ロード状態が遷移していること", viewModel.isLoading, `is`(false))
    }

    @Test
    fun データが空でもisLoadingプロパティが遷移すること() {
        val viewModel = ApplicationInformationListViewModel(object : ApplicationInformationRepository {
            override fun findAllApplications(cb: (List<ApplicationInformation>) -> Unit) {
                cb(emptyList())
            }

            override fun findApplicationByPackageName(packageName: String, cb: (ApplicationInformation?) -> Unit) {
                fail("ここは呼ばれない")
            }
        }, object : ProcessInformationRepository {
            override fun findProcessInformationByName(name: String, cb: (ProcessInformation) -> Unit) {
                val info = ProcessInformation.InActiveProcessInformation(name)
                cb(info)
            }
        })
        assertThat("初期状態ではロード中ではない", viewModel.isLoading, `is`(false))
        viewModel.onCreate()
        assertThat("onCreateをするとロード状態となる", viewModel.isLoading, `is`(true))
        viewModel.fetchApplication()
        assertThat("ロードが完了するとロード状態ではなくなる", viewModel.isLoading, `is`(false))
    }


    @Test
    fun isVisibleプロパティが遷移すること() {
        val viewModel = ApplicationInformationListViewModel(mock(), mock())
        assertThat("初期状態では画面に表示されていない扱いとする", viewModel.isVisible, `is`(false))
        viewModel.onCreate()
        assertThat("画面表示は表示状態となる", viewModel.isVisible, `is`(true))
        viewModel.onDestroy()
        assertThat("画面が終了した後は非表示状態となる", viewModel.isVisible, `is`(false))
    }

}