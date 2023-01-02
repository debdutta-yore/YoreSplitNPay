package co.yore.splitnpay

import io.grpc.ManagedChannel
import io.grpc.ManagedChannelBuilder
import splitpay.CategoryServiceGrpc
import splitpay.ExpenseServiceGrpc
import splitpay.GroupServiceGrpc
import splitpay.Splitpay
import splitpay.UserServiceGrpc

class GrpcServer{
    enum class ShareType{
        Equal,
        Unequal
    }
    sealed class CalculationMethod(val value: String){
        object Proportionate: CalculationMethod("proportionate")
        object OptimalAutomatic: CalculationMethod("optimal.automatic")
        object OptimalDesirable: CalculationMethod("optimal.desirable")
    }
    data class ExpenseMember(
        val phone: String,
        val name: String = "",
        val image: String = "",
        val initialPaidAmount: Double = 0.0,
        val shareAmount: Double = 0.0,
        val toPay: List<ExpenseMemberToPay> = emptyList(),
    )
    data class ExpenseMemberToPay(
        val toUser: String,
        val payable: Double
    )

    object CategoryService{
        suspend fun listCategories(
            accountId: String,
            offset: Long,
            pageSize: Long
        ): Splitpay.ListCategoryResponse{
            ensureSeverRunning()
            val request = Splitpay.ListCategoryRequest.newBuilder()
                .setAccountId(accountId)
                .setOffset(offset)
                .setPageSize(pageSize)
                .build()
            val blockingStub = CategoryServiceGrpc.newBlockingStub(channel)
            return blockingStub.listCategory(request)
        }

        suspend fun createCategory(
            accountId: String,
            name: String,
            isEnabled: Boolean
        ): Splitpay.CreateCategoryResponse{
            ensureSeverRunning()
            val request = Splitpay.CreateCategoryRequest.newBuilder()
                .setPayload(
                    Splitpay.Category.newBuilder()
                        .setAccoundId(accountId)
                        .setName(name)
                        .setIsEnabled(isEnabled)
                )
                .build()
            val blockingStub = CategoryServiceGrpc.newBlockingStub(channel)
            return blockingStub.createCategory(request)
        }

        suspend fun updateCategory(
            id: String,
            accountId: String,
            name: String,
            isEnabled: Boolean,
        ): Splitpay.OperationCodeResponse{
            ensureSeverRunning()
            val request = Splitpay
                .UpdateCategoryRequest
                .newBuilder()
                .setPayload(
                    Splitpay
                        .Category
                        .newBuilder()
                        .setId(id)
                        .setAccoundId(accountId)
                        .setName(name)
                        .setIsEnabled(isEnabled)
                )
                .build()
            val blockingStub = CategoryServiceGrpc.newBlockingStub(channel)
            return blockingStub.updateCategory(request)
        }

        suspend fun deleteCategory(
            id: String
        ): Splitpay.OperationCodeResponse{
            ensureSeverRunning()
            val request = Splitpay
                .DeleteCategoryRequest
                .newBuilder()
                .setId(id)
                .build()
            val blockingStub = CategoryServiceGrpc.newBlockingStub(channel)
            return blockingStub.deleteCategory(request)
        }
    }
    object ExpenseService{
        suspend fun createExpense(
            accountId: String,
            categoryId: String,
            shareType: ShareType,
            amount: Double,
            description: String,
            receiptUrl: String,
            calculationMethod: CalculationMethod,
            expenseMembers: List<ExpenseMember>,
            groupId: String,
            groupName: String,
            groupImageUrl: String
        ): Splitpay.CreateExpenseResponse{
            ensureSeverRunning()
            val request = Splitpay.CreateExpenseRequest.newBuilder()
                .setPayload(
                    Splitpay
                        .Expense
                        .newBuilder()
                        .setCid(categoryId)
                        .setType(shareType.name.lowercase())
                        .setAccountId(accountId)
                        .setAmount(amount)
                        .setDescription(description)
                        .setReceiptUrl(receiptUrl)
                        .setCalculationMethod(calculationMethod.value)
                        .addAllMembers(expenseMembers.splitPayUsers)
                )
                .setGroup(
                    Splitpay
                        .Group
                        .newBuilder()
                        .setId(groupId)
                        .setName(groupName)
                        .setImageUrl(groupImageUrl)
                        .build()
                )
                .build()
            val blockingStub = ExpenseServiceGrpc.newBlockingStub(channel)
            return blockingStub.createExpense(request)
        }

        suspend fun listExpenses(
            accountId: String,
            offset: Long,
            pageSize: Long,
            groupId: String,
            uid: String
        ): Splitpay.ListExpenseResponse{
            ensureSeverRunning()
            val request = Splitpay.ListExpenseRequest.newBuilder()
                .setAccountId(accountId)
                .setGid(groupId)
                .setUid(uid)
                .setOffset(offset)
                .setPageSize(pageSize)
                .build()
            val blockingStub = ExpenseServiceGrpc.newBlockingStub(channel)
            return blockingStub.listExpense(request)
        }

