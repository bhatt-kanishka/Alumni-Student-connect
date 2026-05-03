


import javax.swing.*;
import javax.swing.border.*;
import javax.swing.table.*;
import java.awt.*;
import java.awt.event.*;
import java.util.List;

/**
 * AdminDashboard – Full control panel for administrators.
 * Constructor: AdminDashboard(int userId)
 */
public class AdminDashboard extends JFrame {

    // ── Palette ──────────────────────────────────────────────────────────────
    private static final Color ROYAL_BLUE = new Color(0x00, 0x3B, 0x6B);
    private static final Color BEIGE      = new Color(0xFA, 0xF0, 0xCA);
    private static final Color BG         = new Color(0xF5, 0xF7, 0xFA);
    private static final Color WHITE      = Color.WHITE;
    private static final Color TEXT_DARK  = new Color(0x1A, 0x1A, 0x2E);
    private static final Color TEXT_MUTED = new Color(0x6B, 0x7A, 0x99);
    private static final Color ACCENT     = new Color(0x00, 0x8B, 0xD4);
    private static final Color RED        = new Color(0xE7, 0x4C, 0x3C);
    private static final Color GREEN      = new Color(0x2E, 0xCC, 0x71);
    private static final Color ORANGE     = new Color(0xE6, 0x7E, 0x22);

    private final int userId;
    private JPanel contentArea;

    public AdminDashboard(int userId) {
        this.userId = userId;
        initUI();
    }

    /** No-arg constructor for compatibility with LoginPage calling new AdminDashboard() */
    public AdminDashboard() {
        this(0);
    }

    private void initUI() {
        setTitle("AlumniConnect – Admin Dashboard");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1100, 700);
        setLocationRelativeTo(null);
        setResizable(false);

        JPanel root = new JPanel(new BorderLayout());
        root.setBackground(BG);

        root.add(buildNavBar(), BorderLayout.NORTH);

