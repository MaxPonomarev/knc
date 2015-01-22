import java.util.HashSet;
import javax.swing.table.TableColumn;

import org.apache.poi.hssf.usermodel.*;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.util.CellRangeAddress;
import org.jdesktop.swingx.JXTable;

//для разработчика: 1 пиксель ширины колонки примерно 36,57; округлять до кратного 256
class ExcelCreater {
  private static final String excelColumns = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
  private final HSSFWorkbook wb;
  private final String name;
  private final JXTable table;
  private HSSFSheet sheet;
  private HSSFFont font;
  private HSSFCellStyle footerStyle1;
  private HSSFCellStyle footerStyle2;
  private HSSFCellStyle footerStyle3;
  private HSSFCellStyle footerStyle4;

  public ExcelCreater(HSSFWorkbook wb, String name, JXTable table) {
    this.wb = wb;
    this.name = name;
    this.table = table;
  }

  public HSSFWorkbook createRegistrySheet() {
    createSheet(true, 0, 1);
    Integer[] columnsWidth = new Integer[table.getModel().getColumnCount()];
    columnsWidth[1] = 2048;//№
    columnsWidth[2] = 4608;//Время
    columnsWidth[8] = 2304;//Внесено
    columnsWidth[9] = 2780;//Зачислено
    columnsWidth[10] = 2487;//Комис.(%)
    columnsWidth[11] = 3072;//Комис.(руб.)
    columnsWidth[14] = 3291;//Статус
    createHeader(columnsWidth, 0);
    int[] formatColumns = new int[2];
    formatColumns[0] = 9;//Зачислено
    formatColumns[1] = 11;//Комис.(руб.)
    byte cor = createTable(formatColumns, 1);
    createFooterStyles();
    HSSFRow row = sheet.createRow(table.getRowCount()+1+cor);
    for (int i=0; i<table.getColumnCount(false); i++) {
      if (row.getRowNum() < 2) break;
      int modelColumn = table.convertColumnIndexToModel(i);
      HSSFCell cell = row.createCell(i);
      String excelColumn = getColumnAB(i);
      if (modelColumn == 1) cell.setCellFormula("COUNTA("+excelColumn+"2:"+excelColumn+row.getRowNum()+")");
      if (modelColumn == 8) cell.setCellFormula("SUM("+excelColumn+"2:"+excelColumn+row.getRowNum()+")");
      if (modelColumn == 9) cell.setCellFormula("SUM("+excelColumn+"2:"+excelColumn+row.getRowNum()+")");
      if (modelColumn == 10) {
        int div1 = table.convertColumnIndexToView(11);
        int div2 = table.convertColumnIndexToView(9);
        String v;
        if (div1 < 0 || div2 < 0) v = table.getModel().getValueAt(table.getModel().getRowCount()-1,11).toString()+"/"+table.getModel().getValueAt(table.getModel().getRowCount()-1,9).toString();
        else v = getColumnAB(div1)+(row.getRowNum()+1)+"/"+getColumnAB(div2)+(row.getRowNum()+1);
        cell.setCellFormula(v+"*100");
      }
      if (modelColumn == 11) cell.setCellFormula("SUM("+excelColumn+"2:"+excelColumn+row.getRowNum()+")");
      if (modelColumn == 1 || modelColumn == 8 || modelColumn == 9 || modelColumn == 10 || modelColumn == 11) {
        cell.setCellType(HSSFCell.CELL_TYPE_FORMULA);
      }
      cell.setCellStyle(footerStyle1);
      if (modelColumn == 9 || modelColumn == 10 || modelColumn == 11) {
        cell.setCellStyle(footerStyle2);
      }
    }
    return wb;
  }

  public HSSFWorkbook createIncassSheet() {
    createSheet(true, 0, 1);
    Integer[] columnsWidth = new Integer[table.getModel().getColumnCount()];
    columnsWidth[1] = 4608;//Дата
    columnsWidth[2] = 2780;//Сумма
    columnsWidth[3] = 2780;//Переплата
    columnsWidth[4] = 2304;//Купюр
    columnsWidth[5] = 2304;//Купюра
    columnsWidth[6] = 2304;//Купюра
    columnsWidth[7] = 2304;//Купюра
    columnsWidth[8] = 2304;//Купюра
    columnsWidth[9] = 2304;//Купюра
    columnsWidth[10] = 2304;//Купюра
    createHeader(columnsWidth, 0);
    byte cor = createTable(null, 1, -1, true);
    createFooterStyles();
    HSSFRow row = sheet.createRow(table.getRowCount()+1+cor);
    for (int i=0; i<table.getColumnCount(false); i++) {
      if (row.getRowNum() < 2) break;
      int modelColumn = table.convertColumnIndexToModel(i);
      HSSFCell cell = row.createCell(i);
      String excelColumn = getColumnAB(i);
      if (modelColumn == 1) cell.setCellFormula("COUNTA("+excelColumn+"2:"+excelColumn+row.getRowNum()+")");
      if (modelColumn >= 2 && modelColumn <= 10) cell.setCellFormula("SUM("+excelColumn+"2:"+excelColumn+row.getRowNum()+")");
      if (modelColumn >= 1 && modelColumn <= 10) cell.setCellType(HSSFCell.CELL_TYPE_FORMULA);
      cell.setCellStyle(footerStyle1);
      if (modelColumn == 1) cell.setCellStyle(footerStyle3);
    }
    return wb;
  }

