package com.cdv.upload.util;

import org.apache.commons.lang3.time.DateFormatUtils;

import java.lang.management.ManagementFactory;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.*;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.TimeZone;

/**
 * 时间工具类
 *
 * @author zhangjs
 */
public class DateUtils extends org.apache.commons.lang3.time.DateUtils {
    public static String YYYY = "yyyy";

    public static String YYYY_MM = "yyyy-MM";

    public static String YYYY_MM_DD = "yyyy-MM-dd";

    public static String YYYYMMDDHHMMSS = "yyyyMMddHHmmss";

    public static String YYYY_MM_DD_HH_MM_SS = "yyyy-MM-dd HH:mm:ss";

    private static String[] parsePatterns = {
            "yyyy-MM-dd", "yyyy-MM-dd HH:mm:ss", "yyyy-MM-dd HH:mm", "yyyy-MM",
            "yyyy/MM/dd", "yyyy/MM/dd HH:mm:ss", "yyyy/MM/dd HH:mm", "yyyy/MM",
            "yyyy.MM.dd", "yyyy.MM.dd HH:mm:ss", "yyyy.MM.dd HH:mm", "yyyy.MM"};
    public static final String ISO_8601 = "yyyy-MM-dd'T'HH:mm:ss.SSSXXX";
    public static final String COMMON_DATE = "EEE,d MMM yyyy HH:mm:ss 'GMT'";

    /**
     * 获取当前Date型日期
     *
     * @return Date() 当前日期
     */
    public static Date getNowDate() {
        return new Date();
    }

    /**
     * 获取当前日期, 默认格式为yyyy-MM-dd
     *
     * @return String
     */
    public static String getDate() {
        return dateTimeNow(YYYY_MM_DD);
    }

    public static final String getTime() {
        return dateTimeNow(YYYY_MM_DD_HH_MM_SS);
    }

    public static final String dateTimeNow() {
        return dateTimeNow(YYYYMMDDHHMMSS);
    }

    public static final String dateTimeNow(final String format) {
        return parseDateToStr(format, new Date());
    }

    public static final String dateTime(final Date date) {
        return parseDateToStr(YYYY_MM_DD, date);
    }

    public static final String parseDateToStr(final String format, final Date date) {
        return new SimpleDateFormat(format).format(date);
    }

    public static final Date dateTime(final String format, final String ts) {
        try {
            return new SimpleDateFormat(format).parse(ts);
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 日期路径 即年/月/日 如2018/08/08
     */
    public static final String datePath() {
        Date now = new Date();
        return DateFormatUtils.format(now, "yyyy/MM/dd");
    }

    /**
     * 日期路径 即年/月/日 如20180808
     */
    public static final String dateTime() {
        Date now = new Date();
        return DateFormatUtils.format(now, "yyyyMMdd");
    }

    /**
     * 日期型字符串转化为日期 格式
     */
    public static Date parseDate(Object str) {
        if (str == null) {
            return null;
        }
        try {
            return parseDate(str.toString(), parsePatterns);
        } catch (ParseException e) {
            return null;
        }
    }

    /**
     * 获取服务器启动时间
     */
    public static Date getServerStartDate() {
        long time = ManagementFactory.getRuntimeMXBean().getStartTime();
        return new Date(time);
    }

    /**
     * 计算相差天数
     */
    public static int differentDaysByMillisecond(Date date1, Date date2) {
        return Math.abs((int) ((date2.getTime() - date1.getTime()) / (1000 * 3600 * 24)));
    }

    /**
     * 计算两个时间差
     */
    public static String getDatePoor(Date endDate, Date nowDate) {
        long nd = 1000 * 24 * 60 * 60;
        long nh = 1000 * 60 * 60;
        long nm = 1000 * 60;
        // long ns = 1000;
        // 获得两个时间的毫秒时间差异
        long diff = endDate.getTime() - nowDate.getTime();
        // 计算差多少天
        long day = diff / nd;
        // 计算差多少小时
        long hour = diff % nd / nh;
        // 计算差多少分钟
        long min = diff % nd % nh / nm;
        // 计算差多少秒//输出结果
        // long sec = diff % nd % nh % nm / ns;
        return day + "天" + hour + "小时" + min + "分钟";
    }

    /**
     * 增加 LocalDateTime ==> Date
     */
    public static Date toDate(LocalDateTime temporalAccessor) {
        ZonedDateTime zdt = temporalAccessor.atZone(ZoneId.systemDefault());
        return Date.from(zdt.toInstant());
    }

    /**
     * 增加 LocalDate ==> Date
     */
    public static Date toDate(LocalDate temporalAccessor) {
        LocalDateTime localDateTime = LocalDateTime.of(temporalAccessor, LocalTime.of(0, 0, 0));
        ZonedDateTime zdt = localDateTime.atZone(ZoneId.systemDefault());
        return Date.from(zdt.toInstant());
    }

    /**
     * 帧数转时码,精确到秒
     *
     * @param frame 帧数
     * @return 形如:HH:mm:ss  精确到秒   (long型)
     */
    public static String frametosecond(long frame) {
        long l = frame;
        String str = "";
        long t = l % 25;
        l = l / 25;
        t = l % 60;
        l = l / 60;
        str = (t < 10 ? "0" + String.valueOf(t) : String.valueOf(t)); //秒数
        t = l % 60;
        l = l / 60;
        str = (t < 10 ? "0" + String.valueOf(t) : String.valueOf(t)) + ":" + str; //分钟
        str = (l < 10 ? "0" + String.valueOf(l) : String.valueOf(l)) + ":" + str; //小时
        return str;
    }

    /**
     * 时间戳转换时间戳日期格式字符串
     * @param dateLong 当前应用的时间戳
     * @param zone     Asia/Shanghai
     *                 UTC
     *                 GMT
     *                 GMT+08:00
     * @return
     */
    public static Map<String, Object> getTimeByZone(Long dateLong, String zone) {
        Date date = new Date(dateLong);
        Map<String, Object> map = new HashMap<>();
        SimpleDateFormat formatterISO8601 = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSXXX");
        formatterISO8601.setTimeZone(TimeZone.getTimeZone(zone));
        String formatISO8601 = formatterISO8601.format(date);
        map.put("formatISO8601", formatISO8601);
        SimpleDateFormat formatterGMT = new SimpleDateFormat("EEE,d MMM yyyy HH:mm:ss 'GMT'");
        formatterGMT.setTimeZone(TimeZone.getTimeZone(zone));
        String formatGMT = formatterGMT.format(date);
        map.put("formatGMT", formatGMT);
        return map;
    }

    /**
     * 日期格式字符串转换时间戳
     * @param dateStr 2022-06-28T07:36:11.359Z
     *                2022-06-28 15:36:11
     * @param format  yyyy-MM-dd'T'HH:mm:ss.SSSXXX(ISO8601)
     *                yyyy-MM-dd HH:mm:ss(GMT)
     * @param zone    Asia/Shanghai
     *                UTC
     *                GMT
     *                GMT+08:00
     * @return
     */
    public static Long StringToDateLong(String dateStr, String format, String zone) {
        SimpleDateFormat formatter = new SimpleDateFormat(format);
        formatter.setTimeZone(TimeZone.getTimeZone(zone));
        Date date = null;
        try {
            date = formatter.parse(dateStr);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return date.getTime();
    }
}
