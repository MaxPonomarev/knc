import java.io.*;
import javax.swing.*;

/**
 * <p>Title: Updater</p>
 * <p>Description: ��������� ���������� ��� ��������� Kiosk 3.0</p>
 * <p>Copyright: Copyright (c) 2007 ������� ������� �������</p>
 * <p>Company: ��� "������������ �������������� �����"</p>
 * @author ������� ������� �������
 * @author ��������� ������ ����������
 * @version 1.0
 */

public class Updater {
  static final String updateExt = "kupd";
  static final File updateDir = new File("updates");
  static final String okMessage = "���������� ������� �����������. ���������� �������� ��������� ���������";
  static final String errMessage = "���������� ������ � ��������, ���������, ��� � ��� ���� ����� ��������������";

  public static byte checkUpdates() {
    RPCMessage rpcMessage;

    // �������� ������� � �������� ����� ����������
    File updateDirList[] = updateDir.listFiles();
    String fileExt = "";
    int updatesCount = 0;
    int updateFilePos = 0;

    // ������� ���� �� ����� ����������, ���� ���� �� ���������� ���
    for (int iter = 0; iter < updateDirList.length; iter++) {
      fileExt = UpdaterUtil.getFileExtention(updateDirList[iter].getName());
      if (fileExt.equalsIgnoreCase(updateExt)) {
        System.out.println("Found update file: " + updateDirList[iter].getAbsolutePath());
        updatesCount++;
        updateFilePos = iter;
      }
    }

    // ���� ������ ���������� ������ ������, �� ��������� ������
    if (updatesCount > 1) {
      System.err.println("Update files more than one: " + updatesCount);
      return -1;
    }
    // ���� ������ ���������� ��� �� ��������� � ������ :)
    if (updatesCount == 0) {
      System.out.println("Nothing to update");
      return 0;
    }
    rpcMessage = RPCMessage.showMessage(null, "���� ���������� ���������, ���������� ���������...", "����������", SwingConstants.LEFT);

    // ������� ��� � ������
    try {
      // ��������� zip-����
      ZipExtract updateZip = new ZipExtract(updateDirList[updateFilePos]);
      String updateZipList[] = updateZip.getFilesList();

      // ���� � ������ ����� ���� ���� � �� exe
      if (updateZipList.length == 1 && UpdaterUtil.getFileExtention(updateZipList[0]).equals("exe")) {
        // ��������� �������� ���������� � ���������� �����
        updateZip.setOutDir(updateDir);
        updateZip.unpackFile(updateZipList[0]);

        // ������ �� ���������� � �������� ������ �� ���������� ������� ����������
        Process updateProcess = new ProcessBuilder(updateDir.getAbsolutePath() + File.separator + updateZipList[0], "/s").start();
        BufferedReader in = new BufferedReader(new InputStreamReader(updateProcess.getInputStream()));
        String str;
        while ((str = in.readLine()) != null) {} //��� ����� ������� ������-�� ���������������
        in.close();

        int updateProcRetCode = updateProcess.waitFor();
        Thread.sleep(10000);
        rpcMessage.dispose();
        // ��� ������
        if (updateProcRetCode == 0 || updateProcRetCode == 1000) {//1000 - WinRar SFX SetupCode
          File updateFileExe = new File(updateDir, updateZipList[0]);
          updateFileExe.delete();
          UpdaterUtil.removeFile(updateDirList[updateFilePos]);
          RPCMessage.showMessageDialog(null, okMessage);
          return 1;
        }
        // ��� �����
        else {
          System.err.println("Update error: " + updateProcRetCode);

          // ������� ������������ ���� exe
          File updateFileExe = new File(updateDir, updateZipList[0]);
          updateFileExe.delete();

          // ������� ������������ ���� kupd
          UpdaterUtil.removeFile(updateDirList[updateFilePos]);
          RPCMessage.showMessageDialog(null, errMessage);
          return -1;
        }
      }

      // ���� �� exe ��� ������ ������ ������
      else {
        File zipOutDir = new File(System.getProperty("user.dir"));
        updateZip.setOutDir(zipOutDir);
        updateZip.unpackAllFiles();
        rpcMessage.dispose();
        RPCMessage.showMessageDialog(null, okMessage);
        if (updateDirList[updateFilePos].delete()) {
          return 1;
        } else {
          return -1;
        }
      }
    }
    catch (Exception ex) {
      rpcMessage.dispose();
      RPCMessage.showMessageDialog(null, errMessage);
      ex.printStackTrace();
      return -1;
    }
  }
}
