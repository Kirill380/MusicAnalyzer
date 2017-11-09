package io.redkite.music.analyzer.util;

import lombok.experimental.UtilityClass;

import java.time.Clock;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

@UtilityClass
public class DateUtil {

  private static final DateTimeFormatter ISO_8601_FORMATTER = DateTimeFormatter.ISO_INSTANT;

  private static final String UTC = "UTC";

  /**
   * Returns string representation of given date in ISO 8601 format in UTC timezone.
   *
   * @param localDateTime the date and time as {@link LocalDateTime}
   * @return the date string in ISO 8601 format
   */
  public static String toIso8601DateString(LocalDateTime localDateTime) {
    return ZonedDateTime.of(localDateTime, ZoneId.of(UTC)).format(ISO_8601_FORMATTER);
  }

  /**
   * Returns string representation of given date in ISO 8601 format in UTC timezone.
   *
   * @param timestamp the epoch time in millis
   * @return the date string in ISO 8601 format
   */
  public static String toIso8601DateString(long timestamp) {
    return DateUtil.toIso8601DateString(toLocalDateTimeInUtc(timestamp));
  }


  /**
   * Converts epoch millis to local date time in utc timezone.
   *
   * @param millis the epoch millis
   * @return the local date time
   */
  public static LocalDateTime toLocalDateTimeInUtc(Long millis) {
    return LocalDateTime.ofInstant(Instant.ofEpochMilli(millis), ZoneId.of(UTC));
  }


  /**
   * Converts local date time in utc timezone to epoch millis.
   *
   * @param dateTime the date time
   * @return the millis
   */
  public static Long toMillis(LocalDateTime dateTime) {
    return ZonedDateTime.of(dateTime, ZoneId.of(UTC)).toInstant().toEpochMilli();
  }

  public static final LocalDateTime nowInUtc() {
    return LocalDateTime.now(Clock.systemUTC());
  }

  /**
   * Converts local date time in local timezone to UTC.
   *
   * @param from the local date time in local timezone
   * @return the local date time in UTC time zone
   */
  public static final LocalDateTime toUtc(LocalDateTime from) {
    if (from == null) {
      return null;
    }
    return from.atZone(ZoneId.systemDefault()).withZoneSameInstant(ZoneId.of(UTC)).toLocalDateTime();
  }


}
