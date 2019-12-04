package fr.enssat.pokerplanning.carfantan_Ortiz_Rousse

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class ResultViewModel(private val context: Context) : ViewModel() {

    private val _msgSet = mutableSetOf<Votant>()
    private val _allResults = MutableLiveData<List<Votant>>()

    val results: LiveData<List<Votant>> get() = _allResults

    fun displayResults(msg: String) {
        val message = Message.fromJson(msg)
        val votesMessage = message as VotesMessage

        for (i in votesMessage.liste.indices) {
            _msgSet.add(votesMessage.liste[i])
            _allResults.postValue(_msgSet.toList())
        }
    }
}