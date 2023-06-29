package ru.netology.web;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.Keys;

import java.time.Duration;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import static com.codeborne.selenide.Condition.*;
import static com.codeborne.selenide.Selectors.byText;
import static com.codeborne.selenide.Selenide.*;

public class CardDeliveryTest {
    @BeforeEach
    public void setUp() {
        open("http://localhost:9999/");
    }

    public String generateDate(long addDays, String pattern) {
        return LocalDate.now().plusDays(addDays).format(DateTimeFormatter.ofPattern(pattern));
    }


    @Test
    public void shouldSendForm() {
        String planningDate = generateDate(7, "dd.MM.yyyy");

        $("[data-test-id=city] input").setValue("Казань");
        $(".calendar-input__custom-control input").doubleClick().sendKeys(planningDate);
        $("[data-test-id=name] input").setValue("Александр Мужев-Иванов");
        $("[data-test-id=phone] input").setValue("+79120009999");
        $(".checkbox__box").click();
        $(".button").click();
        $("[data-test-id=notification] .notification__title").shouldHave(exactText("Успешно!"), Duration.ofSeconds(20));
        $("[data-test-id=notification] .notification__content").shouldHave(exactText("Встреча успешно забронирована на " + planningDate));
    }

    @Test
    public void shouldValidateCity() {
        String planningDate = generateDate(9, "dd.MM.yyyy");

        $("[data-test-id=city] input").setValue("Нью-Йорк");
        $(".calendar-input__custom-control input").doubleClick().sendKeys(planningDate);
        $("[data-test-id=name] input").setValue("Александр Мужев-Иванов");
        $("[data-test-id=phone] input").setValue("+79120009999");
        $(".checkbox__box").click();
        $(".button").click();
        $("[data-test-id=city].input_invalid .input__sub").shouldHave(exactText("Доставка в выбранный город недоступна"), Duration.ofSeconds(10));
    }

    @Test
    public void shouldNotCity() {
        String planningDate = generateDate(3, "dd.MM.yyyy");

        $(".calendar-input__custom-control input").doubleClick().sendKeys(planningDate);
        $("[data-test-id=name] input").setValue("Александр Мужев-Иванов");
        $("[data-test-id=phone] input").setValue("+79120009999");
        $(".checkbox__box").click();
        $(".button").click();
        $("[data-test-id=city].input_invalid .input__sub").shouldHave(exactText("Поле обязательно для заполнения"), Duration.ofSeconds(10));
    }

    @Test
    public void shouldValidateDate() {
        $("[data-test-id=city] input").setValue("Казань");
        $(".calendar-input__custom-control input").doubleClick().sendKeys(Keys.BACK_SPACE);
        $("[data-test-id=name] input").setValue("Александр Мужев-Иванов");
        $("[data-test-id=phone] input").setValue("+79120009999");
        $(".checkbox__box").click();
        $(".button").click();
        $("[data-test-id=date] .input_invalid .input__sub").shouldHave(exactText("Неверно введена дата"), Duration.ofSeconds(10));
    }

    @Test
    public void shouldValidateName() {
        String planningDate = generateDate(5, "dd.MM.yyyy");

        $("[data-test-id=city] input").setValue("Казань");
        $(".calendar-input__custom-control input").doubleClick().sendKeys(planningDate);
        $("[data-test-id=name] input").setValue("Aleksandr!@$#@$#");
        $("[data-test-id=phone] input").setValue("+79120009999");
        $(".checkbox__box").click();
        $(".button").click();
        $("[data-test-id=name].input_invalid .input__sub").shouldHave(exactText("Имя и Фамилия указаные неверно. Допустимы только русские буквы, пробелы и дефисы."), Duration.ofSeconds(10));
    }

