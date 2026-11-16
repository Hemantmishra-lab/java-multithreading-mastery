package core.java.concurrency;

import java.text.MessageFormat;
import java.util.Locale;
import java.util.ResourceBundle;

/**
 * Demonstrates Java Localization (i18n / l10n):
 * 1. Reading and setting the system/application Locale using Locale objects.
 * 2. Loading locale-specific ResourceBundles (.properties files).
 * 3. ResourceBundle fallback mechanism when a specific locale is unavailable.
 */
public class Locale_And_ResourceBundle_Demo {

    public static void main(String[] args) {
        System.out.println("=== Java Localization (Locale & ResourceBundle) Demo ===\n");

        // 1. Read the current default JVM Locale
        Locale initialDefault = Locale.getDefault();
        System.out.println("Initial System Default Locale: " + initialDefault
                + " (" + initialDefault.getDisplayName() + ")\n");

        // 2. Create specific Locale objects (Java 19+ Locale.of() replaces deprecated constructors)
        Locale usLocale = Locale.of("en", "US");
        Locale franceLocale = Locale.of("fr", "FR");
        Locale indiaHindiLocale = Locale.of("hi", "IN");
        Locale unsupportedLocale = Locale.of("ja", "JP"); // Will fall back to default

        // 3. Load and display messages for each Locale
        displayLocalizedMessages(usLocale);
        displayLocalizedMessages(franceLocale);
        displayLocalizedMessages(indiaHindiLocale);

        // 4. Demonstrate changing the JVM default Locale dynamically
        System.out.println("--- Changing Default Locale to France ---");
        Locale.setDefault(franceLocale);
        System.out.println("Updated Default Locale: " + Locale.getDefault());

        // 5. Demonstrate Fallback behavior (ja_JP is not defined -> falls back to default Locale)
        System.out.println("\n--- Testing Fallback for Unsupported Locale (ja_JP) ---");
        displayLocalizedMessages(unsupportedLocale);

        // Restore original default locale
        Locale.setDefault(initialDefault);
    }

    private static void displayLocalizedMessages(Locale targetLocale) {
        // Loads Messages_<lang>_<COUNTRY>.properties from the classpath root (core/)
        ResourceBundle bundle = ResourceBundle.getBundle("Messages", targetLocale);

        System.out.println("Locale: " + targetLocale.getDisplayName() + " [" + targetLocale + "]");
        System.out.println("  -> " + bundle.getString("greeting"));
        System.out.println("  -> " + bundle.getString("currency.label"));
        System.out.println("  -> " + bundle.getString("farewell"));
        System.out.println();
    }
}