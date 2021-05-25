package by.vashkevich.teamwork2.data.dto.weather


import com.google.gson.annotations.SerializedName

data class DaysResponse(
    @SerializedName("daily")
    val daily: List<Daily>,
    @SerializedName("lat")
    val lat: Double,
    @SerializedName("lon")
    val lon: Double,
    @SerializedName("timezone")
    val timezone: String,
) {
    data class Daily(
        @SerializedName("dt")
        val dt: Int,
        @SerializedName("temp")
        val temp: Temp,
        @SerializedName("weather")
        val weather: List<Weather>,
    ) {

        data class Temp(
            @SerializedName("day")
            val dayTemp: Double,
            @SerializedName("night")
            val nightTemp: Double
        )

        data class Weather(
            @SerializedName("icon")
            val icon: String,
            @SerializedName("id")
            val id: Int,
        )
    }
}