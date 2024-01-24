package MODEL;

import java.sql.Date;
import java.sql.Time;

public class Visiona {
    private Date dataVisione;
    private Time oraVisione;
    private Pagina pagina;
    private Utente utente;

    public Visiona(Date dataVisione, Time oraVisione, Pagina pagina, Utente utente){
        this.utente = utente;
        this.pagina = pagina;
        this.dataVisione = dataVisione;
        this.oraVisione = oraVisione;
        utente.addPagineVisualizzate(this);
        pagina.addViaualizzatori(this);
    }

    public Date getDataVisione() {
        return dataVisione;
    }
    public void setDataVisione(Date dataVisione) {
        this.dataVisione = dataVisione;
    }
    public Time getOraVisione() {
        return oraVisione;
    }
    public void setOraVisione(Time oraVisione) {
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
