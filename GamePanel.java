import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.util.Random;

public class GamePanel extends JPanel implements ActionListener {

    static final int SCREEN_WIDTH = 600;
    static final int SCREEN_HEIGHT = 600;
    static final int DEFAULT_BODY = 6;
    static final int UNIT_SIZE = 25;
    static final int GAME_UNITS = (SCREEN_WIDTH*SCREEN_HEIGHT) / UNIT_SIZE;
    static final int DELAY = 75; 

    final int x[] = new int[GAME_UNITS]; // Holds all x co-ords of the body of the snake
    final int y[] = new int[GAME_UNITS]; // Holds all y co-ords of the body of the snake
    int bodyParts = DEFAULT_BODY;
    
    int applesEaten;
    int appleX; 
    int appleY;
    char direction = 'R'; // R for right, L for left, D for down, U for up

    boolean running = false;
    Timer timer;
    Random random;

    GamePanel(){
        random = new Random();
        this.setPreferredSize(new Dimension(SCREEN_WIDTH,SCREEN_HEIGHT));
        this.setBackground(Color.BLACK);
        this.setFocusable(true);
        this.addKeyListener(new MyKeyAdapter());
        startGame();
    }

    public void startGame(){
        newApple();
        running = true;
        timer = new Timer(DELAY, this);
        timer.start();
    }

    public void paintComponent(Graphics g){
        super.paintComponent(g);
        draw(g);
    }

    public void draw(Graphics g){

        if(running) {

            // Un-comment if you would like to see gridlines appear

            // g.setColor(Color.darkGray);
            // for(int i=0;i<(SCREEN_HEIGHT/UNIT_SIZE);i++){
            //     g.drawLine((i*UNIT_SIZE), 0, (i*UNIT_SIZE), SCREEN_HEIGHT);
            //     g.drawLine(0,i*UNIT_SIZE, SCREEN_WIDTH, i*UNIT_SIZE);
            // }

            g.setColor(Color.red);
            g.fillOval(appleX, appleY, UNIT_SIZE, UNIT_SIZE);

            for(int i=0; i<bodyParts; i++){
                if (i == 0){
                    g.setColor(Color.green);
                    g.fillRect(x[i], y[i], UNIT_SIZE, UNIT_SIZE);
                } else {
                    g.setColor(new Color(45,180,0));
                    g.fillRect(x[i], y[i], UNIT_SIZE, UNIT_SIZE);
                }
            }

            // Score tracking at the top     
            g.setColor(Color.RED);
            g.setFont(new Font("Arial",Font.BOLD,40));
            FontMetrics metrics = getFontMetrics(g.getFont());
            g.drawString("Score: " + applesEaten, (SCREEN_WIDTH - metrics.stringWidth("Score: " + applesEaten))/2, g.getFont().getSize());
        } else {
            gameOver(g);
        }
    }

    public void move(){
        for(int i = bodyParts;i > 0; i--){
            x[i] = x[i-1];
            y[i] = y[i-1];
        }

        switch (direction) {
            case 'U':
                y[0] = y[0] - UNIT_SIZE;
                break;
            case 'D':
                y[0] = y[0] + UNIT_SIZE;
                break;            
            case 'R':
                x[0] = x[0] + UNIT_SIZE;
                break;       
            case 'L':
                x[0] = x[0] - UNIT_SIZE;
                break;        
        }
    }

    public void checkApple(){

        if ((x[0] == appleX) && (y[0] == appleY)){
            ++applesEaten;
            ++bodyParts;
            newApple();
        }
    }

    public void checkCollisions(){

        // Checks if head collides with body
        for(int i=bodyParts; i<0; i--){
            if((x[0] == x[i]) && (y[0] == y[i])){
                running = false;
            }
        }

        //Checks if head touches left border
        if (x[0] < 0){
            running = false;
            timer.stop();
        }

        //Checks if head touches right border
        if (x[0] > SCREEN_WIDTH){
            running = false;
            timer.stop();
        }

        //Check if head touches top border
        if (y[0] < 0){
            running = false;
            timer.stop();
        }

        //Checks if head touches bottom border
        if (y[0] > SCREEN_HEIGHT){
            running = false;
            timer.stop();
        }
    }

    public void newApple(){
        appleX = random.nextInt((int)(SCREEN_WIDTH/UNIT_SIZE))*UNIT_SIZE;
        appleY = random.nextInt((int)(SCREEN_HEIGHT/UNIT_SIZE))*UNIT_SIZE;
    }

    public void gameOver(Graphics g){

        // Game over text
        g.setColor(Color.red);
        g.setFont(new Font("Times New Roman",Font.BOLD,75));
        FontMetrics metrics = getFontMetrics(g.getFont());
        g.drawString("GAME OVER", (SCREEN_WIDTH - metrics.stringWidth("GAME OVER"))/2, SCREEN_HEIGHT/2);

        // Score display on the game over screen
        g.setFont(new Font("Times New Roman",Font.BOLD,75));
        FontMetrics metrics2 = getFontMetrics(g.getFont());
        g.drawString("You scored: " + applesEaten, (SCREEN_WIDTH - metrics2.stringWidth("You scored: " + applesEaten))/2, g.getFont().getSize());

        // Restart text display
        g.setFont(new Font("Times New Roman",Font.BOLD,40));
        FontMetrics metrics3 = getFontMetrics(g.getFont());
        g.drawString("Press 'r' to restart!", (SCREEN_WIDTH - metrics3.stringWidth("Press 'r' to restart!"))/2, g.getFont().getSize() + UNIT_SIZE * 3);
    }

    public void restart(){
        applesEaten = 0;
        bodyParts = DEFAULT_BODY;
        new GameFrame();
        startGame();
    }

    @Override
    public void actionPerformed(ActionEvent e){
        if(running){
            move();
            checkApple();
            checkCollisions();
        }
        repaint();
    }

    public class MyKeyAdapter extends KeyAdapter{
        @Override
        public void keyPressed(KeyEvent e){

            switch (e.getKeyCode()) {
                case KeyEvent.VK_LEFT:
                    if(direction != 'R'){
                        direction = 'L';
                    }
                    break;
                case KeyEvent.VK_RIGHT:
                    if(direction != 'L'){
                        direction = 'R';
                    }
                    break;

                case KeyEvent.VK_UP:
                    if(direction != 'D'){
                        direction = 'U';
                    }
                    break;

                case KeyEvent.VK_DOWN:
                    if(direction != 'U'){
                        direction = 'D';
                    }
                    break;

                case KeyEvent.VK_R:
                    restart();
                default:
                    break;
            }
        }
    }
}