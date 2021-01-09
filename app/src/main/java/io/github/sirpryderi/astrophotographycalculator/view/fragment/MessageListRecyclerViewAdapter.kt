package io.github.sirpryderi.astrophotographycalculator.view.fragment

import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import io.github.sirpryderi.astrophotographycalculator.R

import io.github.sirpryderi.astrophotographycalculator.dummy.DummyContent.DummyItem
import io.github.sirpryderi.astrophotographycalculator.model.Message

/**
 * [RecyclerView.Adapter] that can display a [Message].
 */
class MessageListRecyclerViewAdapter(private val values: List<Message>)
    : RecyclerView.Adapter<MessageListRecyclerViewAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.fragment_message_card, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = values[position]
        holder.iconView.setImageResource(item.icon)
        holder.contentView.text = item.message
    }

    override fun getItemCount(): Int = values.size

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val iconView: ImageView = view.findViewById(R.id.icon)
        val contentView: TextView = view.findViewById(R.id.content)

        override fun toString(): String {
            return super.toString() + " '" + contentView.text + "'"
        }
    }
}