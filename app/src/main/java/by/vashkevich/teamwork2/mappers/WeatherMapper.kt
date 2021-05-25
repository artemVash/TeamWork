package by.vashkevich.teamwork2.mappers

import by.vashkevich.teamwork2.data.dto.weather.DaysResponse
import by.vashkevich.teamwork2.data.entities.weather.Days


class WeatherMapper : Mapper<DaysResponse, Days> {


    override fun map(from: DaysResponse?): Days {
        return Days(
            daily = from?.daily?.map { mapInDaily(it) }.orEmpty(),
            lat = from?.lat ?: 0.0,
            lon = from?.lon ?: 0.0,
            timezone = from?.timezone.orEmpty()
        )
    }

    private fun mapInDaily(from: DaysResponse.Daily?): Days.Daily {
        return Days.Daily(
            dt = from?.dt ?: -1,
            temp = mapInTemp(from?.temp),
            weather = from?.weather?.map { mapWeather(it) }.orEmpty()
        )
    }

    private fun mapInTemp(from: DaysResponse.Daily.Temp?): Days.Daily.Temp {
        return Days.Daily.Temp(
            dayTemp = from?.dayTemp ?: 0.0,
            nightTemp = from?.nightTemp ?: 0.0
        )
    }

    private fun mapWeather(from: DaysResponse.Daily.Weather?): Days.Daily.Weather {
        return Days.Daily.Weather(
            icon = from?.icon.orEmpty(),
            id = from?.id ?: -1
        )


    }
}