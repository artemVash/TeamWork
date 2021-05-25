package by.vashkevich.teamwork2.repositories.weather

import by.vashkevich.teamwork2.data.entities.weather.Days
import by.vashkevich.teamwork2.mappers.WeatherMapper
import by.vashkevich.teamwork2.networking.weather.WeatherServiceProvider


class WeatherRepository {

    private val weatherMapper = WeatherMapper()
    private val weatherService = WeatherServiceProvider.providerWeatherService()

    //for creating or getting repository
    //singleton
    companion object {
        private var INSTANCE: WeatherRepository? = null

        fun getRepository(): WeatherRepository {
            return if (INSTANCE == null) {
                INSTANCE = WeatherRepository()
                INSTANCE as WeatherRepository
            } else {
                INSTANCE as WeatherRepository
            }
        }
    }

    suspend fun loadWeather(lat: Double, lon: Double): Days {
        val response = weatherService.loadWeather(lat, lon)
        return if (response.isSuccessful) {
            weatherMapper.map(response.body())
        } else {
            throw Throwable(response.errorBody().toString())
        }
    }
}