package ru.netology.data;

import com.github.javafaker.Faker;
import lombok.Value;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Locale;
import java.util.Random;

public class DataHelper {
    private static final Faker FAKER = new Faker(Locale.ENGLISH);
    private static final Faker cyrillicFAKER = new Faker(new Locale("ru", "RU"));

    private DataHelper() {
    }

    //валидные номера карт

    public static String getApprovedCard() {
        return "4444 4444 4444 4441";
    }

    public static String getDeclinedCard() {
        return "4444 4444 4444 4442";
    }

    public static String getRandomValidCardNumber() {
        return FAKER.business().creditCardNumber();
    }

    //невалидные номера карт

    public static String getEmptyCardNumber() {
        return "";
    }

    public static String getShortCardNumber() {
        return FAKER.numerify("#### #### #### ###");
    }

    public static String getLongCardNumber() {
        return FAKER.numerify("#### #### #### #### ##");
    }

    public static String getCardNumberWithLatinLetters() {
        return FAKER.letterify("???? ???? ???? ????");
    }

    public static String getCardNumberWithCyrillicLetters() {
        return cyrillicFAKER.letterify("???? ???? ???? ????");
    }

    public static String getCardNumberWithSpecialSymbols() {
        return "!»№; %:?* ()_+ []<>";
    }


//валидное значение поля "Месяц"

    public static String getValidMonth() {
        var months = new String[]{
                "01", "02", "03", "04", "05", "06", "07", "08", "09", "10", "11", "12"
        };
        return months[new Random().nextInt(months.length)];
    }

    //невалидные значения поля "Месяц"

    public static String getEmptyMonth() {
        return "";
    }

    public static String getMonthWithSpecialSymbols() {
        return "№;";
    }

    public static String getMonthWithLatinLetters() {
        return FAKER.letterify("??");
    }

    public static String getMonthWithCyrillicLetters() {
        return cyrillicFAKER.letterify("??");
    }

    public static String getShortMonth() {
        int month = FAKER.number().numberBetween(0, 9);
        return String.valueOf(month);
    }

    public static String getLongMonth() {
        int month = FAKER.number().numberBetween(100, 129);
        return String.valueOf(month);
    }

    public static String getZeroMonth() {
        return "00";
    }

    public static String getMonthMoreThan12() {
        int month = FAKER.number().numberBetween(13, 99);
        return String.valueOf(month);
    }

//валидное значение поля "Год"

    public static String getValidYear() {
        var year = LocalDate.now().plusYears(5).format(DateTimeFormatter.ofPattern("yy"));
        return year;
    }

    //невалидные значения поля "Год"

    public static String getEmptyYear() {
        return "";
    }

    public static String getYearWithSpecialSymbols() {
        return "^&";
    }

    public static String getYearWithLatinLetters() {
        return FAKER.letterify("??");
    }

    public static String getYearWithCyrillicLetters() {
        return cyrillicFAKER.letterify("??");
    }

    public static String getShortYear() {
        int year = FAKER.number().numberBetween(0, 9);
        return String.valueOf(year);
    }

    public static String getLongYear() {
        int year = FAKER.number().numberBetween(250, 309);
        return String.valueOf(year);
    }

    public static String getPreviousYear() {
        var year = LocalDate.now().minusYears(3).format(DateTimeFormatter.ofPattern("yy"));
        return year;
    }

    public static String getYearPlusMoreThan5() {
        var year = LocalDate.now().plusYears(7).format(DateTimeFormatter.ofPattern("yy"));
        return year;
    }

    //валидное значение поля "CVC/CVV"

    public static String getValidCVC() {
        return FAKER.numerify("###");
    }

    //невалидные значения поля "CVC/CVV"

    public static String getEmptyCVC() {
        return "";
    }

    public static String getCVCWithSpecialSymbols() {
        return "^&$";
    }

    public static String getCVCWithLatinLetters() {
        return FAKER.letterify("???");
    }

    public static String getCVCWithCyrillicLetters() {
        return cyrillicFAKER.letterify("???");
    }

    public static String getShortCVC() {
        return FAKER.numerify("##");
    }

    public static String getLongCVC() {
        return FAKER.numerify("####");
    }

    //валидные значения поля "Владелец"

    public static String getValidOwner() {
        return FAKER.name().lastName().toUpperCase() + " " + FAKER.name().firstName().toUpperCase();
    }

    public static String getValidOwnerWithDoubleFirstName() {
        return FAKER.name().lastName().toUpperCase() + " " + FAKER.name().firstName().toUpperCase() + "-" + FAKER.name().firstName().toUpperCase();
    }

    public static String getValidOwnerWithDoubleLastName() {
        return FAKER.name().lastName().toUpperCase() + "-" + FAKER.name().lastName().toUpperCase() + " " + FAKER.name().firstName().toUpperCase();
    }

    public static String getValidOwnerWithDoubleFirstAndLastNames() {
        return FAKER.name().lastName().toUpperCase() + "-" + FAKER.name().lastName().toUpperCase() + " " + FAKER.name().firstName().toUpperCase() + "-" + FAKER.name().firstName().toUpperCase();
    }

    //невалидные значения поля "Владелец"

    public static String getEmptyOwner() {
        return "";
    }

    public static String getOwnerWithSpecialSymbols() {
        return "^&$%:? ;%:?:";
    }

    public static String getOwnerWithCyrillicLetters() {
        return cyrillicFAKER.name().lastName().toUpperCase() + " " + cyrillicFAKER.name().firstName().toUpperCase();
    }

    public static String getOwnerWithNumbers() {
        return FAKER.numerify("#########") + " " + FAKER.numerify("########");
    }

    public static String getOwnerWithLatinLettersAndNumbers() {
        return FAKER.name().lastName().toUpperCase() + " " + FAKER.numerify("########");
    }

    public static String getOwnerWithLatinLettersAndSpecialSymbols() {
        return "#$%^&*($%^&*" + " " + FAKER.name().firstName().toUpperCase();
    }

    public static String getOwnerWithLatinAndCyrillicLetters() {
        return FAKER.name().lastName().toUpperCase() + " " + cyrillicFAKER.name().firstName().toUpperCase();
    }

    public static String getOwnerWithoutWhitespace() {
        return FAKER.name().lastName().toUpperCase() + FAKER.name().firstName().toUpperCase();
    }

}
