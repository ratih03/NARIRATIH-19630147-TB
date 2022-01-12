import java.sql.Driver;
import java.sql.DriverManager;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.io.File;
import java.sql.ResultSet;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;

import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.design.JRDesignQuery;
import net.sf.jasperreports.engine.design.JasperDesign;
import net.sf.jasperreports.engine.xml.JRXmlLoader;
import net.sf.jasperreports.view.JasperViewer;



/**
 *
 * @author ACER
 */
public class coding {
    






    
    String jdbcUrl ="jdbc:mysql://localhost/ratihrental";
    String username ="root";
    String password ="";
    
    Connection koneksi;
    
    public coding(){}
    
    public coding(String url, String user, String pass) throws SQLException{
        
        
        try(Connection koneksi = DriverManager.getConnection(url, user, pass)){
           
            Driver mysqldriver = new com.mysql.jdbc.Driver();
            DriverManager.registerDriver(mysqldriver);
            
            System.out.println("Berhasil");
        } catch (SQLException error) {
            System.err.println(error.toString());
        
        }
        
    }
    
    
    
    
    public Connection getKoneksi() throws SQLException{
        try{
            Driver mysqldriver = new com.mysql.jdbc.Driver();
            DriverManager.registerDriver(mysqldriver);
            
            
        } catch (SQLException e) {
            
            System.err.println(e.toString());
        }
        
        return DriverManager.getConnection(this.jdbcUrl, this.username, this.password);
    }
    
 
    public boolean duplicateKey (String table, String Primarykey, String value) {
        boolean hasil=false; 
        try{
            Statement sts = getKoneksi ().createStatement (); 
            ResultSet rs = sts.executeQuery ("SELECT*FROM "+table+" WHERE "+Primarykey+" ='"+value+"'");    
            
            hasil = rs.next ();
            
            rs.close() ; 
            sts.close();
            getKoneksi().close(); 
        } catch (Exception e) { 
            System.err.println (e.toString ());
        }
 return hasil;
    }
    
     
public String DapatkanValue (String [] fields) { 
    String hasil="";
    int index = fields.length-1;
    try { 
        for (int i = 0; i < fields.length; i++) {
            
            if (i==index) {
                
                hasil=hasil+"'"+fields[i]+"'"; 
            }else{
                hasil = hasil +"'"+ fields[i]+"'" +"," ; 
            }
        }
    }catch (Exception e) { 
    }
    
return "("+hasil+")";
} 

public String DapatkanFields (String[] fields) {
    String hasil="" ;
    int index =fields.length-1;
    try { 
        for (int i = 0; i < fields.length; i++) {
            if (i==index) {
                hasil=hasil+fields[i];
            }else{
                 hasil = hasil + fields [i]+ ",";
            }
        }
    }catch (Exception e) {
    }
    return "("+hasil+")";
}
 
public void SimpanDinamis(String Table,String[] fields, String[] isi ){
    try {
            String SQL="INSERT INTO "+Table+" "+DapatkanFields(fields)+" VALUES "+DapatkanValue (isi)+""; 
            Statement perintah = getKoneksi ().createStatement ();

            perintah.executeUpdate (SQL);
            perintah.close ();
            getKoneksi ().close ();
            JOptionPane.showMessageDialog (null, "Data Berhasil Disimpan");
    } catch (Exception e) {
        System.out.println (e.toString ());
    }
}
public void HapusDinamis (String Table, String PrimaryKey, String isiPrimary) {
    try {
            String SQLHapus = "DELETE FROM "+Table+" WHERE "+PrimaryKey+"= '"+isiPrimary+"'";
            Statement perintah = getKoneksi ().createStatement ();
            
            perintah.executeUpdate (SQLHapus); perintah.close ();
            getKoneksi ().close (); 
            JOptionPane.showMessageDialog (null, "Data Berhasil dihapus"); 
    } catch (Exception e) { 
        System.out.println (e.toString ()); 
    }
}
public String gabungFieldValue (String[] field, String[] isi) { 
    String hasil = ""; 
    int index = field.length- 1; 
    try {
            for (int i = 0; i < field.length; i++) {
            if (i==index) {
                hasil=hasil+field[i]+"='"+isi[i]+"'"; 
            } else{ 
                hasil=hasil+field[i]+"='"+isi[i]+"',"; 
            } 
        }
    }catch (Exception e) { 
    System.out.println (e.toString ()) ; 
    }
return hasil;
}

public void UbahDinamis(String tabel, String [] field, String[] isi, String Primary, String isiPrimary) {
    try {
            System.out.println("UPDATE "+tabel+" SET "+gabungFieldValue (field, isi)+" WHERE "+Primary+" = '"+isiPrimary+"'");
            String SQLUbah ="UPDATE "+tabel+" SET "+gabungFieldValue (field, isi)+" WHERE "+Primary+" = '"+isiPrimary+"'"; 
            Statement perintah = getKoneksi ().createStatement (); 
            perintah.executeUpdate (SQLUbah);
            perintah.close ();
            getKoneksi ().close ();
            JOptionPane.showMessageDialog (null, "Data Berhasil diubah");
    } catch (Exception e) { 
    System.out.println (e.toString ());
    }
}

