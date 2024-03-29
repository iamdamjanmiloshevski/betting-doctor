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

package com.twoplaytech.drbetting.ui

import android.os.Bundle
import android.view.LayoutInflater
import com.twoplaytech.drbetting.R
import com.twoplaytech.drbetting.databinding.ActivityAppInfoBinding
import com.twoplaytech.drbetting.ui.common.BaseActivity
import com.twoplaytech.drbetting.util.getVersionName
import com.twoplaytech.drbetting.util.writeCopyright

class AppInfoActivity : BaseActivity() {
    private lateinit var binding: ActivityAppInfoBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAppInfoBinding.inflate(LayoutInflater.from(this))
        setContentView(binding.root)
        changeTheme(view = binding.background)
        binding.tvCopyright.text = this.writeCopyright()
        binding.tvAppVersion.text = getString(R.string.version, this.getVersionName())
    }
}