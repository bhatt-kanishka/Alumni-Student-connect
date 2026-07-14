import java.awt.*;
import java.awt.event.*;
import java.sql.*;
import javax.swing.*;
import javax.swing.border.*;

public class HomePage extends JFrame {

    private static final Color DARK_BLUE = new Color(0x00356B);
    private static final Color DARKER_BLUE = new Color(0x002147);
    private static final Color LIGHT_BLUE = new Color(0x1A5FA8);
    private static final Color BEIGE = new Color(0xFAF0CA);
    private static final Color WHITE = Color.WHITE;
    private static final Color MUTED = new Color(0x8A9DB5);
    private static final Color BG = new Color(0xF0F4F8);
    private static final Color GREEN = new Color(0x2D7D46);
    private static final Color ORANGE = new Color(0xB45309);

    private String userName;
    private String userRole;
    private int userId;
    private JPanel tabContent;
    private CardLayout cardLayout;
    private JButton suggestionsTab, exploreTab;

    public HomePage(String name, String role, int userId) {
    this.userName = name;
    this.userRole = role;
    this.userId = userId;
    initUI();
}

    private void initUI() {
        setTitle("AlumniConnect - Home");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1100, 750);
        setMinimumSize(new Dimension(900, 600));
        setLocationRelativeTo(null);

        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(BG);

        mainPanel.add(buildTopBar(), BorderLayout.NORTH);
        mainPanel.add(buildTabsBar(), BorderLayout.CENTER);

        setContentPane(mainPanel);
        setVisible(true);
    }

