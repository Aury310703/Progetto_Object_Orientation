package MODEL;

import java.util.ArrayList;
import java.util.Date;

/**
 * Rappresenta l'Utente generico che accedendo con le sue credenziali, può viusualizzare le varie pagine wiki, può proporre notifiche a esse e può viusalizzare la cronologia delle sue viusalizzazioni.
 * Nel momento in cui l'utente creerà la sua prima pagina il suo ruolo passerà a quello di autore.
 */
public class Utente {
    /**
     *Nome dell'utente.
     */
    protected String nome;
    /**
     *Cognome dell'utente.
     */
    protected String cognome;
    /**
     *Login di accesso dell'utente.
     */
    protected String login;
    /**
     *Password di accesso dell'utente.
     */
    protected String password;
    /**
     *Indirizzo email dell'utente.
     */
    protected String Email;
    /**
     *Data di nascita dell'utente.
     */
    protected Date DataNascita;

    /**
     * Istanzia un nuovo Utente.
     *
     * @param nome indica il nome dell'utente
     * @param cognome indica il cognome dell'utente
     * @param login indica il login di accesso dell'utente
     * @param password indica la password di accesso dell'utente
     * @param email indica l'indirizo email dell'utente
     * @param dataNascita indica la data do nascita dell'utente
     */
    public Utente(String nome, String cognome, String login, String password, String email, Date dataNascita){
        this.nome = nome;
        this.cognome = cognome;
        this.login = login;
        this.password = password;
        this.Email = email;
        this.DataNascita = dataNascita;
    }

    /**
     * ArrayList che rappresenta le pagine visualizzate dall'utente
     */
    protected ArrayList<Visiona> pagineVisualizzate = new ArrayList<>();
    /**
     * ArrayList che rappresenta le modifiche proposte dell'utente alle pagine.
     */
    protected ArrayList<ModificaProposta> frasiProposte = new ArrayList<>();
    /**
     * Restituisce il nome dell'utente.
     *
     * @return nome
     */
    public String getNome() {
        return nome;
    }

    /**
     * Restituisce il cognome dell'utente.
     *
     * @return cognome
     */
    public String getCognome() {
        return cognome;
    }

    /**
     * Restituisce la data di nascita dell'utente.
     *
     * @return dataNascita
     */
    public Date getDataNascita() {
        return DataNascita;
    }

    /**
     * Restituisce l'indirizzo email dell'utente.
     *
     * @return email
     */
    public String getEmail() {
        return Email;
    }

    /**
     * Restituisce il login di accesso dell'utente.
     *
     * @return login
     */
    public String getLogin() {
        return login;
    }

    /**
     * Restituisce la password di accesso dell'utente.
     *
     * @return password
     */
    public String getPassword() {
        return password;
    }

    /**
     * Setta il cognome dell'utente.
     *
     * @param cognome indicia il cognome dell'utente
     */
    public void setCognome(String cognome) {
        this.cognome = cognome;
    }

    /**
     * Setta il login di accesso dell'utente.
     *
     * @param login indica il login di accesso dell'utente
     */
    public void setLogin(String login) {
        this.login = login;
    }

    /**
     * Setta la data di nascita dell'utente.
     *
     * @param dataNascita indica la data di nascita dell'utente
     */
    public void setDataNascita(Date dataNascita) {
        DataNascita = dataNascita;
    }

    /**
     * Setta l'indirizzo email dell'utente.
     *
     * @param email indica l'indirizzo email dell'utente
     */
    public void setEmail(String email) {
        Email = email;
    }

    /**
     * Setta il nome dell'utente.
     *
     * @param nome indica il nome dell'utente
     */
    public void setNome(String nome) {
        this.nome = nome;
    }

    /**
     * Setta la password di accesso dell'utente.
     *
     * @param password indica la password di accesso dell'utente.
     */
    public void setPassword(String password) {
        this.password = password;
    }

    /**
     * Restituisce un ArrayList contenente modifiche proposte dall'utente.
     *
     * @return frasiProposte.
     */
    public ArrayList<ModificaProposta> getFrasiProposte() {
        return frasiProposte;
    }

    /**
     * Setta l'ArrayList di modifiche proposte con quelle fatte dall'utente.
     *
     * @param frasiProposte indica le modifiche proposte fatte dall'utente.
     */
    public void setFrasiProposte(ArrayList<ModificaProposta> frasiProposte) {
        this.frasiProposte = frasiProposte;
    }

    /**
     * Aggiunge all'ArrayList le modifiche fatte dall'utente.
     *
     * @param m indicia la modifica proposta dall'utente da aggiungere all'ArrayList.
     */
    public void addFrasiProposte(ModificaProposta m){
        frasiProposte.add(m);
    }

    /**
     * Rimuove dall'ArrayList le modifiche proposte dall'utente.
     *
     * @param m indica la modifica proposta da rimuovere dall'ArrayList.
     */
    public void removeFrasiProposte(ModificaProposta m){
        frasiProposte.remove(m);
    }
    /**
     * Restituisce la cronologia di pagine visualizzate dall'utente.
     *
     * @return  pagineVisualizzate
     */
    public ArrayList<Visiona> getPagineVisualizzate() {
        return pagineVisualizzate;
    }

    /**
     * Setta la cronologia di pagine visualizzate dall'utente.
     *
     * @param pagineVisualizzate indica le visualizzazioni effettuate dall'utente
     */
    public void setPagineVisualizzate(ArrayList<Visiona> pagineVisualizzate) {
        this.pagineVisualizzate = pagineVisualizzate;
    }

    /**
     * Aggiunge all'ArrayList pagineViusalizzate la pagine viusalizzate dall'utente.
     *
     * @param v indica la visualizzazione effettuata dall'utente da aggiungere all'ArrayList.
     */
    public void addPagineVisualizzate(Visiona v){
        pagineVisualizzate.add(v);
    }

    /**
     * Rimuove dall'ArrayList le viusalizzazione effettuate dall'utente
     *
     * @param v indica la visualizzazione effettuata dall'utente da rimuovere dall'ArrayList.
     */
    public void removePagineVisualizzate(Visiona v){
        pagineVisualizzate.remove(v);
    }
}
