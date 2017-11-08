package net.numa08.kotlinbook.chapter2.activities

import android.content.Context
import android.content.Intent
import android.databinding.DataBindingUtil
import android.databinding.Observable
import android.graphics.drawable.ColorDrawable
import android.os.Build
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.text.Spannable
import android.text.SpannableString
import android.text.style.ForegroundColorSpan
import android.view.MenuItem
import android.view.WindowManager
import net.numa08.kotlinbook.chapter2.BR
import net.numa08.kotlinbook.chapter2.R
import net.numa08.kotlinbook.chapter2.app.Chapter2Application
import net.numa08.kotlinbook.chapter2.databinding.ActivityApplicationInformationBinding
import net.numa08.kotlinbook.chapter2.models.ApplicationInformation
import net.numa08.kotlinbook.chapter2.util.ColorUtil
import net.numa08.kotlinbook.chapter2.viewmodels.ApplicationInformationViewModel

class ApplicationInformationActivity : AppCompatActivity(), ApplicationInformationViewModel.Injector {


    private val binding: ActivityApplicationInformationBinding by lazy { DataBindingUtil.setContentView<ActivityApplicationInformationBinding>(this, R.layout.activity_application_information) }

    override val context: Context = this

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setSupportActionBar(binding.toolbar)

        val actionbar = requireNotNull(supportActionBar, { "setSupportActionBar が呼ばれていません。setSupportActionBar を呼び出してください" })
        actionbar.setDisplayHomeAsUpEnabled(true)
        binding.viewModel = ApplicationInformationViewModel(
                this,
                (application as Chapter2Application).applicationInformationRepository
        )
        binding.viewModel.onCreate()
        val packageName = intent.getStringExtra(ARG_APPLICATION_INFORMATION_PACKAGE)
        binding.list.adapter = binding.viewModel.adapter
        binding.viewModel.addOnPropertyChangedCallback(object : Observable.OnPropertyChangedCallback() {
            override fun onPropertyChanged(sender: Observable, propertyId: Int) {
                if (propertyId != BR.applicationInformation) {
                    return
                }
                val applicationInformation = binding.viewModel.applicationInformation ?: return
                val actionBar = requireNotNull(supportActionBar, { "setSupportActionBar が呼ばれていません。setSupportActionBar を呼び出してください" })
                actionBar.title = applicationInformation.label
                if (applicationInformation.vibrantRGB != 0) {
                    actionBar.setBackgroundDrawable(ColorDrawable(applicationInformation.vibrantRGB))
                }
                if (applicationInformation.bodyTextColor != 0) {
                    val spannableString = SpannableString(actionBar.title)
                    spannableString.setSpan(
                            ForegroundColorSpan(applicationInformation.bodyTextColor),
                            0,
                            spannableString.length,
                            Spannable.SPAN_INCLUSIVE_INCLUSIVE
                    )
                    actionBar.title = spannableString
                }
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP && applicationInformation.vibrantRGB != 0) {
                    val window = window
                    window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
                    window.statusBarColor = ColorUtil.darken(applicationInformation.vibrantRGB, 12)
                }
            }
        })
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
            binding.viewModel.fetchApplication(packageName)
        }
    }

    override fun onEnterAnimationComplete() {
        super.onEnterAnimationComplete()
        val packageName = intent.getStringExtra(ARG_APPLICATION_INFORMATION_PACKAGE)
        binding.viewModel.fetchApplication(packageName)
    }

    override fun onDestroy() {
        binding.viewModel.onDestroy()
        super.onDestroy()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            supportFinishAfterTransition()
            return true
        }
        return super.onOptionsItemSelected(item)
    }

    companion object {

        val ARG_APPLICATION_INFORMATION_PACKAGE = "application_information_package"

        fun getIntent(context: Context, applicationInformation: ApplicationInformation): Intent {
            val intent = Intent(context, ApplicationInformationActivity::class.java)
            intent.putExtra(ARG_APPLICATION_INFORMATION_PACKAGE, applicationInformation.packageName)
            return intent
        }
    }
}
