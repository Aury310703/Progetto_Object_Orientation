package MODEL;

import java.sql.Date;
import java.sql.Time;
import java.time.LocalDate;
import java.time.LocalTime;

public class Visiona {
    private LocalDate dataVisione;
    private LocalTime oraVisione;
    private Pagina pagina;
    private Utente utente;

    public Visiona(LocalDate dataVisione, LocalTime oraVisione, Pagina pagina, Utente utente){
        this.utente = utente;
        this.pagina = pagina;
        this.dataVisione = dataVisione;
        this.oraVisione = oraVisione;
        utente.addPagineVisualizzate(this);
        pagina.addViaualizzatori(this);
    }

    public LocalDate getDataVisione() {
        return dataVisione;
    }
    public void setDataVisione(LocalDate dataVisione) {
        this.dataVisione = dataVisione;
    }
    public LocalTime getOraVisione() {
        return oraVisione;
    }
    public void setOraVisione(LocalTime oraVisione) {
        this.oraVisione = oraVisione;
    }
    public void setUtente(Utente utente) {
        this.utente = utente;
    }
    public Utente getUtente() {
        return utente;
    }
    public void setPagina(Pagina pagina) {
        this.pagina = pagina;
    }
    public Pagina getPagina() {
        return pagina;
    }
}
