package ru.ystu.encryptionapp.service;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
public class DatabaseServiceImpl implements DatabaseService {
    @PersistenceContext
    private EntityManager entityManager;

    @Transactional
    @Override
    public void clearAllTables() {
        Query query = entityManager.createNativeQuery("SELECT table_name FROM information_schema.tables WHERE table_schema='public'");
        @SuppressWarnings("unchecked")
        List<Object> resultList = query.getResultList();
        List<String> tableNames = resultList.stream()
                .map(Object::toString)
                .toList();
        for (String tableName : tableNames) {
            if (!tableName.equals("pg_stat_statements")) {
                log.info("Truncating table {}", tableName);
                entityManager.createNativeQuery("TRUNCATE TABLE " + tableName + " CASCADE").executeUpdate();
            }
        }
    }
}
