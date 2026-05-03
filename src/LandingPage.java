import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.geom.*;

/**
 * First screen shown when the app starts.
 * Three options: Student Login, Alumni Register, Admin Register.
 */
public class LandingPage extends JFrame {

    public LandingPage() {
        setTitle("AlumniConnect");
        setSize(1050, 660);
        setMinimumSize(new Dimension(860, 540));
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new GridLayout(1, 2));

        add(buildLeftPanel());
        add(buildRightPanel());
        setVisible(true);
    }

    // ── Left: Brand Panel ─────────────────────────────────────────────────────
    private JPanel buildLeftPanel() {
        JPanel left = new JPanel(new BorderLayout()) {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                // Deep blue gradient
                g2.setPaint(new GradientPaint(0, 0, Theme.BLUE_DARK, 0, getHeight(), new Color(0x00, 0x55, 0x99)));
                g2.fillRect(0, 0, getWidth(), getHeight());
                // Decorative circles
                g2.setColor(new Color(255, 255, 255, 10));
                g2.fillOval(-70, -70, 280, 280);
                g2.fillOval(getWidth() - 160, getHeight() - 160, 260, 260);
                g2.setColor(new Color(255, 255, 255, 6));
                g2.fillOval(getWidth() / 2 - 100, getHeight() / 2 - 100, 200, 200);
                g2.dispose();
            }
        };

        JPanel inner = new JPanel();
        inner.setLayout(new BoxLayout(inner, BoxLayout.Y_AXIS));
        inner.setOpaque(false);
        inner.setBorder(new EmptyBorder(0, 44, 0, 44));

        // Logo circle
        JLabel logo = buildLogoLabel();
        logo.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel appName = new JLabel("AlumniConnect");
        appName.setFont(new Font("Georgia", Font.BOLD, 30));
        appName.setForeground(Color.WHITE);
        appName.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel tagline = new JLabel("Reconnect · Grow · Inspire");
        tagline.setFont(new Font("Georgia", Font.ITALIC, 14));
        tagline.setForeground(new Color(255, 255, 255, 170));
        tagline.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Feature bullets
        JPanel bullets = new JPanel();
        bullets.setLayout(new BoxLayout(bullets, BoxLayout.Y_AXIS));
        bullets.setOpaque(false);
        bullets.setBorder(new EmptyBorder(28, 0, 0, 0));

        String[][] features = {
                { "🎓", "Students connect with real-world mentors" },
                { "🏛️", "Alumni share career opportunities" },
                { "🛡️", "Admins manage the network" },
        };
        for (String[] f : features) {
            JPanel row = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 4));
            row.setOpaque(false);
            JLabel ic = new JLabel(f[0]);
            ic.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 16));
            JLabel tx = new JLabel(f[1]);
            tx.setFont(new Font("Segoe UI", Font.PLAIN, 13));
            tx.setForeground(new Color(255, 255, 255, 190));
            row.add(ic);
            row.add(tx);
            row.setMaximumSize(new Dimension(400, 36));
            row.setAlignmentX(Component.CENTER_ALIGNMENT);
            bullets.add(row);
        }

        inner.add(Box.createVerticalGlue());
        inner.add(logo);
        inner.add(Box.createVerticalStrut(20));
        inner.add(appName);
        inner.add(Box.createVerticalStrut(8));
        inner.add(tagline);
        inner.add(bullets);
        inner.add(Box.createVerticalGlue());

        left.add(inner, BorderLayout.CENTER);

        // Bottom tagline bar
        JPanel bottomBar = new JPanel();
        bottomBar.setOpaque(false);
        JLabel version = new JLabel("v1.0  ·  University Edition");
        version.setFont(new Font("Segoe UI", Font.PLAIN, 10));
        version.setForeground(new Color(255, 255, 255, 100));
        bottomBar.add(version);
        left.add(bottomBar, BorderLayout.SOUTH);

        return left;
    }

    // ── Right: Options Panel ──────────────────────────────────────────────────
    private JPanel buildRightPanel() {
        JPanel right = new JPanel(new GridBagLayout()) {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                g.setColor(Theme.BEIGE);
                g.fillRect(0, 0, getWidth(), getHeight());
            }
        };

        JPanel card = new JPanel();
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setBackground(Theme.WHITE);
        card.setBorder(new EmptyBorder(44, 52, 44, 52));

        // Round card wrapper
        JPanel cardWrapper = new JPanel(new BorderLayout()) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(new Color(0, 50, 100, 16));
                g2.fillRoundRect(6, 8, getWidth() - 6, getHeight() - 6, 28, 28);
                g2.setColor(Theme.WHITE);
                g2.fillRoundRect(0, 0, getWidth() - 6, getHeight() - 6, 28, 28);
                g2.dispose();
            }
        };
        cardWrapper.setOpaque(false);
        cardWrapper.setPreferredSize(new Dimension(400, 420));

        // Heading
        JLabel heading = new JLabel("Get Started");
        heading.setFont(new Font("Georgia", Font.BOLD, 28));
        heading.setForeground(Theme.ROYAL_BLUE);
        heading.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel sub = new JLabel("Choose how you'd like to continue");
        sub.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        sub.setForeground(Theme.TEXT_MUTED);
        sub.setAlignmentX(Component.CENTER_ALIGNMENT);

        // ── Student Login button ──────────────────────────────────────────────
        JButton studentBtn = buildBigButton(
                "🎓  Student Login",
                "Already registered? Sign in here",
                Theme.ROYAL_BLUE, new Color(0x00, 0x5B, 0xA4)
        );
        studentBtn.addActionListener(e -> {
            dispose();
            new LoginPage("student");
        });

        // ── Alumni Register button ────────────────────────────────────────────
        JButton alumniBtn = buildBigButton(
                "🏛️  Alumni Register",
                "Create an alumni account",
                new Color(0x00, 0x65, 0x6F), new Color(0x00, 0x80, 0x8A)
        );
        alumniBtn.addActionListener(e -> {
            dispose();
            new AlumniRegisterPage();
        });

        // ── Admin Register button ─────────────────────────────────────────────
        JButton adminBtn = buildBigButton(
                "🛡️  Admin Register",
                "Set up an admin account",
                Theme.ADMIN_MID, new Color(0xA0, 0x20, 0x20)
        );
        adminBtn.addActionListener(e -> {
            dispose();
            new AdminRegisterPage();
        });

        card.add(heading);
        card.add(Box.createVerticalStrut(6));
        card.add(sub);
        card.add(Box.createVerticalStrut(28));
        card.add(studentBtn);
        card.add(Box.createVerticalStrut(14));
        card.add(alumniBtn);
        card.add(Box.createVerticalStrut(14));
        card.add(adminBtn);

        cardWrapper.add(card, BorderLayout.CENTER);
        right.add(cardWrapper);
        return right;
    }

    // ── Big action button ─────────────────────────────────────────────────────
    private JButton buildBigButton(String title, String subtitle, Color from, Color to) {
        JButton btn = new JButton() {
            boolean hov = false;
            {
                addMouseListener(new java.awt.event.MouseAdapter() {
                    public void mouseEntered(java.awt.event.MouseEvent e) { hov = true;  repaint(); }
                    public void mouseExited (java.awt.event.MouseEvent e) { hov = false; repaint(); }
                });
            }

            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                // Gradient fill
                Color f = hov ? to   : from;
                Color t = hov ? from : to;
                g2.setPaint(new GradientPaint(0, 0, f, getWidth(), 0, t));
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 18, 18);
                // Shine overlay
                g2.setColor(new Color(255, 255, 255, hov ? 30 : 14));
                g2.fillRoundRect(0, 0, getWidth(), getHeight() / 2, 18, 18);
                g2.dispose();

                // Draw text ourselves (two lines)
                FontMetrics fm1 = g.getFontMetrics(new Font("Segoe UI", Font.BOLD, 15));
                FontMetrics fm2 = g.getFontMetrics(new Font("Segoe UI", Font.PLAIN, 11));
                int totalH = fm1.getHeight() + 2 + fm2.getHeight();
                int startY = (getHeight() - totalH) / 2 + fm1.getAscent();
                g.setColor(Color.WHITE);
                g.setFont(new Font("Segoe UI", Font.BOLD, 15));
                g.drawString(title, (getWidth() - fm1.stringWidth(title)) / 2, startY);
                g.setFont(new Font("Segoe UI", Font.PLAIN, 11));
                g.setColor(new Color(255, 255, 255, 190));
                g.drawString(subtitle, (getWidth() - fm2.stringWidth(subtitle)) / 2,
                        startY + fm1.getDescent() + 4 + fm2.getAscent());
            }
        };
        btn.setPreferredSize(new Dimension(300, 68));
        btn.setMaximumSize(new Dimension(Integer.MAX_VALUE, 68));
        btn.setBorderPainted(false);
        btn.setContentAreaFilled(false);
        btn.setFocusPainted(false);
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btn.setAlignmentX(Component.CENTER_ALIGNMENT);
        return btn;
    }

    // ── Logo circle ───────────────────────────────────────────────────────────
    private JLabel buildLogoLabel() {
        final int S = 100;
        JLabel lbl = new JLabel() {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                // Try to load image
                ImageIcon icon = new ImageIcon("AlumniConnect.jpeg");
                Image img = icon.getIconWidth() > 0 ? icon.getImage() : null;
                if (img == null) {
                    java.net.URL url = getClass().getResource("AlumniConnect.jpeg");
                    if (url != null) img = new ImageIcon(url).getImage();
                }
                int p = 4;
                g2.setColor(new Color(255, 255, 255, 35));
                g2.fillOval(0, 0, S, S);
                // clip circle
                g2.setClip(new Ellipse2D.Float(p, p, S - p * 2, S - p * 2));
                if (img != null) {
                    g2.drawImage(img, p, p, S - p * 2, S - p * 2, null);
                } else {
                    g2.setColor(Theme.ROYAL_BLUE);
                    g2.fillOval(p, p, S - p * 2, S - p * 2);
                    g2.setColor(Color.WHITE);
                    g2.setFont(new Font("Georgia", Font.BOLD, 28));
                    FontMetrics fm = g2.getFontMetrics();
                    g2.setClip(null);
                    g2.drawString("AC", (S - fm.stringWidth("AC")) / 2,
                            (S + fm.getAscent() - fm.getDescent()) / 2);
                }
                g2.setClip(null);
                g2.setColor(new Color(255, 255, 255, 160));
                g2.setStroke(new BasicStroke(2.5f));
                g2.drawOval(p, p, S - p * 2, S - p * 2);
                g2.dispose();
            }

            @Override public Dimension getPreferredSize() { return new Dimension(S, S); }
            @Override public Dimension getMaximumSize()   { return new Dimension(S, S); }
        };
        return lbl;
    }

    public static void main(String[] args) {
        Theme.applyNimbus();
        SwingUtilities.invokeLater(LandingPage::new);
    }
}