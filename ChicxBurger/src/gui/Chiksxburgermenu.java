package gui;

import java.awt.*;
import java.awt.geom.RoundRectangle2D;
import javax.swing.*;
import javax.swing.border.EmptyBorder;


public class Chiksxburgermenu extends JFrame {

    // ---------- Paleta de colores de marca ----------
    static final Color BROWN_950 = new Color(0x3A, 0x18, 0x10);
    static final Color MAROON    = new Color(0x5C, 0x27, 0x18);
    static final Color RED       = new Color(0xC1, 0x27, 0x2D);
    static final Color GOLD      = new Color(0xE8, 0xA3, 0x3D);
    static final Color GOLD_DEEP = new Color(0xD9, 0x8A, 0x1F);
    static final Color CREAM     = new Color(0xFB, 0xF1, 0xE2);
    static final Color CREAM_2   = new Color(0xF4, 0xE6, 0xCF);
    static final Color INK       = new Color(0x2B, 0x18, 0x10);
    static final Color INK_SOFT  = new Color(0x6B, 0x4A, 0x3A);

    static final Font FONT_LOGO   = new Font("SansSerif", Font.BOLD, 22);
    static final Font FONT_NAV    = new Font("SansSerif", Font.BOLD, 14);
    static final Font FONT_H1     = new Font("SansSerif", Font.BOLD, 42);
    static final Font FONT_SUB    = new Font("SansSerif", Font.PLAIN, 15);
    static final Font FONT_CARD   = new Font("SansSerif", Font.BOLD, 16);
    static final Font FONT_TAG    = new Font("SansSerif", Font.PLAIN, 12);

    public Chiksxburgermenu() {
        setTitle("Chiksx Burger - Menu");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1180, 760);
        setMinimumSize(new Dimension(760, 560));
        setLocationRelativeTo(null);

        JPanel root = new JPanel(new BorderLayout());
        root.setBackground(CREAM);

        root.add(buildHeader(), BorderLayout.NORTH);

        JPanel center = new JPanel();
        center.setLayout(new BoxLayout(center, BoxLayout.Y_AXIS));
        center.setBackground(CREAM);
        center.add(buildHero());
        center.add(buildGrid());

        JScrollPane scroll = new JScrollPane(center);
        scroll.setBorder(null);
        scroll.getVerticalScrollBar().setUnitIncrement(16);
        scroll.setBackground(CREAM);
        scroll.getViewport().setBackground(CREAM);

        root.add(scroll, BorderLayout.CENTER);
        root.add(buildFooter(), BorderLayout.SOUTH);

