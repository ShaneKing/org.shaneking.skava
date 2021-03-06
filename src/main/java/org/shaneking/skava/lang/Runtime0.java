package org.shaneking.skava.lang;

import com.google.common.base.Strings;
import lombok.extern.slf4j.Slf4j;
import org.shaneking.skava.util.FixedSizeArrayList;
import org.shaneking.skava.util.concurrent.Runtime0Callable;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

@Slf4j
public class Runtime0 {
  public static final long DEFAULT_TIMEOUT_MINUTES = 33;
  public static final String PAUSE_FLAG_CMD = ">pause";
  public static final String PAUSE_FLAG_SHELL = ">read -n 1 -p";

  public static List<String> exec(String command) {
    return exec(command, DEFAULT_TIMEOUT_MINUTES);
  }

  public static List<String> exec(String command, long timeout) {
    return exec(command, timeout, false, null);
  }

  public static List<String> exec(String command, long timeout, int maxRecordSize) {
    return exec(command, timeout, false, null, maxRecordSize);
  }

  public static List<String> exec(String command, boolean value4pause, String pauseFlag) {
    return exec(command, DEFAULT_TIMEOUT_MINUTES, value4pause, pauseFlag);
  }

  public static List<String> exec(String command, boolean value4pause, String pauseFlag, int maxRecordSize) {
    return exec(command, DEFAULT_TIMEOUT_MINUTES, value4pause, pauseFlag, maxRecordSize);
  }

  public static List<String> exec(String command, long timeout, boolean value4pause, String pauseFlag) {
    return exec(command, timeout, value4pause, pauseFlag, FixedSizeArrayList.DEFAULT_SIZE);
  }

  public static List<String> exec(String command, long timeout, boolean value4pause, String pauseFlag, int maxRecordSize) {
    log.info(command);
    List<String> rtnList = new FixedSizeArrayList<String>(maxRecordSize);

    if (!Strings.isNullOrEmpty(command)) {
      Process process = null;
      Future<List<String>> iFuture = null;
      Future<List<String>> eFuture = null;
      try {
        process = Runtime.getRuntime().exec(command);
        ExecutorService es = Executors.newFixedThreadPool(2);
        iFuture = es.submit(new Runtime0Callable(process.getInputStream(), false, value4pause, pauseFlag, maxRecordSize));
        eFuture = es.submit(new Runtime0Callable(process.getErrorStream(), true, value4pause, pauseFlag, maxRecordSize));
        rtnList.addAll(iFuture.get(timeout, TimeUnit.MINUTES));
        rtnList.addAll(eFuture.get(timeout, TimeUnit.MINUTES));
        rtnList.add("process.waitFor()=" + process.waitFor());
      } catch (Exception e) {
        log.error(e.getMessage(), e);
        rtnList.add(e.toString());
      } finally {
        if (null != iFuture) {
          iFuture.cancel(true);
        }
        if (null != eFuture) {
          eFuture.cancel(true);
        }
        if (null != process) {
          process.destroy();
        }
      }
    }
    return rtnList;
  }
}
