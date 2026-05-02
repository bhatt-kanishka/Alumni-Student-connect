import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.sql.*;
import javax.imageio.ImageIO;

public class ProfilePage extends JFrame {

    // ── Palette ───────────────────────────────────────────────────
    static final Color YALE_BLUE     = new Color(0x00, 0x3B, 0x6B);
    static final Color YALE_HOVER    = new Color(0x00, 0x52, 0x96);
    static final Color YALE_LIGHT    = new Color(0xE6, 0xF1, 0xFB);
    static final Color LEMON_CHIFFON = new Color(0xFA, 0xF0, 0xCA);
    static final Color WHITE         = Color.WHITE;
    static final Color LABEL_DARK    = new Color(0x22, 0x33, 0x44);
    static final Color FIELD_BG      = new Color(0xF5, 0xF7, 0xFA);
    static final Color FIELD_BORDER  = new Color(0xC0, 0xCE, 0xDA);
    static final Color ERROR_RED     = new Color(0xD0, 0x32, 0x32);
    static final Color SUCCESS_GREEN = new Color(0x1D, 0x9E, 0x75);
    static final Color SKILL_BG      = new Color(0xE6, 0xF1, 0xFB);
    static final Color SKILL_TEXT    = new Color(0x0C, 0x44, 0x7C);
    static final Color DIVIDER       = new Color(0xE8, 0xEE, 0xF4);

    // ── User state ────────────────────────────────────────────────
    private int    userId;
    private String userName;
    private String userRole;
    private boolean editMode = false;
    private BufferedImage profilePhoto = null;

    // ── UI refs ───────────────────────────────────────────────────
    private JLabel        photoLabel;
    private JLabel        nameDisplay;
    private JLabel        roleDisplay;
    private JLabel        batchDisplay;
    private JTextArea     bioDisplay;
    private JPanel        skillsDisplay;

    // Edit-mode fields
    private JTextField    nameField;
    private JTextField    batchField;
    private JComboBox<String> roleBox;
    private JTextArea     bioField;
    private JTextField    skillsField;

    // Panels to swap
    private JPanel        viewPanel;
    private JPanel        editPanel;
    private JPanel        contentArea;

    private JButton       editBtn;
    private JButton       saveBtn;
    private JButton       cancelBtn;
    private JLabel        statusLabel;
    private JButton       mentorBtn;
    private JButton       logoutBtn;

    // ── Constructor: called from LoginPage with user id + name ────
    ProfilePage(String name, String role) {
        this.userName = name;
        this.userRole = role;
        // Fetch full profile from DB by name (or pass userId from LoginPage)
        buildUI();
        loadProfileFromDB();
    }

    ProfilePage(int userId, String name, String role) {
        this.userId   = userId;
        this.userName = name;
        this.userRole = role;
        buildUI();
        loadProfileFromDB();
    }

    // ── Build full UI ─────────────────────────────────────────────
    private void buildUI() {
        setTitle("AlumniConnect – Profile");
        setSize(1100, 720);
        setMinimumSize(new Dimension(900, 600));
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        // ── TOP NAV BAR ───────────────────────────────────────────
        JPanel navbar = new JPanel(null) {
            @Override protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                g.setColor(YALE_BLUE);
                g.fillRect(0, 0, getWidth(), getHeight());
            }
        };
        navbar.setPreferredSize(new Dimension(0, 56));

        JLabel logo = new JLabel("AlumniConnect");
        logo.setFont(new Font("Georgia", Font.BOLD, 20));
        logo.setForeground(WHITE);
        logo.setBounds(24, 14, 220, 28);
        navbar.add(logo);

        // Nav links
        String[] navItems = {"Profile", "Mentors", "Network"};
        int nx = 280;
        for (String item : navItems) {
            JButton nb = new JButton(item);
            nb.setFont(new Font("Segoe UI", Font.PLAIN, 13));
            nb.setForeground(item.equals("Profile")
                    ? new Color(0xFA, 0xF0, 0xCA)
                    : new Color(255, 255, 255, 160));
            nb.setBorderPainted(false);
            nb.setContentAreaFilled(false);
            nb.setFocusPainted(false);
            nb.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
            nb.setBounds(nx, 14, 90, 28);
            if (item.equals("Mentors")) {
                nb.addActionListener(e -> goToMentors());
            }
            navbar.add(nb);
            nx += 96;
        }