        setContentPane(root);
    }

    // ---------- Barra superior ----------
    private JPanel buildHeader() {
        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(MAROON);
        header.setBorder(BorderFactory.createMatteBorder(0, 0, 4, 0, GOLD));
        header.setPreferredSize(new Dimension(10, 76));

        JPanel logoPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        logoPanel.setOpaque(false);
        logoPanel.setBorder(new EmptyBorder(0, 28, 0, 0));

        JLabel mark = new JLabel(buildLogoIcon(40));
        logoPanel.add(mark);

        JPanel logoText = new JPanel();
        logoText.setOpaque(false);
        logoText.setLayout(new BoxLayout(logoText, BoxLayout.Y_AXIS));
        JLabel title = new JLabel("CHIKSX BURGER");
        title.setFont(FONT_LOGO);
        title.setForeground(CREAM);
        JLabel subtitle = new JLabel("SABOR QUE ALIMENTA");
        subtitle.setFont(new Font("SansSerif", Font.BOLD, 10));
        subtitle.setForeground(GOLD);
        logoText.add(title);
        logoText.add(subtitle);
        logoPanel.add(logoText);

        JPanel navPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 8, 0));
        navPanel.setOpaque(false);
        navPanel.setBorder(new EmptyBorder(0, 0, 0, 28));
        navPanel.add(navLink("Menu", true));
        navPanel.add(navLink("Promociones & Apps", false));
        navPanel.add(navLink("Cajita Feliz", false));

        header.add(logoPanel, BorderLayout.WEST);
        header.add(navPanel, BorderLayout.EAST);
        return header;
    }

    private JLabel navLink(String text, boolean active) {
        JLabel link = new JLabel(text);
        link.setFont(FONT_NAV);
        link.setOpaque(true);
        link.setBorder(new EmptyBorder(10, 18, 10, 18));
        if (active) {
            link.setBackground(GOLD);
            link.setForeground(BROWN_950);
        } else {
            link.setBackground(MAROON);
            link.setForeground(CREAM_2);
        }
        return link;
    }

    private Icon buildLogoIcon(int size) {
        return new Icon() {
            public int getIconWidth() { return size; }
            public int getIconHeight() { return size; }
            public void paintIcon(Component c, Graphics g, int x, int y) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(GOLD);
                g2.fillRoundRect(x, y, size, size, 12, 12);
                g2.setColor(BROWN_950);
                g2.setStroke(new BasicStroke(3.2f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
                int pad = size / 5;
                for (int i = 0; i < 3; i++) {
                    int ly = y + pad + i * (size - 2 * pad) / 2;
                    g2.drawLine(x + pad, ly, x + size - pad, ly);
                }
                g2.dispose();
            }
        };
    }

    // ---------- Hero ----------
    private JPanel buildHero() {
        JPanel hero = new JPanel();
        hero.setLayout(new BoxLayout(hero, BoxLayout.Y_AXIS));
        hero.setBackground(CREAM);
        hero.setBorder(new EmptyBorder(48, 32, 24, 32));
        hero.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel eyebrow = new JLabel("  Actualizado esta semana");
        eyebrow.setFont(new Font("SansSerif", Font.BOLD, 12));
        eyebrow.setForeground(GOLD);
        eyebrow.setOpaque(true);
        eyebrow.setBackground(BROWN_950);
        eyebrow.setBorder(new EmptyBorder(6, 14, 6, 14));
        eyebrow.setAlignmentX(Component.LEFT_ALIGNMENT);
        eyebrow.setMaximumSize(eyebrow.getPreferredSize());

        JLabel h1 = new JLabel("Menu");
        h1.setFont(FONT_H1);
        h1.setForeground(BROWN_950);
        h1.setBorder(new EmptyBorder(14, 0, 8, 0));
        h1.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel p = new JLabel("<html><div style='width:480px'>Elige una categoria y descubre todo lo que preparamos hoy, desde los desayunos hasta los antojos de medianoche.</div></html>");
        p.setFont(FONT_SUB);
        p.setForeground(INK_SOFT);
        p.setAlignmentX(Component.LEFT_ALIGNMENT);

        hero.add(eyebrow);
        hero.add(h1);
        hero.add(p);
        return hero;
    }

    // ---------- Cuadricula de categorias ----------
    private JPanel buildGrid() {
        JPanel wrap = new JPanel(new BorderLayout());
        wrap.setBackground(CREAM);
        wrap.setBorder(new EmptyBorder(10, 32, 60, 32));

        String[][] items = {
            {"Desayunos", "Huevos, pan y mas"},
            {"Almuerzos y cenas", "Hamburguesas clasicas"},
            {"Postres", "Dulce final"},
            {"Bebidas", "Frias y calientes"},
            {"Antojos", "Papas y snacks"},
            {"Cajita Feliz", "Para los mas chiquitos"}
        };
        Color[] badgeColors = { MAROON, RED, GOLD_DEEP };

        JPanel grid = new JPanel(new GridLayout(0, 3, 20, 20));
        grid.setBackground(CREAM);
        for (int i = 0; i < items.length; i++) {
            grid.add(buildCard(items[i][0], items[i][1], badgeColors[i % badgeColors.length]));
        }
        wrap.add(grid, BorderLayout.CENTER);
        return wrap;
    }

    private JPanel buildCard(String label, String tag, Color badgeColor) {
        RoundedPanel card = new RoundedPanel(18, Color.WHITE, CREAM_2);
        card.setLayout(new BorderLayout(16, 0));
        card.setBorder(new EmptyBorder(22, 22, 22, 22));
        card.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

        JLabel badge = new JLabel();
        badge.setPreferredSize(new Dimension(58, 58));
        badge.setOpaque(false);
        badge.setIcon(new Icon() {
            public int getIconWidth() { return 58; }
            public int getIconHeight() { return 58; }
            public void paintIcon(Component c, Graphics g, int x, int y) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(badgeColor);
                g2.fillRoundRect(x, y, 58, 58, 14, 14);
                g2.setColor(CREAM);
                g2.setStroke(new BasicStroke(2.4f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
                g2.drawOval(x + 16, y + 16, 26, 26);
                g2.dispose();
            }
        });

        JPanel textPanel = new JPanel();
        textPanel.setOpaque(false);
        textPanel.setLayout(new BoxLayout(textPanel, BoxLayout.Y_AXIS));
        JLabel title = new JLabel(label);
        title.setFont(FONT_CARD);
        title.setForeground(BROWN_950);
        JLabel tagLbl = new JLabel(tag);
        tagLbl.setFont(FONT_TAG);
        tagLbl.setForeground(INK_SOFT);
        textPanel.add(title);
        textPanel.add(Box.createVerticalStrut(3));
        textPanel.add(tagLbl);

        card.add(badge, BorderLayout.WEST);
        card.add(textPanel, BorderLayout.CENTER);
        return card;
    }

    /** Panel con esquinas redondeadas y borde, para que las tarjetas no se vean "cuadradas y genericas". */
    static class RoundedPanel extends JPanel {
        private final int radius;
        private final Color fill;
        private final Color edge;
        RoundedPanel(int radius, Color fill, Color edge) {
            this.radius = radius;
            this.fill = fill;
            this.edge = edge;
            setOpaque(false);
        }
        @Override protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setColor(fill);
            g2.fill(new RoundRectangle2D.Float(1, 1, getWidth() - 2, getHeight() - 2, radius, radius));
            g2.setColor(edge);
            g2.setStroke(new BasicStroke(2f));
            g2.draw(new RoundRectangle2D.Float(1, 1, getWidth() - 3, getHeight() - 3, radius, radius));
            g2.dispose();
            super.paintComponent(g);
        }
    }

    // ---------- Pie de pagina ----------
    private JPanel buildFooter() {
        JPanel footer = new JPanel(new BorderLayout());
        footer.setBackground(BROWN_950);
        footer.setBorder(new EmptyBorder(16, 32, 16, 32));

        JLabel brand = new JLabel("Orgullosamente parte de Chiksx Mesoamerica", buildLogoIcon(26), SwingConstants.LEFT);
        brand.setFont(new Font("SansSerif", Font.PLAIN, 13));
        brand.setForeground(GOLD);
        brand.setIconTextGap(10);

        JPanel links = new JPanel(new FlowLayout(FlowLayout.RIGHT, 18, 0));
        links.setOpaque(false);
        for (String l : new String[]{"Proveedores", "Contacto", "Trabaja con nosotros", "Linea de Etica"}) {
            JLabel a = new JLabel(l);
            a.setFont(new Font("SansSerif", Font.BOLD, 12));
            a.setForeground(CREAM_2);
            links.add(a);
        }

        footer.add(brand, BorderLayout.WEST);
        footer.add(links, BorderLayout.EAST);
        return footer;
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try {
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            } catch (Exception ignored) { }
            new Chiksxburgermenu().setVisible(true);
        });
    }
}