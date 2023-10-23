package com.decode.udemyfoodapp.ui.fragments.recipes.bottomsheet

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import com.decode.udemyfoodapp.databinding.RecipesBottomSheetBinding
import com.decode.udemyfoodapp.util.Constants.DEFAULT_DIET_TYPE
import com.decode.udemyfoodapp.util.Constants.DEFAULT_MEAL_TYPE
import com.decode.udemyfoodapp.viewmodels.RecipesViewModel
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.android.material.chip.Chip
import com.google.android.material.chip.ChipGroup
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import java.lang.Exception

@AndroidEntryPoint
class RecipesBottomSheet : BottomSheetDialogFragment() {
    private var _binding: RecipesBottomSheetBinding? = null
    private val binding get() = _binding!!

    private val recipesViewModel: RecipesViewModel by viewModels()

    private var mealTypeChip = DEFAULT_MEAL_TYPE
    private var mealTypeChipId = 0
    private var dietTypeChip = DEFAULT_DIET_TYPE
    private var dietTypeChipId = 0
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = RecipesBottomSheetBinding.inflate(inflater, container, false)

        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.CREATED){
                recipesViewModel.readMealAndDietType.collect {value ->
                    mealTypeChip = value.selectedMealType
                    dietTypeChip = value.selectedDietType

                    updateChip(value.selectedMealTypeId, binding.mealTypeChipGroup)
                    updateChip(value.selectedDietTypeId, binding.dietTypeChipGroup)
                }
            }
        }


        binding.mealTypeChipGroup.setOnCheckedStateChangeListener { group, _ ->
            val chipId = group.checkedChipId
            val chip = group.findViewById<Chip>(chipId)
            val selectedMealType = chip.text.toString().lowercase()

            mealTypeChip = selectedMealType
            mealTypeChipId = chipId
        }

        binding.dietTypeChipGroup.setOnCheckedStateChangeListener { group, _ ->
            val chipId = group.checkedChipId
            val chip = group.findViewById<Chip>(chipId)
            val selectedMealType = chip.text.toString().lowercase()

            dietTypeChip = selectedMealType
            dietTypeChipId = chipId
        }

        binding.applyBtn.setOnClickListener {
            recipesViewModel.saveMealAndDietType(
                mealTypeChip,
                mealTypeChipId,
                dietTypeChip,
                dietTypeChipId
            )

            val action = RecipesBottomSheetDirections.actionRecipesBottomSheetToRecipesFragment(true)
            findNavController().navigate(action)
        }


        return binding.root
    }

    private fun updateChip(selectedMealTypeId: Int, mealTypeChipGroup: ChipGroup) {
        if (selectedMealTypeId != 0) {
            try {
                mealTypeChipGroup.findViewById<Chip>(selectedMealTypeId).isChecked = true
            } catch (e: Exception) {
                Log.d("RecipesBottomSheet",e.message.toString())
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}