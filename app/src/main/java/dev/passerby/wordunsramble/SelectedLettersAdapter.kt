package dev.passerby.wordunsramble

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView

class SelectedLettersAdapter(private val viewModel: GameViewModel):
    RecyclerView.Adapter<LettersViewHolder>() {

    private lateinit var letters: MutableList<Char>

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LettersViewHolder {
        val layoutInflater = LayoutInflater
            .from(parent.context)
            .inflate(R.layout.item_letter, parent, false)
        return LettersViewHolder(layoutInflater)
    }

    override fun getItemCount() = letters.size

    override fun onBindViewHolder(holder: LettersViewHolder, position: Int) {
        holder.button.text = letters[position].toString()
        holder.button.setOnClickListener {
            viewModel.addNewFreeLetter(holder.button.text[0])
            viewModel.deleteSelectedLetter(position)
            notifyDataSetChanged()
        }
    }

    fun updateScrambledCity() {
        letters = mutableListOf()
        notifyDataSetChanged()
    }

    fun updateLetters(newLetters: MutableList<Char>) {
        letters = newLetters
        notifyDataSetChanged()
    }

    fun clearSelectedLetters() {
        letters = mutableListOf()
        viewModel.deleteAllSelectedLetters()
        notifyDataSetChanged()
    }
}