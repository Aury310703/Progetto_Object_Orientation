package MODEL;

import java.sql.Time;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Date;

/**
 * Rappresenta una pagina all'interno della Wiki, composta da un testo.
 */
public class Pagina {
    private String titolo;
    private LocalDateTime dataOraCreazione;
    /**
     * Instanzia una nuova Pagina.
     *
     * @param t       indica il titolo della pagina
     * @param dataOra indica la data e l'ora di creazione della pagina
     * @param a       indica l'autore che ha creato la pagina wiki
     */
    public Pagina(String t, LocalDateTime dataOra, Autore a){
        this.titolo = t;
        this.dataOraCreazione = dataOra;
        this.autore = a;
        a.addCreazioni(this);
    }

    /**
     * Istanzia una nuova pagina passando un utente che diventerà poi un autore
     *
     * @param t       indica il titolo della pagina
     * @param dataOra indica la data e l'ora di creazione della pagina
     * @param u       indica l'utente che ha creato la pagina wiki
     */
    public Pagina(String t, LocalDateTime dataOra, Utente u){
        this.titolo = t;
        this.dataOraCreazione = dataOra;
        this.autore = new Autore(u.getNome(), u.getCognome(), u.getLogin(), u.getPassword(), u.getEmail(), u.getDataNascita(),this);
        this.autore.setFrasiProposte(u.getFrasiProposte());
        this.autore.setPagineVisualizzate(u.getPagineVisualizzate());
    }

    /**
     * Istanzia una nuova pagina e il suo autore con tutti i relativi parametri.
     *
     * @param titolo      indica il titolo della pagina
     * @param dataOra     indica la data e l'ora di creazione della pagina
     * @param nome        indica il nome dell'autore della pagina
     * @param cognome     indica il cognome dell'autore della pagina
     * @param login       indica il login di accesso dell'autore della pagina
     * @param password    indica la password di accesso dell'autore della pagina
     * @param email       indica l'indirizzo e-mail dell'autore della pagina
     * @param dataNascita indica la data di nascita dell'autore della pagina
     */
    public Pagina(String titolo, LocalDateTime dataOra, String nome, String cognome, String login, String password, String email, Date dataNascita){
        this.titolo = titolo;
        this.dataOraCreazione = dataOra;
        this.autore = new Autore(nome, cognome, login, password, email, dataNascita,this);
    }
    private ArrayList <Visiona> visualizzatori = new ArrayList<>();
    private Autore autore;
    private ArrayList<Frase_Corrente> frasi_collegate = new ArrayList<>();
    private ArrayList<Frase_Corrente> frasi = new ArrayList<>();
    private ArrayList<Notifica> notificheInviate = new ArrayList<>();

    /**
     * restituisce il titolo della pagina creata.
     *
     * @return titolo.
     */
    public String getTitolo() {
        return titolo;
    }

    /**
     * Setta il titolo della pagina creata.
     *
     * @param titolo indica il titolo della pagina creata,
     */
    public void setTitolo(String titolo) {
        this.titolo = titolo;
    }

    /**
     * Restituisce la data di creazione della pagina.
     *
     * @return creazione.
     */
    public LocalDateTime getDataCreazione() {
        return dataOraCreazione;
    }

    /**
     * Setta la data di creazione della pagina.
     *
     * @param dataCreazione indica la data di creazione della pagina.
     */
    public void setDataCreazione(LocalDateTime dataCreazione) {
        this.dataOraCreazione = dataCreazione;
    }

    /**
     * Restituisce un ArrayList che rappresenta tutte le visualizzazioni fatte alla pagina.
     *
     * @return visualizzatori
     */
    public ArrayList<Visiona> getVisualizzatori() {
        return visualizzatori;
    }

    /**
     * Setta le visualizzazioni alla pagina nell'ArrayList visualizzatori.
     *
     * @param visualizzatori ArrayList che indica tutte le visualizzazioni alla pagina
     */
    public void setVisualizzatori(ArrayList<Visiona> visualizzatori) {
        this.visualizzatori = visualizzatori;
    }

