@file:Suppress("FunctionName")

package net.numa08.kotlinbook.chapter2.viewmodels

import android.content.pm.ApplicationInfo
import android.graphics.drawable.ColorDrawable
import com.nhaarman.mockito_kotlin.any
import com.nhaarman.mockito_kotlin.mock
import kotlinx.coroutines.experimental.Deferred
import kotlinx.coroutines.experimental.async
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
            on { findAllApplicationsAsync() }.
                    thenReturn(async {
                        return@async listOf(
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
                    })
        }

        val mockProcessInformationRepository = mock<ProcessInformationRepository> {
            on { findProcessInformationByNameAsync(any()) }.
                    thenReturn(async {
                        return@async ProcessInformation.InActiveProcessInformation("test")
                    })
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
                fail("ここは呼ばれない")
            }

            override fun findApplicationByPackageName(packageName: String, cb: (ApplicationInformation?) -> Unit) {
                fail("ここは呼ばれない")
            }

            override fun findAllApplicationsAsync(): Deferred<List<ApplicationInformation>> = async {
                return@async emptyList<ApplicationInformation>()
            }

            override fun findApplicationByPackageNameAsync(packageName: String): Deferred<ApplicationInformation?> = async {
                fail("ここは呼ばれない")
                return@async null
            }

        }, object : ProcessInformationRepository {
            override fun findProcessInformationByName(name: String, cb: (ProcessInformation) -> Unit) {
                fail("ここは呼ばれない")
            }

            override fun findProcessInformationByNameAsync(name: String): Deferred<ProcessInformation> = async {
                return@async ProcessInformation.InActiveProcessInformation(name)
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