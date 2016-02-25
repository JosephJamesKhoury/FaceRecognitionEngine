package everteam;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class PersonBO 
{
    private int iD;
    private String firstName;
    private String lastName;
    private String gender;
    private String race;
    private String hairColor;
    private String eyeColor;
    private int height;
    private double weight;
    private String dateOfBirth;
    private String nationality;
    private String remarks;
    
    //Constructors
    public PersonBO(){}
    
    public PersonBO(int iD, String firstName, String lastName, String gender, String race, String hairColor
            , String eyeColor, int height, double weight, String dateOfBirth, String nationality, String remarks)
    {
        this.iD = iD;
        this.firstName = firstName;
        this.lastName = lastName;
        this.gender = gender;
        this.race = race;
        this.hairColor = hairColor;
        this.eyeColor = eyeColor;
        this.height = height;
        this.weight = weight;
        this.dateOfBirth = dateOfBirth;
        this.nationality = nationality;
        this.remarks = remarks;
    }
    
    //Getters
    public int getID() {return iD;}
    public String getFirstName() {return firstName;}
    public String getLastName() {return lastName;}
    public String getGender() {return gender;}
    public String getRace() {return race;}
    public String getHairColor() {return hairColor;}
    public String getEyeColor() {return eyeColor;}
    public int getHeight() {return height;}
    public double getWeight() {return weight;}
    public String getDateOfBirth() {return dateOfBirth;}
    public String getNationality() {return nationality;}
    public String getRemarks() {return remarks;}
    
    //Setters
    public void setID(int val) {iD = val;}
    public void setFirstName(String val) {firstName = val;}
    public void setLastName(String val) {lastName = val;}
    public void setGender(String val) {gender = val;}
    public void setRace(String val) {race = val;}
    public void setHairColor(String val) {hairColor = val;}
    public void setEyeColor(String val) {eyeColor = val;}
    public void setHeight(int val) {height = val;}
    public void setWeight(double val) {weight = val;}
    public void setDateOfBirth(String val) {dateOfBirth = val;}
    public void setNationality(String val) {nationality = val;}
    public void setRemarks(String val) {remarks = val;}
    
    public Object[] getRow(PersonBO person)
    {
        Object[] obj = new Object[12];
        obj[0] = person.getID();
        obj[1] = person.getFirstName();
        obj[2] = person.getLastName();
        obj[3] = person.getGender();
        obj[4] = person.getRace();
        obj[5] = person.getHairColor();
        obj[6] = person.getEyeColor();
        obj[7] = person.getHeight();
        obj[8] = person.getWeight();
        obj[9] = person.getDateOfBirth();
        obj[10] = person.getNationality();
        obj[11] = person.getRemarks();
        
        return obj;
    }
    
    /*
     * Return the list of persons
     * Or the list of a specific person
     */
    public List<PersonBO> getPersons(int ID) throws Exception
    {
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        List<PersonBO> listPersons;

        try
        {
            conn = DBManager.getInstance().getConnetion();
            
            if(ID == -1)
            {
                pstmt = conn.prepareStatement("SELECT P_ID"
                        + ", P_FName"
                        + ", P_LName"
                        + ", CASE WHEN P_Gender = 0 THEN 'Male' WHEN P_Gender = 1 THEN 'Female' ELSE 'Other' END AS [P_Gender]"
                        + ", P_Race"
                        + ", P_HairColor"
                        + ", P_EyeColor"
                        + ", P_Height"
                        + ", P_Weight"
                        + ", P_DateOfBirth"
                        + ", P_Nationality"
                        + ", P_Remarks"
                        + " FROM Person");
            }
            else
            {
                pstmt = conn.prepareStatement("SELECT P_ID"
                        + ", P_FName"
                        + ", P_LName"
                        + ", CASE WHEN P_Gender = 0 THEN 'Male' WHEN P_Gender = 1 THEN 'Female' ELSE 'Other' END AS [P_Gender]"
                        + ", P_Race"
                        + ", P_HairColor"
                        + ", P_EyeColor"
                        + ", P_Height"
                        + ", P_Weight"
                        + ", P_DateOfBirth"
                        + ", P_Nationality"
                        + ", P_Remarks"
                        + " FROM Person"
                        + " WHERE P_ID = ?");
                pstmt.setInt(1, ID);
            }
            
            rs = pstmt.executeQuery();
            
            listPersons = new ArrayList();
            PersonBO person;
            
            while(rs.next())
            {
                person = new PersonBO();
                person.setID(Integer.parseInt(rs.getString("P_ID")));
                person.setFirstName(rs.getString("P_FName"));
                person.setLastName(rs.getString("P_LName"));
                person.setGender(rs.getString("P_Gender"));
                person.setRace(rs.getString("P_Race"));
                person.setHairColor(rs.getString("P_HairColor"));
                person.setEyeColor(rs.getString("P_EyeColor"));
                person.setHeight(Integer.parseInt(rs.getString("P_Height")));
                person.setWeight(Double.parseDouble(rs.getString("P_Weight")));
                person.setDateOfBirth(rs.getString("P_DateOfBirth"));
                person.setNationality(rs.getString("P_Nationality"));
                person.setRemarks(rs.getString("P_Remarks"));
                
                listPersons.add(person);
            }
        }
        finally
        {
            DBManager.getInstance().closeResulSet(rs); 
            DBManager.getInstance().closestatement(pstmt);
            DBManager.getInstance().closeConnection(conn);
        }
        
        return listPersons;
    }
    
    /*
     * Insert into table Person the values sent in parameter
     */
    public boolean insertPerson(String[] values) throws Exception {
        Connection conn = null;
        PreparedStatement pstmt = null;

        try {
            conn = DBManager.getInstance().getConnetion();
            
            pstmt = conn.prepareStatement("INSERT INTO Person (P_FName, P_LName, P_Gender, P_Race, P_HairColor, P_EyeColor"
                    + ", P_Height, P_Weight, P_DateOfBirth, P_Nationality"
                    + ", P_Remarks) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)");
            
            pstmt.setString(1, values[0]);
            pstmt.setString(2, values[1]);
            pstmt.setString(3, values[2]);
            pstmt.setString(4, values[3]);
            pstmt.setString(5, values[4]);
            pstmt.setString(6, values[5]);
            pstmt.setInt(7, Integer.parseInt(values[6]));
            pstmt.setDouble(8, Double.parseDouble(values[7]));
            pstmt.setString(9, values[8]);
            pstmt.setString(10, values[9]);
            pstmt.setString(11, values[10]);
            
            pstmt.executeUpdate();
            
            return true;
        }
        catch(Exception ex){
            ex.printStackTrace();
            return false;
        }
        finally {
            DBManager.getInstance().closestatement(pstmt);
            DBManager.getInstance().closeConnection(conn);
        }
    }
    
    /*
     * Update table Person with the values sent in parameter
     */
    public boolean updatePerson(String[] values, int ID) throws Exception {
        Connection conn = null;
        PreparedStatement pstmt = null;

        try {
            conn = DBManager.getInstance().getConnetion();
            
            pstmt = conn.prepareStatement("UPDATE Person SET P_FName = ?, P_LName = ?"
                    + ", P_Gender = ?, P_Race = ?, P_HairColor = ?, P_EyeColor = ?, P_Height = ?"
                    + ", P_Weight = ?, P_DateOfBirth = ?, P_Nationality = ?, P_Remarks = ?"
                    + " WHERE P_ID = ?");
            
            pstmt.setString(1, values[0]);
            pstmt.setString(2, values[1]);
            pstmt.setString(3, values[2]);
            pstmt.setString(4, values[3]);
            pstmt.setString(5, values[4]);
            pstmt.setString(6, values[5]);
            pstmt.setInt(7, Integer.parseInt(values[6]));
            pstmt.setDouble(8, Double.parseDouble(values[7]));
            pstmt.setString(9, values[8]);
            pstmt.setString(10, values[9]);
            pstmt.setString(11, values[10]);
            pstmt.setInt(12, ID);
            
            pstmt.executeUpdate();
            
            return true;
        }
        catch(Exception ex){
            ex.printStackTrace();
            return false;
        }
        finally {
            DBManager.getInstance().closestatement(pstmt);
            DBManager.getInstance().closeConnection(conn);
        }
    }
}
