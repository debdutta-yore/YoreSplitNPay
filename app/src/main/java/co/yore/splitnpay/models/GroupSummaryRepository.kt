package co.yore.splitnpay.models


interface GroupSummaryRepository {
    suspend fun getMembers(): List<SplitSelectableMember>
    suspend fun getWillPayTransactions(): List<MemberTransact>
    suspend fun getWillGetTransactions(): List<MemberTransact>
}