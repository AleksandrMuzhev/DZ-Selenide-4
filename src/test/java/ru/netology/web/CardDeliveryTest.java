package ru.netology.web;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebElement;

import java.time.Duration;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import static com.codeborne.selenide.Condition.*;
import static com.codeborne.selenide.Selenide.*;

public class CardDeliveryTest {
    @BeforeEach
    public void setUp() {
        open("http://localhost:9999/");
    }

    private String getFutureDate(int addDays) {
        LocalDate currentDate = LocalDate.now();
        LocalDate futureDate = currentDate.plusDays(addDays);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");
        String formattedDate = futureDate.format(formatter);
        return formattedDate;
    }

    @Test
    public void shouldSendForm() {
        $("[data-test-id=city] input").setValue("Казань");
        $(".calendar-input__custom-control input").doubleClick().sendKeys(getFutureDate(4));
        $("[data-test-id=name] input").setValue("Александр Мужев-Иванов");
        $("[data-test-id=phone] input").setValue("+79120009999");
        $(".checkbox__box").click();
        $(".button").click();
        WebElement title = $("[data-test-id=notification] .notification__title").shouldBe(visible, Duration.ofSeconds(20));
        Assertions.assertEquals("Успешно!", title.getText());
        WebElement content = $("[data-test-id=notification] .notification__content");
        Assertions.assertEquals("Встреча успешно забронирована на " + getFutureDate(4), content.getText());
    }

    @Test
    public void shouldValidateCity() {
        $("[data-test-id=city] input").setValue("Нью-Йорк");
        $(".calendar-input__custom-control input").doubleClick().sendKeys(getFutureDate(4));
        $("[data-test-id=name] input").setValue("Александр Мужев-Иванов");
        $("[data-test-id=phone] input").setValue("+79120009999");
        $(".checkbox__box").click();
        $(".button").click();
        $("[data-test-id=city].input_invalid .input__sub").shouldHave(exactText("Доставка в выбранный город недоступна"));
    }

    @Test
    public void shouldNotCity() {
        $(".calendar-input__custom-control input").doubleClick().sendKeys(getFutureDate(4));
        $("[data-test-id=name] input").setValue("Александр Мужев-Иванов");
        $("[data-test-id=phone] input").setValue("+79120009999");
        $(".checkbox__box").click();
        $(".button").click();
        $("[data-test-id=city].input_invalid .input__sub").shouldHave(exactText("Поле обязательно для заполнения"));
    }

    @Test
    public void shouldValidateDate() {
        $("[data-test-id=city] input").setValue("Казань");
        $(".calendar-input__custom-control input").doubleClick().sendKeys(Keys.BACK_SPACE);
        $("[data-test-id=name] input").setValue("Александр Мужев-Иванов");
        $("[data-test-id=phone] input").setValue("+79120009999");
        $(".checkbox__box").click();
        $(".button").click();
        $("[data-test-id=date] .input_invalid .input__sub").shouldHave(exactText("Неверно введена дата"));
    }

    @Test
    public void shouldValidateName() {
        $("[data-test-id=city] input").setValue("Казань");
        $(".calendar-input__custom-control input").doubleClick().sendKeys(getFutureDate(4));
        $("[data-test-id=name] input").setValue("Aleksandr!@$#@$#");
        $("[data-test-id=phone] input").setValue("+79120009999");
        $(".checkbox__box").click();
        $(".button").click();
        $("[data-test-id=name].input_invalid .input__sub").shouldHave(exactText("Имя и Фамилия указаные неверно. Допустимы только русские буквы, пробелы и дефисы."));
    }

    @Test
    public void shouldNoName() {
        $("[data-test-id=city] input").setValue("Казань");
        $(".calendar-input__custom-control input").doubleClick().sendKeys(getFutureDate(4));
        $("[data-test-id=phone] input").setValue("+79120009999");
        $(".checkbox__box").click();
        $(".button").click();
        $("[data-test-id=name].input_invalid .input__sub").shouldHave(exactText("Поле обязательно для заполнения"));
    }

    @Test
    public void shouldValidatePhone() {
        $("[data-test-id=city] input").setValue("Казань");
        $(".calendar-input__custom-control input").doubleClick().sendKeys(getFutureDate(4));
        $("[data-test-id=name] input").setValue("Александр Мужев-Иванов");
        $("[data-test-id=phone] input").setValue("+7912000");
        $(".checkbox__box").click();
        $(".button").click();
        $("[data-test-id=phone].input_invalid .input__sub").shouldHave(exactText("Телефон указан неверно. Должно быть 11 цифр, например, +79012345678."));
    }

    @Test
    public void shouldNoPhone() {
        {
            $("[data-test-id=city] input").setValue("Казань");
            $(".calendar-input__custom-control input").doubleClick().sendKeys(getFutureDate(4));
            $("[data-test-id=name] input").setValue("Александр Мужев-Иванов");
            $(".checkbox__box").click();
            $(".button").click();
            $("[data-test-id=phone].input_invalid .input__sub").shouldHave(exactText("Поле обязательно для заполнения"));
        }
    }

    @Test
    public void shouldInvalidCheckBox() {
        $("[data-test-id=city] input").setValue("Казань");
        $(".calendar-input__custom-control input").doubleClick().sendKeys(getFutureDate(4));
        $("[data-test-id=name] input").setValue("Александр Мужев-Иванов");
        $("[data-test-id=phone] input").setValue("+79120009999");
        $(".button").click();
        $("[data-test-id=agreement].input_invalid").shouldBe(visible, Duration.ofSeconds(5));
    }

}
