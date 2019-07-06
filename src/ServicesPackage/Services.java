package ServicesPackage;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Types;

public class Services {

	private boolean phoneNumberVerification(String inputPhoneNumber) {
	if( String.valueOf(inputPhoneNumber).startsWith("5") ) {
		String pattern = "(?=.*[0-9]).{10}";
	    return inputPhoneNumber.matches(pattern);
		}
	return false;		
	}
	
	private boolean passwordVerification(String inputPassword) {
	    String pattern = "(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z]).{8,}";
	    return inputPassword.matches(pattern);
	}
	
	private boolean firstNameVerification(String inputFirstName) {
		String pattern = "([a-zA-Z]*).{2,}";
	    return inputFirstName.matches(pattern);
	}
	
	private boolean lastNameVerification(String inputLastName) {
	    String pattern = "([a-zA-Z]*).{2,}";
	    return inputLastName.matches(pattern);
	}
	
	private boolean emailVerification(String inputEmail) {
		String pattern ="^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
			+ "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
	return inputEmail.matches(pattern) ;
	}
	
	private boolean TcNumberVerification(String inputTcNumber) {
		String pattern = "(\\d+)";
		String lengthPattern = ".{11}";
		if( inputTcNumber.matches(pattern) && inputTcNumber.matches(lengthPattern) ) {
			
		    char[] chars = inputTcNumber.toCharArray();
		    int[] temporaryTcNumber = new int[11];
		    
		    for(int i=0; i<11; i++) {
		    	temporaryTcNumber[i] = chars[i] - '0';
		    }
	
		    if(temporaryTcNumber[0] == 0) { 
		        return false;
		    }
		    if(temporaryTcNumber[10] % 2 == 1) {
		    	return false;
		    }
	
		    if(((temporaryTcNumber[0] + temporaryTcNumber[2] + temporaryTcNumber[4] + temporaryTcNumber[6] + 
		        	temporaryTcNumber[8]) * 7 - (temporaryTcNumber[1] + temporaryTcNumber[3] + temporaryTcNumber[5] + 
		        			temporaryTcNumber[7])) % 10 != temporaryTcNumber[9]) { 
		        return false;
		    }
	
		    if((temporaryTcNumber[0] + temporaryTcNumber[1] + temporaryTcNumber[2] + temporaryTcNumber[3] + 
		        	temporaryTcNumber[4] + temporaryTcNumber[5] + temporaryTcNumber[6] + temporaryTcNumber[7] + 
		        		temporaryTcNumber[8] + temporaryTcNumber[9]) % 10 != temporaryTcNumber[10]) {
		       return false;
		    }
		    return true;
		}else {
			return false;
		}
	        
	}
	
	private Date changeTypeOfDate(String birthDate) {
		
	    return Date.valueOf(birthDate);
	}
	
