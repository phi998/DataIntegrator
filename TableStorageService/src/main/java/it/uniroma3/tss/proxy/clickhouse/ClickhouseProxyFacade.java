package it.uniroma3.tss.proxy.clickhouse;

import it.uniroma3.tss.config.ClickHouseConfig;
import it.uniroma3.tss.domain.vo.ResultEntry;
import it.uniroma3.tss.domain.vo.TableField;
import it.uniroma3.tss.domain.vo.TableRow;
import it.uniroma3.tss.proxy.ProxyFacade;
import it.uniroma3.tss.utils.Util;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import ru.yandex.clickhouse.ClickHouseConnection;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.*;

@Repository
@Slf4j
public class ClickhouseProxyFacade implements ProxyFacade {

    private static final String dbName = ClickHouseConfig.dbName;

    @Autowired
    private ClickHouseConnection session;

    @Override
    public void loadTableStructure(String tableName, List<TableField> fields) {
        log.info("loadTableStructure(): tableName={}, fields={}", tableName, fields);

        String query = "CREATE TABLE IF NOT EXISTS " + dbName + "." + tableName + " (";

        List<String> tableFields = new ArrayList<>();
        for(TableField tableField: fields) {
            String fieldName = tableField.getSymbolicName();
            String sqlTypeName = tableField.getType().sqlName;

            tableFields.add(fieldName + " " + sqlTypeName);
        }

        query += "id UUID DEFAULT generateUUIDv4()";

        if(tableFields.size() > 0)
            query += "," + String.join(",",tableFields);

        query += ") ENGINE = MergeTree() ORDER BY id";

        log.info("Executing query: "+query);
        try {
            session.createStatement().executeUpdate(query);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Map<Integer, TableRow> uploadData(String tableName, Map<Integer, TableRow> table) {
        log.info("uploadData(): tableName = {}, table={}", tableName, table);

        List<String> columnsNames = this.getColumnsNamesOfTable(tableName);
        table.values().forEach(tr -> tr.deleteKeysNotIn(columnsNames));

        // all table rows have same keys
        List<String> headers = table.values().stream().toList().get(0).getFieldName2Content().keySet().stream().map(Util::convertToSnakeCase).toList();

        List<String> questionMarks = Collections.nCopies(headers.size(), "?");
        String query = "INSERT INTO "+dbName+"."+tableName+" (" + String.join(",", headers) + ") VALUES (" + String.join(",",questionMarks) + ")";

        log.info("query={}", query);
        try {
            PreparedStatement escapedQuery = session.prepareStatement(query);
            for(Map.Entry<Integer, TableRow> entry: table.entrySet()) {
                TableRow row = entry.getValue();

                int i = 1;
                for(String value: row.getFieldName2Content().values()) {
                    escapedQuery.setString(i, value);
                    i++;
                }
                escapedQuery.addBatch();
            }

            escapedQuery.executeBatch();
            return table;
        } catch(SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Collection<ResultEntry> retrieveData(String tableName, Map<String, List<String>> query, int nResults) {
        log.info("retrieveData(): tableName={}, query={}, nResults={}", tableName, query, nResults);

        List<String> headers = query.keySet().stream().toList().stream().map(str -> { // all possible values for an attribute are in OR

            List<String> clausoles = new ArrayList<>();
            for(String value: query.get(str)) {
                String clausole = null;
                String formattedAttrName = Util.convertToSnakeCase(str);
                if(str.equals("id")) {
                    clausole = " " + formattedAttrName + " = '" + value + "' ";
                } else {
                    clausole = " " + formattedAttrName + " LIKE '%" + value + "%' ";
                }

                clausoles.add(clausole);
            }

            return String.join(" OR ", clausoles);
        }
        ).toList();

        String orQuery = String.join(" AND ", headers); // attributes are in AND

        String sqlQuery = "SELECT * FROM "+dbName+"."+tableName;
        if(!orQuery.isBlank())
            sqlQuery += " WHERE " + orQuery;

        log.info("retrieveData(): sqlQuery={}", sqlQuery);

        List<ResultEntry> resultEntries = new ArrayList<ResultEntry>();
        try {
            PreparedStatement escapedQuery = session.prepareStatement(sqlQuery);

            ResultSet results = escapedQuery.executeQuery();
            ResultSetMetaData metaData = results.getMetaData();
            int columnCount = metaData.getColumnCount();

            while(results.next()) {
                ResultEntry resultEntry = new ResultEntry();

                for (int j = 1; j <= columnCount; j++) {
                    String columnName = metaData.getColumnName(j);
                    String columnValue = results.getString(j);

                    resultEntry.addEntry(columnName, columnValue);
                }

                resultEntries.add(resultEntry);
            }
            return resultEntries;
        } catch(SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void addColumns(String tableName, List<TableField> fields) {
        log.info("addColumns(): tableName={}, fields={}", tableName, fields);

        List<String> tableFieldsAddCol = new ArrayList<>();
        for(TableField tableField: fields) {
            String fieldName = tableField.getSymbolicName();
            String sqlTypeName = tableField.getType().sqlName;

            tableFieldsAddCol.add("ADD COLUMN " + fieldName + " " + sqlTypeName);
        }

        String query = "ALTER TABLE " + dbName+"."+tableName + " " + String.join(",", tableFieldsAddCol);
        log.info("Executing query: " + query);
        try {
            session.createStatement().executeUpdate(query);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }



    public boolean existsTable(String tableName) {
        log.info("existsTable(): tableName={}",tableName);

        String query = "SELECT count(*) FROM system.tables WHERE database = ? AND name = ?";
        log.info("query={}",query);

        try (PreparedStatement statement = session.prepareStatement(query)) {
            statement.setString(1, dbName);
            statement.setString(2, tableName);

            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    int count = resultSet.getInt(1);
                    return count > 0;
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return false;
    }

    public List<String> getColumnsNamesOfTable(String tableName) {
        log.info("getColumnsNamesOfTable(): tableName={}",tableName);

        List<String> columnNames = new ArrayList<>();

        String query = "SELECT DISTINCT name FROM system.columns WHERE database = ? AND table = ?";

        try (PreparedStatement statement = session.prepareStatement(query)) {
            statement.setString(1, dbName);
            statement.setString(2, tableName);

            try (ResultSet resultSet = statement.executeQuery()) {
                while(resultSet.next()) {
                    String columnName = resultSet.getString("name");
                    columnNames.add(columnName);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        log.info("getColumnsNamesOfTable(): query={}, columnNames={}", query, columnNames);
        columnNames.remove("id"); // don't need it

        return columnNames;
    }



}
