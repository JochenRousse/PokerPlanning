package fr.enssat.pokerplanning.carfantan_Ortiz_Rousse

import android.app.Activity
import android.content.Intent
import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel


class ClientViewModel(private val activity: Activity) : ViewModel() {

    private val _client =
        ClientSocket(activity, this::onReceiveMessage, this::onStopVote, this::onCancelVote)

    private val _msgSet = mutableSetOf<String>()
    private val _allMessages = MutableLiveData<List<String>>()

    val messages: LiveData<List<String>> get() = _allMessages

    override fun onCleared() {
        _client.stop()
        super.onCleared()
    }

    private fun onReceiveMessage(msg: String) {
        val message = Message.fromJson(msg)
        val simpleMessage = message as SimpleMessage

        _msgSet.add(simpleMessage.msg)
        _allMessages.postValue(_msgSet.toList())
    }

    private fun onStopVote(resultsString: String) {
        val intent = Intent(activity, ResultActivity::class.java)
        intent.putExtra("results", resultsString)
        activity.startActivity(intent)
        activity.finish()
        onCleared()
    }

    private fun onCancelVote(msg: String) {
        val intent = Intent(activity, RoomActivity::class.java)
        activity.startActivity(intent)
        Toast.makeText(activity, msg, Toast.LENGTH_LONG).show()
        activity.finish()
        onCleared()
    }

    fun connect(serverIp: String, serverPort: Int?) {
        _client.connect(serverIp, serverPort)
    }

    fun send(msg: String) {
        _client.send(msg)
    }
}