	public int login(String inputPhoneNumber, String inputPassword) {
		
		if( !phoneNumberVerification(inputPhoneNumber) || !passwordVerification(inputPassword) ) {
			return 0;
		}
		
		int result ;
		Connection connectionDB = null;
		CallableStatement csProcedureObject = null;
		try {
			DriverManager.registerDriver(new oracle.jdbc.driver.OracleDriver());
			connectionDB = DriverManager.getConnection("jdbc:oracle:thin:@68.183.75.84:49161:xe", "SYSTEM","oracle");
			csProcedureObject = connectionDB.prepareCall("{CALL CHECK_USER(?,?,?)}");

			csProcedureObject.setString(1, inputPhoneNumber);
			csProcedureObject.setString(2, inputPassword);
			csProcedureObject.registerOutParameter(3, Types.INTEGER);

			csProcedureObject.execute();
			
			result = csProcedureObject.getInt(3);
			csProcedureObject.close();
			
		} catch (SQLException ex) {
			ex.getMessage();
			result = 0;
		} finally {
			
			try {
				csProcedureObject.close();
				connectionDB.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		
		return result;
	}

	public int createAccount(String inputFirstName,    String inputLastName,   
							 String inputPhoneNumber,  String inputEmail, 
							 String inputPassword,     String inputBirthDate,
							 Double inputTcNumber) {

		if( !firstNameVerification(inputFirstName) || !lastNameVerification(inputLastName) || 
				!phoneNumberVerification(inputPhoneNumber) || !passwordVerification(inputPassword) || 
					!emailVerification(inputEmail)) {
			return 0;
		}
		
		
		/* TC Numarasi DB de Hala Number tipinde olduðundan dolayi suan eklenmedi ve tipi degistirilmedi. 
		   Daha sonra tipi değiştirilip verification eklenecek.
		    if( !TcNumberVerification(inputTcNumber) ) {
			return 0;
		}
		*/
		Date birthDate = changeTypeOfDate(inputBirthDate);
		
		Connection connectionDB = null;
		CallableStatement csProcedureObject = null;
		int result;
		
		try {
			
			DriverManager.registerDriver(new oracle.jdbc.driver.OracleDriver());
			
			connectionDB = DriverManager.getConnection("jdbc:oracle:thin:@68.183.75.84:49161:xe", "SYSTEM","oracle");
			csProcedureObject = connectionDB.prepareCall("{CALL CREATE_USER(?,?,?,?,?,?,?,?)}");
			
			csProcedureObject.registerOutParameter(8, Types.INTEGER);
			csProcedureObject.setString(1, inputFirstName);
			csProcedureObject.setString(2, inputLastName);
			csProcedureObject.setString(3, inputPhoneNumber);
			csProcedureObject.setString(4, inputEmail);
			csProcedureObject.setString(5, inputPassword);
			csProcedureObject.setDate(6, birthDate);
			csProcedureObject.setDouble(7, inputTcNumber);

			csProcedureObject.execute();
			result = csProcedureObject.getInt(8);
			
		} catch (SQLException ex) {
			ex.getMessage();
			result = 0;
		} finally {
			
			try {
				connectionDB.close();
				csProcedureObject.close();
			} catch (SQLException e) {
				
				e.printStackTrace();
			}
		}
		
		return result;
	}
	
	public Integer[] getBalances(String inputPhoneNumber) {
		Integer[] remainResults = new Integer[3];
		
		if( !firstNameVerification(inputPhoneNumber)) {
			return remainResults;
		}
		
		
		Connection connectionDB = null;
		CallableStatement csProcedureObject = null;
		try {
			DriverManager.registerDriver(new oracle.jdbc.driver.OracleDriver());
			connectionDB = DriverManager.getConnection("jdbc:oracle:thin:@68.183.75.84:49161:xe", "SYSTEM","oracle");
			csProcedureObject = connectionDB.prepareCall("{CALL GET_BALANCES(?,?,?,?)}");
		
			csProcedureObject.setString(1, inputPhoneNumber);
			
			csProcedureObject.registerOutParameter(2, Types.DOUBLE);
			csProcedureObject.registerOutParameter(3, Types.DOUBLE);
			csProcedureObject.registerOutParameter(4, Types.DOUBLE);
			
			csProcedureObject.execute();
			
			remainResults[0] =  csProcedureObject.getInt(2);
			remainResults[1] =  csProcedureObject.getInt(3);
			remainResults[2] =  csProcedureObject.getInt(4);
			
		} catch (SQLException ex) {
			remainResults = null;
			ex.getMessage();
			
		} finally {
			
			try {
				csProcedureObject.close();
				connectionDB.close();
			} catch (SQLException e) {
				
				e.printStackTrace();
			}
			
		}
		return remainResults;
	}
	
	
	public int changePassword(String inputPhoneNumber, Double inputTcNumber,
							  String inputNewPassword ) {
		
		if( !firstNameVerification(inputPhoneNumber) || !phoneNumberVerification(inputNewPassword) ) {
			return 0;
		}
		
		int result ;
		Connection connectionDB = null;
		CallableStatement csProcedureObject = null;
		try {
			DriverManager.registerDriver(new oracle.jdbc.driver.OracleDriver());
			connectionDB = DriverManager.getConnection("jdbc:oracle:thin:@68.183.75.84:49161:xe", "SYSTEM","oracle");
			csProcedureObject = connectionDB.prepareCall("{CALL CHANGE_PASSWORD(?,?,?,?)}");

			csProcedureObject.setString(1, inputPhoneNumber);
			csProcedureObject.setDouble(2, inputTcNumber);
			csProcedureObject.setString(3, inputNewPassword);
			csProcedureObject.registerOutParameter(4, Types.INTEGER);
			
			csProcedureObject.execute();
			
			result = csProcedureObject.getInt(4);
			
			
		} catch (SQLException ex) {
			ex.getMessage();
			result = 0;
		} finally {
			
			try {
				csProcedureObject.close();
				connectionDB.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}		
		}
		return result;
	}

}
