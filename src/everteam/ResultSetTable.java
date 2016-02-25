package everteam;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
 
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
 
/**
 * A JTable used to display a SQL ResultSet.
 * @author fahdshariff
 *
 */
public class ResultSetTable extends JTable{
 
  public DefaultTableModel getTableModel(ResultSet rs) throws SQLException {
      
    DefaultTableModel dataModel = new DefaultTableModel();
    setModel(dataModel);
 
    try {
      //create an array of column names
      ResultSetMetaData mdata = rs.getMetaData();
      int colCount = mdata.getColumnCount();
      String[] colNames = new String[colCount];
      for (int i = 1; i <= colCount; i++) {
        colNames[i - 1] = mdata.getColumnName(i);
      }
      dataModel.setColumnIdentifiers(colNames);
 
      //now populate the data
      while (rs.next()) {
        String[] rowData = new String[colCount];
        for (int i = 1; i <= colCount; i++) {
          rowData[i - 1] = rs.getString(i);
        }
        dataModel.addRow(rowData);
      }
      
      return dataModel;
    }
    finally{
      try {
        rs.close();
      }
      catch (SQLException ex) {
          // Silent
      }
    }
  }
}
