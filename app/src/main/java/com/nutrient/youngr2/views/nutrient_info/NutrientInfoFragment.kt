package com.nutrient.youngr2.views.nutrient_info

import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.navigation.ui.onNavDestinationSelected
import com.bumptech.glide.Glide
import com.nutrient.youngr2.R
import com.nutrient.youngr2.base.BaseFragment
import com.nutrient.youngr2.databinding.FragmentNutrientInfoBinding

class NutrientInfoFragment : BaseFragment<FragmentNutrientInfoBinding>(R.layout.fragment_nutrient_info) {
    private val safeArgs : NutrientInfoFragmentArgs by navArgs()
    override fun init() {
        /* Action bar 초기화 */
        val actionBar = (activity as AppCompatActivity).supportActionBar
        actionBar?.apply {
            title = requireContext().getString(R.string.title_product_info)
            setDisplayHomeAsUpEnabled(true)
            show()
        }

        binding.apply {
            nutrient = safeArgs.productInfo
            Glide.with(requireContext())
                .load(nutrient?.imageUrl)
                .error(R.drawable.ic_no_image)
                .override(800, 800)
                .into(ivProduct)
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when(item.itemId) {
            android.R.id.home -> {
                findNavController().navigateUp()
                true
            }
            else -> {
                (item.onNavDestinationSelected(findNavController())
                        || super.onOptionsItemSelected(item))
            }
        }
    }
}