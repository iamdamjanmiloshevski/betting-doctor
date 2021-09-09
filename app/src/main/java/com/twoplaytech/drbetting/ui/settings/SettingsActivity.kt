/*
 * MIT License
 *
 * Copyright (c)  2021 Damjan Miloshevski
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package com.twoplaytech.drbetting.ui.settings

import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.webkit.WebResourceRequest
import android.webkit.WebResourceResponse
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.ProgressBar
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatDelegate
import androidx.appcompat.widget.Toolbar
import androidx.core.view.isVisible
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.afollestad.materialdialogs.MaterialDialog
import com.afollestad.materialdialogs.list.listItemsSingleChoice
import com.twoplaytech.drbetting.ui.AppInfoActivity
import com.twoplaytech.drbetting.R
import com.twoplaytech.drbetting.data.models.Status
import com.twoplaytech.drbetting.databinding.ActivitySettingsBinding
import com.twoplaytech.drbetting.persistence.IPreferences.Companion.KEY_DARK_MODE
import com.twoplaytech.drbetting.ui.adapters.OnSettingsItemClickListener
import com.twoplaytech.drbetting.ui.adapters.SettingsRecyclerViewAdapter
import com.twoplaytech.drbetting.ui.common.BaseActivity
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SettingsActivity : BaseActivity(), OnSettingsItemClickListener {
    private lateinit var binding: ActivitySettingsBinding
    private val settingsAdapter = SettingsRecyclerViewAdapter()
    private val viewModel: SettingsViewModel by viewModels()
    private lateinit var progressBar: ProgressBar
    private lateinit var webView: WebView
    private lateinit var settingsItems: RecyclerView
    private lateinit var toolbar: Toolbar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySettingsBinding.inflate(LayoutInflater.from(this))
        toolbar = binding.toolbar
        setSupportActionBar(toolbar)
        initUI()
        setContentView(binding.root)
        observeData()
    }

    override fun initUI() {
        progressBar = binding.content.progressBar
        webView = binding.content.webView
        settingsItems = binding.content.rvItems
        changeTheme(toolbar = toolbar)
        toolbar.setNavigationIcon(R.drawable.ic_back)
        toolbar.setNavigationOnClickListener {
            onBackPressed()
        }
        settingsAdapter.setOnSettingsItemClickListener(this)
        settingsItems.apply {
            this.adapter = settingsAdapter
            layoutManager =
                LinearLayoutManager(this@SettingsActivity, LinearLayoutManager.VERTICAL, false)
            this.addItemDecoration(
                DividerItemDecoration(
                    this@SettingsActivity,
                    LinearLayoutManager.VERTICAL
                )
            )
        }
    }

    override fun observeData() {
        viewModel.observeUrl().observe(this, { resource ->
            when (resource.status) {
                Status.SUCCESS -> {
                    val url = resource.data
                    url?.let {
                        settingsItems.visibility = View.GONE
                        webView.visibility = View.VISIBLE
                        loadUrl(it)
                    }
                }
                Status.ERROR -> {

                }
                Status.LOADING -> {

                }
            }
        })
    }

    override fun onSettingsItemClick(item: SettingsItem) {
        when (item) {
            is SettingsItem.AppInfo -> {
                startActivity(Intent(this, AppInfoActivity::class.java))
            }
            is SettingsItem.PrivacyPolicy -> {
                viewModel.setUrl(PRIVACY_POLICY)
            }
            is SettingsItem.RateUs -> {
                startActivity(
                    Intent(
                        Intent.ACTION_VIEW,
                        Uri.parse("market://details?id=$packageName")
                    )
                )
            }
            is SettingsItem.TermsOfUse -> {
                viewModel.setUrl(TERMS_OF_USE)
            }
            is SettingsItem.ThirdPartySoftware -> {
                viewModel.setUrl(THIRD_PARTY_SOFTWARE)
            }
            is SettingsItem.NightMode -> {
                val darkMode = preferencesManager.getInteger(KEY_DARK_MODE)
                MaterialDialog(this).show {
                    title(R.string.dark_mode)
                    cancelable(false)
                    listItemsSingleChoice(
                        R.array.dark_mode_options,
                        initialSelection = darkMode
                    ) { _, index, _ ->
                        when (index) {
                            0 -> AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
                            1 -> AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
                            2 -> AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM)
                        }
                        preferencesManager.saveInteger(KEY_DARK_MODE,index)
                    }
                }
            }
            else -> return
        }
    }

    private fun loadUrl(url: String) {
        webView.loadUrl(url)
        webView.webViewClient = object : WebViewClient() {
            override fun onReceivedHttpError(
                view: WebView?,
                request: WebResourceRequest?,
                errorResponse: WebResourceResponse?
            ) {
            }

            override fun onPageStarted(view: WebView?, url: String?, favicon: Bitmap?) {
                progressBar.visibility = View.VISIBLE
            }

            override fun shouldOverrideUrlLoading(view: WebView?, url: String?): Boolean {
                url?.apply {
                    webView.loadUrl(this)
                }
                return false
            }

            override fun onPageFinished(view: WebView?, url: String?) {
                super.onPageFinished(view, url)
                progressBar.visibility = View.GONE
                toolbar.title = view?.title
            }
        }
    }


    override fun onBackPressed() {
        if (webView.isVisible) {
            webView.visibility = View.GONE
            settingsItems.visibility = View.VISIBLE
            toolbar.title = getString(R.string.title_activity_settings)
        } else {
            super.onBackPressed()
        }
    }

    companion object {
        const val PRIVACY_POLICY = "https://betting-tips-2-odds.firebaseapp.com/privacy-policy.html"
        const val TERMS_OF_USE = "https://betting-tips-2-odds.firebaseapp.com/terms-of-use.html"
        const val THIRD_PARTY_SOFTWARE =
            "https://betting-tips-2-odds.firebaseapp.com/third-party.html"
    }
}