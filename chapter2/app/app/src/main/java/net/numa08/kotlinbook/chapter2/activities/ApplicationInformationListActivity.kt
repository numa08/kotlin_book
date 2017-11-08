package net.numa08.kotlinbook.chapter2.activities

import android.content.Intent
import android.databinding.DataBindingUtil
import android.os.Build
import android.os.Bundle
import android.support.v4.app.ActivityOptionsCompat
import android.support.v7.app.AlertDialog
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.DividerItemDecoration
import android.widget.LinearLayout
import net.numa08.kotlinbook.chapter2.R
import net.numa08.kotlinbook.chapter2.app.Chapter2Application
import net.numa08.kotlinbook.chapter2.databinding.ActivityApplicationInformationListBinding
import net.numa08.kotlinbook.chapter2.util.isAllowedUsageStatsAccess
import net.numa08.kotlinbook.chapter2.viewmodels.ApplicationInformationListViewModel

class ApplicationInformationListActivity : AppCompatActivity() {

    private val binding by lazy { DataBindingUtil.setContentView<ActivityApplicationInformationListBinding>(this, R.layout.activity_application_information_list) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding.viewModel = ApplicationInformationListViewModel(
                (application as Chapter2Application).applicationInformationRepository,
                (application as Chapter2Application).processInformationRepository)
        binding.viewModel.onCreate()
        setSupportActionBar(binding.toolbar)

        val actionbar = requireNotNull(supportActionBar, { "setSupportActionBar が呼ばれていません。setSupportActionBar を呼び出してください" })
        actionbar.setTitle(R.string.app_name)
        binding.list.adapter = binding.viewModel.adapter
        binding.list.addItemDecoration(DividerItemDecoration(this, LinearLayout.VERTICAL))
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            if (!isAllowedUsageStatsAccess()) {
                AlertDialog.Builder(this)
                        .setTitle(R.string.dialog_usage_stats_access_title)
                        .setMessage(R.string.dialog_usage_stats_access_message)
                        .setPositiveButton(R.string.dialog_usage_stats_access_positive) { dialog, _ ->
                            dialog.dismiss()
                            startActivity(Intent("android.settings.USAGE_ACCESS_SETTINGS"))
                            finish()
                        }.setCancelable(false).create().show()
            } else {
                binding.viewModel.fetchApplication()
            }
        } else {
            binding.viewModel.fetchApplication()
        }
        binding.viewModel.setOnClickListener({ _, _, binding ->
                    val applicationInformation = binding.viewModel.applicationInformation ?: return@setOnClickListener
                    val intent = ApplicationInformationActivity.getIntent(this@ApplicationInformationListActivity, applicationInformation)
                    startActivity(intent,
                            ActivityOptionsCompat.makeSceneTransitionAnimation(
                                    this@ApplicationInformationListActivity,
                                    binding.icon,
                                    getString(R.string.transition_name_icon)
                            ).toBundle())
                }
        )
    }

    override fun onDestroy() {
        binding.viewModel.onDestroy()
        super.onDestroy()
    }
}
