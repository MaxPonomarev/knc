

import java.util.*;

class FilterMenuFactory {
  private final HashSet<FilterMenu> menus = new HashSet<>();

  public FilterMenu getMenu() {
    FilterMenu menu = FilterMenu.createMenu();
    menus.add(menu);
    return menu;
  }

  public FilterMenu getMenu(Map<String, HashSet<String>> filtersMap, boolean saveDelExt) {//ключи Map - фильтры
    FilterMenu menu = FilterMenu.createMenu(filtersMap, saveDelExt);
    menus.add(menu);
    return menu;
  }

  public FilterMenu getMenu(String[] filters, boolean saveDelExt) {
    FilterMenu menu = FilterMenu.createMenu(filters, saveDelExt);
    menus.add(menu);
    return menu;
  }

  public void reconstAutoFilters(Map<String, String> filtersMap) {//ключи Map - фильтры
    for (FilterMenu menu : menus) {
      menu.reconstAutoFilters(filtersMap);
    }
  }

  public void reconstAutoFilters(String[] newFilters) {
    for (FilterMenu menu : menus) {
      menu.reconstAutoFilters(newFilters);
    }
  }

  public void reconstFilters(Map<String, HashSet<String>> filtersMap) {//ключи Map - фильтры
    for (FilterMenu menu : menus) {
      menu.reconstFilters(filtersMap);
    }
  }

  public void reconstFilters(String[] newFilters) {
    for (FilterMenu menu : menus) {
      menu.reconstFilters(newFilters);
    }
  }
}
