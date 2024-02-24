package MODEL;

import javax.xml.crypto.Data;
import java.sql.Time;
import java.util.Date;

/**
 * La Frase è una generalizzazione. Rappresenta una frase all' interno di un testo con annessa la sua posizione
 */
public class Frase {
    /**
     * La Stringa inserita, rappresenta la frase inserita dall'utente .
     */
    protected String stringa_inserita;

    /**
     * Istanzia una nuova frase indicndo la stringa.
     *
     * @param s è la stringa inserita dall'autore della pagina
     */
    public Frase(String s) {
        this.stringa_inserita = s;

    }

    /**
     * La pagina in cui si tova la frase.
     */
    protected Pagina pagina;

    /**
     * Restituisce la stringa inserita dall'autore.
     *
     * @return la stringa inserita
     */
    public String getStringa_inserita() {
        return stringa_inserita;
    }

    /**
     * Setta la stringa inserita dall'autore.
     *
     * @param stringa_inserita è la stringa inserita dall'autore della pagina
     */
    public void setStringa_inserita(String stringa_inserita) {
        this.stringa_inserita = stringa_inserita;
    }

    /**
     * Restituisce la pagina, in cui si trova la frase.
     *
     * @return la pagina
     */
    public Pagina getPagina() {
        return pagina;
    }

    /**
     * Setta la pagina in cui si trova la frase.
     *
     * @param pagina la pagina in cui si trova la frase
     */
    public void setPagina(Pagina pagina) {
        this.pagina = pagina;
    }

}
