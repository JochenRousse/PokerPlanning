package fr.enssat.pokerplanning.carfantan_Ortiz_Rousse

import android.content.Context
import android.content.SharedPreferences

class SessionManager(
    context: Context
) {
    private val sharedPrefer: SharedPreferences
    private val editor: SharedPreferences.Editor
    private val PRIVATE_MODE = 0

    init {
        sharedPrefer = context.getSharedPreferences(
            PREF_NAME,
            PRIVATE_MODE
        )
        editor = sharedPrefer.edit()
    }

    fun updateUserSession(key: String, value: String) {
        when (key) {
            KEY_USERNAME -> editor.putString(KEY_USERNAME, value)
            KEY_ROOM -> editor.putString(KEY_ROOM, value)
            else -> {
            }
        }
        editor.commit()
    }

    val userDetails: HashMap<String, String?>
        get() {
            val user = HashMap<String, String?>()
            user["userName"] = sharedPrefer.getString(KEY_USERNAME, null)
            user["room"] = sharedPrefer.getString(KEY_ROOM, null)
            return user
        }

    companion object {
        private const val PREF_NAME = "UserSession"
        private const val KEY_USERNAME = "userName"
        private const val KEY_ROOM = "room"
    }
}