import java.util.Date;
import java.sql.Time;


public class Person {
    private String nazwisko;
    private String imie;
    private double temperatura;
    private Date data;
    private Time godzina;

    // Konstruktor
    public Person(String nazwisko, String imie, double temperatura, Date data, Time godzina) {
        this.nazwisko = nazwisko;
        this.imie = imie;
        this.temperatura = temperatura;
        this.data = data;
        this.godzina = godzina;
    }

    // Metody dostępowe (getter) i ustawiające (setter) dla pól

    public String getNazwisko() {
        return nazwisko;
    }

    public void setNazwisko(String nazwisko) {
        this.nazwisko = nazwisko;
    }

    public String getImie() {
        return imie;
    }

    public void setImie(String imie) {
        this.imie = imie;
    }

    public double getTemperatura() {
        return temperatura;
    }

    public void setTemperatura(double temperatura) {
        this.temperatura = temperatura;
    }

    public Date getData() {
        return data;
    }

    public void setData(Date data) {
        this.data = data;
    }

    public Time getGodzina() {
        return godzina;
    }

    public void setGodzina(Time godzina) {
        this.godzina = godzina;
    }
}