private JPanel buildTopBar() {
    JPanel bar = new JPanel(new BorderLayout());
    bar.setBackground(DARKER_BLUE);
    bar.setBorder(new EmptyBorder(10, 20, 10, 20));
    bar.setPreferredSize(new Dimension(0, 68));

    JPanel leftPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
    leftPanel.setOpaque(false);
    leftPanel.add(buildAvatar(userName, 44));
    bar.add(leftPanel, BorderLayout.WEST);

    JLabel title = new JLabel("Alumni Connect", SwingConstants.CENTER);
    title.setFont(new Font("SansSerif", Font.BOLD, 22));
    title.setForeground(BEIGE);
    bar.add(title, BorderLayout.CENTER);

    JPanel rightPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
    rightPanel.setOpaque(false);

    JButton dashboardBtn = createNavButton("Dashboard", GREEN, WHITE);
    dashboardBtn.addActionListener(e -> openDashboard());

    JButton profileBtn = createNavButton("My Profile", LIGHT_BLUE, WHITE);
    profileBtn.addActionListener(e -> openProfile());

    JButton logoutBtn = createNavButton("Logout", new Color(0xCC3333), WHITE);
    logoutBtn.addActionListener(e -> logout());

    rightPanel.add(dashboardBtn);
    rightPanel.add(profileBtn);
    rightPanel.add(logoutBtn);

    bar.add(rightPanel, BorderLayout.EAST);

    return bar;
}

    private JPanel buildTabsBar() {
        JPanel wrapper = new JPanel(new BorderLayout());
        wrapper.setBackground(BG);

        JPanel tabsRow = new JPanel(new BorderLayout());
        tabsRow.setPreferredSize(new Dimension(0, 46));
        tabsRow.setBackground(DARK_BLUE);

        JPanel tabBtns = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        tabBtns.setOpaque(false);

        suggestionsTab = createTabButton("Suggestions", true);
        exploreTab = createTabButton("Explore", false);

        suggestionsTab.addActionListener(e -> switchTab("suggestions", suggestionsTab, exploreTab));
        exploreTab.addActionListener(e -> switchTab("explore", exploreTab, suggestionsTab));

        tabBtns.add(suggestionsTab);
        tabBtns.add(exploreTab);
        tabsRow.add(tabBtns, BorderLayout.WEST);

        cardLayout = new CardLayout();
        tabContent = new JPanel(cardLayout);
        tabContent.setBackground(BG);
        tabContent.add(buildMainBody(), "suggestions");
        tabContent.add(buildExplorePanel(), "explore");

        wrapper.add(tabsRow, BorderLayout.NORTH);
        wrapper.add(tabContent, BorderLayout.CENTER);

        return wrapper;
    }

    private JButton createTabButton(String text, boolean active) {
        JButton btn = new JButton(text) {
            @Override
            protected void paintComponent(Graphics g) {
                boolean isActive = Boolean.TRUE.equals(getClientProperty("active"));

                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                        RenderingHints.VALUE_ANTIALIAS_ON);

                g2.setColor(isActive ? LIGHT_BLUE : DARK_BLUE);
                g2.fillRect(0, 0, getWidth(), getHeight());

                if (isActive) {
                    g2.setColor(BEIGE);
                    g2.fillRect(0, getHeight() - 3, getWidth(), 3);
                }

                g2.setColor(isActive ? WHITE : MUTED);
                g2.setFont(getFont());

                FontMetrics fm = g2.getFontMetrics();
                int x = (getWidth() - fm.stringWidth(getText())) / 2;
                int y = (getHeight() + fm.getAscent() - fm.getDescent()) / 2;

                g2.drawString(getText(), x, y);
                g2.dispose();
            }
        };

        btn.putClientProperty("active", active);
        btn.setFont(new Font("SansSerif", Font.BOLD, 13));
        btn.setPreferredSize(new Dimension(140, 46));
        btn.setBorderPainted(false);
        btn.setFocusPainted(false);
        btn.setContentAreaFilled(false);
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

        return btn;
    }

    private void switchTab(String card, JButton active, JButton inactive) {
        active.putClientProperty("active", true);
        inactive.putClientProperty("active", false);

        cardLayout.show(tabContent, card);

        active.repaint();
        inactive.repaint();
    }

    private JPanel buildMainBody() {
        JPanel body = new JPanel(new BorderLayout(12, 0));
        body.setBackground(BG);
        body.setBorder(new EmptyBorder(16, 18, 16, 18));

        JPanel feedOuter = new JPanel(new BorderLayout());
        feedOuter.setOpaque(false);

        JLabel feedLabel = new JLabel("  Latest Updates");
        feedLabel.setFont(new Font("SansSerif", Font.BOLD, 15));
        feedLabel.setForeground(DARK_BLUE);
        feedLabel.setBorder(new EmptyBorder(0, 0, 8, 0));

        feedOuter.add(feedLabel, BorderLayout.NORTH);
        feedOuter.add(buildFeed(), BorderLayout.CENTER);

        JPanel rightPanel = new JPanel(new BorderLayout());
        rightPanel.setOpaque(false);
        rightPanel.setPreferredSize(new Dimension(260, 0));
        rightPanel.add(buildSuggestionsPanel(), BorderLayout.CENTER);

        body.add(feedOuter, BorderLayout.CENTER);
        body.add(rightPanel, BorderLayout.EAST);

        return body;
    }

    private JScrollPane buildFeed() {
        JPanel feed = new JPanel();
        feed.setLayout(new BoxLayout(feed, BoxLayout.Y_AXIS));
        feed.setBackground(BG);

        String[][] posts = {
                {"TechCorp Inc.", "2 hours ago", "We're hiring! Looking for passionate software engineers to join our team in Dehradun. UPES alumni preferred. Apply now and grow with us! #Hiring #Tech #UPES"},
                {"Priya Sharma (Alumni '20)", "5 hours ago", "Just got promoted to Senior Product Manager at Zomato! Grateful for the foundation laid at UPES. Keep pushing your limits, juniors! 🎉 #Alumni #CareerGrowth"},
                {"UPES Placement Cell", "1 day ago", "Congratulations to the batch of 2024! Record placements this year with 95% students placed. Average CTC 8.4 LPA. Proud of you all! #Placements #UPES2024"},
                {"Rahul Verma (Student)", "2 days ago", "Excited to share that I'll be interning at Microsoft this summer! Couldn't have done it without the amazing mentors and the AlumniConnect network. Thank you! 💼 #Internship #Microsoft"}
        };

        for (String[] post : posts) {
            feed.add(buildPostCard(post[0], post[1], post[2]));
            feed.add(Box.createVerticalStrut(12));
        }

        JScrollPane scroll = new JScrollPane(feed);
        scroll.setBorder(BorderFactory.createEmptyBorder());
        scroll.setOpaque(false);
        scroll.getViewport().setOpaque(false);
        scroll.getVerticalScrollBar().setUnitIncrement(16);
        scroll.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);

        return scroll;
    }

    private JPanel buildPostCard(String author, String time, String content) {
        JPanel card = new RoundedPanel(14, WHITE);
        card.setLayout(new BorderLayout(0, 8));
        card.setBorder(new EmptyBorder(14, 16, 14, 16));
        card.setMaximumSize(new Dimension(Integer.MAX_VALUE, 240));
        card.setPreferredSize(new Dimension(700, 220));

        JPanel header = new JPanel(new BorderLayout());
        header.setOpaque(false);

        JPanel authorPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 0));
        authorPanel.setOpaque(false);
        authorPanel.add(buildAvatar(author, 36));

        JPanel authorInfo = new JPanel();
        authorInfo.setLayout(new BoxLayout(authorInfo, BoxLayout.Y_AXIS));
        authorInfo.setOpaque(false);

        JLabel nameLabel = new JLabel(author);
        nameLabel.setFont(new Font("SansSerif", Font.BOLD, 13));
        nameLabel.setForeground(DARK_BLUE);

        JLabel timeLabel = new JLabel(time);
        timeLabel.setFont(new Font("SansSerif", Font.PLAIN, 11));
        timeLabel.setForeground(MUTED);

        authorInfo.add(nameLabel);
        authorInfo.add(timeLabel);
        authorPanel.add(authorInfo);
        header.add(authorPanel, BorderLayout.WEST);

        JTextArea textArea = new JTextArea(content);
        textArea.setFont(new Font("SansSerif", Font.PLAIN, 13));
        textArea.setForeground(new Color(0x2D3748));
        textArea.setOpaque(false);
        textArea.setEditable(false);
        textArea.setLineWrap(true);
        textArea.setWrapStyleWord(true);
        textArea.setBorder(null);

        JPanel imgPlaceholder = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                        RenderingHints.VALUE_ANTIALIAS_ON);

                g2.setColor(new Color(0xE8EEF5));
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 8, 8);

                g2.setColor(MUTED);
                g2.setFont(new Font("SansSerif", Font.PLAIN, 12));

                String txt = "[ Image Placeholder ]";
                FontMetrics fm = g2.getFontMetrics();
                g2.drawString(txt, (getWidth() - fm.stringWidth(txt)) / 2, getHeight() / 2);

                g2.dispose();
            }
        };

        imgPlaceholder.setPreferredSize(new Dimension(0, 70));
        imgPlaceholder.setOpaque(false);

        JPanel actions = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 0));
        actions.setOpaque(false);

        actions.add(createLikeButton());
        actions.add(createCommentButton(author));
        actions.add(createShareButton());

        JPanel centerPanel = new JPanel();
        centerPanel.setLayout(new BoxLayout(centerPanel, BoxLayout.Y_AXIS));
        centerPanel.setOpaque(false);
        centerPanel.add(textArea);
        centerPanel.add(Box.createVerticalStrut(8));
        centerPanel.add(imgPlaceholder);

        card.add(header, BorderLayout.NORTH);
        card.add(centerPanel, BorderLayout.CENTER);
        card.add(actions, BorderLayout.SOUTH);

        addHoverEffect(card, WHITE, new Color(0xF7FAFF));

        return card;
    }

    private JPanel buildSuggestionsPanel() {
        JPanel panel = new RoundedPanel(14, WHITE);
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBorder(new EmptyBorder(14, 14, 14, 14));

        JLabel title = new JLabel("People You May Know");
        title.setFont(new Font("SansSerif", Font.BOLD, 14));
        title.setForeground(DARK_BLUE);
        title.setAlignmentX(Component.LEFT_ALIGNMENT);

        panel.add(title);
        panel.add(Box.createVerticalStrut(12));

        String[][] users = {
                {"Ananya Singh", "Alumni '21", "Product @ Flipkart"},
                {"Rohan Mehta", "Student", "B.Tech CSE, 3rd Year"},
                {"Kavita Nair", "Alumni '19", "SDE-2 @ Amazon"},
                {"Arjun Patel", "Student", "MBA 2nd Year"},
                {"Sunita Joshi", "Alumni '22", "Data Analyst @ Deloitte"}
        };

        for (String[] user : users) {
            panel.add(buildSuggestionCard(user[0], user[1], user[2]));
            panel.add(Box.createVerticalStrut(8));
        }

        return panel;
    }

    private JPanel buildSuggestionCard(String name, String badge, String role) {
        JPanel card = new JPanel(new BorderLayout(8, 0));
        card.setOpaque(false);
        card.setMaximumSize(new Dimension(Integer.MAX_VALUE, 58));
        card.setAlignmentX(Component.LEFT_ALIGNMENT);

        card.add(buildAvatar(name, 38), BorderLayout.WEST);

        JPanel info = new JPanel();
        info.setLayout(new BoxLayout(info, BoxLayout.Y_AXIS));
        info.setOpaque(false);

        JLabel nameLabel = new JLabel(name);
        nameLabel.setFont(new Font("SansSerif", Font.BOLD, 12));
        nameLabel.setForeground(DARK_BLUE);

        JLabel roleLabel = new JLabel(role);
        roleLabel.setFont(new Font("SansSerif", Font.PLAIN, 11));
        roleLabel.setForeground(MUTED);

        info.add(nameLabel);
        info.add(roleLabel);

        JButton connectBtn = createNavButton("Connect", LIGHT_BLUE, WHITE);
        connectBtn.setPreferredSize(new Dimension(84, 28));

        connectBtn.addActionListener(e -> {
            connectBtn.setText("Connected");
            connectBtn.setEnabled(false);
        });

        card.add(info, BorderLayout.CENTER);
        card.add(connectBtn, BorderLayout.EAST);

        return card;
    }

    private JPanel buildExplorePanel() {
        JPanel outer = new JPanel(new BorderLayout());
        outer.setBackground(BG);
        outer.setBorder(new EmptyBorder(16, 18, 16, 18));

        JPanel card = new RoundedPanel(14, WHITE);
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setBorder(new EmptyBorder(20, 20, 20, 20));

        JLabel heading = new JLabel("Explore Topics");
        heading.setFont(new Font("SansSerif", Font.BOLD, 18));
        heading.setForeground(DARK_BLUE);
        heading.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel sub = new JLabel("Click a category to explore content");
        sub.setFont(new Font("SansSerif", Font.PLAIN, 13));
        sub.setForeground(MUTED);
        sub.setAlignmentX(Component.LEFT_ALIGNMENT);

        card.add(heading);
        card.add(Box.createVerticalStrut(6));
        card.add(sub);
        card.add(Box.createVerticalStrut(20));

        String[][] categories = {
                {"💻 Technology", "#1A5FA8"},
                {"💰 Finance", "#2D7D46"},
                {"🚀 Startups", "#B45309"},
                {"📋 Internships", "#6B21A8"},
                {"🎓 Research", "#C2410C"},
                {"🌐 Networking", "#0E7490"}
        };

        JPanel tagsPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 10));
        tagsPanel.setOpaque(false);
        tagsPanel.setAlignmentX(Component.LEFT_ALIGNMENT);

        ButtonGroup group = new ButtonGroup();

        for (String[] cat : categories) {
            JToggleButton tag = createCategoryTag(cat[0], Color.decode(cat[1]));
            group.add(tag);
            tagsPanel.add(tag);
        }

        card.add(tagsPanel);
        card.add(Box.createVerticalStrut(20));

        JLabel infoLabel = new JLabel("Select a category to see related posts and opportunities.");
        infoLabel.setFont(new Font("SansSerif", Font.ITALIC, 13));
        infoLabel.setForeground(MUTED);
        infoLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

        card.add(infoLabel);
        outer.add(card, BorderLayout.NORTH);

        return outer;
    }

    private JToggleButton createCategoryTag(String text, Color color) {
        JToggleButton btn = new JToggleButton(text) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                        RenderingHints.VALUE_ANTIALIAS_ON);

                if (isSelected()) {
                    g2.setColor(color);
                    g2.fillRoundRect(0, 0, getWidth(), getHeight(), 24, 24);
                    g2.setColor(WHITE);
                } else {
                    g2.setColor(WHITE);
                    g2.fillRoundRect(0, 0, getWidth(), getHeight(), 24, 24);

                    g2.setColor(color);
                    g2.setStroke(new BasicStroke(1.5f));
                    g2.drawRoundRect(1, 1, getWidth() - 2, getHeight() - 2, 24, 24);

                    g2.setColor(color);
                }

                g2.setFont(getFont());
                FontMetrics fm = g2.getFontMetrics();

                g2.drawString(getText(),
                        (getWidth() - fm.stringWidth(getText())) / 2,
                        (getHeight() + fm.getAscent() - fm.getDescent()) / 2);

                g2.dispose();
            }
        };

        btn.setFont(new Font("SansSerif", Font.BOLD, 13));
        btn.setPreferredSize(new Dimension(160, 40));
        btn.setBorderPainted(false);
        btn.setFocusPainted(false);
        btn.setContentAreaFilled(false);
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

        return btn;
    }

    private JButton createLikeButton() {
        JButton btn = new JButton("👍 Like") {
            @Override
            protected void paintComponent(Graphics g) {
                boolean liked = Boolean.TRUE.equals(getClientProperty("liked"));

                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                        RenderingHints.VALUE_ANTIALIAS_ON);

                if (liked) {
                    g2.setColor(GREEN);
                    g2.fillRoundRect(0, 0, getWidth(), getHeight(), 14, 14);
                    g2.setColor(WHITE);
                } else {
                    g2.setColor(WHITE);
                    g2.fillRoundRect(0, 0, getWidth(), getHeight(), 14, 14);

                    g2.setColor(GREEN);
                    g2.setStroke(new BasicStroke(1.5f));
                    g2.drawRoundRect(1, 1, getWidth() - 2, getHeight() - 2, 14, 14);

                    g2.setColor(GREEN);
                }

                g2.setFont(getFont());
                FontMetrics fm = g2.getFontMetrics();

                g2.drawString(getText(),
                        (getWidth() - fm.stringWidth(getText())) / 2,
                        (getHeight() + fm.getAscent() - fm.getDescent()) / 2);

                g2.dispose();
            }
        };

        btn.putClientProperty("liked", false);
        btn.setFont(new Font("SansSerif", Font.BOLD, 11));
        btn.setPreferredSize(new Dimension(100, 30));
        btn.setBorderPainted(false);
        btn.setFocusPainted(false);
        btn.setContentAreaFilled(false);
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

        btn.addActionListener(e -> {
            boolean current = Boolean.TRUE.equals(btn.getClientProperty("liked"));
            btn.putClientProperty("liked", !current);
            btn.setText(!current ? "👍 Liked" : "👍 Like");
            btn.repaint();
        });

        return btn;
    }

    private JButton createCommentButton(String postAuthor) {
        JButton btn = createActionButton("💬 Comment", GREEN);

        btn.addActionListener(e -> {
            String comment = JOptionPane.showInputDialog(
                    this,
                    "Write your comment:",
                    "Add Comment",
                    JOptionPane.PLAIN_MESSAGE
            );

            if (comment != null && !comment.trim().isEmpty()) {
                saveComment(postAuthor, comment.trim());
                JOptionPane.showMessageDialog(this, "Comment saved successfully!");
            }
        });

        return btn;
    }

    private JButton createShareButton() {
        return createActionButton("↗ Share", ORANGE);
    }

    private JButton createActionButton(String text, Color color) {
        JButton btn = new JButton(text) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                        RenderingHints.VALUE_ANTIALIAS_ON);

                if (getModel().isRollover()) {
                    g2.setColor(color);
                    g2.fillRoundRect(0, 0, getWidth(), getHeight(), 14, 14);
                    g2.setColor(WHITE);
                } else {
                    g2.setColor(WHITE);
                    g2.fillRoundRect(0, 0, getWidth(), getHeight(), 14, 14);

                    g2.setColor(color);
                    g2.setStroke(new BasicStroke(1.2f));
                    g2.drawRoundRect(1, 1, getWidth() - 2, getHeight() - 2, 14, 14);

                    g2.setColor(color);
                }

                g2.setFont(getFont());
                FontMetrics fm = g2.getFontMetrics();

                g2.drawString(getText(),
                        (getWidth() - fm.stringWidth(getText())) / 2,
                        (getHeight() + fm.getAscent() - fm.getDescent()) / 2);

                g2.dispose();
            }
        };

        btn.setFont(new Font("SansSerif", Font.BOLD, 11));
        btn.setPreferredSize(new Dimension(100, 30));
        btn.setBorderPainted(false);
        btn.setFocusPainted(false);
        btn.setContentAreaFilled(false);
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

        return btn;
    }

    private JLabel buildAvatar(String name, int size) {
        String initials = getInitials(name);

        JLabel avatar = new JLabel(initials, SwingConstants.CENTER) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                        RenderingHints.VALUE_ANTIALIAS_ON);

                int s = Math.min(getWidth(), getHeight());

                g2.setColor(LIGHT_BLUE);
                g2.fillOval((getWidth() - s) / 2, (getHeight() - s) / 2, s, s);

                g2.setColor(WHITE);
                g2.setFont(new Font("SansSerif", Font.BOLD, s / 3));

                FontMetrics fm = g2.getFontMetrics();
                int x = (getWidth() - fm.stringWidth(initials)) / 2;
                int y = (getHeight() + fm.getAscent() - fm.getDescent()) / 2;

                g2.drawString(initials, x, y);
                g2.dispose();
            }
        };

        avatar.setPreferredSize(new Dimension(size, size));
        avatar.setMinimumSize(new Dimension(size, size));
        avatar.setMaximumSize(new Dimension(size, size));

        return avatar;
    }

    private String getInitials(String name) {
        if (name == null || name.isEmpty()) return "?";

        String[] parts = name.trim().split("\\s+");

        if (parts.length == 1) {
            return parts[0].substring(0, Math.min(2, parts[0].length())).toUpperCase();
        }

        return ("" + parts[0].charAt(0) + parts[1].charAt(0)).toUpperCase();
    }

    private JButton createNavButton(String text, Color bg, Color fg) {
        JButton btn = new JButton(text) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                        RenderingHints.VALUE_ANTIALIAS_ON);

                Color c = getModel().isRollover() ? bg.brighter() : bg;

                g2.setColor(c);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 20, 20);

                g2.setColor(fg);
                g2.setFont(getFont());

                FontMetrics fm = g2.getFontMetrics();

                g2.drawString(getText(),
                        (getWidth() - fm.stringWidth(getText())) / 2,
                        (getHeight() + fm.getAscent() - fm.getDescent()) / 2);

                g2.dispose();
            }
        };

        btn.setFont(new Font("SansSerif", Font.BOLD, 12));
        btn.setPreferredSize(new Dimension(100, 34));
        btn.setBorderPainted(false);
        btn.setFocusPainted(false);
        btn.setContentAreaFilled(false);
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

        return btn;
    }

    private void addHoverEffect(JPanel card, Color normal, Color hover) {
        card.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                ((RoundedPanel) card).setPanelColor(hover);
                card.repaint();
            }

            @Override
            public void mouseExited(MouseEvent e) {
                ((RoundedPanel) card).setPanelColor(normal);
                card.repaint();
            }
        });
    }

    private void saveComment(String postAuthor, String commentText) {
        try {
            Connection con = DBConnection.getConnection();

            String query = "INSERT INTO comments (post_author, comment_text) VALUES (?, ?)";
            PreparedStatement ps = con.prepareStatement(query);

            ps.setString(1, postAuthor);
            ps.setString(2, commentText);

            ps.executeUpdate();
            con.close();

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error saving comment: " + ex.getMessage());
        }
    }

  

