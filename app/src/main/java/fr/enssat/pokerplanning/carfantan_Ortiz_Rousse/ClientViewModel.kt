package fr.enssat.pokerplanning.carfantan_Ortiz_Rousse

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class ClientViewModel(context: Context): ViewModel() {

    private val _client = ClientSocket(context, this::onReceiveMessage)

    private val _msgSet = mutableSetOf<String>()
    private val _allMessages = MutableLiveData<List<String>>()

    val messages: LiveData<List<String>> get() = _allMessages

    override fun onCleared() {
        _client.stop()
        super.onCleared()
    }

    fun onReceiveMessage(msg: String) {
        val message = Message.fromJson(msg)
        val msg = message as SimpleMessage

        _msgSet.add(msg.msg)
        _allMessages.postValue(_msgSet.toList())
    }

    fun connect(serverIp:String, serverPort:Int?){
        _client.connect(serverIp, serverPort)
    }

    fun send(msg:String){
        _client.sendAndReceive(msg)
    }
}