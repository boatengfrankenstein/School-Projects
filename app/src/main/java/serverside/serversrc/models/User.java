package serverside.serversrc.models;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
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
    private String fName;
    private String lName;
    private String role;
    private String email;
    private String phone;
    private String address;
    private String zipCode;
    private String city;
    private String state;
    private String birthday;
    private double clientRating; //max 5
    private double runnerRating; //max 5
    private int comRunsRunner; //completed runs as a runner
    private int comRunsClient; //completed runs as a client
    private String isVerified; 	//for email verification

    private ArrayList<Rating> ratings;

    private LinkedHashMap<String, String> errors; //error map

    /**Creates a User object, all fields are required.
     *This method will never return an exception or error, but will put errors into the errors map
     */
    public User(String userName, String passwd, String retyped, String fName,
                String lName, String role, String phone, String email,
                String city, String state, String zipCode, String birthday) {

        this.userID = 0;
        this.isVerified = null;
        setRatings(null);
        Pattern special = Pattern.compile("[^A-Za-z0-9]+"); //checks for special chars
        Pattern letters = Pattern.compile("[a-zA-Z]+");		//checks for letters
        Pattern notletters = Pattern.compile("[^a-zA-Z]+"); //checks for no letters
        Pattern numbers = Pattern.compile("[0-9]+");		//checks for numbers
        Pattern notnumbers = Pattern.compile("[^0-9-]+");  //checks for no numbers

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

        //phone
        if(phone == null || phone.isEmpty()){
            errors.put("phone", "Phone Number is required");
        }else if(notnumbers.matcher(phone).find() || phone.length()<7){
            errors.put("phone", "Phone Number not valid");
        }
        this.phone = phone;

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

        //zip code
        if(zipCode == null || zipCode.isEmpty()){
            errors.put("zipcode", "Zip Code is required");
        }else if(notnumbers.matcher(zipCode).find() || zipCode.length() < 5){
            errors.put("zipcode", "Zip Code is invalid");
        }
        this.zipCode = zipCode;

        //birthday
        SimpleDateFormat ft = new SimpleDateFormat ("MM-dd-yyyy");
        Date d;
        Date date = new Date();

        if(birthday == null || birthday.isEmpty()){
            errors.put("birthday", "Birthday is required");
        }else if(birthday.length()<8){
            errors.put("birthday", "Birthday is invalid");
        }else{
            try { //checks to see if birthday is in future
                d = ft.parse(birthday);
                if(d.after(date)){
                    errors.put("birthday", "Birthday is invalid");
                }
            } catch (ParseException e) {
                errors.put("birthday", "Birthday is invalid");
            }
        }
        this.birthday = birthday;

        //other values
        this.clientRating = 0;
        this.runnerRating = 0;
        this.comRunsRunner = 0;
        this.comRunsClient = 0;

    }

    /**Initialize a junk user, used for testing
     *
     * @return User object
     */
    public static User initializeBaseUser() {
        User user = new User("username", "password1", "password1", "first", "last", "both", "5555555555","email@gmail.com", "city", "state", "00000","00-00-0000");
        return user;
    }

    /**Initialize a junk user, used for returning database errors
     *
     * @return User object
     */
    public static User initializeErrorUser(){
        User user = new User("error", "error1", "error1", "error", "error", "both", "0000000000", "error@error.com", "error", "error", "00000", "00-00-0000");
        return user;
    }

    /**Edits a User
     */
    public void editUser(String newRole, String newPhone, String newEmail,
                         String newAddress, String newCity, String newState, String newZipcode){

        Pattern notnumbers = Pattern.compile("[^0-9-]+");  //checks for no numbers

        Pattern em = Pattern.compile("^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
                + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$"); //checks for valid email

        Pattern c = Pattern.compile("([a-zA-Z]+|[a-zA-Z]+\\s[a-zA-Z]+)");//checks for valid city and state

        //role
        if(!this.getRole().equals(newRole)){ //if role was changed
            if(newRole == null || newRole.isEmpty()){
                this.addError("role", "Role is required");
            }
            else if(!newRole.equalsIgnoreCase("client") && !newRole.equalsIgnoreCase("runner") &&
                    !newRole.equalsIgnoreCase("both") && !newRole.equalsIgnoreCase("admin")){
                this.addError("role", "Invalid role");
            }else{
                this.setRole(newRole);
            }
        }

        //phone
        if(!this.getPhone().equals(newPhone)) {//if phone number was changed
            if(newPhone == null || newPhone.isEmpty()){
                this.addError("phone", "Phone Number is required");
            }else if(notnumbers.matcher(newPhone).find() || newPhone.length()<7){
                this.addError("phone", "Phone Number not valid");
            }else{
                this.setPhone(newPhone);
            }
        }

        //email
        if(!this.getEmail().equals(newEmail)){ //if email was changed
            if(newEmail == null || newEmail.isEmpty()){
                this.addError("email", "Email is required");
            }else if(!em.matcher(newEmail).find()){
                this.addError("email", "Email is invalid");
            }else{
                this.setEmail(newEmail);
            }
        }

        //address
        if(this.getAddress()==null) this.setAddress("");
        if(!this.getAddress().equals(newAddress)){ //if address was changed

            if(newAddress == null || newAddress.isEmpty()){
                this.addError("address", "Address is required");
            }else if(newAddress.length()<4){
                this.addError("address", "Address must be greater than 4 characters");
            }else if(newAddress.length()>40){
                this.addError("address", "Address must be less than 40 characters");
            }else{
                this.setAddress(newAddress);
            }
        }

        //city
        if(!this.getCity().equals(newCity)){ //if city was changed
            if(newCity == null || newCity.isEmpty()){
                this.addError("city", "City is required");
            }else if(!c.matcher(newCity).find()){
                this.addError("city", "City is invalid");
            }else{
                this.setCity(newCity);
            }
        }

        //state
        if(!this.getState().equals(newState)){ //state was changed
            if(newState == null || newState.isEmpty()){
                this.addError("state", "State is required");
            }else if(!c.matcher(newState).find()){
                this.addError("state", "State is invalid");
            }else{
                this.setState(newState);
            }
        }

        //zip code
        if(!this.getZipCode().equals(newZipcode)) {//zipcode was changed
            if(newZipcode == null || newZipcode.isEmpty()){
                this.addError("zipcode", "Zip Code is required");
            }else if(notnumbers.matcher(newZipcode).find() || newZipcode.length() < 5){
                this.addError("zipcode", "Zip Code is invalid");
            }else{
                this.setZipCode(newZipcode);
            }
        }

    }

    /**Initializes user statistics from database only
     */
    public void initializeStats(int userId, double clientRating, double runnerRating, int comRunsRunner,
                                int comRunsClient){
        this.setUserID(userId);
        this.setClientRating(clientRating);
        this.setRunnerRating(runnerRating);
        this.setComRunsRunner(comRunsRunner);
        this.setComRunsClient(comRunsClient);
    }



    public String printUser(){
        return "Username: " + this.userName + ", First Name: " + this.fName +
                ", Last Name: " + this.lName + ", Role: " + this.role + ", Email: " + this.email+ ", City: " +
                this.city + ", State: " + this.state + ", Birthday: " + this.birthday;
    }

    public String printAddress(){
        return "Phone: " + this.phone + ", Address: " + this.address
                + ", Zip Code: " + this.zipCode + ", City: " + this.city + ", State: " + this.state;
    }

    public String printOthers(){
        return "User Id: " + this.userID + ", Client Rating : " + this.clientRating + ", Runner Rating: "
                + this.runnerRating + ", Completed Runs as Runner: "
                + this.comRunsRunner + ", Completed Runs as Client: "
                + this.comRunsClient;
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

    public String getPasswd() {
        return passwd;
    }

    public String getfName() {
        return fName;
    }

    public String getlName() {
        return lName;
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

    public void setAddress(String address) {
        this.address = address;
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

    public int getComRunsClient() {
        return comRunsClient;
    }

    private void setComRunsClient(int comRunsClient) {
        this.comRunsClient = comRunsClient;
    }

    public String getEmail() {
        return email;
    }

    private void setEmail(String email) {
        this.email = email;
    }

    public double getClientRating() {
        return clientRating;
    }

    private void setClientRating(double clientRating) {
        this.clientRating = clientRating;
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

    public ArrayList<Rating> getRatings() {
        return ratings;
    }

    public void setRatings(ArrayList<Rating> ratings) {
        this.ratings = ratings;
    }

    public String getVerification(){
        return isVerified;
    }

    public void setVerification(String verified){
        this.isVerified = verified;
    }



}