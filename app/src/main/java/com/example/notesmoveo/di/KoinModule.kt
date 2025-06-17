package com.example.notesmoveo.di

import android.content.Context
import com.example.notesmoveo.domain.impl.AuthRepositoryImpl
import com.example.notesmoveo.domain.impl.NoteRepositoryImpl
import com.example.notesmoveo.domain.impl.UserRepositoryImpl
import com.example.notesmoveo.domain.repository.AuthRepository
import com.example.notesmoveo.domain.repository.NoteRepository
import com.example.notesmoveo.domain.repository.UserRepository
import com.example.notesmoveo.screen.Login.LoginViewModel
import com.example.notesmoveo.screen.Signup.SighupViewModel
import com.example.notesmoveo.screen.Home.HomeViewModel
import com.example.notesmoveo.screen.Note.NoteViewModel
import com.example.notesmoveo.utils.LocationProviderImpl
import com.example.notesmoveo.utils.LocationProvider
import org.koin.android.ext.koin.androidContext
import org.koin.core.KoinApplication
import org.koin.core.context.startKoin
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

fun sharedModule(appContext: Context) = module {
    single<AuthRepository> { AuthRepositoryImpl() }
    single<UserRepository> { UserRepositoryImpl() }
    single<NoteRepository> { NoteRepositoryImpl() }
    single<LocationProvider> { LocationProviderImpl(appContext) }
    single { com.example.notesmoveo.utils.PhotoPicker() }
    viewModelOf(::LoginViewModel)
    viewModelOf(::SighupViewModel)
    viewModelOf(::HomeViewModel)
    viewModelOf(::NoteViewModel)
}


fun initializeKoin(
    context: Context,
    config: (KoinApplication.() -> Unit)? = null
){
    startKoin {
        androidContext(context)
        config?.invoke(this)
        modules(sharedModule(context))
    }
}