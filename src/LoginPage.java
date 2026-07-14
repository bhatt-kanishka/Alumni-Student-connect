import java.awt.*;
import java.awt.event.*;
import java.awt.geom.*;
import java.sql.*;
import javax.swing.*;

public class LoginPage extends JFrame implements ActionListener {


    private String allowedRole = "all"; 

    // ── Palette ──────────────────────────────────────────────────
    static final Color YALE_BLUE     = new Color(0x00, 0x3B, 0x6B);
    static final Color YALE_HOVER    = new Color(0x00, 0x52, 0x96);
    static final Color LEMON_CHIFFON = new Color(0xFA, 0xF0, 0xCA);
    static final Color WHITE         = Color.WHITE;
    static final Color LABEL_DARK    = new Color(0x22, 0x33, 0x44);
    static final Color FIELD_BG      = new Color(0xF5, 0xF7, 0xFA);
    static final Color FIELD_BORDER  = new Color(0xC0, 0xCE, 0xDA);

    JTextField     emailField;
    JPasswordField passField;
    JButton        loginBtn;

    LoginPage() {
    this("all");
}

    LoginPage(String allowedRole) {
    this.allowedRole = allowedRole;
        setTitle("AlumniConnect – Login");
        setSize(1100, 680);
        setMinimumSize(new Dimension(900, 560));
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new GridLayout(1, 2));

        // ── LEFT PANEL ────────────────────────────────────────────
        JPanel left = new JPanel(new BorderLayout()) {
            @Override protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g;
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                                    RenderingHints.VALUE_ANTIALIAS_ON);
                GradientPaint gp = new GradientPaint(
                        0, 0,           new Color(0x00, 0x22, 0x44),
                        0, getHeight(), new Color(0x00, 0x55, 0x99));
                g2.setPaint(gp);
                g2.fillRect(0, 0, getWidth(), getHeight());

