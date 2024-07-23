package data.local

import com.russhwolf.settings.ExperimentalSettingsApi
import com.russhwolf.settings.ObservableSettings
import com.russhwolf.settings.Settings
import com.russhwolf.settings.coroutines.FlowSettings
import com.russhwolf.settings.coroutines.toFlowSettings
import domain.PreferencesRepository
import kotlinx.datetime.Instant
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime

@OptIn(ExperimentalSettingsApi::class)
class PreferencesImpl(private val settings: Settings) : PreferencesRepository {
    companion object {
        const val TIMESTAMP_KEY = "last_updated"

    }

    private val flowSettings: FlowSettings = (settings as ObservableSettings).toFlowSettings()


    override suspend fun saveLastUpdated(lastUpdated: String) {
        flowSettings.putLong(
            TIMESTAMP_KEY,
            Instant.parse(lastUpdated).toEpochMilliseconds()
        )
    }

    override suspend fun isDataFresh(currentTimeStamp: Long): Boolean {
        val savedtimestamp = flowSettings.getLong(TIMESTAMP_KEY, 0L)
        return if (savedtimestamp != 0L) {
            val currentInstant = Instant.fromEpochMilliseconds(currentTimeStamp)
            val savedInstant = Instant.fromEpochMilliseconds(savedtimestamp)
            val currentDateTime = currentInstant.toLocalDateTime(TimeZone.currentSystemDefault())
            val savedDateTime = savedInstant.toLocalDateTime(TimeZone.currentSystemDefault())
            val daysDifference = currentDateTime.date.dayOfYear - savedDateTime.date.dayOfYear
            daysDifference < 1
        }else false
    }
}