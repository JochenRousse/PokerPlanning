package fr.enssat.pokerplanning.carfantan_Ortiz_Rousse

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView


class MessageViewHolder(val view: View) : RecyclerView.ViewHolder(view) {
    val voteView = view.findViewById<TextView>(R.id.vote)

    fun setVote(msg: String) {
        voteView.text = msg
    }
}

class MessageAdapter : RecyclerView.Adapter<MessageViewHolder>() {

    var list: List<String> = emptyList()
        set(l) {
            field = l
            notifyDataSetChanged()
        }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MessageViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val view = inflater.inflate(R.layout.list_item_vote, parent, false)
        return MessageViewHolder(view)
    }

    override fun onBindViewHolder(holder: MessageViewHolder, position: Int) {
        holder.setVote(list[position])
    }
}