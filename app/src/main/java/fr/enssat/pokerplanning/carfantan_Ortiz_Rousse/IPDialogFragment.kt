package fr.enssat.pokerplanning.carfantan_Ortiz_Rousse

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import kotlinx.android.synthetic.main.dialog_ip.view.*


class IPDialogFragment : DialogFragment() {
    private lateinit var listener: NoticeDialogListener

    interface NoticeDialogListener {
        fun onDialogPositiveClick(dialog: DialogFragment, ip: String)
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
            val dialogView = inflater.inflate(R.layout.dialog_ip, null)

            builder.setView(dialogView)
                .setPositiveButton(
                    "Join"
                ) { _, _ ->
                    listener.onDialogPositiveClick(
                        this,
                        dialogView.ipAddressEditText.text.toString()
                    )
                }
                .setNegativeButton(
                    "Cancel"
                ) { _, _ ->
                    listener.onDialogNegativeClick(this)
                    dialog?.cancel()
                }
            builder.create()
        } ?: throw IllegalStateException("Activity cannot be null")
    }
}