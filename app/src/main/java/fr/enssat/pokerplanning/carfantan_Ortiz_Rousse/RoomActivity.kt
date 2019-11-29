package fr.enssat.pokerplanning.carfantan_Ortiz_Rousse

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import fr.enssat.pokerplanning.carfantan_Ortiz_Rousse.databinding.ActivityRoomBinding


class RoomActivity : AppCompatActivity() {
    private lateinit var binding: ActivityRoomBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = DataBindingUtil.setContentView(this, R.layout.activity_room)
        val model = ViewModelProviders.of(this, MulticastViewModelFactory(this)).get(
            MultiCastViewModel::class.java
        )

        binding.createRoomButton.setOnClickListener {
            createRoom(model)
        }

        val adapter = RoomAdapter(this::connectToRoom)
        binding.roomList.adapter = adapter

        model.rooms.observe(this, Observer { list ->
            adapter.list = list
        })

        binding.refreshButton.setOnClickListener {
            adapter.notifyDataSetChanged()
        }
    }

    fun connectToRoom(room: String) {
        Log.d("DEBUG-Activiy", room)
    }

    private fun createRoom(model: MultiCastViewModel) {
        if (binding.roomEditText.text.toString().trim().isNotEmpty()) {
            model.send(binding.roomEditText.text.toString())

            startActivity(Intent(this, VoteActivity::class.java))
        } else {
            Toast.makeText(this, "Please enter a room name.", Toast.LENGTH_SHORT).show()
        }
    }
}
