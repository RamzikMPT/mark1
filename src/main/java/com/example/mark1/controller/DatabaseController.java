package com.example.mark1.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import javax.sql.DataSource;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.*;

@Controller
public class DatabaseController {

    @Autowired
    private DataSource dataSource;

    // Экспорт всей базы данных в CSV
    @GetMapping("/export/csv")
    public ResponseEntity<byte[]> exportDatabaseToCSV() throws SQLException, IOException {
        StringBuilder csvContent = new StringBuilder();

        try (Connection connection = dataSource.getConnection()) {
            DatabaseMetaData metaData = connection.getMetaData();
            ResultSet tables = metaData.getTables(null, null, "%", new String[]{"TABLE"});

            while (tables.next()) {
                String tableName = tables.getString("TABLE_NAME");
                csvContent.append("### Table: ").append(tableName).append("\n");
                Statement stmt = connection.createStatement();
                ResultSet resultSet = stmt.executeQuery("SELECT * FROM " + tableName);
                ResultSetMetaData rsMetaData = resultSet.getMetaData();
                int columnCount = rsMetaData.getColumnCount();

                // Заголовок
                for (int i = 1; i <= columnCount; i++) {
                    csvContent.append(rsMetaData.getColumnName(i)).append(",");
                }
                csvContent.append("\n");

                // Данные
                while (resultSet.next()) {
                    for (int i = 1; i <= columnCount; i++) {
                        csvContent.append(resultSet.getString(i)).append(",");
                    }
                    csvContent.append("\n");
                }
                csvContent.append("\n");
                resultSet.close();
            }
        }

        byte[] csvBytes = csvContent.toString().getBytes();
        HttpHeaders headers = new HttpHeaders();
        headers.set(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=database.csv");
        headers.setContentType(org.springframework.http.MediaType.TEXT_PLAIN);

        return new ResponseEntity<>(csvBytes, headers, HttpStatus.OK);
    }

    // Экспорт всей базы данных в SQL
    @GetMapping("/export/sql")
    public ResponseEntity<byte[]> exportDatabaseToSQL() throws SQLException {
        StringBuilder sqlContent = new StringBuilder();

        try (Connection connection = dataSource.getConnection()) {
            DatabaseMetaData metaData = connection.getMetaData();
            ResultSet tables = metaData.getTables(null, null, "%", new String[]{"TABLE"});

            while (tables.next()) {
                String tableName = tables.getString("TABLE_NAME");

                sqlContent.append("-- Table: ").append(tableName).append("\n");

                // Получение данных
                Statement stmt = connection.createStatement();
                ResultSet resultSet = stmt.executeQuery("SELECT * FROM " + tableName);
                ResultSetMetaData rsMetaData = resultSet.getMetaData();
                int columnCount = rsMetaData.getColumnCount();

                while (resultSet.next()) {
                    sqlContent.append("INSERT INTO ").append(tableName).append(" (");
                    for (int i = 1; i <= columnCount; i++) {
                        sqlContent.append(rsMetaData.getColumnName(i));
                        if (i < columnCount) {
                            sqlContent.append(", ");
                        }
                    }
                    sqlContent.append(") VALUES (");
                    for (int i = 1; i <= columnCount; i++) {
                        String value = resultSet.getString(i);
                        sqlContent.append(value != null ? "'" + value + "'" : "NULL");
                        if (i < columnCount) {
                            sqlContent.append(", ");
                        }
                    }
                    sqlContent.append(");\n");
                }
                sqlContent.append("\n");
                resultSet.close();
            }
        }

        byte[] sqlBytes = sqlContent.toString().getBytes();
        HttpHeaders headers = new HttpHeaders();
        headers.set(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=database.sql");
        headers.setContentType(org.springframework.http.MediaType.TEXT_PLAIN);

        return new ResponseEntity<>(sqlBytes, headers, HttpStatus.OK);
    }

    // Импорт данных из файла (CSV или SQL)
    @PostMapping("/import")
    public String importDatabase(@RequestParam("file") MultipartFile file, Model model) {
        try {
            String fileType = file.getOriginalFilename().toLowerCase();

            try (Connection connection = dataSource.getConnection()) {
                if (fileType.endsWith(".csv")) {
                    BufferedReader reader = new BufferedReader(new InputStreamReader(file.getInputStream()));
                    String line;
                    String currentTable = null;

                    while ((line = reader.readLine()) != null) {
                        if (line.startsWith("### Table: ")) {
                            currentTable = line.replace("### Table: ", "").trim();
                        } else if (currentTable != null && !line.isEmpty()) {
                            String[] values = line.split(",");
                            String query = buildInsertQuery(currentTable, values);
                            PreparedStatement stmt = connection.prepareStatement(query);
                            stmt.executeUpdate();
                        }
                    }
                } else if (fileType.endsWith(".sql")) {
                    String sqlContent = new String(file.getBytes());
                    Statement stmt = connection.createStatement();
                    stmt.execute(sqlContent);
                }
            }

            model.addAttribute("message", "Импорт данных успешно выполнен!");
        } catch (Exception e) {
            model.addAttribute("error", "Ошибка импорта данных: " + e.getMessage());
        }

        return "adminbdpage";
    }

    private String buildInsertQuery(String tableName, String[] values) {
        StringBuilder query = new StringBuilder("INSERT INTO ");
        query.append(tableName).append(" VALUES (");
        for (int i = 0; i < values.length; i++) {
            query.append("'").append(values[i].replace("'", "''")).append("'");
            if (i < values.length - 1) {
                query.append(", ");
            }
        }
        query.append(")");
        return query.toString();
    }
}
