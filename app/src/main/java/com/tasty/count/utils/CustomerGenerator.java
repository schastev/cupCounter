package com.tasty.count.utils;

import com.github.javafaker.Faker;

import java.time.LocalDate;
import java.util.Locale;
import java.util.concurrent.ThreadLocalRandom;

public abstract class CustomerGenerator {

    public static String randomName() {
        Faker faker = new Faker(Locale.forLanguageTag("ru"));
        return faker.name().firstName();
    }

    public static String randomNumber() {
        Faker faker = new Faker(Locale.forLanguageTag("ru"));
        return faker.phoneNumber().cellPhone();

    }

    public static LocalDate randomRegistrationDate() {
        long minDay = LocalDate.of(2021, 6, 1).toEpochDay();
        long maxDay = LocalDate.now().toEpochDay();
        long randomDay = ThreadLocalRandom.current().nextLong(minDay, maxDay);
        return LocalDate.ofEpochDay(randomDay);
    }

    public static LocalDate randomLastVisitDate(LocalDate min) {
        long minDay = min.toEpochDay();
        long maxDay = LocalDate.now().toEpochDay();
        long randomDay = ThreadLocalRandom.current().nextLong(minDay, maxDay);
        return LocalDate.ofEpochDay(randomDay);
    }

    public static int randomCups() {
        return (int) Math.floor(Math.random() * (50));
    }


}