  public HSSFWorkbook createReport0Sheet(String[] conditionValues, int gatewaysCount) {
    int startRow = 3;
    int skipModelColumn = 0;
    createSheet(true, 0, startRow+1);
    createConditions(conditionValues, 0, 1);
    Integer[] columnsWidth = new Integer[table.getModel().getColumnCount()];
    columnsWidth[3] = 2600;//Выручка
    for (int i=0; i<gatewaysCount; i++) {
      columnsWidth[4+i] = 2600;//Выручка шлюза
    }
    columnsWidth[4+gatewaysCount] = 2600;//Доход
    columnsWidth[5+gatewaysCount] = 4160;//Рентабельность
    columnsWidth[6+gatewaysCount] = 2720;//Операций
    columnsWidth[7+gatewaysCount] = 2440;//Средний платеж
    columnsWidth[8+gatewaysCount] = 2280;//Доля в обороте
    columnsWidth[9+gatewaysCount] = 2000;//Доля в доходе
    columnsWidth[10+gatewaysCount] = 3480;//Актуальность
    createHeader(columnsWidth, startRow, skipModelColumn, true);
    int[] formatColumns = new int[2];
    formatColumns[0] = 4+gatewaysCount;//Доход
    formatColumns[1] = 5+gatewaysCount;//Рентабельность
    byte cor = createTable(formatColumns, startRow+1, skipModelColumn);
    createFooterStyles();
    HSSFRow row = sheet.createRow(table.getRowCount()+1+cor+startRow);
    int ri = 0;
    for (int i=0; i<table.getColumnCount(false); i++) {
      if (row.getRowNum() < 2+startRow) break;
      int modelColumn = table.convertColumnIndexToModel(i);
      if (modelColumn == skipModelColumn) continue;
      HSSFCell cell = row.createCell(ri);
      String excelColumn = getColumnAB(ri);
      if (modelColumn == 1) cell.setCellFormula("COUNTA("+excelColumn+(2+startRow)+":"+excelColumn+row.getRowNum()+")");
      if (modelColumn == 2) cell.setCellValue("Итого");
      if ((modelColumn >= 3 && modelColumn <= 4+gatewaysCount) || modelColumn == 6+gatewaysCount)
        cell.setCellFormula("SUM("+excelColumn+(2+startRow)+":"+excelColumn+row.getRowNum()+")");
      if (modelColumn == 5+gatewaysCount) {
        int div1 = table.convertColumnIndexToView(4+gatewaysCount);
        int div2 = table.convertColumnIndexToView(3);
        if (div1 > table.convertColumnIndexToView(skipModelColumn)) div1--;
        if (div2 > table.convertColumnIndexToView(skipModelColumn)) div2--;
        String v;
        if (div1 < 0 || div2 < 0)
          v = table.getModel().getValueAt(table.getModel().getRowCount()-1,4+gatewaysCount).toString()+"/"+
              table.getModel().getValueAt(table.getModel().getRowCount()-1,3).toString();
        else v = getColumnAB(div1)+(row.getRowNum()+1)+"/"+getColumnAB(div2)+(row.getRowNum()+1);
        cell.setCellFormula(v+"*100");
      }
      if (modelColumn == 7+gatewaysCount) {
        int div1 = table.convertColumnIndexToView(3);
        int div2 = table.convertColumnIndexToView(6+gatewaysCount);
        if (div1 > table.convertColumnIndexToView(skipModelColumn)) div1--;
        if (div2 > table.convertColumnIndexToView(skipModelColumn)) div2--;
        String v;
        if (div1 < 0 || div2 < 0)
          v = table.getModel().getValueAt(table.getModel().getRowCount()-1,3).toString()+"/"+
              table.getModel().getValueAt(table.getModel().getRowCount()-1,6+gatewaysCount).toString();
        else v = getColumnAB(div1)+(row.getRowNum()+1)+"/"+getColumnAB(div2)+(row.getRowNum()+1);
        cell.setCellFormula(v);
      }
      if (modelColumn == 8+gatewaysCount || modelColumn == 9+gatewaysCount) cell.setCellValue(new Double(100));
      if (modelColumn >= 1 && modelColumn <= 7+gatewaysCount && modelColumn != 2) cell.setCellType(HSSFCell.CELL_TYPE_FORMULA);
      if (modelColumn == 1 || modelColumn == 2) cell.setCellStyle(footerStyle3);
      else if (modelColumn == 4+gatewaysCount || modelColumn == 5+gatewaysCount || modelColumn == 7+gatewaysCount) cell.setCellStyle(footerStyle2);
      else cell.setCellStyle(footerStyle1);
      ri++;
    }
    return wb;
  }

  public HSSFWorkbook createReport12Sheet(String[] conditionValues, int gatewaysCount) {
    int startRow = 3;
    createSheet(true, 0, startRow+1);
    createConditions(conditionValues, 0, 0);
    Integer[] columnsWidth = new Integer[table.getModel().getColumnCount()];
    columnsWidth[1] = 2600;//Выручка
    for (int i=0; i<gatewaysCount; i++) {
      columnsWidth[2+i] = 2600;//Выручка шлюза
    }
    columnsWidth[2+gatewaysCount] = 2600;//Доход
    columnsWidth[3+gatewaysCount] = 4160;//Рентабельность
    columnsWidth[4+gatewaysCount] = 2720;//Операций
    columnsWidth[5+gatewaysCount] = 2440;//Средний платеж
    columnsWidth[6+gatewaysCount] = 2280;//Доля в обороте
    columnsWidth[7+gatewaysCount] = 2000;//Доля в доходе
    createHeader(columnsWidth, startRow, true);
    int[] formatColumns = new int[2];
    formatColumns[0] = 2+gatewaysCount;//Доход
    formatColumns[1] = 3+gatewaysCount;//Рентабельность
    byte cor = createTable(formatColumns, startRow+1);
    createFooterStyles();
    HSSFRow row = sheet.createRow(table.getRowCount()+1+cor+startRow);
    for (int i=0; i<table.getColumnCount(false); i++) {
      if (row.getRowNum() < 2+startRow) break;
      int modelColumn = table.convertColumnIndexToModel(i);
      HSSFCell cell = row.createCell(i);
      String excelColumn = getColumnAB(i);
      if (modelColumn == 0) cell.setCellFormula("CONCATENATE(\"Итого: \",COUNTA("+excelColumn+(2+startRow)+":"+excelColumn+row.getRowNum()+"))");
      if ((modelColumn >= 1 && modelColumn <= 2+gatewaysCount) || modelColumn == 4+gatewaysCount)
        cell.setCellFormula("SUM("+excelColumn+(2+startRow)+":"+excelColumn+row.getRowNum()+")");
      if (modelColumn == 3+gatewaysCount) {
        int div1 = table.convertColumnIndexToView(2+gatewaysCount);
        int div2 = table.convertColumnIndexToView(1);
        String v;
        if (div1 < 0 || div2 < 0)
          v = table.getModel().getValueAt(table.getModel().getRowCount()-1,2+gatewaysCount).toString()+"/"+
              table.getModel().getValueAt(table.getModel().getRowCount()-1,1).toString();
        else v = getColumnAB(div1)+(row.getRowNum()+1)+"/"+getColumnAB(div2)+(row.getRowNum()+1);
        cell.setCellFormula(v+"*100");
      }
      if (modelColumn == 5+gatewaysCount) {
        int div1 = table.convertColumnIndexToView(1);
        int div2 = table.convertColumnIndexToView(4+gatewaysCount);
        String v;
        if (div1 < 0 || div2 < 0)
          v = table.getModel().getValueAt(table.getModel().getRowCount()-1,1).toString()+"/"+
              table.getModel().getValueAt(table.getModel().getRowCount()-1,4+gatewaysCount).toString();
        else v = getColumnAB(div1)+(row.getRowNum()+1)+"/"+getColumnAB(div2)+(row.getRowNum()+1);
        cell.setCellFormula(v);
      }
      if (modelColumn == 6+gatewaysCount || modelColumn == 7+gatewaysCount) cell.setCellValue(new Double(100));
      if (modelColumn >= 0 && modelColumn <= 5+gatewaysCount) cell.setCellType(HSSFCell.CELL_TYPE_FORMULA);
      if (modelColumn == 0) cell.setCellStyle(footerStyle3);
      else if (modelColumn == 2+gatewaysCount || modelColumn == 3+gatewaysCount || modelColumn == 5+gatewaysCount) cell.setCellStyle(footerStyle2);
      else cell.setCellStyle(footerStyle1);
    }
    return wb;
  }

