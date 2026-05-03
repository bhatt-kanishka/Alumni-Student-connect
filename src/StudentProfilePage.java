import java.awt.*;
import javax.swing.*;
import javax.swing.border.*;

public class StudentProfilePage extends JFrame {

    private static final Color ROYAL_BLUE = new Color(0x003B6B);
    private static final Color BEIGE = new Color(0xFAF0CA);
    private static final Color BG = new Color(0xF5F7FA);
    private static final Color WHITE = Color.WHITE;
    private static final Color TEXT_DARK = new Color(0x1A1A2E);
    private static final Color TEXT_MUTED = new Color(0x6B7A99);
    private static final Color ACCENT = new Color(0x008BD4);

    private final int userId;

    public StudentProfilePage(int userId) {
        this.userId = userId;
        initUI();
    }

    private void initUI() {
        setTitle("AlumniConnect - Student Profile");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(780, 640);
        setLocationRelativeTo(null);
        setResizable(false);

        JPanel root = new JPanel(new BorderLayout());
        root.setBackground(BG);

        root.add(buildNavBar(), BorderLayout.NORTH);

        JPanel content = new JPanel(new GridBagLayout());
        content.setBackground(BG);
        content.setBorder(new EmptyBorder(30, 40, 30, 40));

        JPanel card = buildProfileCard();
        content.add(card);

        root.add(content, BorderLayout.CENTER);
        setContentPane(root);
        setVisible(true);
    }

    private JPanel buildNavBar() {
        JPanel nav = new JPanel(new BorderLayout());
        nav.setBackground(ROYAL_BLUE);
        nav.setPreferredSize(new Dimension(0, 56));
        nav.setBorder(new EmptyBorder(0, 24, 0, 24));

        JLabel logo = new JLabel("AlumniConnect");
        logo.setFont(new Font("Segoe UI", Font.BOLD, 20));
        logo.setForeground(WHITE);
        nav.add(logo, BorderLayout.WEST);

        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 8));
        btnPanel.setOpaque(false);

        JButton dashboardBtn = navButton("← Dashboard");
        dashboardBtn.addActionListener(e -> {
            dispose();
            new StudentDashboard(userId);
        });

        JButton logoutBtn = navButton("Logout");
        logoutBtn.addActionListener(e -> {
            dispose();
            new LoginPage();
        });

        btnPanel.add(dashboardBtn);
        btnPanel.add(logoutBtn);
        nav.add(btnPanel, BorderLayout.EAST);

        return nav;
    }

    private JButton navButton(String text) {
        JButton btn = new JButton(text);
        btn.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        btn.setForeground(WHITE);
        btn.setBorder(new EmptyBorder(6, 14, 6, 14));
        btn.setFocusPainted(false);
        btn.setContentAreaFilled(false);
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        return btn;
    }

    private JPanel buildProfileCard() {
        JPanel card = new JPanel(new BorderLayout()) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                g2.setColor(WHITE);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 20, 20);

                g2.setColor(new Color(0, 0, 0, 20));
                g2.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 20, 20);

                g2.dispose();
                super.paintComponent(g);
            }
        };

        card.setOpaque(false);
        card.setPreferredSize(new Dimension(680, 480));

        JPanel header = new JPanel(null);
        header.setBackground(ROYAL_BLUE);
        header.setPreferredSize(new Dimension(0, 120));

        JPanel strip = new JPanel();
        strip.setBackground(BEIGE);
        strip.setBounds(0, 110, 680, 10);
        header.add(strip);

        card.add(header, BorderLayout.NORTH);

        JPanel body = new JPanel();
        body.setOpaque(false);
        body.setLayout(new BoxLayout(body, BoxLayout.Y_AXIS));
        body.setBorder(new EmptyBorder(30, 40, 30, 40));

        JLabel avatar = new JLabel("S", SwingConstants.CENTER);
        avatar.setFont(new Font("Segoe UI", Font.BOLD, 30));
        avatar.setForeground(WHITE);
        avatar.setOpaque(true);
        avatar.setBackground(ROYAL_BLUE);
        avatar.setMaximumSize(new Dimension(80, 80));
        avatar.setPreferredSize(new Dimension(80, 80));
        avatar.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel nameLabel = new JLabel("Student User");
        nameLabel.setFont(new Font("Segoe UI", Font.BOLD, 24));
        nameLabel.setForeground(TEXT_DARK);
        nameLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel roleLabel = new JLabel("Student Profile");
        roleLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        roleLabel.setForeground(ACCENT);
        roleLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

        body.add(avatar);
        body.add(Box.createVerticalStrut(15));
        body.add(nameLabel);
        body.add(Box.createVerticalStrut(5));
        body.add(roleLabel);
        body.add(Box.createVerticalStrut(25));

        JPanel grid = new JPanel(new GridLayout(0, 2, 16, 12));
        grid.setOpaque(false);
        grid.setAlignmentX(Component.LEFT_ALIGNMENT);

        addInfoCard(grid, "🏷 User ID", String.valueOf(userId));
        addInfoCard(grid, "🎓 Course", "B.Tech CSE");
        addInfoCard(grid, "📚 Specialization", "Computer Science");
        addInfoCard(grid, "📅 Batch", "2026");

        body.add(grid);
        body.add(Box.createVerticalStrut(25));

        JLabel about = new JLabel("About");
        about.setFont(new Font("Segoe UI", Font.BOLD, 14));
        about.setForeground(ROYAL_BLUE);
        about.setAlignmentX(Component.LEFT_ALIGNMENT);
        body.add(about);

        JTextArea bio = new JTextArea("No bio added yet. Update your profile later.");
        bio.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        bio.setForeground(TEXT_MUTED);
        bio.setBackground(new Color(0xF0F4FF));
        bio.setLineWrap(true);
        bio.setWrapStyleWord(true);
        bio.setEditable(false);
        bio.setBorder(new EmptyBorder(10, 12, 10, 12));
        bio.setMaximumSize(new Dimension(Integer.MAX_VALUE, 70));
        body.add(Box.createVerticalStrut(8));
        body.add(bio);

        card.add(body, BorderLayout.CENTER);
        return card;
    }

    private void addInfoCard(JPanel parent, String label, String value) {
        JPanel chip = new JPanel(new BorderLayout(0, 2));
        chip.setBackground(new Color(0xF0F4FF));
        chip.setBorder(new CompoundBorder(
                new LineBorder(new Color(0xD0DAF0), 1, true),
                new EmptyBorder(8, 12, 8, 12)
        ));

        JLabel lbl = new JLabel(label);
        lbl.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        lbl.setForeground(TEXT_MUTED);

        JLabel val = new JLabel(value);
        val.setFont(new Font("Segoe UI", Font.BOLD, 13));
        val.setForeground(TEXT_DARK);

        chip.add(lbl, BorderLayout.NORTH);
        chip.add(val, BorderLayout.CENTER);

        parent.add(chip);
    }
}