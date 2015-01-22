import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.math.BigDecimal;
import java.util.*;
import java.util.List;
import java.util.prefs.Preferences;
import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;

public class KNC_Terminal implements ActionListener {
  private final boolean ACTIVE_CHANGING_IP = true; //если true, то используется система смены IP со старого на новый
  private boolean usingNewIP = false;
  private final String newIP = "94.125.184.38";
  private UserProfile data1NewIP;
  private Object[] data2NewIP;

  private RPCProgress progress;
  private Object[] needUpdate;
  static boolean isVistaSeven = false;
  static final List<Image> icons = new ArrayList<>(6);
  static {
    icons.add(new ImageIcon("images/icon.png").getImage());
    icons.add(new ImageIcon("images/icon32.png").getImage());
    icons.add(new ImageIcon("images/icon48.png").getImage());
    icons.add(new ImageIcon("images/icon64.png").getImage());
    icons.add(new ImageIcon("images/icon128.png").getImage());
    icons.add(new ImageIcon("images/icon256.png").getImage());
  }
  static final Preferences PREF = Preferences.userRoot().node(KNC_Terminal.class.getName().toLowerCase());
  static final File userHome = getUserHome("KNC_Terminal");
  static final String wasSent = "Запрос отправлен";
  static final String wasGot = "Получены данные";
  static final String wasLoaded = "Загружены данные";
  static final String queryFilter = "фильтр запроса";
  static final String updatingTitle = " (Обновление данных...)";
  static final String machineTitle = " [Машина времени включена]";
  static final String groupTitle = " [Автоматическая группировка включена]";
  static final String actualTitle = " (Данные неактуальны)";
  static final String machineNotAllow = "В режиме машины времени данная команда недоступна";
  static final ImageIcon alertYellowIcon = new ImageIcon("images/alert_yell.png");
  static final ImageIcon alertRedIcon = new ImageIcon("images/alert_red.png");
  static private String login;
  static MainFrame frame;
  static JFileChooser saveRegistryFileDialog;
  static JFileChooser saveIncassFileDialog;
  static JFileChooser saveLogFileDialog;
  static JFileChooser saveZReportsFileDialog;
  static JFileChooser openUpdateDialog;
  static FilterMenuFactory filterMenuFactory;
  static String interruptTitle = "";

  static byte defaultSortColumn;
  static byte defaultTableMaximized;
  static int delay;
  static int tickerDelay;
  static String serverIP;
  static int serverPort;

  static UserProfile user = null;
  static DataUpdater dataUpdater = null;
  //Версия! Ставится только здесь:
  static final String ver = "4.2.006";

