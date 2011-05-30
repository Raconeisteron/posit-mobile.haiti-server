package haiti.server.datamodel;

import haiti.server.gui.DataEntryGUI;

import java.util.Locale;
import java.util.ResourceBundle;

public class LocaleManager {
	
	public static Locale[] supportedLocales = {Locale.FRENCH, Locale.ENGLISH};	
	public static ResourceBundle resources;
	public static Locale currentLocale;
	
	public LocaleManager() {
		currentLocale = Locale.FRENCH;
		resources =  ResourceBundle.getBundle("MenusBundle", currentLocale);
	}

}
