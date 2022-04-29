package com.twoplaytech.drbetting.ui

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.widget.Toolbar
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.twoplaytech.drbetting.R
import com.twoplaytech.drbetting.data.models.FeedbackMessage
import com.twoplaytech.drbetting.data.models.Status
import com.twoplaytech.drbetting.databinding.ActivityFeedbackBinding
import com.twoplaytech.drbetting.ui.common.BaseActivity
import com.twoplaytech.drbetting.ui.common.TextWatcher
import com.twoplaytech.drbetting.ui.states.FeedbackUiState
import com.twoplaytech.drbetting.ui.viewmodels.FeedbackViewModel
import com.twoplaytech.drbetting.util.getRandomBackground
import com.twoplaytech.drbetting.util.isValidEmail
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import timber.log.Timber

@AndroidEntryPoint
class FeedbackActivity : BaseActivity(), View.OnClickListener {
    private lateinit var binding: ActivityFeedbackBinding
    private lateinit var toolbar: Toolbar
    private val feedbackViewModel: FeedbackViewModel by viewModels()
    private var email: String = ""
    private var name: String = ""
    private var message: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFeedbackBinding.inflate(layoutInflater)
        setSupportActionBar(binding.toolbar)
        initUI()
        observeData()
        setContentView(binding.root)
    }


    override fun observeData() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED){
                feedbackViewModel.feedbackState.collectLatest { uiState ->
                    when(uiState){
                        is FeedbackUiState.Error -> {
                            binding.content.loadingView.show(false)
                            Timber.e(uiState.message)
                            Toast.makeText(this@FeedbackActivity, uiState.message, Toast.LENGTH_SHORT).show()
                            clearFocus()
                        }
                        FeedbackUiState.Loading -> {
                            val backgrounds = com.twoplaytech.drbetting.util.getRandomBackground()
                            binding.content.loadingView.setText("Sending feedback... Please wait")
                            binding.content.loadingView.setBackground(backgroundResource = backgrounds.first)
                            binding.content.loadingView.show(true)
                        }
                        is FeedbackUiState.Success -> {
                            binding.content.loadingView.show(false)
                            Toast.makeText(this@FeedbackActivity, "Success", Toast.LENGTH_SHORT).show()
                            clearFocus()
                        }
                        FeedbackUiState.Neutral -> {
                            /**
                             * do nothing
                             */
                        }
                    }
                }
            }
        }
//        feedbackViewModel.observeFeedback().observe(this, Observer { resource ->
//            when (resource.status) {
//                Status.SUCCESS -> {
//                    binding.content.loadingView.show(false)
//                    Toast.makeText(this, resource.message, Toast.LENGTH_SHORT).show()
//                    clearFocus()
//                }
//                Status.ERROR -> {
//                    binding.content.loadingView.show(false)
//                    Toast.makeText(this, resource.message, Toast.LENGTH_SHORT).show()
//                    clearFocus()
//                }
//                Status.LOADING -> {
//                    val backgrounds = com.twoplaytech.drbetting.util.getRandomBackground()
//                    binding.content.loadingView.setText(resource.message ?: "")
//                    binding.content.loadingView.setBackground(backgroundResource = backgrounds.first)
//                    binding.content.loadingView.show(true)
//                }
//            }
//        })
       observeAppTheme()
    }

    private fun clearFocus() {
        val fields = listOf(
            binding.content.email,
            binding.content.name,
            binding.content.message
        )
        fields.forEach {
            it.text = null
            it.clearFocus()
        }
    }


    override fun initUI() {
        binding.content.loadingView.show(false)
        toolbar = binding.toolbar
        changeTheme(toolbar = toolbar, view = binding.content.background)
        binding.content.submit.setBackgroundResource(com.twoplaytech.drbetting.util.getRandomBackground().first)
        toolbar.setNavigationIcon(R.drawable.ic_baseline_arrow_back_24)
        toolbar.setNavigationOnClickListener {
            onBackPressed()
        }
        binding.content.email.addTextChangedListener(TextWatcher(callback = {
            feedbackViewModel.setEmailError(it.isNullOrEmpty())
            email = it.toString()
        }))
        binding.content.name.addTextChangedListener(TextWatcher(callback = {
            feedbackViewModel.setNameError(it.isNullOrEmpty())
            name = it.toString()
        }))
        binding.content.message.addTextChangedListener(TextWatcher(callback = {
            feedbackViewModel.setMessageError(it.isNullOrEmpty())
            message = it.toString()
        }))
        binding.content.submit.setOnClickListener(this)
        setErrors()
    }

    private fun setErrors() {
        feedbackViewModel.setEmailError(email.isEmpty())
        if (!email.isValidEmail()) feedbackViewModel.setEmailError(true)
        feedbackViewModel.setNameError(name.isEmpty())
        feedbackViewModel.setMessageError(message.isEmpty())
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.submit -> {
                if (feedbackViewModel.nameError()) {
                    binding.content.name.error =
                        getString(R.string.blank_name_err)
                }
                if (feedbackViewModel.emailError()) {
                    binding.content.email.error =
                        getString(R.string.blank_email_err)
                }
                if (feedbackViewModel.messageError()) {
                    binding.content.message.error =
                        getString(R.string.blank_msg_err)
                }
                if(!email.isValidEmail()){
                    binding.content.email.error =
                        getString(R.string.invalid_email_err)
                }
                else {
                    val message = FeedbackMessage(name = name, email = email, message = message)
                    feedbackViewModel.sendFeedback(message)
                }
            }
        }
    }

}