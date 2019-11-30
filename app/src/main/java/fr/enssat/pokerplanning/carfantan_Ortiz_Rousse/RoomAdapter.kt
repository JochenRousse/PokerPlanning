package fr.enssat.pokerplanning.carfantan_Ortiz_Rousse

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView


class RoomViewHolder(view: View) : RecyclerView.ViewHolder(view) {
    val roomView = view.findViewById<TextView>(R.id.room)

    fun setRoom(room: String, listener: (String, Boolean) -> Unit) {
        roomView.text = room
        roomView.setOnClickListener {
            listener(room, false)
        }
    }
}

class RoomAdapter(private val listener: (String, Boolean) -> Unit) : RecyclerView.Adapter<RoomViewHolder>() {

    var list: List<String> = emptyList()
        set(l) {
            field = l
            notifyDataSetChanged()
        }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RoomViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val view = inflater.inflate(R.layout.list_item_room, parent, false)
        return RoomViewHolder(view)
    }

    override fun onBindViewHolder(holder: RoomViewHolder, position: Int) {
        holder.setRoom(list[position], listener)
    }
}