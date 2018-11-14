package cz.wa.secretagent.utils.raycaster;

import cz.wa.wautils.math.Vector2I;
import org.apache.commons.math3.geometry.euclidean.twod.Vector2D;
import secretAgent.utils.RayCaster;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.Random;

/**
 * Test raycast.
 * 
 * @author Ondrej Milenovsky
 */
public class RayCasterMain extends JFrame {

    private static final long serialVersionUID = 7769639237175665889L;

    private JPanel canvas;

    private final int size = 20;

    private final int sizeX = 20;
    private final int sizeY = 20;
    private final double wallsPr = 0.2;

    private final double speed = 0.2;
    private final double rotate = 0.05;

    private int[][] map;
    private Vector2D pos;
    private double angle;

    public RayCasterMain() {
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setBounds(20, 20, 800, 600);
        initComponents();
        setVisible(true);
        init();
    }

    private void init() {
        pos = new Vector2D(5, 5);
        angle = 0;
        Random rnd = new Random();
        map = new int[sizeX][sizeY];
        for (int i = 0; i < sizeX; i++) {
            map[i][0] = 1;
            map[i][sizeY - 1] = 1;
        }
        for (int i = 0; i < sizeY; i++) {
            map[0][i] = 1;
            map[sizeX - 1][i] = 1;
        }

        for (int y = 1; y < sizeY - 1; y++) {
            for (int x = 1; x < sizeX - 1; x++) {
                if (rnd.nextDouble() < wallsPr) {
                    map[x][y] = 1;
                }
            }
        }
    }

    private void initComponents() {
        canvas = new JPanel();
        add(canvas, BorderLayout.CENTER);
        addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                keyPressed2(e.getKeyCode());
            }
        });
    }

    private void keyPressed2(int key) {
        if (key == KeyEvent.VK_UP) {
            pos = pos.add(new Vector2D(Math.cos(angle), Math.sin(angle)).scalarMultiply(speed));
        } else if (key == KeyEvent.VK_DOWN) {
            pos = pos.subtract(new Vector2D(Math.cos(angle), Math.sin(angle)).scalarMultiply(speed));
        }

        if (key == KeyEvent.VK_LEFT) {
            angle -= rotate;
        } else if (key == KeyEvent.VK_RIGHT) {
            angle += rotate;
        }
        draw();
    }

    private void draw() {
        Graphics2D g = (Graphics2D) canvas.getGraphics();
        int width = canvas.getWidth();
        int height = canvas.getHeight();

        // bg
        g.setColor(Color.LIGHT_GRAY);
        g.fillRect(0, 0, width, height);

        RayHit hit = new RayCaster(pos, new Vector2D(Math.cos(angle), Math.sin(angle)), 10000.0) {
            @Override
            protected boolean isWall(Vector2I i) {
                return map[i.getX()][i.getY()] > 0;
            }
        }.castRay();

        // map
        for (int y = 0; y < sizeY; y++) {
            for (int x = 0; x < sizeX; x++) {
                Vector2I i = new Vector2I(x, y);
                if ((hit != null) && hit.getMapPos().equals(i)) {
                    drawTile(i, Color.RED, g);
                } else if (map[x][y] > 0) {
                    drawTile(i, Color.BLACK, g);
                }
            }
        }
        // vector
        g.setColor(Color.GREEN);
        Vector2D pos2 = pos.add(new Vector2D(Math.cos(angle), Math.sin(angle)));
        g.setStroke(new BasicStroke(3));
        g.drawLine(round(pos.getX() * size), round(pos.getY() * size), round(pos2.getX() * size),
                round(pos2.getY() * size));
        if (hit != null) {
            pos2 = hit.getHitPos();
            g.setStroke(new BasicStroke(1));
            g.drawLine(round(pos.getX() * size), round(pos.getY() * size), round(pos2.getX() * size),
                    round(pos2.getY() * size));
        }
    }

    private void drawTile(Vector2I pos, Color color, Graphics g) {
        int x = pos.getX();
        int y = pos.getY();
        g.setColor(color);
        g.fillRect(x * size, y * size, size, size);
    }

    private int round(double d) {
        return (int) Math.round(d);
    }

    public static void main(String[] args) {
        new RayCasterMain();
    }

}
