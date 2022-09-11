

public class Controller {
    private GUI gui;
    private Model model;
    private int dim;
    private Thread klokkeTraad;
    Retning rettning = Retning.SOR;

    public Controller () {
        gui = new GUI(this);
    }

    public void startModel(int dim){
        model = new Model(gui, dim, this);
    }

    public void start(){
        klokkeTraad = new Thread(new Klokke());
        klokkeTraad.start();
    }

    //klokketråd:
    class Klokke implements Runnable{
        @Override 
        public void run(){
            while(true){
                try {
                    Thread.sleep(250);
                } catch (Exception e) {
                    return;
                }
                flytt();
            }
        }
    }
    

    private void flytt(){
        model.flytt(rettning);
    }

    public void settRetning(Retning retning){
        this.rettning = retning;
    }

    public void sluttVinner(){
        klokkeTraad.interrupt();
        gui.vinner();
    }

    public void sluttTaper(){
        klokkeTraad.interrupt();
        gui.taper();
    }


    public void avslutt() {
        System.exit(0);
    }

    public Model hentModel(){
        return model;
    }

    
}


