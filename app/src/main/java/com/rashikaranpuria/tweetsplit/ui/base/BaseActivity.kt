package com.rashikaranpuria.tweetsplit.ui.base

import android.annotation.SuppressLint
import android.app.ProgressDialog
import android.support.v7.app.AppCompatActivity
import org.jetbrains.anko.toast

@SuppressLint("Registered")
open class BaseActivity: AppCompatActivity(), IBaseView {

    open lateinit var mProgressDialog: ProgressDialog

    override fun showProgressBar() {
        mProgressDialog.show()
    }

    override fun hideProgressDialog() {
        mProgressDialog.hide()
    }

    override fun showError(errorStr: String) {
        toast(errorStr)
    }
}