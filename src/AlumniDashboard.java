import dao.UserDAO;
import model.User;

import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.awt.event.*;

/**
 * AlumniDashboard – Main dashboard for alumni users.
 * Constructor: AlumniDashboard(int userId)
 */
public class AlumniDashboard extends JFrame {

    // ── Palette ──────────────────────────────────────────────────────────────
    private static final Color ROYAL_BLUE = new Color(0x00, 0x3B, 0x6B);
    private static final Color BEIGE      = new Color(0xFA, 0xF0, 0xCA);
    private static final Color BG         = new Color(0xF5, 0xF7, 0xFA);
    private static final Color WHITE      = Color.WHITE;
    private static final Color TEXT_DARK  = new Color(0x1A, 0x1A, 0x2E);
    private static final Color TEXT_MUTED = new Color(0x6B, 0x7A, 0x99);
    private static final Color ACCENT     = new Color(0x00, 0x8B, 0xD4);
    private static final Color GREEN      = new Color(0x2E, 0xCC, 0x71);

    private final int userId;
    private String alumniName = "Alumni";
    private JPanel contentArea;

    public AlumniDashboard(int userId) {
        this.userId = userId;
        loadAlumniName();
        initUI();
    }

    /** No-arg constructor for compatibility with LoginPage calling new AlumniDashboard() */
    public AlumniDashboard() {
        this(0);
    }

    private void loadAlumniName() {
        try {
            UserDAO dao = new UserDAO();
            AlumniProfile p = dao.getAlumniByUserId(userId);
            if (p != null && p.getName() != null) alumniName = p.getName();
        } catch (Exception ignored) {}
    }

    private void initUI() {
        setTitle("AlumniConnect – Alumni Dashboard");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1000, 660);
        setLocationRelativeTo(null);
        setResizable(false);

        JPanel root = new JPanel(new BorderLayout());
        root.setBackground(BG);

        root.add(buildNavBar(), BorderLayout.NORTH);

