package minesweeper;

import javax.swing.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.util.*;
import java.awt.*;

public class GUI extends JFrame {

    int spacing = 2;
    int neighs = 0;

    public int mx = -100;
    public int my = -100;

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
                if (rand.nextInt(100) < 20) {
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
                    if (mines[i][j] == 1) {
                        g.setColor(Color.yellow);
                    }
                    if (revaled[i][j] == true) {
                        g.setColor(Color.white);
                        if (mines[i][j] == 1) {
                            g.setColor(Color.red);
                        }
                    }
                    if (mx >= spacing + i * 60 && mx < spacing + i * 60 + 60 - spacing
                            && my >= spacing + j * 60 + 60 + 16 && my < spacing + j * 60 + 60 + 60 - 2 * spacing + 16) {
                        g.setColor(Color.lightGray);
                    }
                    g.fillRect(spacing + i * 60, spacing + j * 60 + 60, 60 - 2 * spacing, 60 - 2 * spacing);
                    if (revaled[i][j] == true) {
                        g.setColor(Color.black);
                        if (mines[i][j] == 0) {
                            g.setFont(new Font("Arial", Font.BOLD, 40));
                            g.drawString(Integer.toString(neighbours[i][j]), i * 60 + 20, j * 60 + 60 + 45);
                        } else {
                            g.fillRect(i * 60 + 20, j * 60 + 75, 20, 20); // rysowanie miny
                            g.fillRect(i * 60 + 15, j * 60 + 80, 30, 20); // rysowanie miny
                            g.fillRect(i * 60 + 20, j * 60 + 85, 20, 20); // rysowanie miny
                        }
                    }
                }
            }
        }
    }

    public class Move implements MouseMotionListener {

        @Override
        public void mouseDragged(MouseEvent e) {

        }

        @Override
        public void mouseMoved(MouseEvent e) {
            mx = e.getX();
            my = e.getY();
//            System.out.println(mx + " " + my);
        }
    }

    public class Click implements MouseListener {

        @Override
        public void mouseClicked(MouseEvent e) {

            if (inBoxX() != -1 && inBoxY() != -1) {
                revaled[inBoxX()][inBoxY()] = true;
            }

            if (inBoxX() != -1 && inBoxY() != -1) {
                System.out.println("Kliknięcie w: " + inBoxX() + " " + inBoxY() + "  liczba sąsiadów min: " + neighbours[inBoxX()][inBoxY()]);
            } else {
                System.out.println("Kliknięcie poza");
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

    public int inBoxX() {
        for (int i = 0; i < 16; i++) {
            for (int j = 0; j < 9; j++) {
                if (mx >= spacing + i * 60 && mx < spacing + i * 60 + 60 - spacing
                        && my >= spacing + j * 60 + 60 + 16 && my < spacing + j * 60 + 60 + 60 - 2 * spacing + 16) {
                    return i;
                }
            }
        }
        return -1;
    }

    public int inBoxY() {
        for (int i = 0; i < 16; i++) {
            for (int j = 0; j < 9; j++) {
                if (mx >= spacing + i * 60 && mx < spacing + i * 60 + 60 - spacing
                        && my >= spacing + j * 60 + 60 + 16 && my < spacing + j * 60 + 60 + 60 - 2 * spacing + 16) {
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
