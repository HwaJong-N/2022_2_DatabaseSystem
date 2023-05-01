

import java.sql.Connection;
import java.sql.DriverManager;
import javax.swing.JOptionPane;

public class MyConnection {
	
	public static Connection mackConnection() {
		Connection con=null;
		try {

			// load and register the Driver
			Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");

			String url = "jdbc:sqlserver://DESKTOP-2RF7HP2\\SQLEXPRESS:1433;encrypt=true;trustServerCertificate=true;databaseName=Project";

			
			// Get connection using URL through Driver Manager
			con = DriverManager.getConnection(url, "sa", "1234");
			
			System.out.println("Database connection Established Succusfully");
			System.out.println(" Connected!..");


		} catch (Exception e) {
			System.out.println("Connection Failed");
			System.out.println(e.getMessage());
	
		}
		return con;

	}

}
