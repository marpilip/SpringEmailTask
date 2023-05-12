package com.testSoftaria.SpringEmailTask;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import test_task.pilipenko.information.PageStatus;
import test_task.pilipenko.information.WebSite;

import java.util.HashMap;
import java.util.Map;

@SpringBootApplication
public class SpringEmailTaskApplication {

    @Autowired
    private EmailSenderService senderService;

    public static void main(String[] args) {
        SpringApplication.run(SpringEmailTaskApplication.class, args);
    }

    @EventListener(ApplicationReadyEvent.class)
    public void sendEmail() {
        Map<String, WebSite> yesterdaySites = new HashMap<>();
        Map<String, WebSite> todaySites = new HashMap<>();
        String filePath = "report.txt";

        yesterdaySites.put("http://site1.com", new WebSite("http://site1.com", "<html>...</html>"));
        yesterdaySites.put("http://site2.com", new WebSite("http://site2.com", "<html>...</html>"));
        yesterdaySites.put("http://site3.com", new WebSite("http://site3.com", "<html>...</html>"));

        todaySites.put("http://site1.com", new WebSite("http://site1.com", "<html>...</html>"));
        todaySites.put("http://site3.com", new WebSite("http://site3.com", "<html>...</html>"));
        todaySites.put("http://site4.com", new WebSite("http://site4.com", "<html>...</html>"));

        PageStatus pageStatus = new PageStatus(yesterdaySites, todaySites);

        senderService.sendEmail("mar.pilip@gmail.com", "Changes on sites", pageStatus.getReport());
    }
}