  public KNC_Terminal() {
    FileNameExtensionFilter filter;
    saveRegistryFileDialog = new JFileChooser();
    saveRegistryFileDialog.setDialogTitle("Сохранить...");
    saveRegistryFileDialog.setAcceptAllFileFilterUsed(false);
    saveRegistryFileDialog.setFileFilter(new FileNameExtensionFilter("Microsoft Excel", "xls"));
    saveIncassFileDialog = new JFileChooser();
    saveIncassFileDialog.setDialogTitle("Сохранить...");
    saveIncassFileDialog.setAcceptAllFileFilterUsed(false);
    filter = new FileNameExtensionFilter("Microsoft Excel", "xls");
    saveIncassFileDialog.addChoosableFileFilter(filter);
    saveIncassFileDialog.addChoosableFileFilter(new FileNameExtensionFilter("1С Предприятие", "txt"));
    saveIncassFileDialog.setFileFilter(filter);
    saveIncassFileDialog.addPropertyChangeListener(new JFileChooserPropertyChangeListener());
    saveLogFileDialog = new JFileChooser(System.getProperty("user.dir"));
    saveLogFileDialog.setDialogTitle("Сохранить лог");
    saveLogFileDialog.setAcceptAllFileFilterUsed(false);
    saveLogFileDialog.setFileFilter(new FileNameExtensionFilter("Kiosk log files", "log"));
    saveZReportsFileDialog = new JFileChooser();
    saveZReportsFileDialog.setDialogTitle("Сохранить...");
    saveZReportsFileDialog.setAcceptAllFileFilterUsed(false);
    saveZReportsFileDialog.addChoosableFileFilter(new FileNameExtensionFilter("Microsoft Excel", "xls"));
    filter = new FileNameExtensionFilter("1С Предприятие", "txt");
    saveZReportsFileDialog.addChoosableFileFilter(filter);
    saveZReportsFileDialog.setFileFilter(filter);
    saveZReportsFileDialog.addPropertyChangeListener(new JFileChooserPropertyChangeListener());
    openUpdateDialog = new JFileChooser(System.getProperty("user.dir"));
    openUpdateDialog.setDialogTitle("Открыть обновление");
    openUpdateDialog.setFileFilter(new FileNameExtensionFilter("Kiosk update files", "kupd"));
    filterMenuFactory = new FilterMenuFactory();
    File f = new File("config.txt");
    try {
      Properties props = new Properties();
      try (FileInputStream in = new FileInputStream(f)) {
        props.load(in);
      }
      defaultSortColumn = Byte.valueOf(props.getProperty("DefaultSortColumn"));
      defaultTableMaximized = Byte.valueOf(props.getProperty("DefaultTableMaximized"));
      delay = Integer.valueOf(props.getProperty("Delay"));
      if (delay < 15000) delay = 15000;
      tickerDelay = Integer.valueOf(props.getProperty("TeekerDelay"));
      if (tickerDelay < 20000) tickerDelay = 20000;
      serverIP = props.getProperty("KNC_ip");
      serverPort = Integer.valueOf(props.getProperty("KNC_port"));
    } catch (Exception e) {
      System.err.println("Can't operate with properties file config.txt");
      System.exit(-1);
    }
    frame = new MainFrame();
    frame.validate();
    frame.setLocation(-500, -400);
    frame.setVisible(true);
    long tzIDOS = 0;
    try {
      tzIDOS = TimeZoneUtils.getOSTzID();
    } catch (Exception ignored) {}
    String tzIDJava = "";
    try {
      tzIDJava = TimeZoneUtils.getJavaTzID();
    } catch (Exception ignored) {}
    AuthDialog auth = new AuthDialog(frame, Dialog.ModalityType.APPLICATION_MODAL, login);
    auth.setVisible(true);
    UserProfile userProfile = new UserProfile(auth.getUserName(), auth.getUserPass());
    auth.dispose();
    progress = new RPCProgress(frame, "Работа в сети", Dialog.ModalityType.APPLICATION_MODAL);
    progress.setProgressValue(10);

    boolean is64bit = false;
    String osArch;
    String osName = System.getProperty("os.name");
    try {
      if (osName.contains("Windows")) {
        is64bit = (System.getenv("ProgramFiles(x86)") != null);
      } else {
        is64bit = (System.getProperty("os.arch").contains("64"));
      }
    } catch (Exception ignored) {
    }
    osArch = (is64bit ? "x64" : System.getProperty("os.arch"));
    if (osName.contains("Windows 7") || osName.contains("Windows Vista")) isVistaSeven = true;

    data1NewIP = userProfile;
    data2NewIP = new Object[]{ver, System.getProperty("java.version"), osName + " " + osArch + " " + System.getProperty("sun.os.patch.level"), tzIDJava, tzIDOS};

    new At_net("TERMINAL_AUTORIZE", new Request(userProfile, data2NewIP), this).start();
    progress.setVisible(true);
    PREF.put("login", auth.getUserName());

    Runtime.getRuntime().addShutdownHook(new Thread() {
      public void run() {
        shutdown();
      }
    });

    //Center the window
    Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
    Dimension frameSize = frame.getSize();
    if (frameSize.height > screenSize.height - 100) {
      frameSize.height = screenSize.height;
    }
    if (frameSize.width > screenSize.width) {
      frameSize.width = screenSize.width;
    }
    frame.setLocation((screenSize.width - frameSize.width) / 2, (screenSize.height - 100 - frameSize.height) / 2);
    if ((Boolean) needUpdate[0] && (Long) needUpdate[2] > 0) {
      String sizeStr;
      long size = (Long) needUpdate[2] / 1024;
      if (size < 1024) sizeStr = size + " Кб";
      else sizeStr = (size / 1024) + " Мб";
      if (RPCDialog.showMessageDialog(frame, "Вышла новая версия терминала управления " + needUpdate[1] + "<br>Размер обновления: " + sizeStr + "<br>Хотите загрузить сейчас?", "Вопрос", SwingConstants.LEFT) == RPCDialog.Result.OK) {
        progress = new RPCProgress(frame, "Работа в сети", Dialog.ModalityType.APPLICATION_MODAL);
        progress.setProgressValue(0);
        new At_net("GET_UPDATE", new Request(userProfile, new Object[]{ver}), this).start();
        progress.setVisible(true);
      }
    } else {
      if (auth.getPassStrength() < PasswordChecker.THRESHOLD) {
        RPCMessage.showMessageDialog(frame, "Вы используете слишком простой пароль, необходимо сменить пароль");
        new ChPassDialog(frame, Dialog.ModalityType.APPLICATION_MODAL).setVisible(true);
      }
      if (tzIDOS < 131793152L) {
        if (RPCDialog.showMessageDialog(frame, "<font size=3>Вероятно ваша операционная система требует обновления. В связи с изменением законодательства РФ в отношении часовых поясов и отмены перехода на летнее время необходимо загрузить и установить обновление Windows KB2570791. В противном случае время в программе может отображаться неправильно. Загрузить сейчас?</font>") == RPCDialog.Result.OK) {
          TimeZoneUtils.updateOSTz();
        }
      }
      try {
        int year = Integer.parseInt(tzIDJava.substring(6, 10));
        String v = tzIDJava.substring(10);
        boolean needTZUpdate = false;
        if (year < 2011) needTZUpdate = true;
        else if (year == 2011) {
          if (v.equals("a") || v.equals("b") || v.equals("c") || v.equals("d") || v.equals("e") || v.equals("f") || v.equals("g")) needTZUpdate = true;
        }
        if (needTZUpdate) {
          if (RPCDialog.showMessageDialog(frame, "<font size=3>Ваша исполняющая среда Java требует обновления. В связи с изменением законодательства РФ в отношении часовых поясов и отмены перехода на летнее время необходимо обновить исполняющую среду Java. В противном случае время в программе может отображаться неправильно. Обновить сейчас?</font>") == RPCDialog.Result.OK) {
            RPCMessage rpcMessage = RPCMessage.showMessage(frame, "Идет обновление среды Java, пожалуйста подождите...", "Обновление", SwingConstants.LEFT);
            boolean updRes = false;
            try {
              updRes = TimeZoneUtils.updateJavaTz();
            } finally {
              rpcMessage.dispose();
            }
            if (updRes) RPCMessage.showMessageDialog(frame, "Обновление установлено. Необходимо повторно запустить программу чтобы обновление вступило в силу");
            else {
              if (isVistaSeven) RPCMessage.showMessageDialog(frame, "<font size=3>Обновление установить не удалось. Перезапустите программу от имени администратора (Нажмите правой кнопкой мыши на ярлык и выберите \"Запуск от имени администратора\")</font>");
              else RPCMessage.showMessageDialog(frame, "Обновление прошло с ошибками, убедитесь, что у вас есть права администратора");
            }
          }
        }
      } catch (Exception ignored) {
      }
    }
  }

