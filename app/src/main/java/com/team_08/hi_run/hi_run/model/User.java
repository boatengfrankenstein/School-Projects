package com.team_08.hi_run.hi_run.model;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.regex.Pattern;

/*class User written by A.Bylander
 *model for User object
 *constructors will not return errors but instead fill the hash map 'errors'
 *
 *methods:
 *constructor User
 *initializeBaseUser //for database only
 *initializeAddress
 *initializeOther
 *getters and setters
 */
public class User {

	private int userID;
	private String userName;
	private String passwd;
	private String retyped;
	private String fName;
	private String lName;
	private String role;
	private String email;
	private String phone;
	private String address;
	private String aptNo;
	private String addAddrInfo; //additional address information
	private String zipCode;
	private String city;
	private String state;
	private String birthday;
	private double runnerRating; //max 5
	private int comRunsRunner; //completed runs as a runner
	private int failRunsRunner; //failed runs as a runner
	private int comRunsClient; //completed runs as a client
	private int totRunsClient; //total runs as a client
	
	private LinkedHashMap<String, String> errors; //error map

	/**Creates a User object, all fields are required.
	 *This method will never return an exception or error, but will put errors into the errors map
	 */
	public User(String userName, String passwd, String retyped, String fName,
                String lName, String role, String email, String city,
                String state, String birthday) {
		
		this.userID = 0;
		Pattern special = Pattern.compile("[^A-Za-z0-9]+"); //checks for special chars
		Pattern letters = Pattern.compile("[a-zA-Z]+");		//checks for letters
		Pattern notletters = Pattern.compile("[^a-zA-Z]+"); //checks for no letters
		Pattern numbers = Pattern.compile("[0-9]+");		//checks for numbers
		
		Pattern em = Pattern.compile("^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
				+ "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$"); //checks for valid email
		
		Pattern c = Pattern.compile("([a-zA-Z]+|[a-zA-Z]+\\s[a-zA-Z]+)");//checks for valid city and state
		
		errors = new LinkedHashMap<String, String>(); //initialize error map
		
		//user name
		if(userName == null || userName.isEmpty()){
			errors.put("username", "Username is required");
		}else if(userName.length() > 20){
			errors.put("username","Username must be less than 20 characters");
		}else if(userName.length()<5){
			errors.put("username", "Username must be greater than 5 characters");
		}else if(special.matcher(userName).find()){
			errors.put("username", "Username cannot contain special characters");
		}
		this.userName = userName;
		
		//password
		if(passwd == null || passwd.isEmpty()){
			errors.put("password", "Password is required");
		}else if(passwd.length() > 25){
			errors.put("password", "Password must be less than 25 characters");
		}else if(passwd.length() < 6){
			errors.put("password", "Password must be greater than 6 characters");
		}else if(!letters.matcher(passwd).find() || !numbers.matcher(passwd).find()){
			errors.put("password", "Password must contain a letter and a number");
		}else if(retyped == null || !passwd.equals(retyped)){
			errors.put("password", "Passwords do not match");
		}
		this.passwd = passwd;
		this.setRetyped(retyped);
		
		//first name
		if(fName == null || lName.isEmpty()){
			errors.put("fname", "First Name is required");
		}else if(fName.length() > 20){
			errors.put("fName", "First Name must be less than 20 characters");
		}else if(fName.length() < 2){
			errors.put("fName", "First Name must be greater than 2 characters");
		}else if(notletters.matcher(fName).find()){
			errors.put("fName", "First Name can contain only letters");
		}
		this.fName = fName;
		
		//last name
		if(lName == null || lName.isEmpty()){
			errors.put("lname", "Last Name is required");
		}else if(lName.length() > 20){
			errors.put("lName", "Last Name must be less than 20 characters");
		}else if(lName.length() < 2){
			errors.put("lName", "Last Name must be greater than 2 characters");
		}else if(notletters.matcher(lName).find()){
			errors.put("lName", "Last Name can contain only letters");
		}
		this.lName = lName;
		
		//role
		if(role == null || role.isEmpty()){
			errors.put("role", "Role is required");
		}
		else if(!role.equalsIgnoreCase("client") && !role.equalsIgnoreCase("runner") &&
				!role.equalsIgnoreCase("both") && !role.equalsIgnoreCase("admin")){
			errors.put("role", "Invalid role");
		}
		this.role = role;
		
		//email
		if(email == null || email.isEmpty()){
			errors.put("email", "Email is required");
		}else if(!em.matcher(email).find()){
			errors.put("email", "Email is invalid");
		}
		this.email = email;
		
		//city
		if(city == null || city.isEmpty()){
			errors.put("city", "City is required");
		}else if(!c.matcher(city).find()){
			errors.put("city", "City is invalid");
		}
		this.city = city;
		
		//state
		if(state == null || state.isEmpty()){
			errors.put("state", "State is required");
		}else if(!c.matcher(state).find()){
			errors.put("state", "State is invalid");
		}
		this.state = state;
		
		Pattern slash = Pattern.compile("[//]+");
		//birthday
		if(birthday == null || birthday.isEmpty()){
			errors.put("birthday", "Birthday is required");
		}else if(birthday.length()<8){
			errors.put("birthday", "Birthday is invalid");
		}else if(slash.matcher(birthday).find()){
			errors.put("birthday", "Birthday cannot contain '/' characters");
		}
		this.birthday = birthday;
		
		//other values
		this.runnerRating = 0;
		this.comRunsRunner = 0;
		this.failRunsRunner = 0;
		this.comRunsClient = 0;
		this.totRunsClient = 0;
		
	}

