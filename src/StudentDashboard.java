import java.awt.*;
import javax.swing.*;

public class StudentDashboard extends JFrame {

    static final Color YALE_BLUE    = new Color(0x00, 0x3B, 0x6B);
    static final Color BG_LIGHT     = new Color(0xF2, 0xF5, 0xFA);
    static final Color WHITE        = Color.WHITE;
    static final Color LABEL_DARK   = new Color(0x12, 0x22, 0x34);
    static final Color LABEL_LIGHT  = new Color(0x88, 0x99, 0xAA);
    static final Color CARD_BG      = WHITE;
    static final Color ACCENT_BLUE  = new Color(0x18, 0x76, 0xD0);
    static final Color SUCCESS_GREEN= new Color(0x1D, 0x9E, 0x75);
    static final Color STAT_PURPLE  = new Color(0x8B, 0x5C, 0xF6);
    static final Color STAT_ORANGE  = new Color(0xF5, 0x9E, 0x0B);
    static final Color STAT_GREEN   = new Color(0x10, 0xB9, 0x81);

    // ── Layout constants tuned for 1920 × 1080 ──────────────────────────────
    // Usable canvas inside the JScrollPane (with ~24 px margin on each side)
    static final int CANVAS_W  = 1872;   // 1920 - 2*24
    static final int CANVAS_H  = 1080;   // enough for all rows without scrolling

    static final int MARGIN    = 28;     // left / right gutter
    static final int INNER_W   = CANVAS_W - 2 * MARGIN;  // 1816

    private int    userId;
    private String userName;

    public StudentDashboard(int userId) { this(userId, "Student"); }

    public StudentDashboard(int userId, String userName) {
        this.userId   = userId;
        this.userName = userName;

        setTitle("AlumniConnect – Student Dashboard");
        setSize(1920, 1080);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setExtendedState(JFrame.MAXIMIZED_BOTH);   // go full-screen on start

        JScrollPane sp = new JScrollPane(buildPanel());
        sp.setBorder(null);
        sp.getVerticalScrollBar().setUnitIncrement(20);
        sp.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);

