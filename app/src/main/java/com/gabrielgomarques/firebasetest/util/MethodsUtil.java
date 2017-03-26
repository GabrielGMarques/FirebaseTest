package com.gabrielgomarques.firebasetest.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Gabriel on 16/01/2017.
 */

public class MethodsUtil {

    public static String verifyIsNotNull(Object object){
        return object!=null?object.toString():null;
    }
    public static String matchesPattern(String pattern,String sentence) {
        Pattern p = Pattern.compile(pattern);
        Matcher m = p.matcher(sentence);

        if (m.find()) {
            return m.group();
        }

        return null;
    }

}
