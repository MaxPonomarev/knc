import java.io.*;
import javax.swing.*;

/**
 * <p>Title: Updater</p>
 * <p>Description: Установка обновления для программы Kiosk 3.0</p>
 * <p>Copyright: Copyright (c) 2007 Чувпило Евгений Юрьевич</p>
 * <p>Company: ООО "Региональный Процессинговый Центр"</p>
 * @author Чувпило Евгений Юрьевич
 * @author Пономарев Максим Евгеньевич
 * @version 1.0
 */

public class Updater {
  static final String updateExt = "kupd";
  static final File updateDir = new File("updates");
  static final String okMessage = "Обновление успешно установлено. Необходимо повторно запустить программу";
  static final String errMessage = "Обновление прошло с ошибками, убедитесь, что у вас есть права администратора";

  public static byte checkUpdates() {
    RPCMessage rpcMessage;

    // проверка наличия в каталоге файла обновления
    File updateDirList[] = updateDir.listFiles();
    String fileExt = "";
    int updatesCount = 0;
    int updateFilePos = 0;

    // смотрим есть ли файлы обновлений, если есть то запоминаем где
    for (int iter = 0; iter < updateDirList.length; iter++) {
      fileExt = UpdaterUtil.getFileExtention(updateDirList[iter].getName());
      if (fileExt.equalsIgnoreCase(updateExt)) {
        System.out.println("Found update file: " + updateDirList[iter].getAbsolutePath());
        updatesCount++;
        updateFilePos = iter;
      }
    }

    // если файлов обновлений больше одного, то завершаем работу
    if (updatesCount > 1) {
      System.err.println("Update files more than one: " + updatesCount);
      return -1;
    }
    // если файлов обновлений нет то прощаемся и уходим :)
    if (updatesCount == 0) {
      System.out.println("Nothing to update");
      return 0;
    }
    rpcMessage = RPCMessage.showMessage(null, "Идет обновление программы, пожалуйста подождите...", "Обновление", SwingConstants.LEFT);

    // смотрим что в архиве
    try {
      // открываем zip-файл
      ZipExtract updateZip = new ZipExtract(updateDirList[updateFilePos]);
      String updateZipList[] = updateZip.getFilesList();

      // если в архиве всего один файл и он exe
      if (updateZipList.length == 1 && UpdaterUtil.getFileExtention(updateZipList[0]).equals("exe")) {
        // установка каталога распаковки и извличение файла
        updateZip.setOutDir(updateDir);
        updateZip.unpackFile(updateZipList[0]);

        // запуск на выполнение и проверка удачно ли завершился процесс выполнения
        Process updateProcess = new ProcessBuilder(updateDir.getAbsolutePath() + File.separator + updateZipList[0], "/s").start();
        BufferedReader in = new BufferedReader(new InputStreamReader(updateProcess.getInputStream()));
        String str;
        while ((str = in.readLine()) != null) {} //без этого процесс почему-то останавливается
        in.close();

        int updateProcRetCode = updateProcess.waitFor();
        Thread.sleep(10000);
        rpcMessage.dispose();
        // Все хорошо
        if (updateProcRetCode == 0 || updateProcRetCode == 1000) {//1000 - WinRar SFX SetupCode
          File updateFileExe = new File(updateDir, updateZipList[0]);
          updateFileExe.delete();
          UpdaterUtil.removeFile(updateDirList[updateFilePos]);
          RPCMessage.showMessageDialog(null, okMessage);
          return 1;
        }
        // все плохо
        else {
          System.err.println("Update error: " + updateProcRetCode);

          // удаляем неправильный файл exe
          File updateFileExe = new File(updateDir, updateZipList[0]);
          updateFileExe.delete();

          // удаляем неправильный файл kupd
          UpdaterUtil.removeFile(updateDirList[updateFilePos]);
          RPCMessage.showMessageDialog(null, errMessage);
          return -1;
        }
      }

      // если не exe или файлов больше одного
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
