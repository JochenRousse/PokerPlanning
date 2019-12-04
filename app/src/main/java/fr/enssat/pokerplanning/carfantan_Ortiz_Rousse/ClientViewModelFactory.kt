package fr.enssat.pokerplanning.carfantan_Ortiz_Rousse

import android.app.Activity
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class ClientViewModelFactory(private val activity: Activity): ViewModelProvider.Factory {

    //factory crée afin de parvenir à passer un parameter en argument ici l'activity au view model...
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return ClientViewModel(activity) as T
    }
}