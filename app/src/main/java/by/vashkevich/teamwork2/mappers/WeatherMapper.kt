package by.vashkevich.teamwork2.mappers

import by.vashkevich.teamwork2.data.dto.weather.DaysResponse
import by.vashkevich.teamwork2.data.entities.weather.Days


class WeatherMapper : Mapper<DaysResponse, Days> {


    override fun map(from: DaysResponse): Days {
        return Days(
            daily = from.daily.map { mapInDaily(it) },
            lat = from.lat,
            lon = from.lon,
            timezone = from.timezone
        )
    }

    private fun mapInDaily(from: DaysResponse.Daily): Days.Daily {
        return Days.Daily(
            dt = from.dt,
            temp = mapInTemp(from.temp),
            weather = from.weather.map { mapWeather(it) }
        )
    }

    private fun mapInTemp(from: DaysResponse.Daily.Temp): Days.Daily.Temp {
        return Days.Daily.Temp(
            dayTemp = from.dayTemp,
            nightTemp = from.nightTemp
        )
    }

    private fun mapWeather(from: DaysResponse.Daily.Weather): Days.Daily.Weather {
        return Days.Daily.Weather(
            icon = from.icon,
            id = from.id
        )


    }
}