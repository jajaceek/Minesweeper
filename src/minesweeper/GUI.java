package minesweeper;

import javax.swing.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.util.*;
import java.awt.*;

public class GUI extends JFrame {

    public boolean resetter = false;
    public boolean flagger = false;

    public int flaggedCount = 0;

    Date startDate = new Date();
    Date endDate;

    int spacing = 2;
    int neighs = 0;
    String vicMes = "";

    public int mx = -100;
    public int my = -100;

    public int smileyX = 453;
    public int smileyY = 5;

    public int smileCenterX = smileyX + 25;
    public int smileCenterY = smileyY + 25;

    public int flaggerX = 365;
    public int flaggerY = 5;

    public int flaggerCenterX = flaggerX + 25;
    public int flaggerCenterY = flaggerY + 25;

    public int timeX = 840;
    public int timeY = 3;

    public int vicMesX = 540;
    public int vicMesY = -50;

    public int infoMinesX = 3;
    public int infoMinesY = 3;

    public int sec = 0;

    public boolean happiness = true;

    public boolean victory = false;

    public boolean defeat = false;

    Random rand = new Random();

    int[][] mines = new int[16][9];
    int[][] neighbours = new int[16][9];
    boolean[][] revaled = new boolean[16][9];
    boolean[][] flagged = new boolean[16][9];

    public GUI() {
        this.setTitle("Saper");
        this.setSize(976, 639);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setVisible(true);
        this.setLocationRelativeTo(null);
        this.setResizable(false);

        for (int i = 0; i < 16; i++) {
            for (int j = 0; j < 9; j++) {
                if (rand.nextInt(100) < 10) { // ustala ilość min
                    mines[i][j] = 1;
                } else {
                    mines[i][j] = 0;
                }
                revaled[i][j] = false;
            }
        }

        for (int i = 0; i < 16; i++) {
            for (int j = 0; j < 9; j++) {
                neighs = 0;
                for (int m = 0; m < 16; m++) {
                    for (int n = 0; n < 9; n++) {
                        if (!(m == i && n == j)) { // dzięki temu nie liczy pola środkowego z minami
                            if (isN(i, j, m, n) == true)
                                neighs++;
                        }
                    }
                }
                neighbours[i][j] = neighs;
            }
        }

        Board board = new Board();
        this.setContentPane(board);

        Move move = new Move();
        this.addMouseMotionListener(move);

        Click click = new Click();
        this.addMouseListener(click);
    }

