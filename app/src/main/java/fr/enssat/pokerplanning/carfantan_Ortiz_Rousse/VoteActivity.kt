package fr.enssat.pokerplanning.carfantan_Ortiz_Rousse

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import fr.enssat.pokerplanning.carfantan_Ortiz_Rousse.databinding.ActivityVoteBinding


class VoteActivity : AppCompatActivity(), VoteDialogFragment.NoticeDialogListener {
    private lateinit var binding: ActivityVoteBinding
    private lateinit var model: ClientViewModel
    lateinit var session: SessionManager
    lateinit var user: HashMap<String, String?>
    lateinit var username: String
    lateinit var room: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_vote)

        session = SessionManager(applicationContext)
        user = session.userDetails
        username = user["userName"].toString()
        room = user["roomName"].toString()
        setTitle("Vote: $room")

        model = ViewModelProviders.of(this, ClientViewModelFactory(this))
            .get(ClientViewModel::class.java)
        model.connect("192.168.144.4", ServerSocket.PORT)

        val adapter = MessageAdapter()
        binding.voteList.adapter = adapter
        model.messages.observe(this, Observer { list ->
            adapter.list = list
        })

        binding.sendVoteFab.setOnClickListener {
            showNoticeDialog()
        }

        binding.stopVoteButton.setOnClickListener {
            stopVote()
        }
    }

    private fun stopVote() {
        val intent = Intent(this, RoomActivity::class.java)
        startActivity(intent)
        viewModelStore.clear()
        finishActivity(1)
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
            val votes = VotesMessage(room, liste)
            val json = Message.toJson(votes)

            model.send(json)
            binding.sendVoteFab.hide()
        } else {
            Toast.makeText(this, "Please select a voting option.", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onDialogNegativeClick(dialog: DialogFragment) {
        Toast.makeText(this, "Vote cancelled.", Toast.LENGTH_SHORT).show()
    }

}
