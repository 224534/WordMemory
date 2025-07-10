package com.sunnyweather.wordmemory.ui.person

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.sunnyweather.wordmemory.R
import com.sunnyweather.wordmemory.logic.model.WordPrefer

class BookAdapter(private val activity: BookActivity, private var books: List<WordPrefer>) :
    RecyclerView.Adapter<BookAdapter.ViewHolder>() {

    private val context = activity

    private val viewModel = activity.viewModel

    inner class ViewHolder(view: View): RecyclerView.ViewHolder(view) {
        val bookName = view.findViewById<TextView>(R.id.bookName)
        val bookDeleteBtn = view.findViewById<Button>(R.id.bookDeleteBtn)
        val bookSetBtn = view.findViewById<Button>(R.id.bookSetBtn)
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.book_item, parent, false)
        val holder = ViewHolder(view)
        val position = holder.adapterPosition
        val book = books[position]
        holder.apply {
            bookDeleteBtn.setOnClickListener {
                viewModel.deleteBookItem(book)
                books = viewModel.getBooks() //更改数据
                notifyDataSetChanged()
            }
            bookSetBtn.setOnClickListener {
                val intent = Intent(context, BookDialogActivity::class.java)
                intent.putExtra("book_name", book.name)
                intent.putExtra("book_id", book.id)
                context.startActivityForResult(intent, 1)
                books = viewModel.getBooks() //更改数据
                notifyDataSetChanged()
            }
            itemView.setOnClickListener {
                val intent = Intent(context, WordActivity::class.java)
                intent.putExtra("book_id", book.id)
                intent.putExtra("book_name", book.name)
                context.startActivity(intent)
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

}