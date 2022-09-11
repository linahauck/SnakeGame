import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.nio.file.Path;
import java.nio.file.Paths;

public class GUI {
    private JFrame vindu;
    private JPanel panel, tittelPanel, startPanel, spillPanel;
    public JLabel[][] ruter;
    private JLabel tittel, velgLevel, poeng;
    private Controller c;
    private Model m;
    private int dim;
    private Path pathPremie = Paths.get("eple.png"); 
    private Path pathHode = Paths.get("hode2.png"); 
    private Path pathKropp = Paths.get("kropp.png"); 
    private ImageIcon  hode = new ImageIcon(pathHode.toString());
    private ImageIcon kropp = new ImageIcon(pathKropp.toString());
    private ImageIcon premie = new ImageIcon(pathPremie.toString());

    public GUI(Controller c){
        this.c = c;

        try {
            UIManager.setLookAndFeel(
                UIManager.getCrossPlatformLookAndFeelClassName()
            );
        } catch (Exception e) {
            System.exit(1);
        }

        velkomstPanel();

    }

    public void velkomstPanel(){
        vindu = new JFrame("Slangespillet");
        vindu.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        panel = new JPanel();
        panel.setLayout(new BorderLayout());
        panel.setBackground(new Color(204,255,229));

    //tittelPanel med spillets tittel
        tittelPanel = new JPanel();
        tittelPanel.setLayout(new BorderLayout());
        tittelPanel.setBackground(new Color(153,255,204));
        
        tittel = new JLabel("The Snake Game!");
        tittel.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 40));
        tittel.setForeground(Color.BLACK);
        tittelPanel.add(tittel, BorderLayout.LINE_START);
    
    //----start panel------
        startPanel = new JPanel();
        startPanel.setLayout(new BorderLayout());
        startPanel.setBackground(new Color(204,255,229));

        velgLevel = new JLabel("Velg et level for å starte spillet:", SwingConstants.CENTER);
        velgLevel.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 15));
        velgLevel.setForeground(Color.BLACK);
        startPanel.add(velgLevel, BorderLayout.NORTH);

        startPanel.add(lagStartLett(), BorderLayout.WEST); 
        startPanel.add(lagStartMiddels(), BorderLayout.CENTER);
        startPanel.add(lagStartVanskelig(), BorderLayout.EAST);
        startPanel.add(lagExit(), BorderLayout.SOUTH);
        
    //--------------------
        panel.add(tittelPanel, BorderLayout.NORTH);
        panel.add(startPanel, BorderLayout.SOUTH);

        vindu.add(panel);
        vindu.pack();
        centreWindow(vindu);
        vindu.setVisible(true);
    }

    private JButton lagStartLett(){
        JButton lett = new JButton("Lett");
        lett.setBackground(new Color(255,255,153));
        lett.setOpaque(true);
        lett.setForeground(Color.BLACK);
        lett.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 20));
        class StartLettAction implements ActionListener{
            @Override
            public void actionPerformed (ActionEvent e){
                dim = 20;
                startSpillet(dim);
            }
        }
        lett.addActionListener(new StartLettAction());

        return lett;
    }

    private JButton lagStartMiddels(){
        JButton middels = new JButton("Middels");
        middels.setBackground(new Color(255,204,153));
        middels.setOpaque(true);
        middels.setForeground(Color.BLACK);
        middels.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 20));
        class StartMiddelsAction implements ActionListener{
            @Override
            public void actionPerformed (ActionEvent e){
                dim = 12;
                startSpillet(dim);
            }
        }
        middels.addActionListener(new StartMiddelsAction());

        return middels;
    }

    private JButton lagStartVanskelig(){
        JButton vanskelig = new JButton("Vanskelig");
        vanskelig.setBackground(new Color(255,153,153));
        vanskelig.setOpaque(true);
        vanskelig.setForeground(Color.BLACK);
        vanskelig.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 20));
        class StartVanskeligAction implements ActionListener{
            @Override
            public void actionPerformed (ActionEvent e){
                dim = 8;
                startSpillet(dim);
            }
        }
        vanskelig.addActionListener(new StartVanskeligAction());

        return vanskelig;
    }

    private JButton lagExit(){
        JButton exit = new JButton("EXIT");
        exit.setBackground(new Color(153,255,204));
        exit.setOpaque(true);
        exit.setForeground(Color.BLACK);
        exit.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 20));
        
        class StoppAction implements ActionListener{
            @Override
            public void actionPerformed (ActionEvent e){
                c.avslutt();
            }
        }
        exit.addActionListener(new StoppAction());
        

        return exit;
    }

    public static void centreWindow(JFrame frame) {
    //denne er funnet på stackoverflow, og lagt til i koden kun for gøy
        Dimension dimension = Toolkit.getDefaultToolkit().getScreenSize();
        int x = (int) ((dimension.getWidth() - frame.getWidth()) / 2);
        int y = (int) ((dimension.getHeight() - frame.getHeight()) / 2);
        frame.setLocation(x, y);
    }

    private void startSpillet(int dim){
        ruter = new JLabel[dim][dim];
        poeng = new JLabel("Poeng: " + 0);
        tittelPanel.add(poeng, BorderLayout.SOUTH);
        panel.remove(startPanel);
        spillPanel = new JPanel();
        spillPanel.setLayout(new GridLayout(dim, dim));
        
        for (int i=0; i < dim; i++) {
            for (int j=0; j< dim; j++){
                ruter[i][j] = new JLabel(" ",SwingConstants.CENTER);
                JLabel rute = ruter[i][j];
                //rute.setBorder(BorderFactory.createLineBorder(Color.BLACK));
                rute.setPreferredSize(new Dimension(30,30));
                spillPanel.add(ruter[i][j]);
            }
        }

        c.startModel(dim);
        m = c.hentModel();
        
        spillPanel.setBackground(new Color(204,255,229));

        panel.add(spillPanel, BorderLayout.CENTER);
        panel.add(lagMeny(), BorderLayout.SOUTH);
        vindu.addKeyListener(new TrykkKnapp());
        vindu.setFocusable(true);
        vindu.requestFocus();

        c.start();

        vindu.setSize(dim*50, dim*50);
        centreWindow(vindu);
        vindu.validate();
    }

    class TrykkKnapp implements KeyListener{
        @Override
        public void keyPressed(KeyEvent e){
            int nokkel = e.getKeyCode();
            if (nokkel == KeyEvent.VK_LEFT){
                c.settRetning(Retning.VEST);
            }
            if (nokkel == KeyEvent.VK_UP){
                c.settRetning(Retning.NORD);
            }
            if (nokkel == KeyEvent.VK_RIGHT){
                c.settRetning(Retning.OST);
            }
            if (nokkel == KeyEvent.VK_DOWN){
                c.settRetning(Retning.SOR);
            }
            
        }

        @Override
        public void keyReleased(KeyEvent e){
        }

        @Override
        public void keyTyped(KeyEvent e){
        }
    }

    private JButton lagMeny(){
        JButton meny = new JButton("HOVEDMENY");

        meny.setBackground(new Color(153,255,204));
        meny.setOpaque(true);
        meny.setForeground(Color.BLACK);
        meny.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 20));
        

        class Meny implements ActionListener{
            @Override
            public void actionPerformed (ActionEvent e){
                vindu.dispose();
                velkomstPanel();
            }
        }
        meny.addActionListener(new Meny());

        return meny;
    }

    public void tegnPremie(int lengde, int bredde){
        JLabel rute = ruter[lengde][bredde];

        Image img = premie.getImage();
        Image newImg = img.getScaledInstance(30, 30,  java.awt.Image.SCALE_SMOOTH);
        premie = new ImageIcon(newImg);

        rute.setIcon(premie);
    }

    public void tegnHode(int lengde, int bredde){
        JLabel rute = ruter[lengde][bredde];

        Image img = hode.getImage();
        Image newImg = img.getScaledInstance(50, 50,  java.awt.Image.SCALE_SMOOTH);
        hode = new ImageIcon(newImg);

        rute.setIcon(hode);
    }

    public void tegnKropp(int lengde, int bredde){
        JLabel rute = ruter[lengde][bredde];

        Image img = kropp.getImage();
        Image newImg = img.getScaledInstance(40, 40,  java.awt.Image.SCALE_SMOOTH);
        kropp = new ImageIcon(newImg);

        rute.setIcon(kropp);
    }

    /*public void tegnPaaBrett(String symbol, int lengde, int bredde){
        
        if (symbol.equals("p")){
            JLabel rute = ruter[lengde][bredde];

            Image img = premie.getImage();
            Image newImg = img.getScaledInstance(20, 20,  java.awt.Image.SCALE_SMOOTH);
            premie = new ImageIcon(newImg);

            rute.setIcon(premie);
            //rute.setText("$");
            //rute.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 20));
        }
        else{
            ruter[lengde][bredde].setBackground(Color.YELLOW);
            ruter[lengde][bredde].setOpaque(true);
            ruter[lengde][bredde].setText(" ");
        }
    }*/

    public void fjernPremie(int lengde, int bredde){
        ruter[lengde][bredde].setIcon(null);
        ruter[lengde][bredde].setBackground(new Color(204,255,229));
        ruter[lengde][bredde].setOpaque(true);
    }

    public void fjernHode(int lengde, int bredde){
        ruter[lengde][bredde].setIcon(null);
        ruter[lengde][bredde].setBackground(new Color(204,255,229));
        ruter[lengde][bredde].setOpaque(true);
    }

    public void fjernKropp(int lengde, int bredde){
        ruter[lengde][bredde].setIcon(null);
        ruter[lengde][bredde].setBackground(new Color(204,255,229));
        ruter[lengde][bredde].setOpaque(true);
    }

    /* 
    public void fjernFraBrett(int lengde, int bredde, String symbol){
        //ruter[lengde][bredde].setText(" ");
        ruter[lengde][bredde].setBackground(new Color(204,255,229));
        ruter[lengde][bredde].setOpaque(true);

    }*/

    public void leggTilPoeng(int p){
        poeng.setText("Poeng: " + p);
    }

    public void vinner(){
        //JPanel vinnerPanel = new JPanel();
        //vinnerPanel.setLayout(new BorderLayout());

        JLabel vinnerTxt = new JLabel("GRATULERER! Du VANT!!", SwingConstants.CENTER);
        vinnerTxt.setForeground(Color.BLACK);
        vinnerTxt.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 20));

        panel.add(vinnerTxt, BorderLayout.CENTER);
        //vinnerPanel.setBackground(Color.BLACK);

        panel.remove(spillPanel);
        //panel.add(vinnerPanel, BorderLayout.CENTER);
        vindu.validate();
    }

    public void taper(){
        JLabel gameOver = new JLabel("GAME OVER!", SwingConstants.CENTER);
        gameOver.setForeground(Color.BLACK);
        gameOver.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 40));

        panel.add(gameOver, BorderLayout.CENTER);

        panel.remove(spillPanel);
        vindu.validate();
    }
}
