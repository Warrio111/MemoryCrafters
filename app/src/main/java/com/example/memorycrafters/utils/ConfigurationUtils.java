package com.example.memorycrafters.utils;
import android.annotation.SuppressLint;
import android.content.Context;
import com.example.memorycrafters.R;
import com.firebase.ui.auth.AuthUI;
import com.google.firebase.auth.ActionCodeSettings;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
@SuppressLint("RestrictedApi")
public final class ConfigurationUtils {
    private ConfigurationUtils() {
        throw new AssertionError("No instance for you!");
    }
    public static boolean isGoogleMisconfigured(@NonNull Context context) {
        return AuthUI.UNCONFIGURED_CONFIG_VALUE.equals(
                context.getString(com.firebase.ui.auth.R.string.default_web_client_id));
    }
    @NonNull
    public static List<AuthUI.IdpConfig> getConfiguredProviders(@NonNull Context context) {
        List<AuthUI.IdpConfig> providers = new ArrayList<>();

        if (!isGoogleMisconfigured(context)) {
            providers.add(new AuthUI.IdpConfig.GoogleBuilder().build());
        }

        ActionCodeSettings actionCodeSettings = ActionCodeSettings.newBuilder()
                .setAndroidPackageName("com.example.memorycrafters", true, null)
                .setHandleCodeInApp(true)
                .setUrl("https://google.com")
                .build();

        providers.add(new AuthUI.IdpConfig.EmailBuilder()
                .setAllowNewAccounts(true)
                .enableEmailLinkSignIn()
                .setActionCodeSettings(actionCodeSettings)
                .build());

        return providers;
    }
}
