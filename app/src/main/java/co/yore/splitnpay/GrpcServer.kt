package co.yore.splitnpay

import android.graphics.Bitmap
import com.google.protobuf.ByteString
import io.grpc.ManagedChannel
import io.grpc.ManagedChannelBuilder
import splitpay.*
import yore_file_upload.YoreFileUploadGrpc
import java.io.ByteArrayOutputStream
import java.util.UUID
import kotlin.random.Random


class GrpcServer{
    enum class ShareType{
        Equal,
        Unequal;

        companion object{
            fun fromString(value: String): ShareType{
                return when(value.lowercase()){
                    "equal"-> Equal
                    "unequal"-> Unequal
                    else-> throw IllegalArgumentException("Unknown value passed for ShareType. Allowed values are ${values().joinToString(", ")}")
                }
            }
        }

    }
    sealed class CalculationMethod(val value: String){
        object Proportionate: CalculationMethod("proportionate")
        object OptimalAutomatic: CalculationMethod("optimal.automatic")
        object OptimalDesirable: CalculationMethod("optimal.desirable")

        companion object{
            fun fromString(value: String): CalculationMethod{
                return when(value.lowercase()){
                    "proportionate"-> Proportionate
                    "optimal.automatic"->OptimalAutomatic
                    "optimal.desirable"->OptimalDesirable
                    else-> throw IllegalArgumentException("Unknown value passed for ShareType. Allowed values are ${ShareType.values().joinToString(", ")}")
                }
            }
        }
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

        /*{
            "gid": "6672cd76-dcb6-4dba-98cf-de545d26b1f3",
            "uid": "",
            "accountId": "8967114927",
            "needGetPay": true
        }*/

        suspend fun groupDetails(
            accountId: String,
            uid: String,
            gid: String,
            needGetPay: Boolean
        ): Splitpay.GroupDetailResponse{
            ensureSeverRunning()
            val request = Splitpay.GroupDetailRequest.newBuilder()
                .setAccountId(accountId)
                .setUid(uid)
                .setGid(gid)
                .setNeedGetPay(needGetPay)
                .build()
            val blockingStub = GroupServiceGrpc.newBlockingStub(channel)
            return blockingStub.groupDetail(request)
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

        suspend fun listUser(
            accountId: String,
            createdByMe: Boolean,
            createdByOthers: Boolean,
            onlyUnsettled: Boolean,
            offset: Long,
            pageSize: Long
        ): Splitpay.ListUserResponse{
            ensureSeverRunning()
            val request = Splitpay
                .ListUserRequest
                .newBuilder()
                .setAccountId(accountId)
                .setCreatedByMe(createdByMe)
                .setCreatedByOthers(createdByOthers)
                .setOnlyUnsettled(onlyUnsettled)
                .setOffset(offset)
                .setPageSize(pageSize)
                .build()
            val blockingStub = UserServiceGrpc.newBlockingStub(channel)
            return blockingStub.listUser(request)
        }
    }
    object FileService{
        suspend fun upload(
            bitmap: Bitmap
        ): yore_file_upload.File.UploadFileResponse{

            val out = ByteArrayOutputStream()
            if (bitmap.compress(Bitmap.CompressFormat.JPEG, 20, out)) {
                out.flush()
                out.close()
            }
            val byteArray = out.toByteArray()
            val uuid = UUID.nameUUIDFromBytes(byteArray)
            val timestamp = System.currentTimeMillis()
            val rand = Random(System.nanoTime()).nextInt(1000,10000)
            val name = "${uuid}_${timestamp}_${rand}.jpeg"
            ensureSeverRunning()
            val request = yore_file_upload.File.UploadFileRequest.newBuilder()
                .setObjectName(name)
                .setFileContents(ByteString.copyFrom(byteArray))
                .build()
            val blockingStub = YoreFileUploadGrpc.newBlockingStub(fileChannel)
            return blockingStub.uploadFile(request)
        }
    }
    companion object{
        private var channel: ManagedChannel? = null
        private var fileChannel: ManagedChannel? = null
        private val ip = "192.168.0.102"
        private val port = 9090
        private val file_port = 9091
        private var status = false
        private var file_status = false

        fun start() {
            try {
                channel = ManagedChannelBuilder.forAddress(ip, port).usePlaintext().build()
                status = true
            } catch (e: Exception) {
                status = false
            }
        }

        fun startFile() {
            try {
                fileChannel = ManagedChannelBuilder.forAddress(ip, file_port).usePlaintext().build()
                file_status = true
            } catch (e: Exception) {
                file_status = false
            }
        }

        fun stop() {
            channel?.shutdown()
            status = false
        }

        fun stopFile() {
            fileChannel?.shutdown()
            file_status = false
        }

        private fun ensureSeverRunning() {
            if(!status){
                start()
                startFile()
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