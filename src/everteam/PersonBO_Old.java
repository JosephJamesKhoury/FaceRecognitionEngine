package everteam;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class PersonBO_Old {

    public ResultSet getPersons(String[] columns, String[] conditions) throws Exception {
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            conn = DBManager.getInstance().getConnetion();
            
            String strColumns, strConditions;
            strColumns = "*";
            strConditions = "";
            
            if (columns.length > 0)
            {
                strColumns = columns[0];
                
                for(int i = 1; i < columns.length; i++)
                {
                    strColumns += ", " + columns[i];
                }
            }
            
            if (conditions.length > 0)
            {
                strConditions = " WHERE " + conditions[0];
                
                for(int i = 1; i < conditions.length; i++)
                {
                    strConditions += " AND " + conditions[i];
                }
            }
            
            pstmt = conn.prepareStatement("SELECT " + strColumns + " FROM Person" + strConditions);
            
            rs = pstmt.executeQuery();
            
            return rs;
            
        } finally {
            //DBManager.getInstance().closeResulSet(rs);
            //DBManager.getInstance().closestatement(pstmt);
            //DBManager.getInstance().closeConnection(conn);
        }
    }
    
    public boolean setPerson(String[] columns, String[] values) throws Exception {
        Connection conn = null;
        PreparedStatement pstmt = null;

        try {
            conn = DBManager.getInstance().getConnetion();
            
            String strColumns, strValues;
            strColumns = "";
            strValues = "";
            
            if (columns.length > 0)
            {
                strColumns = "(" + columns[0];
                
                for(int i = 1; i < columns.length; i++)
                {
                    strColumns += ", " + columns[i];
                }
                
                strColumns += ")";
            }
            
            if (values.length > 0)
            {
                strValues = "('" + values[0] + "'";
                
                for(int i = 1; i < values.length; i++)
                {
                    strValues += ", '" + values[i] + "'";
                }
                
                strValues += ")";
            }
            
            pstmt = conn.prepareStatement("INSERT INTO Person " + strColumns + " VALUES " + strValues);
            
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
    
    public boolean updatePerson(String[] columns, String[] conditions) throws Exception {
        Connection conn = null;
        PreparedStatement pstmt = null;

        try {
            conn = DBManager.getInstance().getConnetion();
            
            String strColumns, strConditions;
            strColumns = "";
            strConditions = "";
            
            if (columns.length > 0)
            {
                strColumns = columns[0];
                
                for(int i = 1; i < columns.length; i++)
                {
                    strColumns += ", " + columns[i];
                }
            }
            
            if (conditions.length > 0)
            {
                strConditions = " WHERE " + conditions[0];
                
                for(int i = 1; i < conditions.length; i++)
                {
                    strConditions += " AND " + conditions[i];
                }
            }
            
            pstmt = conn.prepareStatement("UPDATE Person SET " + strColumns + strConditions);
            
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
