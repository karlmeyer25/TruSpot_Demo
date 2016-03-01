package com.truspot.backend.util;

import java.util.regex.Pattern;

/**
 * Created by yavoryordanov on 2/25/16.
 */
public class ValidateUtil {

    /**
     * Throws Exception if it's not valid.
     * */
    public static void checkNotNull(Object obj) throws Exception {
        if (obj == null) {
            throw new Exception("Object is null!");
        }
    }

    /**
     * Throws Exception if it's not valid.
     * */
    public static void checkNotNull(Object... objs) throws Exception {
        for (Object obj : objs) {
            checkNotNull(obj);
        }
    }


    /**
     * Throws Exception if it's not valid.
     * */
    public static void checkNotEmpty(String str) throws Exception {
        if (Util.isStringEmpty(str)) {
            throw new Exception("String is empty!");
        }
    }

    /**
     * Throws Exception if it's not valid.
     * */
    public static void checkNotEmpty(String... strings) throws Exception {
        for (String str : strings) {
            checkNotEmpty(str);
        }
    }

    /**
     * Throws Exception if it's not valid.
     * */
    public static void checkNumberIsPositive(long number) throws Exception {
        if (number <= 0) {
            throw new Exception("Number is not positive!");
        }
    }

    /**
     * Throws Exception if it's not valid.
     * */
    public static void checkNumbersArePositive(long... numbers) throws Exception {
        for (long num : numbers) {
            checkNumberIsPositive(num);
        }
    }

    /**
     * Throws Exception if it's not valid.
     * */
    public static void checkUsername(String username) throws Exception {
        if (!Pattern.matches("^[a-z0-9_-]{6,15}$", username)) {
            throw new Exception("UID must contain only lowercase, numbers, _, - and must be with length between 6 and 15!");
        }
    }

    /**
     * Throws Exception if it's not valid.
     * */
    public static void checkPassword(String password) throws Exception {
        if (!Pattern.matches("((?=.*\\d)(?=.*[a-z])(?=.*[A-Z]).{6,20})", password)) {
            throw new Exception("Password must contains one digit from 0-9, one lowercase characters, one uppercase characters and has length at least 6 characters and maximum of 20!");
        }
    }

    /**
     * Throws Exception if it's not valid.
     * */
    public static void checkEmail(String email) throws Exception {
        if (!Pattern.matches("^[_A-Za-z0-9-]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$", email)) {
            throw new Exception("Email is not valid! ");
        }
    }

    public static void checkColorCode(String colorCode) throws Exception {
        if (!Pattern.matches("^#[a-zA-z0-9]{6}$", colorCode)) {
            throw new Exception("Color code is not valid! ");
        }
    }
}