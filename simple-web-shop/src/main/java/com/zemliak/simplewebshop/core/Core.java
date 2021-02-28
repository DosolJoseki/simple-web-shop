package com.zemliak.simplewebshop.core;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Core {
    private Core(){};

    public static String convertToFormattedString(LocalDateTime dateToConvert) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS");
        return dateToConvert.format(formatter);
    }
}