        logoutBtn = new JButton("Log out");
        logoutBtn.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        logoutBtn.setForeground(new Color(255, 255, 255, 200));
        logoutBtn.setBorderPainted(false);
        logoutBtn.setContentAreaFilled(false);
        logoutBtn.setFocusPainted(false);
        logoutBtn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        logoutBtn.addActionListener(e -> { dispose(); new LoginPage(); });

        // Position logout on right — use ComponentListener for dynamic width
        navbar.addComponentListener(new ComponentAdapter() {
            @Override public void componentResized(ComponentEvent e) {
                logoutBtn.setBounds(navbar.getWidth() - 100, 14, 80, 28);
            }
        });
        navbar.add(logoutBtn);
        add(navbar, BorderLayout.NORTH);

        // ── MAIN BODY ─────────────────────────────────────────────
        JPanel body = new JPanel(new BorderLayout()) {
            @Override protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                g.setColor(LEMON_CHIFFON);
                g.fillRect(0, 0, getWidth(), getHeight());
            }
        };

        // ── LEFT SIDEBAR ──────────────────────────────────────────
        JPanel sidebar = new JPanel(null) {
            @Override protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                                    RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(WHITE);
                g2.fillRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 0, 0);
                g2.dispose();
            }
        };
        sidebar.setPreferredSize(new Dimension(280, 0));
        sidebar.setBorder(BorderFactory.createMatteBorder(0, 0, 0, 1,
                new Color(0xE8, 0xEE, 0xF4)));

        // Profile photo circle
        photoLabel = new JLabel() {
            @Override protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                                    RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION,
                                    RenderingHints.VALUE_INTERPOLATION_BICUBIC);
                int w = getWidth(), h = getHeight();
                Shape circle = new Ellipse2D.Float(3, 3, w - 6, h - 6);
                // Shadow
                g2.setColor(new Color(0, 60, 120, 18));
                g2.fillOval(5, 6, w - 6, h - 6);
                // Photo or initials
                g2.setClip(circle);
                if (profilePhoto != null) {
                    g2.drawImage(profilePhoto, 3, 3, w-6, h-6, null);
                } else {
                    // Yale Blue background with initials
                    g2.setColor(YALE_BLUE);
                    g2.fillOval(3, 3, w-6, h-6);
                    g2.setClip(null);
                    g2.setColor(WHITE);
                    g2.setFont(new Font("Georgia", Font.BOLD, 40));
                    String initials = getInitials(userName);
                    FontMetrics fm = g2.getFontMetrics();
                    g2.drawString(initials,
                            (w - fm.stringWidth(initials)) / 2,
                            (h + fm.getAscent() - fm.getDescent()) / 2);
                }
                g2.setClip(null);
                // Border ring
                g2.setColor(YALE_BLUE);
                g2.setStroke(new BasicStroke(2.5f));
                g2.drawOval(3, 3, w-6, h-6);
                g2.dispose();
            }
        };
        photoLabel.setBounds(80, 40, 120, 120);
        photoLabel.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        photoLabel.setToolTipText("Click to change photo");
        photoLabel.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) { choosePhoto(); }
        });
        sidebar.add(photoLabel);

        // Camera icon hint
        JLabel photoHint = new JLabel("Click to change photo");
        photoHint.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        photoHint.setForeground(new Color(0x88, 0x88, 0x99));
        photoHint.setBounds(42, 164, 196, 16);
        sidebar.add(photoHint);

        // Name + role in sidebar
        nameDisplay = new JLabel(userName, SwingConstants.CENTER);
        nameDisplay.setFont(new Font("Georgia", Font.BOLD, 17));
        nameDisplay.setForeground(LABEL_DARK);
        nameDisplay.setBounds(20, 190, 240, 26);
        sidebar.add(nameDisplay);

        roleDisplay = new JLabel("", SwingConstants.CENTER);
        roleDisplay.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        roleDisplay.setForeground(new Color(0x55, 0x66, 0x77));
        roleDisplay.setBounds(20, 218, 240, 20);
        sidebar.add(roleDisplay);

        // Divider
        JSeparator sep = new JSeparator();
        sep.setBounds(24, 252, 232, 1);
        sep.setForeground(DIVIDER);
        sidebar.add(sep);

        // Batch display in sidebar
        JLabel batchTitleLbl = new JLabel("Batch Year");
        batchTitleLbl.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        batchTitleLbl.setForeground(new Color(0x88, 0x88, 0x99));
        batchTitleLbl.setBounds(24, 264, 120, 16);
        sidebar.add(batchTitleLbl);

        batchDisplay = new JLabel("–");
        batchDisplay.setFont(new Font("Segoe UI", Font.BOLD, 15));
        batchDisplay.setForeground(YALE_BLUE);
        batchDisplay.setBounds(24, 282, 232, 22);
        sidebar.add(batchDisplay);

        // Edit / Mentors / Save / Cancel buttons in sidebar
        editBtn = makeSideBtn("Edit Profile", false);
        editBtn.setBounds(24, 330, 232, 42);
        editBtn.addActionListener(e -> enterEditMode());
        sidebar.add(editBtn);

        mentorBtn = makeSideBtn("Browse Mentors", true);
        mentorBtn.setBounds(24, 382, 232, 42);
        mentorBtn.addActionListener(e -> goToMentors());
        sidebar.add(mentorBtn);

        saveBtn = makeSideBtn("Save Changes", false);
        saveBtn.setBounds(24, 330, 232, 42);
        saveBtn.setVisible(false);
        saveBtn.addActionListener(e -> saveProfile());
        sidebar.add(saveBtn);

        cancelBtn = new JButton("Cancel");
        cancelBtn.setBounds(24, 382, 232, 42);
        cancelBtn.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        cancelBtn.setForeground(new Color(0x55, 0x66, 0x77));
        cancelBtn.setBackground(new Color(0xF0, 0xF2, 0xF5));
        cancelBtn.setBorderPainted(false);
        cancelBtn.setFocusPainted(false);
        cancelBtn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        cancelBtn.setVisible(false);
        cancelBtn.addActionListener(e -> cancelEdit());
        sidebar.add(cancelBtn);

        statusLabel = new JLabel(" ");
        statusLabel.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        statusLabel.setForeground(ERROR_RED);
        statusLabel.setBounds(24, 432, 232, 16);
        statusLabel.setHorizontalAlignment(SwingConstants.CENTER);
        sidebar.add(statusLabel);

        body.add(sidebar, BorderLayout.WEST);

        // ── MAIN CONTENT AREA ─────────────────────────────────────
        contentArea = new JPanel(new CardLayout());
        contentArea.setOpaque(false);

        viewPanel = buildViewPanel();
        editPanel = buildEditPanel();

        contentArea.add(viewPanel, "VIEW");
        contentArea.add(editPanel, "EDIT");

        JScrollPane scrollPane = new JScrollPane(contentArea);
        scrollPane.setBorder(null);
        scrollPane.setOpaque(false);
        scrollPane.getViewport().setOpaque(false);
        scrollPane.getVerticalScrollBar().setUnitIncrement(12);
        body.add(scrollPane, BorderLayout.CENTER);

        add(body, BorderLayout.CENTER);
        setVisible(true);
    }

    // ── VIEW PANEL ────────────────────────────────────────────────
    private JPanel buildViewPanel() {
        JPanel panel = new JPanel(null) {
            @Override protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                g.setColor(LEMON_CHIFFON);
                g.fillRect(0, 0, getWidth(), getHeight());
            }
        };
        panel.setPreferredSize(new Dimension(700, 580));

        // Card wrapper
        JPanel card = makeCard();
        card.setBounds(32, 32, 600, 500);

        // Section: About
        JLabel aboutTitle = sectionTitle("About Me");
        aboutTitle.setBounds(28, 24, 200, 24);

        bioDisplay = new JTextArea("No bio added yet.");
        bioDisplay.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        bioDisplay.setForeground(LABEL_DARK);
        bioDisplay.setLineWrap(true);
        bioDisplay.setWrapStyleWord(true);
        bioDisplay.setEditable(false);
        bioDisplay.setOpaque(false);
        bioDisplay.setBorder(null);
        bioDisplay.setBounds(28, 56, 544, 80);

        JSeparator s1 = new JSeparator();
        s1.setBounds(28, 148, 544, 1);
        s1.setForeground(DIVIDER);

        // Section: Skills
        JLabel skillsTitle = sectionTitle("Skills");
        skillsTitle.setBounds(28, 162, 200, 24);

        skillsDisplay = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 6));
        skillsDisplay.setOpaque(false);
        skillsDisplay.setBounds(28, 192, 544, 80);
        // placeholder
        skillsDisplay.add(makeSkillChip("No skills added yet"));

        JSeparator s2 = new JSeparator();
        s2.setBounds(28, 284, 544, 1);
        s2.setForeground(DIVIDER);

        // Section: Details
        JLabel detailsTitle = sectionTitle("Details");
        detailsTitle.setBounds(28, 298, 200, 24);

        // Detail rows
        JPanel detailGrid = new JPanel(new GridLayout(0, 2, 16, 12));
        detailGrid.setOpaque(false);
        detailGrid.setBounds(28, 328, 544, 120);

        detailGrid.add(makeDetailKey("Name"));
        nameDisplay = new JLabel(userName);
        nameDisplay.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        nameDisplay.setForeground(LABEL_DARK);
        detailGrid.add(nameDisplay);

        detailGrid.add(makeDetailKey("Role"));
        roleDisplay = new JLabel(userRole != null ? capitalize(userRole) : "–");
        roleDisplay.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        roleDisplay.setForeground(LABEL_DARK);
        detailGrid.add(roleDisplay);

        detailGrid.add(makeDetailKey("Batch Year"));
        batchDisplay = new JLabel("–");
        batchDisplay.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        batchDisplay.setForeground(LABEL_DARK);
        detailGrid.add(batchDisplay);

        detailGrid.add(makeDetailKey("Status"));
        JLabel statusBadge = makeRoleBadge(userRole);
        detailGrid.add(statusBadge);

        card.add(aboutTitle);
        card.add(bioDisplay);
        card.add(s1);
        card.add(skillsTitle);
        card.add(skillsDisplay);
        card.add(s2);
        card.add(detailsTitle);
        card.add(detailGrid);

        panel.add(card);

        // Dynamic card sizing
        panel.addComponentListener(new ComponentAdapter() {
            @Override public void componentResized(ComponentEvent e) {
                int w = Math.min(panel.getWidth() - 64, 700);
                card.setBounds(32, 32, w, 500);
                bioDisplay.setBounds(28, 56, w-56, 80);
                skillsDisplay.setBounds(28, 192, w-56, 80);
                s1.setBounds(28, 148, w-56, 1);
                s2.setBounds(28, 284, w-56, 1);
                detailGrid.setBounds(28, 328, w-56, 120);
            }
        });

        return panel;
    }

    // ── EDIT PANEL ────────────────────────────────────────────────
    private JPanel buildEditPanel() {
        JPanel panel = new JPanel(null) {
            @Override protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                g.setColor(LEMON_CHIFFON);
                g.fillRect(0, 0, getWidth(), getHeight());
            }
        };
        panel.setPreferredSize(new Dimension(700, 600));

        JPanel card = makeCard();
        card.setBounds(32, 32, 600, 520);

        JLabel editTitle = new JLabel("Edit Profile");
        editTitle.setFont(new Font("Georgia", Font.BOLD, 20));
        editTitle.setForeground(YALE_BLUE);
        editTitle.setBounds(28, 20, 300, 30);
        card.add(editTitle);

        Font lf = new Font("Segoe UI", Font.PLAIN, 12);
        Font inf = new Font("Segoe UI", Font.PLAIN, 14);

        // Full Name
        card.add(makeEditLabel("Full Name", lf, 28, 64));
        nameField = makeEditField(inf);
        nameField.setBounds(28, 82, 544, 42);
        card.add(nameField);

        // Batch + Role row
        card.add(makeEditLabel("Batch Year", lf, 28, 140));
        batchField = makeEditField(inf);
        batchField.setBounds(28, 158, 160, 42);
        card.add(batchField);

        card.add(makeEditLabel("Role", lf, 212, 140));
        roleBox = new JComboBox<>(new String[]{"student", "alumni"});
        styleComboBox(roleBox, inf);
        roleBox.setBounds(212, 158, 360, 42);
        card.add(roleBox);

        // Bio
        card.add(makeEditLabel("Bio  (tell others about yourself)", lf, 28, 216));
        bioField = new JTextArea();
        bioField.setFont(inf);
        bioField.setLineWrap(true);
        bioField.setWrapStyleWord(true);
        bioField.setBorder(BorderFactory.createEmptyBorder(10, 14, 10, 14));
        JScrollPane bioScroll = new JScrollPane(bioField) {
            @Override protected void paintBorder(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                                    RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(FIELD_BG);
                g2.fillRoundRect(0,0,getWidth(),getHeight(),16,16);
                g2.setColor(FIELD_BORDER);
                g2.setStroke(new BasicStroke(1.3f));
                g2.drawRoundRect(0,0,getWidth()-1,getHeight()-1,16,16);
                g2.dispose();
            }
        };
        bioScroll.setBorder(null);
        bioScroll.setOpaque(false);
        bioScroll.getViewport().setOpaque(false);
        bioField.setOpaque(false);
        bioScroll.setBounds(28, 234, 544, 100);
        card.add(bioScroll);

        // Skills
        card.add(makeEditLabel("Skills  (comma-separated, e.g. Java, SQL, Python)", lf, 28, 348));
        skillsField = makeEditField(inf);
        skillsField.setBounds(28, 366, 544, 42);
        card.add(skillsField);

        JLabel hint = new JLabel("Tip: separate each skill with a comma");
        hint.setFont(new Font("Segoe UI", Font.ITALIC, 11));
        hint.setForeground(new Color(0xAA, 0xAA, 0xBB));
        hint.setBounds(28, 412, 300, 16);
        card.add(hint);

        panel.add(card);

        panel.addComponentListener(new ComponentAdapter() {
            @Override public void componentResized(ComponentEvent e) {
                int w = Math.min(panel.getWidth()-64, 700);
                card.setBounds(32, 32, w, 520);
                nameField.setBounds(28, 82, w-56, 42);
                bioScroll.setBounds(28, 234, w-56, 100);
                skillsField.setBounds(28, 366, w-56, 42);
                roleBox.setBounds(212, 158, w-240, 42);
            }
        });

        return panel;
    }

    // ── LOAD PROFILE FROM DB ──────────────────────────────────────
    private void loadProfileFromDB() {
        new SwingWorker<Object[], Void>() {
            @Override protected Object[] doInBackground() {
                try (Connection con = DBConnection.getConnection()) {
                    String sql;
                    PreparedStatement ps;
                    if (userId > 0) {
                        sql = "SELECT name,email,role,batch,bio,skills,photo " +
                              "FROM users WHERE id=?";
                        ps = con.prepareStatement(sql);
                        ps.setInt(1, userId);
                    } else {
                        sql = "SELECT name,email,role,batch,bio,skills,photo " +
                              "FROM users WHERE name=?";
                        ps = con.prepareStatement(sql);
                        ps.setString(1, userName);
                    }
                    ResultSet rs = ps.executeQuery();
                    if (rs.next()) {
                        return new Object[]{
                            rs.getString("name"),
                            rs.getString("role"),
                            rs.getObject("batch"),  // may be null
                            rs.getString("bio"),
                            rs.getString("skills"),
                            rs.getString("photo")
                        };
                    }
                } catch (Exception ex) { ex.printStackTrace(); }
                return null;
            }

            @Override protected void done() {
                try {
                    Object[] data = get();
                    if (data == null) return;

                    String name   = (String)  data[0];
                    String role   = (String)  data[1];
                    Object batchO = data[2];
                    String bio    = (String)  data[3];
                    String skills = (String)  data[4];
                    String photo  = (String)  data[5];

                    userName = name != null ? name : userName;
                    userRole = role != null ? role : userRole;

                    // Update sidebar
                    nameDisplay.setText(userName);
                    roleDisplay.setText(capitalize(userRole));
                    batchDisplay.setText(batchO != null ? batchO.toString() : "–");

                    // Update bio
                    bioDisplay.setText((bio != null && !bio.isEmpty())
                            ? bio : "No bio added yet.");

                    // Update skills chips
                    refreshSkillChips(skills);

                    // Load photo
                    if (photo != null && !photo.isEmpty()) {
                        try {
                            profilePhoto = ImageIO.read(new File(photo));
                            photoLabel.repaint();
                        } catch (Exception ignored) {}
                    }

                    // Pre-fill edit fields
                    nameField.setText(userName);
                    if (batchO != null) batchField.setText(batchO.toString());
                    roleBox.setSelectedItem(userRole);
                    bioField.setText(bio != null ? bio : "");
                    skillsField.setText(skills != null ? skills : "");

                } catch (Exception ex) { ex.printStackTrace(); }
            }
        }.execute();
    }

    // ── SAVE PROFILE ──────────────────────────────────────────────
    private void saveProfile() {
        String name   = nameField.getText().trim();
        String batch  = batchField.getText().trim();
        String role   = (String) roleBox.getSelectedItem();
        String bio    = bioField.getText().trim();
        String skills = skillsField.getText().trim();

        // Validate
        if (name.isEmpty()) {
            showStatus("Name cannot be empty.", true);
            return;
        }
        if (!batch.isEmpty()) {
            try {
                int y = Integer.parseInt(batch);
                if (y < 1980 || y > 2030) throw new NumberFormatException();
            } catch (NumberFormatException e) {
                showStatus("Batch year must be 1980–2030.", true);
                return;
            }
        }

        saveBtn.setEnabled(false);
        showStatus("Saving...", false);

        new SwingWorker<Boolean, Void>() {
            @Override protected Boolean doInBackground() {
                try (Connection con = DBConnection.getConnection()) {
                    String sql;
                    PreparedStatement ps;
                    if (userId > 0) {
                        sql = "UPDATE users SET name=?,role=?,batch=?,bio=?,skills=? WHERE id=?";
                        ps = con.prepareStatement(sql);
                        ps.setString(1, name);
                        ps.setString(2, role);
                        ps.setObject(3, batch.isEmpty() ? null : Integer.parseInt(batch));
                        ps.setString(4, bio);
                        ps.setString(5, skills);
                        ps.setInt   (6, userId);
                    } else {
                        sql = "UPDATE users SET name=?,role=?,batch=?,bio=?,skills=? WHERE name=?";
                        ps = con.prepareStatement(sql);
                        ps.setString(1, name);
                        ps.setString(2, role);
                        ps.setObject(3, batch.isEmpty() ? null : Integer.parseInt(batch));
                        ps.setString(4, bio);
                        ps.setString(5, skills);
                        ps.setString(6, userName);
                    }
                    ps.executeUpdate();
                    return true;
                } catch (Exception ex) {
                    ex.printStackTrace();
                    return false;
                }
            }

            @Override protected void done() {
                saveBtn.setEnabled(true);
                try {
                    if (get()) {
                        // Update in-memory state
                        userName = name;
                        userRole = role;

                        // Refresh view panel
                        nameDisplay.setText(userName);
                        roleDisplay.setText(capitalize(role));
                        batchDisplay.setText(batch.isEmpty() ? "–" : batch);
                        bioDisplay.setText(bio.isEmpty() ? "No bio added yet." : bio);
                        refreshSkillChips(skills);

                        showStatus("Profile saved!", false);
                        Timer t = new Timer(1200, e -> exitEditMode());
                        t.setRepeats(false);
                        t.start();
                    } else {
                        showStatus("Save failed. Try again.", true);
                    }
                } catch (Exception ex) {
                    showStatus("Unexpected error.", true);
                }
            }
        }.execute();
    }

    // ── PHOTO PICKER ──────────────────────────────────────────────
    private void choosePhoto() {
        JFileChooser fc = new JFileChooser();
        fc.setFileFilter(new javax.swing.filechooser.FileNameExtensionFilter(
                "Images (jpg, png, gif)", "jpg", "jpeg", "png", "gif"));
        if (fc.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
            File f = fc.getSelectedFile();
            try {
                profilePhoto = ImageIO.read(f);
                photoLabel.repaint();
                // Optionally save photo path to DB here
                updatePhotoInDB(f.getAbsolutePath());
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }

    private void updatePhotoInDB(String path) {
        new SwingWorker<Void, Void>() {
            @Override protected Void doInBackground() {
                try (Connection con = DBConnection.getConnection()) {
                    String sql = userId > 0
                            ? "UPDATE users SET photo=? WHERE id=?"
                            : "UPDATE users SET photo=? WHERE name=?";
                    PreparedStatement ps = con.prepareStatement(sql);
                    ps.setString(1, path);
                    if (userId > 0) ps.setInt(2, userId);
                    else            ps.setString(2, userName);
                    ps.executeUpdate();
                } catch (Exception ex) { ex.printStackTrace(); }
                return null;
            }
        }.execute();
    }

    // ── MODE SWITCHING ────────────────────────────────────────────
    private void enterEditMode() {
        editMode = true;
        CardLayout cl = (CardLayout) contentArea.getLayout();
        cl.show(contentArea, "EDIT");
        editBtn.setVisible(false);
        mentorBtn.setVisible(false);
        saveBtn.setVisible(true);
        cancelBtn.setVisible(true);
        statusLabel.setText(" ");
    }

    private void exitEditMode() {
        editMode = false;
        CardLayout cl = (CardLayout) contentArea.getLayout();
        cl.show(contentArea, "VIEW");
        editBtn.setVisible(true);
        mentorBtn.setVisible(true);
        saveBtn.setVisible(false);
        cancelBtn.setVisible(false);
        statusLabel.setText(" ");
    }

    private void cancelEdit() {
        // Restore fields to current saved values
        nameField.setText(userName);
        roleBox.setSelectedItem(userRole);
        bioField.setText(bioDisplay.getText().equals("No bio added yet.") ? "" : bioDisplay.getText());
        exitEditMode();
    }

    // ── SKILL CHIPS ───────────────────────────────────────────────
    private void refreshSkillChips(String skills) {
        skillsDisplay.removeAll();
        if (skills == null || skills.trim().isEmpty()) {
            skillsDisplay.add(makeSkillChip("No skills added yet"));
        } else {
            for (String s : skills.split(",")) {
                String sk = s.trim();
                if (!sk.isEmpty()) skillsDisplay.add(makeSkillChip(sk));
            }
        }
        skillsDisplay.revalidate();
        skillsDisplay.repaint();
    }

    private JLabel makeSkillChip(String text) {
        JLabel chip = new JLabel(text) {
            @Override protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                                    RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(SKILL_BG);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 20, 20);
                g2.dispose();
                super.paintComponent(g);
            }
        };
        chip.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        chip.setForeground(SKILL_TEXT);
        chip.setBorder(BorderFactory.createEmptyBorder(5, 12, 5, 12));
        chip.setOpaque(false);
        return chip;
    }

    // ── UI HELPERS ────────────────────────────────────────────────
    private JPanel makeCard() {
        return new JPanel(null) {
            @Override protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                                    RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(new Color(0, 40, 80, 14));
                g2.fillRoundRect(5, 7, getWidth()-5, getHeight()-5, 24, 24);
                g2.setColor(WHITE);
                g2.fillRoundRect(0, 0, getWidth()-5, getHeight()-5, 24, 24);
                g2.dispose();
            }
        };
    }

    private JLabel sectionTitle(String text) {
        JLabel l = new JLabel(text);
        l.setFont(new Font("Georgia", Font.BOLD, 16));
        l.setForeground(YALE_BLUE);
        return l;
    }

    private JLabel makeDetailKey(String text) {
        JLabel l = new JLabel(text);
        l.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        l.setForeground(new Color(0x88, 0x88, 0x99));
        return l;
    }

    private JLabel makeRoleBadge(String role) {
        JLabel badge = new JLabel(role != null ? capitalize(role) : "–") {
            @Override protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                                    RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor("alumni".equalsIgnoreCase(userRole)
                        ? new Color(0xE1,0xF5,0xEE) : SKILL_BG);
                g2.fillRoundRect(0,0,getWidth(),getHeight(),16,16);
                g2.dispose();
                super.paintComponent(g);
            }
        };
        badge.setFont(new Font("Segoe UI", Font.BOLD, 12));
        badge.setForeground("alumni".equalsIgnoreCase(role)
                ? new Color(0x08,0x50,0x41) : SKILL_TEXT);
        badge.setBorder(BorderFactory.createEmptyBorder(4, 10, 4, 10));
        badge.setOpaque(false);
        return badge;
    }

    private JButton makeSideBtn(String text, boolean outline) {
        JButton btn = new JButton(text) {
            boolean hovered = false;
            {
                addMouseListener(new MouseAdapter() {
                    public void mouseEntered(MouseEvent e){ hovered=true;  repaint(); }
                    public void mouseExited (MouseEvent e){ hovered=false; repaint(); }
                });
            }
            @Override protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                                    RenderingHints.VALUE_ANTIALIAS_ON);
                if (outline) {
                    g2.setColor(hovered ? YALE_LIGHT : WHITE);
                    g2.fillRoundRect(0,0,getWidth(),getHeight(),20,20);
                    g2.setColor(YALE_BLUE);
                    g2.setStroke(new BasicStroke(1.5f));
                    g2.drawRoundRect(0,0,getWidth()-1,getHeight()-1,20,20);
                } else {
                    g2.setColor(hovered ? YALE_HOVER : YALE_BLUE);
                    g2.fillRoundRect(0,0,getWidth(),getHeight(),20,20);
                }
                g2.dispose();
                super.paintComponent(g);
            }
        };
        btn.setForeground(outline ? YALE_BLUE : WHITE);
        btn.setFont(new Font("Segoe UI", Font.BOLD, 13));
        btn.setBorderPainted(false);
        btn.setFocusPainted(false);
        btn.setContentAreaFilled(false);
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        return btn;
    }

    private JLabel makeEditLabel(String text, Font f, int x, int y) {
        JLabel l = new JLabel(text);
        l.setFont(f);
        l.setForeground(LABEL_DARK);
        l.setBounds(x, y, 400, 16);
        return l;
    }

    private JTextField makeEditField(Font f) {
        JTextField tf = new JTextField() {
            @Override protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                                    RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(FIELD_BG);
                g2.fillRoundRect(0,0,getWidth(),getHeight(),16,16);
                g2.setColor(FIELD_BORDER);
                g2.setStroke(new BasicStroke(1.3f));
                g2.drawRoundRect(0,0,getWidth()-1,getHeight()-1,16,16);
                g2.dispose();
                super.paintComponent(g);
            }
        };
        tf.setFont(f);
        tf.setBorder(BorderFactory.createEmptyBorder(5,14,5,14));
        tf.setOpaque(false);
        return tf;
    }

    private void styleComboBox(JComboBox<String> cb, Font f) {
        cb.setFont(f);
        cb.setBorder(BorderFactory.createEmptyBorder(5,10,5,10));
        cb.setOpaque(false);
        cb.setBackground(FIELD_BG);
    }

    private void showStatus(String msg, boolean error) {
        statusLabel.setForeground(error ? ERROR_RED : SUCCESS_GREEN);
        statusLabel.setText(msg);
    }

    private String getInitials(String name) {
        if (name == null || name.isEmpty()) return "?";
        String[] parts = name.trim().split("\\s+");
        if (parts.length == 1) return parts[0].substring(0, Math.min(2, parts[0].length())).toUpperCase();
        return ("" + parts[0].charAt(0) + parts[parts.length-1].charAt(0)).toUpperCase();
    }

    private String capitalize(String s) {
        if (s == null || s.isEmpty()) return s;
        return Character.toUpperCase(s.charAt(0)) + s.substring(1).toLowerCase();
    }

    private void goToMentors() {
        dispose();
        new MentorPage(userId, userName, userRole);
    }

    public static void main(String[] args) {
        try {
            for (UIManager.LookAndFeelInfo lf : UIManager.getInstalledLookAndFeels())
                if ("Nimbus".equals(lf.getName())) {
                    UIManager.setLookAndFeel(lf.getClassName());
                    break;
                }
        } catch (Exception ignored) {}
        SwingUtilities.invokeLater(() -> new ProfilePage(1, "Rahul Sharma", "student"));
    }
}