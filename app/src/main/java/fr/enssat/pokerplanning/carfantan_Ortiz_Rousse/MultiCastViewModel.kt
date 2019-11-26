package fr.enssat.pokerplanning.carfantan_Ortiz_Rousse

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class MultiCastViewModel(context: Context): ViewModel() {

    private val _roomSet = mutableSetOf<String>()
    private val _allrooms = MutableLiveData<List<String>>()
    private val _multicast = MultiCastAgent(this::onReceiveRoom)

    val rooms: LiveData<List<String>> get() = _allrooms

    init {
        MultiCastAgent.wifiLock(context)
//        _multicast.startReceiveLoop()
    }

    override fun onCleared() {
        _multicast.stopReceiveLoop()
        MultiCastAgent.releaseWifiLock()
        super.onCleared()
    }

    fun onReceiveRoom(msg: String) {
        _roomSet.add(msg)
        _allrooms.postValue(_roomSet.toList())
    }

    fun send(msg: String) {
        _multicast.send(msg)
    }
}