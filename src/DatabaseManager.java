import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class DatabaseManager {
    private Connection connection;

    public void connect() {
        try {
            // Tworze połączenia z bazą danych
            connection = DriverManager.getConnection("jdbc:sqlite:database.db");
            System.out.println("Połączono z bazą danych SQLite.");
        } catch (SQLException e) {
            System.out.println("Błąd podczas łączenia z bazą danych: " + e.getMessage());
        }
    }

    public void disconnect() {
        try {
            // Zamykam połączenia z bazą danych
            if (connection != null) {
                connection.close();
                System.out.println("Rozłączono z bazą danych SQLite.");
            }
        } catch (SQLException e) {
            System.out.println("Błąd podczas zamykania połączenia z bazą danych: " + e.getMessage());
        }
    }

    public void createTable() {
        try {
            Statement statement = connection.createStatement();
            String query = "CREATE TABLE IF NOT EXISTS people (id INTEGER PRIMARY KEY AUTOINCREMENT, nazwisko TEXT, imie TEXT, temperatura REAL, data DATE)";
            statement.execute(query);
            System.out.println("Tabela people utworzona lub już istnieje.");
        } catch (SQLException e) {
            System.out.println("Błąd podczas tworzenia tabeli: " + e.getMessage());
        }
    }

    public void savePerson(Person person) {
        try {
            // Tworze i wykonuje zapytania do zapisu danych osobowych
            String query = "INSERT INTO people (nazwisko, imie, temperatura, data) VALUES (?, ?, ?, ?)";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setString(1, person.getNazwisko());
            statement.setString(2, person.getImie());
            statement.setDouble(3, person.getTemperatura());
            statement.setDate(4, new java.sql.Date(person.getData().getTime()));
            statement.executeUpdate();
            System.out.println("Dane osobowe zapisane w bazie danych.");
        } catch (SQLException e) {
            System.out.println("Błąd podczas zapisu danych osobowych: " + e.getMessage());
        }
    }

}