  //отдельный поток
  private void shutdown() {
    if (user != null) {
      Thread at = new Thread(new At_net("USER_EXIT", user, this));
      at.start();
      if (dataUpdater != null) {
        dataUpdater.saveResults();
      }
      try {at.join(10000);}catch (InterruptedException ignored) {}
    }
  }

  public void actionPerformed(ActionEvent e) {
    if (e.getActionCommand().equals("ret_TERMINAL_AUTORIZE") || e.getActionCommand().equals("ret_KIOSKNET_STATISTIC")) {
      progress.setProgressValue(progress.getProgressValue() + 10);
    }
    if (e.getActionCommand().equals("finished_TERMINAL_AUTORIZE")) {
      Request result = (Request) ((At_net) e.getSource()).getResult();
      user = result.user;
      if (user.name.equals("DENIED") || user.name.equals("LOCKED")) {
        if (user.name.equals("DENIED")) RPCMessage.showMessageDialog(progress, "Указано неправильное имя или пароль");
        if (user.name.equals("LOCKED")) RPCMessage.showMessageDialog(progress, "Доступ запрещен, поскольку такие имя и пароль уже используются");
        System.exit(1);
      } else {
        if (usingNewIP) {
          File conf = new File("config.txt");
          File temp = new File("config.tmp");
          StringBuilder buf = new StringBuilder();
          String line;
          try {
            try (BufferedReader in = new BufferedReader(new FileReader(conf))) {
              while ((line = in.readLine()) != null) {
                buf.append(line).append("\r\n");
              }
            }
            line = buf.toString();
            line = line.replaceFirst("KNC_ip=.*", "KNC_ip=" + newIP);
            try (BufferedWriter out = new BufferedWriter(new FileWriter(temp, false))) {
              out.write(line);
            }
            if (!conf.delete()) throw new Exception("Не удалось удалить старый файл конфигурации");
            if (!temp.renameTo(conf)) throw new Exception("Не удалось заменить старый файл конфигурации");
          } catch (Exception ex) {
            if (isVistaSeven) RPCMessage.showMessageDialog(frame, "<font size=3>Не удалось изменить адрес сервера. Перезапустите программу от имени администратора (Нажмите правой кнопкой мыши на ярлык и выберите \"Запуск от имени администратора\")</font>");
            else RPCMessage.showMessageDialog(frame, "Не удалось изменить адрес сервера: " + ex.getMessage());
          }
        }
        new Ticker().start();
        try {
          SwingUtilities.invokeAndWait(new Runnable() {
            public void run() {
              frame.completeInit();
            }
          });
        } catch (Exception ignored) {
        }//не должно быть
        new At_net("KIOSKNET_STATISTIC", user, this).start();
      }
      needUpdate = result.data;
    }
    if (e.getActionCommand().equals("finished_KIOSKNET_STATISTIC")) {
      progress.setProgressValue(100);
      KioskNetProfile kioskNetProfile = (KioskNetProfile) ((At_net) e.getSource()).getResult();
      frame.update(kioskNetProfile);
      progress.dispose();
    }
    if (e.getActionCommand().equals("fail_TERMINAL_AUTORIZE") || e.getActionCommand().equals("fail_KIOSKNET_STATISTIC")) {
      if (ACTIVE_CHANGING_IP && usingNewIP == false && !newIP.equals(serverIP) && e.getActionCommand().equals("fail_TERMINAL_AUTORIZE")) {
        serverIP = newIP;
        usingNewIP = true;
        progress.setProgressValue(15);
        new At_net("TERMINAL_AUTORIZE", new Request(data1NewIP, data2NewIP), this).start();
      } else {
        RPCMessage.showMessageDialog(progress, "Ошибка соединения с центральным сервером");
        System.exit(1);
      }
    }
    if (e.getActionCommand().equals("ret_GET_UPDATE")) {
      if (e.getID() > 10) {
        double proc = e.getID() / ((Long) needUpdate[2]).doubleValue() * 100;
        progress.setProgressValue((int) proc);
      }
    }
    if (e.getActionCommand().equals("finished_GET_UPDATE")) {
      progress.setProgressValue(100);
      progress.dispose();
      Object[] result = (Object[]) ((At_net) e.getSource()).getResult();
      File updFile = new File("updates", (String) result[0]);
      byte[] file = (byte[]) result[1];
      try (FileOutputStream out = new FileOutputStream(updFile)) {
        out.write(file);
        RPCMessage.showMessageDialog(frame, "Обновление будет установлено при следующем запуске программы");
      } catch (IOException ex) {
        if (isVistaSeven) RPCMessage.showMessageDialog(frame, "<font size=3>Не удалось сохранить обновление. Перезапустите программу от имени администратора (Нажмите правой кнопкой мыши на ярлык и выберите \"Запуск от имени администратора\")</font>");
        else RPCMessage.showMessageDialog(frame, "Не удалось сохранить обновление: " + ex.getMessage());
      }
    }
    if (e.getActionCommand().equals("fail_GET_UPDATE")) {
      RPCMessage.showMessageDialog(progress, "Ошибка соединения с центральным сервером");
      progress.dispose();
    }
  }

