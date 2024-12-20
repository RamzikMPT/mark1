package com.example.mark1.utils;

import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

public class DateUtils {
    public static String formatDate(LocalDateTime localDateTime) {
        if (localDateTime == null) {
            return ""; // Возврат пустой строки, если дата null
        }
        SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy HH:mm");
        return formatter.format(Date.from(localDateTime.atZone(ZoneId.systemDefault()).toInstant()));
    }
}