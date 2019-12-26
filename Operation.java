
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;


public class Operation {
    //kullanılacaklar: import edilecekler:
    //*import ettiklerimize referans isimleri verdik
    Connection con = null;
    Statement sta= null;
    PreparedStatement psta =null;
    
    public int bookCount(){
        int lbl =0;
        String sorgu = "SELECT COUNT(*) FROM books_database ";
        try {
            sta = con.createStatement();
            ResultSet rs = sta.executeQuery(sorgu);
            rs.next();
            lbl = rs.getInt(1);
            
        } catch (SQLException ex) {
            Logger.getLogger(Operation.class.getName()).log(Level.SEVERE, null, ex);
            return 0;
        }
        return lbl;
    }
    public void bookDelete(int id){
        String sorgu ="Delete FROM books_database WHERE id=?";
        try {
            psta = con.prepareStatement(sorgu);
            psta.setInt(1, id);
            psta.executeUpdate();
        } catch (SQLException ex) {
            Logger.getLogger(Operation.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    public void bookUpdate(int id, String new_name, String new_writer, String new_type, String new_publisher){
        String sorgu = "Update books_database SET book_name=?,book_writer=?,book_type=?,book_publisher=? WHERE id=?";
        try {
            psta =con.prepareStatement(sorgu);
            //yukarıdaki sorgudaki soru işaretlerinin yerine verileri atayalım
            psta.setString(1, new_name);
            psta.setString(2, new_writer);
            psta.setString(3, new_type);
            psta.setString(4, new_publisher);
            psta.setInt(5, id);
            psta.executeUpdate();
            
        } catch (SQLException ex) {
            Logger.getLogger(Operation.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }
    
    
    
    public void bookAdd(String name, String writer, String type, String publisher){
        //verileri veritabanına ekle
        String sorgu ="Insert into books_database(book_name, book_writer, book_type, book_publisher) VALUES (? , ? , ? , ?)";
        try {
            psta = con.prepareStatement(sorgu);
            //yukarıdaki sql sorgusundaki soru işaretlerine değerleri aktaralım
            psta.setString(1, name);
            psta.setString(2, writer);
            psta.setString(3, type);
            psta.setString(4, publisher);
            //yukarıdaki set işlemlerini sql üzerinde çalıştıralım
            psta.executeUpdate();
        } catch (SQLException ex) {
            Logger.getLogger(Operation.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        
        
    }
    public ArrayList<Book> BookCome(){
        ArrayList<Book> list = new ArrayList<Book>();
        String sorgu="Select * from books_database";
        
        try {
            //bağlantıyı sağla
            sta = con.createStatement();
            //ResultSet gelecek değerleri içerisinde tutup java ortamında kullanabilmemizi sağlar
            ResultSet rs = sta.executeQuery(sorgu);
            
            //Gelen verileri yazdırmak için döngü oluştur
            //tablodaki değer boş olana kadar döngüye al
            while(rs.next()){
                int id = rs.getInt("id");
                String book_name =rs.getString("book_name");
                String book_writer =rs.getString("book_writer");
                String book_type =rs.getString("book_type");
                String book_publisher =rs.getString("book_publisher");
                
                //listeye ekleyelim bunları
                list.add(new Book(id, book_name, book_writer,book_type, book_publisher));
            }
            return list;
        } catch (SQLException ex) {
            Logger.getLogger(Operation.class.getName()).log(Level.SEVERE, null, ex);
            return null; //liste boşsa null dönsün
        }
        
        
    }
    
    
    public boolean Login(String id, String password){
        String sorgu = "Select * from admin where id =? and password =?";
        try {
            psta = con.prepareStatement(sorgu);
            psta.setString(1,id);
            psta.setString(2, password);
            ResultSet rs = psta.executeQuery();
            return rs.next();
        } catch (SQLException ex) {
            Logger.getLogger(Operation.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }
       
    }
    //*proje başlar başlamaz veri tabanı güncel bir şekilde gelmeli
    //*her sınıfın ilk çalışacak metodu kendi consructor'dır 
    public Operation() {
         //url--> jdbc:mysql://host:port/db_name;id;password 
         String url ="jdbc:mysql://" +Database.host+":"+Database.port+"/"+Database.db_name;
        try {      
            Class.forName("com.mysql.jdbc.Driver");
            con = DriverManager.getConnection(url,Database.id,Database.password);
            System.out.println("Veritabanına başarıyla bağlandınız.");
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(Operation.class.getName()).log(Level.SEVERE, null, ex);
            System.out.println("Driver Çalışmadı");
        } catch (SQLException ex) {
            Logger.getLogger(Operation.class.getName()).log(Level.SEVERE, null, ex);
            System.out.println("Connection Çalışamadı");
        }
        
        
    }
    
    public static void main(String[] args) {
        Operation op = new Operation();
    }
}
