import java.util.ArrayList;
import java.util.Date;
public class Utente {
    String nome;
    String cognome;
    String login;
    String password;
    String Email;
    Date DataNascita;
    public Utente(String n, String c, String l, String p, String e, Date d){
        this.nome = n;
        this.cognome = c;
        this.login = l;
        this.password = p;
        this.Email = e;
        this.DataNascita = d;
    }
    ArrayList<Visiona> pagineVisualizzate = new ArrayList<>();
    ArrayList<ModificaProposta> frasiProposte = new ArrayList<>();
}
