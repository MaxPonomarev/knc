import java.io.*;
import java.text.DecimalFormat;
import java.util.*;

import sun.util.calendar.ZoneInfoFile;

class TimeZoneUtils {
  public static String getJavaTzID() throws Exception {
    String paramString = toZiDirName(System.getProperty("java.home"));
    String str = null;
    try {
      byte[] arrayOfByte = readZoneInfoMappings(paramString);
      int i = ZoneInfoFile.JAVAZM_LABEL.length + 1;
      int j = arrayOfByte.length;

      while (i < j) {
        int k = arrayOfByte[(i++)];
        int m = ((arrayOfByte[(i++)] & 0xFF) << 8) + (arrayOfByte[(i++)] & 0xFF);

        switch (k) {
          case 68:
            str = new String(arrayOfByte, i, m - 1, "utf-8");
            break;
          default:
            i += m;
        }
      }
    } catch (IOException ex) {
      throw new Exception("can't read ZoneInfoMappings at " + paramString, ex);
    } catch (Exception ex) {
      throw new Exception("ZoneInfoMappings file is corrupted.", ex);
    }
    return str;
  }

  public static String getJavaOffset() {
    DecimalFormat minFormat = new DecimalFormat("00");
    String result = "GMT";
    TimeZone tz = TimeZone.getDefault();
    int offset = tz.getRawOffset();
    int hours = offset / 1000 / 60 / 60;
    int mins = Math.abs(offset / 1000 / 60) % 60;
    if (offset > 0) result += "+";
    if (offset != 0) result += minFormat.format(hours) + ":" + minFormat.format(mins);
    if (tz.inDaylightTime(new Date())) result += " DST";
    return result;
  }

  //возвращает true - если обновление прошло успешно, false - если обновление не требуется или прошло с ошибками
  public static boolean updateJavaTz() throws Exception {
    String paramString = System.getProperty("java.home");
    String str = paramString + File.separator + "jre" + File.separator + "bin";
    File localFile = new File(str);
    if (!localFile.isDirectory()) {
      str = paramString + File.separator + "bin";
      localFile = new File(str);
    }
    if (!localFile.isDirectory()) throw new Exception("Couldn't find java.exe for update");
    String cmd = localFile.getPath() + File.separator + "java -jar tzupdater.jar -u";
    Process process = Runtime.getRuntime().exec(cmd);
    BufferedReader in = new BufferedReader(new InputStreamReader(process.getInputStream()));
    while ((str = in.readLine()) != null) {} //без этого процесс почему-то останавливается
    in.close();
    process.waitFor();
    return (process.exitValue() == 0);
  }

  public static long getOSTzID() throws IOException {
    Process process = Runtime.getRuntime().exec("reg QUERY \"HKLM\\SOFTWARE\\Microsoft\\Windows NT\\CurrentVersion\\Time Zones\" /v TZVersion");
    BufferedReader in = new BufferedReader(new InputStreamReader(process.getInputStream()));
    String str;
    Long res = null;
    while ((str = in.readLine()) != null) {
      if (str.contains("TZVersion")) {
        int i = str.indexOf("0x");
        res = Long.decode(str.substring(i));
      }
    }
    in.close();
    try {
      process.waitFor();
    } catch (InterruptedException ignored) {
    }
    if (res == null) return 0;
    else return res;
  }

  public static String getOSOffset() throws IOException {
    DecimalFormat minFormat = new DecimalFormat("00");
    String result = "GMT";

    Process process = Runtime.getRuntime().exec("reg QUERY HKLM\\SYSTEM\\CurrentControlSet\\Control\\TimeZoneInformation /v Bias");
    BufferedReader in = new BufferedReader(new InputStreamReader(process.getInputStream()));
    String str;
    Long res = null;
    while ((str = in.readLine()) != null) {
      if (str.contains("Bias")) {
        int i = str.indexOf("0x");
        res = Long.decode(str.substring(i));
      }
    }
    in.close();
    try {
      process.waitFor();
    } catch (InterruptedException ignored) {
    }
    if (res == null) throw new IOException("Could not found OS offset");
    int offset = -res.intValue();
    int hours = offset / 60;
    int mins = Math.abs(offset) % 60;
    if (offset > 0) result += "+";
    if (offset != 0) result += minFormat.format(hours) + ":" + minFormat.format(mins);

    process = Runtime.getRuntime().exec("reg QUERY HKLM\\SYSTEM\\CurrentControlSet\\Control\\TimeZoneInformation /v ActiveTimeBias");
    in = new BufferedReader(new InputStreamReader(process.getInputStream()));
    while ((str = in.readLine()) != null) {
      if (str.contains("ActiveTimeBias")) {
        int i = str.indexOf("0x");
        res = Long.decode(str.substring(i));
      }
    }
    in.close();
    try {
      process.waitFor();
    } catch (InterruptedException ignored) {
    }
    if (res.intValue() != -offset) result += " DST";
    return result;
  }

  public static void updateOSTz() {
    try {
      Runtime.getRuntime().exec("rundll32 url.dll, FileProtocolHandler http://support.microsoft.com/kb/2570791/ru");
    }
    catch (IOException ignored) {}
  }

  public static String convertOSTzID(long OSTzID) {
    long year = OSTzID >>> 16;
    long ver = (OSTzID >>> 8) & 0xFF;
    return year + "." + ver;
  }

  private static byte[] readZoneInfoMappings(String paramString) throws IOException {
    String str = toZiFileName(paramString, "ZoneInfoMappings");
    File localFile = new File(str);
    int i = (int) localFile.length();
    byte[] arrayOfByte = new byte[i];
    FileInputStream localFileInputStream = new FileInputStream(localFile);
    if (localFileInputStream.read(arrayOfByte) != i) {
      localFileInputStream.close();
      throw new IOException("read error on " + str);
    }
    localFileInputStream.close();
    return arrayOfByte;
  }

  private static String toZiDirName(String paramString) {
    String str = paramString + File.separator + "jre" + File.separator + "lib" + File.separator + "zi";
    File localFile = new File(str);
    if (localFile.isDirectory()) {
      return str;
    }
    str = paramString + File.separator + "lib" + File.separator + "zi";
    localFile = new File(str);
    return localFile.isDirectory() ? str : null;
  }

  private static String toZiFileName(String paramString1, String paramString2) {
    return paramString1 + File.separator + paramString2;
  }
}
