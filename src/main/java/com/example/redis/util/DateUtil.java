package com.example.redis.util;

import java.time.DayOfWeek;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.Period;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAdjusters;
import java.util.Calendar;
import java.util.Date;

public class DateUtil {

    public static final String YYYY = "yyyy";
    public final static String MM = "MM";
    public final static String DD = "dd";
    public final static String YYYY_MM_DD = "yyyy-MM-dd";
    public final static String MM_DD = "MM月dd日";
    public final static String MM_DD_1 = "MM.dd";
    public final static String YYYY_MM = "yyyy-MM";
    public final static String HH_MM_SS = "HH:mm:ss";
    public final static String HH_MM = "HH:mm";
    public final static String YYYY_MM_DD_HH_MM_SS = "yyyy-MM-dd HH:mm:ss";
    private static final ZoneId CHINA_ZONE = ZoneId.systemDefault();


    /**
     * 将毫秒数转换为日期格式
     * @param dateTime 毫秒数
     * @return 日期格式
     */
    public static String long2Str(Long dateTime) {
        Date date = new Date(dateTime);
        return date2StrByPattern(date, YYYY_MM_DD_HH_MM_SS);
    }

    /**
     * 格式化日期
     *
     * @param dateTime 日期
     */
    public static String date2StrByPattern(Date dateTime, String pattern) {
        Instant instantDate = dateTime.toInstant();
        LocalDateTime localDate = instantDate.atZone(CHINA_ZONE).toLocalDateTime();
        return localDate.format(DateTimeFormatter.ofPattern(pattern));
    }


    /**
     * 格式化日期
     *
     * @param localDateTime 日期
     */
    public static String localDate2StrByPattern(LocalDateTime localDateTime, String pattern) {
        return localDateTime.format(DateTimeFormatter.ofPattern(pattern));
    }


    /**
     * DateTime对象转换为LocalDateTime对象
     *
     * @param date 转换对象
     * @return 日期对象
     */
    public static LocalDateTime date2LocalDateTime(Date date) {
        return LocalDateTime.ofInstant(date.toInstant(), ZoneId.of(ZoneId.SHORT_IDS.get("???")));
    }


    /**
     * 日期时间对象转换为日期对象
     *
     * @param localDateTime 日期时间对象
     * @return 日期对象
     */
    public static LocalDate localDateTime2LocalDate(LocalDateTime localDateTime) {
        return localDateTime.toLocalDate();

    }

    /**
     * 日期对象转换为日期对象
     *
     * @param localDate 日期对象
     * @return 日期时间对象
     */
    public static LocalDateTime localDate2LocalDateTime(LocalDate localDate) {
        return LocalDateTime.of(localDate, LocalTime.NOON);
    }


    /**
     * 字符串转换为日期
     *
     * @param strDate 字符串日期
     * @return 日期对象 yyyy-mm-dd
     */
    public static Date str2Date(String strDate) {
        LocalDate localDate = LocalDate.parse(strDate, DateTimeFormatter.ISO_DATE);
        Instant instant = localDate2LocalDateTime(localDate).atZone(CHINA_ZONE).toInstant();
        return Date.from(instant);
    }

    /**
     * 字符串转换为日期时间对象
     *
     * @param strDateTime 字符串日期
     * @return 日期对象 yyyy-mm-dd
     */
    public static Date str2DateTime(String strDateTime) {
        LocalDateTime localDateTime = LocalDateTime.parse(strDateTime, DateTimeFormatter.ofPattern(YYYY_MM_DD_HH_MM_SS));
        Instant instant = localDateTime.atZone(CHINA_ZONE).toInstant();
        return Date.from(instant);
    }

    /**
     * 字符串转换为日期
     *
     * @param strDate 字符串日期
     * @return 日期对象 yyyy-mm-dd
     */
    public static LocalDate str2LocalDate(String strDate) {
        return LocalDate.parse(strDate, DateTimeFormatter.ISO_DATE);
    }


    /**
     * 日期对象转换为字符串
     *
     * @param localDate 日期对象
     * @return 日期字符串 yyyy-mm-dd
     */
    public static String localDate2Str(LocalDate localDate) {
        return localDate.format(DateTimeFormatter.ISO_DATE);
    }


    /**
     * 日期时间对象转换为字符串
     *
     * @param localDateTime     日期时间对象
     * @param dateTimeFormatter 格式化字符串
     * @return 日期字符串
     */
    public static String localDateTime2Str(LocalDateTime localDateTime, String dateTimeFormatter) {
        return localDateTime.format(DateTimeFormatter.ofPattern(dateTimeFormatter));
    }

