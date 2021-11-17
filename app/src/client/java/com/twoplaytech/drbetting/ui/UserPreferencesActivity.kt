package com.twoplaytech.drbetting.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.MenuItem
import androidx.appcompat.widget.Toolbar
import androidx.preference.Preference
import androidx.preference.PreferenceFragmentCompat
import androidx.preference.PreferenceManager
import androidx.preference.SwitchPreferenceCompat
import com.twoplaytech.drbetting.R
import com.twoplaytech.drbetting.databinding.ActivityUserPreferencesBinding
import com.twoplaytech.drbetting.ui.common.BaseActivity
import com.twoplaytech.drbetting.ui.util.NotificationsManager
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class UserPreferencesActivity : BaseActivity() {
    private lateinit var binding: ActivityUserPreferencesBinding
    private lateinit var toolbar: Toolbar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityUserPreferencesBinding.inflate(LayoutInflater.from(this))
        toolbar = binding.toolbar
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        initUI()
        setContentView(binding.root)
        if (savedInstanceState == null) {
            supportFragmentManager
                .beginTransaction()
                .replace(R.id.settings, SettingsFragment())
                .commit()
        }

    }

    override fun initUI() {
        changeTheme(toolbar = toolbar)
    }

    override fun onResume() {
        super.onResume()
        observeData()
    }
    override fun observeData() {
        super.observeData()
        observeAppTheme()
    }
    class SettingsFragment : PreferenceFragmentCompat() {
        override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
            setPreferencesFromResource(R.xml.root_preferences, rootKey)
            bindPreferenceSummaryToValue(findPreference(getString(R.string.notifications_preferences_key)))
        }

        private fun bindPreferenceSummaryToValue(preference: Preference?) {
            preference?.onPreferenceChangeListener = preferenceListener
            preferenceListener.onPreferenceChange(
                preference,
                PreferenceManager
                    .getDefaultSharedPreferences(preference?.context)
                    .getBoolean(preference?.key, true)
            )
        }

        private val preferenceListener =
            Preference.OnPreferenceChangeListener { preference, newValue ->
                if (preference is SwitchPreferenceCompat) {
                    val value =  newValue as Boolean
                    preference.isChecked = value
                    NotificationsManager.toggleNotifications(value)
                }
                true
            }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            onBackPressed()
        }
        return super.onOptionsItemSelected(item)
    }

}