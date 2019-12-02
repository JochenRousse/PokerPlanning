package fr.enssat.pokerplanning.carfantan_Ortiz_Rousse

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import fr.enssat.pokerplanning.carfantan_Ortiz_Rousse.databinding.ActivityMainBinding


class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    private val userName: User = User("name")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)

        binding.doneButton.setOnClickListener {
            addUsername()
        }
    }

    private fun addUsername() {
        binding.apply {
            userName.name = textUsername.text.toString()
            invalidateAll()
        }

        if (binding.textUsername.text.toString().trim().isNotEmpty()) {
            val session = SessionManager(applicationContext)
            session.updateUserSession("userName", userName.name)

            startActivity(Intent(this, RoomActivity::class.java))
        } else {
            Toast.makeText(this, "Please enter a name.", Toast.LENGTH_SHORT).show()
        }
    }
}
