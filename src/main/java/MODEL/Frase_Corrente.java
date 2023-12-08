package MODEL;

import java.util.ArrayList;

public class Frase_Corrente extends Frase {
    int Numerazione;

    public Frase_Corrente(String s, int n, Pagina p) {
        super(s);
        this.Numerazione = n;
        this.pagina = p;
        p.Frasi.add(this);
    }
    ArrayList<ModificaProposta> proposte = new ArrayList<>();
    Pagina pagina;
}
