package com.expert.mark.util.parser;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DateParser {
    private static final DateFormat dateFormatWithMinutes = new SimpleDateFormat("HH:mm dd, MMMM, yyyy");

    public static String parseToString(Date date) {
        return dateFormatWithMinutes.format(date);
    }

    public static Date parseToDateWithMinutes(String date) {
        try {
            return (date == null) ? null : dateFormatWithMinutes.parse(date);
        } catch (ParseException e) {
            e.printStackTrace();
            throw new IllegalArgumentException("Wrong date format");
        }
    }
}