  public HSSFWorkbook createReport34Sheet(String[] conditionValues, byte type) throws ExcelCreaterException {
    int startRow = 3;
    int skipModelColumn = 0;
    int modelColumnCount = table.getModel().getColumnCount();
    if (modelColumnCount-1 > 256) throw new ExcelCreaterException("Данный формат не поддерживает больше 256 колонок");
    if (table.convertColumnIndexToModel(1) == 1 && table.convertColumnIndexToModel(2) == 2)
      createSheet(true, 2, startRow+1);
    else
      createSheet(true, 0, startRow+1);
    createConditions(conditionValues, 0, 1);
    Integer[] columnsWidth = new Integer[modelColumnCount];
    int len = 0;
    if (type == 3) len = 3000;//Выручка за
    if (type == 4) len = 2600;//Доход за
    for (int i=3; i<columnsWidth.length-1; i++) {
      columnsWidth[i] = len;
    }
    columnsWidth[columnsWidth.length-1] = 3480;//Актуальность
    createHeader(columnsWidth, startRow, skipModelColumn, true);
    int[] formatColumns = null;
    if (type == 4) {
      formatColumns = new int[modelColumnCount-4];
      for (int i=0; i<formatColumns.length; i++) {
        formatColumns[i] = i+3;//Доход
      }
    }
    byte cor = createTable(formatColumns, startRow+1, skipModelColumn);
    createFooterStyles();
    HSSFRow row = sheet.createRow(table.getRowCount()+1+cor+startRow);
    int ri = 0;
    for (int i=0; i<table.getColumnCount(false); i++) {
      if (row.getRowNum() < 2+startRow) break;
      int modelColumn = table.convertColumnIndexToModel(i);
      if (modelColumn == skipModelColumn) continue;
      HSSFCell cell = row.createCell(ri);
      String excelColumn = getColumnAB(ri);
      if (modelColumn == 1) cell.setCellFormula("COUNTA("+excelColumn+(2+startRow)+":"+excelColumn+row.getRowNum()+")");
      if (modelColumn == 2) cell.setCellValue("Итого");
      if (modelColumn >= 3 && modelColumn <= modelColumnCount-2)
        cell.setCellFormula("SUM("+excelColumn+(2+startRow)+":"+excelColumn+row.getRowNum()+")");
      if (modelColumn >= 1 && modelColumn <= modelColumnCount-2 && modelColumn != 2) cell.setCellType(HSSFCell.CELL_TYPE_FORMULA);
      if (modelColumn == 1 || modelColumn == 2) cell.setCellStyle(footerStyle3);
      else if (modelColumn >= 3 && modelColumn <= modelColumnCount-2) cell.setCellStyle(type==4?footerStyle2:footerStyle1);
      else cell.setCellStyle(footerStyle1);
      ri++;
    }
    return wb;
  }

  public HSSFWorkbook createTableSheet() {
    int skipModelColumn = 0;
    createSheet(true, 0, 1);
    Integer[] columnsWidth = new Integer[table.getModel().getColumnCount()];
    columnsWidth[1] = 2048;//№
    columnsWidth[3] = 4608;//Дата инкассации
    columnsWidth[4] = 2304;//Сумма
    columnsWidth[5] = 2304;//Купюры
    columnsWidth[6] = 2304;//Стекер
    columnsWidth[7] = 2780;//Операции
    columnsWidth[8] = 2780;//Ошибки
    columnsWidth[9] = 2780;//Лента
    columnsWidth[10] = 2304;//Буфер
    columnsWidth[12] = 5376;//Последняя операция
    createHeader(columnsWidth, 0, skipModelColumn);
    byte cor = createTable(null, 1, skipModelColumn);
    createFooterStyles();
    HSSFRow row = sheet.createRow(table.getRowCount()+1+cor);
    int ri = 0;
    for (int i=0; i<table.getColumnCount(false); i++) {
      if (row.getRowNum() < 2) break;
      int modelColumn = table.convertColumnIndexToModel(i);
      if (modelColumn == skipModelColumn) continue;
      HSSFCell cell = row.createCell(ri);
      String excelColumn = getColumnAB(ri);
      if (modelColumn == 1) cell.setCellFormula("COUNTA("+excelColumn+"2:"+excelColumn+row.getRowNum()+")");
      if (modelColumn == 2) cell.setCellValue("Итого");
      if (modelColumn == 4) cell.setCellFormula("SUM("+excelColumn+"2:"+excelColumn+row.getRowNum()+")");
      if (modelColumn == 5) cell.setCellFormula("SUM("+excelColumn+"2:"+excelColumn+row.getRowNum()+")");
      if (modelColumn == 7) cell.setCellFormula("SUM("+excelColumn+"2:"+excelColumn+row.getRowNum()+")");
      if (modelColumn == 8) cell.setCellFormula("SUM("+excelColumn+"2:"+excelColumn+row.getRowNum()+")");
      if (modelColumn == 1 || modelColumn == 4 || modelColumn == 5 || modelColumn == 7 || modelColumn == 8) {
        cell.setCellType(HSSFCell.CELL_TYPE_FORMULA);
      }
      if (modelColumn == 1 || modelColumn == 2) cell.setCellStyle(footerStyle3);
      else cell.setCellStyle(footerStyle1);
      ri++;
    }
    return wb;
  }

  public HSSFWorkbook createStatSheet() {
    int skipModelColumn = 0;
    createSheet(true, 0, 1);
    Integer[] columnsWidth = new Integer[table.getModel().getColumnCount()];
    columnsWidth[1] = 2048;//№
    createHeader(columnsWidth, 0, skipModelColumn);
    byte cor = createTable(null, 1, skipModelColumn);
    createFooterStyles();
    HSSFRow row = sheet.createRow(table.getRowCount()+1+cor);
    int ri = 0;
    for (int i=0; i<table.getColumnCount(false); i++) {
      if (row.getRowNum() < 2) break;
      int modelColumn = table.convertColumnIndexToModel(i);
      if (modelColumn == skipModelColumn) continue;
      if (modelColumn == 7) continue;
      HSSFCell cell = row.createCell(ri);
      String excelColumn = getColumnAB(ri);
      if (modelColumn == 1) cell.setCellFormula("COUNTA("+excelColumn+"2:"+excelColumn+row.getRowNum()+")");
      if (modelColumn == 2) cell.setCellValue("Итого");
      if (modelColumn > 2 && modelColumn < 7) cell.setCellValue(table.getValueAt(table.getRowCount()-1,i).toString());
      if (modelColumn == 1) {
        cell.setCellType(HSSFCell.CELL_TYPE_FORMULA);
      }
      if (modelColumn >= 1 && modelColumn < 7) cell.setCellStyle(footerStyle3);
      else cell.setCellStyle(footerStyle1);
      ri++;
    }
    return wb;
  }

  public HSSFWorkbook createSettingsSheet1() {
    int skipModelColumn = 0;
    createSheet(true, 2, 1);
    Integer[] columnsWidth = new Integer[table.getModel().getColumnCount()];
    columnsWidth[1] = 2048;//№
    columnsWidth[10] = 3328;//Порт КП
    columnsWidth[11] = 4352;//Протокол КП
    columnsWidth[12] = 4352;//Автоинкассация
    columnsWidth[13] = 3072;//Автозапуск
    columnsWidth[14] = 6144;//Время резервирования
    columnsWidth[15] = 5632;//Путь резервирования
    columnsWidth[16] = 4608;//Путь к flash-ключу
    columnsWidth[17] = 6144;//Извлечение flash-ключа
    columnsWidth[18] = 5120;//Создание новой БД
    columnsWidth[19] = 6144;//Номер первой операции
    columnsWidth[20] = 4352;//Время простоя
    columnsWidth[21] = 8192;//Кол-во попыток принять купюру
    columnsWidth[23] = 8704;//Блокировка при окончании рулона
    columnsWidth[24] = 5376;//Длина целого рулона
    columnsWidth[26] = 5376;//Длительность печати
    columnsWidth[27] = 3328;//Автодозвон
    columnsWidth[28] = 5632;//Название соединения
    createHeader(columnsWidth, 0, skipModelColumn, true);
    byte cor = createTable(null, 1, skipModelColumn);
    createFooterStyles();
    HSSFRow row = sheet.createRow(table.getRowCount()+1+cor);
    int ri = 0;
    for (int i=0; i<table.getColumnCount(false); i++) {
      if (row.getRowNum() < 2) break;
      int modelColumn = table.convertColumnIndexToModel(i);
      if (modelColumn == skipModelColumn) continue;
      HSSFCell cell = row.createCell(ri);
      String excelColumn = getColumnAB(ri);
      if (modelColumn == 1) cell.setCellFormula("COUNTA("+excelColumn+"2:"+excelColumn+row.getRowNum()+")");
      if (modelColumn == 2) cell.setCellValue("Итого");
      if (modelColumn == 1) {
        cell.setCellType(HSSFCell.CELL_TYPE_FORMULA);
      }
      if (modelColumn == 1 || modelColumn == 2) cell.setCellStyle(footerStyle3);
      else cell.setCellStyle(footerStyle1);
      ri++;
    }
    return wb;
  }

