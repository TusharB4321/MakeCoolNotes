package com.example.makecoolnotes.utils

import android.accessibilityservice.InputMethod
import android.content.Context.INPUT_METHOD_SERVICE
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.view.inputmethod.InputMethodManager.HIDE_NOT_ALWAYS

fun View.hideKeyBoard()=(context.getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager)
 .hideSoftInputFromWindow(windowToken,HIDE_NOT_ALWAYS)