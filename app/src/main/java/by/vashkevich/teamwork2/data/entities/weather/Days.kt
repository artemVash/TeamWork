package by.vashkevich.teamwork2.data.entities.weather

import android.os.Parcelable


data class Days(
    val daily: List<Daily>,
    val lat: Double, //широта  (вводятся в запросе как параметры в методе)
    val lon: Double, //долгота
    val timezone: String, //часовой появ пока не нужен для этой домашки
) {
    data class Daily(
        val dt: Int, //дата через форматтер дат перевести
        val temp: Temp,
        val weather: List<Weather>,
    ) {

        data class Temp(
            val dayTemp: Double,
            val nightTemp: Double
        )

        data class Weather(
            val icon: String,  //через picasso картинку грузить
            val id: Int,
        )
    }
}
