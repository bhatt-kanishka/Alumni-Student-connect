import java.awt.*;
import java.awt.event.*;
import java.sql.*;
import javax.swing.*;
import javax.swing.border.*;

public class StudentProfilePage extends JFrame {

    private static final Color ROYAL_BLUE  = new Color(0x00, 0x3B, 0x6B);
    private static final Color BEIGE       = new Color(0xFA, 0xF0, 0xCA);
    private static final Color BG          = new Color(0xF5, 0xF7, 0xFA);
    private static final Color WHITE       = Color.WHITE;
    private static final Color TEXT_DARK   = new Color(0x1A, 0x1A, 0x2E);
    private static final Color TEXT_MUTED  = new Color(0x6B, 0x7A, 0x99);
    private static final Color ACCENT      = new Color(0x00, 0x8B, 0xD4);

    private final int userId;

    public StudentProfilePage(int userId) {
        this.userId = userId;
        initUI();
    }

    private void initUI() {
        setTitle("AlumniConnect – My Profile");
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

        StudentProfile profile = fetchProfile();

        JPanel card = buildProfileCard(profile);
        content.add(card, new GridBagConstraints());

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

        JButton homeBtn = navButton("Home");
        homeBtn.addActionListener(e -> {
            dispose();
            new HomePage(profileNameOnly(), "student", userId);
        });

        JButton logoutBtn = navButton("Logout");
        logoutBtn.addActionListener(e -> {
            dispose();
            new LoginPage();
        });

        btnPanel.add(dashboardBtn);
        btnPanel.add(homeBtn);
        btnPanel.add(logoutBtn);

        nav.add(btnPanel, BorderLayout.EAST);
        return nav;
    }

    private JButton navButton(String text) {
        JButton btn = new JButton(text);
        btn.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        btn.setForeground(WHITE);
        btn.setBackground(new Color(255, 255, 255, 40));
        btn.setBorder(new EmptyBorder(6, 14, 6, 14));
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btn.setFocusPainted(false);
        btn.setOpaque(false);
        btn.setContentAreaFilled(false);

        btn.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) {
                btn.setForeground(BEIGE);
            }

