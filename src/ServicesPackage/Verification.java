package ServicesPackage;

public class Verification {
	private static final String PHONE_NUMBER_VERIFICATION_STRING = "\\d{10}$";
	
	private static final String NAME_VERIFICATION_STRING = "[A-Za-z_ğüşıöçĞÜŞİÖÇ]{2,}$";
	
	private static final String TURKISH_PHONE_NUMBER_PREFIX = "5";
	
	private static final String PASSWORD_VERIFICATION_STRING = "(?=.*[0-9])(?=.*[a-z_ğüşıöç])(?=.*[A-Z_ĞÜŞİÖÇ]).{8,12}";
	
	private static final String EMAIL_VERIFICATION_STRING = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
			+ "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
	
	private static final String TC_NUMBER_VERIFICATION_STRING = "\\d{11}$";

	
	public boolean isNameValid(String inputName) {
	    return inputName.matches(NAME_VERIFICATION_STRING);
	}
	
	public boolean isPhoneNumberValid(String inputPhoneNumber) {

		if( String.valueOf(inputPhoneNumber).startsWith(TURKISH_PHONE_NUMBER_PREFIX) ) {

			return inputPhoneNumber.matches(PHONE_NUMBER_VERIFICATION_STRING);
			}
		return false;		
		}
	
	public boolean isPasswordValid(String inputPassword) {
	    return inputPassword.matches(PASSWORD_VERIFICATION_STRING);
	}
	
	
	public boolean isEmailValid(String inputEmail) {		
		return inputEmail.matches(EMAIL_VERIFICATION_STRING) ;
	}
	
	public boolean isTcNumberValid(String inputTcNumber) {
		
		if( !inputTcNumber.matches(TC_NUMBER_VERIFICATION_STRING) ) {
			return false;
			
		}else {
			
			int[] temporaryTcNumber = convertTcNumberToInt(inputTcNumber);
	
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
		}
	}

	private int[] convertTcNumberToInt(String inputTcNumber) {
		int[] temporaryTcNumber = new int[11];
		
		for(int i =0; i<inputTcNumber.length();i++){
			
			temporaryTcNumber[i] = Integer.parseInt(String.valueOf(inputTcNumber.charAt(i)));
		}
		return temporaryTcNumber;
	}
	
	public boolean checkLoginInfo(String inputPhoneNumber, String inputPassword) {

		if ( !isPhoneNumberValid(inputPhoneNumber) || !isPasswordValid(inputPassword) ) {
			return false;
		}
		return true;
	}
	
	public boolean checkUserInfo(String inputFirstName, String inputLastName, String inputPhoneNumber,
			String inputEmail, String inputPassword, String inputTcNumber) {

		if (!isNameValid(inputFirstName) || !isNameValid(inputLastName) || !isPhoneNumberValid(inputPhoneNumber)
				|| !isPasswordValid(inputPassword) || !isEmailValid(inputEmail) || !isTcNumberValid(inputTcNumber)) {
			return false;
		}
		return true;
	}
		
	public boolean checkChangePasswordInfo(String inputPhoneNumber, String inputTcNumber, String inputPassword) {

		if ( !isPhoneNumberValid(inputPhoneNumber) || !isPasswordValid(inputPassword) || !isTcNumberValid(inputTcNumber) ) {
			return false;
		}
		return true;
	}

}
