import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.sql.Time;

public class Main {
    private JFrame frame;
    private JTextField textFieldNazwisko;
    private JTextField textFieldImie;
    private JTextField textFieldTemperatura;
    private JButton buttonDodaj;
    private JButton buttonSortujNazwisko;
    private JButton buttonSortujDate;
    private JButton buttonSortujTemperatura;
    private JTextArea textAreaDane;

    private ArrayList<Person> peopleList;
    private Connection connection;

    public Main() {
        initialize();
        connectToDatabase();
    }

    private void initialize() {
        frame = new JFrame();
        frame.setBounds(100, 100, 450, 300);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.getContentPane().setLayout(null);

        JLabel labelNazwisko = new JLabel("Nazwisko:");
        labelNazwisko.setBounds(10, 10, 80, 20);
        frame.getContentPane().add(labelNazwisko);

        textFieldNazwisko = new JTextField();
        textFieldNazwisko.setBounds(100, 10, 150, 20);
        frame.getContentPane().add(textFieldNazwisko);
        textFieldNazwisko.setColumns(10);

        JLabel labelImie = new JLabel("Imię:");
        labelImie.setBounds(10, 40, 80, 20);
        frame.getContentPane().add(labelImie);

        textFieldImie = new JTextField();
        textFieldImie.setBounds(100, 40, 150, 20);
        frame.getContentPane().add(textFieldImie);
        textFieldImie.setColumns(10);

        JLabel labelTemperatura = new JLabel("Temperatura (C):");
        labelTemperatura.setBounds(10, 70, 120, 20);
        frame.getContentPane().add(labelTemperatura);

        textFieldTemperatura = new JTextField();
        textFieldTemperatura.setBounds(130, 70, 70, 20);
        frame.getContentPane().add(textFieldTemperatura);
        textFieldTemperatura.setColumns(10);

        buttonDodaj = new JButton("Dodaj");
        buttonDodaj.setBounds(10, 100, 80, 20);
        frame.getContentPane().add(buttonDodaj);

        buttonSortujNazwisko = new JButton("Sortuj po nazwisku");
        buttonSortujNazwisko.setBounds(10, 130, 150, 20);
        frame.getContentPane().add(buttonSortujNazwisko);

        buttonSortujDate = new JButton("Sortuj po dacie");
        buttonSortujDate.setBounds(170, 130, 120, 20);
        frame.getContentPane().add(buttonSortujDate);

        buttonSortujTemperatura = new JButton("Sortuj po temperaturze");
        buttonSortujTemperatura.setBounds(300, 130, 150, 20);
        frame.getContentPane().add(buttonSortujTemperatura);

        textAreaDane = new JTextArea();
        JScrollPane scrollPane = new JScrollPane(textAreaDane);
        scrollPane.setBounds(10, 160, 430, 90);
        frame.getContentPane().add(scrollPane);

        buttonDodaj.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                dodajDane();
            }
        });

        buttonSortujNazwisko.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                sortujPoNazwisku();
            }
        });

        buttonSortujDate.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                sortujPoDacie();
            }
        });

        buttonSortujTemperatura.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                sortujPoTemperaturze();
            }
        });
    }

    private void connectToDatabase() {
        try {
            connection = DriverManager.getConnection("jdbc:sqlite:database.db");
            System.out.println("Połączono z bazą danych SQLite.");
        } catch (SQLException e) {
            System.out.println("Błąd podczas łączenia z bazą danych: " + e.getMessage());
        }
    }

    private void disconnectFromDatabase() {
        try {
            if (connection != null) {
                connection.close();
                System.out.println("Rozłączono z bazą danych SQLite.");
            }
        } catch (SQLException e) {
            System.out.println("Błąd podczas zamykania połączenia z bazą danych: " + e.getMessage());
        }
    }

    private void dodajDane() {
        String nazwisko = textFieldNazwisko.getText();
        String imie = textFieldImie.getText();
        double temperatura = Double.parseDouble(textFieldTemperatura.getText());
        Date data = new Date();

        Person person = new Person(nazwisko, imie, temperatura, data);
        savePersonToDatabase(person);

        textFieldNazwisko.setText("");
        textFieldImie.setText("");
        textFieldTemperatura.setText("");

        updateDataList();
    }

    private void savePersonToDatabase(Person person) {
        try {
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

    private void updateDataList() {
        try {
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT * FROM people");

            peopleList = new ArrayList<>();

            while (resultSet.next()) {
                String nazwisko = resultSet.getString("nazwisko");
                String imie = resultSet.getString("imie");
                double temperatura = resultSet.getDouble("temperatura");
                Date data = resultSet.getDate("data");

                Person person = new Person(nazwisko, imie, temperatura, data);
                peopleList.add(person);
            }

            displayDataList();
        } catch (SQLException e) {
            System.out.println("Błąd podczas pobierania danych z bazy danych: " + e.getMessage());
        }
    }

    private void displayDataList() {
        textAreaDane.setText("");

        for (Person person : peopleList) {
            textAreaDane.append(person.toString() + "\n");
        }
    }

    private void sortujPoNazwisku() {
        Collections.sort(peopleList, new Comparator<Person>() {
            public int compare(Person p1, Person p2) {
                return p1.getNazwisko().compareToIgnoreCase(p2.getNazwisko());
            }
        });

        displayDataList();
    }

    private void sortujPoDacie() {
        Collections.sort(peopleList, new Comparator<Person>() {
            public int compare(Person p1, Person p2) {
                return p1.getData().compareTo(p2.getData());
            }
        });

        displayDataList();
    }

    private void sortujPoTemperaturze() {
        Collections.sort(peopleList, new Comparator<Person>() {
            public int compare(Person p1, Person p2) {
                return Double.compare(p1.getTemperatura(), p2.getTemperatura());
            }
        });

        displayDataList();
    }

    public static void main(String[] args) {
        EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                    Main window = new Main();
                    window.frame.setVisible(true);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }
}
