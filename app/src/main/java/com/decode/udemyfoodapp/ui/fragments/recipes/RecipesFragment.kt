package com.decode.udemyfoodapp.ui.fragments.recipes

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.LinearLayoutManager
import com.decode.udemyfoodapp.viewmodels.MainViewModel
import com.decode.udemyfoodapp.adapters.RecipesAdapter
import com.decode.udemyfoodapp.databinding.FragmentRecipesBinding
import com.decode.udemyfoodapp.util.NetworkResult
import com.decode.udemyfoodapp.viewmodels.RecipesViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
@AndroidEntryPoint
class RecipesFragment : Fragment() {

    private var _binding: FragmentRecipesBinding? = null
    private val binding get() = _binding!!
    private val recipeAdapter: RecipesAdapter by lazy { RecipesAdapter() }
    private val mainViewModel: MainViewModel by viewModels()
    private val recipesViewModel: RecipesViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentRecipesBinding.inflate(inflater, container, false)

        setupRecyclerView()
        requestApiData()
        return binding.root
    }

    private fun requestApiData() {
        mainViewModel.getRecipes(recipesViewModel.applyQueries())
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                mainViewModel.recipesResponse.collect { response ->
                    when (response) {
                        is NetworkResult.Success -> {
                            hideShimmer()
                            response.data.let { recipeAdapter.differ.submitList(it.results) }
                        }

                        is NetworkResult.Error -> {
                            hideShimmer()
                            Toast.makeText(
                                requireContext(),
                                response.message.toString(),
                                Toast.LENGTH_SHORT
                            ).show()
                        }

                        NetworkResult.Loading -> {
                            showShimmer()
                        }
                    }
                }
            }
        }
    }

    private fun setupRecyclerView() {
        binding.recyclerview.apply {
            adapter = recipeAdapter
            layoutManager = LinearLayoutManager(requireContext())
        }
        showShimmer()
    }

    private fun showShimmer() {
        binding.recyclerview.showShimmerAdapter()
    }

    private fun hideShimmer() {
        binding.recyclerview.hideShimmerAdapter()
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}