import java.awt.*;
import javax.swing.*;

public class StudentDashboard extends JFrame {

    static final Color YALE_BLUE = new Color(0x00, 0x3B, 0x6B);
    static final Color BG_LIGHT = new Color(0xF2, 0xF5, 0xFA);
    static final Color WHITE = Color.WHITE;
    static final Color LABEL_DARK = new Color(0x12, 0x22, 0x34);
    static final Color LABEL_LIGHT = new Color(0x88, 0x99, 0xAA);
    static final Color CARD_BG = WHITE;
    static final Color ACCENT_BLUE = new Color(0x18, 0x76, 0xD0);
    static final Color SUCCESS_GREEN = new Color(0x1D, 0x9E, 0x75);
    static final Color STAT_PURPLE = new Color(0x8B, 0x5C, 0xF6);
    static final Color STAT_ORANGE = new Color(0xF5, 0x9E, 0x0B);
    static final Color STAT_GREEN = new Color(0x10, 0xB9, 0x81);

    private int userId;
    private String userName;

    public StudentDashboard(int userId) {
        this(userId, "Student");
    }

    public StudentDashboard(int userId, String userName) {
        this.userId = userId;
        this.userName = userName;

        setTitle("AlumniConnect - Student Dashboard");
        setSize(1100, 760);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JScrollPane scrollPane = new JScrollPane(buildPanel());
        scrollPane.setBorder(null);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);