private void openDashboard() {
    try {
        if (userRole.equalsIgnoreCase("student")) {
            new StudentDashboard(userId, userName);
        } 
        else if (userRole.equalsIgnoreCase("alumni")) {
            new AlumniDashboard(userId);
        } 
        else if (
            userRole.equalsIgnoreCase("IT_ADMIN") ||
            userRole.equalsIgnoreCase("PROFESSOR") ||
            userRole.equalsIgnoreCase("HOD") ||
            userRole.equalsIgnoreCase("APO_ADMIN") ||
            userRole.equalsIgnoreCase("PLACEMENT")
        ) {
            new AdminDashboard(userId);
        }
        else {
            JOptionPane.showMessageDialog(this,
                    "Unknown role: " + userRole,
                    "Role Error",
                    JOptionPane.ERROR_MESSAGE);
            return;
        }

        dispose();

    } catch (Exception ex) {
        ex.printStackTrace();
        JOptionPane.showMessageDialog(this,
                "Dashboard could not open:\n" + ex.getMessage());
    }
}

private void openProfile() {
    try {
        if (userRole.equalsIgnoreCase("student")) {
            new StudentProfilePage(userId);
        } 
        else if (userRole.equalsIgnoreCase("alumni")) {
            JOptionPane.showMessageDialog(this,
                    "Alumni profile page is not active yet.",
                    "Coming Soon",
                    JOptionPane.INFORMATION_MESSAGE);
        } 
        else {
            JOptionPane.showMessageDialog(this,
                    "Admin profile page is not active yet.",
                    "Coming Soon",
                    JOptionPane.INFORMATION_MESSAGE);
        }

    } catch (Exception ex) {
        ex.printStackTrace();
        JOptionPane.showMessageDialog(this,
                "Profile could not open:\n" + ex.getMessage(),
                "Profile Error",
                JOptionPane.ERROR_MESSAGE);
    }
}

    private void logout() {
        dispose();
        new LoginPage();
    }

    static class RoundedPanel extends JPanel {
        private int radius;
        private Color bg;

        RoundedPanel(int radius, Color bg) {
            this.radius = radius;
            this.bg = bg;
            setOpaque(false);
        }

        public void setPanelColor(Color c) {
            this.bg = c;
        }

        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();

            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                    RenderingHints.VALUE_ANTIALIAS_ON);

            g2.setColor(bg);
            g2.fillRoundRect(0, 0, getWidth(), getHeight(), radius, radius);

            g2.setColor(new Color(0, 0, 0, 15));
            g2.setStroke(new BasicStroke(1));
            g2.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, radius, radius);

            g2.dispose();
            super.paintComponent(g);
        }
    }
}