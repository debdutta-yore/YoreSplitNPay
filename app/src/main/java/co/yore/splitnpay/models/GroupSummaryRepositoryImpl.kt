package co.yore.splitnpay.models

class GroupSummaryRepositoryImpl : GroupSummaryRepository {

    val members = listOf(
        SplitSelectableMember(name = "You", "https://i.pravatar.cc/300?", true),
        SplitSelectableMember(name = "Sushil", "https://i.pravatar.cc/300?", false)
    )

    private val getList = listOf(
        MemberTransact(
            name = "You",
            image = "https://i.pravatar.cc/300?",
            mobile = "9563376942",
            amount = 30000f
        ),
        MemberTransact(
            name = "Sushil Roy",
            image = "https://i.pravatar.cc/300?",
            mobile = "9563376942",
            amount = 1000f
        ),
        MemberTransact(
            name = "Manisha Roy",
            image = "https://i.pravatar.cc/300?",
            mobile = "9563376942",
            amount = 500f
        ),
        MemberTransact(
            name = "Sanjanaa Ray",
            image = "https://i.pravatar.cc/300?",
            mobile = "9563376942",
            amount = 0f
        )
    )
    private val paidList = emptyList<MemberTransact>()

    override suspend fun getMembers(): List<SplitSelectableMember> {
        return members
    }

    override suspend fun getWillGetTransactions(): List<MemberTransact> {
        return getList
    }

    override suspend fun getWillPayTransactions(): List<MemberTransact> {
        return paidList
    }

}
