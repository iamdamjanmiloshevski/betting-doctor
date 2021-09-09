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

package com.twoplaytech.drbetting.di

import com.twoplaytech.drbetting.domain.usecases.*
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent

/*
    Author: Damjan Miloshevski 
    Created on 24.8.21 10:50
    Project: Dr.Betting
    © 2Play Tech  2021. All rights reserved
*/
@Module
@InstallIn(ViewModelComponent::class)
interface UseCasesModule {
    @Binds
    fun bindGetBettingTipsUseCase(getBettingTipsUseCaseImpl: GetBettingTipsUseCaseImpl): GetBettingTipsUseCase

    @Binds
    fun bindInsertBettingTipUseCase(insertBettingTipUseCaseImpl: InsertBettingTipUseCaseImpl): InsertBettingTipUseCase

    @Binds
    fun bindDeleteBettingUseCase(deleteBettingTipUseCaseImpl: DeleteBettingTipUseCaseImpl): DeleteBettingTipUseCase

    @Binds
    fun bindUpdateBettingTipsUseCase(updateBettingTipUseCaseImpl: UpdateBettingTipUseCaseImpl): UpdateBettingTipUseCase

    @Binds
    fun bindSignInUserUserCase(signInUseCaseImpl: SignInUseCaseImpl): SignInUseCase

    @Binds
    fun bindAppLaunchUseCase(appLaunchUseCaseImpl: AppLaunchUseCaseImpl):AppLaunchUseCase
}