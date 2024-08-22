package com.polsat.visualskript.util;

import com.polsat.visualskript.gui.manager.notification.DialogAlert;

public class ErrorHandler {
    public static void alert(String error) {
        DialogAlert.alertError(error, "The program encountered an unusual error.");
    }
}
