package com.victorhvs.tfc.data.di

import com.google.firebase.appcheck.AppCheckProviderFactory
import com.google.firebase.appcheck.debug.DebugAppCheckProviderFactory
import com.google.firebase.appcheck.playintegrity.PlayIntegrityAppCheckProviderFactory
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase
import com.victorhvs.tfc.BuildConfig
import com.victorhvs.tfc.core.DispatcherProvider
import com.victorhvs.tfc.core.DispatcherProviderImpl
import com.victorhvs.tfc.data.datasource.FirebaseDataSource
import com.victorhvs.tfc.data.datasource.FirebaseDataSourceImp
import com.victorhvs.tfc.data.repository.AuthRepositoryImpl
import com.victorhvs.tfc.data.repository.ProfileRepositoryImpl
import com.victorhvs.tfc.data.repository.RankingRepositoryImpl
import com.victorhvs.tfc.data.repository.StockRepositoryImpl
import com.victorhvs.tfc.domain.repository.AuthRepository
import com.victorhvs.tfc.domain.repository.ProfileRepository
import com.victorhvs.tfc.domain.repository.RankingRepository
import com.victorhvs.tfc.domain.repository.StockRepository
import com.victorhvs.tfc.domain.use_case.IsUserAuthenticated
import com.victorhvs.tfc.domain.use_case.SignInAnonymously
import com.victorhvs.tfc.domain.use_case.SignOut
import com.victorhvs.tfc.domain.use_case.UseCases
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DI {

    @Provides
    @Singleton
    fun provideAppCheckFactory(): AppCheckProviderFactory {
        return if (BuildConfig.DEBUG) {
            DebugAppCheckProviderFactory.getInstance()
        } else {
            PlayIntegrityAppCheckProviderFactory.getInstance()
        }
    }

    @Provides
    @Singleton
    fun provideFirestoreClient(): FirebaseFirestore = FirebaseFirestore.getInstance()

    @Provides
    @Singleton
    fun provideDisparcherProvider(): DispatcherProvider = DispatcherProviderImpl()

    @Provides
    @Singleton
    fun provideFirestoreDataSource(
        client: FirebaseFirestore,
        dispacher: DispatcherProvider
    ): FirebaseDataSource =
        FirebaseDataSourceImp(
            client = client,
            dispatcher = dispacher,
        )

    @Provides
    @Singleton
    fun provideStockRepository(
        client: FirebaseFirestore,
        dispacher: DispatcherProvider
    ): StockRepository = StockRepositoryImpl(
        dispatcher = dispacher,
        client = client,
    )

    @Provides
    fun provideFirebaseAuth() = Firebase.auth

    @Provides
    fun provideAuthRepository(
        auth: FirebaseAuth
    ): AuthRepository = AuthRepositoryImpl(auth)

    @Provides
    fun provideProfileRepository(
        auth: FirebaseAuth
    ): ProfileRepository = ProfileRepositoryImpl(auth)

    @Provides
    fun provideRankingRepository(
        client: FirebaseDataSource,
        dispacher: DispatcherProvider
    ): RankingRepository = RankingRepositoryImpl(
        firebaseDataSource = client,
        dispatcher = dispacher
    )

    @Provides
    fun provideUseCases(
        authRepo: AuthRepository,
        profileRepo: ProfileRepository
    ) = UseCases(
        isUserAuthenticated = IsUserAuthenticated(authRepo),
        signInAnonymously = SignInAnonymously(authRepo),
        signOut = SignOut(profileRepo)
    )
}
