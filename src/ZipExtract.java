

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
 * <p>Description: ��������� ���������� ��� ��������� Kiosk 3</p>
 * <p>Copyright: Copyright (c) 2007 ������� ������� �������</p>
 * <p>Company: ��� "������������ �������������� �����"</p>
 * @author ������� ������� �������
 * @version 1.0
 */

public class ZipExtract {
  /**
   * ������ ��� ������ � zip-������
   */
  private ZipFile zipFile = null;
  /**
   * ������ ��� ������ � ��������� ����������
   */
  private File outDir = null;
  /**
   * ��������� � ������� ����� ����������� ����� ������ � ���������
   */
  private String outCP = "CP866";
  /**
   * �����������
   * @param zipFile File ������ ���� ��� zip-������
   * @throws IOException
   */
  public ZipExtract(File zipFile) throws IOException {
    this.zipFile = new ZipFile(zipFile);
  }

  /**
   * �����������
   * @param zipFile File ������ ���� ��� zip-������
   * @param outDir File ������ ���� ��� �������� ����������
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
   * ������������
   * @param zipFile File ������ ���� ��� zip-������
   * @param outCP String - ��������� � ������� ���� �������������� ����� � ������
   * @throws IOException
   */
  public ZipExtract(File zipFile, String outCP) throws IOException {
    this.zipFile = new ZipFile(zipFile);
    this.outCP = outCP;
  }

  /**
   * ���������� ����������� ����� � ������
   * @param fileName String ��� �����
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
   * ���������� ���� ������ �� ������
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
   * ��������� ������ ������ � ������
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
   * ��������� �������� ����������
   * @param outDir File ������ ���� ��� �������� ����������
   * @return boolean ��������� �������� �������� ����������
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
   * ��������� ���-�� ������ � ������
   * @return int ���-�� ������ � ������
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
   * �������� ������ ��������� ������
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
      // ���� ������� ������� zip-������ ������� �� ������ ���
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
   * ������ ������ �� ����
   * @param curEntry ZipEntry ��� �������� ����� � ������
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
    // ���� ������� ���������� ����� �� ������ ���� ��� ��� �������
    if (!outDir.equals(null)) {
      outFile = new File(outDir, strCP);
    } else {
      outFile = new File(strCP);
    }

    // ����������� ������� �/�
    InputStream inputStreamFromZip = zipFile.getInputStream(curEntry);
    FileOutputStream outStreamToFile = new FileOutputStream(outFile);

    // ������ � ����
    while ( (length = inputStreamFromZip.read(outBuf)) > -1) {
      outStreamToFile.write(outBuf, 0, length);
    }

    inputStreamFromZip.close();
    outStreamToFile.close();
  }
}
