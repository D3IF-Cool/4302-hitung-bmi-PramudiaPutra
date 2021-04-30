package org.d3if2015.hitungbmi.ui.hitung

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import org.d3if2015.hitungbmi.db.BmiDao
import java.lang.IllegalArgumentException

class HitungViewModelFactory(
    private val db: BmiDao
) : ViewModelProvider.Factory {
    @Suppress("unchecked_cast")
    override fun <T: ViewModel?> create(modelClass: Class<T>) : T {
        if (modelClass.isAssignableFrom(HitungViewModel::class.java)) {
            return HitungViewModel(db) as T
        }
        throw IllegalArgumentException("Unknown viewModel class")
    }
}