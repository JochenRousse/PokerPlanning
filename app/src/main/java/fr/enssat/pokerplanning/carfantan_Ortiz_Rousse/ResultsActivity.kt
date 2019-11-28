package fr.enssat.pokerplanning.carfantan_Ortiz_Rousse

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import fr.enssat.pokerplanning.carfantan_Ortiz_Rousse.databinding.ActivityResultsBinding


class ResultsActivity : AppCompatActivity() {
    private lateinit var binding: ActivityResultsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_results)

        binding.okButton.setOnClickListener {
            returnHome(it)
        }
    }

    private fun returnHome(view: View) {
        binding.apply {
            invalidateAll()
        }
        startActivity(Intent(this, RoomActivity::class.java))
    }
}
