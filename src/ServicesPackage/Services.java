package ServicesPackage;

public class Services {

	private static final int FALSE = 0;

	private Verification verification;
	private ServicesDao servicesDao;

	private Verification getVerification() {
		if (this.verification == null)
			this.verification = new Verification();
		return verification;
	}

	private ServicesDao getServicesDao() {
		if (this.servicesDao == null)
			this.servicesDao = new ServicesDao();
		return servicesDao;
	}

	public int login(String inputPhoneNumber, String inputPassword) {

		if (!getVerification().checkLoginInfo(inputPhoneNumber, inputPassword)) {
			return FALSE;
		}
		int result;
		User user = new User();
		user.setPhoneNumber(inputPhoneNumber);
		user.setPassword(inputPassword);
		result = getServicesDao().login(user);
		
		return result;

	}

	public int createAccount(String inputFirstName, String inputLastName, String inputPhoneNumber, String inputEmail,
			String inputPassword, String inputBirthDate, String inputTcNumber) {

		if (!getVerification().checkUserInfo(inputFirstName, inputLastName, inputPhoneNumber, inputEmail, inputPassword,
				inputTcNumber)) {
			return FALSE;
		}
		int result;
		User user = new User(inputFirstName, inputLastName, inputPhoneNumber, inputEmail, 
				     inputPassword, inputBirthDate, inputTcNumber);
		result = getServicesDao().saveUser(user);

		return result;
	}

	public Double[] getBalances(String inputPhoneNumber) {

		Double[] remainResults = new Double[3];
		User user = new User();
		user.setPhoneNumber(inputPhoneNumber);
		remainResults = getServicesDao().getBalances(user);

		return remainResults;
	}

	public int changePassword(String inputPhoneNumber, String inputTcNumber, String inputNewPassword) {

		if (!getVerification().checkChangePasswordInfo(inputPhoneNumber, inputTcNumber, inputNewPassword)) {
			return FALSE;
		}

		int result;
		User user = new User();
		user.setPhoneNumber(inputPhoneNumber);
		user.setTcNo(inputTcNumber);
		user.setPassword(inputNewPassword);
		result = getServicesDao().changePassword(user);

		return result;
	}

	public String[] showProfile(String inputPhoneNumber) {

		String[] userProfileDetails = new String[7];
		User user = new User();
		user.setPhoneNumber(inputPhoneNumber);
		userProfileDetails = getServicesDao().showProfile(user);

		return userProfileDetails;
	}

}