        JSplitPane split = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT,
                buildSidebar(), buildMainContent());
        split.setDividerSize(0);
        split.setEnabled(false);
        split.setDividerLocation(230);
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

        JLabel logo = new JLabel("AlumniConnect  |  Admin Panel");
        logo.setFont(new Font("Segoe UI", Font.BOLD, 18));
        logo.setForeground(WHITE);
        nav.add(logo, BorderLayout.WEST);

        JPanel right = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        right.setOpaque(false);

        JLabel adminBadge = new JLabel("● ADMIN");
        adminBadge.setFont(new Font("Segoe UI", Font.BOLD, 11));
        adminBadge.setForeground(BEIGE);
        right.add(adminBadge);

        JButton logout = navButton("Logout");
        logout.addActionListener(e -> {
            int confirm = JOptionPane.showConfirmDialog(this,
                    "Logout from Admin Panel?", "Confirm Logout",
                    JOptionPane.YES_NO_OPTION);
            if (confirm == JOptionPane.YES_OPTION) {
                dispose();
                new LoginPage("student");
            }
        });
        right.add(logout);
        nav.add(right, BorderLayout.EAST);
        return nav;
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
        sidebar.setBorder(new EmptyBorder(24, 14, 24, 14));
        sidebar.setPreferredSize(new Dimension(230, 0));

        StudentProfilePage.AvatarPanel avatar = new StudentProfilePage.AvatarPanel("AD", 56);
        avatar.setAlignmentX(Component.CENTER_ALIGNMENT);
        avatar.setMaximumSize(new Dimension(56, 56));
        sidebar.add(avatar);
        sidebar.add(Box.createVerticalStrut(8));

        JLabel lbl = new JLabel("Administrator");
        lbl.setFont(new Font("Segoe UI", Font.BOLD, 13));
        lbl.setForeground(TEXT_DARK);
        lbl.setAlignmentX(Component.CENTER_ALIGNMENT);
        sidebar.add(lbl);

        JLabel sub = new JLabel("Full Access");
        sub.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        sub.setForeground(RED);
        sub.setAlignmentX(Component.CENTER_ALIGNMENT);
        sidebar.add(sub);

        sidebar.add(Box.createVerticalStrut(20));
        sidebar.add(new JSeparator());
        sidebar.add(Box.createVerticalStrut(14));

        addMenuGroup(sidebar, "USER MANAGEMENT");
        addMenuItem(sidebar, "👥  All Users",  e -> showAllUsers());
        addMenuItem(sidebar, "🎓  Students",   e -> showStudents());
        addMenuItem(sidebar, "🏢  Alumni",     e -> showAlumni());
        sidebar.add(Box.createVerticalStrut(8));

        addMenuGroup(sidebar, "CONTENT");
        addMenuItem(sidebar, "📋  Internships", e -> showPlaceholder("Internships"));
        addMenuItem(sidebar, "💬  Messages",    e -> showPlaceholder("Messages"));
        sidebar.add(Box.createVerticalStrut(8));

        addMenuGroup(sidebar, "SYSTEM");
        addMenuItem(sidebar, "⚙  Settings", e -> showPlaceholder("Settings"));
        addMenuItem(sidebar, "📊  Reports",  e -> showPlaceholder("Reports"));

        sidebar.add(Box.createVerticalGlue());

        JLabel ver = new JLabel("AlumniConnect v1.0");
        ver.setFont(new Font("Segoe UI", Font.PLAIN, 10));
        ver.setForeground(new Color(0xCC, 0xD0, 0xDA));
        ver.setAlignmentX(Component.CENTER_ALIGNMENT);
        sidebar.add(ver);

        return sidebar;
    }

    private void addMenuGroup(JPanel parent, String label) {
        JLabel lbl = new JLabel(label);
        lbl.setFont(new Font("Segoe UI", Font.BOLD, 10));
        lbl.setForeground(TEXT_MUTED);
        lbl.setBorder(new EmptyBorder(0, 4, 4, 0));
        lbl.setAlignmentX(Component.LEFT_ALIGNMENT);
        parent.add(lbl);
    }

    private void addMenuItem(JPanel parent, String text, ActionListener action) {
        JButton btn = new JButton(text);
        btn.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        btn.setForeground(TEXT_DARK);
        btn.setBackground(BG);
        btn.setHorizontalAlignment(SwingConstants.LEFT);
        btn.setBorder(new EmptyBorder(9, 10, 9, 10));
        btn.setMaximumSize(new Dimension(Integer.MAX_VALUE, 38));
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btn.setFocusPainted(false);
        btn.setContentAreaFilled(false);
        btn.setOpaque(true);
        btn.addActionListener(action);
        btn.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) { btn.setBackground(BEIGE); }
            public void mouseExited(MouseEvent e)  { btn.setBackground(BG); }
        });
        parent.add(btn);
        parent.add(Box.createVerticalStrut(2));
    }

    // ── Main Content ───────────────────────────────────────────────────────
    private JPanel buildMainContent() {
        contentArea = new JPanel(new BorderLayout());
        contentArea.setBackground(BG);
        showOverview();
        return contentArea;
    }

    // ── Overview ───────────────────────────────────────────────────────────
    private void showOverview() {
        contentArea.removeAll();
        JPanel panel = new JPanel();
        panel.setBackground(BG);
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBorder(new EmptyBorder(28, 28, 28, 28));

        JLabel title = new JLabel("System Overview");
        title.setFont(new Font("Segoe UI", Font.BOLD, 22));
        title.setForeground(ROYAL_BLUE);
        title.setAlignmentX(Component.LEFT_ALIGNMENT);
        panel.add(title);
        panel.add(Box.createVerticalStrut(6));

        JLabel sub = new JLabel("Monitor and manage all platform activity from here.");
        sub.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        sub.setForeground(TEXT_MUTED);
        sub.setAlignmentX(Component.LEFT_ALIGNMENT);
        panel.add(sub);
        panel.add(Box.createVerticalStrut(24));

        JPanel stats = new JPanel(new GridLayout(1, 4, 16, 0));
        stats.setOpaque(false);
        stats.setMaximumSize(new Dimension(Integer.MAX_VALUE, 100));
        stats.add(statCard("Total Users", "—", ROYAL_BLUE));
        stats.add(statCard("Students",    "—", ACCENT));
        stats.add(statCard("Alumni",      "—", GREEN));
        stats.add(statCard("Admins",      "—", ORANGE));
        panel.add(stats);
        panel.add(Box.createVerticalStrut(24));

        JLabel actLbl = new JLabel("Admin Actions");
        actLbl.setFont(new Font("Segoe UI", Font.BOLD, 16));
        actLbl.setForeground(TEXT_DARK);
        actLbl.setAlignmentX(Component.LEFT_ALIGNMENT);
        panel.add(actLbl);
        panel.add(Box.createVerticalStrut(12));

        JPanel actions = new JPanel(new GridLayout(2, 2, 14, 14));
        actions.setOpaque(false);
        actions.setMaximumSize(new Dimension(Integer.MAX_VALUE, 160));
        actions.add(actionBtn("👥 View All Users",   ROYAL_BLUE, e -> showAllUsers()));
        actions.add(actionBtn("🎓 View Students",    ACCENT,     e -> showStudents()));
        actions.add(actionBtn("🏢 View Alumni",      GREEN,      e -> showAlumni()));
        actions.add(actionBtn("⚙  System Settings", ORANGE,     e -> showPlaceholder("Settings")));
        panel.add(actions);

        JScrollPane scroll = new JScrollPane(panel);
        scroll.setBorder(null);
        contentArea.add(scroll, BorderLayout.CENTER);
        contentArea.revalidate();
        contentArea.repaint();
    }

    // ── All Users Table ────────────────────────────────────────────────────
    private void showAllUsers() {
        contentArea.removeAll();
        JPanel panel = new JPanel(new BorderLayout(0, 14));
        panel.setBackground(BG);
        panel.setBorder(new EmptyBorder(24, 24, 24, 24));

        JLabel title = sectionTitle("All Users");
        panel.add(title, BorderLayout.NORTH);

        String[] cols = { "ID", "Name", "Email", "Role", "Created At", "Actions" };
        DefaultTableModel tableModel = new DefaultTableModel(cols, 0) {
            @Override
            public boolean isCellEditable(int r, int c) { return c == 5; }
        };

        try {
            UserDAO dao = new UserDAO();
            List<User> users = dao.getUsersByRole("student");
            for (User u : users) {
                tableModel.addRow(new Object[]{
                        u.getUserId(),
                        u.getName(),
                        u.getEmail(),
                        u.getRole(),
                        "-",
                        "Manage"
                });
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        JTable table = buildStyledTable(tableModel);
        table.getColumnModel().getColumn(3).setCellRenderer(new RoleCellRenderer());
        table.getColumnModel().getColumn(5).setCellRenderer(new ButtonRenderer("Manage"));
        table.getColumnModel().getColumn(5).setCellEditor(new ButtonEditor(
                new JCheckBox(), "Manage", e -> JOptionPane.showMessageDialog(this,
                        "User management actions (Edit/Delete) will be implemented with DB.",
                        "Manage User", JOptionPane.INFORMATION_MESSAGE)));

        table.getColumnModel().getColumn(0).setMaxWidth(50);
        table.getColumnModel().getColumn(3).setMaxWidth(90);
        table.getColumnModel().getColumn(4).setMaxWidth(110);
        table.getColumnModel().getColumn(5).setMaxWidth(90);

        JScrollPane sp = new JScrollPane(table);
        styleScrollPane(sp);

        JPanel tablePanel = new JPanel(new BorderLayout(0, 8));
        tablePanel.setOpaque(false);
        tablePanel.add(buildSearchBar(tableModel, table, 1), BorderLayout.NORTH);
        tablePanel.add(sp, BorderLayout.CENTER);
        panel.add(tablePanel, BorderLayout.CENTER);

        contentArea.add(panel, BorderLayout.CENTER);
        contentArea.revalidate();
        contentArea.repaint();
    }

    // ── Students Table ─────────────────────────────────────────────────────
    private void showStudents() {
        contentArea.removeAll();
        JPanel panel = new JPanel(new BorderLayout(0, 14));
        panel.setBackground(BG);
        panel.setBorder(new EmptyBorder(24, 24, 24, 24));
        panel.add(sectionTitle("Students"), BorderLayout.NORTH);

        String[] cols = { "ID", "Name", "Email", "Course", "Specialization", "Batch" };
        DefaultTableModel tableModel = new DefaultTableModel(cols, 0) {
            @Override
            public boolean isCellEditable(int r, int c) { return false; }
        };

        try {
            UserDAO dao = new UserDAO();
            List<User> rows = dao.getUsersByRole("student");
            for (User u : rows) {
                tableModel.addRow(new Object[]{
                        u.getUserId(),
                        u.getName(),
                        u.getEmail(),
                        "Course",
                        "Specialization",
                        "Batch"
                });
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        JTable table = buildStyledTable(tableModel);
        JScrollPane sp = new JScrollPane(table);
        styleScrollPane(sp);

        JPanel center = new JPanel(new BorderLayout(0, 8));
        center.setOpaque(false);
        center.add(buildSearchBar(tableModel, table, 1), BorderLayout.NORTH);
        center.add(sp, BorderLayout.CENTER);
        panel.add(center, BorderLayout.CENTER);

        contentArea.add(panel, BorderLayout.CENTER);
        contentArea.revalidate();
        contentArea.repaint();
    }

    // ── Alumni Table ───────────────────────────────────────────────────────
    private void showAlumni() {
        contentArea.removeAll();
        JPanel panel = new JPanel(new BorderLayout(0, 14));
        panel.setBackground(BG);
        panel.setBorder(new EmptyBorder(24, 24, 24, 24));
        panel.add(sectionTitle("Alumni"), BorderLayout.NORTH);

        String[] cols = { "ID", "Name", "Email", "Company", "Designation", "Graduation Year" };
        DefaultTableModel tableModel = new DefaultTableModel(cols, 0) {
            @Override
            public boolean isCellEditable(int r, int c) { return false; }
        };

        try {
            UserDAO dao = new UserDAO();
            List<User> rows = dao.getUsersByRole("alumni");
            for (User u : rows) {
                tableModel.addRow(new Object[]{
                        u.getUserId(),
                        u.getName(),
                        u.getEmail(),
                        "Company",
                        "Designation",
                        "Year"
                });
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        JTable table = buildStyledTable(tableModel);
        JScrollPane sp = new JScrollPane(table);
        styleScrollPane(sp);

        JPanel center = new JPanel(new BorderLayout(0, 8));
        center.setOpaque(false);
        center.add(buildSearchBar(tableModel, table, 1), BorderLayout.NORTH);
        center.add(sp, BorderLayout.CENTER);
        panel.add(center, BorderLayout.CENTER);

        contentArea.add(panel, BorderLayout.CENTER);
        contentArea.revalidate();
        contentArea.repaint();
    }

    // ── Placeholder ────────────────────────────────────────────────────────
    private void showPlaceholder(String section) {
        contentArea.removeAll();
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBackground(BG);

        JLabel lbl = new JLabel(section + " — Coming Soon");
        lbl.setFont(new Font("Segoe UI", Font.BOLD, 20));
        lbl.setForeground(TEXT_MUTED);
        panel.add(lbl);

        contentArea.add(panel, BorderLayout.CENTER);
        contentArea.revalidate();
        contentArea.repaint();
    }

    // ── Helpers ────────────────────────────────────────────────────────────
    private JTable buildStyledTable(DefaultTableModel model) {
        JTable table = new JTable(model);
        table.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        table.setRowHeight(34);
        table.setIntercellSpacing(new Dimension(0, 1));
        table.setShowHorizontalLines(true);
        table.setShowVerticalLines(false);
        table.setGridColor(new Color(0xEA, 0xEE, 0xF6));
        table.setSelectionBackground(new Color(0xE3, 0xF0, 0xFF));
        table.setSelectionForeground(TEXT_DARK);

        JTableHeader header = table.getTableHeader();
        header.setFont(new Font("Segoe UI", Font.BOLD, 13));
        header.setBackground(ROYAL_BLUE);
        header.setForeground(WHITE);
        header.setPreferredSize(new Dimension(0, 38));
        header.setBorder(null);

        table.setDefaultRenderer(Object.class, new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable t, Object val,
                    boolean sel, boolean foc, int row, int col) {
                super.getTableCellRendererComponent(t, val, sel, foc, row, col);
                setBorder(new EmptyBorder(0, 12, 0, 12));
                if (!sel)
                    setBackground(row % 2 == 0 ? WHITE : new Color(0xF7, 0xF9, 0xFF));
                return this;
            }
        });
        return table;
    }

    private void styleScrollPane(JScrollPane sp) {
        sp.setBorder(new LineBorder(new Color(0xD0, 0xDA, 0xF0), 1, true));
        sp.getVerticalScrollBar().setUnitIncrement(12);
    }

    private JPanel buildSearchBar(DefaultTableModel model, JTable table, int searchCol) {
        JPanel bar = new JPanel(new BorderLayout(8, 0));
        bar.setOpaque(false);
        bar.setMaximumSize(new Dimension(Integer.MAX_VALUE, 36));

        JTextField tf = new JTextField();
        tf.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        tf.setBorder(new CompoundBorder(
                new LineBorder(new Color(0xC8, 0xD4, 0xE8), 1, true),
                new EmptyBorder(6, 10, 6, 10)));
        tf.putClientProperty("JTextField.placeholderText", "Search...");

        tf.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                String filter = tf.getText().trim().toLowerCase();
                TableRowSorter<DefaultTableModel> sorter = new TableRowSorter<>(model);
                table.setRowSorter(sorter);
                sorter.setRowFilter(filter.isEmpty() ? null
                        : RowFilter.regexFilter("(?i)" + filter, searchCol));
            }
        });

        JLabel searchIcon = new JLabel("🔍 ");
        searchIcon.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        bar.add(searchIcon, BorderLayout.WEST);
        bar.add(tf, BorderLayout.CENTER);
        return bar;
    }

    private JPanel statCard(String label, String value, Color color) {
        JPanel card = new JPanel(new BorderLayout(0, 4)) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g;
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                        RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(WHITE);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 12, 12);
                g2.setColor(color);
                g2.fillRoundRect(0, getHeight() - 4, getWidth(), 4, 4, 4);
            }
        };
        card.setOpaque(false);
        card.setBorder(new EmptyBorder(16, 18, 16, 18));

        JLabel val = new JLabel(value);
        val.setFont(new Font("Segoe UI", Font.BOLD, 26));
        val.setForeground(color);

        JLabel lbl = new JLabel(label);
        lbl.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        lbl.setForeground(TEXT_MUTED);

        card.add(val, BorderLayout.CENTER);
        card.add(lbl, BorderLayout.SOUTH);
        return card;
    }

    private JButton actionBtn(String text, Color color, ActionListener action) {
        JButton btn = new JButton(text);
        btn.setFont(new Font("Segoe UI", Font.BOLD, 13));
        btn.setBackground(color);
        btn.setForeground(WHITE);
        btn.setBorder(new EmptyBorder(14, 20, 14, 20));
        btn.setFocusPainted(false);
        btn.setOpaque(true);
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btn.addActionListener(action);
        return btn;
    }

    private JLabel sectionTitle(String text) {
        JLabel lbl = new JLabel(text);
        lbl.setFont(new Font("Segoe UI", Font.BOLD, 20));
        lbl.setForeground(ROYAL_BLUE);
        lbl.setBorder(new EmptyBorder(0, 0, 8, 0));
        return lbl;
    }

    // ── Role Cell Renderer ─────────────────────────────────────────────────
    static class RoleCellRenderer extends DefaultTableCellRenderer {
        @Override
        public Component getTableCellRendererComponent(JTable t, Object val,
                boolean sel, boolean foc, int row, int col) {
            super.getTableCellRendererComponent(t, val, sel, foc, row, col);
            String role = val == null ? "" : val.toString();
            setBorder(new EmptyBorder(4, 8, 4, 8));
            setHorizontalAlignment(SwingConstants.CENTER);
            switch (role.toUpperCase()) {
                case "STUDENT": setForeground(new Color(0x00, 0x6B, 0xD4)); break;
                case "ALUMNI":  setForeground(new Color(0x27, 0xAE, 0x60)); break;
                case "ADMIN":   setForeground(new Color(0xC0, 0x39, 0x2B)); break;
                default:        setForeground(Color.GRAY);
            }
            setFont(new Font("Segoe UI", Font.BOLD, 12));
            if (!sel)
                setBackground(row % 2 == 0 ? Color.WHITE : new Color(0xF7, 0xF9, 0xFF));
            return this;
        }
    }

    // ── Button Renderer / Editor ───────────────────────────────────────────
    static class ButtonRenderer extends JButton implements TableCellRenderer {
        ButtonRenderer(String label) {
            setText(label);
            setFont(new Font("Segoe UI", Font.BOLD, 11));
            setBackground(new Color(0x00, 0x3B, 0x6B));
            setForeground(Color.WHITE);
            setBorder(new EmptyBorder(4, 10, 4, 10));
            setFocusPainted(false);
            setOpaque(true);
        }

        @Override
        public Component getTableCellRendererComponent(JTable t, Object val,
                boolean sel, boolean foc, int row, int col) {
            return this;
        }
    }

    static class ButtonEditor extends DefaultCellEditor {
        private final JButton button;
        private ActionListener externalAction;

        ButtonEditor(JCheckBox cb, String label, ActionListener action) {
            super(cb);
            this.externalAction = action;
            button = new JButton(label);
            button.setFont(new Font("Segoe UI", Font.BOLD, 11));
            button.setBackground(new Color(0x00, 0x3B, 0x6B));
            button.setForeground(Color.WHITE);
            button.setBorder(new EmptyBorder(4, 10, 4, 10));
            button.setFocusPainted(false);
            button.setOpaque(true);
            button.addActionListener(e -> {
                fireEditingStopped();
                externalAction.actionPerformed(e);
            });
        }

        @Override
        public Component getTableCellEditorComponent(JTable t, Object val,
                boolean sel, int row, int col) {
            return button;
        }

        @Override
        public Object getCellEditorValue() { return ""; }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new AdminDashboard(1));
    }
}