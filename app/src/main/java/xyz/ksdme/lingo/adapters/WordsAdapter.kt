package xyz.ksdme.lingo.adapters

import android.content.Context
import android.graphics.Typeface
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.SwitchCompat
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.element_word_list.view.*

import xyz.ksdme.lingo.R

class WordsAdapter(private val context: Context,
                   private val words: ArrayList<String>,
                   private val typeface: Typeface):
        RecyclerView.Adapter<WordsAdapter.Holder>() {

    override fun getItemCount(): Int {
        return this.words.size
    }

    override fun onBindViewHolder(holder: Holder, position: Int) {
        holder.word.text = this.words[position]
    }

    override fun onCreateViewHolder(parent: ViewGroup, position: Int): Holder {
        val holder = Holder(LayoutInflater.from(this.context).inflate(
            R.layout.element_word_list, parent, false))

        holder.word.typeface = typeface

        return holder
    }


    class Holder(view: View): RecyclerView.ViewHolder(view) {
        val word: SwitchCompat = view.word
    }

}
