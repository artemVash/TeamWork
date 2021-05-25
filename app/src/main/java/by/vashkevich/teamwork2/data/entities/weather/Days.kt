package by.vashkevich.teamwork2.data.entities.weather


data class Days(
    val daily: List<Daily>,
    val lat: Double,
    val lon: Double,
    val timezone: String,
) {
    data class Daily(
        val dt: Int,
        val temp: Temp,
        val weather: List<Weather>,
    ) {

        data class Temp(
            val dayTemp: Double,
            val nightTemp: Double
        )

        data class Weather(
            val icon: String,
            val id: Int,
        )
    }
}