        setContentPane(scrollPane);
        setVisible(true);
    }

    public JPanel buildPanel() {
        JPanel panel = new JPanel(null);
        panel.setBackground(BG_LIGHT);
        panel.setPreferredSize(new Dimension(900, 950));

        JPanel banner = new JPanel(null) {
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                GradientPaint gp = new GradientPaint(
                        0, 0, YALE_BLUE,
                        getWidth(), 0, new Color(0x18, 0x76, 0xD0)
                );

                g2.setPaint(gp);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 18, 18);

                g2.setColor(new Color(255, 255, 255, 18));
                g2.fillOval(getWidth() - 170, -60, 230, 230);
                g2.fillOval(-40, -40, 130, 130);

                g2.dispose();
            }
        };

        banner.setBounds(24, 24, 820, 130);
        banner.setOpaque(false);

        JLabel icon = new JLabel("🎓");
        icon.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 42));
        icon.setBounds(28, 32, 60, 60);
        banner.add(icon);

        JLabel greet = new JLabel("Welcome, " + firstName() + "!");
        greet.setFont(new Font("Georgia", Font.BOLD, 25));
        greet.setForeground(WHITE);
        greet.setBounds(95, 24, 520, 35);
        banner.add(greet);

        JLabel sub = new JLabel("Explore mentors, internships, opportunities and build your career network.");
        sub.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        sub.setForeground(new Color(0xD8, 0xEC, 0xFF));
        sub.setBounds(95, 60, 600, 22);
        banner.add(sub);

        JLabel badge = new JLabel("Student ID: " + userId);
        badge.setFont(new Font("Segoe UI", Font.BOLD, 12));
        badge.setForeground(WHITE);
        badge.setBounds(95, 88, 240, 22);
        banner.add(badge);

        panel.add(banner);

        int cardY = 174;
        int cardH = 96;
        int gap = 16;
        int cardW = (820 - 3 * gap) / 4;

        JPanel[] stats = {
                buildStatCard("Applications", "12", "+3 this week", ACCENT_BLUE, "📋"),
                buildStatCard("Mentors", "8", "2 active chats", SUCCESS_GREEN, "👥"),
                buildStatCard("Courses", "5", "In progress", STAT_PURPLE, "📚"),
                buildStatCard("Messages", "16", "4 unread", STAT_ORANGE, "💬")
        };

        for (int i = 0; i < stats.length; i++) {
            stats[i].setBounds(24 + i * (cardW + gap), cardY, cardW, cardH);
            panel.add(stats[i]);
        }

        JPanel mentorCard = makeWhiteCard();
        mentorCard.setBounds(24, 295, 396, 220);
        addSectionTitle(mentorCard, "👥 Suggested Mentors", 20, 18);

        String[][] mentors = {
                {"AV", "Aditya Verma", "SDE @ TCS", "Java, Backend"},
                {"KS", "Kiara Singh", "Data Analyst @ Infosys", "Data Science"},
                {"VP", "Vihaan Patel", "Cyber Analyst @ Wipro", "Security"}
        };

        Color[] mentorColors = {ACCENT_BLUE, STAT_PURPLE, SUCCESS_GREEN};

        int my = 52;
        for (int i = 0; i < mentors.length; i++) {
            mentorCard.add(buildMentorRow(
                    mentors[i][0], mentors[i][1], mentors[i][2], mentors[i][3],
                    mentorColors[i], my, 356
            ));
            my += 56;
        }

        panel.add(mentorCard);

        JPanel internCard = makeWhiteCard();
        internCard.setBounds(436, 295, 408, 220);
        addSectionTitle(internCard, "💼 Internship Opportunities", 20, 18);

        String[][] jobs = {
                {"Backend Intern", "Google", "Remote · 3 months"},
                {"Data Analyst Intern", "Flipkart", "Bangalore · 6 months"},
                {"Frontend Intern", "Zomato", "Hybrid · 2 months"},
                {"Cybersecurity Intern", "Wipro", "Noida · 4 months"}
        };

        int jy = 52;
        for (String[] job : jobs) {
            internCard.add(buildJobRow(job[0], job[1], job[2], jy, 368));
            jy += 42;
        }

        panel.add(internCard);

        JPanel courseCard = makeWhiteCard();
        courseCard.setBounds(24, 540, 396, 220);
        addSectionTitle(courseCard, "📚 Learning Progress", 20, 18);

        String[][] courses = {
                {"Java OOP", "80"},
                {"DBMS / MySQL", "65"},
                {"Frontend Development", "72"},
                {"Data Communication", "58"}
        };

        int cy = 55;
        Color[] courseColors = {ACCENT_BLUE, SUCCESS_GREEN, STAT_PURPLE, STAT_ORANGE};

        for (int i = 0; i < courses.length; i++) {
            courseCard.add(buildProgressRow(
                    courses[i][0],
                    Integer.parseInt(courses[i][1]),
                    courseColors[i],
                    cy,
                    356
            ));
            cy += 42;
        }

        panel.add(courseCard);

        JPanel activityCard = makeWhiteCard();
        activityCard.setBounds(436, 540, 408, 220);
        addSectionTitle(activityCard, "🔔 Recent Activity", 20, 18);

        String[][] activities = {
                {"✅", "Applied to Backend Intern role", "Today"},
                {"💬", "New message from Aditya Verma", "Yesterday"},
                {"📚", "Completed Java OOP module", "2 days ago"},
                {"👥", "Connected with Kiara Singh", "3 days ago"}
        };

        int ay = 55;
        for (String[] a : activities) {
            activityCard.add(buildActivityRow(a[0], a[1], a[2], ay, 368));
            ay += 42;
        }

        panel.add(activityCard);

        JPanel actionCard = makeWhiteCard();
        actionCard.setBounds(24, 785, 820, 90);
        addSectionTitle(actionCard, "⚡ Quick Actions", 20, 16);

        String[] actions = {
                "👤 My Profile",
                "👥 Find Alumni",
                "💼 Apply Internship",
                "💬 Messages",
                "📚 Courses",
                "🏠 Home"
        };

        int ax = 16;
        for (String act : actions) {
            JButton btn = buildQuickActionBtn(act);
            btn.setBounds(ax, 42, 125, 36);

            if (act.contains("Home")) {
                btn.addActionListener(e -> {
                    dispose();
                    new HomePage(userName, "student", userId);
                });
            } else if (act.contains("Profile")) {
                btn.addActionListener(e -> new StudentProfilePage(userId));
            } else {
                btn.addActionListener(e ->
                        JOptionPane.showMessageDialog(this, act + " feature will be added later."));
            }

            actionCard.add(btn);
            ax += 133;
        }

        panel.add(actionCard);

        return panel;
    }

    private JPanel buildStatCard(String title, String value, String sub, Color accent, String icon) {
        JPanel card = makeWhiteCard();

        JLabel ic = new JLabel(icon);
        ic.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 22));
        ic.setBounds(14, 12, 32, 32);
        card.add(ic);

        JLabel ttl = new JLabel(title);
        ttl.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        ttl.setForeground(LABEL_LIGHT);
        ttl.setBounds(14, 30, 150, 16);
        card.add(ttl);

        JLabel val = new JLabel(value);
        val.setFont(new Font("Georgia", Font.BOLD, 24));
        val.setForeground(LABEL_DARK);
        val.setBounds(14, 48, 130, 30);
        card.add(val);

        JLabel sb = new JLabel(sub);
        sb.setFont(new Font("Segoe UI", Font.BOLD, 11));
        sb.setForeground(accent);
        sb.setBounds(14, 76, 150, 16);
        card.add(sb);

        return card;
    }

    private JPanel buildMentorRow(String initials, String name, String role, String skills, Color color, int y, int w) {
        JPanel row = new JPanel(null);
        row.setOpaque(false);
        row.setBounds(14, y, w, 50);

        JLabel av = avatar(initials, color, 38);
        av.setBounds(0, 6, 38, 38);
        row.add(av);

        JLabel nm = new JLabel(name);
        nm.setFont(new Font("Segoe UI", Font.BOLD, 13));
        nm.setForeground(LABEL_DARK);
        nm.setBounds(46, 4, 180, 18);
        row.add(nm);

        JLabel info = new JLabel(role + " · " + skills);
        info.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        info.setForeground(LABEL_LIGHT);
        info.setBounds(46, 22, 210, 16);
        row.add(info);

        JButton connect = buildActionBtn("Connect", color);
        connect.setBounds(w - 88, 12, 76, 26);
        row.add(connect);

        return row;
    }

    private JPanel buildJobRow(String title, String company, String detail, int y, int w) {
        JPanel row = new JPanel(null);
        row.setOpaque(false);
        row.setBounds(14, y, w, 36);

        JLabel t = new JLabel(title);
        t.setFont(new Font("Segoe UI", Font.BOLD, 12));
        t.setForeground(LABEL_DARK);
        t.setBounds(0, 0, 220, 16);
        row.add(t);

        JLabel d = new JLabel(company + " · " + detail);
        d.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        d.setForeground(LABEL_LIGHT);
        d.setBounds(0, 17, 250, 16);
        row.add(d);

        JButton apply = buildActionBtn("Apply", SUCCESS_GREEN);
        apply.setBounds(w - 75, 5, 65, 24);
        row.add(apply);

        return row;
    }

    private JPanel buildProgressRow(String title, int pct, Color color, int y, int w) {
        JPanel row = new JPanel(null);
        row.setOpaque(false);
        row.setBounds(14, y, w, 35);

        JLabel name = new JLabel(title);
        name.setFont(new Font("Segoe UI", Font.BOLD, 12));
        name.setForeground(LABEL_DARK);
        name.setBounds(0, 0, 180, 16);
        row.add(name);

        JLabel p = new JLabel(pct + "%");
        p.setFont(new Font("Segoe UI", Font.BOLD, 11));
        p.setForeground(color);
        p.setBounds(w - 45, 0, 45, 16);
        row.add(p);

        JPanel bar = new JPanel(null) {
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                g2.setColor(new Color(0xE8, 0xEE, 0xF4));
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 8, 8);

                g2.setColor(color);
                g2.fillRoundRect(0, 0, (int) (getWidth() * pct / 100.0), getHeight(), 8, 8);

                g2.dispose();
            }
        };

        bar.setOpaque(false);
        bar.setBounds(0, 22, w - 20, 8);
        row.add(bar);

        return row;
    }

    private JPanel buildActivityRow(String icon, String text, String time, int y, int w) {
        JPanel row = new JPanel(null);
        row.setOpaque(false);
        row.setBounds(14, y, w, 36);

        JLabel ic = new JLabel(icon);
        ic.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 16));
        ic.setBounds(0, 5, 30, 24);
        row.add(ic);

        JLabel msg = new JLabel(text);
        msg.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        msg.setForeground(LABEL_DARK);
        msg.setBounds(34, 4, 250, 16);
        row.add(msg);

        JLabel tm = new JLabel(time);
        tm.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        tm.setForeground(LABEL_LIGHT);
        tm.setBounds(34, 20, 160, 14);
        row.add(tm);

        return row;
    }

    private JButton buildActionBtn(String label, Color color) {
        JButton btn = new JButton(label);
        btn.setFont(new Font("Segoe UI", Font.BOLD, 11));
        btn.setForeground(color);
        btn.setBorderPainted(false);
        btn.setContentAreaFilled(false);
        btn.setFocusPainted(false);
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        return btn;
    }

    private JButton buildQuickActionBtn(String label) {
        JButton btn = new JButton(label);
        btn.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        btn.setForeground(LABEL_DARK);
        btn.setBackground(new Color(0xF5, 0xF7, 0xFA));
        btn.setFocusPainted(false);
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        return btn;
    }

    private JPanel makeWhiteCard() {
        JPanel card = new JPanel(null) {
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                g2.setColor(new Color(0, 0, 0, 10));
                g2.fillRoundRect(3, 4, getWidth() - 3, getHeight() - 4, 18, 18);

                g2.setColor(CARD_BG);
                g2.fillRoundRect(0, 0, getWidth() - 3, getHeight() - 4, 18, 18);

                g2.dispose();
            }
        };

        card.setOpaque(false);
        return card;
    }

    private JLabel avatar(String text, Color color, int size) {
        JLabel lbl = new JLabel(text, SwingConstants.CENTER) {
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                g2.setColor(new Color(color.getRed(), color.getGreen(), color.getBlue(), 28));
                g2.fillOval(0, 0, size, size);

                g2.setColor(color);
                g2.setFont(new Font("Georgia", Font.BOLD, 13));

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
        lbl.setFont(new Font("Segoe UI", Font.BOLD, 14));
        lbl.setForeground(LABEL_DARK);
        lbl.setBounds(x, y, 400, 20);
        parent.add(lbl);
    }

    private String firstName() {
        if (userName == null || userName.trim().isEmpty()) return "Student";
        return userName.trim().split("\\s+")[0];
    }
}