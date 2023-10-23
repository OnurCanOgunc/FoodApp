package com.decode.udemyfoodapp.data

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.decode.udemyfoodapp.util.Constants.DEFAULT_DIET_TYPE
import com.decode.udemyfoodapp.util.Constants.DEFAULT_MEAL_TYPE
import com.decode.udemyfoodapp.util.Constants.PREFERENCES_DIET_TYPE
import com.decode.udemyfoodapp.util.Constants.PREFERENCES_DIET_TYPE_ID
import com.decode.udemyfoodapp.util.Constants.PREFERENCES_MEAL_TYPE
import com.decode.udemyfoodapp.util.Constants.PREFERENCES_MEAL_TYPE_ID
import com.decode.udemyfoodapp.util.Constants.PREFERENCES_NAME
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.android.scopes.ActivityRetainedScoped
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import java.io.IOException
import javax.inject.Inject
@ActivityRetainedScoped
class DataStoreRepository @Inject constructor(@ApplicationContext private val context: Context) {

    companion object PreferencesKeys {
        val selectedMealType = stringPreferencesKey(PREFERENCES_MEAL_TYPE)
        val selectedMealTypeId = intPreferencesKey(PREFERENCES_MEAL_TYPE_ID)
        val selectedDietType = stringPreferencesKey(PREFERENCES_DIET_TYPE)
        val selectedDietTypeId = intPreferencesKey(PREFERENCES_DIET_TYPE_ID)
    }

    private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = PREFERENCES_NAME)
    private val dataStore = context.dataStore

    suspend fun saveMealAndDietType(
        mealType: String,
        mealTypeId: Int,
        dietType: String,
        dietTypeId: Int
    ) {
        dataStore.edit { preferences ->
            preferences[selectedMealType] = mealType
            preferences[selectedMealTypeId] = mealTypeId
            preferences[selectedDietType] = dietType
            preferences[selectedDietTypeId] = dietTypeId
        }
    }

    val readMealAndDietType: Flow<MealAndDietType> = dataStore.data
        .catch { exception ->
            exception as? IOException ?: throw exception
            emit(emptyPreferences())
        }.map {preferences ->
            val selectedMealType = preferences[selectedMealType] ?: DEFAULT_MEAL_TYPE
            val selectedMealTypeId = preferences[selectedMealTypeId] ?: 0
            val selectedDietType = preferences[selectedDietType] ?: DEFAULT_DIET_TYPE
            val selectedDietTypeId = preferences[selectedDietTypeId] ?: 0
            MealAndDietType(
                selectedMealType,
                selectedMealTypeId,
                selectedDietType,
                selectedDietTypeId
            )
        }


}

data class MealAndDietType(
    val selectedMealType: String,
    val selectedMealTypeId: Int,
    val selectedDietType: String,
    val selectedDietTypeId: Int
)