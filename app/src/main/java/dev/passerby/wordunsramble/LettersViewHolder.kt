package dev.passerby.wordunsramble

import android.view.View
import androidx.appcompat.widget.AppCompatButton
import androidx.recyclerview.widget.RecyclerView

class LettersViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    val button: AppCompatButton = itemView.findViewById(R.id.item_letter_button)
}