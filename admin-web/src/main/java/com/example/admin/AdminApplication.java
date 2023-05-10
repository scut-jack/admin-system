package com.example.admin;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;

/**
 * 启动程序
 */
@Slf4j
@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class})
public class AdminApplication {
    public static void main(String[] args) {
        // System.setProperty("spring.devtools.restart.enabled", "false");
        SpringApplication.run(AdminApplication.class, args);
        log.info("\n" +
                "________  ________  _____ ______   ___  ________      \n" +
                "|\\   __  \\|\\   ___ \\|\\   _ \\  _   \\|\\  \\|\\   ___  \\    \n" +
                "\\ \\  \\|\\  \\ \\  \\_|\\ \\ \\  \\\\\\__\\ \\  \\ \\  \\ \\  \\\\ \\  \\   \n" +
                " \\ \\   __  \\ \\  \\ \\\\ \\ \\  \\\\|__| \\  \\ \\  \\ \\  \\\\ \\  \\  \n" +
                "  \\ \\  \\ \\  \\ \\  \\_\\\\ \\ \\  \\    \\ \\  \\ \\  \\ \\  \\\\ \\  \\ \n" +
                "   \\ \\__\\ \\__\\ \\_______\\ \\__\\    \\ \\__\\ \\__\\ \\__\\\\ \\__\\\n" +
                "    \\|__|\\|__|\\|_______|\\|__|     \\|__|\\|__|\\|__| \\|__|");
    }
}