    public class Board extends JPanel {
        public void paintComponent(Graphics g) {
            g.setColor(new Color(89, 53, 9));
            g.fillRect(0, 0, 1280, 800);
            for (int i = 0; i < 16; i++) {
                for (int j = 0; j < 9; j++) {
                    g.setColor(new Color(216, 113, 54));
//                    if (mines[i][j] == 1) {
//                        g.setColor(Color.yellow);
//                    }
                    if (revaled[i][j] == true) {
                        g.setColor(new Color(165, 93, 4));
                        if (mines[i][j] == 1) {
                            g.setColor(Color.red);
                        }
                    }
                    if (mx >= i * 60 + i * spacing / 2 && mx < i * 60 + 60 + i * spacing / 2
                            && my >= j * 60 + 60 + 30 + j * spacing / 2 && my < j * 60 + 60 + 60 + j * spacing / 2 + 30) {
                        g.setColor(Color.lightGray);
                    }
                    g.fillRect(spacing + i * 60, spacing + j * 60 + 60, 60 - 2 * spacing, 60 - 2 * spacing);
                    if (revaled[i][j] == true) {
                        g.setColor(Color.black);
                        if (mines[i][j] == 0 && neighbours[i][j] != 0) {
                            if (neighbours[i][j] == 1) {
                                g.setColor(Color.blue);
                            } else if (neighbours[i][j] == 2) {
                                g.setColor(Color.green);
                            } else if (neighbours[i][j] == 3) {
                                g.setColor(Color.red);
                            } else if (neighbours[i][j] == 4) {
                                g.setColor(new Color(0, 0, 128));
                            } else if (neighbours[i][j] == 5) {
                                g.setColor(new Color(178, 34, 34));
                            } else if (neighbours[i][j] == 6) {
                                g.setColor(new Color(72, 209, 204));
                            } else if (neighbours[i][j] == 7) {
                                g.setColor(new Color(0, 0, 128));
                            } else if (neighbours[i][j] == 8) {
                                g.setColor(Color.darkGray);
                            }
                            g.setFont(new Font("Arial", Font.BOLD, 40));
                            g.drawString(Integer.toString(neighbours[i][j]), i * 60 + 20, j * 60 + 60 + 45);
                        } else if (mines[i][j] == 1) {
                            g.fillRect(i * 60 + 20, j * 60 + 75, 20, 20); // rysowanie miny
                            g.fillRect(i * 60 + 15, j * 60 + 80, 30, 20); // rysowanie miny
                            g.fillRect(i * 60 + 20, j * 60 + 85, 20, 20); // rysowanie miny
                            g.fillRect(i * 60 + 30, j * 60 + 60, 4, 50);
                            g.fillRect(i * 60, j * 60 + 60 + 30, 50, 4);
                        }
                    }
                    // rysuje flagę
                    if (flagged[i][j] == true) {
                        g.setColor(Color.black);
                        g.fillRect(i * 60 + 27, j * 60 + 70 + 8, 5, 31);
                        g.fillRect(i * 60 + 17, j * 60 + 70 + 33, 25, 5);
                        g.setColor(Color.red);
                        g.fillRect(i * 60 + 9, j * 60 + 70 + 8, 20, 12);
                        g.setColor(Color.black);
                        g.drawRect(i * 60 + 9, j * 60 + 70 + 8, 20, 12);
                        g.drawRect(i * 60 + 10, j * 60 + 70 + 9, 18, 10);
                    }
                }
            }
            // similey painting - rysowanie uśmiechu
            g.setColor(Color.yellow);
            g.fillOval(smileyX, smileyY, 50, 50); // głowa
            g.setColor(Color.black);
            g.fillOval(smileyX + 10, smileyY + 12, 8, 8); // lewe oko
            g.fillOval(smileyX + 30, smileyY + 12, 8, 8); // prawe oko
            if (happiness == true) {
                g.fillRect(smileyX + 11, smileyY + 32, 28, 4);
                g.fillRect(smileyX + 10, smileyY + 28, 4, 4);
                g.fillRect(smileyX + 36, smileyY + 28, 4, 4);
            } else {
                g.fillRect(smileyX + 11, smileyY + 32, 28, 4);
                g.fillRect(smileyX + 10, smileyY + 36, 4, 4);
                g.fillRect(smileyX + 36, smileyY + 36, 4, 4);
            }

            // rysuje przełącznik flagi

            g.setColor(Color.black);
            g.fillRect(flaggerX + 27, flaggerY + 8, 5, 31);
            g.fillRect(flaggerX + 17, flaggerY + 33, 25, 5);
            g.setColor(Color.red);
            g.fillRect(flaggerX + 9, flaggerY + 8, 20, 12);
            g.setColor(Color.black);
            g.drawRect(flaggerX + 9, flaggerY + 8, 20, 12);
            g.drawRect(flaggerX + 10, flaggerY + 9, 18, 10);

            if (flagger == true) {
                g.setColor(Color.red);
            }
            g.drawOval(flaggerX, flaggerY, 50, 50);
            g.drawOval(flaggerX + 1, flaggerY + 1, 48, 48);


            // pokazuje czas gry
            g.setColor(Color.black);
            g.fillRect(timeX, timeY, 118, 55);
            if (defeat == false & victory == false) { // zatrzymuje zagar
                sec = (int) ((new Date().getTime() - startDate.getTime()) / 1000);
            }
            if (sec > 999) {
                sec = 999;
            }
            g.setColor(new Color(216, 113, 54));
            if (victory == true) {  // zmienia kolor zegara na zielony
                g.setColor(Color.green);
            }
            if (defeat == true) { // zmienia kolor zegara na czerwony
                g.setColor(Color.red);
            }
            g.setFont(new Font("Arial", Font.PLAIN, 50));
            if (sec < 10) {
                g.drawString("00" + Integer.toString(sec), timeX + 18, timeY + 45);
            } else if (sec < 99) {
                g.drawString("0" + Integer.toString(sec), timeX + 18, timeY + 45);
            } else {
                g.drawString(Integer.toString(sec), timeX + 18, timeY + 45);
            }

            // rysowanie wiadmości o zwycięstwie

            if (victory == true) {
                g.setColor(Color.green);
                vicMes = "Wygrałeś!!!";
            } else if (defeat == true) {
                g.setColor(Color.red);
                vicMes = "Przegrałeś!!!";
            }

            if (victory == true || defeat == true) {
                vicMesY = -50 + (int) (new Date().getTime() - endDate.getTime()) / 10;
                if (vicMesY > 50) { // zatrzymuje napis
                    vicMesY = 50;
                }
                g.setFont(new Font("Arial", Font.PLAIN, 50));
                g.drawString(vicMes, vicMesX, vicMesY);
            }

            // rysowanie info o ilości min
            g.setColor(Color.black);
            g.fillRect(infoMinesX, infoMinesY, 296, 55);
            g.setColor(new Color(216, 113, 54));
            g.setFont(new Font("Arial", Font.PLAIN, 50));
            g.drawString("Miny: " + flaggedCount + "/" + String.valueOf(totalMines()), infoMinesX + 18, infoMinesY + 45);

        }
    }

    public class Move implements MouseMotionListener {

        @Override
        public void mouseDragged(MouseEvent e) {

        }

        @Override
        public void mouseMoved(MouseEvent e) {
            mx = e.getX(); // czyta wartość korsura X
            my = e.getY(); // czyta wartość kursora y
//            System.out.println(mx + " " + my);
        }
    }

    public class Click implements MouseListener {


