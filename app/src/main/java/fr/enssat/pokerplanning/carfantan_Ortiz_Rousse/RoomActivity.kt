package fr.enssat.pokerplanning.carfantan_Ortiz_Rousse

import android.content.Intent
import android.os.Bundle
import android.util.Patterns
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.DividerItemDecoration
import fr.enssat.pokerplanning.carfantan_Ortiz_Rousse.databinding.ActivityRoomBinding


class RoomActivity : AppCompatActivity(), IPDialogFragment.NoticeDialogListener {
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

        session.updateUserSession("roomName", room)

        if (owner) {
            startActivityForResult(Intent(this, VoteActivityOwner::class.java), 1)
        } else {
            showNoticeDialog()
        }
    }

    private fun createRoom(model: MultiCastViewModel) {
        val room = binding.roomEditText.text.toString()
        if (room.trim().isNotEmpty()) {
            val session = SessionManager(applicationContext)
            val user = session.userDetails
            val username = user["userName"].toString()
            val data = RoomMessage(room, username)
            model.send(data)
            connectToRoom(room, true)
        } else {
            Toast.makeText(this, "Please enter a room name.", Toast.LENGTH_SHORT).show()
        }
    }

    private fun showNoticeDialog() {
        // Create an instance of the dialog fragment and show it
        val dialog = IPDialogFragment()
        dialog.show(supportFragmentManager, "IPDialogFragment")
    }

    override fun onDialogPositiveClick(dialog: DialogFragment, ip: String) {
        if (ip.trim().isNotEmpty() && Patterns.IP_ADDRESS.matcher(ip).matches()) {
            val intent = Intent(this, VoteActivity::class.java)
            intent.putExtra("ip", ip)
            startActivityForResult(intent, 1)
        } else {
            Toast.makeText(this, "Please enter a valid IP address.", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onDialogNegativeClick(dialog: DialogFragment) {
        Toast.makeText(this, "Join cancelled.", Toast.LENGTH_SHORT).show()
    }
}
