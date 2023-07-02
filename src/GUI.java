import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class GUI {
    private List<Person> people;
    private DefaultListModel<String> listModel;
    private JList<String> personList;
    private DatabaseManager dbManager;

    public GUI() {
        people = new ArrayList<>();
        listModel = new DefaultListModel<>();
        personList = new JList<>(listModel);
        dbManager = new DatabaseManager();
    }

    public void show() {
        JFrame frame = new JFrame("Temperatures");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(new BorderLayout());

        // Panel z przyciskami i polami tekstowymi
        JPanel inputPanel = new JPanel(new GridLayout(4, 2));
        inputPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JLabel nameLabel = new JLabel("Nazwisko:");
        JTextField nameField = new JTextField();
        JLabel firstNameLabel = new JLabel("Imię:");
        JTextField firstNameField = new JTextField();
        JLabel temperatureLabel = new JLabel("Temperatura (°C):");
        JTextField temperatureField = new JTextField();
        JButton addButton = new JButton("Dodaj");
        JButton sortButton = new JButton("Sortuj");

        inputPanel.add(nameLabel);
        inputPanel.add(nameField);
        inputPanel.add(firstNameLabel);
        inputPanel.add(firstNameField);
        inputPanel.add(temperatureLabel);
        inputPanel.add(temperatureField);
        inputPanel.add(addButton);
        inputPanel.add(sortButton);

        // Panel z listą osób
        JPanel listPanel = new JPanel(new BorderLayout());
        listPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        listPanel.add(new JScrollPane(personList), BorderLayout.CENTER);

        // Dodawanie paneli do głównego okna
        frame.add(inputPanel, BorderLayout.NORTH);
        frame.add(listPanel, BorderLayout.CENTER);

        // Obsługa zdarzenia przycisku "Dodaj"
        addButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String nazwisko = nameField.getText();
                String imie = firstNameField.getText();
                double temperatura = Double.parseDouble(temperatureField.getText());
                Person person = new Person(nazwisko, imie, temperatura, new java.util.Date());
                people.add(person);
                listModel.addElement(person.getNazwisko() + ", " + person.getImie());
                nameField.setText("");
                firstNameField.setText("");
                temperatureField.setText("");
                savePersonToDatabase(person);
            }
        });

        // Obsługa zdarzenia przycisku "Sortuj"
        sortButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                sortPeople();
            }
        });

        // Wyświetlanie okna
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

    private void savePersonToDatabase(Person person) {
        dbManager.connect();
        dbManager.createTable();
        dbManager.savePerson(person);
        dbManager.disconnect();
    }

    private void sortPeople() {
        Collections.sort(people, Comparator.comparing(Person::getNazwisko));
        listModel.clear();
        for (Person person : people) {
            listModel.addElement(person.getNazwisko() + ", " + person.getImie());
        }
    }
}
