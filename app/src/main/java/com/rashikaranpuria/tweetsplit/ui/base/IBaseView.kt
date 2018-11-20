package com.rashikaranpuria.tweetsplit.ui.base

interface IBaseView {
    fun showProgressBar()
    fun hideProgressDialog()
    fun showError(errorStr: String)
}