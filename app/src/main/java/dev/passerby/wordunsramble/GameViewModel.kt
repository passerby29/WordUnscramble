package dev.passerby.wordunsramble

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import dev.passerby.wordunsramble.Constants.MAX_NO_OF_WORDS
import dev.passerby.wordunsramble.Constants.SCORE_INCREASE
import dev.passerby.wordunsramble.Constants.cityNamesList
import java.util.Locale

class GameViewModel : ViewModel() {

    private val _score = MutableLiveData(0)
    val score: LiveData<Int>
        get() = _score

    private val _currentCityNumber = MutableLiveData(0)
    val currentCityNumber: LiveData<Int>
        get() = _currentCityNumber

    private val _currentScrambledCity = MutableLiveData<String>()
    val currentScrambledCity: LiveData<String>
        get() = _currentScrambledCity

    private val _currentFreeLetters = MutableLiveData(mutableListOf<Char>())
    val currentFreeLetters: LiveData<MutableList<Char>>
        get() = _currentFreeLetters

    private val _currentSelectedLetters = MutableLiveData(mutableListOf<Char>())
    val currentSelectedLetters: LiveData<MutableList<Char>>
        get() = _currentSelectedLetters

    private var wordsList: MutableList<String> = mutableListOf()
    private lateinit var currentCity: String

    fun nextWord(): Boolean {
        return if (_currentCityNumber.value!! < MAX_NO_OF_WORDS) {
            getNextCity()
            true
        } else false
    }

    fun isUserCityCorrect(playerWord: String): Boolean {
        if (playerWord.equals(currentCity, true)) {
            increaseScore()
            return true
        }
        return false
    }

    fun addNewSelectedLetter(newLetter: Char) {
        _currentSelectedLetters.value?.add(newLetter)
        _currentSelectedLetters.value = currentSelectedLetters.value
    }

    fun addNewFreeLetter(newLetter: Char) {
        _currentFreeLetters.value?.add(newLetter)
        _currentFreeLetters.value = currentFreeLetters.value
    }

    fun addAllNewFreeLetters(allNewLetters: MutableList<Char>) {
        deleteAllFreeLetters()
        _currentFreeLetters.value?.addAll(allNewLetters)
    }

    fun deleteAllSelectedLetters() {
        _currentSelectedLetters.value?.clear()
        _currentSelectedLetters.value = currentSelectedLetters.value
    }

    fun deleteSelectedLetter(position: Int) {
        _currentSelectedLetters.value?.removeAt(position)
        _currentSelectedLetters.value = _currentSelectedLetters.value
    }

    fun deleteFreeLetter(position: Int) {
        _currentFreeLetters.value?.removeAt(position)
        _currentFreeLetters.value = currentFreeLetters.value
    }

    private fun getNextCity(){
        currentCity = cityNamesList.random().lowercase(Locale.ROOT)

        val tempCity = currentCity.toCharArray()
        tempCity.shuffle()

        while (String(tempCity).equals(currentCity, false)) {
            tempCity.shuffle()
        }

        if (wordsList.contains(currentCity)) {
            getNextCity()
        } else {
            deleteAllSelectedLetters()
            deleteAllFreeLetters()

            _currentScrambledCity.value = String(tempCity)
            _currentCityNumber.value = (_currentCityNumber.value)?.inc()
            wordsList.add(currentCity)
        }
    }

    private fun increaseScore() {
        _score.value = (_score.value)?.plus(SCORE_INCREASE)
    }

    private fun deleteAllFreeLetters() {
        _currentFreeLetters.value?.clear()
        _currentFreeLetters.value = currentFreeLetters.value
    }

    init {
        getNextCity()
    }
}