package ru.ystu.encryptionapp.config;

import jakarta.transaction.Transactional;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import ru.ystu.encryptionapp.service.DatabaseService;

@Configuration
@EnableScheduling
public class DatabaseCleanupJob {
    private final DatabaseService databaseService;

    public DatabaseCleanupJob(DatabaseService databaseService) {
        this.databaseService = databaseService;
    }

    @Scheduled(cron = "0 0 0 */3 * *") // Запуск в полночь каждые 3 дня
    @Transactional
    public void clearDatabaseTables() {
        databaseService.clearAllTables();
    }
}

