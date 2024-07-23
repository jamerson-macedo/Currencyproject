package presentation.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.koin.getScreenModel
import presentation.components.HomeHeader
import surfaceColor

class HomeScreen :Screen {
    @Composable
    override fun Content() {
        val viewmodel = getScreenModel<HomeViewModel>()
        val rateStatus by viewmodel.rateStatus

  Column(modifier = Modifier.fillMaxSize().background(surfaceColor)) {
      HomeHeader(status =rateStatus , onRateRefresh = {
          viewmodel.sendEvent(HomeUiEvent.RefreshRates)
      })


  }
}
}