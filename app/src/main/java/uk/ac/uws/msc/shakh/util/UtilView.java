package uk.ac.uws.msc.shakh.util;

import android.view.View;

public class UtilView {
    public static void showProgress(View view) {
        view.setVisibility(View.VISIBLE);
    }

    public static void hidProgress(View view) {
        view.setVisibility(View.GONE);
    }
}
