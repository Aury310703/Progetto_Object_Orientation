package MODEL;

import java.sql.Date;
import java.sql.Time;

public class ModificaProposta extends Frase {
    int stato = 0;
    Date dataProposta;
    Time oraProposta;
    Date dataValutazione;
    Time oraValutazione;
    public ModificaProposta(Date dP, Time oP, Autore a, Utente u, Frase_Corrente fc, String s){
        super(s);
        this.dataProposta = dP;
        this.oraProposta = oP;
        this.autore = a;
        this.utente = u;
        this.fraseCorrente = fc;
        a.Valutate.add(this);
        u.frasiProposte.add(this);
        fc.proposte.add(this);
    }
    Autore autore;
    Utente utente;
    Frase_Corrente fraseCorrente;
}
