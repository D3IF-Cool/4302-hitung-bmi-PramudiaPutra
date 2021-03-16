package org.d3if2015.hitungbmi.ui

import android.os.Bundle
import android.text.TextUtils
import android.view.*
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import org.d3if2015.hitungbmi.R
import org.d3if2015.hitungbmi.data.KategoriBmi
import org.d3if2015.hitungbmi.databinding.FragmentHitungBinding

class HitungFragment : Fragment() {

    private lateinit var binding: FragmentHitungBinding
    private lateinit var kategoriBmi: KategoriBmi

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.option_menu, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.menu_about){
            findNavController().navigate(
                R.id.action_hitungFragment_to_aboutFragment
            )
            return true
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentHitungBinding.inflate(layoutInflater, container, false)
        binding.btnHitung.setOnClickListener { hitungBmi() }
        binding.btnSaran.setOnClickListener { view:View ->
            view.findNavController().navigate(
                HitungFragmentDirections.actionHitungFragmentToSaranFragment(kategoriBmi)
            )
        }
        setHasOptionsMenu(true)
        return binding.root
    }

    private fun hitungBmi() {
        val berat = binding.etBeratBadan.text.toString()
        val tinggi = binding.etTinggiBadan.text.toString()
        val selectedId = binding.rgPilihKelamin.checkedRadioButtonId

        if (TextUtils.isEmpty(berat)) {
            Toast.makeText(
                context,
                R.string.berat_invalid, Toast.LENGTH_LONG
            ).show()
            return
        }

        if (TextUtils.isEmpty(tinggi)) {
            Toast.makeText(
                context,
                R.string.tinggi_invalid, Toast.LENGTH_LONG
            ).show()
            return
        }

        if (selectedId == -1) {
            Toast.makeText(
                context,
                R.string.kelamin_invalid, Toast.LENGTH_LONG
            ).show()
            return
        }

        val isMale = selectedId == R.id.rbPria
        val tinggiCm = tinggi.toFloat() / 100
        val bmi = berat.toFloat() / (tinggiCm * tinggiCm)
        val kategori = getKategori(bmi, isMale)

        binding.tvBmi.text = getString(R.string.bmi_x, bmi)
        binding.tvKategori.text = getString(R.string.kategori_x, kategori)
        binding.btnSaran.visibility = View.VISIBLE
    }

    private fun getKategori(bmi: Float, male: Boolean): String {
        kategoriBmi = if (male) {
            when {
                bmi < 20.5 -> KategoriBmi.KURUS
                bmi >= 27.0 -> KategoriBmi.GEMUK
                else -> KategoriBmi.IDEAL
            }
        } else {
            when {
                bmi < 18.5 -> KategoriBmi.KURUS
                bmi >= 25.0 -> KategoriBmi.GEMUK
                else -> KategoriBmi.IDEAL
            }

        }
        val stringRes = when(kategoriBmi) {
            KategoriBmi.KURUS -> R.string.kurus
            KategoriBmi.IDEAL -> R.string.ideal
            KategoriBmi.GEMUK -> R.string.gemuk
        }
        return getString(stringRes)
    }
}