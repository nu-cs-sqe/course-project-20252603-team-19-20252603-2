package ui;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.Locale;
import org.junit.jupiter.api.Test;
import ui.model.AppModel;

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
}
