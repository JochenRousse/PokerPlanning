package fr.enssat.pokerplanning.carfantan_Ortiz_Rousse

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.DialogFragment
import fr.enssat.pokerplanning.carfantan_Ortiz_Rousse.databinding.ActivityVoteBinding


class VoteActivity : AppCompatActivity(), VoteDialogFragment.NoticeDialogListener {
    private lateinit var binding: ActivityVoteBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_vote)

        val session =
            SessionManager(applicationContext)
        val user = session.userDetails
        val username = user["userName"].toString()
        val role = user["userRole"].toString()

        binding.sendVoteFab.setOnClickListener {
            showNoticeDialog()
        }

        binding.stopVoteButton.setOnClickListener {
            stopVote(it)
        }
    }

    private fun stopVote(view: View) {
        binding.apply {
            invalidateAll()
        }
        startActivity(Intent(this, ResultsActivity::class.java))
    }

    fun showNoticeDialog() {
        // Create an instance of the dialog fragment and show it
        val dialog = VoteDialogFragment()
        dialog.show(supportFragmentManager, "VoteDialogFragment")
    }

    override fun onDialogPositiveClick(dialog: DialogFragment, vote: String) {
        if (vote.trim().isNotEmpty()) {
            Log.d("TAG", vote)
            binding.sendVoteFab.hide()
        } else {
            Toast.makeText(this, "Please select a voting option.", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onDialogNegativeClick(dialog: DialogFragment) {
        Toast.makeText(this, "Vote cancelled.", Toast.LENGTH_SHORT).show()
    }

}
