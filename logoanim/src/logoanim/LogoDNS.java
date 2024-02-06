package logoanim;

import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;
import javax.imageio.ImageIO;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.WindowConstants;

public class LogoDNS extends JFrame {
    private static LogoDNS logodns;
    private static long last_frame_time;
    private static Image logo;
    private static Image fon;
    private static float drop_left = 20;
    private static float drop_top = 20;
    private static float drop_vx = 200;
    private static float drop_vy = 200; // Изменено: добавлено движение по вертикали
    private static int score = 0;

    public static void main(String[] args) throws IOException {
        fon = ImageIO.read(LogoDNS.class.getResourceAsStream("fon.jpg"));
        logo = ImageIO.read(LogoDNS.class.getResourceAsStream("logo.png"));
        logodns = new LogoDNS();
        logodns.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        logodns.setLocation(200, 50);
        logodns.setSize(2283, 1501);
        logodns.setResizable(false);
        last_frame_time = System.nanoTime();
        GameField game_field = new GameField();
        game_field.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                int x = e.getX();
                int y = e.getY();
                boolean is_drop = x >= drop_left && x <= drop_left + logo.getWidth(null)
                        && y >= drop_top && y <= drop_top + logo.getHeight(null);

                if (is_drop) {
                    drop_top = 20;
                    drop_left = (int) (Math.random() * (game_field.getWidth() - logo.getWidth(null)));
                    score++;
                    logodns.setTitle("Score:" + score);
                }
            }
        });
        logodns.add(game_field);
        logodns.setVisible(true);
    }

    private static void onRepaint(Graphics g) {
        long current_time = System.nanoTime();
        float delta_time = (current_time - last_frame_time) * 0.000000001f;
        last_frame_time = current_time;

        // Изменено: движение по диагонали
        drop_left = drop_left + drop_vx * delta_time;
        drop_top = drop_top + drop_vy * delta_time;

        // Изменено: отражение от краев экрана по обеим осям
        if (drop_left + logo.getWidth(null) > logodns.getWidth() || drop_left < 0) {
            drop_vx = -drop_vx;
        }
        if (drop_top + logo.getHeight(null) > logodns.getHeight() || drop_top < 0) {
            drop_vy = -drop_vy;
        }

        g.drawImage(fon, 0, 0, null);
        g.drawImage(logo, (int) drop_left, (int) drop_top, null);
    }

    private static class GameField extends JPanel {
        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            onRepaint(g);
            repaint();
        }
    }
}
