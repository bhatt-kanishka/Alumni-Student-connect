

/**
 * AlumniProfile – POJO returned by UserDAO.getAlumniByUserId()
 */
public class AlumniProfile {
    private int userId;
    private String name;
    private String email;
    private String company;
    private String designation;
    private int graduationYear;
    private String bio;

    // ── Getters ──────────────────────────────────────────────────────────────
    public int    getUserId()        { return userId; }
    public String getName()          { return name; }
    public String getEmail()         { return email; }
    public String getCompany()       { return company; }
    public String getDesignation()   { return designation; }
    public int    getGraduationYear(){ return graduationYear; }
    public String getBio()           { return bio; }

    // ── Setters ──────────────────────────────────────────────────────────────
    public void setUserId(int userId)              { this.userId = userId; }
    public void setName(String name)               { this.name = name; }
    public void setEmail(String email)             { this.email = email; }
    public void setCompany(String company)         { this.company = company; }
    public void setDesignation(String designation) { this.designation = designation; }
    public void setGraduationYear(int year)        { this.graduationYear = year; }
    public void setBio(String bio)                 { this.bio = bio; }
}