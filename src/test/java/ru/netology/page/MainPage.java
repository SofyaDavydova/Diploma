package ru.netology.page;

import com.codeborne.selenide.SelenideElement;

import static com.codeborne.selenide.Condition.exactText;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selectors.byText;
import static com.codeborne.selenide.Selenide.$;

public class MainPage {

    private final SelenideElement buyButton = $(byText("Купить"));
    private final SelenideElement buyOnCreditButton = $(byText("Купить в кредит"));

    public DebitPage debitPayment() {
        buyButton.click();
        return new DebitPage();
    }


    public CreditPage creditPayment() {
        buyOnCreditButton.click();
        return new CreditPage();
    }

}
