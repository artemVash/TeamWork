package by.vashkevich.teamwork2.networking.weather

import by.vashkevich.teamwork2.data.dto.weather.DaysResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query


interface WeatherService {

    @GET("data/2.5/onecall?exclude=minutely,hourly,current&appid=066684c224288ec83f079c8017eb1057&units=metric")
    suspend fun loadWeather(
        @Query("lat")
        lat: Double,
        @Query("lon")
        lon: Double
    ): Response<DaysResponse>
}