 public void setJudulTabel (JTable Tabelnya, String[] JudulKolomnya) { 
     try { 
         DefaultTableModel modelnya = new DefaultTableModel ();
         Tabelnya.setModel (modelnya);
         for (int i = 0; i< JudulKolomnya.length; i++) {
         modelnya.addColumn (JudulKolomnya[i]);
     }
     
 }    catch (Exception e) { 
        JOptionPane.showMessageDialog(null, e.toString ());
 }
 }
  public void setLebarKolom(JTable tabelnya,int[] JudulKolom){
      
      try{
          TableColumn Kolomnya = new TableColumn();
          for (int i = 0; i < JudulKolom.length; i++) {
          Kolomnya = tabelnya.getColumnModel().getColumn(i);
          Kolomnya.setPreferredWidth(JudulKolom[i]);
          }
                  
      } catch (Exception e) {
          JOptionPane.showMessageDialog(null,e.toString());
      }
  }

         public Object[][] isiTabel(String SQL, int jumlah){
      Object[][] data =null;
      try {
         Statement perintah = getKoneksi().createStatement();
         ResultSet dataset = perintah.executeQuery(SQL);
         dataset.last();
         int baris = dataset.getRow();
         dataset.beforeFirst();
         int j =0;
         
         data = new Object[baris][jumlah];
         
         while (dataset.next()){
             for (int i = 0; i < jumlah; i++) {
                 data[j][i]=dataset.getString(i+1);
             }
             j++;
         }
         
      } catch (Exception e) {
      }
      
      return data;
  }
  
  public void tampilTabel(JTable Tabelnya, String SQL, String[] Judul){
      try {
        Tabelnya.setModel(new DefaultTableModel(isiTabel(SQL,Judul.length), Judul));
      } catch (Exception e) {
          System.out.println(e.toString());
      }
  }
  public void tampilLaporan(String laporanFile, String SQL){
      try {
          File file = new File (laporanFile);
          JasperDesign jasDes = JRXmlLoader.load(file);
          
          JRDesignQuery sqlQuery = new JRDesignQuery();
          sqlQuery.setText(SQL);
          jasDes.setQuery(sqlQuery);
          
          JasperReport JR = JasperCompileManager.compileReport(jasDes);
          JasperPrint JP = JasperFillManager.fillReport(JR,null, getKoneksi());
          JasperViewer.viewReport(JP);
      } catch (Exception e) {
          JOptionPane.showMessageDialog(null,e.toString());

          
          
      }
  }
  public String getMyparams(int jumlah){
      String hasil="";
              int index=jumlah-1;
              try{
                  for (int i =0; i < jumlah; i++) {
                      if (i==index){
                          hasil=hasil+"?";
                      } else {
                          hasil=hasil+"?"+",";
                      }
                  }
        } catch (Exception e) {
        }
              
    return hasil;
        }
             
                          
                     
}