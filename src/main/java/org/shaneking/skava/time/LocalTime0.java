package org.shaneking.skava.time;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;
import org.shaneking.skava.util.Date0;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

@Accessors(chain = true)
@ToString
public class LocalTime0 {
  @Getter
  @Setter
  private LocalTime localTime = LocalTime.now();

  private LocalTime0(LocalTime localTime) {
    this.localTime = localTime;
  }

  public static LocalTime now() {
    return LocalTime.now();
  }

  public static LocalTime0 on() {
    return LocalTime0.on(LocalTime0.now());
  }

  public static LocalTime0 on(LocalTime localTime) {
    return new LocalTime0(localTime);
  }

  public String format(String pattern) {
    return this.getLocalTime().format(DateTimeFormatter.ofPattern(pattern));
  }

  public LocalTime0 parse(String s) {
    return parse(Date0.H_MI_S, s);
  }

  public LocalTime0 parse(String pattern, String s) {
    return this.setLocalTime(LocalTime.parse(s, DateTimeFormatter.ofPattern(pattern)));
  }

  public String time() {
    return format(Date0.H_MI_S);
  }

  public String timeS() {
    return format(Date0.TIME_SSS);
  }

  public String times() {
    return format(Date0.TIMESSS);
  }
}
