package ru.netology.test;

import com.codeborne.selenide.logevents.SelenideLogger;
import io.qameta.allure.selenide.AllureSelenide;
import org.junit.jupiter.api.*;
import ru.netology.data.DataHelper;
import ru.netology.data.SQLHelper;
import ru.netology.page.DebitPage;
import ru.netology.page.MainPage;

import static com.codeborne.selenide.Selenide.open;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static ru.netology.data.SQLHelper.cleanDatabase;

public class DebitTest {

    MainPage mainPage;
    DebitPage debitPage;

    @BeforeAll
    static void setUpAll() {
        SelenideLogger.addListener("allure", new AllureSelenide());
    }

    @AfterAll
    static void tearDownAll() {
        SelenideLogger.removeListener("allure");
    }

    @BeforeEach
    void setup() {
        open("http://localhost:8080/");
        mainPage = new MainPage();
        debitPage = mainPage.debitPayment();
    }

    @AfterEach
    void cleaningAllTables() {
        cleanDatabase();
    }

    //Позитивные сценарии

    @Test
    @DisplayName("Попадание на страницу формы оплаты по карте")
    public void shouldOpenDebitPaymentPage() {
        debitPage.checkOpenDebitPage();
    }

    @Test
    @DisplayName("Успешная отправка формы для карты со статусом APPROVED")
    public void shouldSendFormAndPayWithApprovedCard() {
        debitPage.enterCardData(DataHelper.getApprovedCard(), DataHelper.getValidMonth(), DataHelper.getValidYear(), DataHelper.getValidOwner(), DataHelper.getValidCVC());
        debitPage.checkSuccessMessage();
    }

    @Test
    @DisplayName("Успешная отправка формы для карты со статусом DECLINED")
    public void shouldSendFormAndNotPayWithDeclinedCard() {
        debitPage.enterCardData(DataHelper.getDeclinedCard(), DataHelper.getValidMonth(), DataHelper.getValidYear(), DataHelper.getValidOwner(), DataHelper.getValidCVC());
        debitPage.checkErrorMessage();
    }

    @Test
    @DisplayName("Успешная отправка формы для произвольной карты (не из заданного набора карт)")
    public void shouldSendFormAndNotPayWithRandomCard() {
        debitPage.enterCardData(DataHelper.getRandomValidCardNumber(), DataHelper.getValidMonth(), DataHelper.getValidYear(), DataHelper.getValidOwner(), DataHelper.getValidCVC());
        debitPage.checkErrorMessage();
    }

    @Test
    @DisplayName("Отправка формы с введением в поле Владелец двойного имени")
    public void shouldSendFormAndPayWithDoubleFirstNameInOwner() {
        debitPage.enterCardData(DataHelper.getApprovedCard(), DataHelper.getValidMonth(), DataHelper.getValidYear(), DataHelper.getValidOwnerWithDoubleFirstName(), DataHelper.getValidCVC());
        debitPage.checkSuccessMessage();
    }

    @Test
    @DisplayName("Отправка формы с введением в поле Владелец двойной фамилии")
    public void shouldSendFormAndPayWithDoubleLastNameInOwner() {
        debitPage.enterCardData(DataHelper.getApprovedCard(), DataHelper.getValidMonth(), DataHelper.getValidYear(), DataHelper.getValidOwnerWithDoubleLastName(), DataHelper.getValidCVC());
        debitPage.checkSuccessMessage();
    }

    @Test
    @DisplayName("Отправка формы с введением в поле Владелец двойных имени и фамилии")
    public void shouldSendFormAndPayWithDoubleFirstAndLastNamesInOwner() {
        debitPage.enterCardData(DataHelper.getApprovedCard(), DataHelper.getValidMonth(), DataHelper.getValidYear(), DataHelper.getValidOwnerWithDoubleFirstAndLastNames(), DataHelper.getValidCVC());
        debitPage.checkSuccessMessage();
    }

    //Негативные сценарии

