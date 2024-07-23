package data.remote.api

import domain.CurrencyApiService
import domain.PreferencesRepository
import domain.model.ApiResponse
import domain.model.Currency
import domain.model.CurrencyCode
import domain.model.RequestState
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.plugins.DefaultRequest
import io.ktor.client.plugins.HttpTimeout
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.request.get
import io.ktor.client.request.headers
import io.ktor.client.utils.EmptyContent.headers
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json

class CurrencyApiServiceImpl(private val preferencesRepository: PreferencesRepository) :
    CurrencyApiService {

    companion object {
        const val ENDPOINT = "https://api.currencyapi.com/v3/latest"
        const val API_KEY = "cur_live_fWeQLuUPvfGZf4jlFEUlmee5TKFCOdgN4QqDFZJy"
    }

    private val httpClient = HttpClient {
        install(ContentNegotiation) {
            json(Json {
                prettyPrint = true
                isLenient = true
                ignoreUnknownKeys = true
            })
        }
        install(HttpTimeout) {
            requestTimeoutMillis = 1000 * 15
        }
        install(DefaultRequest) {
            headers {
                append("apikey", API_KEY)
            }
        }
    }


    override suspend fun getLatestExchangeRates(): RequestState<List<Currency>> {
        return try {
            val response = httpClient.get(ENDPOINT)
            if (response.status.value == 200) {
                println("API Response: ${response.body<String>()}")
                val apiResponse = Json.decodeFromString<ApiResponse>(response.body())

                val avaliableCurrencyCodes = apiResponse.data.keys
                    .filter {
                        CurrencyCode.entries.map { code -> code.name }.toSet().contains(it)
                    }
                val avaliableCurrency = apiResponse.data.values.filter { currency ->
                    avaliableCurrencyCodes.contains(currency.code)

                }

                // persistindo os dadods
                val lastUpdated = apiResponse.meta.lastUpdatedAt
                preferencesRepository.saveLastUpdated(lastUpdated)


                RequestState.Success(avaliableCurrency)
            } else {
                RequestState.Error("Failed to fetch data${response.status}")
            }

        } catch (e: Exception) {
            RequestState.Error(message = e.message.toString())

        }
    }

}