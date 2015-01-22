

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.Enumeration;

import org.apache.tools.zip.ZipFile;
import org.apache.tools.zip.ZipEntry;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.io.FileNotFoundException;

/**
 *
 * <p>Title: ZipExtract</p>
 * <p>Description: Установка обновления для программы Kiosk 3</p>
 * <p>Copyright: Copyright (c) 2007 Чувпило Евгений Юрьевич</p>
 * <p>Company: ООО "Региональный Процессинговый Центр"</p>
 * @author Чувпило Евгений Юрьевич
 * @version 1.0
 */

public class ZipExtract {
  /**
   * Объект для работы с zip-файлом
   */
  private ZipFile zipFile = null;
  /**
   * Объект для работы с каталогом распаковки
   */
  private File outDir = null;
  /**
   * Кодировка в которую будут переводится имена файлов с кирилицей
   */
  private String outCP = "CP866";
  /**
   * Конструктор
   * @param zipFile File объект файл для zip-архива
   * @throws IOException
   */
  public ZipExtract(File zipFile) throws IOException {
    this.zipFile = new ZipFile(zipFile);
  }

  /**
   * Конструктор
   * @param zipFile File объект файл для zip-архива
   * @param outDir File объект файл для каталога распаковки
   * @throws IOException
   */
  public ZipExtract(File zipFile, File outDir) throws IOException {
    this.zipFile = new ZipFile(zipFile);
    this.outDir = outDir;
    if (!this.outDir.exists()) {
      this.outDir.mkdir();
    }
  }

  /**
   * Консттруктор
   * @param zipFile File объект файл для zip-архива
   * @param outCP String - кодировка в которую надо перекодировать файлы в архиве
   * @throws IOException
   */
  public ZipExtract(File zipFile, String outCP) throws IOException {
    this.zipFile = new ZipFile(zipFile);
    this.outCP = outCP;
  }

  /**
   * Распаковка конкретного файла в архиве
   * @param fileName String имя файла
   * @throws IOException
   */
  public void unpackFile(String fileName) throws IOException {
    String strCP;
    try {
      strCP = new String(fileName.getBytes(), outCP);
    }
    catch (UnsupportedEncodingException ex) {
      strCP = new String(fileName.getBytes());
    }
    ZipEntry curEntry = zipFile.getEntry(strCP);
    this.createDirTree();
    this.writeFile(curEntry);
    zipFile.close();
  }

  /**
   * Распаковка всех файлов из архива
   * @throws IOException
   * @throws UnsupportedEncodingException
   * @throws FileNotFoundException
   */
  public void unpackAllFiles() throws IOException, FileNotFoundException,
      UnsupportedEncodingException {
    ZipEntry curEntry = null;
    Enumeration entryList = zipFile.getEntries();
    this.createDirTree();
    while (entryList.hasMoreElements()) {
      curEntry = (ZipEntry) entryList.nextElement();
      if (curEntry.isDirectory()) {
        continue;
      }
      else {
        this.writeFile(curEntry);
      }
    }
    zipFile.close();
  }

  /**
   * Получение списка файлов в архиве
   * @return String[]
   */
  public String[] getFilesList() {
    String filesList[] = new String[this.getFilesCount()];
    Enumeration entryList = zipFile.getEntries();
    ZipEntry curEntry = null;
    int iter = 0;

    while (entryList.hasMoreElements()) {
      curEntry = (ZipEntry) entryList.nextElement();
      filesList[iter] = curEntry.getName();
      iter++;
    }
    return filesList;
  }

  /**
   * Утсановка каталога распоковки
   * @param outDir File объект файл для каталога распаковки
   * @return boolean результат создания каталога распаковки
   * @throws SecurityException
   */
  public boolean setOutDir (File outDir) throws SecurityException {
    boolean outDirCreateRes = true;
    this.outDir = outDir;
    if (!this.outDir.exists()) {
      outDirCreateRes = this.outDir.mkdir();
    }
    return outDirCreateRes;
  }

  /**
   * Получение кол-ва файлов в архиве
   * @return int Кол-во файлов в архиве
   */
  private int getFilesCount() {
    int filesCount = 0;
    Enumeration entryList = zipFile.getEntries();
    ZipEntry curEntry = null;

    while (entryList.hasMoreElements()) {
      curEntry = (ZipEntry) entryList.nextElement();
      filesCount++;
    }
    return filesCount;
  }

  /**
   * Создание дерева каталогов архива
   * @throws IOException
   */
  private void createDirTree() throws IOException {
    Enumeration entryList = zipFile.getEntries();
    ZipEntry curEntry = null;
    File curDir = null;

    while (entryList.hasMoreElements()) {
      curEntry = (ZipEntry) entryList.nextElement();
      String strCP;
      try {
        strCP = new String(curEntry.getName().getBytes(), outCP);
      }
      catch (UnsupportedEncodingException ex) {
        strCP = new String(curEntry.getName().getBytes());
      }
      // если текущий элемент zip-архива каталог то создаём его
      if (curEntry.isDirectory()) {

        if (!outDir.equals(null)) {
          curDir = new File(outDir, strCP);
        } else {
          curDir = new File(strCP);
        }
        curDir.mkdirs();
      }
    }
  }

  /**
   * Запись файлов на диск
   * @param curEntry ZipEntry для текущего файла в архиве
   * @throws IOException
   * @throws UnsupportedEncodingException
   * @throws FileNotFoundException
   */
  private void writeFile(ZipEntry curEntry) throws IOException {
    byte outBuf[] = new byte[512];
    int length = 0;
    File outFile = null;
    String strCP;
    try {
      strCP = new String(curEntry.getName().getBytes(), outCP);
    }
    catch (UnsupportedEncodingException ex) {
      strCP = new String(curEntry.getName().getBytes());
    }
    // если каталог распаковки задан то создаём файл как его потомка
    if (!outDir.equals(null)) {
      outFile = new File(outDir, strCP);
    } else {
      outFile = new File(strCP);
    }

    // определение потоков в/в
    InputStream inputStreamFromZip = zipFile.getInputStream(curEntry);
    FileOutputStream outStreamToFile = new FileOutputStream(outFile);

    // запись в файл
    while ( (length = inputStreamFromZip.read(outBuf)) > -1) {
      outStreamToFile.write(outBuf, 0, length);
    }

    inputStreamFromZip.close();
    outStreamToFile.close();
  }
}
