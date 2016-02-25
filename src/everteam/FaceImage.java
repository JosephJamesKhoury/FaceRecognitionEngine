package everteam;

import java.sql.Connection;
import java.sql.PreparedStatement;

public class FaceImage {
    
    /*
     * Return the list of persons
     * Or the list of a specific person
     */
    public boolean insertFaceImage(String[] values) throws Exception
    {
        Connection conn = null;
        PreparedStatement pstmt = null;

        try {
            conn = DBManager.getInstance().getConnetion();
            
            pstmt = conn.prepareStatement("INSERT INTO Face_Image (P_ID, OI_ID, FI_Face_Image) VALUES (?, ?, ?)");
            
            pstmt.setInt(1, Integer.parseInt(values[0]));
            pstmt.setInt(2, Integer.parseInt(values[1]));
            pstmt.setBytes(3, values[2].getBytes());
            
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