  public static void sortGateways(ArrayList<String> gatewayList) {
    final String[] gatewayReverseOrder = new String[]{"PIN", "CYBERPLAT", "EPORT", "FMSA", "FMSK", "GREENPOST", "GORODK", "RPC"};
    HashSet<String> set = new HashSet<>(gatewayList);
    gatewayList.clear();
    gatewayList.addAll(set);
    for (String gatewayOrder : gatewayReverseOrder) {
      for (int j = 0; j < gatewayList.size(); j++) {
        String gateway = gatewayList.get(j);
        if (gateway.equals(gatewayOrder)) {
          gatewayList.remove(j);
          gatewayList.add(0, gateway);
          break;
        }
      }
    }
  }

  public static String getGatewayName(String gateway) {
    if (gateway.equals("RPC")) return "РПЦ";
    else if (gateway.equals("GORODK")) return "Город-К";
    else if (gateway.equals("GREENPOST")) return "GreenPost";
    else if (gateway.equals("FMSK")) return "ФМС КК";
    else if (gateway.equals("FMSA")) return "ФМС РА";
    else if (gateway.equals("EPORT")) return "E-Port";
    else if (gateway.equals("CYBERPLAT")) return "CyberPlat";
    else if (gateway.equals("PIN")) return "Пин-коды";
    else if (gateway.equals("FMS")) return "ФМС";
    else return gateway;
  }

