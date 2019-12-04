package fr.enssat.pokerplanning.carfantan_Ortiz_Rousse

import android.app.Activity
import android.content.Intent
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class ServerViewModel(private val activity: Activity) : ViewModel() {

    private val _server = ServerSocket(activity, this::onReceiveVote)

    private val _voteSet = mutableSetOf<String>()
    private val _allVotes = MutableLiveData<List<String>>()
    lateinit var _results: VotesMessage

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
            _server.stopVote(Message.toJson(SimpleMessage("Nobody voted.")), 3)
            val intent = Intent(activity, RoomActivity::class.java)
            activity.startActivity(intent)
        } else {
            val jsonVotesMessage = Message.toJson(_results)
            _server.stopVote(jsonVotesMessage, 2)
            val intent = Intent(activity, ResultActivity::class.java)
            intent.putExtra("results", jsonVotesMessage)
            activity.startActivity(intent)
        }
    }

    fun onReceiveVote(msg: String) {
        val session = SessionManager(activity)
        val user = session.userDetails
        val msgRoom = Message.fromJson(user["room"].toString())
        val room = msgRoom as RoomMessage

        val message = Message.fromJson(msg)
        val votesMessage = message as VotesMessage

        if (!::_results.isInitialized) {
            _results = votesMessage
        } else {
            _results.liste += votesMessage.liste
        }

        if (votesMessage.roomId == room.id) {
            _voteSet.add(votesMessage.liste[0].name + " voted.")
            _allVotes.postValue(_voteSet.toList())
        }
    }
}