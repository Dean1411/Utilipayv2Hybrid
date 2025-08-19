package UtilipayV2Hybrid.utilities;

import java.io.InputStream;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.stream.Collectors;

public class DatabaseCleanupHelper {

    private static Properties getDatabaseProperties() {
        Properties properties = new Properties();
        try (InputStream input = DatabaseCleanupHelper.class.getClassLoader().getResourceAsStream("data.properties")) {
            if (input == null) {
                System.err.println("data.properties not found in src/test/resources!");
                return null;
            }
            properties.load(input);
        } catch (Exception e) {
            System.err.println("Failed to load database properties: " + e.getMessage());
        }
        return properties;
    }
    
    public static void collectAndRunCleanupForMeter(String meterNumber) {
        Properties props = getDatabaseProperties();
        if (props == null) return;

        String url = props.getProperty("DB_CONNECTION_STRING");
        String user = props.getProperty("DB_USERNAME");
        String pass = props.getProperty("DB_PASSWORD");

        List<Integer> standIds = new ArrayList<>();
        List<Integer> meterIds = new ArrayList<>();
        List<Integer> municipalityIds = new ArrayList<>();

        try (Connection conn = DriverManager.getConnection(url, user, pass)) {

            // 1. Get stand IDs for the meter number
            String sqlStand = "SELECT DISTINCT s.Id FROM Meters m " +
                              "JOIN Stands s ON m.StandId = s.Id " +
                              "WHERE m.MeterNumber = ?";
            try (PreparedStatement stmt = conn.prepareStatement(sqlStand)) {
                stmt.setString(1, meterNumber);
                ResultSet rs = stmt.executeQuery();
                while (rs.next()) {
                    standIds.add(rs.getInt("Id"));
                }
            }

            if (standIds.isEmpty()) {
                System.out.println("No stand IDs found for meter: " + meterNumber);
                return;
            }


           // runCleanupWithStands(conn, standIds);

            // 2. Get meter IDs belonging to those stands
            String sqlMeters = "SELECT Id FROM Meters WHERE StandId IN (" + toSqlList(standIds) + ")";
            try (Statement stmt = conn.createStatement()) {
                ResultSet rs = stmt.executeQuery(sqlMeters);
                while (rs.next()) {
                    meterIds.add(rs.getInt("Id"));
                }
            }

            // 3. Get municipality IDs linked to those stands
            String sqlMunicipality = "SELECT DISTINCT MunicipalityId FROM Stands WHERE Id IN (" + toSqlList(standIds) + ")";
            try (Statement stmt = conn.createStatement()) {
                ResultSet rs = stmt.executeQuery(sqlMunicipality);
                while (rs.next()) {
                    municipalityIds.add(rs.getInt("MunicipalityId"));
                }
            }

            System.out.println("Perform Cleanup");

            // DELETE from Transactions
            if (!meterIds.isEmpty()) {
                executeDelete(conn, "Transactions", "MeterId", meterIds);
            }
            
            // DELETE from MeterInstallationHistory
            executeDelete(conn, "MeterInstallationHistory", "StandId", standIds);

            // DELETE from Meters
            executeDelete(conn, "Meters", "StandId", standIds);

            // DELETE from Accounts
            executeDelete(conn, "Accounts", "StandId", standIds);

            // DELETE from SystemLogEntries
            if (!municipalityIds.isEmpty()) {
                executeDelete(conn, "SystemLogEntries", "MunicipalityId", municipalityIds);
            }

            // DELETE from Municipality
            if (!municipalityIds.isEmpty()) {
                executeDelete(conn, "Municipality", "Id", municipalityIds);
            }

            System.out.println("Cleanup completed for meter number: " + meterNumber);

        } catch (SQLException e) {
            System.err.println("Error during cleanup: " + e.getMessage());
            e.printStackTrace();
        }
    }

    // Helper methods
    private static String toSqlList(List<Integer> ids) {
        return ids.stream()
                  .map(String::valueOf)
                  .collect(Collectors.joining(","));
    }

    private static void executeDelete(Connection conn, String table, String column, List<Integer> ids) throws SQLException {
        if (ids.isEmpty()) return;

        String sql = "DELETE FROM " + table + " WHERE " + column + " IN (" + toSqlList(ids) + ")";
        try (Statement stmt = conn.createStatement()) {
            int deletedRows = stmt.executeUpdate(sql);
            System.out.println("Executed: [" + sql + "] → Rows affected: " + deletedRows);
        }
    }

}
