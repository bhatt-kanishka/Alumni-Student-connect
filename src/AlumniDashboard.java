import java.awt.*;
import javax.swing.*;

public class AlumniDashboard extends JFrame {

    static final Color ACCENT_BLUE   = new Color(0x18, 0x76, 0xD0);
    static final Color ACCENT_GOLD   = new Color(0xF5, 0xC5, 0x18);
    static final Color BG_LIGHT      = new Color(0xF2, 0xF5, 0xFA);
    static final Color WHITE         = Color.WHITE;
    static final Color LABEL_DARK    = new Color(0x12, 0x22, 0x34);
    static final Color LABEL_MID     = new Color(0x44, 0x55, 0x66);
    static final Color LABEL_LIGHT   = new Color(0x88, 0x99, 0xAA);
    static final Color FIELD_BG      = new Color(0xF5, 0xF7, 0xFA);
    static final Color SUCCESS_GREEN = new Color(0x1D, 0x9E, 0x75);
    static final Color ERROR_RED     = new Color(0xD0, 0x32, 0x32);
    static final Color CARD_BG       = Color.WHITE;
    static final Color STAT_PURPLE   = new Color(0x8B, 0x5C, 0xF6);
    static final Color STAT_GREEN    = new Color(0x10, 0xB9, 0x81);
    static final Color STAT_ORANGE   = new Color(0xF5, 0x9E, 0x0B);
    static final Color STAT_BLUE     = new Color(0x38, 0xBD, 0xF8);

    static final Color ALU_PRIMARY   = new Color(0x05, 0x7A, 0x55);
    static final Color ALU_SECONDARY = new Color(0xD9, 0x77, 0x06);
    static final Color ALU_ACCENT    = new Color(0x1D, 0x4E, 0xD8);

    private final String userName;
    private final int userId;

    public AlumniDashboard(int userId) {
        this(userId, "Alumni");
    }