  public static String getGatewayByID(int id) {
    if (id > -5500 && id <= -5000) return "FMS";
    else if (id > -4200 && id <= -4000) return "GREENPOST";
    else if (id > -4000 && id <= -2000) return "GORODK";
    else if (id == -1120) return "RPC";
    else if (id > -2000 && id <= -1000) return "CYBERPLAT";
    else if (id > -1000 && id <= 1000) return "RPC";
    else if (id > 1000 && id <= 10000) return "EPORT";
    else return String.valueOf(id);
  }

  public static ArrayList<ProductID<String, String>> getProductIDs(KioskProfile[] kiosks) {
    ArrayList<ProductID<String, String>> productIDs = new ArrayList<>();
    Object[][] productList = kiosks[0].getProductList();
    for (Object[] aProductList : productList) {
      productIDs.add(new ProductID<>(aProductList[0].toString(), aProductList[1].toString()));
    }
    for (int k = 1; k < kiosks.length; k++) {
      productList = kiosks[k].getProductList();
      int i = 0;
      while (i < productIDs.size()) {
        ProductID<String, String> productID = productIDs.get(i);
        boolean matchFlag = false;
        for (Object[] aProductList : productList) {
          if (productID.id.equals(aProductList[1])) {
            matchFlag = true;
            break;
          }
        }
        if (matchFlag) i++;
        else productIDs.remove(i);
      }
    }
    //если есть услуги с одинаковым названием, но разными шлюзами в одном киоске
    for (int i = 0; i < productIDs.size() - 1; i++) {
      String str = productIDs.get(i).name;
      ProductID<String, String> prod;
      String g;
      for (int j = i + 1; j < productIDs.size(); j++) {
        if (str.equals((productIDs.get(j)).name)) {
          prod = productIDs.get(i);
          try {
            g = getGatewayByID(Integer.parseInt(prod.id));
          } catch (NumberFormatException ex) {
            g = null;
          }
          g = getGatewayName(g);
          prod.name = prod.name + " [" + g + "]";
          prod = productIDs.get(j);
          try {
            g = getGatewayByID(Integer.parseInt(prod.id));
          } catch (NumberFormatException ex) {
            g = null;
          }
          g = getGatewayName(g);
          prod.name = prod.name + " [" + g + "]";
        }
      }
    }
    return productIDs;
  }

  public static double round(double value, int decimalPlaces) {
    if (Double.isNaN(value)) return 0.0d;
    if (Double.isInfinite(value)) return value;
    BigDecimal x = new BigDecimal(Double.toString(value));
    x = x.setScale(decimalPlaces, BigDecimal.ROUND_HALF_UP);
    return x.doubleValue();
  }

  public static Date endOfDay(Date beginningOfDay) {//получает дату с 0:00:00.000 возвращает дату с 23:59:59.999
    GregorianCalendar cal = new GregorianCalendar();
    cal.setTime(beginningOfDay);
    cal.add(Calendar.DATE, 1);
    cal.add(Calendar.MILLISECOND, -1);
    return cal.getTime();
  }

