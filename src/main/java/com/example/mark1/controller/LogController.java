package com.example.mark1.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.sql.DataSource;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Controller
public class LogController {

    @Autowired
    private DataSource dataSource;

    private static final String LOG_FILE = System.getProperty("user.dir") + "/logs/actions.log";  // Указан абсолютный путь
    private static final String BACKUP_FILE_SQL = "backup/backup.sql";
    private static final String BACKUP_FILE_CSV = "backup/backup.csv";

    private ExecutorService executorService = Executors.newFixedThreadPool(2);

    @GetMapping("/logpage")
    public String showLogPage(Model model) {
        return "logpage";
    }

    @GetMapping("/startBackup")
    public String startBackup(Model model) {
        executorService.submit(this::backupDatabaseToSQL);
        executorService.submit(this::backupDatabaseToCSV);
        model.addAttribute("message", "Резервное копирование запущено.");
        return "logpage";
    }

    @GetMapping("/logs")
    @ResponseBody
    public String getLogs() {
        try {
            return Files.readString(Paths.get(LOG_FILE));
        } catch (Exception e) {
            return "Ошибка загрузки логов: " + e.getMessage();
        }
    }

    private void backupDatabaseToSQL() {
        try (Connection connection = dataSource.getConnection();
             PrintWriter writer = new PrintWriter(BACKUP_FILE_SQL)) {

            Statement stmt = connection.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT * FROM us");

            writer.println("-- Резервная копия таблицы us");
            while (rs.next()) {
                writer.printf("INSERT INTO us (user_id, username, email, password, role_id) VALUES (%d, '%s', '%s', '%s', %d);\n",
                        rs.getInt("user_id"),
                        rs.getString("username"),
                        rs.getString("email"),
                        rs.getString("password"),
                        rs.getInt("role_id"));
            }
            logAction("Резервное копирование SQL завершено.");
        } catch (Exception e) {
            logAction("Ошибка резервного копирования SQL: " + e.getMessage());
        }
    }

    private void backupDatabaseToCSV() {
        try (Connection connection = dataSource.getConnection();
             FileWriter writer = new FileWriter(BACKUP_FILE_CSV)) {

            Statement stmt = connection.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT * FROM us");

            writer.write("user_id,username,email,password,role_id\n");
            while (rs.next()) {
                writer.write(String.format("%d,%s,%s,%s,%d\n",
                        rs.getInt("user_id"),
                        rs.getString("username"),
                        rs.getString("email"),
                        rs.getString("password"),
                        rs.getInt("role_id")));
            }
            logAction("Резервное копирование CSV завершено.");
        } catch (Exception e) {
            logAction("Ошибка резервного копирования CSV: " + e.getMessage());
        }
    }

    private void logAction(String action) {
        try (FileWriter writer = new FileWriter(LOG_FILE, true)) {  // Открытие для дозаписи
            String timestamp = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
            writer.write(String.format("[%s] %s\n", timestamp, action));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
