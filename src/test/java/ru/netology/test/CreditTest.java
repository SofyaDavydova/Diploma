package ru.netology.test;

import com.codeborne.selenide.logevents.SelenideLogger;
import io.qameta.allure.selenide.AllureSelenide;
import org.junit.jupiter.api.*;
import ru.netology.data.DataHelper;
import ru.netology.data.SQLHelper;
import ru.netology.page.CreditPage;
import ru.netology.page.MainPage;

import static com.codeborne.selenide.Selenide.open;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static ru.netology.data.SQLHelper.cleanDatabase;

public class CreditTest {
    MainPage mainPage;
    CreditPage creditPage;

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
        creditPage = mainPage.creditPayment();
    }

    @AfterEach
    void cleaningAllTables() {
    cleanDatabase();
    }


    //Позитивные сценарии

    @Test
    @DisplayName("Попадание на страницу формы для получения кредита по данным карты")
    public void shouldOpenCreditPaymentPage() {
        creditPage.checkOpenCreditPage();
    }

    @Test
    @DisplayName("Успешная отправка формы для карты со статусом APPROVED")
    public void shouldSendFormAndPayWithApprovedCard() {
        creditPage.enterCardData(DataHelper.getApprovedCard(), DataHelper.getValidMonth(), DataHelper.getValidYear(), DataHelper.getValidOwner(), DataHelper.getValidCVC());
        creditPage.checkSuccessMessage();
    }

    @Test
    @DisplayName("Успешная отправка формы для карты со статусом DECLINED")
    public void shouldSendFormAndNotPayWithDeclinedCard() {
        creditPage.enterCardData(DataHelper.getDeclinedCard(), DataHelper.getValidMonth(), DataHelper.getValidYear(), DataHelper.getValidOwner(), DataHelper.getValidCVC());
        creditPage.checkErrorMessage();
    }

    @Test
    @DisplayName("Успешная отправка формы для произвольной карты (не из заданного набора карт)")
    public void shouldSendFormAndNotPayWithRandomCard() {
        creditPage.enterCardData(DataHelper.getRandomValidCardNumber(), DataHelper.getValidMonth(), DataHelper.getValidYear(), DataHelper.getValidOwner(), DataHelper.getValidCVC());
        creditPage.checkErrorMessage();
    }

    @Test
    @DisplayName("Отправка формы с введением в поле Владелец двойного имени")
    public void shouldSendFormAndPayWithDoubleFirstNameInOwner() {
        creditPage.enterCardData(DataHelper.getApprovedCard(), DataHelper.getValidMonth(), DataHelper.getValidYear(), DataHelper.getValidOwnerWithDoubleFirstName(), DataHelper.getValidCVC());
        creditPage.checkSuccessMessage();
    }

    @Test
    @DisplayName("Отправка формы с введением в поле Владелец двойной фамилии")
    public void shouldSendFormAndPayWithDoubleLastNameInOwner() {
        creditPage.enterCardData(DataHelper.getApprovedCard(), DataHelper.getValidMonth(), DataHelper.getValidYear(), DataHelper.getValidOwnerWithDoubleLastName(), DataHelper.getValidCVC());
        creditPage.checkSuccessMessage();
    }

    @Test
    @DisplayName("Отправка формы с введением в поле Владелец двойных имени и фамилии")
    public void shouldSendFormAndPayWithDoubleFirstAndLastNamesInOwner() {
        creditPage.enterCardData(DataHelper.getApprovedCard(), DataHelper.getValidMonth(), DataHelper.getValidYear(), DataHelper.getValidOwnerWithDoubleFirstAndLastNames(), DataHelper.getValidCVC());
        creditPage.checkSuccessMessage();
    }

    //Негативные сценарии

    @Test
    @DisplayName("Отправка формы с пустым полем Номер карты")
    public void shouldNotSendFormWithEmptyCardNumberField() {
        creditPage.enterCardData(DataHelper.getEmptyCardNumber(), DataHelper.getValidMonth(), DataHelper.getValidYear(), DataHelper.getValidOwner(), DataHelper.getValidCVC());
        creditPage.checkWrongFormatMessage();
    }

    @Test
    @DisplayName("Отправка формы с пустым полем Месяц")
    public void shouldNotSendFormWithEmptyMonthField() {
        creditPage.enterCardData(DataHelper.getApprovedCard(), DataHelper.getEmptyMonth(), DataHelper.getValidYear(), DataHelper.getValidOwner(), DataHelper.getValidCVC());
        creditPage.checkWrongFormatMessage();
    }

    @Test
    @DisplayName("Отправка формы с пустым полем Год")
    public void shouldNotSendFormWithEmptyYearField() {
        creditPage.enterCardData(DataHelper.getApprovedCard(), DataHelper.getValidMonth(), DataHelper.getEmptyYear(), DataHelper.getValidOwner(), DataHelper.getValidCVC());
        creditPage.checkWrongFormatMessage();
    }

    @Test
    @DisplayName("Отправка формы с пустым полем Владелец")
    public void shouldNotSendFormWithEmptyOwnerField() {
        creditPage.enterCardData(DataHelper.getApprovedCard(), DataHelper.getValidMonth(), DataHelper.getValidYear(), DataHelper.getEmptyOwner(), DataHelper.getValidCVC());
        creditPage.checkEmptyInputMessage();
    }

    @Test
    @DisplayName("Отправка формы с пустым полем CVC/CVV")
    public void shouldNotSendFormWithEmptyCVCField() {
        creditPage.enterCardData(DataHelper.getApprovedCard(), DataHelper.getValidMonth(), DataHelper.getValidYear(), DataHelper.getValidOwner(), DataHelper.getEmptyCVC());
        creditPage.checkWrongFormatMessage();
    }

    @Test
    @DisplayName("Отправка формы с введением в поле Номер карты менее 16 цифр")
    public void shouldNotSendFormWithShortCardNumber() {
        creditPage.enterCardData(DataHelper.getShortCardNumber(), DataHelper.getValidMonth(), DataHelper.getValidYear(), DataHelper.getValidOwner(), DataHelper.getValidCVC());
        creditPage.checkWrongFormatMessage();
    }

    @Test
    @DisplayName("Отправка формы с введением в поле Номер карты более 16 цифр")
    public void shouldNotSendFormWithLongCardNumber() {
        creditPage.enterCardData(DataHelper.getLongCardNumber(), DataHelper.getValidMonth(), DataHelper.getValidYear(), DataHelper.getValidOwner(), DataHelper.getValidCVC());
        creditPage.checkErrorMessage();
    }

    @Test
    @DisplayName("Отправка формы с введением в поле Номер карты букв на кириллице")
    public void shouldNotSendFormWithCyrillicLettersInCardNumber() {
        creditPage.enterCardData(DataHelper.getCardNumberWithCyrillicLetters(), DataHelper.getValidMonth(), DataHelper.getValidYear(), DataHelper.getValidOwner(), DataHelper.getValidCVC());
        creditPage.checkWrongFormatMessage();
    }

    @Test
    @DisplayName("Отправка формы с введением в поле Номер карты букв на латиннице")
    public void shouldNotSendFormWithLatinLettersInCardNumber() {
        creditPage.enterCardData(DataHelper.getCardNumberWithLatinLetters(), DataHelper.getValidMonth(), DataHelper.getValidYear(), DataHelper.getValidOwner(), DataHelper.getValidCVC());
        creditPage.checkWrongFormatMessage();
    }

    @Test
    @DisplayName("Отправка формы с введением в поле Номер карты спецсимволов")
    public void shouldNotSendFormWithSpecialSymbolsInCardNumber() {
        creditPage.enterCardData(DataHelper.getCardNumberWithSpecialSymbols(), DataHelper.getValidMonth(), DataHelper.getValidYear(), DataHelper.getValidOwner(), DataHelper.getValidCVC());
        creditPage.checkWrongFormatMessage();
    }

    @Test
    @DisplayName("Отправка формы с введением в поле Месяц спецсимволов")
    public void shouldNotSendFormWithSpecialSymbolsInMonth() {
        creditPage.enterCardData(DataHelper.getApprovedCard(), DataHelper.getMonthWithSpecialSymbols(), DataHelper.getValidYear(), DataHelper.getValidOwner(), DataHelper.getValidCVC());
        creditPage.checkWrongFormatMessage();
    }

    @Test
    @DisplayName("Отправка формы с введением в поле Месяц букв на кириллице")
    public void shouldNotSendFormWithCyrillicLettersInMonth() {
        creditPage.enterCardData(DataHelper.getApprovedCard(), DataHelper.getMonthWithCyrillicLetters(), DataHelper.getValidYear(), DataHelper.getValidOwner(), DataHelper.getValidCVC());
        creditPage.checkWrongFormatMessage();
    }

    @Test
    @DisplayName("Отправка формы с введением в поле Месяц букв на латиннице")
    public void shouldNotSendFormWithLatinLettersInMonth() {
        creditPage.enterCardData(DataHelper.getApprovedCard(), DataHelper.getMonthWithLatinLetters(), DataHelper.getValidYear(), DataHelper.getValidOwner(), DataHelper.getValidCVC());
        creditPage.checkWrongFormatMessage();
    }

    @Test
    @DisplayName("Отправка формы с введением в поле Месяц менее двух значений")
    public void shouldNotSendFormWithLessThanTwoSymbolsInMonth() {
        creditPage.enterCardData(DataHelper.getApprovedCard(), DataHelper.getShortMonth(), DataHelper.getValidYear(), DataHelper.getValidOwner(), DataHelper.getValidCVC());
        creditPage.checkWrongFormatMessage();
    }

    @Test
    @DisplayName("Отправка формы с введением в поле Месяц более двух значений")
    public void shouldNotSendFormWithMoreThanTwoSymbolsInMonth() {
        creditPage.enterCardData(DataHelper.getApprovedCard(), DataHelper.getLongMonth(), DataHelper.getValidYear(), DataHelper.getValidOwner(), DataHelper.getValidCVC());
        creditPage.checkSuccessMessage();
    }

    @Test
    @DisplayName("Отправка формы с введением в поле Месяц значения 00")
    public void shouldNotSendFormWithDoubleZeroMonth() {
        creditPage.enterCardData(DataHelper.getApprovedCard(), DataHelper.getZeroMonth(), DataHelper.getValidYear(), DataHelper.getValidOwner(), DataHelper.getValidCVC());
        creditPage.checkWrongDateMessage();
    }

    @Test
    @DisplayName("Отправка формы с введением в поле Месяц значения больше, чем 12")
    public void shouldNotSendFormWithMonthMoreThanTwelve() {
        creditPage.enterCardData(DataHelper.getApprovedCard(), DataHelper.getMonthMoreThan12(), DataHelper.getValidYear(), DataHelper.getValidOwner(), DataHelper.getValidCVC());
        creditPage.checkWrongDateMessage();
    }

    @Test
    @DisplayName("Отправка формы с введением в поле Год спецсимволов")
    public void shouldNotSendFormWithSpecialSymbolsInYear() {
        creditPage.enterCardData(DataHelper.getApprovedCard(), DataHelper.getValidMonth(), DataHelper.getYearWithSpecialSymbols(), DataHelper.getValidOwner(), DataHelper.getValidCVC());
        creditPage.checkWrongFormatMessage();
    }

    @Test
    @DisplayName("Отправка формы с введением в поле Год букв на кириллице")
    public void shouldNotSendFormWithCyrillicLettersInYear() {
        creditPage.enterCardData(DataHelper.getApprovedCard(), DataHelper.getValidMonth(), DataHelper.getYearWithCyrillicLetters(), DataHelper.getValidOwner(), DataHelper.getValidCVC());
        creditPage.checkWrongFormatMessage();
    }

    @Test
    @DisplayName("Отправка формы с введением в поле Год букв на латиннице")
    public void shouldNotSendFormWithLatinLettersInYear() {
        creditPage.enterCardData(DataHelper.getApprovedCard(), DataHelper.getValidMonth(), DataHelper.getYearWithLatinLetters(), DataHelper.getValidOwner(), DataHelper.getValidCVC());
        creditPage.checkWrongFormatMessage();
    }

    @Test
    @DisplayName("Отправка формы с введением в поле Год менее двух значений")
    public void shouldNotSendFormWithLessThanTwoSymbolsInYear() {
        creditPage.enterCardData(DataHelper.getApprovedCard(), DataHelper.getValidMonth(), DataHelper.getShortYear(), DataHelper.getValidOwner(), DataHelper.getValidCVC());
        creditPage.checkWrongFormatMessage();
    }

    @Test
    @DisplayName("Отправка формы с введением в поле Год более двух значений")
    public void shouldNotSendFormWithMoreThanTwoSymbolsInYear() {
        creditPage.enterCardData(DataHelper.getApprovedCard(), DataHelper.getValidMonth(), DataHelper.getLongYear(), DataHelper.getValidOwner(), DataHelper.getValidCVC());
        creditPage.checkSuccessMessage();
    }

    @Test
    @DisplayName("Отправка формы с введением в поле Год значения, раньше текущего года")
    public void shouldNotSendFormWithPreviousYear() {
        creditPage.enterCardData(DataHelper.getApprovedCard(), DataHelper.getValidMonth(), DataHelper.getPreviousYear(), DataHelper.getValidOwner(), DataHelper.getValidCVC());
        creditPage.checkExpiredCardMessage();
    }

    @Test
    @DisplayName("Отправка формы с введением в поле Год значения, позже текущего года +5 лет")
    public void shouldNotSendFormWithYearPlusMoreThan5() {
        creditPage.enterCardData(DataHelper.getApprovedCard(), DataHelper.getValidMonth(), DataHelper.getYearPlusMoreThan5(), DataHelper.getValidOwner(), DataHelper.getValidCVC());
        creditPage.checkWrongDateMessage();
    }

    @Test
    @DisplayName("Отправка формы с введением в поле CVC/CVV спецсимволов")
    public void shouldNotSendFormWithSpecialSymbolsInCVC() {
        creditPage.enterCardData(DataHelper.getApprovedCard(), DataHelper.getValidMonth(), DataHelper.getValidYear(), DataHelper.getValidOwner(), DataHelper.getCVCWithSpecialSymbols());
        creditPage.checkWrongFormatMessage();
    }

    @Test
    @DisplayName("Отправка формы с введением в поле CVC/CVV букв на кириллице")
    public void shouldNotSendFormWithCyrillicLettersInCVC() {
        creditPage.enterCardData(DataHelper.getApprovedCard(), DataHelper.getValidMonth(), DataHelper.getValidYear(), DataHelper.getValidOwner(), DataHelper.getCVCWithCyrillicLetters());
        creditPage.checkWrongFormatMessage();
    }

    @Test
    @DisplayName("Отправка формы с введением в поле CVC/CVV букв на латиннице")
    public void shouldNotSendFormWithLatinLettersInCVC() {
        creditPage.enterCardData(DataHelper.getApprovedCard(), DataHelper.getValidMonth(), DataHelper.getValidYear(), DataHelper.getValidOwner(), DataHelper.getCVCWithLatinLetters());
        creditPage.checkWrongFormatMessage();
    }

    @Test
    @DisplayName("Отправка формы с введением в поле CVC/CVV менее трех значений")
    public void shouldNotSendFormWithLessThanThreeSymbolsInCVC() {
        creditPage.enterCardData(DataHelper.getApprovedCard(), DataHelper.getValidMonth(), DataHelper.getValidYear(), DataHelper.getValidOwner(), DataHelper.getShortCVC());
        creditPage.checkWrongFormatMessage();
    }

    @Test
    @DisplayName("Отправка формы с введением в поле CVC/CVV более трех значений")
    public void shouldNotSendFormWithMoreThanThreeSymbolsInCVC() {
        creditPage.enterCardData(DataHelper.getApprovedCard(), DataHelper.getValidMonth(), DataHelper.getValidYear(), DataHelper.getValidOwner(), DataHelper.getLongCVC());
        creditPage.checkSuccessMessage();
    }

    @Test
    @DisplayName("Отправка формы с введением в поле Владелец спецсимволов")
    public void shouldNotSendFormWithSpecialSymbolsInOwner() {
        creditPage.enterCardData(DataHelper.getApprovedCard(), DataHelper.getValidMonth(), DataHelper.getValidYear(), DataHelper.getOwnerWithSpecialSymbols(), DataHelper.getValidCVC());
        creditPage.checkWrongFormatMessage();
    }

    @Test
    @DisplayName("Отправка формы с введением в поле Владелец букв на кириллице")
    public void shouldNotSendFormWithCyrillicLettersInOwner() {
        creditPage.enterCardData(DataHelper.getApprovedCard(), DataHelper.getValidMonth(), DataHelper.getValidYear(), DataHelper.getOwnerWithCyrillicLetters(), DataHelper.getValidCVC());
        creditPage.checkWrongFormatMessage();
    }

    @Test
    @DisplayName("Отправка формы с введением в поле Владелец цифр")
    public void shouldNotSendFormWithNumbersInOwner() {
        creditPage.enterCardData(DataHelper.getApprovedCard(), DataHelper.getValidMonth(), DataHelper.getValidYear(), DataHelper.getOwnerWithNumbers(), DataHelper.getValidCVC());
        creditPage.checkWrongFormatMessage();
    }

    @Test
    @DisplayName("Отправка формы с введением в поле Владелец букв на латиннице и спецсимволов")
    public void shouldNotSendFormWithLatinLettersAndSpecialSymbolsInOwner() {
        creditPage.enterCardData(DataHelper.getApprovedCard(), DataHelper.getValidMonth(), DataHelper.getValidYear(), DataHelper.getOwnerWithLatinLettersAndSpecialSymbols(), DataHelper.getValidCVC());
        creditPage.checkWrongFormatMessage();
    }

    @Test
    @DisplayName("Отправка формы с введением в поле Владелец букв на латиннице и кириллице")
    public void shouldNotSendFormWithLatinAndCyrillicLettersInOwner() {
        creditPage.enterCardData(DataHelper.getApprovedCard(), DataHelper.getValidMonth(), DataHelper.getValidYear(), DataHelper.getOwnerWithLatinAndCyrillicLetters(), DataHelper.getValidCVC());
        creditPage.checkWrongFormatMessage();
    }

    @Test
    @DisplayName("Отправка формы с введением в поле Владелец букв на латиннице и цифр")
    public void shouldNotSendFormWithLatinLettersAndNumbersInOwner() {
        creditPage.enterCardData(DataHelper.getApprovedCard(), DataHelper.getValidMonth(), DataHelper.getValidYear(), DataHelper.getOwnerWithLatinLettersAndNumbers(), DataHelper.getValidCVC());
        creditPage.checkWrongFormatMessage();
    }

    @Test
    @DisplayName("Отправка формы с введением в поле Владелец фамилии и имени без пробела")
    public void shouldNotSendFormWithoutWhitespaceInOwner() {
        creditPage.enterCardData(DataHelper.getApprovedCard(), DataHelper.getValidMonth(), DataHelper.getValidYear(), DataHelper.getOwnerWithoutWhitespace(), DataHelper.getValidCVC());
        creditPage.checkWrongFormatMessage();
    }
}
