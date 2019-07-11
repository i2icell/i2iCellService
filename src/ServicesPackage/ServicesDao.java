package ServicesPackage;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Types;

public class ServicesDao {

	private static final int FALSE = 0;

	private Connector connector;
	private Connection connection;

	private Connector getConnector() {
		if (this.connector == null)
			this.connector = new Connector();
		return connector;
	}

	private Connection getConnection() {
		if (this.connection == null)
			this.connection = this.getConnector().connect();
		return connection;
	}

	public int login(User user) {

		CallableStatement csProcedureObject = null;
		int result;
		
		try {
			csProcedureObject = this.getConnection().prepareCall("{CALL CHECK_USER(?,?,?)}");

			csProcedureObject.setString(1, user.getPhoneNumber());
			csProcedureObject.setString(2, user.getPassword());
			csProcedureObject.registerOutParameter(3, Types.INTEGER);

			csProcedureObject.execute();

			result = csProcedureObject.getInt(3);

		} catch (SQLException ex) {
			ex.getMessage();
			result = FALSE;

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

	public int saveUser(User user) {
		
		int result;
		CallableStatement csProcedureObject = null;
		
		try {
			csProcedureObject = this.getConnection().prepareCall("{CALL CREATE_USER(?,?,?,?,?,?,?,?)}");

			csProcedureObject.setString(1, user.getFirstName());
			csProcedureObject.setString(2, user.getLastName());
			csProcedureObject.setString(3, user.getPhoneNumber());
			csProcedureObject.setString(4, user.getEmail());
			csProcedureObject.setString(5, user.getPassword());
			csProcedureObject.setString(6, user.getBirthDate());
			csProcedureObject.setString(7, user.getTcNo());
			csProcedureObject.registerOutParameter(8, Types.INTEGER);
			
			csProcedureObject.execute();

			result = csProcedureObject.getInt(8);
			
		} catch (SQLException ex) {
			ex.getMessage();
			result = FALSE;
			
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

	public Double[] getBalances(User user) {

		Double[] remainResults = new Double[3];
		CallableStatement csProcedureObject = null;
		
		try {
			csProcedureObject = this.getConnection().prepareCall("{CALL GET_BALANCES(?,?,?,?)}");

			csProcedureObject.setString(1, user.getPhoneNumber());
			csProcedureObject.registerOutParameter(2, Types.DOUBLE);
			csProcedureObject.registerOutParameter(3, Types.DOUBLE);
			csProcedureObject.registerOutParameter(4, Types.DOUBLE);

			csProcedureObject.execute();

			remainResults[0] = csProcedureObject.getDouble(2);
			remainResults[1] = csProcedureObject.getDouble(3);
			remainResults[2] = csProcedureObject.getDouble(4);

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

	public int changePassword(User user) {

		int result;
		CallableStatement csProcedureObject = null;

		try {

			csProcedureObject = this.getConnection().prepareCall("{CALL CHANGE_PASSWORD(?,?,?,?)}");
			
			csProcedureObject.setString(1, user.getPhoneNumber());
			csProcedureObject.setString(2, user.getTcNo());
			csProcedureObject.setString(3, user.getPassword());
			csProcedureObject.registerOutParameter(4, Types.INTEGER);

			csProcedureObject.execute();

			result = csProcedureObject.getInt(4);

		} catch (SQLException ex) {
			ex.getMessage();
			result = FALSE;
			
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

	public String[] showProfile(User user) {

		String[] userProfileDetails = new String[7];
		CallableStatement csProcedureObject = null;

		try {
			csProcedureObject = this.getConnection().prepareCall("{CALL SHOW_PROFILE(?,?,?,?,?,?,?,?)}");

			csProcedureObject.setString(1, user.getPhoneNumber());
			csProcedureObject.registerOutParameter(2, Types.VARCHAR);
			csProcedureObject.registerOutParameter(3, Types.VARCHAR);
			csProcedureObject.registerOutParameter(4, Types.VARCHAR);
			csProcedureObject.registerOutParameter(5, Types.VARCHAR);
			csProcedureObject.registerOutParameter(6, Types.INTEGER);
			csProcedureObject.registerOutParameter(7, Types.INTEGER);
			csProcedureObject.registerOutParameter(8, Types.INTEGER);

			csProcedureObject.execute();

			userProfileDetails[0] = csProcedureObject.getString(2);
			userProfileDetails[1] = csProcedureObject.getString(3);
			userProfileDetails[2] = csProcedureObject.getString(4);
			userProfileDetails[3] = csProcedureObject.getString(5);
			userProfileDetails[4] = csProcedureObject.getString(6);
			userProfileDetails[5] = csProcedureObject.getString(7);
			userProfileDetails[6] = csProcedureObject.getString(8);

		} catch (SQLException ex) {
			userProfileDetails = null;
			ex.getMessage();

		} finally {

			try {
				csProcedureObject.close();
				connection.close();
			} catch (SQLException e) {

				e.printStackTrace();
			}
		}
		return userProfileDetails;
	}

}