    /**
     * 日期时间转字符串函数
     * 返回ISO标准的日期字符串
     *
     * @param localDateTime 日期时间对象
     * @return 日期字符串
     */
    public static String localDateTime2Str(LocalDateTime localDateTime) {
        return localDateTime.format(DateTimeFormatter.ISO_DATE_TIME);
    }


    /**
     * 计算两个日期之间相差的天数
     *
     * @param date1 起始日期
     * @param date2 结束日期
     * @return 相差天数
     */
    public static int daysBetween(LocalDate date1, LocalDate date2) {
        Period period = Period.between(date1, date2);
        return period.getDays();
    }

    /**
     * 计算两个日期之间相差的月数
     *
     * @param date1 起始日期
     * @param date2 结束日期
     * @return 相差月数
     */
    public static int monthsBetween(LocalDate date1, LocalDate date2) {
        Period period = Period.between(date1, date2);
        return period.getMonths();
    }

    /**
     * 计算两个日期之间相差的年数
     *
     * @param date1 起始日期
     * @param date2 结束日期
     * @return 相差年数
     */
    public static int yearsBetween(LocalDate date1, LocalDate date2) {
        Period period = Period.between(date1, date2);
        return period.getYears();
    }

    /**
     * 计算两个日期之间相差的天数
     *
     * @param date1 起始日期
     * @param date2 结束日期
     * @return 相差天数
     */
    public static int daysBetween(Date date1, Date date2) {
        Instant instantDate1 = date1.toInstant();
        Instant instantDate2 = date2.toInstant();
        LocalDate localDate1 = instantDate1.atZone(CHINA_ZONE).toLocalDate();
        LocalDate localDate2 = instantDate2.atZone(CHINA_ZONE).toLocalDate();
        instantDate1.atZone(CHINA_ZONE);
        Period period = Period.between(localDate1, localDate2);
        return period.getDays();
    }


    /**
     * 计算两个日期之间相差的月数
     *
     * @param date1 起始日期
     * @param date2 结束日期
     * @return 相差月数
     */
    public static int monthsBetween(Date date1, Date date2) {
        Instant instantDate1 = date1.toInstant();
        Instant instantDate2 = date2.toInstant();
        LocalDate localDate1 = instantDate1.atZone(CHINA_ZONE).toLocalDate();
        LocalDate localDate2 = instantDate2.atZone(CHINA_ZONE).toLocalDate();
        instantDate1.atZone(CHINA_ZONE);
        Period period = Period.between(localDate1, localDate2);
        return period.getMonths();
    }

    /**
     * 计算两个日期之间相差的年数
     *
     * @param date1 起始日期
     * @param date2 结束日期
     * @return 相差年数
     */
    public static int yearsBetween(Date date1, Date date2) {
        Instant instantDate1 = date1.toInstant();
        Instant instantDate2 = date2.toInstant();
        LocalDate localDate1 = instantDate1.atZone(CHINA_ZONE).toLocalDate();
        LocalDate localDate2 = instantDate2.atZone(CHINA_ZONE).toLocalDate();
        instantDate1.atZone(CHINA_ZONE);
        Period period = Period.between(localDate1, localDate2);
        return period.getYears();
    }

    /**
     * 获取指定日期对象当前月的起始日
     *
     * @param localDate 指定日期
     * @return 起始日
     */
    public static int getFirstDayInMonth(LocalDate localDate) {
        LocalDate result = localDate.with(TemporalAdjusters.firstDayOfMonth());
        return result.getDayOfMonth();
    }

    /**
     * 获取指定日期对象的当前月的结束日
     *
     * @param localDate 指定日期
     * @return 结束日
     */
    public static int getLastDayInMonth(LocalDate localDate) {
        LocalDate result = localDate.with(TemporalAdjusters.lastDayOfMonth());
        return result.getDayOfMonth();
    }


    /**
     * 获取指定日期对象本月的某周某天的日期
     *
     * @param localDate  日期对象
     * @param weekNumber 周
     * @param dayNumber  日
     * @return 日期
     */
    public static LocalDate getLocalDateBydayAndWeek(LocalDate localDate, int weekNumber, int dayNumber) {
        return localDate.with(TemporalAdjusters.dayOfWeekInMonth(weekNumber, DayOfWeek.of(dayNumber)));
    }

    /**
     * 计算几分钟后的时间
     *
     * @param minute 几分钟后
     * @return 日期
     */
    public static Date addTime(int minute) {
        Calendar now = Calendar.getInstance();
        now.add(Calendar.MINUTE, minute);
        return now.getTime();
    }
}
