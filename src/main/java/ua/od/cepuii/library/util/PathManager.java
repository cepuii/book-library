package ua.od.cepuii.library.util;

import java.util.ResourceBundle;

public class PathManager {

    private static final ResourceBundle resourceBundle = ResourceBundle.getBundle("path");

    private PathManager() {
    }

    public static String getProperty(String key) {
        return resourceBundle.getString(key);
    }
}
