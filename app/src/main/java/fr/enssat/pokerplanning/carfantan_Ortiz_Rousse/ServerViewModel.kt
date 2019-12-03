package fr.enssat.pokerplanning.carfantan_Ortiz_Rousse

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class ServerViewModel(private val context: Context) : ViewModel() {

    private val _server = ServerSocket(context, this::onReceiveVote)

    private val _voteSet = mutableSetOf<String>()
    private val _allVotes = MutableLiveData<List<String>>()
    private lateinit var _results: VotesMessage

    val votes: LiveData<List<String>> get() = _allVotes

    init {
        _server.startListening()
    }

    override fun onCleared() {
        _server.stopListening()
        super.onCleared()
    }

    fun stopVote() {
        if (!::_results.isInitialized) {
            _server.stopVote(Message.toJson(SimpleMessage("Nobody voted.")), 1)
        } else {
            _server.stopVote(Message.toJson(_results), 2)
        }
    }

    fun onReceiveVote(msg: String) {
        val session = SessionManager(context)
        val user = session.userDetails
        val msgRoom = Message.fromJson(user["room"].toString())
        val room = msgRoom as RoomMessage

        val message = Message.fromJson(msg)
        val msg = message as VotesMessage

        if (!::_results.isInitialized) {
            _results = VotesMessage(msg.roomId, msg.liste)
        } else {
            _results.liste += msg.liste
        }

        if (msg.roomId == room.id) {
            _voteSet.add(msg.liste[0].name + " : " + msg.liste[0].note)
            _allVotes.postValue(_voteSet.toList())
        }
    }
}