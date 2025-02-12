package ru.nirs.util;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.springframework.core.io.ClassPathResource;
import org.springframework.jdbc.datasource.init.ScriptUtils;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.support.TransactionTemplate;

import javax.sql.DataSource;

/**
 * Утильный класс, помогающий запускать скрипты перед/после теста, когда необходимо запустить несколько
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class ScriptUtilsHelper {

    public static void executeSqlScripts(DataSource dataSource, PlatformTransactionManager transactionManager, String... pathsToSqlFiles) {
        //Оборачиваем в TransactionTemplate для коммита, если автокоммит выключен
        new TransactionTemplate(transactionManager).execute(ts -> {
            try (var connection = dataSource.getConnection()) {
                for (String pathToSqlFile : pathsToSqlFiles) {
                    ScriptUtils.executeSqlScript(connection, new ClassPathResource(pathToSqlFile));
                }
                connection.commit();
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        });
    }

}
