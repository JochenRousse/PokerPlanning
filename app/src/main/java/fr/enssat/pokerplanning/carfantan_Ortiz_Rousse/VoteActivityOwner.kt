package fr.enssat.pokerplanning.carfantan_Ortiz_Rousse

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import fr.enssat.pokerplanning.carfantan_Ortiz_Rousse.databinding.ActivityVoteBinding


class VoteActivityOwner : AppCompatActivity(), VoteDialogFragment.NoticeDialogListener {
    private lateinit var binding: ActivityVoteBinding
    private lateinit var model: ServerViewModel
    private lateinit var modelClient: ClientViewModel
    lateinit var session: SessionManager
    lateinit var user: HashMap<String, String?>
    lateinit var username: String
    lateinit var room: RoomMessage

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_vote)

        session = SessionManager(applicationContext)
        user = session.userDetails
        username = user["userName"].toString()
        val message = Message.fromJson(user["room"].toString())
        room = message as RoomMessage

        setTitle("Vote: ${room.name}")

        model = ViewModelProviders.of(this, ServerViewModelFactory(this))
            .get(ServerViewModel::class.java)

        val adapter = MessageAdapter()
        binding.voteList.adapter = adapter
        model.votes.observe(this, Observer { list ->
            adapter.list = list
        })

        modelClient = ViewModelProviders.of(this, ClientViewModelFactory(this))
            .get(ClientViewModel::class.java)

        modelClient.connect(room.ip, ServerSocket.PORT)

        binding.sendVoteFab.setOnClickListener {
            showNoticeDialog()
        }

        binding.stopVoteButton.setOnClickListener {
            stopVote()
        }
    }

    private fun stopVote() {
        model.stopVote()
        viewModelStore.clear()
        finish()
    }

    private fun showNoticeDialog() {
        // Create an instance of the dialog fragment and show it
        val dialog = VoteDialogFragment()
        dialog.show(supportFragmentManager, "VoteDialogFragment")
    }

    override fun onDialogPositiveClick(dialog: DialogFragment, vote: String) {
        if (vote.trim().isNotEmpty()) {
            val liste = listOf(
                Votant(username, vote.toInt())
            )
            val votes = VotesMessage(room.id, liste)
            val json = Message.toJson(votes)

            model.onReceiveVote(json)
            binding.sendVoteFab.hide()
        } else {
            Toast.makeText(this, "Please select a voting option.", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onDialogNegativeClick(dialog: DialogFragment) {
        Toast.makeText(this, "Vote cancelled.", Toast.LENGTH_SHORT).show()
    }

}
