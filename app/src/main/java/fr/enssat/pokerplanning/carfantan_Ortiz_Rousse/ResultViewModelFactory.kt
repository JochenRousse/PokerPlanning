package fr.enssat.pokerplanning.carfantan_Ortiz_Rousse

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class ResultViewModelFactory(val context: Context): ViewModelProvider.Factory {

    //factory crée afin de parvenir à passer un parameter en argument ici le context au view model...
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return ResultViewModel(context.applicationContext) as T
    }
}