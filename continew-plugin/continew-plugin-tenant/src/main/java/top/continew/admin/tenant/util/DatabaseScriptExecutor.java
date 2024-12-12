package top.continew.admin.tenant.util;


import org.springframework.jdbc.core.JdbcTemplate;

import javax.sql.DataSource;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

/**
 * @description: 数据库脚本执行器
 * @author: 小熊
 * @create: 2024-12-12 16:38
 */
public class DatabaseScriptExecutor {

    private JdbcTemplate jdbcTemplate;

    public DatabaseScriptExecutor(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    public void executeScript(String scriptPath) {
        try (BufferedReader reader = new BufferedReader(new FileReader(scriptPath))) {
            String sql;
            while ((sql = reader.readLine()) != null) {
                jdbcTemplate.execute(sql);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