        @Override
        public void mouseClicked(MouseEvent e) {

            mx = e.getX(); // czyta wartość korsura X
            my = e.getY(); // czyta wartość kursora y

            if (inBoxX() != -1 && inBoxY() != -1) {
                System.out.println("Kliknięcie w: " + inBoxX() + " " + inBoxY() + "  liczba sąsiadów min: " + neighbours[inBoxX()][inBoxY()]);
                if (flagger == true && revaled[inBoxX()][inBoxY()] == false) {
                    if (flagged[inBoxX()][inBoxY()] == false) {
                        flagged[inBoxX()][inBoxY()] = true;
                        flaggedCount++;
                    } else {
                        flagged[inBoxX()][inBoxY()] = false;
                        flaggedCount--;
                    }
                } else {
                    if (flagged[inBoxX()][inBoxY()] == false) {
                        revaled[inBoxX()][inBoxY()] = true;
                    }
                }
            } else {
//                System.out.println("Kliknięcie poza");
            }

            if (inSmiley() == true) {
                resetAll();
            }

            if (inFlagger() == true) {
                if (flagger == false) {
                    flagger = true;
                } else {
                    flagger = false;
                }
            }
        }

        @Override
        public void mousePressed(MouseEvent e) {

        }

        @Override
        public void mouseReleased(MouseEvent e) {

        }

        @Override
        public void mouseEntered(MouseEvent e) {

        }

        @Override
        public void mouseExited(MouseEvent e) {

        }

    }

    public void checkVictoryStatus() {
        if (defeat == false) {
            for (int i = 0; i < 16; i++) {
                for (int j = 0; j < 9; j++) {
                    if (revaled[i][j] == true && mines[i][j] == 1) {
                        defeat = true;
                        happiness = false;
                        endDate = new Date();
                    }
                }
            }
        }

        if (totalBoxesRevealed() >= 144 - totalMines() && victory == false) {
            victory = true;
            endDate = new Date();
        }
    }

    public int totalMines() {
        int total = 0;
        for (int i = 0; i < 16; i++) {
            for (int j = 0; j < 9; j++) {
                if (mines[i][j] == 1) {
                    total++;
                }
            }
        }
        return total;
    }

    public int totalBoxesRevealed() {
        int total = 0;
        for (int i = 0; i < 16; i++) {
            for (int j = 0; j < 9; j++) {
                if (revaled[i][j] == true) {
                    total++;
                }
            }
        }
        return total;
    }

    public void resetAll() {

        flagger = false;
        resetter = true;

        flaggedCount = 0;

        startDate = new Date();

        vicMesY = -50;
        vicMes = "Do zrobienia";

        happiness = true;
        victory = false;
        defeat = false;


        for (int i = 0; i < 16; i++) {
            for (int j = 0; j < 9; j++) {
                if (rand.nextInt(100) < 20) { // ustala ilość min
                    mines[i][j] = 1;
                } else {
                    mines[i][j] = 0;
                }
                revaled[i][j] = false;
                flagged[i][j] = false;
            }
        }

        for (int i = 0; i < 16; i++) {
            for (int j = 0; j < 9; j++) {
                neighs = 0;
                for (int m = 0; m < 16; m++) {
                    for (int n = 0; n < 9; n++) {
                        if (!(m == i && n == j)) { // dzięki temu nie liczy pola środkowego z minami
                            if (isN(i, j, m, n) == true)
                                neighs++;
                        }
                    }
                }
                neighbours[i][j] = neighs;
            }
        }
        resetter = false;
    }

    public boolean inSmiley() {
        int dif = (int) Math.sqrt(Math.abs(mx - smileCenterX) * Math.abs(mx - smileCenterX) + Math.abs(my - smileCenterY) * Math.abs(my - smileCenterY));
        if (dif < 50) {
            return true;
        }
        return false;
    }

    public boolean inFlagger() {
        int dif = (int) Math.sqrt(Math.abs(mx - flaggerCenterX) * Math.abs(mx - flaggerCenterX) + Math.abs(my - flaggerCenterY) * Math.abs(my - flaggerCenterY));
        if (dif < 50) {
            return true;
        }
        return false;
    }


    public int inBoxX() {
        for (int i = 0; i < 16; i++) {
            for (int j = 0; j < 9; j++) {
                if (mx >= i * 60 + i * spacing / 2 && mx < i * 60 + 60 + i * spacing / 2
                        && my >= j * 60 + 60 + 30 + j * spacing / 2 && my < j * 60 + 60 + 60 + j * spacing / 2 + 30) {
                    return i;
                }
            }
        }
        return -1;
    }

    public int inBoxY() {
        for (int i = 0; i < 16; i++) {
            for (int j = 0; j < 9; j++) {
                if (mx >= i * 60 + i * spacing / 2 && mx < i * 60 + 60 + i * spacing / 2
                        && my >= j * 60 + 60 + 30 + j * spacing / 2 && my < j * 60 + 60 + 60 + j * spacing / 2 + 30) {
                    return j;
                }
            }
        }
        return -1;
    }

    public boolean isN(int mX, int mY, int cX, int cY) {
        if (mX - cX < 2 && mX - cX > -2 && mY - cY < 2 && mY - cY > -2 && mines[cX][cY] == 1) {

            return true;
        }
        return false;
    }

}