    @Test
    @DisplayName("Отправка формы с пустым полем Номер карты")
    public void shouldNotSendFormWithEmptyCardNumberField() {
        debitPage.enterCardData(DataHelper.getEmptyCardNumber(), DataHelper.getValidMonth(), DataHelper.getValidYear(), DataHelper.getValidOwner(), DataHelper.getValidCVC());
        debitPage.checkWrongFormatMessage();
    }

    @Test
    @DisplayName("Отправка формы с пустым полем Месяц")
    public void shouldNotSendFormWithEmptyMonthField() {
        debitPage.enterCardData(DataHelper.getApprovedCard(), DataHelper.getEmptyMonth(), DataHelper.getValidYear(), DataHelper.getValidOwner(), DataHelper.getValidCVC());
        debitPage.checkWrongFormatMessage();
    }

    @Test
    @DisplayName("Отправка формы с пустым полем Год")
    public void shouldNotSendFormWithEmptyYearField() {
        debitPage.enterCardData(DataHelper.getApprovedCard(), DataHelper.getValidMonth(), DataHelper.getEmptyYear(), DataHelper.getValidOwner(), DataHelper.getValidCVC());
        debitPage.checkWrongFormatMessage();
    }

    @Test
    @DisplayName("Отправка формы с пустым полем Владелец")
    public void shouldNotSendFormWithEmptyOwnerField() {
        debitPage.enterCardData(DataHelper.getApprovedCard(), DataHelper.getValidMonth(), DataHelper.getValidYear(), DataHelper.getEmptyOwner(), DataHelper.getValidCVC());
        debitPage.checkEmptyInputMessage();
    }

    @Test
    @DisplayName("Отправка формы с пустым полем CVC/CVV")
    public void shouldNotSendFormWithEmptyCVCField() {
        debitPage.enterCardData(DataHelper.getApprovedCard(), DataHelper.getValidMonth(), DataHelper.getValidYear(), DataHelper.getValidOwner(), DataHelper.getEmptyCVC());
        debitPage.checkWrongFormatMessage();
    }

    @Test
    @DisplayName("Отправка формы с введением в поле Номер карты менее 16 цифр")
    public void shouldNotSendFormWithShortCardNumber() {
        debitPage.enterCardData(DataHelper.getShortCardNumber(), DataHelper.getValidMonth(), DataHelper.getValidYear(), DataHelper.getValidOwner(), DataHelper.getValidCVC());
        debitPage.checkWrongFormatMessage();
    }

    @Test
    @DisplayName("Отправка формы с введением в поле Номер карты более 16 цифр")
    public void shouldNotSendFormWithLongCardNumber() {
        debitPage.enterCardData(DataHelper.getLongCardNumber(), DataHelper.getValidMonth(), DataHelper.getValidYear(), DataHelper.getValidOwner(), DataHelper.getValidCVC());
        debitPage.checkErrorMessage();
    }

    @Test
    @DisplayName("Отправка формы с введением в поле Номер карты букв на кириллице")
    public void shouldNotSendFormWithCyrillicLettersInCardNumber() {
        debitPage.enterCardData(DataHelper.getCardNumberWithCyrillicLetters(), DataHelper.getValidMonth(), DataHelper.getValidYear(), DataHelper.getValidOwner(), DataHelper.getValidCVC());
        debitPage.checkWrongFormatMessage();
    }

    @Test
    @DisplayName("Отправка формы с введением в поле Номер карты букв на латиннице")
    public void shouldNotSendFormWithLatinLettersInCardNumber() {
        debitPage.enterCardData(DataHelper.getCardNumberWithLatinLetters(), DataHelper.getValidMonth(), DataHelper.getValidYear(), DataHelper.getValidOwner(), DataHelper.getValidCVC());
        debitPage.checkWrongFormatMessage();
    }

