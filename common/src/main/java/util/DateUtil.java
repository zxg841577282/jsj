package util;

import java.util.Calendar;
import java.util.Date;

/**
 * @Author: zhou_xg
 * @Date: 2020/3/12 15:15
 * @Purpose:
 */

public class DateUtil {

    /**
     * 获取YYYY/MM/DD格式日期
     * @param date
     * @return
     */
    public static String getDate(Date date){
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        String month = "";
        int a = (cal.get(Calendar.MONTH)+1);
        if ((cal.get(Calendar.MONTH)+1)<10){
            month = "0" + a;
        }else {
            month = a + "";
        }
        return cal.get(Calendar.YEAR)+"/"+month+"/"+cal.get(Calendar.DATE);
    }

    /**
     * 获取HH:MM:SS格式时间
     * @param date
     * @return
     */
    public static String getTime(Date date){
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);

        int i = cal.get(Calendar.MINUTE);
        String a = i + "";
        if (i<10){
            a = "0" + i;
        }

        return cal.get(Calendar.HOUR_OF_DAY)+":"+ a +":"+cal.get(Calendar.SECOND);
    }

    /**
     * 获取MMDD格式日期
     * @param date
     * @return
     */
    public static String getMMDDDate(Date date){
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        String month = "";
        int a = (cal.get(Calendar.MONTH)+1);
        if ((cal.get(Calendar.MONTH)+1)<10){
            month = "0" + a;
        }else {
            month = a + "";
        }
        return month+cal.get(Calendar.DATE);
    }

}
