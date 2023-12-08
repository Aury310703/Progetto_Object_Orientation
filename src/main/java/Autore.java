import java.util.ArrayList;
import java.util.Date;

public class Autore extends Utente{
    public Autore(String n, String c, String l, String p, String e, Date d, Pagina pg) {
        super(n, c, l, p, e, d);
        this.creazioni.add(pg);
    }
    ArrayList<ModificaProposta> Valutate = new ArrayList<>();
    ArrayList<Pagina> creazioni = new ArrayList<>();
}
