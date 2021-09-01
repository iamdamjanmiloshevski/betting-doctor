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

package com.twoplaytech.drbetting.admin.ui.auth

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.google.android.material.snackbar.Snackbar
import com.twoplaytech.drbetting.R
import com.twoplaytech.drbetting.admin.ui.admin.AdminActivity
import com.twoplaytech.drbetting.admin.ui.viewmodels.LoginViewModel
import com.twoplaytech.drbetting.admin.util.dispatchCredentialsDialog
import com.twoplaytech.drbetting.admin.util.isValidEmail
import com.twoplaytech.drbetting.admin.util.isValidPasswordFormat
import com.twoplaytech.drbetting.data.entities.Status
import com.twoplaytech.drbetting.databinding.FragmentLoginBinding
import com.twoplaytech.drbetting.ui.common.BaseFragment
import com.twoplaytech.drbetting.ui.common.TextWatcher
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class LoginFragment : BaseFragment() {
    private lateinit var loginBinding: FragmentLoginBinding

    private val loginViewModel: LoginViewModel by viewModels()

    private var email: String = ""
    private var pwd: String = ""
    private var credentialsSaved = false
    private var backgroundId = 0
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        initBinding(inflater, container)
        initUI()
        initListeners()
        return loginBinding.root
    }

    private fun initListeners() {
        loginBinding.btLogin.setOnClickListener {
            when {
                email.isEmpty() -> {
                    loginBinding.lytEmail.error = getString(R.string.email_blank_msg)
                    loginViewModel.enableLogin(false)
                }
                pwd.isEmpty() -> {
                    loginBinding.lytPassword.error = getString(R.string.pwd_blank_msg)
                    loginViewModel.enableLogin(false)
                }
                else -> loginViewModel.login(requireContext(), email, pwd)
            }
        }
        loginBinding.etEmail.addTextChangedListener(TextWatcher { emailInput ->
            if (!emailInput.toString().isValidEmail()) {
                loginViewModel.enableLogin(false)
                loginBinding.lytEmail.error = getString(R.string.invalid_email_format_msg)
            } else {
                loginViewModel.enableLogin(true)
                loginBinding.lytEmail.error = null
                email = emailInput.toString()
            }
        })
        loginBinding.etPassword.addTextChangedListener(TextWatcher { passwordInput ->
            if (passwordInput.isValidPasswordFormat()) {
                loginBinding.lytPassword.error = null
                loginViewModel.enableLogin(true)
                pwd = passwordInput.toString()
            } else {
                loginViewModel.enableLogin(false)
                loginBinding.lytPassword.error =
                    getString(R.string.login_error_msg)
            }
        })
    }

    override fun observeData() {
        loginViewModel.observeLogin().observe(viewLifecycleOwner, { resource ->
            when (resource.status) {
                Status.SUCCESS -> {
                    proceedToApp()
                }
                Status.ERROR -> {
                    displayLoader(false)
                    Snackbar.make(
                        loginBinding.lytLogin,
                        resource.message.toString(),
                        Snackbar.LENGTH_SHORT
                    ).show()
                }
                Status.LOADING -> {
                    val msg = resource.message
                    msg?.let {
                        displayLoader(true, it)
                    }
                }
            }
        })

        loginViewModel.observeForCredentials().observe(viewLifecycleOwner, { resource ->
            when (resource.status) {
                Status.SUCCESS -> {
                    if (resource.data != null) {
                        val credentials = resource.data
                        email = credentials.email
                        pwd = credentials.password
                        credentialsSaved = true
                    } else {
                        email = loginBinding.etEmail.text.toString()
                        pwd = loginBinding.etPassword.text.toString()
                    }
                    if (email.isNotEmpty()) loginBinding.etEmail.setText(email)
                    if (pwd.isNotEmpty()) loginBinding.etPassword.setText(pwd)
                }
                Status.ERROR -> {
                    email = loginBinding.etEmail.text.toString()
                    pwd = loginBinding.etPassword.text.toString()
                }
                Status.LOADING -> {
                }
            }
        })
        loginViewModel.observeLoginEnabled().observe(viewLifecycleOwner, { isEnabled ->
            loginBinding.btLogin.isEnabled = isEnabled
        })
    }

    private fun displayLoader(shouldDisplay: Boolean, loadingMessage: String = "") {
        when (shouldDisplay) {
            true -> {
                loginBinding.loadingView.show(true)
                loginBinding.loadingView.setText(loadingMessage)
                loginBinding.loadingView.setBackground(backgroundId)
                loginBinding.btLogin.visibility = View.GONE
            }
            else -> {
                loginBinding.loadingView.show(false)
                loginBinding.btLogin.visibility = View.VISIBLE
            }
        }

    }

    private fun proceedToApp() {
        val shouldKeepUserSignedIn = loginBinding.cbKeepMeSignedIn.isChecked
        if (shouldKeepUserSignedIn) {
            loginViewModel.saveLogin(shouldKeepUserSignedIn)
            askForCredentialsSave()
        } else {
            loginViewModel.saveLogin(false)
            askForCredentialsSave()
        }
    }

    private fun askForCredentialsSave() {
        if (!credentialsSaved) {
            requireContext().dispatchCredentialsDialog { shouldSaveCredentials ->
                if (shouldSaveCredentials) {
                    loginViewModel.saveUserCredentials(email, pwd)
                    enter()
                } else {
                    enter()
                }
            }
        } else {
            enter()
        }
    }


    override fun initUI() {
        arguments?.let {
            backgroundId = it.getInt(KEY_BACKGROUND_RESOURCE)
            loginBinding.lytLogin.setBackgroundResource(backgroundId)
        }
        loginBinding.loadingView.show(false)
        loginViewModel.enableLogin(false)
        loginViewModel.retrieveCredentials()
        loginViewModel.isLoggedIn(requireContext())
    }

    override fun initBinding(inflater: LayoutInflater, container: ViewGroup?) {
        loginBinding = FragmentLoginBinding.inflate(inflater, container, false)
    }

    override fun onResume() {
        super.onResume()
        observeData()
    }

    private fun enter() {
        val intent = Intent(this.requireContext(), AdminActivity::class.java)
        startActivity(intent)
        this.requireActivity().finish()
    }

    companion object {
        const val KEY_BACKGROUND_RESOURCE = "backgroundId"
    }
}