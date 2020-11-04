package com.krenog.myf.user.services.authentication.codegenerator;

import org.apache.commons.lang3.RandomStringUtils;

public class RandomSmsCodeGenerator {
    private RandomSmsCodeGenerator() {
    }

    public static String generateCode(int length) {
        return RandomStringUtils.randomNumeric(length);
    }

}
