package UtilipayV2Hybrid.utilities;

import java.io.InputStream;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

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

        try (Connection conn = DriverManager.getConnection(url, user, pass)) {

            // Get stand IDs for the meter number
            String sql = "SELECT DISTINCT s.Id FROM Meters m " +
                         "JOIN Stands s ON m.StandId = s.Id " +
                         "WHERE m.MeterNumber = ?";
            try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                stmt.setString(1, meterNumber);
                ResultSet rs = stmt.executeQuery();
                while (rs.next()) {
                    standIds.add(rs.getInt("Id"));
                }
            }

            if (standIds.isEmpty()) {
                System.out.println("⚠️ No stand IDs found for meter: " + meterNumber);
                return;
            }

            // Now perform cleanup
            runCleanupWithStands(conn, standIds);

        } catch (SQLException e) {
            System.err.println("Error during cleanup: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private static void runCleanupWithStands(Connection conn, List<Integer> standIds) throws SQLException {
        String idsCsv = standIds.toString().replace("[", "").replace("]", "");

        String[] deleteStatements = {
            "DELETE FROM [dbo].[Municipality] WHERE Name = 'Regression Municipality'",
            "DELETE FROM [dbo].[Accounts] WHERE StandId IN (" + idsCsv + ")",
            "DELETE FROM [dbo].[MeterInstallationHistory] WHERE StandId IN (" + idsCsv + ")",
            "DELETE FROM [dbo].[Stands] WHERE Id IN (" + idsCsv + ")",
            "DELETE FROM [dbo].[Meters] WHERE StandId IN (" + idsCsv + ")"
        };

        try (Statement stmt = conn.createStatement()) {
            for (String sql : deleteStatements) {
                int affected = stmt.executeUpdate(sql);
                System.out.println("✅ Executed: [" + sql + "] → Rows affected: " + affected);
            }
        }
    }
}
