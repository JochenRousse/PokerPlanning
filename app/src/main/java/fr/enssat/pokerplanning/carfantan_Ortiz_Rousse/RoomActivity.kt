package fr.enssat.pokerplanning.carfantan_Ortiz_Rousse

import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import android.os.Bundle
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
            model.send(binding.roomEditText.text.toString())
        }

        val adapter = RoomAdapter {}
        binding.roomList.adapter = adapter

        model.rooms.observe(this, Observer { list ->
            adapter.list = list
        })

        binding.refreshButton.setOnClickListener {
            adapter.notifyDataSetChanged()
        }
    }
}
