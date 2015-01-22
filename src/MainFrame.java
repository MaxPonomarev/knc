import java.awt.*;
import java.awt.event.*;
import java.util.*;
import javax.swing.*;

class MainFrame extends JFrame implements ActionListener {
  private int autoFiltersCount = 0;
  private TreeMap<String,String> autoFilters = new TreeMap<>();

  private TableFrame tableFrame = null;
  private StatFrame statisticsFrame = null;
  private SettingsFrame settingsFrame = null;
  private FinanceFrame financeFrame = null;
  private ReportFrame reportFrame = null;
  private JPanel contentPane;
  private JLayeredPane layeredPane = new JLayeredPane();
  private JLabel background = new JLabel();
  private JLabel chooseLabel = new JLabel();
  private JComboBox<SelectID> choiceComboBox = new JComboBox<>();
  private JButton nextButton = new JButton();
  private JLabel[] stat1Label = new JLabel[5];
  private JLabel[] stat2Label = new JLabel[5];

  public MainFrame() {
    try {
      jbInit();
    }
    catch(Exception e) {
      e.printStackTrace();
      System.exit(-1);
    }
  }

  private void jbInit() {
    contentPane = (JPanel) this.getContentPane();
    contentPane.setLayout(null);
    layeredPane.setSize(480, 360);
    this.setIconImages(KNC_Terminal.icons);
    this.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
    this.setResizable(false);
    this.setSize(new Dimension(480, 360));
    this.setTitle("�������� ���������� ������ "+KNC_Terminal.ver);
    background.setBounds(0, 0, 480, 360);
    background.setIcon(new ImageIcon("images/main.png"));
    chooseLabel.setText("�������� ��������:");
    chooseLabel.setBounds(140, 50, 150, 25);
    chooseLabel.setFont(new Font("Dialog", Font.BOLD, 12));
    chooseLabel.setForeground(Color.WHITE);
    choiceComboBox.setBounds(140, 85, 220, 30);
    choiceComboBox.setFont(new Font("Dialog", Font.BOLD, 12));
    choiceComboBox.setBorder(null);
    choiceComboBox.setMaximumRowCount(14);
    nextButton.setBounds(380, 61, 80, 80);
    nextButton.setIcon(new ImageIcon("images/but3.png"));
    nextButton.setPressedIcon(new ImageIcon("images/but3pressed.png"));
    nextButton.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        nextButton_actionPerformed();
      }
    });
    nextButton.setBorderPainted(false);
    nextButton.setContentAreaFilled(false);
    stat1Label[0] = new JLabel(" ����� �������");
    stat1Label[1] = new JLabel(" ���������� ��������");
    stat1Label[2] = new JLabel(" ����������� �������");
    stat1Label[3] = new JLabel(" ���������� �� (����������)");
    stat1Label[4] = new JLabel(" � ���������� �����");
    int y = 140;
    int x = 200;
    for (int i=0; i<5; i++) {
      if (i > 2) x = 230;
      stat1Label[i].setBounds(x, y, 180, 30);
      stat1Label[i].setFont(new Font("Dialog", Font.BOLD, 12));
      stat1Label[i].setForeground(new Color(0xb5b5ff));
      stat2Label[i] = new JLabel("0");
      stat2Label[i].setBounds(425, y, 30, 30);
      stat2Label[i].setFont(new Font("Dialog", Font.BOLD, 12));
      stat2Label[i].setForeground(new Color(0xb5b5ff));
      layeredPane.add(stat1Label[i], new Integer(1));
      layeredPane.add(stat2Label[i], new Integer(1));
      y += 30;
    }
    layeredPane.add(background, new Integer(0));
    layeredPane.add(chooseLabel, new Integer(1));
    layeredPane.add(choiceComboBox, new Integer(1));
    layeredPane.add(nextButton, new Integer(1));
    contentPane.add(layeredPane);
  }

  public void completeInit() {
    tableFrame = new TableFrame();
    statisticsFrame = new StatFrame();
    settingsFrame = new SettingsFrame();
    financeFrame = new FinanceFrame();
    reportFrame = new ReportFrame();
    if (choiceComboBox.getItemCount() == 0) {
      choiceComboBox.addItem(new SelectID(" �������� ������� �������", 0));
      if (KNC_Terminal.user.level >= 1) {
        choiceComboBox.addItem(new SelectID(" �������� ������� ����������", 12));
        choiceComboBox.addItem(new SelectID(" �������� ������� ��������", 11));
        choiceComboBox.addItem(new SelectID(" �������� ���������� ������", 8));
        choiceComboBox.addItem(new SelectID(" �������� �����", 10));
        if (KNC_Terminal.user.companies != null) choiceComboBox.addItem(new SelectID(" �������� ������ Z-�������", 13));
        choiceComboBox.addItem(new SelectID(" ����������� �� ��������", 9));
      }
      choiceComboBox.addItem(new SelectID(" �������� ������", 1));
      if (KNC_Terminal.user.level >= 3) {
        choiceComboBox.addItem(new SelectID(" �������� ������������", 2));
        choiceComboBox.addItem(new SelectID(" �������� ����� ������������", 3));
        choiceComboBox.addItem(new SelectID(" ������� ������������", 4));
        choiceComboBox.addItem(new SelectID(" �������� ������ �������������", 5));
      }
      if (KNC_Terminal.user.level >= 4) {
        choiceComboBox.addItem(new SelectID(" �������� ���-���� �������", 6));
        choiceComboBox.addItem(new SelectID(" ���������� ������ �������", 7));
      }
    }
  }

  public synchronized void update(KioskNetProfile kioskNetProfile) {
    for (int i=0; i<5; i++) {
      stat2Label[i].setText(kioskNetProfile.data[i]+"");
    }
  }

  public void updateReportFrame(final Object[] report) {
    SwingUtilities.invokeLater(new Runnable() {
      public void run() {
        reportFrame.update(report);
      }
    });
  }

  public void updatingKioskListStart() {
    tableFrame.setUpdatingStart();
  }

  public void updatingKioskListFinish() {
    tableFrame.setUpdatingFinish();
  }

  public void interrupt() {
    KNC_Terminal.interruptTitle = " (������ ��������)";
    this.setTitle("�������� ���������� ������ "+KNC_Terminal.ver+KNC_Terminal.interruptTitle);
    tableFrame.interrupt();
  }

  void nextButton_actionPerformed() {
    int sid = choiceComboBox.getItemAt(choiceComboBox.getSelectedIndex()).id;
    switch (sid) {
      case 0: //�������� ������� �������
        tableFrame.setVisible(true);
        tableFrame.setState(Frame.NORMAL);
        break;
      case 12: //�������� ������� ����������
        statisticsFrame.setVisible(true);
        statisticsFrame.setState(Frame.NORMAL);
        break;
      case 11: //�������� ������� ��������
        settingsFrame.setVisible(true);
        settingsFrame.setState(Frame.NORMAL);
        break;
      case 8: //�������� ���������� �������
        financeFrame.setVisible(true);
        financeFrame.setState(Frame.NORMAL);
        break;
      case 10: //�������� �����
        reportFrame.setVisible(true);
        reportFrame.setState(Frame.NORMAL);
        break;
      case 13: //�������� ������ Z-�������
        new GetZReportsDialog(this, Dialog.ModalityType.DOCUMENT_MODAL).setVisible(true);
        break;
      case 9: //����������� �� ��������
        new SubscribeUserDialog(this, Dialog.ModalityType.DOCUMENT_MODAL).setVisible(true);
        break;
      case 1: //�������� ������
        new ChPassDialog(this, Dialog.ModalityType.DOCUMENT_MODAL).setVisible(true);
        break;
      case 2: //�������� ������������
        new AddUserDialog(this, Dialog.ModalityType.MODELESS).setVisible(true);
        break;
      case 3: //�������� ����� ������������
        new ChUserDialog(this, Dialog.ModalityType.MODELESS).setVisible(true);
        break;
      case 4: //������� ������������
        new DelUserDialog(this, Dialog.ModalityType.MODELESS).setVisible(true);
        break;
      case 5: //�������� ������ �������������
        new UserListDialog(this, Dialog.ModalityType.MODELESS).setVisible(true);
        break;
      case 6: //�������� ���� �������
        new GetLogDialog2(this, Dialog.ModalityType.DOCUMENT_MODAL).setVisible(true);
        break;
      case 7: //���������� ������ �������
        if (RPCDialog.showMessageDialog(this, "�� ������������� ������ ���������� ����������� ������ ������� ����������?") == RPCDialog.Result.OK) {
          new At_net("STOP_SERVER", KNC_Terminal.user, this).start();
        }
        break;
    }
  }

  public TreeMap<String,String> getAutoFilters() {
    return autoFilters;
  }

  public void updateAutoFilters(ArrayList<?> list) {
    if (list.size() != autoFiltersCount) {
      autoFiltersCount = list.size();
      HashSet<String> numbers = new HashSet<>();
      Object test = list.get(0);
      if (test instanceof KioskProfile) {
        for (Object o : list) {
          String kioskNumber = (String) ((KioskProfile) o).data[1];
          numbers.add(kioskNumber);
        }
      } else if (test instanceof FinanceProfile) {
        for (Object o : list) {
          String kioskNumber = (String) ((FinanceProfile) o).data[1];
          numbers.add(kioskNumber);
        }
      } else if (test instanceof StatProfile) {
        for (Object o : list) {
          String kioskNumber = ((StatProfile) o).number;
          numbers.add(kioskNumber);
        }
      } else if (test instanceof String) {
        for (Object o : list) {
          String kioskNumber = (String) o;
          numbers.add(kioskNumber);
        }
      }
      TreeSet<String> set = new TreeSet<>();
      for (String kioskNumber : numbers) {
        int knl = kioskNumber.length();
        if (knl > 2) {
          String key = kioskNumber.substring(0,knl-2);
          if (!set.contains(key)) set.add(key);
        }
      }
      TreeMap<String,String> mapFinal = new TreeMap<>();
      for (String key : set) {
        mapFinal.put("����������: "+key+"xx", key);
      }
      if (mapFinal.size() >= 2) {
        //���������� ����������� �� ������� (������ 2 �����)
        TreeMap<String,HashSet<String>> regionMap = new TreeMap<>();
        for (String kioskNumber : set) {
          int knl = kioskNumber.length();
          if (knl > 2) {
            String key = kioskNumber.substring(0,2);
            if (!regionMap.containsKey(key)) regionMap.put(key, new HashSet<String>());
            regionMap.get(key).add(kioskNumber);
          }
        }
        for (String key : regionMap.keySet()) {
          HashSet<String> regionSet = regionMap.get(key);
          if (regionSet.size() >= 2) {//���� ������ 1 ����������� � ���� ��������
            mapFinal.put("����������: "+key, key);
          }
        }
        //^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^
        autoFilters = mapFinal;
        KNC_Terminal.filterMenuFactory.reconstAutoFilters(autoFilters);
      }
    }
  }

  public void actionPerformed(ActionEvent e) {
    if (e.getActionCommand().equals("finished_STOP_SERVER")) {
      new At_net("USER_EXIT", KNC_Terminal.user, this).start();
      if (((At_net)e.getSource()).getResult().equals("OK")) RPCMessage.showMessageDialog(this, "����������� ������ ������� ����������");
    }
    if (e.getActionCommand().equals("fail_STOP_SERVER")) {
      RPCMessage.showMessageDialog(this, "������ ���������� � ����������� ��������");
    }
  }
}
