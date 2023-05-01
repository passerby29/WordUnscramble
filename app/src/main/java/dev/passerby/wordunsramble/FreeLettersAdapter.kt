package dev.passerby.wordunsramble

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView

class FreeLettersAdapter(private val viewModel: GameViewModel) :
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
            viewModel.addNewSelectedLetter(holder.button.text[0])
            viewModel.deleteFreeLetter(position)
            notifyItemRemoved(position)
        }
    }

    fun updateScrambledCity(newLetters: MutableList<Char>) {
        letters = newLetters
        viewModel.addAllNewFreeLetters(letters)
        notifyDataSetChanged()
    }

    fun updateLetters(newLetters: MutableList<Char>) {
        letters = newLetters
        notifyDataSetChanged()
    }

    fun returnFreeLetters(letters: MutableList<Char>) {
        this.letters = letters
        viewModel.addAllNewFreeLetters(letters)
        notifyDataSetChanged()
    }
}