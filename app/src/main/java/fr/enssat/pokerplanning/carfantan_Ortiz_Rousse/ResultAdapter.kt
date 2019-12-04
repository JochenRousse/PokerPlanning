package fr.enssat.pokerplanning.carfantan_Ortiz_Rousse

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView


class ResultViewHolder(val view: View) : RecyclerView.ViewHolder(view) {
    val noteView = view.findViewById<TextView>(R.id.voteResult)
    val voterView = view.findViewById<TextView>(R.id.voter)

    fun setVotant(votant: Votant) {
        noteView.text = noteView.getContext().getString(R.string.note, votant.note)
        voterView.text = voterView.getContext().getString(R.string.voter, votant.name)
    }
}

class ResultAdapter :
    RecyclerView.Adapter<ResultViewHolder>() {

    var list: List<Votant> = emptyList()
        set(l) {
            field = l
            notifyDataSetChanged()
        }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ResultViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val view = inflater.inflate(R.layout.list_item_vote_result, parent, false)
        return ResultViewHolder(view)
    }

    override fun onBindViewHolder(holder: ResultViewHolder, position: Int) {
        holder.setVotant(list[position])
    }
}