  public HSSFWorkbook createSettingsSheet2() {
    int skipModelColumn = 0;
    createSheet(true, 2, 1);
    Integer[] columnsWidth = new Integer[table.getModel().getColumnCount()];
    columnsWidth[1] = 2048;//№
    columnsWidth[3] = 4096;//СУ Интервал связи
    columnsWidth[6] = 4096;//ЦФС Интервал связи
    columnsWidth[8] = 4096;//ППУ Интервал связи
    columnsWidth[10] = 4096;//РПЦ Интервал связи
    columnsWidth[16] = 4096;//E-Port Интервал связи
    columnsWidth[23] = 4096;//CyberPlat Интервал связи
    columnsWidth[29] = 4096;//Город-К Интервал связи
    columnsWidth[33] = 4096;//GreenPost Интервал связи
    columnsWidth[13] = 5120;//РПЦ Лимит потери связи
    createHeader(columnsWidth, 0, skipModelColumn, true);
    byte cor = createTable(null, 1, skipModelColumn);
    createFooterStyles();
    HSSFRow row = sheet.createRow(table.getRowCount()+1+cor);
    int ri = 0;
    for (int i=0; i<table.getColumnCount(false); i++) {
      if (row.getRowNum() < 2) break;
      int modelColumn = table.convertColumnIndexToModel(i);
      if (modelColumn == skipModelColumn) continue;
      HSSFCell cell = row.createCell(ri);
      String excelColumn = getColumnAB(ri);
      if (modelColumn == 1) cell.setCellFormula("COUNTA("+excelColumn+"2:"+excelColumn+row.getRowNum()+")");
      if (modelColumn == 2) cell.setCellValue("Итого");
      if (modelColumn == 1) {
        cell.setCellType(HSSFCell.CELL_TYPE_FORMULA);
      }
      if (modelColumn == 1 || modelColumn == 2) cell.setCellStyle(footerStyle3);
      else cell.setCellStyle(footerStyle1);
      ri++;
    }
    return wb;
  }

  public HSSFWorkbook createSettingsSheet3() {
    int skipModelColumn = 0;
    createSheet(true, 2, 1);
    Integer[] columnsWidth = new Integer[table.getModel().getColumnCount()];
    columnsWidth[1] = 2048;//№
    createHeader(columnsWidth, 0, skipModelColumn, false);
    byte cor = createTable(null, 1, skipModelColumn);
    createFooterStyles();
    HSSFRow row = sheet.createRow(table.getRowCount()+1+cor);
    int ri = 0;
    for (int i=0; i<table.getColumnCount(false); i++) {
      if (row.getRowNum() < 2) break;
      int modelColumn = table.convertColumnIndexToModel(i);
      if (modelColumn == skipModelColumn) continue;
      HSSFCell cell = row.createCell(ri);
      String excelColumn = getColumnAB(ri);
      if (modelColumn == 1) cell.setCellFormula("COUNTA("+excelColumn+"2:"+excelColumn+row.getRowNum()+")");
      if (modelColumn == 2) cell.setCellValue("Итого");
      if (modelColumn == 1) {
        cell.setCellType(HSSFCell.CELL_TYPE_FORMULA);
      }
      if (modelColumn == 1 || modelColumn == 2) cell.setCellStyle(footerStyle3);
      else cell.setCellStyle(footerStyle1);
      ri++;
    }
    return wb;
  }

  public HSSFWorkbook createSettingsSheet4() {
    int skipModelColumn = 0;
    createSheet(true, 2, 1);
    Integer[] columnsWidth = new Integer[table.getModel().getColumnCount()];
    columnsWidth[1] = 2048;//№
    createHeader(columnsWidth, 0, skipModelColumn, false);
    byte cor = createTable(null, 1, skipModelColumn);
    createFooterStyles();
    HSSFRow row = sheet.createRow(table.getRowCount()+1+cor);
    int ri = 0;
    for (int i=0; i<table.getColumnCount(false); i++) {
      if (row.getRowNum() < 2) break;
      int modelColumn = table.convertColumnIndexToModel(i);
      if (modelColumn == skipModelColumn) continue;
      HSSFCell cell = row.createCell(ri);
      String excelColumn = getColumnAB(ri);
      if (modelColumn == 1) cell.setCellFormula("COUNTA("+excelColumn+"2:"+excelColumn+row.getRowNum()+")");
      if (modelColumn == 2) cell.setCellValue("Итого");
      if (modelColumn == 1) {
        cell.setCellType(HSSFCell.CELL_TYPE_FORMULA);
      }
      if (modelColumn == 1 || modelColumn == 2) cell.setCellStyle(footerStyle3);
      else cell.setCellStyle(footerStyle1);
      ri++;
    }
    return wb;
  }

  public HSSFWorkbook createSettingsSheet5() {
    int skipModelColumn = 0;
    createSheet(true, 2, 1);
    Integer[] columnsWidth = new Integer[table.getModel().getColumnCount()];
    columnsWidth[1] = 2048;//№
    columnsWidth[4] = 4096;//Дата установки
    columnsWidth[10] = 4352;//Автоматическое обновление
    columnsWidth[11] = 15396;//Часовой пояс
    createHeader(columnsWidth, 0, skipModelColumn, false);
    byte cor = createTable(null, 1, skipModelColumn);
    createFooterStyles();
    HSSFRow row = sheet.createRow(table.getRowCount()+1+cor);
    int ri = 0;
    for (int i=0; i<table.getColumnCount(false); i++) {
      if (row.getRowNum() < 2) break;
      int modelColumn = table.convertColumnIndexToModel(i);
      if (modelColumn == skipModelColumn) continue;
      HSSFCell cell = row.createCell(ri);
      String excelColumn = getColumnAB(ri);
      if (modelColumn == 1) cell.setCellFormula("COUNTA("+excelColumn+"2:"+excelColumn+row.getRowNum()+")");
      if (modelColumn == 2) cell.setCellValue("Итого");
      if (modelColumn == 1) {
        cell.setCellType(HSSFCell.CELL_TYPE_FORMULA);
      }
      if (modelColumn == 1 || modelColumn == 2) cell.setCellStyle(footerStyle3);
      else cell.setCellStyle(footerStyle1);
      ri++;
    }
    return wb;
  }