        suspend fun splitDetails(
            accountId: String,
            expenseId: String
        ): Splitpay.ExpenseDetailResponse{
            ensureSeverRunning()
            val request = Splitpay.ExpenseDetailRequest.newBuilder()
                .setAccountId(accountId)
                .setEid(expenseId)
                .build()
            val blockingStub = ExpenseServiceGrpc.newBlockingStub(channel)
            return blockingStub.expenseDetail(request)
        }

        suspend fun getPayAmountsForGroupExpense(
            accountId: String,
            groupId: String,
            uid: String,
        ):Splitpay.ListUserResponse {
            ensureSeverRunning()
            val request = Splitpay.GroupDetailRequest.newBuilder()
                .setAccountId(accountId)
                .setGid(groupId)
                .setUid(uid)
                .build()
            val blockingStub = ExpenseServiceGrpc.newBlockingStub(channel)
            return blockingStub.getPayAmountsForGroupExpense(request)
        }

        suspend fun getSettlementPreview(
            calculationMethod: CalculationMethod,
            members: List<ExpenseMember>
        ): Splitpay.GetSettlementPreviewResponse{
            ensureSeverRunning()
            val request = Splitpay.GetSettlementPreviewRequest.newBuilder()
                .addAllUsers(
                    members.splitPayUsers
                )
                .setMethod(calculationMethod.value)
                .build()
            val blockingStub = ExpenseServiceGrpc.newBlockingStub(channel)
            return blockingStub.getSettlementPreview(request)
        }
    }
    object GroupService{
        suspend fun createGroup(
            accountId: String,
            groupName: String,
            groupImageUrl: String,
            members: List<ExpenseMember>
        ): Splitpay.CreateGroupResponse{
            ensureSeverRunning()
            val request = Splitpay.CreateGroupRequest.newBuilder()
                .setPayload(
                    Splitpay.Group.newBuilder()
                        .setName(groupName)
                        .setImageUrl(groupImageUrl)
                        .addAllMembers(members.splitPayUsers)
                        .setAccountId(accountId)
                        .build()
                )
                .build()
            val blockingStub = GroupServiceGrpc.newBlockingStub(channel)
            return blockingStub.createGroup(request)
        }

        suspend fun updateGroup(
            groupId: String,
            accountId: String,
            groupName: String,
            groupImageUrl: String
        ): Splitpay.OperationCodeResponse{
            ensureSeverRunning()
            val request = Splitpay.UpdateGroupRequest.newBuilder()
                .setPayload(
                    Splitpay.Group.newBuilder()
                        .setId(groupId)
                        .setName(groupName)
                        .setImageUrl(groupImageUrl)
                        .setAccountId(accountId)
                        .build()
                )
                .build()
            val blockingStub = GroupServiceGrpc.newBlockingStub(channel)
            return blockingStub.updateGroup(request)
        }

        suspend fun listGroup(
            accountId: String,
            createdByMe: Boolean,
            createdByOthers: Boolean,
            uid: String,
            offset: Long,
            pageSize: Long
        ): Splitpay.ListGroupResponse{
            ensureSeverRunning()
            val request = Splitpay.ListGroupRequest.newBuilder()
                .setAccountId(accountId)
                .setCreatedByMe(createdByMe)
                .setCreatedByOthers(createdByOthers)
                .setUid(uid)
                .setOffset(offset)
                .setPageSize(pageSize)
                .build()
            val blockingStub = GroupServiceGrpc.newBlockingStub(channel)
            return blockingStub.listGroup(request)
        }
    }
    object UserService{
        suspend fun userData(
            accountId: String,
            needSplitTotal: Boolean
        ): Splitpay.UserDataResponse{
            ensureSeverRunning()
            val request = Splitpay.UserDataRequest.newBuilder()
                .setAccountId(accountId)
                .setNeedSplitTotal(needSplitTotal)
                .build()
            val blockingStub = UserServiceGrpc.newBlockingStub(channel)
            return blockingStub.userData(request)
        }
    }
    companion object{
        private var channel: ManagedChannel? = null
        private val ip = "10.0.2.2"
        private val port = 9090
        private var status = false

        fun start() {
            try {
                channel = ManagedChannelBuilder.forAddress(ip, port).usePlaintext().build()
                status = true
            } catch (e: Exception) {
                status = false
            }
        }

        fun stop() {
            channel?.shutdown()
            status = false
        }

        private fun ensureSeverRunning() {
            if(!status){
                start()
            }
            if(!status){
                throw GrpcServerNotRunningException()
            }
        }
    }
}

class GrpcServerNotRunningException: Exception("Grpc server not running")

val GrpcServer.ExpenseMemberToPay.paybleFromToAmounts get() = Splitpay
    .PaybleFromToAmounts
    .newBuilder()
    .setTo(toUser)
    .setPayble(payable)
    .build()
val List<GrpcServer.ExpenseMemberToPay>.payableFromToAmountsList get() = map{
    it.paybleFromToAmounts
}
val GrpcServer.ExpenseMember.toSplitPayUser get() = Splitpay.User.newBuilder()
    .setPhoneNumber(phone)
    .setImageUrl(image)
    .setFullName(name)
    .setInitialPaidAmount(initialPaidAmount)
    .setShareAmount(shareAmount)
    .addAllToPay(toPay.payableFromToAmountsList)
    .build()
val List<GrpcServer.ExpenseMember>.splitPayUsers get() = map {
    it.toSplitPayUser
}