    @Test
    public void shouldNoName() {
        String planningDate = generateDate(4, "dd.MM.yyyy");

        $("[data-test-id=city] input").setValue("Казань");
        $(".calendar-input__custom-control input").doubleClick().sendKeys(planningDate);
        $("[data-test-id=phone] input").setValue("+79120009999");
        $(".checkbox__box").click();
        $(".button").click();
        $("[data-test-id=name].input_invalid .input__sub").shouldHave(exactText("Поле обязательно для заполнения"), Duration.ofSeconds(10));
    }

    @Test
    public void shouldValidatePhone() {
        String planningDate = generateDate(12, "dd.MM.yyyy");

        $("[data-test-id=city] input").setValue("Казань");
        $(".calendar-input__custom-control input").doubleClick().sendKeys(planningDate);
        $("[data-test-id=name] input").setValue("Александр Мужев-Иванов");
        $("[data-test-id=phone] input").setValue("+7912000");
        $(".checkbox__box").click();
        $(".button").click();
        $("[data-test-id=phone].input_invalid .input__sub").shouldHave(exactText("Телефон указан неверно. Должно быть 11 цифр, например, +79012345678."), Duration.ofSeconds(10));
    }

    @Test
    public void shouldNoPhone() {
        String planningDate = generateDate(8, "dd.MM.yyyy");

        $("[data-test-id=city] input").setValue("Казань");
        $(".calendar-input__custom-control input").doubleClick().sendKeys(planningDate);
        $("[data-test-id=name] input").setValue("Александр Мужев-Иванов");
        $(".checkbox__box").click();
        $(".button").click();
        $("[data-test-id=phone].input_invalid .input__sub").shouldHave(exactText("Поле обязательно для заполнения"), Duration.ofSeconds(10));
    }

    @Test
    public void shouldInvalidCheckBox() {
        String planningDate = generateDate(6, "dd.MM.yyyy");

        $("[data-test-id=city] input").setValue("Казань");
        $(".calendar-input__custom-control input").doubleClick().sendKeys(planningDate);
        $("[data-test-id=name] input").setValue("Александр Мужев-Иванов");
        $("[data-test-id=phone] input").setValue("+79120009999");
        $(".button").click();
        $("[data-test-id=agreement].input_invalid").shouldHave(exactText("Я соглашаюсь с условиями обработки и использования моих персональных данных"), Duration.ofSeconds(10));
    }

    @Test
    public void shouldSendFormTwoCharCity() {
        String planningDate = generateDate(10, "dd.MM.yyyy");

        $("[data-test-id=city] input").setValue("Ка").click();
        $(".popup__inner").shouldBe(exist, Duration.ofSeconds(5));
        $(byText("Екатеринбург")).click();
        $(".calendar-input__custom-control input").doubleClick().sendKeys(planningDate);
        $("[data-test-id=name] input").setValue("Александр Мужев-Иванов");
        $("[data-test-id=phone] input").setValue("+79120009999");
        $(".checkbox__box").click();
        $(".button").click();
        $("[data-test-id=notification] .notification__title").shouldHave(exactText("Успешно!"), Duration.ofSeconds(20));
        $("[data-test-id=notification] .notification__content").shouldHave(exactText("Встреча успешно забронирована на " + planningDate));
    }

    @Test
    public void shouldSendFormCalendar() {
        $("[data-test-id=city] input").setValue("Казань");
        $("button span.icon_name_calendar").click();
        $("div.calendar-input__calendar-wrapper").shouldBe(visible, Duration.ofSeconds(15));
        if (!generateDate(7, "MM").equals(generateDate(3, "MM"))) {
            $("div.popup div:nth-child(4)").click();
        }
        $(byText("4")).click();
        $("[data-test-id=name] input").setValue("Александр Мужев-Иванов");
        $("[data-test-id=phone] input").setValue("+79120009999");
        $(".checkbox__box").click();
        $(".button").click();
        $("[data-test-id=notification] .notification__title").shouldHave(exactText("Успешно!"), Duration.ofSeconds(20));
    }

}
