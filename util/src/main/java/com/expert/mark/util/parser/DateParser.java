package com.expert.mark.util.parser;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DateParser {
    private static final DateFormat dateFormat = new SimpleDateFormat("dd, MMMM, yyyy");

    public static String parseToString(Date date) {
        return dateFormat.format(date);
    }

    public static Date parseToDate(String date) {
        try {
            return (date == null) ? null : dateFormat.parse(date);
        } catch (ParseException e) {
            e.printStackTrace();
            throw new IllegalArgumentException("Wrong date format");
        }
    }
}
