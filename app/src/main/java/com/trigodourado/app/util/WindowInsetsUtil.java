package com.trigodourado.app.util;

import android.app.Activity;
import android.view.View;

import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowCompat;
import androidx.core.view.WindowInsetsCompat;

public final class WindowInsetsUtil {
    private WindowInsetsUtil() {
    }

    public static void aplicarSafeArea(Activity activity, View root) {
        WindowCompat.setDecorFitsSystemWindows(activity.getWindow(), false);
        aplicarInsets(root, false);
    }

    public static void aplicarSafeArea(View root) {
        aplicarInsets(root, false);
    }

    public static void aplicarSafeAreaComTeclado(View root) {
        aplicarInsets(root, true);
    }

    private static void aplicarInsets(View root, boolean incluirTeclado) {
        int paddingLeft = root.getPaddingLeft();
        int paddingTop = root.getPaddingTop();
        int paddingRight = root.getPaddingRight();
        int paddingBottom = root.getPaddingBottom();

        ViewCompat.setOnApplyWindowInsetsListener(root, (view, windowInsets) -> {
            Insets systemBars = windowInsets.getInsets(
                    WindowInsetsCompat.Type.systemBars() | WindowInsetsCompat.Type.displayCutout());
            int bottom = systemBars.bottom;
            if (incluirTeclado) {
                bottom = Math.max(bottom,
                        windowInsets.getInsets(WindowInsetsCompat.Type.ime()).bottom);
            }
            view.setPadding(
                    paddingLeft + systemBars.left,
                    paddingTop + systemBars.top,
                    paddingRight + systemBars.right,
                    paddingBottom + bottom
            );
            return windowInsets;
        });
        ViewCompat.requestApplyInsets(root);
    }
}
