import java.sql.Time;
import java.util.ArrayList;
import java.util.Date;

public class Pagina {
    String titolo;
    Date dataCreazione;
    Time oraCreazione;

    public Pagina(String t, Date dc, Time oc, Autore a){
        this.titolo = t;
        this.dataCreazione = dc;
        this.oraCreazione = oc;
        this.autore = a;
    }
    public Pagina(String t, Date dc, Time oc, Utente u){
        this.titolo = t;
        this.dataCreazione = dc;
        this.oraCreazione = oc;
        this.autore = new Autore(u.nome, u.cognome, u.login, u.password, u.Email, u.DataNascita,this);
    }
    ArrayList <Visiona> visualizzatori = new ArrayList<>();
    Autore autore;
    ArrayList<Frase> Frasi_collegate = new ArrayList<>();
    ArrayList<Frase_Corrente> Frasi = new ArrayList<>();
}
