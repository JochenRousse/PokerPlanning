package fr.enssat.pokerplanning.carfantan_Ortiz_Rousse

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import fr.enssat.pokerplanning.carfantan_Ortiz_Rousse.databinding.ActivityVoteBinding


class VoteActivity : AppCompatActivity() {
    private lateinit var binding: ActivityVoteBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_vote)

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
}
