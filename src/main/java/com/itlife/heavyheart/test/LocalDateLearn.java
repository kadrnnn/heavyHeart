package com.itlife.heavyheart.test;

import java.time.*;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;

/**
 * @Author kex
 * @Create 2020/6/4 10:21
 * @Description
 */
public class LocalDateLearn {
    public static void main(String[] args) {
        LocalDate today = LocalDate.now();
        System.out.println(today);
//        Date date = new Date();
//        System.out.println(date);
        System.out.println(today.getYear() + "" +
                today.getMonthValue() + "" +
                today.getDayOfMonth());
        LocalDate dateOfBirth = LocalDate.of(1996, 4, 12);
        System.out.println(dateOfBirth);
        System.out.println(today.equals(dateOfBirth));
        MonthDay birthday = MonthDay.from(dateOfBirth);
        MonthDay currentMonth = MonthDay.from(today);
        System.out.println(birthday.equals(currentMonth) + "///" + birthday);
        System.out.println(birthday + "" + currentMonth);
        LocalTime time = LocalTime.now();
        System.out.println(time);
        LocalTime newTime = time.plusHours(2);
        System.out.println(newTime);
        today.plus(1, ChronoUnit.WEEKS);
        Clock defaultclock = Clock.systemDefaultZone();
        Clock clock = Clock.systemUTC();
        System.out.println(defaultclock + "" + clock);
        System.out.println(today.isAfter(dateOfBirth));
        System.out.println(today.isBefore(dateOfBirth));
        ZoneId america = ZoneId.of("Asia/Shanghai");
        LocalDateTime localDateTime = LocalDateTime.now();
        ZonedDateTime zonedDateTime = ZonedDateTime.of(localDateTime, america);
        System.out.println("现在的日期和时间在特定的时区：" + zonedDateTime);
        System.out.println(today.isLeapYear());
        Period between = Period.between(dateOfBirth, today);
        System.out.println(between.getYears());
        LocalDateTime datetime = LocalDateTime.of(2018, Month.FEBRUARY, 14, 19, 30);
        ZoneOffset offset = ZoneOffset.of("+05:30");
        OffsetDateTime date = OffsetDateTime.of(datetime, offset);
        System.out.println("Date and Time with timezone offset in Java : " + date);
        System.out.println(Instant.now());
        String dayday ="20200604";
        LocalDate parse = LocalDate.parse(dayday, DateTimeFormatter.BASIC_ISO_DATE);
        System.out.println(parse);
        System.out.println( Math.round(-10.2));
    }
}
