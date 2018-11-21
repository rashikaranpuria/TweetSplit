package com.rashikaranpuria.tweetsplit.ui.base

import android.annotation.SuppressLint
import android.app.ProgressDialog
import android.support.v7.app.AppCompatActivity
import org.jetbrains.anko.toast

open class BaseActivity: AppCompatActivity(), IBaseView {
    override fun showError(errorId: Int) {
        toast(errorId)
    }

    override fun showMessage(msg_id: Int) {
        toast(msg_id)
    }

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