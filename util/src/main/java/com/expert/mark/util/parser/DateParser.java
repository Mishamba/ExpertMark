package com.expert.mark.util.parser;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DateParser {
    // TODO change format to extended json
    private static final DateFormat dateFormat = new SimpleDateFormat("dd MMMM yyyy zzzz");

    public static String parseToString(Date date) {
        return dateFormat.format(date);
    }

    public static Date parseToDate(String date) {
        try {
            return dateFormat.parse(date);
        } catch (ParseException e) {
            e.printStackTrace();
            throw new IllegalArgumentException("Wrong date format");
        }
    }
}
