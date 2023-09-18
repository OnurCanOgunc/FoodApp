package com.decode.udemyfoodapp.viewmodels

import android.app.Application
import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.decode.udemyfoodapp.data.Repository
import com.decode.udemyfoodapp.data.database.entity.Recipes
import com.decode.udemyfoodapp.models.FoodRecipe
import com.decode.udemyfoodapp.util.NetworkResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import retrofit2.Response
import java.lang.Exception
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    application: Application,
    private val repository: Repository
) : AndroidViewModel(application) {

    private val _recipesResponse =
        MutableStateFlow<NetworkResult<FoodRecipe>>(NetworkResult.Loading)
    val recipesResponse get() = _recipesResponse.asStateFlow()

    private val _databaseRecipes = MutableLiveData<List<Recipes>>()
    val databaseRecipes: LiveData<List<Recipes>> get() = _databaseRecipes

    /** ROOM */


    fun getAllRecipes() = viewModelScope.launch {
        repository.local.readRecipes().collect { recipes: List<Recipes> ->
            _databaseRecipes.postValue(recipes)
        }
    }

    private fun insertRecipes(recipes: Recipes) = viewModelScope.launch(Dispatchers.IO) {
           repository.local.insertRecipes(recipes)
            getAllRecipes()
        }


    /** RETROFİT */
    fun getRecipes(queries: Map<String, String>) = viewModelScope.launch {
        if (hasInternetConnection()) {
            try {
                val response = repository.remote.getRecipes(queries)
                _recipesResponse.value = handleFoodRecipesResponse(response)


                _recipesResponse.collectLatest {
                    when (it) {
                        is NetworkResult.Success -> {
                            offlineCacheRecipes(it.data)

                        }

                        else -> {}
                    }
                }

            } catch (e: Exception) {
                _recipesResponse.value = NetworkResult.Error("Recipes Not Found")
            }
        } else {
            _recipesResponse.value = NetworkResult.Error("No Internet Connection")
        }
    }

    private fun offlineCacheRecipes(foodRecipe: FoodRecipe) {
        val recipesEntity = Recipes(foodRecipe)
        insertRecipes(recipesEntity)
    }


    private fun handleFoodRecipesResponse(response: Response<FoodRecipe>): NetworkResult<FoodRecipe> {
        return when {
            response.message().toString().contains("timeout") -> NetworkResult.Error("Timeout")
            response.code() == 402 -> NetworkResult.Error("API Key Limited")
            response.body()!!.results.isEmpty() -> NetworkResult.Error("Recipes Not Found")
            response.isSuccessful -> NetworkResult.Success(response.body()!!)
            else -> NetworkResult.Error(response.message())
        }
    }

    private fun hasInternetConnection(): Boolean {
        val connectivityManager =
            getApplication<Application>().getSystemService(
                Context.CONNECTIVITY_SERVICE
            ) as ConnectivityManager
        val activeNetwork = connectivityManager.activeNetwork ?: return false
        val capabilities = connectivityManager.getNetworkCapabilities(activeNetwork) ?: return false
        return when {
            capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true
            capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> true
            capabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> true
            else -> false
        }
    }
}