                // Decorative circles
                g2.setColor(new Color(255, 255, 255, 12));
                g2.fillOval(-60, -60, 260, 260);
                g2.fillOval(getWidth() - 140, getHeight() - 140, 240, 240);
            }
        };

        JPanel leftInner = new JPanel();
        leftInner.setLayout(new BoxLayout(leftInner, BoxLayout.Y_AXIS));
        leftInner.setOpaque(false);
        leftInner.setBorder(BorderFactory.createEmptyBorder(0, 40, 0, 40));

        // ── Load logo image ──────────────────────────────────────
        // Place AlumniConnect.jpeg in the same folder as this .java file
        int logoSize = 200;

        JLabel logoLabel = new JLabel() {
            Image logoImage;

            {
                // Load the image — try same directory, then classpath
                ImageIcon icon = new ImageIcon("AlumniConnect.jpeg");
                if (icon.getIconWidth() > 0) {
                    logoImage = icon.getImage();
                } else {
                    // fallback: try loading from class resources
                    java.net.URL url = getClass().getResource("AlumniConnect.jpeg");
                    if (url != null) logoImage = new ImageIcon(url).getImage();
                }
            }

            @Override protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                                    RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION,
                                    RenderingHints.VALUE_INTERPOLATION_BICUBIC);

                int w = getWidth(), h = getHeight();

                // White glow ring behind image
                g2.setColor(new Color(255, 255, 255, 40));
                g2.fillOval(0, 0, w, h);

                // Clip to circle and draw actual photo
                int pad = 5;
                Shape circle = new Ellipse2D.Float(pad, pad, w - pad * 2, h - pad * 2);
                g2.setClip(circle);

                if (logoImage != null) {
                    g2.drawImage(logoImage, pad, pad, w - pad * 2, h - pad * 2, null);
                } else {
                    // Fallback: draw AC monogram if image not found
                    g2.setColor(YALE_BLUE);
                    g2.fillOval(pad, pad, w - pad * 2, h - pad * 2);
                    g2.setColor(Color.WHITE);
                    g2.setFont(new Font("Georgia", Font.BOLD, 56));
                    FontMetrics fm = g2.getFontMetrics();
                    String mono = "AC";
                    g2.setClip(null);
                    g2.drawString(mono,
                            (w - fm.stringWidth(mono)) / 2,
                            (h + fm.getAscent() - fm.getDescent()) / 2);
                }

                // White border ring on top of image
                g2.setClip(null);
                g2.setColor(new Color(255, 255, 255, 180));
                g2.setStroke(new BasicStroke(3f));
                g2.drawOval(pad, pad, w - pad * 2, h - pad * 2);

                g2.dispose();
            }

            @Override public Dimension getPreferredSize() { return new Dimension(logoSize, logoSize); }
            @Override public Dimension getMaximumSize()   { return new Dimension(logoSize, logoSize); }
        };
        logoLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel appName = new JLabel("AlumniConnect");
        appName.setFont(new Font("Georgia", Font.BOLD, 28));
        appName.setForeground(Color.WHITE);
        appName.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel tagline = new JLabel("Reconnect · Grow · Inspire");
        tagline.setFont(new Font("Georgia", Font.ITALIC, 14));
        tagline.setForeground(new Color(255, 255, 255, 180));
        tagline.setAlignmentX(Component.CENTER_ALIGNMENT);

        leftInner.add(Box.createVerticalGlue());
        leftInner.add(logoLabel);
        leftInner.add(Box.createVerticalStrut(22));
        leftInner.add(appName);
        leftInner.add(Box.createVerticalStrut(10));
        leftInner.add(tagline);
        leftInner.add(Box.createVerticalGlue());

        left.add(leftInner, BorderLayout.CENTER);

        // ── RIGHT PANEL ───────────────────────────────────────────
        JPanel right = new JPanel(null) {
            @Override protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                g.setColor(LEMON_CHIFFON);
                g.fillRect(0, 0, getWidth(), getHeight());
            }
        };

        // White login card
        JPanel card = new JPanel(null) {
            @Override protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                                    RenderingHints.VALUE_ANTIALIAS_ON);
                // shadow
                g2.setColor(new Color(0, 40, 80, 18));
                g2.fillRoundRect(6, 8, getWidth() - 6, getHeight() - 6, 32, 32);
                // card
                g2.setColor(WHITE);
                g2.fillRoundRect(0, 0, getWidth() - 6, getHeight() - 6, 32, 32);
                g2.dispose();
            }
        };
        card.setOpaque(false);
        final int cw = 370, ch = 400;

        Font labelFont = new Font("Segoe UI", Font.PLAIN, 13);
        Font inputFont = new Font("Segoe UI", Font.PLAIN, 15);
        Font boldFont  = new Font("Segoe UI", Font.BOLD,  15);

        JLabel heading = new JLabel("Welcome back");
        heading.setFont(new Font("Georgia", Font.BOLD, 26));
        heading.setForeground(YALE_BLUE);
        heading.setBounds(32, 32, 306, 38);

        JLabel sub = new JLabel("Sign in to your AlumniConnect account");
        sub.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        sub.setForeground(new Color(0x88, 0x88, 0x99));
        sub.setBounds(32, 72, 306, 20);

        JLabel emailLabel = new JLabel("Email Address");
        emailLabel.setFont(labelFont);
        emailLabel.setForeground(LABEL_DARK);
        emailLabel.setBounds(32, 114, 200, 18);

        emailField = new JTextField() {
            @Override protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                                    RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(FIELD_BG);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 18, 18);
                g2.setColor(FIELD_BORDER);
                g2.setStroke(new BasicStroke(1.3f));
                g2.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 18, 18);
                g2.dispose();
                super.paintComponent(g);
            }
        };
        emailField.setBounds(32, 136, 306, 46);
        emailField.setFont(inputFont);
        emailField.setBorder(BorderFactory.createEmptyBorder(5, 14, 5, 14));
        emailField.setOpaque(false);

        JLabel passLabel = new JLabel("Password");
        passLabel.setFont(labelFont);
        passLabel.setForeground(LABEL_DARK);
        passLabel.setBounds(32, 198, 200, 18);

        passField = new JPasswordField() {
            @Override protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                                    RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(FIELD_BG);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 18, 18);
                g2.setColor(FIELD_BORDER);
                g2.setStroke(new BasicStroke(1.3f));
                g2.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 18, 18);
                g2.dispose();
                super.paintComponent(g);
            }
        };
        passField.setBounds(32, 220, 306, 46);
        passField.setFont(inputFont);
        passField.setBorder(BorderFactory.createEmptyBorder(5, 14, 5, 14));
        passField.setOpaque(false);

        // Login button with hover effect
        loginBtn = new JButton("Login") {
            boolean hovered = false;
            {
                addMouseListener(new MouseAdapter() {
                    public void mouseEntered(MouseEvent e) { hovered = true;  repaint(); }
                    public void mouseExited (MouseEvent e) { hovered = false; repaint(); }
                });
            }
            @Override protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                                    RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(hovered ? YALE_HOVER : YALE_BLUE);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 24, 24);
                g2.dispose();
                super.paintComponent(g);
            }
        };
        loginBtn.setBounds(32, 296, 306, 52);
        loginBtn.setForeground(WHITE);
        loginBtn.setFont(boldFont);
        loginBtn.setBorderPainted(false);
        loginBtn.setFocusPainted(false);
        loginBtn.setContentAreaFilled(false);
        loginBtn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        loginBtn.addActionListener(this);

        // Enter key triggers login
        passField.addKeyListener(new KeyAdapter() {
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) loginBtn.doClick();
            }
        });

        card.add(heading);
        card.add(sub);
        card.add(emailLabel);
        card.add(emailField);
        card.add(passLabel);
        card.add(passField);
        card.add(loginBtn);

        // Keep card centered on resize
        right.addComponentListener(new ComponentAdapter() {
            @Override public void componentResized(ComponentEvent e) {
                int x = (right.getWidth()  - cw) / 2;
                int y = (right.getHeight() - ch) / 2;
                card.setBounds(x, y, cw + 8, ch + 8);
            }
        });
        right.add(card);

        add(left);
        add(right);
        setVisible(true);
    }

    private String getUserName(Connection con, int userId, String role) throws Exception {
    String tableName;

    if (role.equals("student")) {
        tableName = "students";
    } else if (role.equals("alumni")) {
        tableName = "alumni";
    } else {
        tableName = "admins";
    }

    String query = "SELECT name FROM " + tableName + " WHERE user_id=?";
    PreparedStatement ps = con.prepareStatement(query);
    ps.setInt(1, userId);

    ResultSet rs = ps.executeQuery();

    if (rs.next()) {
        return rs.getString("name");
    }

    return "User";
}

 
public void actionPerformed(ActionEvent e) {
    String email = emailField.getText().trim();
    String pass  = new String(passField.getPassword());

    if (email.isEmpty() || pass.isEmpty()) {
        JOptionPane.showMessageDialog(this,
                "Please enter both email and password.",
                "Missing Fields", JOptionPane.WARNING_MESSAGE);
        return;
    }

    try {
        Connection con = DBConnection.getConnection();

        String query = "SELECT user_id, role FROM users WHERE email=? AND password=?";
        PreparedStatement ps = con.prepareStatement(query);
        ps.setString(1, email);
        ps.setString(2, pass);

        ResultSet rs = ps.executeQuery();

        if (rs.next()) {
            int userId = rs.getInt("user_id");
            String role = rs.getString("role");
            if (!allowedRole.equalsIgnoreCase("all") && !role.equalsIgnoreCase(allowedRole)) {
            JOptionPane.showMessageDialog(this,
            "You are not allowed to login from this page.",
            "Wrong Login Page",
            JOptionPane.WARNING_MESSAGE);
    return;
}
            String name = getUserName(con, userId, role);

            dispose();
            new HomePage(name, role, userId);

        } else {
            shakeWindow();
            JOptionPane.showMessageDialog(this,
                    "Incorrect email or password. Please try again.",
                    "Login Failed", JOptionPane.ERROR_MESSAGE);
        }

        con.close();

    } catch (Exception ex) {
        ex.printStackTrace();
        JOptionPane.showMessageDialog(this,
                "Database error: " + ex.getMessage(),
                "Error", JOptionPane.ERROR_MESSAGE);
    }
}

    /** Horizontal shake animation on failed login */
    private void shakeWindow() {
        Point origin = getLocation();
        int[] dx = {-8, 8, -6, 6, -4, 4, -2, 2, 0};
        Timer t = new Timer(28, null);
        int[] i = {0};
        t.addActionListener(ev -> {
            if (i[0] < dx.length) setLocation(origin.x + dx[i[0]++], origin.y);
            else { setLocation(origin); t.stop(); }
        });
        t.start();
    }

    public static void main(String[] args) {
        try {
            for (UIManager.LookAndFeelInfo lf : UIManager.getInstalledLookAndFeels())
                if ("Nimbus".equals(lf.getName())) {
                    UIManager.setLookAndFeel(lf.getClassName());
                    break;
                }
        } catch (Exception ignored) {}

        SwingUtilities.invokeLater(LoginPage::new);
    }
}