            public void mouseExited(MouseEvent e) {
                btn.setForeground(WHITE);
            }
        });

        return btn;
    }

    private JPanel buildProfileCard(StudentProfile p) {
        JPanel card = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);

                Graphics2D g2 = (Graphics2D) g;
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                g2.setColor(WHITE);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 20, 20);

                g2.setColor(new Color(0, 0, 0, 18));
                g2.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 20, 20);
            }
        };

        card.setOpaque(false);
        card.setLayout(new BorderLayout(0, 0));
        card.setPreferredSize(new Dimension(680, 500));

        JPanel header = new JPanel(null);
        header.setBackground(ROYAL_BLUE);
        header.setPreferredSize(new Dimension(0, 120));

        JPanel strip = new JPanel();
        strip.setBackground(BEIGE);
        strip.setBounds(0, 110, 680, 10);
        header.add(strip);

        JLabel headerText = new JLabel("Student Profile");
        headerText.setFont(new Font("Georgia", Font.BOLD, 24));
        headerText.setForeground(WHITE);
        headerText.setBounds(40, 30, 300, 32);
        header.add(headerText);

        JLabel headerSub = new JLabel("View your academic and personal details");
        headerSub.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        headerSub.setForeground(new Color(255, 255, 255, 190));
        headerSub.setBounds(40, 62, 360, 22);
        header.add(headerSub);

        card.add(header, BorderLayout.NORTH);

        String initials = getInitials(p.name);
        AvatarPanel avatar = new AvatarPanel(initials, 86);

        JPanel avatarWrapper = new JPanel(null);
        avatarWrapper.setOpaque(false);
        avatarWrapper.setPreferredSize(new Dimension(0, 60));
        avatar.setBounds(40, -43, 86, 86);
        avatarWrapper.add(avatar);

        JPanel body = new JPanel();
        body.setOpaque(false);
        body.setLayout(new BoxLayout(body, BoxLayout.Y_AXIS));
        body.setBorder(new EmptyBorder(0, 40, 30, 40));

        body.add(avatarWrapper);

        JPanel nameRow = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        nameRow.setOpaque(false);

        JLabel nameLabel = new JLabel(p.name);
        nameLabel.setFont(new Font("Segoe UI", Font.BOLD, 22));
        nameLabel.setForeground(TEXT_DARK);

        JLabel roleLabel = new JLabel("  •  Student");
        roleLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        roleLabel.setForeground(ACCENT);

        nameRow.add(nameLabel);
        nameRow.add(roleLabel);
        body.add(nameRow);

        body.add(Box.createVerticalStrut(6));

        JLabel emailLabel = new JLabel(p.email);
        emailLabel.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        emailLabel.setForeground(TEXT_MUTED);
        body.add(emailLabel);

        body.add(Box.createVerticalStrut(20));

        JPanel grid = new JPanel(new GridLayout(0, 2, 16, 12));
        grid.setOpaque(false);

        addInfoCard(grid, "🎓 Course", p.course);
        addInfoCard(grid, "📚 Specialization", p.specialization);
        addInfoCard(grid, "📅 Batch", p.batch);
        addInfoCard(grid, "🔢 SAP ID", p.sapId);
        addInfoCard(grid, "📧 Email", p.email);
        addInfoCard(grid, "🏷 User ID", "#" + userId);

        body.add(grid);

        body.add(Box.createVerticalStrut(20));

        JLabel bioTitle = new JLabel("About");
        bioTitle.setFont(new Font("Segoe UI", Font.BOLD, 14));
        bioTitle.setForeground(ROYAL_BLUE);
        body.add(bioTitle);

        body.add(Box.createVerticalStrut(6));

        String bioText = p.bio == null || p.bio.trim().isEmpty()
                ? "No bio added yet. Update your profile to tell others about yourself."
                : p.bio;

        JTextArea bio = new JTextArea(bioText);
        bio.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        bio.setForeground(TEXT_MUTED);
        bio.setBackground(new Color(0xF0, 0xF4, 0xFF));
        bio.setLineWrap(true);
        bio.setWrapStyleWord(true);
        bio.setEditable(false);
        bio.setBorder(new CompoundBorder(
                new LineBorder(new Color(0xD0, 0xDA, 0xF0), 1, true),
                new EmptyBorder(10, 12, 10, 12)
        ));
        bio.setMaximumSize(new Dimension(Integer.MAX_VALUE, 70));
        bio.setPreferredSize(new Dimension(600, 60));

        body.add(bio);

        card.add(body, BorderLayout.CENTER);
        return card;
    }

    private void addInfoCard(JPanel parent, String label, String value) {
        JPanel chip = new JPanel(new BorderLayout(0, 2));
        chip.setBackground(new Color(0xF0, 0xF4, 0xFF));
        chip.setBorder(new CompoundBorder(
                new LineBorder(new Color(0xD0, 0xDA, 0xF0), 1, true),
                new EmptyBorder(8, 12, 8, 12)
        ));

        JLabel lbl = new JLabel(label);
        lbl.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        lbl.setForeground(TEXT_MUTED);

        JLabel val = new JLabel(value == null || value.trim().isEmpty() ? "—" : value);
        val.setFont(new Font("Segoe UI", Font.BOLD, 13));
        val.setForeground(TEXT_DARK);

        chip.add(lbl, BorderLayout.NORTH);
        chip.add(val, BorderLayout.CENTER);

        parent.add(chip);
    }

    private StudentProfile fetchProfile() {
        StudentProfile p = new StudentProfile();

        try {
            Connection con = DBConnection.getConnection();

            String query =
                    "SELECT s.name, u.email, s.sap_id, s.course, s.batch, s.specialization " +
                    "FROM students s " +
                    "JOIN users u ON s.user_id = u.user_id " +
                    "WHERE s.user_id = ?";

            PreparedStatement ps = con.prepareStatement(query);
            ps.setInt(1, userId);

            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                p.name = rs.getString("name");
                p.email = rs.getString("email");
                p.sapId = rs.getString("sap_id");
                p.course = rs.getString("course");
                p.batch = rs.getString("batch");
                p.specialization = rs.getString("specialization");
                p.bio = "I am a student of " + p.course + " specializing in " + p.specialization + ".";
            } else {
                return fallbackProfile();
            }

            rs.close();
            ps.close();
            con.close();

        } catch (Exception ex) {
            ex.printStackTrace();
            return fallbackProfile();
        }

        return p;
    }

    private StudentProfile fallbackProfile() {
        StudentProfile p = new StudentProfile();
        p.name = "Student User";
        p.email = "student@alumniconnect.com";
        p.sapId = "—";
        p.course = "B.Tech CSE";
        p.batch = "2026";
        p.specialization = "Computer Science";
        p.bio = "No bio added yet. Update your profile to tell others about yourself.";
        return p;
    }

    private String profileNameOnly() {
        return fetchProfile().name;
    }

    private String getInitials(String name) {
        if (name == null || name.trim().isEmpty()) return "S";

        String[] parts = name.trim().split("\\s+");
        StringBuilder sb = new StringBuilder();

        for (int i = 0; i < Math.min(2, parts.length); i++) {
            sb.append(Character.toUpperCase(parts[i].charAt(0)));
        }

        return sb.toString();
    }

    static class AvatarPanel extends JPanel {
        private final String initials;
        private final int size;

        AvatarPanel(String initials, int size) {
            this.initials = initials;
            this.size = size;
            setPreferredSize(new Dimension(size, size));
            setOpaque(false);
        }

        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            g2.setColor(new Color(0, 0, 0, 40));
            g2.fillOval(3, 4, size - 2, size - 2);

            g2.setColor(Color.WHITE);
            g2.fillOval(0, 0, size, size);

            g2.setColor(ROYAL_BLUE);
            g2.fillOval(3, 3, size - 6, size - 6);

            g2.setColor(Color.WHITE);
            g2.setFont(new Font("Segoe UI", Font.BOLD, size / 3));

            FontMetrics fm = g2.getFontMetrics();
            int tx = (size - fm.stringWidth(initials)) / 2;
            int ty = (size + fm.getAscent() - fm.getDescent()) / 2;

            g2.drawString(initials, tx, ty);
            g2.dispose();
        }
    }

    static class StudentProfile {
        String name;
        String email;
        String sapId;
        String course;
        String batch;
        String specialization;
        String bio;
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new StudentProfilePage(1));
    }
}