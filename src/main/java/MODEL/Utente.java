package MODEL;

import java.util.ArrayList;
import java.util.Date;
public class Utente {
    protected String nome;
    protected String cognome;
    protected String login;
    protected String password;
    protected String Email;
    protected Date DataNascita;
    public Utente(String n, String c, String l, String p, String e, Date d){
        this.nome = n;
        this.cognome = c;
        this.login = l;
        this.password = p;
        this.Email = e;
        this.DataNascita = d;
    }
    protected ArrayList<Visiona> pagineVisualizzate = new ArrayList<>();
    protected ArrayList<ModificaProposta> frasiProposte = new ArrayList<>();
    protected ArrayList<Notifica> notificheRicevute = new ArrayList<>();

    public String getNome() {
        return nome;
    }

    public String getCognome() {
        return cognome;
    }

    public Date getDataNascita() {
        return DataNascita;
    }

    public String getEmail() {
        return Email;
    }

    public String getLogin() {
        return login;
    }

    public String getPassword() {
        return password;
    }

    public void setCognome(String cognome) {
        this.cognome = cognome;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public void setDataNascita(Date dataNascita) {
        DataNascita = dataNascita;
    }

    public void setEmail(String email) {
        Email = email;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public ArrayList<ModificaProposta> getFrasiProposte() {
        return frasiProposte;
    }

    public void setFrasiProposte(ArrayList<ModificaProposta> frasiProposte) {
        this.frasiProposte = frasiProposte;
    }

    public void addFrasiProposte(ModificaProposta m){
        frasiProposte.add(m);
    }

    public void removeFrasiProposte(ModificaProposta m){
        frasiProposte.remove(m);
    }

    public ArrayList<Notifica> getNotificheRicevute() {
        return notificheRicevute;
    }

    public void setNotificheRicevute(ArrayList<Notifica> notificheRicevute) {
        this.notificheRicevute = notificheRicevute;
    }

    public void addNotificheRicevute(Notifica n){
        notificheRicevute.add(n);
    }

    public void removeNotificheRicevute(Notifica n){
        notificheRicevute.remove(n);
    }

    public ArrayList<Visiona> getPagineVisualizzate() {
        return pagineVisualizzate;
    }

    public void setPagineVisualizzate(ArrayList<Visiona> pagineVisualizzate) {
        this.pagineVisualizzate = pagineVisualizzate;
    }

    public void addPagineVisualizzate(Visiona v){
        pagineVisualizzate.add(v);
    }

    public void removePagineVisualizzate(Visiona v){
        pagineVisualizzate.remove(v);
    }
}
