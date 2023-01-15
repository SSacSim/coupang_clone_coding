package com.example.demo.utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ValidationRegex {
    public static boolean isRegexEmail(String target) {
        String regex = "^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$";
        Pattern pattern = Pattern.compile(regex, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(target);
        return matcher.find();
    }


    public static boolean isRegexPhone(String target) {
        String regex = "^[0-9]+$";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(target);
        return matcher.find();
    }

    public static boolean isRegexBlank(String target) {
        String regex = "(\\s)";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(target);
        return matcher.find();
    }

    public static boolean isRegexSameword(String target) {
        String regex = "(\\w)\\1\\1";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(target);
        return matcher.find();
    }

    public static boolean isRegexKoreanWord(String target) {
        String regex = "^[가-힣a-zA-Z]+$";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(target);
        return matcher.find();
    }

    public static boolean isRegexInvalidationWord(String target) {
        String regex = "[÷× ]";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(target);
        return matcher.find();
    }

    public static boolean isRegexImg(String target) {
        String regex = "(\\.jpg|\\.JPG|\\.jpeg|\\.JPEG|\\.png|\\.PNG)$";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(target);
        return matcher.find();
    }


    public static boolean isRegexcontinuity(String target){
        int strLen = target.length();
        // ASCII Char를 담을 배열 선언
        int[] tmpArray = new int[strLen];

        // Make Array
        for (int i = 0; i < strLen; i++) {
            tmpArray[i] = target.charAt(i);
        }

        // Validation Array
        for (int i = 0; i < strLen - 2; i++) {
            // 첫 글자 A-Z / 0-9
            if ((tmpArray[i] > 47
                && tmpArray[i + 2] < 58)
                || (tmpArray[i] > 64
                && tmpArray[i + 2] < 91)
                ||(tmpArray[i] > 96
                && tmpArray[i + 2] < 123)) {
            // 배열의 연속된 수 검사
            // 3번째 글자 - 2번째 글자 = 1, 3번째 글자 - 1번째 글자 = 2
            if (Math.abs(tmpArray[i + 2] - tmpArray[i + 1]) == 1
                && Math.abs(tmpArray[i + 2] - tmpArray[i]) == 2) {
                char c1 = (char) tmpArray[i];
                char c2 = (char) tmpArray[i + 1];
                char c3 = (char) tmpArray[i + 2];
                return true;
                }
            }
        }
        // Validation Complete
        return false;
    }
}

