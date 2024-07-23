package presentation.screen

import androidx.compose.foundation.interaction.PressInteraction
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import cafe.adriel.voyager.core.model.ScreenModel
import cafe.adriel.voyager.core.model.screenModelScope
import domain.CurrencyApiService
import domain.PreferencesRepository
import domain.model.RateStatus
import kotlinx.coroutines.launch
import kotlinx.datetime.Clock

sealed class HomeUiEvent {
    data object RefreshRates : HomeUiEvent()
}

class HomeViewModel(
    private val preferencesRepository: PreferencesRepository,
    private val apiService: CurrencyApiService
) : ScreenModel {
    private var _rateStatus: MutableState<RateStatus> = mutableStateOf(RateStatus.Idle)
    val rateStatus: State<RateStatus> = _rateStatus

    init {
        screenModelScope.launch {
            fetchNewsRates()

        }
    }
    fun sendEvent(event: HomeUiEvent) {
        when (event) {
            HomeUiEvent.RefreshRates -> {
                screenModelScope.launch {
                    fetchNewsRates()

                }
            }
        }
    }
    private suspend fun fetchNewsRates() {
        try {
            apiService.getLatestExchangeRates()
            getRateStatus()
        } catch (e: Exception) {
            println(e.toString())
        }
    }
    private suspend fun getRateStatus(){
        _rateStatus.value = if (preferencesRepository.isDataFresh(Clock.System.now().toEpochMilliseconds()))
            RateStatus.Fresh
        else
            RateStatus.Stale
    }
}