    @Test
    @DisplayName("Отправка формы с введением в поле Номер карты спецсимволов")
    public void shouldNotSendFormWithSpecialSymbolsInCardNumber() {
        debitPage.enterCardData(DataHelper.getCardNumberWithSpecialSymbols(), DataHelper.getValidMonth(), DataHelper.getValidYear(), DataHelper.getValidOwner(), DataHelper.getValidCVC());
        debitPage.checkWrongFormatMessage();
    }

    @Test
    @DisplayName("Отправка формы с введением в поле Месяц спецсимволов")
    public void shouldNotSendFormWithSpecialSymbolsInMonth() {
        debitPage.enterCardData(DataHelper.getApprovedCard(), DataHelper.getMonthWithSpecialSymbols(), DataHelper.getValidYear(), DataHelper.getValidOwner(), DataHelper.getValidCVC());
        debitPage.checkWrongFormatMessage();
    }

    @Test
    @DisplayName("Отправка формы с введением в поле Месяц букв на кириллице")
    public void shouldNotSendFormWithCyrillicLettersInMonth() {
        debitPage.enterCardData(DataHelper.getApprovedCard(), DataHelper.getMonthWithCyrillicLetters(), DataHelper.getValidYear(), DataHelper.getValidOwner(), DataHelper.getValidCVC());
        debitPage.checkWrongFormatMessage();
    }

    @Test
    @DisplayName("Отправка формы с введением в поле Месяц букв на латиннице")
    public void shouldNotSendFormWithLatinLettersInMonth() {
        debitPage.enterCardData(DataHelper.getApprovedCard(), DataHelper.getMonthWithLatinLetters(), DataHelper.getValidYear(), DataHelper.getValidOwner(), DataHelper.getValidCVC());
        debitPage.checkWrongFormatMessage();
    }

    @Test
    @DisplayName("Отправка формы с введением в поле Месяц менее двух значений")
    public void shouldNotSendFormWithLessThanTwoSymbolsInMonth() {
        debitPage.enterCardData(DataHelper.getApprovedCard(), DataHelper.getShortMonth(), DataHelper.getValidYear(), DataHelper.getValidOwner(), DataHelper.getValidCVC());
        debitPage.checkWrongFormatMessage();
    }

    @Test
    @DisplayName("Отправка формы с введением в поле Месяц более двух значений")
    public void shouldNotSendFormWithMoreThanTwoSymbolsInMonth() {
        debitPage.enterCardData(DataHelper.getApprovedCard(), DataHelper.getLongMonth(), DataHelper.getValidYear(), DataHelper.getValidOwner(), DataHelper.getValidCVC());
        debitPage.checkSuccessMessage();
    }

    @Test
    @DisplayName("Отправка формы с введением в поле Месяц значения 00")
    public void shouldNotSendFormWithDoubleZeroMonth() {
        debitPage.enterCardData(DataHelper.getApprovedCard(), DataHelper.getZeroMonth(), DataHelper.getValidYear(), DataHelper.getValidOwner(), DataHelper.getValidCVC());
        debitPage.checkWrongDateMessage();
    }

    @Test
    @DisplayName("Отправка формы с введением в поле Месяц значения больше, чем 12")
    public void shouldNotSendFormWithMonthMoreThanTwelve() {
        debitPage.enterCardData(DataHelper.getApprovedCard(), DataHelper.getMonthMoreThan12(), DataHelper.getValidYear(), DataHelper.getValidOwner(), DataHelper.getValidCVC());
        debitPage.checkWrongDateMessage();
    }

    @Test
    @DisplayName("Отправка формы с введением в поле Год спецсимволов")
    public void shouldNotSendFormWithSpecialSymbolsInYear() {
        debitPage.enterCardData(DataHelper.getApprovedCard(), DataHelper.getValidMonth(), DataHelper.getYearWithSpecialSymbols(), DataHelper.getValidOwner(), DataHelper.getValidCVC());
        debitPage.checkWrongFormatMessage();
    }

