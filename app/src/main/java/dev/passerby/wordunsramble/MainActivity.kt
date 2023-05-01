package dev.passerby.wordunsramble

import android.content.SharedPreferences
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import dev.passerby.wordunsramble.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private val viewModel by lazy {
        ViewModelProvider(this)[GameViewModel::class.java]
    }

    private var preferences: SharedPreferences? = null

    private lateinit var binding: ActivityMainBinding
    private lateinit var freeLettersAdapter: FreeLettersAdapter
    private lateinit var selectedLettersAdapter: SelectedLettersAdapter

    private var mScore: Int = 0
    private var best: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        freeLettersAdapter = FreeLettersAdapter(viewModel)
        freeLettersAdapter.updateScrambledCity(
            viewModel.currentScrambledCity.value.toString().toMutableList()
        )

        selectedLettersAdapter = SelectedLettersAdapter(viewModel)
        selectedLettersAdapter.updateScrambledCity()

        binding.apply {
            gameTimer.start()
            gameFreeLettersRecyclerview.apply {
                layoutManager =
                    LinearLayoutManager(this@MainActivity, LinearLayoutManager.HORIZONTAL, false)
                adapter = freeLettersAdapter
            }

            gameSelectedLettersRecyclerview.apply {
                layoutManager =
                    LinearLayoutManager(this@MainActivity, LinearLayoutManager.HORIZONTAL, false)
                adapter = selectedLettersAdapter
            }

            gameSubmitButton.setOnClickListener { onSubmitCity() }
            gameSkipButton.setOnClickListener { onSkipCity() }
            gameRefreshButton.setOnClickListener {
                selectedLettersAdapter.clearSelectedLetters()
                freeLettersAdapter.returnFreeLetters(
                    viewModel.currentScrambledCity.value.toString().toCharArray().toMutableList()
                )
            }
        }

        viewModel.currentScrambledCity.observe(this) {
            freeLettersAdapter.updateScrambledCity(it.toString().toMutableList())
            selectedLettersAdapter.updateScrambledCity()
        }

        viewModel.currentSelectedLetters.observe(this) {
            if (it.isNotEmpty()) {
                selectedLettersAdapter.updateLetters(it)
            }
        }
        viewModel.currentFreeLetters.observe(this) {
            if (it.isNotEmpty()) {
                freeLettersAdapter.updateLetters(it)
            }
        }
        viewModel.currentScrambledCity.observe(this) {
            binding.gameScrambledCityTextview.text = it
        }
        viewModel.score.observe(this) {
            binding.gameScoreTextview.text = it.toString()
            mScore = it
        }
        viewModel.currentCityNumber.observe(this) {
            binding.gameCityNumberTextview.text =
                String.format(getString(R.string.city_number), it, 10)
        }
    }

    private fun onSubmitCity() {
        val playerCity = viewModel.currentSelectedLetters.value!!.joinToString("")

        if (playerCity.isEmpty()) {
            Toast.makeText(
                this,
                getString(R.string.no_selected_letters),
                Toast.LENGTH_SHORT
            ).show()
        } else {
            if (viewModel.isUserCityCorrect(playerCity)) {
                if (!viewModel.nextWord()) {
                    if (mScore > best) {
                        best = mScore
                    }
                    val editor = preferences!!.edit()
                    editor.putInt("best", best)
                    editor.apply()
                    Toast.makeText(this, "Game is finished", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun onSkipCity() {
        if (!viewModel.nextWord()) {
            if (mScore > best) {
                best = mScore
            }
            val editor = preferences!!.edit()
            editor.putInt("best", best)
            editor.apply()
            Toast.makeText(this, "Game is finished", Toast.LENGTH_SHORT).show()
        }
    }
}