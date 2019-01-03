package com.example.q.cs496_2.helpers

import android.content.Context
import androidx.appcompat.app.AlertDialog

class DialogHelper {
    fun makeYesOrNoDialog(context: Context, function: () -> Unit) {
        AlertDialog.Builder(context)
            .setTitle("Delete Image")
            .setMessage("Are you sure you want to delete this entry in your phone?")
            .setPositiveButton(android.R.string.yes) { dialog, which ->
                function()
                // TODO(@estanie): Adds snackbar or alert
            }
            .setNegativeButton(android.R.string.no) { dialog, which ->
            }
            .show()
    }
}