  public HSSFWorkbook createSettingsSheet6() {
    int skipModelColumn = 0;
    createSheet(true, 2, 1);
    Integer[] columnsWidth = new Integer[table.getModel().getColumnCount()];
    columnsWidth[1] = 2048;//№
    columnsWidth[3] = 11008;//Купюроприемник
    columnsWidth[4] = 23808;//Принтер
    createHeader(columnsWidth, 0, skipModelColumn, false);
    byte cor = createTable(null, 1, skipModelColumn);
    createFooterStyles();
    HSSFRow row = sheet.createRow(table.getRowCount()+1+cor);
    int ri = 0;
    for (int i=0; i<table.getColumnCount(false); i++) {
      if (row.getRowNum() < 2) break;
      int modelColumn = table.convertColumnIndexToModel(i);
      if (modelColumn == skipModelColumn) continue;
      HSSFCell cell = row.createCell(ri);
      String excelColumn = getColumnAB(ri);
      if (modelColumn == 1) cell.setCellFormula("COUNTA("+excelColumn+"2:"+excelColumn+row.getRowNum()+")");
      if (modelColumn == 2) cell.setCellValue("Итого");
      if (modelColumn == 1) {
        cell.setCellType(HSSFCell.CELL_TYPE_FORMULA);
      }
      if (modelColumn == 1 || modelColumn == 2) cell.setCellStyle(footerStyle3);
      else cell.setCellStyle(footerStyle1);
      ri++;
    }
    return wb;
  }

  public HSSFWorkbook createSettingsSheet7() {
    int skipModelColumn = 0;
    createSheet(true, 0, 1);
    Integer[] columnsWidth = new Integer[table.getModel().getColumnCount()];
    columnsWidth[1] = 2048;//№
    columnsWidth[3] = 11008;//Антивирус
    columnsWidth[4] = 7424;//Брандмауэр
    columnsWidth[5] = 7424;//Статус
    createHeader(columnsWidth, 0, skipModelColumn, false);
    byte cor = createTable(null, 1, skipModelColumn);
    createFooterStyles();
    HSSFRow row = sheet.createRow(table.getRowCount()+1+cor);
    int ri = 0;
    for (int i=0; i<table.getColumnCount(false); i++) {
      if (row.getRowNum() < 2) break;
      int modelColumn = table.convertColumnIndexToModel(i);
      if (modelColumn == skipModelColumn) continue;
      HSSFCell cell = row.createCell(ri);
      String excelColumn = getColumnAB(ri);
      if (modelColumn == 1) cell.setCellFormula("COUNTA("+excelColumn+"2:"+excelColumn+row.getRowNum()+")");
      if (modelColumn == 2) cell.setCellValue("Итого");
      if (modelColumn == 1) {
        cell.setCellType(HSSFCell.CELL_TYPE_FORMULA);
      }
      if (modelColumn == 1 || modelColumn == 2) cell.setCellStyle(footerStyle3);
      else cell.setCellStyle(footerStyle1);
      ri++;
    }
    return wb;
  }

  public HSSFWorkbook createSettingsSheet8() {
    int skipModelColumn = 0;
    createSheet(true, 0, 1);
    Integer[] columnsWidth = new Integer[table.getModel().getColumnCount()];
    columnsWidth[1] = 2048;//№
    columnsWidth[3] = 4864;//Интернет
    columnsWidth[4] = 4608;//Качество связи за сутки
    columnsWidth[5] = 6656;//Качество обслуживания за сутки
    columnsWidth[6] = 4608;//Качество связи за неделю
    columnsWidth[7] = 6656;//Качество обслуживания за неделю
    createHeader(columnsWidth, 0, skipModelColumn, true);
    byte cor = createTable(null, 1, skipModelColumn);
    createFooterStyles();
    HSSFRow row = sheet.createRow(table.getRowCount()+1+cor);
    int ri = 0;
    for (int i=0; i<table.getColumnCount(false); i++) {
      if (row.getRowNum() < 2) break;
      int modelColumn = table.convertColumnIndexToModel(i);
      if (modelColumn == skipModelColumn) continue;
      HSSFCell cell = row.createCell(ri);
      String excelColumn = getColumnAB(ri);
      if (modelColumn == 1) cell.setCellFormula("COUNTA("+excelColumn+"2:"+excelColumn+row.getRowNum()+")");
      if (modelColumn == 2) cell.setCellValue("Итого");
      if (modelColumn == 1) {
        cell.setCellType(HSSFCell.CELL_TYPE_FORMULA);
      }
      if (modelColumn == 1 || modelColumn == 2) cell.setCellStyle(footerStyle3);
      else cell.setCellStyle(footerStyle1);
      ri++;
    }
    return wb;
  }

  public HSSFWorkbook createSettingsSheet9() {
    int skipModelColumn = 0;
    createSheet(true, 0, 1);
    Integer[] columnsWidth = new Integer[table.getModel().getColumnCount()];
    columnsWidth[1] = 2048;//№
    columnsWidth[3] = 3072;//Долгота
    columnsWidth[4] = 3072;//Широта
    createHeader(columnsWidth, 0, skipModelColumn, false);
    byte cor = createTable(null, 1, skipModelColumn);
    createFooterStyles();
    HSSFRow row = sheet.createRow(table.getRowCount()+1+cor);
    int ri = 0;
    for (int i=0; i<table.getColumnCount(false); i++) {
      if (row.getRowNum() < 2) break;
      int modelColumn = table.convertColumnIndexToModel(i);
      if (modelColumn == skipModelColumn) continue;
      if (modelColumn == 5 || modelColumn == 6 || modelColumn == 7) continue;
      HSSFCell cell = row.createCell(ri);
      String excelColumn = getColumnAB(ri);
      if (modelColumn == 1) cell.setCellFormula("COUNTA("+excelColumn+"2:"+excelColumn+row.getRowNum()+")");
      if (modelColumn == 2) cell.setCellValue("Итого");
      if (modelColumn == 1) {
        cell.setCellType(HSSFCell.CELL_TYPE_FORMULA);
      }
      if (modelColumn == 1 || modelColumn == 2) cell.setCellStyle(footerStyle3);
      else cell.setCellStyle(footerStyle1);
      ri++;
    }
    return wb;
  }