	/**Initialize a junk user, used for testing
	 * 
	 * @return User object
	 */
	public static User initializeBaseUser() {
		User user = new User("username", "password1", "password1", "first", "last", "both", "email@gmail.com", "city", "state", "birthday");
		return user;
	}
	
	/**Initialize a junk user, used for returning database errors
	 * 
	 * @return User object
	 */
	public static User initializeErrorUser(){
		User user = new User("error", "error1", "error1", "error", "error", "both", "error@error.com", "error", "error", "birthday");
		return user;
	}
	
	/**Initialize an address w/ phone number
	 */
	public void initializeAddress(String phone, String address, String aptNo, String addAddrInfo, 
			String zipCode, String city, String state){
		
		Pattern notnumbers = Pattern.compile("[^0-9-]+");  //checks for no numbers
		Pattern special = Pattern.compile("[^A-Za-z0-9 ]+");//checks for special characters
		Pattern c = Pattern.compile("([a-zA-Z]+|[a-zA-Z]+\\s[a-zA-Z]+)");//checks for valid city and state
		
		//phone
		if(phone == null || phone.isEmpty()){
			this.addError("phone", "Phone Number is required");
		}else if(notnumbers.matcher(phone).find() || phone.length()<7){
			this.addError("phone", "Phone Number not valid");
		}
		this.setPhone(phone);
		
		//address
		if(address == null || address.isEmpty()){
			this.addError("address", "Address is required");
		}else if(address.length()<4){
			this.addError("address", "Address must be greater than 4 characters");
		}else if(address.length()>30){
			this.addError("address", "Address must be less than 30 characters");
		}else if(special.matcher(address).find()){
			this.addError("address", "Address cannot contain special characters");
		}
		this.setAddress(address);
		
		//apt No
		if(aptNo!=null){
			if(special.matcher(aptNo).find()){
				this.addError("aptNo", "Apt. No cannot contain special characters");
			}
		}
		this.setAptNo(aptNo);
		
		//additional address info
		if(addAddrInfo!=null){
			if(addAddrInfo.length()>100){
				this.addError("addaddrinfo", "Must be less than 100 characters");
			}
		}
		this.setAddAddrInfo(addAddrInfo);
		
		//zip code
		if(zipCode == null || zipCode.isEmpty()){
			this.addError("zipcode", "Zip Code is required");
		}else if(notnumbers.matcher(zipCode).find() || zipCode.length() < 5){
			this.addError("zipcode", "Zip Code is invalid");
		}
		this.setZipCode(zipCode);
		
		//city
		if(city == null){
			this.addError("city", "City is required");
		}else if(!c.matcher(city).find()){
			this.addError("city", "City is invalid");
		}
		this.setCity(city);
		
		//state
		if(state == null){
			this.addError("state", "State is required");
		}else if(!c.matcher(state).find()){
			this.addError("state", "State is invalid");
		}
		this.setState(state);
		
	}
	
