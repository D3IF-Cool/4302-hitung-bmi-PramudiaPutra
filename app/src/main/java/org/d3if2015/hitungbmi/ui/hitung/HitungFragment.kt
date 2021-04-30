package org.d3if2015.hitungbmi.ui.hitung

import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.*
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.observe
import androidx.navigation.fragment.findNavController
import org.d3if2015.hitungbmi.R
import org.d3if2015.hitungbmi.data.KategoriBmi
import org.d3if2015.hitungbmi.databinding.FragmentHitungBinding
import org.d3if2015.hitungbmi.db.BmiDb

class HitungFragment : Fragment() {

    private val viewModel: HitungViewModel by lazy {
        val db = BmiDb.getInstance(requireContext())
        val factory = HitungViewModelFactory(db.dao)
        ViewModelProvider(this, factory).get(HitungViewModel::class.java)
    }

    private lateinit var binding: FragmentHitungBinding

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.option_menu, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId) {
            R.id.menu_history -> {
                findNavController().navigate(
                    R.id.action_hitungFragment_to_historiFragment
                )
                return true
            }

            R.id.menu_about -> {
                findNavController().navigate(
                    R.id.action_hitungFragment_to_aboutFragment
                )
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentHitungBinding.inflate(layoutInflater, container, false)
        binding.btnHitung.setOnClickListener { hitungBmi() }
        binding.btnSaran.setOnClickListener { viewModel.mulaiNavigasi() }
        binding.btnShare.setOnClickListener { shareData() }
        setHasOptionsMenu(true)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.getNavigasi().observe(viewLifecycleOwner) {
            if (it == null) return@observe
            findNavController().navigate(
                HitungFragmentDirections.actionHitungFragmentToSaranFragment(
                    it
                )
            )
            viewModel.selesaiNavigasi()
        }

        viewModel.getHasilBmi().observe(viewLifecycleOwner) {
            if (it == null) return@observe
            binding.tvBmi.text = getString(R.string.bmi_x, it.bmi)
            binding.tvKategori.text = getString(R.string.kategori_x, getKategori(it.kategori))
            binding.groupBtn.visibility = View.VISIBLE
        }

        viewModel.data.observe(viewLifecycleOwner) {
            if (it == null) return@observe
            Log.d("HitungFragment", "Data Tersimpan. ID = ${it.id}")
        }
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

        viewModel.hitungBmi(berat, tinggi, isMale)
    }

    private fun shareData() {
        val selectedId = binding.rgPilihKelamin.checkedRadioButtonId
        val gender = if (selectedId == R.id.rbPria)
            getString(R.string.pria)
        else
            getString(R.string.wanita)

        val message = getString(
            R.string.bagikan_template,
            binding.etBeratBadan.text,
            binding.etTinggiBadan.text,
            gender,
            binding.tvBmi.text,
            binding.tvKategori.text
        )

        val shareIntent = Intent(Intent.ACTION_SEND)
        shareIntent.setType("text/plain").putExtra(Intent.EXTRA_TEXT, message)
        if (shareIntent.resolveActivity(
                requireActivity().packageManager
            ) != null
        ) {
            startActivity(shareIntent)
        }
    }

    private fun getKategori(kategori: KategoriBmi): String {

        val stringRes = when (kategori) {
            KategoriBmi.KURUS -> R.string.kurus
            KategoriBmi.IDEAL -> R.string.ideal
            KategoriBmi.GEMUK -> R.string.gemuk
        }
        return getString(stringRes)
    }
}