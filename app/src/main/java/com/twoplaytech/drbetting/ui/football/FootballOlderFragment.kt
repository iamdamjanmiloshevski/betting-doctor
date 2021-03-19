/*
 * This is free and unencumbered software released into the public domain.
 *
 * Anyone is free to copy, modify, publish, use, compile, sell, or
 * distribute this software, either in source code form or as a compiled
 * binary, for any purpose, commercial or non-commercial, and by any
 * means.
 *
 * In jurisdictions that recognize copyright laws, the author or authors
 * of this software dedicate any and all copyright interest in the
 * software to the public domain. We make this dedication for the benefit
 * of the public at large and to the detriment of our heirs and
 * successors. We intend this dedication to be an overt act of
 * relinquishment in perpetuity of all present and future rights to this
 * software under copyright law.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND,
 * EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF
 * MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT.
 * IN NO EVENT SHALL THE AUTHORS BE LIABLE FOR ANY CLAIM, DAMAGES OR
 * OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE,
 * ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR
 * OTHER DEALINGS IN THE SOFTWARE.
 *
 * For more information, please refer to <https://unlicense.org>
 */

package com.twoplaytech.drbetting.ui.football

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import com.twoplaytech.drbetting.data.BettingType
import com.twoplaytech.drbetting.data.Resource
import com.twoplaytech.drbetting.data.Sport
import com.twoplaytech.drbetting.data.Status
import com.twoplaytech.drbetting.ui.common.BaseChildFragment
import dagger.hilt.android.AndroidEntryPoint

/*
    Author: Damjan Miloshevski 
    Created on 3/10/21 12:37 PM

*/
@AndroidEntryPoint
class FootballOlderFragment:BaseChildFragment(),Observer<Resource<List<BettingType>>> {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View{
        initBinding(inflater, container)
        initUI()
        return binding.root
    }
    override fun initUI() {
        binding.noDataView.setVisible(false)
        setUpDataAdapter()
        requestOlderData(Sport.FOOTBALL)
        viewModel.observeOnOlderTips().observe(viewLifecycleOwner, this)
    }
    companion object{
        fun getInstance():FootballOlderFragment{
            return FootballOlderFragment()
        }
    }



    override fun onChanged(resource: Resource<List<BettingType>>) {
       when(resource.status){
           Status.SUCCESS -> {
               binding.progressBar.visibility = View.GONE
               val data = resource.data as List<BettingType>
               adapter.addData(data)
           }
           Status.LOADING -> {
               binding.progressBar.visibility = View.VISIBLE
           }
           Status.ERROR -> {


           }
       }
    }
}