    @Test
    @DisplayName("Отправка формы с введением в поле Год букв на кириллице")
    public void shouldNotSendFormWithCyrillicLettersInYear() {
        debitPage.enterCardData(DataHelper.getApprovedCard(), DataHelper.getValidMonth(), DataHelper.getYearWithCyrillicLetters(), DataHelper.getValidOwner(), DataHelper.getValidCVC());
        debitPage.checkWrongFormatMessage();
    }

    @Test
    @DisplayName("Отправка формы с введением в поле Год букв на латиннице")
    public void shouldNotSendFormWithLatinLettersInYear() {
        debitPage.enterCardData(DataHelper.getApprovedCard(), DataHelper.getValidMonth(), DataHelper.getYearWithLatinLetters(), DataHelper.getValidOwner(), DataHelper.getValidCVC());
        debitPage.checkWrongFormatMessage();
    }

    @Test
    @DisplayName("Отправка формы с введением в поле Год менее двух значений")
    public void shouldNotSendFormWithLessThanTwoSymbolsInYear() {
        debitPage.enterCardData(DataHelper.getApprovedCard(), DataHelper.getValidMonth(), DataHelper.getShortYear(), DataHelper.getValidOwner(), DataHelper.getValidCVC());
        debitPage.checkWrongFormatMessage();
    }

    @Test
    @DisplayName("Отправка формы с введением в поле Год более двух значений")
    public void shouldNotSendFormWithMoreThanTwoSymbolsInYear() {
        debitPage.enterCardData(DataHelper.getApprovedCard(), DataHelper.getValidMonth(), DataHelper.getLongYear(), DataHelper.getValidOwner(), DataHelper.getValidCVC());
        debitPage.checkSuccessMessage();
    }

    @Test
    @DisplayName("Отправка формы с введением в поле Год значения, раньше текущего года")
    public void shouldNotSendFormWithPreviousYear() {
        debitPage.enterCardData(DataHelper.getApprovedCard(), DataHelper.getValidMonth(), DataHelper.getPreviousYear(), DataHelper.getValidOwner(), DataHelper.getValidCVC());
        debitPage.checkExpiredCardMessage();
    }

    @Test
    @DisplayName("Отправка формы с введением в поле Год значения, позже текущего года +5 лет")
    public void shouldNotSendFormWithYearPlusMoreThan5() {
        debitPage.enterCardData(DataHelper.getApprovedCard(), DataHelper.getValidMonth(), DataHelper.getYearPlusMoreThan5(), DataHelper.getValidOwner(), DataHelper.getValidCVC());
        debitPage.checkWrongDateMessage();
    }

    @Test
    @DisplayName("Отправка формы с введением в поле CVC/CVV спецсимволов")
    public void shouldNotSendFormWithSpecialSymbolsInCVC() {
        debitPage.enterCardData(DataHelper.getApprovedCard(), DataHelper.getValidMonth(), DataHelper.getValidYear(), DataHelper.getValidOwner(), DataHelper.getCVCWithSpecialSymbols());
        debitPage.checkWrongFormatMessage();
    }

    @Test
    @DisplayName("Отправка формы с введением в поле CVC/CVV букв на кириллице")
    public void shouldNotSendFormWithCyrillicLettersInCVC() {
        debitPage.enterCardData(DataHelper.getApprovedCard(), DataHelper.getValidMonth(), DataHelper.getValidYear(), DataHelper.getValidOwner(), DataHelper.getCVCWithCyrillicLetters());
        debitPage.checkWrongFormatMessage();
    }

    @Test
    @DisplayName("Отправка формы с введением в поле CVC/CVV букв на латиннице")
    public void shouldNotSendFormWithLatinLettersInCVC() {
        debitPage.enterCardData(DataHelper.getApprovedCard(), DataHelper.getValidMonth(), DataHelper.getValidYear(), DataHelper.getValidOwner(), DataHelper.getCVCWithLatinLetters());
        debitPage.checkWrongFormatMessage();
    }

