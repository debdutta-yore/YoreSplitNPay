package co.yore.splitnpay.models

import co.yore.splitnpay.components.components.MemberTransact
import co.yore.splitnpay.pages.SplitSelectableMember

interface GroupSummaryRepository {
    suspend fun getMembers(): List<SplitSelectableMember>
    suspend fun getWillPayTransactions(): List<MemberTransact>
    suspend fun getWillGetTransactions(): List<MemberTransact>
}