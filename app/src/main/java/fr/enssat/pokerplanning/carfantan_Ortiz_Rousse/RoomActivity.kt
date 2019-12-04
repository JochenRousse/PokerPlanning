package fr.enssat.pokerplanning.carfantan_Ortiz_Rousse

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.DividerItemDecoration
import fr.enssat.pokerplanning.carfantan_Ortiz_Rousse.databinding.ActivityRoomBinding


class RoomActivity : AppCompatActivity() {
    private lateinit var binding: ActivityRoomBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = DataBindingUtil.setContentView(this, R.layout.activity_room)
        val model = ViewModelProviders.of(this, MulticastViewModelFactory(this)).get(
            MultiCastViewModel::class.java
        )

        val itemDecor = DividerItemDecoration(applicationContext, DividerItemDecoration.VERTICAL)
        itemDecor.setDrawable(applicationContext.getResources().getDrawable(R.drawable.divider))
        binding.roomList.addItemDecoration(itemDecor)

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

    private fun connectToRoom(room: String, owner: Boolean) {
        val session = SessionManager(applicationContext)
        session.updateUserSession("room", room)

        if (owner) {
            startActivity(Intent(this, VoteActivityOwner::class.java))
        } else {
            startActivity(Intent(this, VoteActivity::class.java))
        }
        finish()
    }

    private fun createRoom(model: MultiCastViewModel) {
        val room = binding.roomEditText.text.toString()
        if (room.trim().isNotEmpty()) {
            val session = SessionManager(applicationContext)
            val user = session.userDetails
            val username = user["userName"].toString()

            val cleanIp = NetworkUtils.getIpAddress(this).toString().replace("/", "")

            val data = RoomMessage(
                room,
                username,
                cleanIp,
                java.util.UUID.randomUUID().toString()
            )
            val roomJson = Message.toJson(data)

            model.send(roomJson)
            connectToRoom(roomJson, true)
        } else {
            Toast.makeText(this, "Please enter a room name.", Toast.LENGTH_SHORT).show()
        }
    }
}
