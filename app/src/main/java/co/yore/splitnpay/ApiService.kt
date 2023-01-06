package co.yore.splitnpay

import android.content.Context
import android.graphics.Bitmap
import android.util.Log
import co.yore.splitnpay.libs.root
import co.yore.splitnpay.models.Category
import co.yore.splitnpay.models.ContactData
import co.yore.splitnpay.models.GroupData
import co.yore.splitnpay.models.MemberPayment
import io.grpc.StatusRuntimeException
import splitpay.Splitpay
import javax.inject.Inject

class ApiService @Inject constructor(
    val accountService: AccountService
) {
    suspend fun users(

    ): List<Splitpay.User>{
        return GrpcServer
            .UserService
            .listUser(
                accountId = accountService.getAccountId(),
                createdByMe = true,
                createdByOthers = true,
                onlyUnsettled = true,
                offset = 0,
                pageSize = 100
            ).resultsList
    }

    suspend fun userData(): Splitpay.UserDataResponse?{
        try {
            return GrpcServer.UserService.userData(accountService.getAccountId(),needSplitTotal = true)
        }
        catch (ex: Exception){
            val cause = ex.root
            if(cause is StatusRuntimeException){
                return null
            }
        }
        return null
    }

    suspend fun groups(): Splitpay.ListGroupResponse{
        return GrpcServer
            .GroupService
            .listGroup(
                accountService.getAccountId(),
                createdByMe = true,
                createdByOthers = true,
                "",
                offset = 0,
                pageSize = 100
            )
    }

    suspend fun categories(): Splitpay.ListCategoryResponse{
        return GrpcServer
            .CategoryService
            .listCategories(
                accountService.getAccountId(),
                offset = 0,
                pageSize = 100
            )
    }

    suspend fun createCategory(name: String) {
        GrpcServer.CategoryService.createCategory(
            accountService.getAccountId(),
            name,
            true
        )
    }

    suspend fun renameCategory(category: Category, name: String) {
        GrpcServer
            .CategoryService
            .updateCategory(
                id = category.id.toString(),
                accountId = accountService.getAccountId(),
                name = name,
                isEnabled = true
            )
    }

    suspend fun createGroup(members: List<ContactData>, groupName: String, image: Bitmap?): String? {
        if(image!=null){
            val url = GrpcServer.FileService.upload(image).fileUrl
            val imageUploadedMembers = members.map {
                it.copy(
                    image = (if(it.image is Bitmap) GrpcServer.FileService.upload(it.image as Bitmap).fileUrl else "")
                )
            }
            return GrpcServer.GroupService.createGroup(
                accountService.getAccountId(),
                groupName,
                url,
                imageUploadedMembers.map {
                    GrpcServer.ExpenseMember(
                        phone = it.mobile,
                        name = it.name,
                        image = (it.image as? String)?:"",
                    )
                }
            ).id
        }
        return null
    }

    suspend fun createExpense(
        categoryId: String,
        shareType: String,
        amount: Double,
        description: String,
        receipt: Any?,
        calculationMethod: String,
        members: List<MemberPayment>,
        groupId: String,
        groupName: String,
        groupImage: Any?
    ): Pair<String,String> {
        val groupImageUrl = when (groupImage) {
            is Bitmap -> GrpcServer.FileService.upload(groupImage).fileUrl
            is String -> groupImage
            else -> ""
        }

        val receiptUrl = when (receipt) {
            is Bitmap -> GrpcServer.FileService.upload(receipt).fileUrl
            is String -> receipt
            else -> ""
        }

        val response = GrpcServer
            .ExpenseService
            .createExpense(
                accountId = accountService.getAccountId(),
                categoryId = categoryId,
                shareType = GrpcServer.ShareType.fromString(shareType),
                amount = amount,
                description = description,
                receiptUrl = receiptUrl,
                calculationMethod = GrpcServer.CalculationMethod.fromString(calculationMethod),
                expenseMembers = members.map {
                    GrpcServer.ExpenseMember(
                        phone = it.mobile,
                        name = it.name,
                        image = when (it.image) {
                            is Bitmap -> GrpcServer.FileService.upload(it.image).fileUrl
                            is String -> it.image
                            else -> ""
                        },
                        initialPaidAmount = it.paid,
                        shareAmount = it.toPay
                    )
                },
                groupId = groupId,
                groupName = groupName,
                groupImageUrl = groupImageUrl
            )
        val eid = response.eid
        val gid = response.gid
        return Pair(eid, gid)
    }

    suspend fun groupDetails(groupId: String): GroupData {
        var group = GrpcServer.GroupService.groupDetails(
            accountId = accountService.getAccountId(),
            uid = "",
            gid = groupId,
            needGetPay = true
        ).result
        return GroupData(
            id = group.id,
            image = group.imageUrl,
            name = group.name,
            members = group.membersList.map {
                ContactData(
                    id = it.id,
                    image = if(it.imageUrl == null || it.imageUrl.isEmpty()) "name://${it.fullName}" else it.imageUrl,
                    name = it.fullName,
                    mobile = it.phoneNumber,
                    willPay = it.willPay,
                    willGet = it.willGet,
                    createdAt = it.createdAt.seconds*1000,
                    updatedAt = it.updatedAt.seconds*1000
                )
            },
            willGet = group.willGet,
            willPay = group.willPay,
            createdAt = group.createdAt.seconds*1000,
            updatedAt = group.updatedAt.seconds*1000
        )
    }
}