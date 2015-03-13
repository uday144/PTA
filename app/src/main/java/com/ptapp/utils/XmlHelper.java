package com.ptapp.utils;

/**
 * Created by lifestyle on 30-01-15.
 */
public class XmlHelper {
    public static String encodeEntities(String content) {
        content = content.replace("&", "&amp;");
        content = content.replace("<", "&lt;");
        content = content.replace(">", "&gt;");
        content = content.replace("\"", "&quot;");
        content = content.replace("'", "&apos;");
        return content;
    }
}
