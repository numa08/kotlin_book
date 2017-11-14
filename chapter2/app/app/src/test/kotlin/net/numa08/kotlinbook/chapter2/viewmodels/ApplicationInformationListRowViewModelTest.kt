@file:Suppress("FunctionName")

package net.numa08.kotlinbook.chapter2.viewmodels

import android.content.Context
import android.content.pm.ApplicationInfo
import android.graphics.drawable.ColorDrawable
import net.numa08.kotlinbook.chapter2.models.ApplicationInformation
import net.numa08.kotlinbook.chapter2.models.ProcessInformation
import org.hamcrest.core.Is.`is`
import org.junit.Assert.assertThat
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.RuntimeEnvironment

@RunWith(RobolectricTestRunner::class)
class ApplicationInformationListRowViewModelTest {

    private val current = System.currentTimeMillis()

    @Test
    fun プロセスの状況に応じてDescriptionが得られること() {
        val viewModel =
                ApplicationInformationListRowViewModel(mockInjector())
        viewModel.setProcessInformation(
                ProcessInformation.
                        InActiveProcessInformation("package")
        )
        assertThat("起動していないときのメッセージが得られる",
                viewModel.description!!.toString(),
                `is`("起動していません"))
        viewModel.setProcessInformation(
                ProcessInformation.
                        ActiveProcessInformation("package"))
        assertThat("起動中のメッセージが得られる",
                viewModel.description!!.toString(),
                `is`("起動中です"))
        viewModel.setProcessInformation(ProcessInformation
                .ActiveProcessInformationV21("package", current))
        assertThat("起動してからの経過時間を含んだメッセージが得られる",
                viewModel.description!!.toString(),
                `is`("0 秒前に起動しました"))
    }

    @Test
    fun アプリケーションの名前が得られること() {
        val viewModel =
                ApplicationInformationListRowViewModel(mockInjector())
        viewModel.applicationInformation = ApplicationInformation(
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
        assertThat("セットされたアプリケーションの名前が得られる",
                viewModel.label!!.toString(),
                `is`("label 1"))

    }

    private fun mockInjector(): ApplicationInformationListRowViewModel.
    Injector
            = object : ApplicationInformationListRowViewModel.Injector {
        override fun context(): Context = RuntimeEnvironment.application

        override fun currentTimeMillis(): Long = current
    }

}