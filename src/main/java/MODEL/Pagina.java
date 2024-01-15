package MODEL;

import java.sql.Time;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;

public class Pagina {
    private String titolo;
    private LocalDateTime dataOraCreazione;

    public Pagina(String t, LocalDateTime dataOra, Autore a){
        this.titolo = t;
        this.dataOraCreazione = dataOra;
        this.autore = a;
    }
    public Pagina(String t, LocalDateTime dataOra, Utente u){
        this.titolo = t;
        this.dataOraCreazione = dataOra;
        this.autore = new Autore(u.getNome(), u.getCognome(), u.getLogin(), u.getPassword(), u.getEmail(), u.getDataNascita(),this);
    }
    public Pagina(String titolo, LocalDateTime dataOra, String nome, String cognome, String login, String password, String email, Date dataNascita){
        this.titolo = titolo;
        this.dataOraCreazione = dataOra;
        this.autore = new Autore(nome, cognome, login, password, email, dataNascita,this);
    }
    private ArrayList <Visiona> visualizzatori = new ArrayList<>();
    private Autore autore;
    private ArrayList<Frase> frasi_collegate = new ArrayList<>();
    private ArrayList<Frase_Corrente> frasi = new ArrayList<>();
    private ArrayList<Notifica> notificheInviate = new ArrayList<>();
    public String getTitolo() {
        return titolo;
    }
    public void setTitolo(String titolo) {
        this.titolo = titolo;
    }
    public LocalDateTime getDataCreazione() {
        return dataOraCreazione;
    }
    public void setDataCreazione(LocalDateTime dataCreazione) {
        this.dataOraCreazione = dataCreazione;
    }
    public ArrayList<Visiona> getVisualizzatori() {
        return visualizzatori;
    }
    public void setVisualizzatori(ArrayList<Visiona> visualizzatori) {
        this.visualizzatori = visualizzatori;
    }

    public void addViaualizzatori(Visiona v) {
        visualizzatori.add(v);
    }
    public void removeVisualizzatori(Visiona v){
        visualizzatori.remove(v);
    }
    public Autore getAutore() {
        return autore;
    }
    public void setAutore(Autore autore) {
        this.autore = autore;
    }

    public ArrayList<Frase> getFrasi_collegate() {
        return frasi_collegate;
    }

    public void setFrasi_collegate(ArrayList<Frase> frasi_collegate) {
        frasi_collegate = frasi_collegate;
    }

    public void addFrasi_collegate(Frase f){
        frasi_collegate.add(f);
    }

    public void removeFrasi_collegate(Frase f){
        frasi_collegate.remove(f);
    }

    public ArrayList<Frase_Corrente> getFrasi() {
        return frasi;
    }

    public void setFrasi(ArrayList<Frase_Corrente> frasi) {
        frasi = frasi;
    }

    public void addFrasi(Frase_Corrente f){
    frasi.add(f);
    }

    public void removeFrasi(Frase_Corrente f){
        frasi.remove(f);
    }

    public ArrayList<Notifica> getNotificheInviate() {
        return notificheInviate;
    }

    public void setNotificheInviate(ArrayList<Notifica> notificheInviate) {
        this.notificheInviate = notificheInviate;
    }

    public void addNotificheInviate(Notifica n){
        notificheInviate.add(n);
    }

    public void removeNotificheInviate(Notifica n){
        notificheInviate.remove(n);
    }
}