        // Sidebar + Content split
        JSplitPane split = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT,
                buildSidebar(), buildMainContent());
        split.setDividerSize(0);
        split.setEnabled(false);
        split.setDividerLocation(220);
        split.setBorder(null);

        root.add(split, BorderLayout.CENTER);
        setContentPane(root);
        setVisible(true);
    }

    // ── Navigation Bar ─────────────────────────────────────────────────────
    private JPanel buildNavBar() {
        JPanel nav = new JPanel(new BorderLayout());
        nav.setBackground(ROYAL_BLUE);
        nav.setPreferredSize(new Dimension(0, 56));
        nav.setBorder(new EmptyBorder(0, 24, 0, 24));

        JLabel logo = new JLabel("AlumniConnect");
        logo.setFont(new Font("Segoe UI", Font.BOLD, 20));
        logo.setForeground(WHITE);
        nav.add(logo, BorderLayout.WEST);

        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 8, 0));
        btnPanel.setOpaque(false);

        String[] navItems = {"Profile", "Explore", "Messages", "Logout"};
        for (String item : navItems) {
            JButton btn = navButton(item);
            btn.addActionListener(e -> handleNavClick(item));
            btnPanel.add(btn);
        }
        nav.add(btnPanel, BorderLayout.EAST);
        return nav;
    }

    private void handleNavClick(String item) {
        switch (item) {
            case "Profile":
                String[] data = new dao.UserDAO().getAlumniProfile(userId);
                JOptionPane.showMessageDialog(this,
                    "Name: " + data[0] + "\nCompany: " + data[1] + "\nBio: " + data[2],
                    "My Profile", JOptionPane.INFORMATION_MESSAGE);
                break;
            case "Explore":
                showExploreStudents();
                break;
            case "Messages":
                showMessages();
                break;
            case "Logout":
                int confirm = JOptionPane.showConfirmDialog(this,
                        "Are you sure you want to logout?", "Logout",
                        JOptionPane.YES_NO_OPTION);
                if (confirm == JOptionPane.YES_OPTION) {
                    dispose();
                    new LoginPage("alumni");
                }
                break;
        }
    }

    private JButton navButton(String text) {
        JButton btn = new JButton(text);
        btn.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        btn.setForeground(WHITE);
        btn.setBorder(new EmptyBorder(6, 14, 6, 14));
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btn.setFocusPainted(false);
        btn.setOpaque(false);
        btn.setContentAreaFilled(false);
        btn.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) { btn.setForeground(BEIGE); }
            public void mouseExited(MouseEvent e)  { btn.setForeground(WHITE); }
        });
        return btn;
    }

    // ── Sidebar ────────────────────────────────────────────────────────────
    private JPanel buildSidebar() {
        JPanel sidebar = new JPanel();
        sidebar.setBackground(WHITE);
        sidebar.setLayout(new BoxLayout(sidebar, BoxLayout.Y_AXIS));
        sidebar.setBorder(new EmptyBorder(24, 16, 24, 16));
        sidebar.setPreferredSize(new Dimension(220, 0));

        // Avatar
        StudentProfilePage.AvatarPanel avatar =
                new StudentProfilePage.AvatarPanel(getInitials(alumniName), 64);
        avatar.setAlignmentX(Component.CENTER_ALIGNMENT);
        avatar.setMaximumSize(new Dimension(64, 64));
        sidebar.add(avatar);

        sidebar.add(Box.createVerticalStrut(12));

        JLabel nameLabel = new JLabel(alumniName);
        nameLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
        nameLabel.setForeground(TEXT_DARK);
        nameLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        sidebar.add(nameLabel);

        JLabel roleLabel = new JLabel("Alumni");
        roleLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        roleLabel.setForeground(ACCENT);
        roleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        sidebar.add(roleLabel);

        sidebar.add(Box.createVerticalStrut(24));
        sidebar.add(new JSeparator());
        sidebar.add(Box.createVerticalStrut(16));

        // Menu items
        String[][] menus = {
            {"🏠", "Home"},
            {"🎓", "Explore Students"},
            {"📋", "Post Internship"},
            {"💬", "Messages"},
            {"👤", "My Profile"},
        };
        for (String[] m : menus) {
            sidebar.add(sidebarItem(m[0] + "  " + m[1]));
            sidebar.add(Box.createVerticalStrut(4));
        }

        sidebar.add(Box.createVerticalGlue());
        return sidebar;
    }

    private JButton sidebarItem(String label) {
        JButton btn = new JButton(label);
        btn.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        btn.setForeground(TEXT_DARK);
        btn.setBackground(BG);
        btn.setHorizontalAlignment(SwingConstants.LEFT);
        btn.setBorder(new EmptyBorder(10, 12, 10, 12));
        btn.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btn.setFocusPainted(false);
        btn.setContentAreaFilled(false);
        btn.setOpaque(true);
        btn.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) { btn.setBackground(BEIGE); }
            public void mouseExited(MouseEvent e)  { btn.setBackground(BG); }
        });
        return btn;
    }

    // ── Main Content ───────────────────────────────────────────────────────
    private JPanel buildMainContent() {
        contentArea = new JPanel(new BorderLayout());
        contentArea.setBackground(BG);
        showHomeContent();
        return contentArea;
    }

    private void showHomeContent() {
        contentArea.removeAll();
        JPanel panel = new JPanel();
        panel.setBackground(BG);
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBorder(new EmptyBorder(30, 30, 30, 30));

        // Welcome banner
        JPanel banner = new JPanel(new BorderLayout());
        banner.setBackground(ROYAL_BLUE);
        banner.setBorder(new EmptyBorder(24, 28, 24, 28));
        banner.setMaximumSize(new Dimension(Integer.MAX_VALUE, 90));

        JLabel welcome = new JLabel("Welcome back, " + alumniName + "! 👋");
        welcome.setFont(new Font("Segoe UI", Font.BOLD, 22));
        welcome.setForeground(WHITE);
        banner.add(welcome, BorderLayout.WEST);

        JLabel sub = new JLabel("Give back, connect, inspire.");
        sub.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        sub.setForeground(BEIGE);
        banner.add(sub, BorderLayout.SOUTH);
        JPanel bannerWrapper = roundedWrapper(banner, 14);
        panel.add(bannerWrapper);
        panel.add(Box.createVerticalStrut(24));

        // Stats row
        JPanel statsRow = new JPanel(new GridLayout(1, 3, 16, 0));
        statsRow.setOpaque(false);
        statsRow.setMaximumSize(new Dimension(Integer.MAX_VALUE, 90));
        statsRow.add(statCard("👥 Students", "Explore Profiles", ACCENT));
        statsRow.add(statCard("📋 Internships", "Post Opportunities", GREEN));
        statsRow.add(statCard("💬 Messages", "Connect & Mentor", new Color(0xE6, 0x7E, 0x22)));
        panel.add(statsRow);
        panel.add(Box.createVerticalStrut(24));

        // Quick actions
        JLabel actTitle = new JLabel("Quick Actions");
        actTitle.setFont(new Font("Segoe UI", Font.BOLD, 16));
        actTitle.setForeground(TEXT_DARK);
        actTitle.setAlignmentX(Component.LEFT_ALIGNMENT);
        panel.add(actTitle);
        panel.add(Box.createVerticalStrut(12));

        JPanel actions = new JPanel(new GridLayout(1, 2, 16, 0));
        actions.setOpaque(false);
        actions.setMaximumSize(new Dimension(Integer.MAX_VALUE, 120));
        actions.add(actionCard("🎓 Explore Students",
                "Browse student profiles and connect.", ACCENT,
                e -> showExploreStudents()));
        actions.add(actionCard("📋 Post Internship",
                "Share opportunities with students.", GREEN,
                e -> showPostInternship()));
        panel.add(actions);

        JScrollPane scroll = new JScrollPane(panel);
        scroll.setBorder(null);
        scroll.getVerticalScrollBar().setUnitIncrement(12);
        contentArea.add(scroll, BorderLayout.CENTER);
        contentArea.revalidate();
        contentArea.repaint();
    }

    private void showExploreStudents() {
        contentArea.removeAll();
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(BG);
        panel.setBorder(new EmptyBorder(24, 24, 24, 24));

        JLabel title = new JLabel("Explore Students");
        title.setFont(new Font("Segoe UI", Font.BOLD, 20));
        title.setForeground(ROYAL_BLUE);
        panel.add(title, BorderLayout.NORTH);

        // Placeholder table
        String[] cols = {"Name", "Course", "Batch", "Specialization", "Action"};
        Object[][] data = {
            {"Rahul Sharma", "B.Tech", "2024", "CSE", "View Profile"},
            {"Priya Singh",  "MCA",    "2023", "Data Science", "View Profile"},
            {"Amit Kumar",   "B.Tech", "2025", "ECE", "View Profile"},
        };
        JTable table = new JTable(data, cols);
        table.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        table.setRowHeight(32);
        table.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 13));
        table.getTableHeader().setBackground(ROYAL_BLUE);
        table.getTableHeader().setForeground(WHITE);
        table.setSelectionBackground(new Color(0xE3, 0xF0, 0xFF));
        table.setGridColor(new Color(0xE0, 0xE8, 0xF0));

        JScrollPane sp = new JScrollPane(table);
        sp.setBorder(new LineBorder(new Color(0xD0, 0xDA, 0xF0), 1, true));
        panel.add(sp, BorderLayout.CENTER);

        JLabel note = new JLabel("  Showing sample data. Connect DB to load live student records.");
        note.setFont(new Font("Segoe UI", Font.ITALIC, 11));
        note.setForeground(TEXT_MUTED);
        panel.add(note, BorderLayout.SOUTH);

        contentArea.add(panel, BorderLayout.CENTER);
        contentArea.revalidate();
        contentArea.repaint();
    }

    private void showPostInternship() {
        contentArea.removeAll();
        JPanel panel = new JPanel();
        panel.setBackground(BG);
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBorder(new EmptyBorder(30, 40, 30, 40));

        JLabel title = new JLabel("Post an Internship");
        title.setFont(new Font("Segoe UI", Font.BOLD, 20));
        title.setForeground(ROYAL_BLUE);
        title.setAlignmentX(Component.LEFT_ALIGNMENT);
        panel.add(title);
        panel.add(Box.createVerticalStrut(20));

        String[] fields = {"Company Name", "Role / Position", "Location",
                           "Duration", "Stipend (per month)", "Apply Link"};
        for (String f : fields) {
            JLabel lbl = new JLabel(f);
            lbl.setFont(new Font("Segoe UI", Font.BOLD, 13));
            lbl.setForeground(TEXT_DARK);
            lbl.setAlignmentX(Component.LEFT_ALIGNMENT);
            panel.add(lbl);
            panel.add(Box.createVerticalStrut(4));

            JTextField tf = new JTextField();
            tf.setFont(new Font("Segoe UI", Font.PLAIN, 13));
            tf.setBorder(new CompoundBorder(
                    new LineBorder(new Color(0xC0, 0xCC, 0xE0), 1, true),
                    new EmptyBorder(8, 10, 8, 10)));
            tf.setMaximumSize(new Dimension(Integer.MAX_VALUE, 38));
            tf.setAlignmentX(Component.LEFT_ALIGNMENT);
            panel.add(tf);
            panel.add(Box.createVerticalStrut(12));
        }

        // Description
        JLabel descLbl = new JLabel("Description");
        descLbl.setFont(new Font("Segoe UI", Font.BOLD, 13));
        descLbl.setForeground(TEXT_DARK);
        descLbl.setAlignmentX(Component.LEFT_ALIGNMENT);
        panel.add(descLbl);
        panel.add(Box.createVerticalStrut(4));

        JTextArea desc = new JTextArea(4, 30);
        desc.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        desc.setLineWrap(true);
        desc.setWrapStyleWord(true);
        desc.setBorder(new CompoundBorder(
                new LineBorder(new Color(0xC0, 0xCC, 0xE0), 1),
                new EmptyBorder(8, 10, 8, 10)));
        JScrollPane descScroll = new JScrollPane(desc);
        descScroll.setAlignmentX(Component.LEFT_ALIGNMENT);
        descScroll.setMaximumSize(new Dimension(Integer.MAX_VALUE, 100));
        panel.add(descScroll);
        panel.add(Box.createVerticalStrut(20));

        JButton submit = new JButton("Submit Internship Post");
        submit.setFont(new Font("Segoe UI", Font.BOLD, 14));
        submit.setBackground(ROYAL_BLUE);
        submit.setForeground(WHITE);
        submit.setBorder(new EmptyBorder(12, 24, 12, 24));
        submit.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        submit.setFocusPainted(false);
        submit.setOpaque(true);
        submit.setAlignmentX(Component.LEFT_ALIGNMENT);
        submit.addActionListener(e ->
            JOptionPane.showMessageDialog(this,
                "Internship posted successfully!\n(DB integration coming soon)",
                "Success", JOptionPane.INFORMATION_MESSAGE));
        panel.add(submit);

        JScrollPane scroll = new JScrollPane(panel);
        scroll.setBorder(null);
        scroll.getVerticalScrollBar().setUnitIncrement(12);
        contentArea.add(scroll, BorderLayout.CENTER);
        contentArea.revalidate();
        contentArea.repaint();
    }

    private void showMessages() {
        contentArea.removeAll();
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(BG);
        panel.setBorder(new EmptyBorder(30, 30, 30, 30));

        JLabel title = new JLabel("Messages  (Coming Soon)");
        title.setFont(new Font("Segoe UI", Font.BOLD, 20));
        title.setForeground(ROYAL_BLUE);

        JLabel sub = new JLabel("Real-time messaging will be available in the next version.");
        sub.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        sub.setForeground(TEXT_MUTED);

        JPanel center = new JPanel();
        center.setBackground(BG);
        center.setLayout(new BoxLayout(center, BoxLayout.Y_AXIS));
        title.setAlignmentX(Component.CENTER_ALIGNMENT);
        sub.setAlignmentX(Component.CENTER_ALIGNMENT);
        center.add(Box.createVerticalGlue());
        center.add(title);
        center.add(Box.createVerticalStrut(8));
        center.add(sub);
        center.add(Box.createVerticalGlue());
        panel.add(center, BorderLayout.CENTER);

        contentArea.add(panel, BorderLayout.CENTER);
        contentArea.revalidate();
        contentArea.repaint();
    }

    // ── Card builders ──────────────────────────────────────────────────────
    private JPanel statCard(String title, String subtitle, Color accent) {
        JPanel card = roundedCard();
        card.setLayout(new BorderLayout(0, 4));
        card.setBorder(new CompoundBorder(
                new LineBorder(new Color(0xD8, 0xE3, 0xF0), 1, true),
                new EmptyBorder(16, 18, 16, 18)));

        JLabel t = new JLabel(title);
        t.setFont(new Font("Segoe UI", Font.BOLD, 14));
        t.setForeground(TEXT_DARK);

        JLabel s = new JLabel(subtitle);
        s.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        s.setForeground(accent);

        card.add(t, BorderLayout.NORTH);
        card.add(s, BorderLayout.CENTER);
        return card;
    }

    private JPanel actionCard(String title, String desc, Color color,
                              ActionListener action) {
        JPanel card = roundedCard();
        card.setLayout(new BorderLayout(0, 8));
        card.setBorder(new CompoundBorder(
                new LineBorder(new Color(0xD8, 0xE3, 0xF0), 1, true),
                new EmptyBorder(20, 20, 20, 20)));
        card.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

        JLabel t = new JLabel(title);
        t.setFont(new Font("Segoe UI", Font.BOLD, 15));
        t.setForeground(TEXT_DARK);

        JLabel d = new JLabel("<html>" + desc + "</html>");
        d.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        d.setForeground(TEXT_MUTED);

        JButton btn = new JButton("Open →");
        btn.setFont(new Font("Segoe UI", Font.BOLD, 12));
        btn.setBackground(color);
        btn.setForeground(WHITE);
        btn.setBorder(new EmptyBorder(6, 14, 6, 14));
        btn.setFocusPainted(false);
        btn.setOpaque(true);
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btn.addActionListener(action);

        card.add(t, BorderLayout.NORTH);
        card.add(d, BorderLayout.CENTER);
        card.add(btn, BorderLayout.SOUTH);
        return card;
    }

    private JPanel roundedCard() {
        JPanel card = new JPanel() {
            @Override protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g;
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                        RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(WHITE);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 14, 14);
            }
        };
        card.setOpaque(false);
        return card;
    }

    private JPanel roundedWrapper(JPanel inner, int radius) {
        JPanel wrap = new JPanel(new BorderLayout()) {
            @Override protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g;
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                        RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(ROYAL_BLUE);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), radius, radius);
            }
        };
        wrap.setOpaque(false);
        wrap.add(inner);
        return wrap;
    }

    private String getInitials(String name) {
        if (name == null || name.trim().isEmpty()) return "A";
        String[] parts = name.trim().split("\\s+");
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < Math.min(2, parts.length); i++)
            sb.append(Character.toUpperCase(parts[i].charAt(0)));
        return sb.toString();
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new AlumniDashboard(1));
    }
}