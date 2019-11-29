package fr.enssat.pokerplanning.carfantan_Ortiz_Rousse

import android.app.Dialog
import android.content.Context
import android.content.DialogInterface
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import kotlinx.android.synthetic.main.dialog_vote.view.*
import java.util.*


class VoteDialogFragment : DialogFragment() {
    private lateinit var listener: NoticeDialogListener

    interface NoticeDialogListener {
        fun onDialogPositiveClick(dialog: DialogFragment, vote: String)
        fun onDialogNegativeClick(dialog: DialogFragment)
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        try {
            listener = context as NoticeDialogListener
        } catch (e: ClassCastException) {
            throw ClassCastException(
                (context.toString() +
                        " must implement NoticeDialogListener")
            )
        }
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return activity?.let {
            val builder = AlertDialog.Builder(it)
            // Get the layout inflater
            val inflater = requireActivity().layoutInflater
            val dialogView = inflater.inflate(R.layout.dialog_vote, null)
            val votesValue: ArrayList<Button> = arrayListOf(
                dialogView.findViewById(R.id.button1),
                dialogView.findViewById(R.id.button2),
                dialogView.findViewById(R.id.button3),
                dialogView.findViewById(R.id.button4),
                dialogView.findViewById(R.id.button5),
                dialogView.findViewById(R.id.button6),
                dialogView.findViewById(R.id.button7),
                dialogView.findViewById(R.id.button8),
                dialogView.findViewById(R.id.button9),
                dialogView.findViewById(R.id.button10),
                dialogView.findViewById(R.id.button11),
                dialogView.findViewById(R.id.button12)
            )

            for (i in 0 until 12) {
                votesValue.get(i).setOnClickListener {
                    dialogView.currentVoteTextView.text = votesValue.get(i).text.toString()
                }
            }

            builder.setView(dialogView)
                .setPositiveButton("Vote",
                    DialogInterface.OnClickListener { dialog, _ ->
                        listener.onDialogPositiveClick(
                            this,
                            dialogView.currentVoteTextView.text.toString()
                        )
                    })
                .setNegativeButton("Cancel",
                    DialogInterface.OnClickListener { dialog, _ ->
                        listener.onDialogNegativeClick(this)
                        getDialog()?.cancel()
                    })
            builder.create()
        } ?: throw IllegalStateException("Activity cannot be null")
    }
}