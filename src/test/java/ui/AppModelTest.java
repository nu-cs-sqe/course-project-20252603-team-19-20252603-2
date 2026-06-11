package ui;

import static org.junit.jupiter.api.Assertions.assertEquals;

import domain.model.AppModel;
import java.util.Locale;
import org.junit.jupiter.api.Test;

class AppModelTest {
    @Test
    void toggleLanguage_whenEnglish_switchesToChinese() {
        AppModel model = new AppModel();

        Locale expectedInitialLocale = Locale.ENGLISH;
        assertEquals(expectedInitialLocale, model.getSelectedLocale());

        model.toggleLanguage();

        Locale expectedLocale = Locale.SIMPLIFIED_CHINESE;
        assertEquals(expectedLocale, model.getSelectedLocale());
    }

    @Test
    void toggleLanguage_whenEnglishTwoToggle_stayAsEnglish() {
        AppModel model = new AppModel();

        Locale expectedInitialLocale = Locale.ENGLISH;
        assertEquals(expectedInitialLocale, model.getSelectedLocale());

        model.toggleLanguage();
        model.toggleLanguage();

        Locale expectedLocale = Locale.ENGLISH;
        assertEquals(expectedLocale, model.getSelectedLocale());
    }
}
