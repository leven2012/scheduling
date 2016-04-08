/*
 * ################################################################
 *
 * Date tools class
 *
 * ################################################################
 * $$ACTIVEEON_CONTRIBUTOR$$
 */
package org.ow2.proactive.scheduler.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;


public class DateUtil {

    private final static String FORMAT_DATE_001="yyyy-MM-dd";

    public static final String nextDate(String dateStr) {
        Date date = formatDate(dateStr,FORMAT_DATE_001);
        Calendar calendar = new GregorianCalendar();
        calendar.setTime(date);
        calendar.add(calendar.DATE,1);
        date=calendar.getTime();
        return formatStr(date);
    }

    public static final Date formatDate(String dateStr,String format){
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        Date date = null;
        try {
            date = sdf.parse(dateStr);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return date;
    }

    public static  final String formatStr(Date date){
        SimpleDateFormat sdf=new SimpleDateFormat(FORMAT_DATE_001);
        String str=sdf.format(date);
        return str;
    }

}