package sample.crypto.bux.com.crypto.utils

import android.app.Activity
import android.support.v4.app.Fragment
import android.widget.Toast

fun Activity.showShortToast(message: String) = Toast.makeText(this, message, Toast.LENGTH_SHORT).show()

fun Activity.showShortToast(message: Int) = Toast.makeText(this, message, Toast.LENGTH_SHORT).show()

fun Fragment.showShortToast(message: String) = Toast.makeText(context, message, Toast.LENGTH_SHORT).show()

fun Fragment.showShortToast(message: Int) = Toast.makeText(context, message, Toast.LENGTH_SHORT).show()