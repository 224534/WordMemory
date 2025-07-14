package com.sunnyweather.wordmemory.ui.person

import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.RecyclerView
import com.sunnyweather.wordmemory.R
import com.sunnyweather.wordmemory.logic.model.WordPrefer
import com.sunnyweather.wordmemory.ui.home.MainActivity

class BookAdapter(activity: BookActivity, private var books: List<WordPrefer>) : //源list直接来自viewModel中，因此在设置完，检测到改变后，直接刷新即可
    RecyclerView.Adapter<BookAdapter.ViewHolder>() {

    private val context = activity

    private val viewModel = activity.viewModel

    inner class ViewHolder(view: View): RecyclerView.ViewHolder(view) {
        val bookName = view.findViewById<TextView>(R.id.bookName)
        val bookDeleteBtn = view.findViewById<Button>(R.id.bookDeleteBtn)
        val bookSetBtn = view.findViewById<Button>(R.id.bookSetBtn)
        val open = view.findViewById<Button>(R.id.open)
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.book_item, parent, false)
        val holder =  ViewHolder(view)
        holder.apply {
            bookDeleteBtn.setOnClickListener {
                AlertDialog.Builder(context).apply {
                    setMessage("删除这个文件？")
                    setCancelable(true)
                    setPositiveButton("确定") { dialog, which ->
                        val book = books[adapterPosition]
                        viewModel.deleteBookItem(book)
                    }
                    setNegativeButton("取消") { dialog, which -> }
                    show()
                }
            }
            bookSetBtn.setOnClickListener {
                val book = books[adapterPosition]
                val intent = Intent(context, BookDialogActivity::class.java)
                intent.putExtra("book_name", book.name)
                intent.putExtra("book_id", book.id)
                intent.putExtra("position", adapterPosition)
                context.setResultLauncher.launch(intent)
                //更改数据在onResult部分
            }
            itemView.setOnClickListener {
                val book = books[adapterPosition]
                val intent = Intent(context, WordActivity::class.java)
                intent.putExtra("book_id", book.id)
                intent.putExtra("book_name", book.name)
                intent.putExtra("like_or_not", viewModel.likeOrNot)
                context.startActivity(intent)
            }
            open.setOnClickListener {
                val book = books[adapterPosition]
                Log.d("BookAdapter", "open is click")
                val intent = Intent(context, MainActivity::class.java)
                intent.putExtra("book_id", book.id)
                intent.putExtra("like_or_not", viewModel.likeOrNot)
                intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK //清楚所有其他页面
                context.startActivity(intent)
                context.finish()
            }
        }
        return holder
    }

    override fun onBindViewHolder(
        holder: ViewHolder,
        position: Int
    ) {
        holder.bookName.text = books[position].name
    }

    override fun getItemCount() = books.size

    fun submitList(newBooks: List<WordPrefer>) {
        books = newBooks
        notifyDataSetChanged()
    }

}