package fr.enssat.pokerplanning.carfantan_Ortiz_Rousse

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.inputmethod.InputMethodManager
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
            addUsername(it)
        }
    }

    private fun addUsername(view: View) {
        binding.apply {
            userName.name = textUsername.text.toString()
            invalidateAll()
        }

        if (binding.textUsername.text.toString().trim().isNotEmpty()) {
            val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(view.windowToken, 0)
            startActivity(Intent(this, RoomActivity::class.java))
        } else {
            Toast.makeText(this, "Please enter a name.", Toast.LENGTH_SHORT).show()
        }
    }
}
