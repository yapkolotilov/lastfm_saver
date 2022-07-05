package me.kolotilov.lastfm_saver.ui.artist_albums

import android.app.Dialog
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import me.kolotilov.lastfm_saver.R

/**
 * Storage permission not granted screen.
 */
class StoragePermissionNotGrantedFragment : DialogFragment() {

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return AlertDialog.Builder(requireContext())
            .setTitle(R.string.dialog_title_permission_not_granted)
            .setMessage(R.string.dialog_message_permission_not_granted)
            .create()
    }
}