  public HSSFWorkbook createFinanceSheet(int gatewaysCount) {
    int skipModelColumn = 0;
    createSheet(true, 0, 1);
    Integer[] columnsWidth = new Integer[table.getModel().getColumnCount()];
    columnsWidth[1] = 2048;//№
    columnsWidth[3] = 2600;//Выручка
    for (int i=0; i<gatewaysCount; i++) {
      columnsWidth[4+i] = 2600;//Выручка шлюза
    }
    columnsWidth[4+gatewaysCount] = 2600;//Доход
    columnsWidth[5+gatewaysCount] = 4160;//Рентабельность
    columnsWidth[6+gatewaysCount] = 2720;//Операций
    columnsWidth[7+gatewaysCount] = 2440;//Средний платеж
    columnsWidth[8+gatewaysCount] = 2440;//Выручка в день
    columnsWidth[9+gatewaysCount] = 2440;//Доход в день
    columnsWidth[10+gatewaysCount] = 2720;//Операций в день
    columnsWidth[11+gatewaysCount] = 3480;//Актуальность
    createHeader(columnsWidth, 0, skipModelColumn, true);
    int[] formatColumns = new int[2];
    formatColumns[0] = 4+gatewaysCount;//Доход
    formatColumns[1] = 5+gatewaysCount;//Рентабельность
    byte cor = createTable(formatColumns, 1, skipModelColumn);
    createFooterStyles();
    HSSFRow row = sheet.createRow(table.getRowCount()+1+cor);
    int ri = 0;
    for (int i=0; i<table.getColumnCount(false); i++) {
      if (row.getRowNum() < 2) break;
      int modelColumn = table.convertColumnIndexToModel(i);
      if (modelColumn == skipModelColumn) continue;
      HSSFCell cell = row.createCell(ri);
      String excelColumn = getColumnAB(ri);
      if (modelColumn == 1) cell.setCellFormula("COUNTA("+excelColumn+"2:"+excelColumn+row.getRowNum()+")");
      if (modelColumn == 2) cell.setCellValue("Итого");
      if ((modelColumn >= 3 && modelColumn <= 4+gatewaysCount) || modelColumn == 6+gatewaysCount)
        cell.setCellFormula("SUM("+excelColumn+"2:"+excelColumn+row.getRowNum()+")");
      if (modelColumn == 5+gatewaysCount) {
        int div1 = table.convertColumnIndexToView(4+gatewaysCount);
        int div2 = table.convertColumnIndexToView(3);
        if (div1 > table.convertColumnIndexToView(skipModelColumn)) div1--;
        if (div2 > table.convertColumnIndexToView(skipModelColumn)) div2--;
        String v;
        if (div1 < 0 || div2 < 0)
          v = table.getModel().getValueAt(table.getModel().getRowCount()-1,4+gatewaysCount).toString()+"/"+
              table.getModel().getValueAt(table.getModel().getRowCount()-1,3).toString();
        else v = getColumnAB(div1)+(row.getRowNum()+1)+"/"+getColumnAB(div2)+(row.getRowNum()+1);
        cell.setCellFormula(v+"*100");
      }
      if (modelColumn == 7+gatewaysCount) {
        int div1 = table.convertColumnIndexToView(3);
        int div2 = table.convertColumnIndexToView(6+gatewaysCount);
        if (div1 > table.convertColumnIndexToView(skipModelColumn)) div1--;
        if (div2 > table.convertColumnIndexToView(skipModelColumn)) div2--;
        String v;
        if (div1 < 0 || div2 < 0)
          v = table.getModel().getValueAt(table.getModel().getRowCount()-1,3).toString()+"/"+
              table.getModel().getValueAt(table.getModel().getRowCount()-1,6+gatewaysCount).toString();
        else v = getColumnAB(div1)+(row.getRowNum()+1)+"/"+getColumnAB(div2)+(row.getRowNum()+1);
        cell.setCellFormula(v);
      }
      if (modelColumn == 8+gatewaysCount) {
        cell.setCellFormula("SUM("+excelColumn+"2:"+excelColumn+row.getRowNum()+")/COUNTA("+excelColumn+"2:"+excelColumn+row.getRowNum()+")");
      }
      if (modelColumn == 9+gatewaysCount) {
        cell.setCellFormula("SUM("+excelColumn+"2:"+excelColumn+row.getRowNum()+")/COUNTA("+excelColumn+"2:"+excelColumn+row.getRowNum()+")");
      }
      if (modelColumn == 10+gatewaysCount) {
        cell.setCellFormula("SUM("+excelColumn+"2:"+excelColumn+row.getRowNum()+")/COUNTA("+excelColumn+"2:"+excelColumn+row.getRowNum()+")");
      }
      if (modelColumn >= 3 && modelColumn <= 10+gatewaysCount) {
        cell.setCellType(HSSFCell.CELL_TYPE_FORMULA);
      }
      if (modelColumn == 1 || modelColumn == 2) cell.setCellStyle(footerStyle3);
      else if (modelColumn == 4+gatewaysCount || modelColumn == 5+gatewaysCount || modelColumn == 7+gatewaysCount || modelColumn == 8+gatewaysCount || modelColumn == 9+gatewaysCount) cell.setCellStyle(footerStyle2);
      else if (modelColumn == 10+gatewaysCount) cell.setCellStyle(footerStyle4);
      else cell.setCellStyle(footerStyle1);
      ri++;
    }
    return wb;
  }

  public HSSFWorkbook createZReportsSheet() {
    createSheet(true, 0, 1);
    Integer[] columnsWidth = new Integer[table.getModel().getColumnCount()];
    columnsWidth[0] = 3584;//Номер ФР
    columnsWidth[1] = 2304;//Z-отчет
    columnsWidth[2] = 5376;//Дата
    columnsWidth[3] = 2560;//Сумма
    columnsWidth[4] = 4352;//Необн. сумма
    columnsWidth[5] = 3328;//Инкассация
    columnsWidth[6] = 5376;//Сист. время
    columnsWidth[8] = 2048;//№ киоска
    createHeader(columnsWidth, 0);
    int[] formatColumns = new int[1];
    formatColumns[0] = 4;//Необн. сумма
    byte cor = createTable(formatColumns, 1);
    createFooterStyles();
    HSSFRow row = sheet.createRow(table.getRowCount()+1+cor);
    int ri = 0;
    for (int i=0; i<table.getColumnCount(false); i++) {
      if (row.getRowNum() < 2) break;
      int modelColumn = table.convertColumnIndexToModel(i);
      HSSFCell cell = row.createCell(ri);
      String excelColumn = getColumnAB(ri);
      if (modelColumn == 0) cell.setCellValue("Итого");
      if (modelColumn == 1) cell.setCellFormula("COUNTA("+excelColumn+"2:"+excelColumn+row.getRowNum()+")");
      if (modelColumn == 3) cell.setCellFormula("SUM("+excelColumn+"2:"+excelColumn+row.getRowNum()+")");
      if (modelColumn == 1 || modelColumn == 3) cell.setCellType(HSSFCell.CELL_TYPE_FORMULA);
      if (modelColumn == 3) cell.setCellStyle(footerStyle1);
      else cell.setCellStyle(footerStyle3);
      ri++;
    }
    return wb;
  }

  private void createSheet(boolean landscape, int freezePaneX, int freezePaneY) {
    HashSet<String> nameSet = new HashSet<>();
    for (int i=0; i<wb.getNumberOfSheets(); i++) nameSet.add(wb.getSheetName(i));
    String uniqueName = createUniqueSheetName(nameSet, name);
    sheet = wb.createSheet();
    wb.setSheetName(wb.getNumberOfSheets()-1, uniqueName);
    sheet.getPrintSetup().setLandscape(landscape);
    sheet.getPrintSetup().setPaperSize(HSSFPrintSetup.A4_PAPERSIZE);
    sheet.setMargin(HSSFSheet.LeftMargin, 0.39);
    sheet.setMargin(HSSFSheet.RightMargin, 0.39);
    sheet.setMargin(HSSFSheet.TopMargin, 0.39);
    sheet.setMargin(HSSFSheet.BottomMargin, 0.39);
    if (freezePaneX > 0 || freezePaneY > 0) sheet.createFreezePane(freezePaneX, freezePaneY);
  }

  private void createHeader(Integer[] columnsWidth, int startRow) {
    createHeader(columnsWidth, startRow, -1);
  }

  private void createHeader(Integer[] columnsWidth, int startRow, int skipModelColumn) {
    createHeader(columnsWidth, startRow, skipModelColumn, false);
  }

  private void createHeader(Integer[] columnsWidth, int startRow, boolean multiLine) {
    createHeader(columnsWidth, startRow, -1, multiLine);
  }