    @Test
    @DisplayName("Отправка формы с введением в поле CVC/CVV менее трех значений")
    public void shouldNotSendFormWithLessThanThreeSymbolsInCVC() {
        debitPage.enterCardData(DataHelper.getApprovedCard(), DataHelper.getValidMonth(), DataHelper.getValidYear(), DataHelper.getValidOwner(), DataHelper.getShortCVC());
        debitPage.checkWrongFormatMessage();
    }

    @Test
    @DisplayName("Отправка формы с введением в поле CVC/CVV более трех значений")
    public void shouldNotSendFormWithMoreThanThreeSymbolsInCVC() {
        debitPage.enterCardData(DataHelper.getApprovedCard(), DataHelper.getValidMonth(), DataHelper.getValidYear(), DataHelper.getValidOwner(), DataHelper.getLongCVC());
        debitPage.checkSuccessMessage();
    }

    @Test
    @DisplayName("Отправка формы с введением в поле Владелец спецсимволов")
    public void shouldNotSendFormWithSpecialSymbolsInOwner() {
        debitPage.enterCardData(DataHelper.getApprovedCard(), DataHelper.getValidMonth(), DataHelper.getValidYear(), DataHelper.getOwnerWithSpecialSymbols(), DataHelper.getValidCVC());
        debitPage.checkWrongFormatMessage();
    }

    @Test
    @DisplayName("Отправка формы с введением в поле Владелец букв на кириллице")
    public void shouldNotSendFormWithCyrillicLettersInOwner() {
        debitPage.enterCardData(DataHelper.getApprovedCard(), DataHelper.getValidMonth(), DataHelper.getValidYear(), DataHelper.getOwnerWithCyrillicLetters(), DataHelper.getValidCVC());
        debitPage.checkWrongFormatMessage();
    }

    @Test
    @DisplayName("Отправка формы с введением в поле Владелец цифр")
    public void shouldNotSendFormWithNumbersInOwner() {
        debitPage.enterCardData(DataHelper.getApprovedCard(), DataHelper.getValidMonth(), DataHelper.getValidYear(), DataHelper.getOwnerWithNumbers(), DataHelper.getValidCVC());
        debitPage.checkWrongFormatMessage();
    }

    @Test
    @DisplayName("Отправка формы с введением в поле Владелец букв на латиннице и спецсимволов")
    public void shouldNotSendFormWithLatinLettersAndSpecialSymbolsInOwner() {
        debitPage.enterCardData(DataHelper.getApprovedCard(), DataHelper.getValidMonth(), DataHelper.getValidYear(), DataHelper.getOwnerWithLatinLettersAndSpecialSymbols(), DataHelper.getValidCVC());
        debitPage.checkWrongFormatMessage();
    }

    @Test
    @DisplayName("Отправка формы с введением в поле Владелец букв на латиннице и кириллице")
    public void shouldNotSendFormWithLatinAndCyrillicLettersInOwner() {
        debitPage.enterCardData(DataHelper.getApprovedCard(), DataHelper.getValidMonth(), DataHelper.getValidYear(), DataHelper.getOwnerWithLatinAndCyrillicLetters(), DataHelper.getValidCVC());
        debitPage.checkWrongFormatMessage();
    }

    @Test
    @DisplayName("Отправка формы с введением в поле Владелец букв на латиннице и цифр")
    public void shouldNotSendFormWithLatinLettersAndNumbersInOwner() {
        debitPage.enterCardData(DataHelper.getApprovedCard(), DataHelper.getValidMonth(), DataHelper.getValidYear(), DataHelper.getOwnerWithLatinLettersAndNumbers(), DataHelper.getValidCVC());
        debitPage.checkWrongFormatMessage();
    }

    @Test
    @DisplayName("Отправка формы с введением в поле Владелец фамилии и имени без пробела")
    public void shouldNotSendFormWithoutWhitespaceInOwner() {
        debitPage.enterCardData(DataHelper.getApprovedCard(), DataHelper.getValidMonth(), DataHelper.getValidYear(), DataHelper.getOwnerWithoutWhitespace(), DataHelper.getValidCVC());
        debitPage.checkWrongFormatMessage();
    }
}
