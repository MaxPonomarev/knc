

/**
 * <p>Title: UpdaterUtil</p>
 * <p>Description: ������ ������ ��� ������ Updater</p>
 * <p>Copyright: Copyright (c) 2007 ������� ������� �������</p>
 * <p>Company: ��� "������������ �������������� �����"</p>
 * @author ������� ������� �������
 * @version 1.0
 */

import java.util.Date;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.io.*;

final class UpdaterUtil {
  /**
   * ��������� ������� ����
   * @return String ������ ���� � ������� yyyy-MM-dd_hh-mm-ss
   */
  public static String getCurFullDate() {
    DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd_hh-mm-ss");
    Date curDate = new Date();
    return dateFormat.format(curDate);
  }

  /**
   * ��������� ���������� �����
   * @param fileName String ��� �����
   * @return String ���������� �������� �����
   */
  public static String getFileExtention(String fileName) {
    String curFileName[] = fileName.split("\\.");
    return curFileName[curFileName.length - 1];
  }

  /**
   * ����������� ������ � ���������
   * @param sourceFile File �������� ����
   * @param destFile File ����� ����
   * @throws IOException
   */
  public static void copyFile(File sourceFile, File destFile) throws
      IOException {
    // ���� �������� �������
    if ( (sourceFile.isDirectory() && !destFile.exists()) ||
        (sourceFile.isDirectory() && destFile.exists())) {
      // ������� �������� ����������
      destFile.mkdirs();
      // ��������� ������ ������ � ��������
      File sourceFiles[] = sourceFile.listFiles();

      // �����������
      for (int iter = 0; iter < sourceFiles.length; iter++) {
        File destFinalFile = new File(destFile, sourceFiles[iter].getName());
        copyFile(sourceFiles[iter], destFinalFile);
      }
    }
    else {
      // ������ ��� ������ / ������ ������ �� ������
      FileWriter destFileStream = new FileWriter(destFile);
      FileReader sourceFileStream = new FileReader(sourceFile);
      int ch = 0;

      // ������ � ������
      while ( (ch = sourceFileStream.read()) != -1) {
        destFileStream.write(ch);
      }

      // �������� �������
      destFileStream.close();
      sourceFileStream.close();
    }
  }

  /**
   * �������� ������ � ���������
   * @param toRemove File ��� ���� ��� ��������
   * @throws IOException
   */
  public static void removeFile (File toRemove) throws IOException {
    if (toRemove.isDirectory()) {
      File filesList[] = toRemove.listFiles();

      for (int iter = 0; iter < filesList.length; iter++) {
        removeFile(filesList[iter]);
      }

      toRemove.delete();
    }
    else {
      if (toRemove.exists()) {
       toRemove.delete();
      }
    }
  }
}