  private void createHeader(Integer[] columnsWidth, int startRow, int skipModelColumn, boolean multiLine) {
    HSSFCellStyle headerStyle = wb.createCellStyle();
    headerStyle.setAlignment(HSSFCellStyle.ALIGN_CENTER);
    headerStyle.setBorderLeft(HSSFCellStyle.BORDER_THIN);
    headerStyle.setBorderRight(HSSFCellStyle.BORDER_THIN);
    headerStyle.setBorderTop(HSSFCellStyle.BORDER_THIN);
    headerStyle.setBorderBottom(HSSFCellStyle.BORDER_THIN);
    headerStyle.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
    font = wb.createFont();
    font.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
    headerStyle.setFont(font);
    HSSFRow row;
    row = sheet.createRow(startRow);
    if (multiLine) {
      headerStyle.setWrapText(true);
      row.setHeight((short)(row.getHeight()*2));
    }
    int ri = 0;
    for (int i=0; i<table.getColumnCount(false); i++) {
      TableColumn column = table.getColumn(i);
      int modelColumn = column.getModelIndex();
      if (modelColumn == skipModelColumn) continue;
      if (modelColumn == 7 && table.getModel() instanceof StatTableModel) continue;
      if ((modelColumn == 5 || modelColumn == 6 || modelColumn == 7) && table.getModel() instanceof Settings9TableModel) continue;
      HSSFCell cell = row.createCell(ri);
      cell.setCellValue(removeTags(table.getColumnName(i), true));
      cell.setCellStyle(headerStyle);
      cell.setCellType(HSSFCell.CELL_TYPE_STRING);
      int columnWidth = (column.getWidth() * 40);
      if (columnsWidth[modelColumn] != null) columnWidth = columnsWidth[modelColumn];
      sheet.setColumnWidth(ri, columnWidth);
      ri++;
    }
  }

  private byte createTable(int[] formatColumns, int startRow) {
    return createTable(formatColumns, startRow, -1);
  }

  private byte createTable(int[] formatColumns, int startRow, int skipModelColumn) {
    return createTable(formatColumns, startRow, skipModelColumn, false);
  }

  private byte createTable(int[] formatColumns, int startRow, int skipModelColumn, boolean specForIncass) {
    HSSFCellStyle cellStyle1 = wb.createCellStyle();
    cellStyle1.setAlignment(HSSFCellStyle.ALIGN_RIGHT);
    cellStyle1.setBorderLeft(HSSFCellStyle.BORDER_THIN);
    cellStyle1.setBorderRight(HSSFCellStyle.BORDER_THIN);
    cellStyle1.setBorderTop(HSSFCellStyle.BORDER_THIN);
    cellStyle1.setBorderBottom(HSSFCellStyle.BORDER_THIN);
    HSSFCellStyle cellStyle2 = wb.createCellStyle();
    cellStyle2.setAlignment(HSSFCellStyle.ALIGN_RIGHT);
    cellStyle2.setDataFormat(HSSFDataFormat.getBuiltinFormat("0.00"));
    cellStyle2.setBorderLeft(HSSFCellStyle.BORDER_THIN);
    cellStyle2.setBorderRight(HSSFCellStyle.BORDER_THIN);
    cellStyle2.setBorderTop(HSSFCellStyle.BORDER_THIN);
    cellStyle2.setBorderBottom(HSSFCellStyle.BORDER_THIN);
    HSSFCellStyle cellStyle3 = wb.createCellStyle();
    cellStyle3.setAlignment(HSSFCellStyle.ALIGN_LEFT);
    cellStyle3.setBorderLeft(HSSFCellStyle.BORDER_THIN);
    cellStyle3.setBorderRight(HSSFCellStyle.BORDER_THIN);
    cellStyle3.setBorderTop(HSSFCellStyle.BORDER_THIN);
    cellStyle3.setBorderBottom(HSSFCellStyle.BORDER_THIN);
    HSSFRow row;
    boolean fullIncassData = true;
    if (specForIncass) {
      if (table.convertColumnIndexToView(5) == -1) fullIncassData = false;
      if (table.convertColumnIndexToView(6) == -1) fullIncassData = false;
      if (table.convertColumnIndexToView(7) == -1) fullIncassData = false;
      if (table.convertColumnIndexToView(8) == -1) fullIncassData = false;
      if (table.convertColumnIndexToView(9) == -1) fullIncassData = false;
      if (table.convertColumnIndexToView(10) == -1) fullIncassData = false;
    }
    byte cor = 0; //поправка на учет строки ИТОГО в таблице
    for (int i=0; i<table.getRowCount(); i++) {
      row = sheet.createRow(i+startRow+cor);
      int modelRow = table.convertRowIndexToModel(i);
      if (modelRow == table.getRowCount()-1) {
        cor--;
        continue;
      }
      int rj = 0;
      for (int j=0; j<table.getColumnCount(false); j++) {
        int modelColumn = table.convertColumnIndexToModel(j);
        if (modelColumn == skipModelColumn && !specForIncass) continue;
        if (modelColumn == 7 && table.getModel() instanceof StatTableModel) continue;
        if ((modelColumn == 5 || modelColumn == 6 || modelColumn == 7) && table.getModel() instanceof Settings9TableModel) continue;
        HSSFCell cell = row.createCell(rj);
        Object value = table.getValueAt(i,j);
        if (modelColumn == 2 && specForIncass && fullIncassData) {
          String excelColumn10 = getColumnAB(table.convertColumnIndexToView(5));
          String excelColumn50 = getColumnAB(table.convertColumnIndexToView(6));
          String excelColumn100 = getColumnAB(table.convertColumnIndexToView(7));
          String excelColumn500 = getColumnAB(table.convertColumnIndexToView(8));
          String excelColumn1000 = getColumnAB(table.convertColumnIndexToView(9));
          String excelColumn5000 = getColumnAB(table.convertColumnIndexToView(10));
          int r = i+startRow+cor+1;
          cell.setCellFormula(excelColumn10+r+"*10+"+excelColumn50+r+"*50+"+excelColumn100+r+"*100+"+excelColumn500+r+"*500+"+excelColumn1000+r+"*1000+"+excelColumn5000+r+"*5000");
          cell.setCellType(HSSFCell.CELL_TYPE_FORMULA);
          cell.setCellStyle(cellStyle1);
        } else if (modelColumn == 4 && specForIncass && fullIncassData) {
          String excelColumn10 = getColumnAB(table.convertColumnIndexToView(5));
          String excelColumn50 = getColumnAB(table.convertColumnIndexToView(6));
          String excelColumn100 = getColumnAB(table.convertColumnIndexToView(7));
          String excelColumn500 = getColumnAB(table.convertColumnIndexToView(8));
          String excelColumn1000 = getColumnAB(table.convertColumnIndexToView(9));
          String excelColumn5000 = getColumnAB(table.convertColumnIndexToView(10));
          int r = i+startRow+cor+1;
          cell.setCellFormula(excelColumn10+r+"+"+excelColumn50+r+"+"+excelColumn100+r+"+"+excelColumn500+r+"+"+excelColumn1000+r+"+"+excelColumn5000+r);
          cell.setCellType(HSSFCell.CELL_TYPE_FORMULA);
          cell.setCellStyle(cellStyle1);
        } else if (value instanceof Integer) {
          cell.setCellValue((Integer)value);
          cell.setCellType(HSSFCell.CELL_TYPE_NUMERIC);
          cell.setCellStyle(cellStyle1);
        } else if (value instanceof Double) {
          cell.setCellValue((Double)value);
          cell.setCellType(HSSFCell.CELL_TYPE_NUMERIC);
          cell.setCellStyle(cellStyle1);
        } else if (value instanceof DoubleApproach) {
          cell.setCellValue(((DoubleApproach)value).getFrom());
          cell.setCellType(HSSFCell.CELL_TYPE_NUMERIC);
          cell.setCellStyle(cellStyle1);
        } else if (value instanceof DoubleTotal) {
          cell.setCellValue(((DoubleTotal)value).getValue1());
          cell.setCellType(HSSFCell.CELL_TYPE_NUMERIC);
          cell.setCellStyle(cellStyle1);
        } else if (value instanceof String) {
          cell.setCellValue(removeTags((String)value, false));
          cell.setCellType(HSSFCell.CELL_TYPE_STRING);
          cell.setCellStyle(cellStyle3);
        } else {
          cell.setCellValue(value.toString());
          cell.setCellType(HSSFCell.CELL_TYPE_STRING);
          cell.setCellStyle(cellStyle3);
        }
        if (formatColumns != null) {
          for (int formatColumn : formatColumns) {
            if (modelColumn == formatColumn) {
              cell.setCellStyle(cellStyle2);
              break;
            }
          }
        }
        rj++;
      }
    }
    return cor;
  }

