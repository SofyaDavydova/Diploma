package ru.netology.test;

import com.codeborne.selenide.logevents.SelenideLogger;
import io.qameta.allure.selenide.AllureSelenide;
import org.junit.jupiter.api.*;
import ru.netology.data.DataHelper;
import ru.netology.data.SQLHelper;
import ru.netology.page.CreditPage;
import ru.netology.page.DebitPage;
import ru.netology.page.MainPage;

import java.time.Duration;

import static com.codeborne.selenide.Selenide.open;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static ru.netology.data.SQLHelper.cleanDatabase;

public class SQLTest {
    MainPage mainPage;
    DebitPage debitPage;
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

    }

    @AfterEach
    void cleaningAllTables() {
        cleanDatabase();
    }

    @Test
    @DisplayName("Проверка статуса платежа при покупке картой со статусом APPROVED")
    public void shouldHaveApprovedStatusForPaymentWithApprovedCard() {
        debitPage = mainPage.debitPayment();
        debitPage.enterCardData(DataHelper.getApprovedCard(), DataHelper.getValidMonth(), DataHelper.getValidYear(), DataHelper.getValidOwner(), DataHelper.getValidCVC());
        debitPage.checkSuccessMessage();
        String lastPaymentStatus = SQLHelper.getLastPaymentStatus();
        String expectedStatus = "APPROVED";
        assertEquals(expectedStatus, lastPaymentStatus);
    }

    @Test
    @DisplayName("Проверка статуса платежа при покупке в кредит картой со статусом APPROVED")
    public void shouldHaveApprovedStatusForCreditPaymentWithApprovedCard() {
        creditPage = mainPage.creditPayment();
        creditPage.enterCardData(DataHelper.getApprovedCard(), DataHelper.getValidMonth(), DataHelper.getValidYear(), DataHelper.getValidOwner(), DataHelper.getValidCVC());
        creditPage.checkSuccessMessage();
        String lastCreditPaymentStatus = SQLHelper.getLastCreditPaymentStatus();
        String expectedStatus = "APPROVED";
        assertEquals(expectedStatus, lastCreditPaymentStatus);
    }

    @Test
    @DisplayName("Проверка статуса платежа при покупке картой со статусом DECLINED")
    public void shouldHaveDeclinedStatusForPaymentWithDeclinedCard() {
        debitPage = mainPage.debitPayment();
        debitPage.enterCardData(DataHelper.getDeclinedCard(), DataHelper.getValidMonth(), DataHelper.getValidYear(), DataHelper.getValidOwner(), DataHelper.getValidCVC());
        debitPage.checkSuccessMessage();
        String lastPaymentStatus = SQLHelper.getLastPaymentStatus();
        String expectedStatus = "DECLINED";
        assertEquals(expectedStatus, lastPaymentStatus);
    }

    @Test
    @DisplayName("Проверка статуса платежа при покупке в кредит картой со статусом DECLINED")
    public void shouldHaveDeclinedStatusForCreditPaymentWithDeclinedCard() {
        creditPage = mainPage.creditPayment();
        creditPage.enterCardData(DataHelper.getDeclinedCard(), DataHelper.getValidMonth(), DataHelper.getValidYear(), DataHelper.getValidOwner(), DataHelper.getValidCVC());
        creditPage.checkSuccessMessage();
        String lastCreditPaymentStatus = SQLHelper.getLastCreditPaymentStatus();
        String expectedStatus = "DECLINED";
        assertEquals(expectedStatus, lastCreditPaymentStatus);
    }

    @Test
    @DisplayName("Проверка статуса платежа при покупке произвольной картой")
    public void shouldHaveNullStatusForPaymentWithRandomCard() {
        debitPage = mainPage.debitPayment();
        debitPage.enterCardData(DataHelper.getRandomValidCardNumber(), DataHelper.getValidMonth(), DataHelper.getValidYear(), DataHelper.getValidOwner(), DataHelper.getValidCVC());
        debitPage.checkOnlyErrorMessage();
        String lastPaymentStatus = SQLHelper.getLastPaymentStatus();
        assertNull(lastPaymentStatus);
    }

    @Test
    @DisplayName("Проверка статуса платежа при покупке в кредит произвольной картой")
    public void shouldHaveNullStatusForCreditPaymentWithRandomCard() {
        creditPage = mainPage.creditPayment();
        creditPage.enterCardData(DataHelper.getRandomValidCardNumber(), DataHelper.getValidMonth(), DataHelper.getValidYear(), DataHelper.getValidOwner(), DataHelper.getValidCVC());
        creditPage.checkOnlyErrorMessage();
        String lastCreditPaymentStatus = SQLHelper.getLastCreditPaymentStatus();
        assertNull(lastCreditPaymentStatus);
    }

    @Test
    @DisplayName("Проверка размера платежа при покупке картой со статусом DECLINED")
    public void shouldHave45000AmountPaymentWithDeclinedCard() {
        debitPage = mainPage.debitPayment();
        debitPage.enterCardData(DataHelper.getDeclinedCard(), DataHelper.getValidMonth(), DataHelper.getValidYear(), DataHelper.getValidOwner(), DataHelper.getValidCVC());
        debitPage.checkSuccessMessage();
        int paymentAmount = SQLHelper.getPaymentAmount();
        int expectedAmount = 45_000;
        assertEquals(expectedAmount, paymentAmount);
    }

    @Test
    @DisplayName("Проверка размера платежа при покупке картой со статусом APPROVED")
    public void shouldHave45000AmountPaymentWithApprovedCard() {
        debitPage = mainPage.debitPayment();
        debitPage.enterCardData(DataHelper.getApprovedCard(), DataHelper.getValidMonth(), DataHelper.getValidYear(), DataHelper.getValidOwner(), DataHelper.getValidCVC());
        debitPage.checkSuccessMessage();
        int paymentAmount = SQLHelper.getPaymentAmount();
        int expectedAmount = 45_000;
        assertEquals(expectedAmount, paymentAmount);
    }

}
