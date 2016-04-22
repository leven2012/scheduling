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

    public static final String addDate(String dateStr,int days) {
        Date date = formatDate(dateStr,FORMAT_DATE_001);
        Calendar calendar = new GregorianCalendar();
        calendar.setTime(date);
        calendar.add(calendar.DATE,days);
        date=calendar.getTime();
        return formatStr(date,FORMAT_DATE_001);
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

    public static final String formatStr(Date date,String format){
        SimpleDateFormat sdf=new SimpleDateFormat(format);
        String str=sdf.format(date);
        return str;
    }

    public static final String getCurrentDay(){
        SimpleDateFormat sdf=new SimpleDateFormat(FORMAT_DATE_001);
        String str=sdf.format(new Date());
        return str;
    }

    public static final String getCurrentDay(String format){
        SimpleDateFormat sdf=new SimpleDateFormat(format);
        String str=sdf.format(new Date());
        return str;
    }
}