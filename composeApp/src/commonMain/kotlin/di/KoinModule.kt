package di

import com.russhwolf.settings.Settings
import data.local.PreferencesImpl
import data.remote.api.CurrencyApiServiceImpl
import domain.CurrencyApiService
import domain.PreferencesRepository
import org.koin.core.context.startKoin
import org.koin.dsl.module
import presentation.screen.HomeViewModel


val AppModule = module {
    single { Settings() }
    single<PreferencesRepository> { PreferencesImpl(settings = get()) }
    single <CurrencyApiService>{CurrencyApiServiceImpl(preferencesRepository = get())  }
    factory {
        HomeViewModel(
            preferencesRepository = get(),
            apiService = get()
        )


    }
}
fun initializeKoin(){
    startKoin {
        modules(AppModule)
    }

}