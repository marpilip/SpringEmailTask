package com.testSoftaria.SpringEmailTask.pilipenko.information;

import java.io.*;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class PageStatus {
    private final Map<String, WebSite> yesterdaySites;
    private final Map<String, WebSite> todaySites;
    private final Set<String> disappearedPages = new HashSet<>();
    private final Set<String> appearedPages = new HashSet<>();
    private final Set<String> changedPages = new HashSet<>();

    public PageStatus(Map<String, WebSite> yesterdaySites, Map<String, WebSite> todaySites) {
        this.yesterdaySites = yesterdaySites;
        this.todaySites = todaySites;
    }

    public void checkChanges() {
        yesterdaySites.forEach((url, yesterdayWebsite) -> {
            WebSite todayWebsite = todaySites.get(url);
            if (todayWebsite == null) {
                disappearedPages.add(url);
            } else if (!todayWebsite.html().equals(yesterdayWebsite.html())) {
                changedPages.add(url);
            }
        });

        todaySites.forEach((url, todayWebsite) -> {
            if (!yesterdaySites.containsKey(url)) {
                appearedPages.add(url);
            }
        });
    }

    public String getReport() {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        String name;
        checkChanges();

        try {
            System.out.println("Введите имя и отчество секретаря:");
            name = br.readLine();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        StringBuilder report = new StringBuilder("Здравствуйте, дорогая " + name).append(System.lineSeparator()).append(System.lineSeparator());

        report.append("За последние сутки во вверенных Вам сайтах произошли следующие изменения: ").append(System.lineSeparator()).append(System.lineSeparator());

        String disappearedPagesMessage = "Исчезли следующие страницы:";
        String appearedPagesMessage = "Появились следующие страницы:";
        String changedPagesMessage = "Изменились следующие страницы:";

        report.append(getMessage(disappearedPagesMessage, disappearedPages));
        report.append(getMessage(appearedPagesMessage, appearedPages));
        report.append(getMessage(changedPagesMessage, changedPages));

        report.append(System.lineSeparator()).append("С уважением,").append(System.lineSeparator());
        report.append("автоматизированная система").append(System.lineSeparator()).append("мониторинга.");
        return report.toString();
    }


    private StringBuilder getMessage(String typeMessage, Set<String> pagesType) {
        StringBuilder message = new StringBuilder();

        if (pagesType == null || pagesType.isEmpty()) {
            message.append(typeMessage).append(System.lineSeparator()).append("Нет изменений").append(System.lineSeparator());
            return message;
        }

        message.append(typeMessage).append(System.lineSeparator());

        for (String url : pagesType) {
            message.append(url).append(System.lineSeparator());
        }

        return message.append(System.lineSeparator());
    }

    // метод для записи отчета в файл при необходимости
    public void writeReportToFile(String filePath) {
        File file = new File(filePath);

        try (PrintWriter writer = new PrintWriter(file)) {
            writer.write(getReport());

            System.out.println("Отчет успешно записан в файл");
        } catch (IOException e) {
            System.out.println("Ошибка записи в файл");
        }
    }
}
