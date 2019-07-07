package ServicesPackage;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Types;

public class Services {
	private Connector connector;
	private Connection connection;
	
	public Connector getConnector() {
		if( this.connector == null )
			this.connector = new Connector();
		return connector;
	}

	public Connection getConnection() {
		if( this.connection == null )
			this.connection = this.getConnector().connect();
		return connection;
	}

	private boolean phoneNumberVerification(String inputPhoneNumber) {
	if( String.valueOf(inputPhoneNumber).startsWith("5") ) {
		String pattern = "\\d{10}$";
	    return inputPhoneNumber.matches(pattern);
		}
	return false;		
	}
	
	private boolean passwordVerification(String inputPassword) {
	    String pattern = "(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z]).{8,}";
	    return inputPassword.matches(pattern);
	}
	
	private boolean nameVerification(String inputName) {
		
		String pattern = "([a-zA-Z]){2,}";
	    return inputName.matches(pattern);
	}
	
	private boolean emailVerification(String inputEmail) {
		
		String pattern ="^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
			+ "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
	return inputEmail.matches(pattern) ;
	}
	
	private boolean tcNumberVerification(String inputTcNumber) {
		String pattern = "\\d{11}$";
		if( inputTcNumber.matches(pattern) ) {

		    char[] chars = inputTcNumber.toCharArray();
		    int[] temporaryTcNumber = new int[11];
		    
		    for(int i=0; i<11; i++) {
		    	temporaryTcNumber[i] = chars[i] - '0';
		    }
	
		    if(temporaryTcNumber[0] == 0 || temporaryTcNumber[10] % 2 == 1 ) { 
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
	
	private boolean checkUserInfo(String inputFirstName,    String inputLastName,  String inputPhoneNumber,
				      String inputEmail,        String inputPassword,  String inputTcNumber) {
		
		
		if( !nameVerification(inputFirstName)    || !nameVerification(inputLastName) || !phoneNumberVerification(inputPhoneNumber)|| 
			!passwordVerification(inputPassword) || !emailVerification(inputEmail)   || !tcNumberVerification(inputTcNumber)) {
			return false;
		}
		return true;
	}
	
	public int login(String inputPhoneNumber, String inputPassword) {
		
		if( !phoneNumberVerification(inputPhoneNumber) || !passwordVerification(inputPassword) ) {
			return 0;
		}
		
		int result ;
		CallableStatement csProcedureObject = null;
		try {
			
			csProcedureObject = this.getConnection().prepareCall("{CALL CHECK_USER(?,?,?)}");

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
				connection.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		
		return result;
	}

	public int createAccount(String inputFirstName,    String inputLastName,   
				 String inputPhoneNumber,  String inputEmail, 
				 String inputPassword,     String inputBirthDate,
				 String inputTcNumber) {
		
		
		if(!checkUserInfo( inputFirstName, inputLastName,  inputPhoneNumber,
				   inputEmail,     inputPassword,  inputTcNumber)) {
			return 0;
		}

		CallableStatement csProcedureObject = null;
		
		int result;
		try {
			
			csProcedureObject = this.getConnection().prepareCall("{CALL CREATE_USER(?,?,?,?,?,?,?,?)}");
			
			csProcedureObject.registerOutParameter(8, Types.INTEGER);
			csProcedureObject.setString(1, inputFirstName);
			csProcedureObject.setString(2, inputLastName);
			csProcedureObject.setString(3, inputPhoneNumber);
			csProcedureObject.setString(4, inputEmail);
			csProcedureObject.setString(5, inputPassword);
			csProcedureObject.setString(6, inputBirthDate);
			csProcedureObject.setString(7, inputTcNumber);

			csProcedureObject.execute();
			result = csProcedureObject.getInt(8);
			
		} catch (SQLException ex) {
			ex.getMessage();
			result = 0;
		} finally {
			
			try {
				connection.close();
				csProcedureObject.close();
			} catch (SQLException e) {
				
				e.printStackTrace();
			}
		}
		
		return result;
	}
	
	public Integer[] getBalances(String inputPhoneNumber) {
		Integer[] remainResults = new Integer[3];
		
		CallableStatement csProcedureObject = null;
		try {
			
			csProcedureObject = this.getConnection().prepareCall("{CALL GET_BALANCES(?,?,?,?)}");
		
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
				connection.close();
			} catch (SQLException e) {
				
				e.printStackTrace();
			}
			
		}
		return remainResults;
	}
	
	public int changePassword(String inputPhoneNumber, String inputTcNumber,
							  String inputNewPassword ) {
		
		if( !phoneNumberVerification(inputPhoneNumber) || !passwordVerification(inputNewPassword) || 
				!tcNumberVerification(inputTcNumber)) {
			return 0;
		}
		
		int result ;
		CallableStatement csProcedureObject = null;
		
		try {
			
			csProcedureObject = this.getConnection().prepareCall("{CALL CHANGE_PASSWORD(?,?,?,?)}");
			csProcedureObject.setString(1, inputPhoneNumber);
			csProcedureObject.setString(2, inputTcNumber);
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
				connection.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}		
		}
		return result;
	}

	

}
