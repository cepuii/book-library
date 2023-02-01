package ua.od.cepuii.library.resource;

import java.util.Locale;
import java.util.ResourceBundle;

public class MessageManager {

    //TODO delete messages and this class, send only key, on jsp bundle load necessary message from text.properties in chosen locale

    private static ResourceBundle resourceBundle = ResourceBundle.getBundle("messages");

    private MessageManager() {
    }

    public static String getProperty(String key) {
        return resourceBundle.getString(key);
    }

    public static void setResourceBundleLocale(Locale locale) {
        resourceBundle = ResourceBundle.getBundle("messages", locale);
    }
}
