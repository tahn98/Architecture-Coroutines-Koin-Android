package com.sg.base.utils

//object AppEvent {
//
//    // Popup
//    private var popupEventListeners: Set<PopupEventListener> = CopyOnWriteArraySet()
//
//    fun addPopupEventListener(listener: PopupEventListener?) {
//        if (listener != null)
//            popupEventListeners = popupEventListeners.plus(listener)
//    }
//
//    fun unRegisterPopupEventListener(listener: PopupEventListener?) {
//        popupEventListeners = popupEventListeners.minus(listener ?: return)
//    }
//
//    fun notifyShowPopUp(popup: PopUp? = null) {
//        for (listener in popupEventListeners)
//            listener.onShowPopup(popup)
//    }
//
//    fun notifyClosePopUp() {
//        for (listener in popupEventListeners)
//            listener.onClosePopup()
//    }
//    //end
//
//}
//
//
//interface PopupEventListener {
//    fun onShowPopup(popup: PopUp?)
//    fun onClosePopup()
//}
