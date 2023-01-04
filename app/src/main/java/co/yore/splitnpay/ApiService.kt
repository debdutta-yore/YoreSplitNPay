package co.yore.splitnpay

import android.content.Context
import co.yore.splitnpay.libs.root
import co.yore.splitnpay.models.Category
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
}