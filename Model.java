
public class Model {
    private GUI gui;
    private int dim, posLengde, posBredde;
    private Controller c;
    public String[][] brett;
    //private Premier[] premier = new Premier[10];
    //private LinkedList<String> slange = new LinkedList<>();
    public String slangeHode = "h"; //"😎";
    public String slangeKropp = "k"; //🟡;
    public String premie = "p"; //"🍎";
    private int antPremierTatt = 0, antPremierPaaBrettet = 10;


    public Model(GUI gui, int dimensjon, Controller c){
        this.gui = gui;
        dim = dimensjon;
        this.c = c;
        brett = new String[dim][dim];
        for (int i = 0; i<dim; i++){
            for (int j = 0; j<dim; j++){
                brett[i][j] = "";
            }
        }

        plasserPremier();
        plasserSlange();
    }

    public void flytt(Retning retning){
        int midPosLengde = posLengde;
        int midPosBredde = posBredde;
        if (Retning.NORD == retning) midPosLengde--;
        if (Retning.SOR == retning) midPosLengde++;
        if (Retning.VEST == retning) midPosBredde--;
        if (Retning.OST == retning) midPosBredde++;

        if (erVegg(midPosLengde, midPosBredde) || erSlange(midPosLengde, midPosBredde)){ //TODO: erSlange
            c.sluttTaper();
        }
        else if(antPremierPaaBrettet <= 0) c.sluttVinner();
        else besokRute(midPosLengde, midPosBredde, retning);
    }

    public void plasserSlange(){
        posLengde = dim/2;
        posBredde = dim/2;
        tegn(slangeHode, posLengde, posBredde);
        //slange.add("s");
    }

    public void plasserPremier(){
        int rad, kol;
        for (int i = 0; i<antPremierPaaBrettet;){
            rad = trekk(0,dim-1);
            kol = trekk(0,dim-1);
            if (brett[rad][kol] != premie){
                tegn(premie, rad, kol);
                i++;
            }
        }
    }

    static int trekk (int a, int b) {
        // Trekk et tilfeldig heltall i intervallet [a..b];
        return (int)(Math.random()*(b-a+1))+a;
    }

    public boolean erVegg(int lengde, int bredde){
        if (lengde < 0 || lengde >= dim){
            return true;
        }
        else if (bredde < 0 || bredde >= dim){
            return true;
        }
        else return false;
    }

    public boolean erSlange(int lengde, int bredde){
        return brett[lengde][bredde].equals("s"); 
        //TODO: sjekk denne
    }

    private void besokRute(int nyPosLengde, int nyPosBredde, Retning retning){
        String tmp = brett[nyPosLengde][nyPosBredde];

        tegn(slangeHode, nyPosLengde, nyPosBredde);
        fjern(posLengde, posBredde, slangeHode);
        tegn(slangeKropp, posLengde, posBredde);

        if (tmp.equals(premie)){
            antPremierTatt++; //TODO: tegn i gui??
            gui.leggTilPoeng(antPremierTatt);
            antPremierPaaBrettet--;
        }

        else {
            //må fjerne bakerste slange-rute
            int[][] sjekket = new int[dim][dim]; //liste for å markere sjekkede ruter, denne er kun her fordi det ellers ga feilmelding
            finnOgFjernSisteSlangeRute(posLengde, posBredde, retning, sjekket); //TODO: Sjekk denne!
        }
        
        posLengde = nyPosLengde;
        posBredde = nyPosBredde;
    }

    private void tegn(String symbol, int posLengde, int posBredde){
        if(symbol.equals(premie)) {
            brett[posLengde][posBredde] = premie; 
            gui.tegnPremie(posLengde, posBredde);
        }   //TODO sjekk denne

        if(symbol.equals(slangeHode)) {
            brett[posLengde][posBredde] = "s"; 
            gui.tegnHode(posLengde, posBredde);
        }

        if(symbol.equals(slangeKropp)){
            brett[posLengde][posBredde] = "s"; 
            gui.tegnKropp(posLengde, posBredde);
        }
    }

    private void fjern(int posLengde, int posBredde, String symbol){
        if(symbol.equals(premie)) {
            gui.fjernPremie(posLengde, posBredde);
        }   //TODO sjekk denne

        if(symbol.equals(slangeHode)) {
            gui.fjernHode(posLengde, posBredde);
        }

        if(symbol.equals(slangeKropp)){
            gui.fjernKropp(posLengde, posBredde);
        }

        brett[posLengde][posBredde] = " ";
    }



    private void finnOgFjernSisteSlangeRute(int lengde, int bredde, Retning retningFra, int[][] sjekket){
        sjekket[lengde][bredde] = 1; 


        if (lengde-1 >= 0
            && sjekket[lengde-1][bredde] != 1
            && brett[lengde-1][bredde].equals("s") 
            && retningFra != Retning.NORD){
                finnOgFjernSisteSlangeRute(lengde-1, bredde, Retning.SOR, sjekket);
                return;
        }

        if (lengde+1 < dim
            && sjekket[lengde+1][bredde] != 1
            && brett[lengde+1][bredde].equals("s") 
            && retningFra != Retning.SOR){ 
                finnOgFjernSisteSlangeRute(lengde+1, bredde, Retning.NORD, sjekket);
                return;
        }

        if (bredde+1 < dim
            && sjekket[lengde][bredde+1] != 1
            && brett[lengde][bredde+1].equals("s") 
            && retningFra != Retning.OST){
                finnOgFjernSisteSlangeRute(lengde, bredde+1, Retning.VEST, sjekket);
                return;
        }

        if (bredde-1 >= 0
            && sjekket[lengde][bredde-1] != 1
            && brett[lengde][bredde-1].equals("s") 
            && retningFra != Retning.VEST){
                finnOgFjernSisteSlangeRute(lengde, bredde-1, Retning.OST, sjekket);
                return;
        }

        fjern(lengde, bredde, slangeKropp);
        sjekket = new int[dim][dim];
    }
}



