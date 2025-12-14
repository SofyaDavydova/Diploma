package ru.netology.page;

import com.codeborne.selenide.SelenideElement;

import java.time.Duration;

import static com.codeborne.selenide.Condition.*;
import static com.codeborne.selenide.Selectors.byText;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$$;

public class CreditPage {
    private final SelenideElement headingBuyOnCredit = $(byText("Кредит по данным карты"));
    private SelenideElement cardNumberField = $("[placeholder='0000 0000 0000 0000']");
    private SelenideElement monthField = $("[placeholder='08']");
    private SelenideElement yearField = $("[placeholder='22']");
    private SelenideElement ownerField = $$("[class=input__inner]").findBy(text("Владелец")).$("[class=input__control]");
    private SelenideElement cvcField = $("[placeholder='999']");
    private SelenideElement continueButton = $(byText("Продолжить"));
    private SelenideElement waitMessage = $(byText("Отправляем запрос в Банк..."));
    private SelenideElement successMessage = $(byText("Операция одобрена Банком."));
    private SelenideElement errorMessage = $(byText("Ошибка! Банк отказал в проведении операции."));
    private SelenideElement emptyInputMessage = $(byText("Поле обязательно для заполнения"));
    private SelenideElement wrongFormatMessage = $(byText("Неверный формат"));
    private SelenideElement wrongDateMessage = $(byText("Неверно указан срок действия карты"));
    private SelenideElement expiredCardMessage = $(byText("Истёк срок действия карты"));

    public void checkOpenCreditPage() {
        headingBuyOnCredit.should(visible);
    }

    public void enterCardData(String cardNumber, String month, String year, String owner, String cvc) {
        this.cardNumberField.val(String.valueOf(cardNumber));
        this.monthField.val(String.valueOf(month));
        this.yearField.val(String.valueOf(year));
        this.ownerField.val(owner);
        this.cvcField.val(String.valueOf(cvc));
        continueButton.click();
    }

    public void checkSuccessMessage() {
        waitMessage.shouldBe(visible);
        successMessage.should(visible, Duration.ofSeconds(15));
        errorMessage.shouldBe(hidden, Duration.ofSeconds(15));
    }

    public void checkErrorMessage() {
        waitMessage.shouldBe(visible);
        errorMessage.should(visible, Duration.ofSeconds(15));
        successMessage.shouldBe(hidden, Duration.ofSeconds(15));
    }

    public void checkOnlyErrorMessage() {
        errorMessage.should(visible, Duration.ofSeconds(15));
    }

    public void checkEmptyInputMessage() {
        emptyInputMessage.should(visible);
    }

    public void checkWrongFormatMessage() {
        wrongFormatMessage.should(visible);
    }

    public void checkWrongDateMessage() {
        wrongDateMessage.should(visible);
    }

    public void checkExpiredCardMessage() {
        expiredCardMessage.should(visible);
    }
}
