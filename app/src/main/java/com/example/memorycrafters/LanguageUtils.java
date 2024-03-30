package com.example.memorycrafters;
import android.content.Context;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Build;

import java.util.Locale;

public class LanguageUtils {

    public static void changeLanguage(Context context, String languageCode) {
        Locale newLocale = new Locale(languageCode);
        Locale.setDefault(newLocale);

        Resources resources = context.getResources();
        Configuration configuration = resources.getConfiguration();

        configuration.setLocale(newLocale);

        resources.updateConfiguration(configuration, resources.getDisplayMetrics());
    }
}
