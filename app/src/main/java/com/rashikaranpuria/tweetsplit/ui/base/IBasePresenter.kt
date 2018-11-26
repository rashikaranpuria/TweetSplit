package com.rashikaranpuria.tweetsplit.ui.base

interface IBasePresenter<V : IBaseView> {
    fun onAttach(v: V)

    fun onDetach()
}