package org.shaneking.skava.util.concurrent;

import com.google.common.base.Strings;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.shaneking.skava.io.AC0;
import org.shaneking.skava.util.FixedSizeArrayList;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.LineNumberReader;
import java.text.MessageFormat;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.Callable;

@Slf4j
public class Runtime0Callable implements Callable<List<String>> {
  @Getter
  private InputStream inputStream;
  @Getter
  private boolean errorStream;
  @Getter
  private boolean value4pause;//if meet pause, return true or false?
  @Getter
  private String pauseFlag;
  private int maxRecordSize;

  public Runtime0Callable(InputStream inputStream, boolean errorStream, boolean value4pause, String pauseFlag, int maxRecordSize) {
    super();
    this.inputStream = inputStream;
    this.errorStream = errorStream;
    this.value4pause = value4pause;
    this.pauseFlag = pauseFlag;
    this.maxRecordSize = maxRecordSize;
  }

  @Override
  public List<String> call() throws Exception {
    List<String> rtnList = new FixedSizeArrayList<String>(maxRecordSize);
    rtnList.add(MessageFormat.format("===B:errorStream={0},value4pause={1},pauseFlag={2}===", errorStream, value4pause, pauseFlag));

    LineNumberReader lineNumberReader = null;
    InputStreamReader inputStreamReader = null;
    String line = null;

    try {
      inputStreamReader = new InputStreamReader(this.getInputStream());
      lineNumberReader = new LineNumberReader(inputStreamReader);

      if (Strings.isNullOrEmpty(this.getPauseFlag())) {
        while ((line = lineNumberReader.readLine()) != null) {
          rtnList.add(line);
          if (this.isErrorStream()) {
            log.warn(line);
          } else {
            log.info(line);
          }
        }
      } else {
        while ((line = lineNumberReader.readLine()) != null) {
          rtnList.add(line);
          if (this.isErrorStream()) {
            log.warn(line);
          } else {
            log.info(line);
          }
          if (line.toLowerCase(Locale.ENGLISH).contains(this.getPauseFlag())) {
            rtnList.add("===P:line contains pauseFlag===");
            break;
          }
        }
      }
    } catch (Exception e) {
      log.error(e.getMessage(), e);
      rtnList.add(e.toString());
    } finally {
      AC0.close(lineNumberReader);
      AC0.close(inputStreamReader);
    }
    rtnList.add(MessageFormat.format("===E:errorStream={0},value4pause={1},pauseFlag={2}===", errorStream, value4pause, pauseFlag));
    return rtnList;
  }
}
