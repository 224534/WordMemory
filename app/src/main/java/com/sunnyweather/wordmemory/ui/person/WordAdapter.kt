package com.sunnyweather.wordmemory.ui.person

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.sunnyweather.wordmemory.R
import com.sunnyweather.wordmemory.logic.model.Word

class WordAdapter(private val activity: WordActivity, private var words: MutableList<Word>) :
    RecyclerView.Adapter<WordAdapter.ViewHolder>() {

    private val context = activity

    private val viewModel = activity.viewModel

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val wordText = view.findViewById<TextView>(R.id.wordText)
        val translateText = view.findViewById<TextView>(R.id.translateText)
        val wordDeleteBtn = view.findViewById<Button>(R.id.wordDeleteBtn)
        val likeBtn = view.findViewById<Button>(R.id.likeBtn)
        val wordSetBtn = view.findViewById<Button>(R.id.wordSetBtn)
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.word_item, parent, false)
        val holder = ViewHolder(view)
        val position = holder.adapterPosition
        val word = words[position]
        holder.apply {
            wordDeleteBtn.setOnClickListener {
                viewModel.deleteWord(position)
                words = viewModel.getWords()
                notifyDataSetChanged()
            }
            likeBtn.setOnClickListener {
                word.like = word.like xor true
                viewModel.updateWord(word, position)
                words = viewModel.getWords()
                notifyDataSetChanged()
            }
            wordSetBtn.setOnClickListener {
                val intent = Intent(context, WordDialogActivity::class.java)
                intent.putExtra("position", position)
                intent.putExtra("word", word.word)
                intent.putExtra("like", word.like)
                intent.putExtra("translate", word.translate)
                context.startActivityForResult(intent, 1)
                viewModel.deleteWord(position) //先删除后加入，防止找不到
                words = viewModel.getWords()
                notifyDataSetChanged()
            }
        }
        return holder
    }

    override fun onBindViewHolder(
        holder: ViewHolder,
        position: Int
    ) {
        val word = words[position]
        holder.wordText.text = word.word
        holder.translateText.text = word.translate
    }

    override fun getItemCount() = words.size
}