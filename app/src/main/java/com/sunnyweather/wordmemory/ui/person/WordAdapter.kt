package com.sunnyweather.wordmemory.ui.person

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.sunnyweather.wordmemory.R
import com.sunnyweather.wordmemory.getResultLauncher
import com.sunnyweather.wordmemory.logic.model.Word

class WordAdapter(activity: WordActivity, word: List<Word>) :
    RecyclerView.Adapter<WordAdapter.ViewHolder>() {

    private val context = activity

    private val viewModel = activity.viewModel

    var words: List<Word>

    init {
        words = if(viewModel.likeOrNot == false) word
        else word.filter { it.like == true }
    }

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val wordText = view.findViewById<TextView>(R.id.wordText)
        val translateText = view.findViewById<TextView>(R.id.translateText)
        val wordDeleteBtn = view.findViewById<Button>(R.id.wordDeleteBtn)
        val likeBtn = view.findViewById<ImageView>(R.id.likeBtn)
        val wordSetBtn = view.findViewById<Button>(R.id.wordSetBtn)
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.word_item, parent, false)
        val holder = ViewHolder(view)
        holder.apply {
            wordDeleteBtn.setOnClickListener {
                AlertDialog.Builder(context).apply {
                    setMessage("删除这个单词？")
                    setCancelable(true)
                    setPositiveButton("确定") { dialog, which ->
                        val word = words[adapterPosition]
                        viewModel.deleteWord(word.id)
                    }
                    setNegativeButton("取消") { dialog, which -> }
                    show()
                }
                //words = viewModel.getWords()
                //notifyDataSetChanged()
            }
            likeBtn.setOnClickListener {
                val word = words[adapterPosition]
                word.like = word.like xor true
                viewModel.updateWord(word, word.id)
                //words = if(viewModel.likeOrNot == false) viewModel.getWords()
                        //else viewModel.getWords().filter { it.like == true }
                //notifyDataSetChanged()
            }
            wordSetBtn.setOnClickListener {
                val word = words[adapterPosition]
                val intent = Intent(context, WordDialogActivity::class.java)
                intent.putExtra("id", word.id)
                intent.putExtra("word", word.word)
                intent.putExtra("like", word.like)
                intent.putExtra("translate", word.translate)
                context.setResultLauncher.launch(intent)
                //context.startActivityForResult(intent, 1)
            }
            //不用离开本页面就直接更新
            //需要离开本页面就在activity中recreate
        }
        return holder
    }

    override fun onBindViewHolder(
        holder: ViewHolder,
        position: Int
    ) {
        val word = words[position]
        holder.apply {
            wordText.text = word.word
            translateText.text = word.translate
            if(word.like == true) {
                likeBtn.setBackgroundColor(ContextCompat.getColor(context, R.color.like))
            }
        }
    }

    override fun getItemCount() = words.size

    fun submitList(newWords: MutableList<Word>) { //由于外面只会传入总体数据，因此主构造函数中筛一遍，这里还要筛一遍
        words = if(viewModel.likeOrNot == false) newWords
        else newWords.filter { it.like == true }
        notifyDataSetChanged()
    }

}

