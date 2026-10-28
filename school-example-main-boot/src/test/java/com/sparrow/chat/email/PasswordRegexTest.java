package com.sparrow.chat.email;

import com.sparrow.constant.Regex;
import com.sparrow.utility.RegexUtility;

import java.util.regex.Pattern;

public class PasswordRegexTest {
    public static void main(String[] args) {
        Boolean match = Pattern.compile(Regex.PASSWORD, Regex.OPTION_MULTILINE).matcher("111111").matches();
        System.out.println(match);
        match = RegexUtility.matches("111111", "^[\\w!@#$%^&*()_]{6,20}$");
        System.out.println(match);
    }
}