    public AlumniDashboard(int userId, String userName) {
        this.userId = userId;
        this.userName = userName;

        setTitle("AlumniConnect - Alumni Dashboard");
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
        panel.setPreferredSize(new Dimension(900, 1020));

        JPanel banner = new JPanel(null) {
            @Override protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                GradientPaint gp = new GradientPaint(0, 0, new Color(0x03, 0x4E, 0x37),
                        getWidth(), 0, new Color(0x07, 0x53, 0x85));
                g2.setPaint(gp);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 18, 18);
                g2.setColor(new Color(255, 255, 255, 12));
                g2.fillOval(getWidth() - 180, -60, 240, 240);
                g2.fillOval(getWidth() - 70, 30, 140, 140);
                g2.setColor(new Color(255, 200, 0, 15));
                g2.fillOval(-30, -30, 120, 120);
                g2.dispose();
            }
        };
        banner.setBounds(24, 24, 820, 130);
        banner.setOpaque(false);

        JLabel briefIcon = new JLabel("💼");
        briefIcon.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 38));
        briefIcon.setBounds(24, 30, 54, 54);
        banner.add(briefIcon);

        JLabel greet = new JLabel("Welcome back, " + firstName() + "!");
        greet.setFont(new Font("Georgia", Font.BOLD, 24));
        greet.setForeground(WHITE);
        greet.setBounds(86, 16, 500, 32);
        banner.add(greet);

        JLabel sub = new JLabel("You have 3 mentorship requests and 2 new connection opportunities.");
        sub.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        sub.setForeground(new Color(0xA7, 0xF3, 0xD0));
        sub.setBounds(86, 50, 560, 20);
        banner.add(sub);

        JLabel badgeLbl = new JLabel("🏆 Verified Alumni Mentor");
        badgeLbl.setFont(new Font("Segoe UI", Font.BOLD, 11));
        badgeLbl.setForeground(ACCENT_GOLD);
        badgeLbl.setBounds(86, 82, 200, 20);
        banner.add(badgeLbl);

        JLabel impactLbl = new JLabel("✨ You've helped 24 students so far!");
        impactLbl.setFont(new Font("Segoe UI", Font.BOLD, 11));
        impactLbl.setForeground(new Color(0xA7, 0xF3, 0xD0));
        impactLbl.setBounds(298, 82, 240, 20);
        banner.add(impactLbl);

        panel.add(banner);

        int cardY = 174, cardH = 96, gap = 16;
        JPanel[] statCards = {
                buildStatCard("Students Mentored", "24", "+3 this month", ALU_PRIMARY, "🎓"),
                buildStatCard("Profile Views", "3,812", "+18% this week", ALU_ACCENT, "👁"),
                buildStatCard("Network Size", "1,240", "+32 new", STAT_PURPLE, "🌐"),
                buildStatCard("Endorsements", "87", "+5 this week", ALU_SECONDARY, "⭐")
        };

        int initialCardW = (820 - 3 * gap) / 4;
        for (int i = 0; i < statCards.length; i++) {
            statCards[i].setBounds(24 + i * (initialCardW + gap), cardY, initialCardW, cardH);
            panel.add(statCards[i]);
        }

        JPanel reqCard = makeWhiteCard();
        reqCard.setBounds(24, 290, 396, 220);
        addSectionTitle(reqCard, "📬 Mentorship Requests", 20, 18);

        JLabel reqCount = new JLabel("3 pending");
        reqCount.setFont(new Font("Segoe UI", Font.BOLD, 12));
        reqCount.setForeground(ERROR_RED);
        reqCount.setBounds(296, 18, 90, 18);
        reqCard.add(reqCount);

        String[][] requests = {
                {"RS", "Rahul Sharma", "B.Tech CSE, 3rd Year", "Java, Backend"},
                {"AP", "Ananya Patel", "MCA, 2nd Year", "Data Science"},
                {"VK", "Vikram Kumar", "B.Tech IT, Final Year", "Product Mgmt"}
        };

        Color[] rqColors = {ALU_ACCENT, STAT_PURPLE, STAT_GREEN};
        int rqy = 48;
        for (int i = 0; i < requests.length; i++) {
            reqCard.add(buildRequestRow(requests[i][0], requests[i][1], requests[i][2], requests[i][3], rqColors[i], rqy, 356));
            rqy += 56;
        }
        panel.add(reqCard);

        JPanel schedCard = makeWhiteCard();
        schedCard.setBounds(436, 290, 408, 220);
        addSectionTitle(schedCard, "🗓 My Schedule", 20, 18);

        JLabel addSlot = makeLink("+ Add slot");
        addSlot.setBounds(316, 18, 80, 18);
        schedCard.add(addSlot);

        String[][] sessions = {
                {"📞", "Call: Rahul Sharma", "Today  3:00 PM", "In 2h"},
                {"💬", "Chat: Ananya Patel", "Tomorrow 11AM", "Tomorrow"},
                {"📋", "Review: Vikram's Resume", "Thu  2:00 PM", "Thu"},
                {"🎤", "Webinar: Career in Cloud", "Fri  5:00 PM", "Fri"},
                {"✅", "Group Q&A: Batch 2025", "Sat  10:00 AM", "Sat"}
        };

        Color[] sessColors = {ALU_PRIMARY, ALU_ACCENT, STAT_PURPLE, ALU_SECONDARY, STAT_GREEN};
        int sy = 50;
        for (int i = 0; i < sessions.length; i++) {
            schedCard.add(buildScheduleRow(sessions[i][0], sessions[i][1], sessions[i][2], sessions[i][3], sessColors[i], sy, 368));
            sy += 34;
        }
        panel.add(schedCard);

        JPanel studentsCard = makeWhiteCard();
        studentsCard.setBounds(24, 530, 396, 230);
        addSectionTitle(studentsCard, "🎓 Students I'm Mentoring", 20, 18);

        JLabel viewAll = makeLink("View all →");
        viewAll.setBounds(300, 18, 86, 18);
        studentsCard.add(viewAll);

        String[][] students = {
                {"RS", "Rahul Sharma", "SDE Track", "Session 5 of 10", "50"},
                {"NP", "Neha Pande", "Data Science", "Session 8 of 10", "80"},
                {"AK", "Arjun Khanna", "PM Track", "Session 2 of 10", "20"},
                {"SK", "Sonal Kapoor", "Cloud Arch.", "Session 6 of 10", "60"}
        };

        Color[] stColors = {ALU_ACCENT, STAT_GREEN, STAT_PURPLE, ALU_SECONDARY};
        int sty = 48;
        for (int i = 0; i < students.length; i++) {
            studentsCard.add(buildStudentRow(students[i][0], students[i][1], students[i][2], students[i][3],
                    Integer.parseInt(students[i][4]), stColors[i], sty, 356));
            sty += 46;
        }
        panel.add(studentsCard);

        JPanel impactCard = makeWhiteCard();
        impactCard.setBounds(436, 530, 408, 230);
        addSectionTitle(impactCard, "📣 My Posts & Impact", 20, 18);

        JLabel newPost = makeLink("+ New Post");
        newPost.setBounds(306, 18, 90, 18);
        impactCard.add(newPost);

        String[][] posts = {
                {"How I cracked FAANG: My prep strategy 🚀", "3.2k views  ·  142 likes"},
                {"Top 5 skills for backend engineers in 2024", "1.8k views  ·  87 likes"},
                {"Why mentorship matters more than grades", "5.1k views  ·  231 likes"}
        };

        Color[] postColors = {ALU_ACCENT, STAT_GREEN, ALU_PRIMARY};
        int py = 60;
        for (int i = 0; i < posts.length; i++) {
            impactCard.add(buildPostRow(posts[i][0], posts[i][1], postColors[i], py, 368));
            py += 44;
        }
        panel.add(impactCard);

        JPanel actCard = makeWhiteCard();
        actCard.setBounds(24, 780, 820, 90);
        addSectionTitle(actCard, "⚡ Quick Actions", 20, 16);

        String[][] qActions = {
                {"✏️ Edit Profile", "EDIT"},
                {"📅 Set Availability", "AVAIL"},
                {"📤 Share Post", "POST"},
                {"👥 Browse Students", "STUDENTS"},
                {"📊 View Analytics", "ANALYTICS"},
                {"🏠 Home", "HOME"}
        };

        int qax = 16;
        for (String[] qa : qActions) {
            JButton qb = buildQuickActionBtn(qa[0]);
            qb.setBounds(qax, 40, 125, 38);

            if (qa[1].equals("HOME")) {
                qb.addActionListener(e -> {
                    dispose();
                    new HomePage(userName, "alumni", userId);
                });
            } else if (qa[1].equals("EDIT")) {
                qb.addActionListener(e ->
                        JOptionPane.showMessageDialog(this, "Alumni profile page will be added later."));
            } else {
                qb.addActionListener(e ->
                        JOptionPane.showMessageDialog(this, qa[0] + " feature will be added later."));
            }

            actCard.add(qb);
            qax += 133;
        }
        panel.add(actCard);

        JPanel netwCard = makeWhiteCard();
        netwCard.setBounds(24, 890, 820, 110);
        addSectionTitle(netwCard, "🌐 Network Skill Cloud — Endorsed by Peers", 20, 18);

        JLabel skills = new JLabel("Java   Spring Boot   System Design   AWS   Leadership   Python   SQL   Microservices   REST APIs");
        skills.setFont(new Font("Segoe UI", Font.BOLD, 13));
        skills.setForeground(ALU_PRIMARY);
        skills.setBounds(25, 50, 780, 30);
        netwCard.add(skills);

        panel.add(netwCard);

        return panel;
    }

    private JPanel buildStatCard(String title, String value, String sub, Color accent, String icon) {
        JPanel card = makeWhiteCard();

        JLabel ic = new JLabel(icon);
        ic.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 22));
        ic.setBounds(14, 12, 32, 32);
        card.add(ic);

        JLabel val = new JLabel(value);
        val.setFont(new Font("Georgia", Font.BOLD, 24));
        val.setForeground(LABEL_DARK);
        val.setBounds(14, 42, 130, 30);
        card.add(val);

        JLabel ttl = new JLabel(title);
        ttl.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        ttl.setForeground(LABEL_LIGHT);
        ttl.setBounds(14, 28, 160, 16);
        card.add(ttl);

        JLabel sb = new JLabel(sub);
        sb.setFont(new Font("Segoe UI", Font.BOLD, 11));
        sb.setForeground(accent);
        sb.setBounds(14, 72, 160, 16);
        card.add(sb);

        return card;
    }

    private JPanel buildRequestRow(String initials, String name, String info, String skills, Color color, int y, int w) {
        JPanel row = new JPanel(null);
        row.setOpaque(false);
        row.setBounds(14, y, w, 50);

        JLabel av = avatar(initials, color, 38);
        av.setBounds(0, 6, 38, 38);
        row.add(av);

        JLabel nm = new JLabel(name);
        nm.setFont(new Font("Segoe UI", Font.BOLD, 13));
        nm.setForeground(LABEL_DARK);
        nm.setBounds(46, 4, 160, 17);
        row.add(nm);

        JLabel inf = new JLabel(info + " · " + skills);
        inf.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        inf.setForeground(LABEL_LIGHT);
        inf.setBounds(46, 21, 180, 14);
        row.add(inf);

        JButton accept = buildActionBtn("Accept", SUCCESS_GREEN);
        accept.setBounds(w - 168, 12, 76, 26);
        row.add(accept);

        JButton decline = buildActionBtn("Decline", new Color(0xCC, 0x44, 0x44));
        decline.setBounds(w - 86, 12, 72, 26);
        row.add(decline);

        return row;
    }

    private JPanel buildScheduleRow(String icon, String title, String time, String when, Color color, int y, int w) {
        JPanel row = new JPanel(null);
        row.setOpaque(false);
        row.setBounds(14, y, w, 30);

        JLabel ic = new JLabel(icon, SwingConstants.CENTER);
        ic.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 12));
        ic.setBounds(0, 2, 26, 26);
        row.add(ic);

        JLabel tl = new JLabel(title);
        tl.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        tl.setForeground(LABEL_DARK);
        tl.setBounds(34, 7, w - 200, 16);
        row.add(tl);

        JLabel tm = new JLabel(time);
        tm.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        tm.setForeground(LABEL_LIGHT);
        tm.setBounds(w - 168, 7, 120, 16);
        row.add(tm);

        JLabel wh = new JLabel(when);
        wh.setFont(new Font("Segoe UI", Font.BOLD, 11));
        wh.setForeground(color);
        wh.setBounds(w - 46, 7, 46, 16);
        wh.setHorizontalAlignment(SwingConstants.RIGHT);
        row.add(wh);

        return row;
    }

    private JPanel buildStudentRow(String initials, String name, String track, String sessions, int pct, Color color, int y, int w) {
        JPanel row = new JPanel(null);
        row.setOpaque(false);
        row.setBounds(14, y, w, 42);

        JLabel av = avatar(initials, color, 34);
        av.setBounds(0, 4, 34, 34);
        row.add(av);

        JLabel nm = new JLabel(name);
        nm.setFont(new Font("Segoe UI", Font.BOLD, 12));
        nm.setForeground(LABEL_DARK);
        nm.setBounds(42, 3, 160, 16);
        row.add(nm);

        JLabel tr = new JLabel(track + " · " + sessions);
        tr.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        tr.setForeground(LABEL_LIGHT);
        tr.setBounds(42, 19, 220, 14);
        row.add(tr);

        JLabel pctLbl = new JLabel(pct + "%");
        pctLbl.setFont(new Font("Segoe UI", Font.BOLD, 11));
        pctLbl.setForeground(color);
        pctLbl.setBounds(208, 30, 40, 14);
        row.add(pctLbl);

        JButton noteBtn = buildActionBtn("Note", ALU_ACCENT);
        noteBtn.setBounds(w - 78, 10, 64, 22);
        row.add(noteBtn);

        return row;
    }

    private JPanel buildPostRow(String title, String stats, Color color, int y, int w) {
        JPanel row = new JPanel(null);
        row.setOpaque(false);
        row.setBounds(14, y, w, 34);

        JLabel dot = new JLabel("▍");
        dot.setFont(new Font("Segoe UI", Font.BOLD, 16));
        dot.setForeground(color);
        dot.setBounds(0, 8, 12, 18);
        row.add(dot);

        JLabel tl = new JLabel(title);
        tl.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        tl.setForeground(LABEL_DARK);
        tl.setBounds(16, 4, w - 180, 16);
        row.add(tl);

        JLabel st = new JLabel(stats);
        st.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        st.setForeground(LABEL_LIGHT);
        st.setBounds(16, 20, w - 180, 14);
        row.add(st);

        JLabel view = makeLink("View →");
        view.setBounds(w - 70, 8, 70, 16);
        view.setHorizontalAlignment(SwingConstants.RIGHT);
        row.add(view);

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
        btn.setForeground(LABEL_MID);
        btn.setBorderPainted(false);
        btn.setContentAreaFilled(false);
        btn.setFocusPainted(false);
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        return btn;
    }

    private JPanel makeWhiteCard() {
        JPanel card = new JPanel(null) {
            @Override protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(new Color(0, 0, 0, 9));
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
            @Override protected void paintComponent(Graphics g) {
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
        lbl.setBounds(x, y, 440, 20);
        parent.add(lbl);
    }

    private JLabel makeLink(String text) {
        JLabel lbl = new JLabel(text);
        lbl.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        lbl.setForeground(ACCENT_BLUE);
        lbl.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        return lbl;
    }

    private String firstName() {
        if (userName == null || userName.trim().isEmpty()) return "Alumni";
        return userName.trim().split("\\s+")[0];
    }
}