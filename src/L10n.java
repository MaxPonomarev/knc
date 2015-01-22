import javax.swing.*;

/**
 * Created with IntelliJ IDEA.
 * User: Maksim Ponomarev
 * Date: 25.03.13
 *
 * @version 1.0
 */
final public class L10n {
  static void localize() {
    UIManager.put("JXTable.column.horizontalScroll", "Горизонтальная прокрутка");
    UIManager.put("JXTable.column.packAll", "Уплотнить все столбцы");
    UIManager.put("JXTable.column.packSelected", "Уплотнить выбранные столбцы");
    UIManager.put("JXDatePicker.linkFormat", "Сегодня {0,date, dd MMMM yyyy}");
    UIManager.put("XDialog.close", "Закрыть");

    UIManager.put("Search.matchCase", "с учетом регистра");
    UIManager.put("Search.wrapSearch", "искать с начала");
    UIManager.put("Search.backwardsSearch", "обратный поиск");
    UIManager.put("Search.match", "Найти");
    UIManager.put("Search.close", "Закрыть");
    UIManager.put("Search.searchFieldLabel", "Найти");
    UIManager.put("Search.searchTitle", "Найти");
    UIManager.put("Search.notFound", "Фраза не найдена");

    UIManager.put("FileChooser.acceptAllFileFilterText", "Все файлы");
    UIManager.put("FileChooser.lookInLabelText", "Папка:");
    UIManager.put("FileChooser.saveInLabelText", "Папка:");
    UIManager.put("FileChooser.fileNameLabelText", "Имя файла:");
    UIManager.put("FileChooser.filesOfTypeLabelText", "Тип файла:");
    UIManager.put("FileChooser.openButtonText", "Открыть");
    UIManager.put("FileChooser.openButtonToolTipText", "Открыть");
    UIManager.put("FileChooser.directoryOpenButtonText", "Открыть");
    UIManager.put("FileChooser.directoryOpenButtonToolTipText", "Открыть");
    UIManager.put("FileChooser.saveButtonText", "Сохранить");
    UIManager.put("FileChooser.saveButtonToolTipText", "Сохранить");
    UIManager.put("FileChooser.cancelButtonText", "Отмена");
    UIManager.put("FileChooser.cancelButtonToolTipText", "Отмена");
    UIManager.put("FileChooser.updateButtonText", "Обновить");
    UIManager.put("FileChooser.updateButtonToolTipText", "Обновить");
    UIManager.put("FileChooser.refreshActionLabelText", "Обновить");
    UIManager.put("FileChooser.upFolderToolTipText", "На один уровень вверх");
    UIManager.put("FileChooser.newFolderToolTipText", "Создание новой папки");
    UIManager.put("FileChooser.newFolderAccessibleName", "Создать папку");
    UIManager.put("FileChooser.newFolderDialogText", "Создать папку");
    UIManager.put("FileChooser.newFolderActionLabelText", "Создать папку");
    UIManager.put("FileChooser.listViewButtonToolTipText", "Список");
    UIManager.put("FileChooser.listViewButtonAccessibleName", "Список");
    UIManager.put("FileChooser.listViewActionLabelText", "Список");
    UIManager.put("FileChooser.detailsViewButtonToolTipText", "Таблица");
    UIManager.put("FileChooser.detailsViewButtonAccessibleName", "Таблица");
    UIManager.put("FileChooser.detailsViewActionLabelText", "Таблица");
    UIManager.put("FileChooser.viewMenuLabelText", "Вид");
    UIManager.put("FileChooser.win32.newFolder", "Новая папка");
    UIManager.put("FileChooser.win32.newFolder.subsequent", "Новая папка ({0})");

    UIManager.put("OptionPane.yesButtonText", "Да");
    UIManager.put("OptionPane.noButtonText", "Нет");
    UIManager.put("OptionPane.cancelButtonText", "Отмена");
  }
}
