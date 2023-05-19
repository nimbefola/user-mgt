package com.pentspace.accountmgtservice.serviceUtil;


public class StringUtil {
    public static boolean isNotBlank(String value) {
        if(value.isEmpty()){
            return false;
        }
        return true;
    }
    public static boolean isBlank(String value) {
        if( value.isEmpty()){
            return true;
        }
        return false;
    }

    //Suppose to test for
}
