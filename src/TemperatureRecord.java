import java.util.ArrayList;
import java.util.List;

public class TemperatureRecord {
    private List<Person> people;

    public TemperatureRecord() {
        this.people = new ArrayList<>();
    }

    public void addPerson(Person person) {
        people.add(person);
    }

    public void calculateAverageTemperatures() {
        for (Person person : people) {
            // Obliczanie średnich temperatur dla każdej osoby
        }
    }

    public void sortByName() {
        // Sortowanie po nazwisku
    }

    public void sortByDate() {
        // Sortowanie po dacie
    }

    public void sortByTemperature() {
        // Sortowanie po temperaturze
    }
}
