

/**
 * <p>Title: UpdaterUtil</p>
 * <p>Description: Методы нужныя для работы Updater</p>
 * <p>Copyright: Copyright (c) 2007 Чувпило Евгений Юрьевич</p>
 * <p>Company: ООО "Региональный Процессинговый Центр"</p>
 * @author Чувпило Евгений Юрьевич
 * @version 1.0
 */

import java.util.Date;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.io.*;

final class UpdaterUtil {
  /**
   * Получение текущей даты
   * @return String текщая дата в формате yyyy-MM-dd_hh-mm-ss
   */
  public static String getCurFullDate() {
    DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd_hh-mm-ss");
    Date curDate = new Date();
    return dateFormat.format(curDate);
  }

  /**
   * Получение расширения файла
   * @param fileName String имя файла
   * @return String расширение текущего файла
   */
  public static String getFileExtention(String fileName) {
    String curFileName[] = fileName.split("\\.");
    return curFileName[curFileName.length - 1];
  }

  /**
   * Копированеи файлов и каталогов
   * @param sourceFile File исходный файл
   * @param destFile File новый файл
   * @throws IOException
   */
  public static void copyFile(File sourceFile, File destFile) throws
      IOException {
    // если копируем каталог
    if ( (sourceFile.isDirectory() && !destFile.exists()) ||
        (sourceFile.isDirectory() && destFile.exists())) {
      // содание каталога назначения
      destFile.mkdirs();
      // получеине списка файлов в каталоге
      File sourceFiles[] = sourceFile.listFiles();

      // копирование
      for (int iter = 0; iter < sourceFiles.length; iter++) {
        File destFinalFile = new File(destFile, sourceFiles[iter].getName());
        copyFile(sourceFiles[iter], destFinalFile);
      }
    }
    else {
      // потоки для чтения / записи данных из файлов
      FileWriter destFileStream = new FileWriter(destFile);
      FileReader sourceFileStream = new FileReader(sourceFile);
      int ch = 0;

      // чтение и запись
      while ( (ch = sourceFileStream.read()) != -1) {
        destFileStream.write(ch);
      }

      // закрытие потоков
      destFileStream.close();
      sourceFileStream.close();
    }
  }

  /**
   * Удаление файлов и каталогов
   * @param toRemove File имя файл для удаления
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
