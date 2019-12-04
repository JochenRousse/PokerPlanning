package fr.enssat.pokerplanning.carfantan_Ortiz_Rousse

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.DividerItemDecoration
import fr.enssat.pokerplanning.carfantan_Ortiz_Rousse.databinding.ActivityResultBinding


class ResultActivity : AppCompatActivity() {
    private lateinit var binding: ActivityResultBinding
    private lateinit var model: ResultViewModel
    lateinit var session: SessionManager
    lateinit var user: HashMap<String, String?>
    lateinit var room: RoomMessage

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_result)

        session = SessionManager(applicationContext)
        user = session.userDetails
        val message = Message.fromJson(user["room"].toString())
        room = message as RoomMessage

        setTitle("Results for ${room.name}")

        model = ViewModelProviders.of(this, ResultViewModelFactory(this))
            .get(ResultViewModel::class.java)

        val itemDecor = DividerItemDecoration(applicationContext, DividerItemDecoration.VERTICAL)
        itemDecor.setDrawable(applicationContext.getResources().getDrawable(R.drawable.divider))
        binding.resultList.addItemDecoration(itemDecor)

        val adapter = ResultAdapter()
        binding.resultList.adapter = adapter
        model.results.observe(this, Observer { list ->
            adapter.list = list
        })

        val stringResults = getIntent().getStringExtra("results")

        if (stringResults != null && stringResults.trim().isNotEmpty()) {
            model.displayResults(stringResults)
        }

        binding.returnRoomButton.setOnClickListener {
            returnHome()
        }
    }

    private fun returnHome() {
        val intent = Intent(this, RoomActivity::class.java)
        startActivity(intent)
        finish()
    }
}
