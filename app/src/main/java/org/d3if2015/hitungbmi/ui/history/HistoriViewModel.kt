package org.d3if2015.hitungbmi.ui.history

import androidx.lifecycle.ViewModel
import org.d3if2015.hitungbmi.db.BmiDao

class HistoriViewModel(db:BmiDao): ViewModel() {
    val data = db.getLastBmi()
}