        setContentPane(sp);
        setVisible(true);
    }

    // ────────────────────────────────────────────────────────────────────────
    public JPanel buildPanel() {
        JPanel panel = new JPanel(null);
        panel.setBackground(BG_LIGHT);
        panel.setPreferredSize(new Dimension(CANVAS_W, CANVAS_H));

        // ── 1. BANNER ────────────────────────────────────────────────────────
        JPanel banner = new JPanel(null) {
            @Override protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setPaint(new GradientPaint(0, 0, YALE_BLUE, getWidth(), 0, ACCENT_BLUE));
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 22, 22);
                g2.setColor(new Color(255, 255, 255, 18));
                g2.fillOval(getWidth() - 260, -80, 340, 340);
                g2.fillOval(-60, -60, 200, 200);
                g2.dispose();
            }
        };
        banner.setBounds(MARGIN, 28, INNER_W, 160);
        banner.setOpaque(false);

        JLabel icon = new JLabel("🎓");
        icon.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 56));
        icon.setBounds(36, 34, 70, 70);
        banner.add(icon);

        JLabel greet = new JLabel("Welcome, " + firstName() + "!");
        greet.setFont(new Font("Georgia", Font.BOLD, 34));
        greet.setForeground(WHITE);
        greet.setBounds(118, 28, 900, 48);
        banner.add(greet);

        JLabel sub = new JLabel(
                "Explore mentors, internships, opportunities and build your career network.");
        sub.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        sub.setForeground(new Color(0xD8, 0xEC, 0xFF));
        sub.setBounds(118, 80, 900, 26);
        banner.add(sub);

        JLabel badge = new JLabel("Student ID: " + userId);
        badge.setFont(new Font("Segoe UI", Font.BOLD, 14));
        badge.setForeground(WHITE);
        badge.setBounds(118, 114, 340, 26);
        banner.add(badge);

        panel.add(banner);

        // ── 2. STAT CARDS ────────────────────────────────────────────────────
        int statY  = 216;
        int statH  = 116;
        int statGap= 20;
        int statW  = (INNER_W - 3 * statGap) / 4;

        JPanel[] stats = {
            buildStatCard("Applications", "12", "+3 this week",   ACCENT_BLUE,   "📋"),
            buildStatCard("Mentors",      "8",  "2 active chats", SUCCESS_GREEN, "👥"),
            buildStatCard("Courses",      "5",  "In progress",    STAT_PURPLE,   "📚"),
            buildStatCard("Messages",     "16", "4 unread",       STAT_ORANGE,   "💬")
        };
        for (int i = 0; i < stats.length; i++) {
            stats[i].setBounds(MARGIN + i * (statW + statGap), statY, statW, statH);
            panel.add(stats[i]);
        }

        // ── 3. MENTOR + INTERNSHIP ROW ───────────────────────────────────────
        int row1Y = 354;
        int row1H = 280;
        int halfW = (INNER_W - statGap) / 2;

        // Mentor card
        JPanel mentorCard = makeWhiteCard();
        mentorCard.setBounds(MARGIN, row1Y, halfW, row1H);
        addSectionTitle(mentorCard, "👥 Suggested Mentors", 24, 20);

        String[][] mentors     = {
            {"AV", "Aditya Verma",  "SDE @ TCS",              "Java, Backend"},
            {"KS", "Kiara Singh",   "Data Analyst @ Infosys",  "Data Science"},
            {"VP", "Vihaan Patel",  "Cyber Analyst @ Wipro",   "Security"}
        };
        Color[] mentorColors = {ACCENT_BLUE, STAT_PURPLE, SUCCESS_GREEN};

        int my = 60;
        for (int i = 0; i < mentors.length; i++) {
            mentorCard.add(buildMentorRow(
                mentors[i][0], mentors[i][1], mentors[i][2], mentors[i][3],
                mentorColors[i], my, halfW - 28));
            my += 72;
        }
        panel.add(mentorCard);

        // Internship card
        JPanel internCard = makeWhiteCard();
        internCard.setBounds(MARGIN + halfW + statGap, row1Y, halfW, row1H);
        addSectionTitle(internCard, "💼 Internship Opportunities", 24, 20);

        String[][] jobs = {
            {"Backend Intern",       "Google",   "Remote · 3 months"},
            {"Data Analyst Intern",  "Flipkart", "Bangalore · 6 months"},
            {"Frontend Intern",      "Zomato",   "Hybrid · 2 months"},
            {"Cybersecurity Intern", "Wipro",    "Noida · 4 months"}
        };

        int jy = 60;
        for (String[] job : jobs) {
            internCard.add(buildJobRow(job[0], job[1], job[2], jy, halfW - 28));
            jy += 54;
        }
        panel.add(internCard);

        // ── 4. COURSES + ACTIVITY ROW ────────────────────────────────────────
        int row2Y = row1Y + row1H + statGap;
        int row2H = 280;

        JPanel courseCard = makeWhiteCard();
        courseCard.setBounds(MARGIN, row2Y, halfW, row2H);
        addSectionTitle(courseCard, "📚 Learning Progress", 24, 20);

        String[][] courses = {
            {"Java OOP",              "80"},
            {"DBMS / MySQL",          "65"},
            {"Frontend Development",  "72"},
            {"Data Communication",    "58"}
        };
        Color[] courseColors = {ACCENT_BLUE, SUCCESS_GREEN, STAT_PURPLE, STAT_ORANGE};

        int cy = 62;
        for (int i = 0; i < courses.length; i++) {
            courseCard.add(buildProgressRow(
                courses[i][0], Integer.parseInt(courses[i][1]),
                courseColors[i], cy, halfW - 28));
            cy += 54;
        }
        panel.add(courseCard);

        JPanel activityCard = makeWhiteCard();
        activityCard.setBounds(MARGIN + halfW + statGap, row2Y, halfW, row2H);
        addSectionTitle(activityCard, "🔔 Recent Activity", 24, 20);

        String[][] activities = {
            {"✅", "Applied to Backend Intern role",   "Today"},
            {"💬", "New message from Aditya Verma",    "Yesterday"},
            {"📚", "Completed Java OOP module",         "2 days ago"},
            {"👥", "Connected with Kiara Singh",        "3 days ago"}
        };

        int ay = 62;
        for (String[] a : activities) {
            activityCard.add(buildActivityRow(a[0], a[1], a[2], ay, halfW - 28));
            ay += 54;
        }
        panel.add(activityCard);

        // ── 5. QUICK-ACTIONS BAR ─────────────────────────────────────────────
        int actionY = row2Y + row2H + statGap;
        int actionH = 100;

        JPanel actionCard = makeWhiteCard();
        actionCard.setBounds(MARGIN, actionY, INNER_W, actionH);
        addSectionTitle(actionCard, "⚡ Quick Actions", 24, 14);

        String[] actions = {
            "👤 My Profile", "👥 Find Alumni", "💼 Apply Internship",
            "💬 Messages",   "📚 Courses",     "🏠 Home"
        };

        int btnW  = 200;
        int btnGap= (INNER_W - 48 - actions.length * btnW) / (actions.length - 1);
        int ax    = 24;

        for (String act : actions) {
            JButton btn = buildQuickActionBtn(act);
            btn.setBounds(ax, 44, btnW, 42);

            if (act.contains("Home")) {
                btn.addActionListener(e -> { dispose(); new HomePage(userName, "student", userId); });
            } else if (act.contains("Profile")) {
                btn.addActionListener(e -> new StudentProfilePage(userId));
            } else {
                btn.addActionListener(e ->
                    JOptionPane.showMessageDialog(this, act + " – feature coming soon."));
            }

            actionCard.add(btn);
            ax += btnW + btnGap;
        }
        panel.add(actionCard);

        // adjust canvas height to fit everything
        int totalH = actionY + actionH + MARGIN;
        panel.setPreferredSize(new Dimension(CANVAS_W, Math.max(CANVAS_H, totalH)));

        return panel;
    }

    // ── Component builders ───────────────────────────────────────────────────

    private JPanel buildStatCard(String title, String value, String sub,
                                 Color accent, String icon) {
        JPanel card = makeWhiteCard();

        JLabel ic = new JLabel(icon);
        ic.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 28));
        ic.setBounds(18, 14, 40, 40);
        card.add(ic);

        JLabel ttl = new JLabel(title);
        ttl.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        ttl.setForeground(LABEL_LIGHT);
        ttl.setBounds(18, 36, 200, 20);
        card.add(ttl);

        JLabel val = new JLabel(value);
        val.setFont(new Font("Georgia", Font.BOLD, 30));
        val.setForeground(LABEL_DARK);
        val.setBounds(18, 58, 160, 36);
        card.add(val);

        JLabel sb = new JLabel(sub);
        sb.setFont(new Font("Segoe UI", Font.BOLD, 12));
        sb.setForeground(accent);
        sb.setBounds(18, 94, 200, 18);
        card.add(sb);

        return card;
    }

    private JPanel buildMentorRow(String initials, String name, String role,
                                  String skills, Color color, int y, int w) {
        JPanel row = new JPanel(null);
        row.setOpaque(false);
        row.setBounds(14, y, w, 64);

        JLabel av = avatar(initials, color, 46);
        av.setBounds(0, 8, 46, 46);
        row.add(av);

        JLabel nm = new JLabel(name);
        nm.setFont(new Font("Segoe UI", Font.BOLD, 15));
        nm.setForeground(LABEL_DARK);
        nm.setBounds(58, 6, 260, 22);
        row.add(nm);

        JLabel info = new JLabel(role + " · " + skills);
        info.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        info.setForeground(LABEL_LIGHT);
        info.setBounds(58, 28, 340, 18);
        row.add(info);

        JButton connect = buildActionBtn("Connect", color);
        connect.setBounds(w - 100, 14, 88, 30);
        row.add(connect);

        return row;
    }

    private JPanel buildJobRow(String title, String company,
                               String detail, int y, int w) {
        JPanel row = new JPanel(null);
        row.setOpaque(false);
        row.setBounds(14, y, w, 48);

        JLabel t = new JLabel(title);
        t.setFont(new Font("Segoe UI", Font.BOLD, 14));
        t.setForeground(LABEL_DARK);
        t.setBounds(0, 0, 360, 20);
        row.add(t);

        JLabel d = new JLabel(company + " · " + detail);
        d.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        d.setForeground(LABEL_LIGHT);
        d.setBounds(0, 22, 380, 18);
        row.add(d);

        JButton apply = buildActionBtn("Apply", SUCCESS_GREEN);
        apply.setBounds(w - 92, 8, 80, 28);
        row.add(apply);

        return row;
    }

    private JPanel buildProgressRow(String title, int pct,
                                    Color color, int y, int w) {
        JPanel row = new JPanel(null);
        row.setOpaque(false);
        row.setBounds(14, y, w, 42);

        JLabel name = new JLabel(title);
        name.setFont(new Font("Segoe UI", Font.BOLD, 13));
        name.setForeground(LABEL_DARK);
        name.setBounds(0, 0, 300, 20);
        row.add(name);

        JLabel p = new JLabel(pct + "%");
        p.setFont(new Font("Segoe UI", Font.BOLD, 13));
        p.setForeground(color);
        p.setBounds(w - 52, 0, 52, 20);
        row.add(p);

        JPanel bar = new JPanel(null) {
            @Override protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(new Color(0xE8, 0xEE, 0xF4));
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 10, 10);
                g2.setColor(color);
                g2.fillRoundRect(0, 0, (int)(getWidth() * pct / 100.0), getHeight(), 10, 10);
                g2.dispose();
            }
        };
        bar.setOpaque(false);
        bar.setBounds(0, 28, w - 24, 10);
        row.add(bar);

        return row;
    }

    private JPanel buildActivityRow(String icon, String text,
                                    String time, int y, int w) {
        JPanel row = new JPanel(null);
        row.setOpaque(false);
        row.setBounds(14, y, w, 46);

        JLabel ic = new JLabel(icon);
        ic.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 20));
        ic.setBounds(0, 6, 36, 30);
        row.add(ic);

        JLabel msg = new JLabel(text);
        msg.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        msg.setForeground(LABEL_DARK);
        msg.setBounds(42, 4, 500, 20);
        row.add(msg);

        JLabel tm = new JLabel(time);
        tm.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        tm.setForeground(LABEL_LIGHT);
        tm.setBounds(42, 24, 240, 16);
        row.add(tm);

        return row;
    }

    // ── Helpers ──────────────────────────────────────────────────────────────

    private JButton buildActionBtn(String label, Color color) {
        JButton btn = new JButton(label);
        btn.setFont(new Font("Segoe UI", Font.BOLD, 12));
        btn.setForeground(color);
        btn.setBorderPainted(false);
        btn.setContentAreaFilled(false);
        btn.setFocusPainted(false);
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        return btn;
    }

    private JButton buildQuickActionBtn(String label) {
        JButton btn = new JButton(label);
        btn.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        btn.setForeground(LABEL_DARK);
        btn.setBackground(new Color(0xF5, 0xF7, 0xFA));
        btn.setFocusPainted(false);
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        return btn;
    }

    private JPanel makeWhiteCard() {
        JPanel card = new JPanel(null) {
            @Override protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(new Color(0, 0, 0, 10));
                g2.fillRoundRect(4, 5, getWidth() - 4, getHeight() - 5, 20, 20);
                g2.setColor(CARD_BG);
                g2.fillRoundRect(0, 0, getWidth() - 4, getHeight() - 5, 20, 20);
                g2.dispose();
            }
        };
        card.setOpaque(false);
        return card;
    }

    private JLabel avatar(String text, Color color, int size) {
        JLabel lbl = new JLabel(text, SwingConstants.CENTER) {
            @Override protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(new Color(color.getRed(), color.getGreen(), color.getBlue(), 28));
                g2.fillOval(0, 0, size, size);
                g2.setColor(color);
                g2.setFont(new Font("Georgia", Font.BOLD, 15));
                FontMetrics fm = g2.getFontMetrics();
                g2.drawString(getText(),
                        (size - fm.stringWidth(getText())) / 2,
                        (size + fm.getAscent() - fm.getDescent()) / 2);
                g2.dispose();
            }
        };
        lbl.setOpaque(false);
        return lbl;
    }

    private void addSectionTitle(JPanel parent, String text, int x, int y) {
        JLabel lbl = new JLabel(text);
        lbl.setFont(new Font("Segoe UI", Font.BOLD, 16));
        lbl.setForeground(LABEL_DARK);
        lbl.setBounds(x, y, 600, 24);
        parent.add(lbl);
    }

    private String firstName() {
        if (userName == null || userName.trim().isEmpty()) return "Student";
        return userName.trim().split("\\s+")[0];
    }
}