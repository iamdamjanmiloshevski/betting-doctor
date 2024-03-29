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

package com.twoplaytech.drbetting.admin.di

import com.twoplaytech.drbetting.admin.domain.usecases.*
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent

/*
    Author: Damjan Miloshevski 
    Created on 9.9.21 14:41
    Project: Dr.Betting
    © 2Play Tech  2021. All rights reserved
*/
@Module
@InstallIn(ViewModelComponent::class)
interface UseCasesModule {
    @Binds
    fun bindInsertBettingTipUseCase(insertBettingTipUseCaseImpl: InsertBettingTipUseCaseImpl): InsertBettingTipUseCase

    @Binds
    fun bindDeleteBettingUseCase(deleteBettingTipUseCaseImpl: DeleteBettingTipUseCaseImpl): DeleteBettingTipUseCase

    @Binds
    fun bindUpdateBettingTipsUseCase(updateBettingTipUseCaseImpl: UpdateBettingTipUseCaseImpl): UpdateBettingTipUseCase

    @Binds
    fun bindSignInUserUserCase(signInUseCaseImpl: SignInUseCaseImpl): SignInUseCase

    @Binds
    fun bindGetBettingTipsUseCase(getBettingTipsUseCaseImpl: GetBettingTipsUseCaseImpl):GetBettingTipsUseCase

    @Binds
    fun bindAppLaunchUseCase(appLaunchUseCaseImpl: AppLaunchUseCaseImpl): AppLaunchUseCase

    @Binds
    fun sendNotificationUseCase(sendNotificationUseCaseImpl: SendNotificationUseCaseImpl):SendNotificationUseCase

    @Binds
    fun bindChangeThemeUseCase(changeThemeUseCaseImpl: ChangeThemeUseCaseImpl): ChangeThemeUseCase

    @Binds
    fun bindGetTicketsUseCase(getTicketsUseCase: GetTicketsUseCaseImpl):GetTicketsUseCase

    @Binds
    fun bindsInsertTicketUseCase(insertTicketUseCaseImpl: InsertTicketUseCaseImpl):InsertTicketUseCase
    @Binds
    fun bindsUpdateTicketUseCase(updateTicketUseCaseImpl: UpdateTicketUseCaseImpl):UpdateTicketUseCase
    @Binds
    fun bindDeleteTicketUseCase(deleteTicketUseCaseImpl: DeleteTicketUseCaseImpl):DeleteTicketUseCase
}