package ServicesPackage;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Connector {
	
	
	public Connection connect() {

	Connection connectionDB = null;
	try {
		
		DriverManager.registerDriver(new oracle.jdbc.driver.OracleDriver());
		connectionDB = DriverManager.getConnection("jdbc:oracle:thin:@68.183.75.84:49161:xe", "SYSTEM","oracle");

	} catch (SQLException e) {
		e.printStackTrace();
	}
	return connectionDB;
	}
	
	
}