    /**
     * Aggiunge ogni singolo viusalizzatore all'ArrayList
     *
     * @param v indica il singolo visualizzatore alla pagina che verrà aggiunto all'ArrayList
     */
    public void addViaualizzatori(Visiona v) {
        visualizzatori.add(v);
    }

    /**
     * Rimuove una visualizzazione dall'ArrayList visualizzatori.
     *
     * @param v indica il visualizzatore da rimuovere dall'ArrayList
     */
    public void removeVisualizzatori(Visiona v){
        visualizzatori.remove(v);
    }

    /**
     * Restituisce l'autore della pagina creata.
     *
     * @return autore
     */
    public Autore getAutore() {
        return autore;
    }

    /**
     * Setta l'autore della pagina creata.
     *
     * @param autore indica l'autore della pagina creata.
     */
    public void setAutore(Autore autore) {
        this.autore = autore;
    }

    /**
     * Restituisce le frasi collegate alla pagina.
     *
     * @return frasi_collegate
     */
    public ArrayList<Frase_Corrente> getFrasi_collegate() {
        return frasi_collegate;
    }

    /**
     * Setta le frasi collegate alla pagina.
     *
     * @param frasi_collegate indica le frasi collegate alla pagina
     */
    public void setFrasi_collegate(ArrayList<Frase_Corrente> frasi_collegate) {
        frasi_collegate = frasi_collegate;
    }

    /**
     * Aggiunge all'ArrayList le frasi collegate alla pagina.
     *
     * @param f indica la frase collegata alla pagina da aggiungere all'ArrayList
     */
    public void addFrasi_collegate(Frase_Corrente f){
        frasi_collegate.add(f);
    }

    /**
     * Rimuove dall'ArrayList le frasi collegate alla pagina.
     *
     * @param f indica la frase collegata alla pagina da rimuovere dall'ArrayList
     */
    public void removeFrasi_collegate(Frase_Corrente f){
        frasi_collegate.remove(f);
    }

    /**
     * Restituisce le frasi correnti della pagina.
     *
     * @return frasi
     */
    public ArrayList<Frase_Corrente> getFrasi() {
        return frasi;
    }

    /**
     * Setta le frasi correnti della pagina.
     *
     * @param frasiCorrenti indica le frasiCrorenti della pagina creata
     */
    public void setFrasi(ArrayList<Frase_Corrente> frasiCorrenti) {
        this.frasi = frasiCorrenti;
    }

    /**
     * Aggiunge all'ArrayList frasi le frasi correnti.
     *
     * @param f indica la frase corrente da aggiungere all'ArrayList
     */
    public void addFrasi(Frase_Corrente f){
    frasi.add(f);
    }

    /**
     * Rimuove dall'ArrayList le frasi Correnti.
     *
     * @param f indica la frase corrente da rimnovere dall'ArrayList
     */
    public void removeFrasi(Frase_Corrente f){
        frasi.remove(f);
    }

    /**
     * Restituisce le notifiche inviate all'Autore della pagina.
     *
     * @return notifivheInviate
     */
    public ArrayList<Notifica> getNotificheInviate() {
        return notificheInviate;
    }

    /**
     * Setta le notifiche inviate all'Autore della pagina.
     *
     * @param notificheInviate notifivheInviate
     */
    public void setNotificheInviate(ArrayList<Notifica> notificheInviate) {
        this.notificheInviate = notificheInviate;
    }

    /**
     * Aggiunge all'ArrayList notifiche inviate le notifiche dell'autore della pagina.
     *
     * @param n indica la notifica da aggiungere all'ArrayList.
     */
    public void addNotificheInviate(Notifica n){
        notificheInviate.add(n);
    }

    /**
     * Rimuove le notifiche inviate all'autore dall'ArrayList.
     *
     * @param n indica la notifica da rimuovere dall'ArrayList.
     */
    public void removeNotificheInviate(Notifica n){
        notificheInviate.remove(n);
    }

}