	/**Initializes user statistics from database only
	 */
	public void initializeStats(int userId, double runnerRating, int comRunsRunner, int failRunsRunner,
			int comRunsClient, int totRunsClient){
		this.setUserID(userId);
		this.setRunnerRating(runnerRating);
		this.setComRunsRunner(comRunsRunner);
		this.setFailRunsRunner(failRunsRunner);
		this.setComRunsClient(comRunsClient);
		this.setTotRunsClient(totRunsClient);
	}
	
	public String printUser(){
		return "Username: " + this.userName + ", Password: " + this.passwd + ", First Name: " + this.fName +
				", Last Name: " + this.lName + ", Role: " + this.role + ", Email: " + this.email+ ", City: " +
				this.city + ", State: " + this.state + ", Birthday: " + this.birthday;
	}
	
	public String printAddress(){
		return "Phone: " + this.phone + ", Address: " + this.address + ", Apt No: " + this.aptNo + ", Additional Info: " + this.addAddrInfo
				+ ", Zip Code: " + this.zipCode + ", City: " + this.city + ", State: " + this.state;
	}
	
	public String printOthers(){
		return "User Id: " + this.userID + ", Runner Rating: " + this.runnerRating + ", Completed Runs as Runner: "
				+ this.comRunsRunner + ", Failed Runs as Runner: " + this.failRunsRunner + ", Completed Runs as Client: " +
				this.comRunsClient + ", Total Runs as Client " + this.totRunsClient;
	}
	
	public String toString(){
		return printUser() + "\n" + printAddress() + "\n" + printOthers();
	}

	// getters and setters

	public int getUserID() {
		return userID;
	}

	public void setUserID(int userID) {
		this.userID = userID;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getPasswd() {
		return passwd;
	}

	private void setPasswd(String passwd) {
		this.passwd = passwd;
	}

	public String getRetyped() {
		return retyped;
	}

	private void setRetyped(String retyped) {
		this.retyped = retyped;
	}

	public String getfName() {
		return fName;
	}

	private void setfName(String fName) {
		this.fName = fName;
	}

	public String getlName() {
		return lName;
	}

	private void setlName(String lName) {
		this.lName = lName;
	}

	public String getRole() {
		return role;
	}

	private void setRole(String role) {
		this.role = role;
	}

	public String getPhone() {
		return phone;
	}

	private void setPhone(String phone) {
		this.phone = phone;
	}

	public String getAddress() {
		return address;
	}

	private void setAddress(String address) {
		this.address = address;
	}

	public String getAptNo() {
		return aptNo;
	}

	private void setAptNo(String aptNo) {
		this.aptNo = aptNo;
	}

	public String getAddAddrInfo() {
		return addAddrInfo;
	}

	private void setAddAddrInfo(String addAddrInfo) {
		this.addAddrInfo = addAddrInfo;
	}

	public String getZipCode() {
		return zipCode;
	}

	private void setZipCode(String zipCode) {
		this.zipCode = zipCode;
	}

	public String getCity() {
		return city;
	}

	private void setCity(String city) {
		this.city = city;
	}

	public String getState() {
		return state;
	}

	private void setState(String state) {
		this.state = state;
	}

	public String getBirthday() {
		return birthday;
	}

	private void setBirthday(String birthday) {
		this.birthday = birthday;
	}

	public double getRunnerRating() {
		return runnerRating;
	}

	private void setRunnerRating(double runnerRating) {
		this.runnerRating = runnerRating;
	}

	public int getComRunsRunner() {
		return comRunsRunner;
	}

	private void setComRunsRunner(int comRunsRunner) {
		this.comRunsRunner = comRunsRunner;
	}

	public int getFailRunsRunner() {
		return failRunsRunner;
	}

	private void setFailRunsRunner(int failRunsRunner) {
		this.failRunsRunner = failRunsRunner;
	}

	public int getComRunsClient() {
		return comRunsClient;
	}

	private void setComRunsClient(int comRunsClient) {
		this.comRunsClient = comRunsClient;
	}

	public int getTotRunsClient() {
		return totRunsClient;
	}

	private void setTotRunsClient(int totRunsClient) {
		this.totRunsClient = totRunsClient;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public HashMap<String, String> getErrors() {
		return errors;
	}
	
	public void addError(String key, String value){
		errors.put(key, value);
	}
	
	public void clearErrors(){
		errors.clear();
	}

	

}