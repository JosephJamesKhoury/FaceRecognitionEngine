package everteam;

import java.sql.Connection;
import java.sql.PreparedStatement;

public class OriginalImage {
    /*
     * Return the list of persons
     * Or the list of a specific person
     */
    public boolean insertOriginalImage(String value) throws Exception
    {
        Connection conn = null;
        PreparedStatement pstmt = null;

        try {
            conn = DBManager.getInstance().getConnetion();
            
            pstmt = conn.prepareStatement("INSERT INTO Original_Image (OI_OriginalImage) VALUES (?)");
            
            pstmt.setBytes(1, value.getBytes());
            
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