  private void createFooterStyles() {
    footerStyle1 = wb.createCellStyle();
    footerStyle1.setAlignment(HSSFCellStyle.ALIGN_RIGHT);
    footerStyle1.setBorderLeft(HSSFCellStyle.BORDER_THIN);
    footerStyle1.setBorderRight(HSSFCellStyle.BORDER_THIN);
    footerStyle1.setBorderTop(HSSFCellStyle.BORDER_THIN);
    footerStyle1.setBorderBottom(HSSFCellStyle.BORDER_THIN);
    footerStyle1.setFont(font);
    footerStyle2 = wb.createCellStyle();
    footerStyle2.setAlignment(HSSFCellStyle.ALIGN_RIGHT);
    footerStyle2.setDataFormat(HSSFDataFormat.getBuiltinFormat("0.00"));
    footerStyle2.setBorderLeft(HSSFCellStyle.BORDER_THIN);
    footerStyle2.setBorderRight(HSSFCellStyle.BORDER_THIN);
    footerStyle2.setBorderTop(HSSFCellStyle.BORDER_THIN);
    footerStyle2.setBorderBottom(HSSFCellStyle.BORDER_THIN);
    footerStyle2.setFont(font);
    footerStyle3 = wb.createCellStyle();
    footerStyle3.setAlignment(HSSFCellStyle.ALIGN_LEFT);
    footerStyle3.setBorderLeft(HSSFCellStyle.BORDER_THIN);
    footerStyle3.setBorderRight(HSSFCellStyle.BORDER_THIN);
    footerStyle3.setBorderTop(HSSFCellStyle.BORDER_THIN);
    footerStyle3.setBorderBottom(HSSFCellStyle.BORDER_THIN);
    footerStyle3.setFont(font);
    footerStyle4 = wb.createCellStyle();
    footerStyle4.setAlignment(HSSFCellStyle.ALIGN_RIGHT);
    footerStyle4.setDataFormat(HSSFDataFormat.getBuiltinFormat("0"));
    footerStyle4.setBorderLeft(HSSFCellStyle.BORDER_THIN);
    footerStyle4.setBorderRight(HSSFCellStyle.BORDER_THIN);
    footerStyle4.setBorderTop(HSSFCellStyle.BORDER_THIN);
    footerStyle4.setBorderBottom(HSSFCellStyle.BORDER_THIN);
    footerStyle4.setFont(font);
  }

  private void createConditions(String[] conditionValues, int startRow, int skipColumns) {
    Conditions conditions = new Conditions();
    conditions.add("тип отчета: "+conditionValues[0], "с "+conditionValues[1]+" по "+conditionValues[2]);
    conditions.add(conditionValues[3], conditionValues[4]);
    conditions.add(conditionValues[5], conditionValues[6]);
    conditions.add("фильтр запроса: "+conditionValues[7], "фильтр просмотра: "+conditionValues[8]);
    sheet.addMergedRegion(new CellRangeAddress(startRow,startRow,0,table.getColumnCount(false)-1-skipColumns));
    sheet.addMergedRegion(new CellRangeAddress(startRow+1,startRow+1,0,table.getColumnCount(false)-1-skipColumns));
    sheet.addMergedRegion(new CellRangeAddress(startRow+2,startRow+2,0,table.getColumnCount(false)-1-skipColumns));
    HSSFFont font = wb.createFont();
    font.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
    font.setFontName("Courier New");
    HSSFCellStyle conditionsStyle = wb.createCellStyle();
    conditionsStyle.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
    conditionsStyle.setFillForegroundColor(new HSSFColor.WHITE().getIndex());
    conditionsStyle.setAlignment(HSSFCellStyle.ALIGN_LEFT);
    conditionsStyle.setFont(font);
    HSSFRow row;
    HSSFCell cell;
    row = sheet.createRow(startRow);
    cell = row.createCell(0);
    cell.setCellStyle(conditionsStyle);
    cell.setCellValue(conditions.getLine1());
    row = sheet.createRow(startRow+1);
    cell = row.createCell(0);
    cell.setCellStyle(conditionsStyle);
    cell.setCellValue(conditions.getLine2());
    row = sheet.createRow(startRow+2);
    cell = row.createCell(0);
    cell.setCellStyle(conditionsStyle);
    cell.setCellValue("");
  }

  //создает уникальное имя листа, поскольку документ не должен содержать одинаковых имен
  private String createUniqueSheetName(HashSet<String> nameSet, String name) {
    String newName;
    if (nameSet.contains(name)) {
      newName = name + "(1)";
      int i = 1;
      while (nameSet.contains(newName)) {
        i++;
        newName = name + "("+i+")";
      }
    } else newName = name;
    return newName;
  }

  private String removeTags(String str, boolean wrap) {
    if (wrap) str = str.replaceAll("(?i)<br>", "\n");
    else str = str.replaceAll("(?i)<br>", " ");
    str = str.replaceAll("<.+?>", "");
    return str;
  }

  public static String getColumnAB(int c) {//0-A,1-B,25-Z,26-AA
    if (c < 0 || c > 701) return null;
    String result = "";
    int x1 = (c / 26);
    int x2 = (c % 26);
    if (x1 > 0) result += excelColumns.charAt(x1-1);
    result += excelColumns.charAt(x2);
    return result;
  }
}

class Conditions {
  private StringBuffer line1;
  private StringBuffer line2;

  public Conditions() {
    clear();
  }

  public void add(String str1, String str2) {
    if (line1.length() == 0) {
      line1.append(str1);
      line2.append(str2);
    } else {
      StringBuffer smaller;
      int max = Math.max(line1.length(), line2.length());
      if (line1.length() > line2.length()) smaller = line2; else smaller = line1;
      int smallerLen = smaller.length();
      for (int i=0; i<max-smallerLen; i++) {
        smaller.append(" ");
      }
      line1.append(" | ").append(str1);
      line2.append(" | ").append(str2);
    }
  }

  public void clear() {
    line1 = new StringBuffer();
    line2 = new StringBuffer();
  }

  public String getLine1() {
    return line1.toString();
  }

  public String getLine2() {
    return line2.toString();
  }
}
