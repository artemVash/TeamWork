package by.vashkevich.teamwork2

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import by.vashkevich.teamwork2.data.entities.weather.Days
import by.vashkevich.teamwork2.repositories.weather.WeatherRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


class WeatherViewModel() : ViewModel() {

    private val repository = WeatherRepository.getRepository()
    private val ioScope = CoroutineScope(Dispatchers.IO)

    private val _daysLiveData = MutableLiveData<Days>()
    val daysLiveData: LiveData<Days> = _daysLiveData

    fun load(lat: Double, lon: Double) {
        ioScope.launch {
            try {
                _daysLiveData.postValue(repository.loadWeather(lat, lon))
            } catch (e: Exception) {
                // TODO: 25.05.21
            }
        }
    }
    private val icon = _daysLiveData.value?.daily?.get(0)?.weather?.get(0)?.icon


}

