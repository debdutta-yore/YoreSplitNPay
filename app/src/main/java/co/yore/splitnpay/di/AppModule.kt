package co.yore.splitnpay.di

import android.content.Context
import co.yore.splitnpay.AccountService
import co.yore.splitnpay.ApiService
import co.yore.splitnpay.libs.kontakts.repo.ContactRepo
import co.yore.splitnpay.repo.MasterRepo
import co.yore.splitnpay.repo.MasterRepoImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.qualifiers.ActivityContext
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.android.scopes.ViewModelScoped
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

/*@Module
@InstallIn(SingletonComponent::class)
object AppModule {
}*/

@Module
@InstallIn(ViewModelComponent::class)
object DetailModule {

    @Provides
    @ViewModelScoped
    fun provideRepository(
        contactRepo: ContactRepo,
        apiService: ApiService,
        accountService: AccountService
    ): MasterRepo {
        return MasterRepoImpl(
            contactRepo,
            apiService,
            accountService
        )
    }

    @Provides
    @ViewModelScoped
    fun provideDetailRouter(@ApplicationContext context: Context): ContactRepo {
        return ContactRepo(context)
    }

    @Provides
    @ViewModelScoped
    fun provideAccountService(@ApplicationContext context: Context): AccountService {
        return AccountService(context)
    }

    @Provides
    @ViewModelScoped
    fun provideApiService(accountService: AccountService): ApiService {
        return ApiService(accountService)
    }
}