  public static String getPrinterName(int p, boolean showSS) {
    String ss = "";
    if (p > 100) {
      p -= 100;
      if (showSS) ss = "СС ";
    }
    switch (p) {
      case 1:
        return "EPSON EU-T500 60мм";
      case 2:
        return "Citizen PPU-700 80мм";
      case 3:
        return "CUSTOM VKP-80 80мм";
      case 4:
        return "Citizen CT-S2000 80мм";
      case 5:
        return "Citizen PPU-700 80мм (длинный чек)";
      case 6:
        return "Star TUP 900 80мм";
      case 7:
        return "CUSTOM TPT 52 60мм";
      case 8:
        return "ШТРИХ-МИНИ 80мм";
      case 9:
        return "ШТРИХ-КОМБО 80мм";
      case 10:
        return "ШТРИХ-КИОСК 80мм";
      case 11:
        return ss + "ФР Pay VKP-80K 80мм";
      case 12:
        return ss + "ФР ШТРИХ-МИНИ 80мм";
      case 13:
        return ss + "ФР ЯРУС-02К 80мм";
      case 14:
        return ss + "ФР ЯРУС-01К 80мм";
      case 21:
        return "Сетевой CUSTOM VKP-80 80мм";
      default:
        return "Неизвестный";
    }
  }

  public static void main(String[] args) {
    try {
      Hashtable<Object, Object> defaults = UIManager.getDefaults();
      Hashtable<Object, Object> changes = new Hashtable<>();
      Enumeration<Object> enumer = defaults.keys();
      while (enumer.hasMoreElements()) {
        Object key = enumer.nextElement();
        String keyStr = key.toString();
        if (keyStr.startsWith("ComboBox") || keyStr.startsWith("Button")
                || keyStr.startsWith("ProgressBar") || keyStr.startsWith("Table")
                || keyStr.startsWith("ScrollBar") || keyStr.equals("control")
                || keyStr.startsWith("CheckBox") || keyStr.startsWith("Panel")
                || (keyStr.startsWith("PopupMenu") && !keyStr.equals("PopupMenuUI") && !keyStr.equals("PopupMenuSeparatorUI"))
                || keyStr.startsWith("Menu")
                || keyStr.startsWith("OptionPane") || keyStr.startsWith("FileChooser")
                || keyStr.startsWith("TabbedPane") || keyStr.startsWith("ToggleButton")
                || keyStr.startsWith("Slider")) {
          Object value = defaults.get(key);
          if (value != null) changes.put(key, value);
        }
      }
      UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
      L10n.localize();
      enumer = changes.keys();
      while (enumer.hasMoreElements()) {
        Object key = enumer.nextElement();
        Object value = changes.get(key);
        UIManager.put(key, value);
      }
      UIManager.put("Button.textShiftOffset", 1);//нужно для SlimButton
      UIManager.put("ToggleButton.textShiftOffset", 1);//нужно для SlimToggleButton
      InputMap im = (InputMap) UIManager.get("Button.focusInputMap");
      im.put(KeyStroke.getKeyStroke(KeyEvent.VK_ENTER, 0, false), "pressed");
      im.put(KeyStroke.getKeyStroke(KeyEvent.VK_ENTER, 0, true), "released");
      login = PREF.get("login", "");
      if (args.length > 0) login = args[0];
      if (Updater.checkUpdates() == 1) System.exit(0);
      deleteFolder(new File("jre1.5.0_19"));
    } catch (Exception e) {
      e.printStackTrace();
      System.exit(-1);
    }
    new KNC_Terminal();
  }

  private static File getUserHome(String folder) {
    try {
      File home = new File(System.getProperty("user.home"));
      if (folder == null || folder.length() == 0) return home;
      File temp;

      temp = new File(home, "AppData");// Vista, Seven
      if (temp.isDirectory()) {
        temp = new File(temp, "Local");
        if (!temp.isDirectory()) return home;
        temp = new File(temp, folder);
        if (temp.isDirectory()) return temp;
        if (temp.mkdir()) return temp;
        return home;
      }

      temp = new File(home, "Application Data");// более старая Windows
      if (temp.isDirectory()) {
        temp = new File(temp, folder);
        if (temp.isDirectory()) return temp;
        if (temp.mkdir()) return temp;
        return home;
      }

      temp = new File(home, folder);// всё остальное
      if (temp.isDirectory()) return temp;
      if (temp.mkdir()) return temp;
      return home;
    } catch (Exception ignored) {
    }
    return null;
  }

  private static boolean deleteFolder(File folder) {
    if (!folder.isDirectory()) return false;
    File[] files = folder.listFiles();
    if (files == null) return false;
    boolean result = true;
    for (File file : files) {
      if (file.isDirectory()) result &= deleteFolder(file);
      else result &= file.delete();
    }
    result &= folder.delete();
    return result;
  }
}
//todo фильтры с пкм на панель