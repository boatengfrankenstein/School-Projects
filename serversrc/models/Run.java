package models;

import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.regex.Pattern;

public class Run {

	private int runId;
	private int clntId;	
	private int rnerId;
	private String title;
	private String description;	
	private String status;
	private String moneyReqd;
	private String runType;	
	private double amount;	
	private String timeNeeded;	
	private String cAddress;	
	private String cZipCode;
	private String city;	
	private String state;
	private Date timePosted;
	private Date timeAccepted;	
	private Date timeCompleted;
	private int timesFlagged;
	private int cReviewed;
	private int rReviewed;
	private String version;
	
	

	private LinkedHashMap<String, String> errors;
	
	/**Creates a Run object, all fields are required.
	 *This method will never return an exception or error, but will put errors into the errors map
	 */
	public Run(int clientId, String title, String description, String runType, String amount, 
			String address, String city, String state, String zipcode) {
		
		runId = 0;
		status = "Queued";
		moneyReqd = "Yes";
		timeNeeded = "ASAP";
		timesFlagged = 0;
		cReviewed = 0;
		rReviewed = 0;
		timePosted = new Date();
		
		Pattern notnumbers = Pattern.compile("[^0-9.]+");  //checks for no numbers
		Pattern c = Pattern.compile("([a-zA-Z]+|[a-zA-Z]+\\s[a-zA-Z]+)");//checks for valid city and state
		
		errors = new LinkedHashMap<String, String>(); //initialize error map
		
		//clientId
		if(clientId<=0){
			errors.put("clientId", "Invalid client");
		}
		this.clntId = clientId;
		
		//title
		if(title==null|| title.isEmpty()){
			errors.put("title", "Title is required");
		}else if(title.length()<3){
			errors.put("title", "Title must be greater than 3 characters");
		}else if(title.length()>20){
			errors.put("title", "Title must be less than 20 characters");
		}
		this.title = title;
		
		//description
		if(description==null|| description.isEmpty()){
			errors.put("description", "Decription is required");
		}else if(description.length()<5){
			errors.put("description", "Decription must be greater than 5 characters");
		}else if(description.length()>100){
			errors.put("decription", "Decription must be less than 100 characters");
		}
		this.description = description;
		
		if(runType==null|| runType.isEmpty()){
			errors.put("runType", "runType is required");
		}else if(!runType.equalsIgnoreCase("bid") && !runType.equalsIgnoreCase("offer")){
			errors.put("runType", "Invalid runType");
		}
		this.runType = runType;
		
		//amount
		if(amount==null||amount.isEmpty()){
			amount = "0";
		}else if(notnumbers.matcher(amount).find()){
			errors.put("amount", "Invalid amount");
			amount = "0";
		}else{
			double temp = Double.parseDouble(amount);
			if(temp<0){
				errors.put("amount", "Invalid amount");
			}
		}
		this.amount = Double.parseDouble(amount);
		
		//address
		if(address == null || address.isEmpty()){
			errors.put("address", "Address is required");
		}else if(address.length()<4){
			errors.put("address", "Address must be greater than 4 characters");
		}else if(address.length()>40){
			errors.put("address", "Address must be less than 40 characters");
		}
		this.cAddress = address;
		
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
		if(zipcode == null || zipcode.isEmpty()){
			errors.put("zipcode", "Zip Code is required");
		}else if(notnumbers.matcher(zipcode).find() || zipcode.length() < 5){
			errors.put("zipcode", "Zip Code is invalid");
		}
		this.cZipCode = zipcode;
	};
	
	

	public void EditRun(String newTitle, String newDescription, String newAmount, 
			String newAddress, String newCity, String newState, String newZipcode) {
		
		Pattern notnumbers = Pattern.compile("[^0-9.]+");  //checks for no numbers
		Pattern c = Pattern.compile("([a-zA-Z]+|[a-zA-Z]+\\s[a-zA-Z]+)");//checks for valid city and state
		
		//title
		if(!newTitle.equals(this.getTitle())){ //if title was changed
			if(newTitle==null|| newTitle.isEmpty()){
				this.addError("title", "Title is required");
			}else if(newTitle.length()<3){
				this.addError("title", "Title must be greater than 3 characters");
			}else if(newTitle.length()>20){
				this.addError("title", "Title must be less than 20 characters");
			}else{
				this.setTitle(newTitle);
			}
		}
				
		//description
		if(!newDescription.equals(this.getDescription())){ //if description was changed
			if(newDescription==null|| newDescription.isEmpty()){
				this.addError("description", "Description is required");
			}else if(newDescription.length()<5){
				this.addError("description", "Decription must be greater than 5 characters");
			}else if(newDescription.length()>100){
				this.addError("decription", "Description  must be less than 100 characters");
			}else{
				this.setDescription(newDescription);
			}
		}
				
		//amount
		if(!newAmount.equals(Double.toString(this.getAmount()))){ //if amount was changed
			if(notnumbers.matcher(newAmount).find()){
				this.addError("amount", "Invalid amount");
			}else{
				double temp = Double.parseDouble(newAmount);
				if(temp<=0){
					this.addError("amount", "Invalid amount");
				}else{
					this.setAmount(Double.parseDouble(newAmount));
				}
			}		
		}	
				
		//address
		if(!newAddress.equals(this.getcAddress())){ //if address was changed
			if(newAddress == null || newAddress.isEmpty()){
				this.addError("address", "Address is required");
			}else if(newAddress.length()<4){
				this.addError("address", "Address must be greater than 4 characters");
			}else if(newAddress.length()>40){
				this.addError("address", "Address must be less than 40 characters");
			}else{
				this.setcAddress(newAddress);
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
		if(!this.getcZipCode().equals(newZipcode)) {//zipcode was changed
			if(newZipcode == null || newZipcode.isEmpty()){
				this.addError("zipcode", "Zip Code is required");
			}else if(notnumbers.matcher(newZipcode).find() || newZipcode.length() < 5){
				this.addError("zipcode", "Zip Code is invalid");
			}else{
				this.setcZipCode(newZipcode);	
			}
		}
	}

	public static Run getRun(int runId, ArrayList<Run> runs){
		for(int i = 0; i <runs.size(); i ++){
			if(runs.get(i).getRunId()==runId){
				return runs.get(i);
			}
		}
		return null;
	}
	
	public void removeRun(ArrayList<Run> runs){
		for(int i = 0; i <runs.size(); i ++){
			if(runs.get(i).getRunId()==this.getRunId()){
				runs.remove(i);
			}
		}
	}
	
	public void voided(ArrayList<Run> runs){
		this.setStatus("Voided");
		this.removeRun(runs);
		
	}
	
	public void pending(int runnerId, ArrayList<Run> queRuns, ArrayList<Run> penRuns){
		this.setRnerId(runnerId);
		this.setStatus("Pending");
		penRuns.add(0,this);
		this.removeRun(queRuns);
	}
	
	public void refused(ArrayList<Run> penRuns, ArrayList<Run> queRuns){
		this.setRnerId(-1);
		this.setStatus("Queued");
		queRuns.add(0,this);
		this.removeRun(penRuns);
	}
	
	public void accepted(ArrayList<Run> penRuns, ArrayList<Run> accRuns){
		this.setStatus("Accepted");
		this.setTimeAccepted(new Date());
		accRuns.add(0,this);
		this.removeRun(penRuns);
		
	}
	
	public void completed(ArrayList<Run> accRuns, ArrayList<Run> comRuns){
		this.setStatus("Completed");
		this.setTimeCompleted(new Date());
		comRuns.add(0,this);
		this.removeRun(accRuns);
		
	}
	
	public String toString() {
		return "Run [runId=" + runId + ", clntId=" + clntId + ", rnerId="
				+ rnerId + ", title=" + title + ", description=" + description
				+ ", status=" + status + ", moneyReqd=" + moneyReqd
				+ ", runType=" + runType + ", amount=" + amount
				+ ", timeNeeded=" + timeNeeded + ", cAddress=" + cAddress
				+ ", cZipCode=" + cZipCode + ", city=" + city + ", state="
				+ state + ", timePosted=" + timePosted + ", timeAccepted="
				+ timeAccepted + ", timeCompleted=" + timeCompleted
				+ ", timesFlagged=" + timesFlagged + "]\n";
	}

	//getters and setters
	
	public void setRunId(int runId) {
		this.runId = runId;
	}

	public void setRnerId(int rnerId) {
		this.rnerId = rnerId;
	}

	private void setTitle(String title) {
		this.title = title;
	}

	private void setDescription(String description) {
		this.description = description;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public void setRunType(String runType) {
		this.runType = runType;
	}

	private void setAmount(double amount) {
		this.amount = amount;
	}

	public void setTimeNeeded(String timeNeeded) {
		this.timeNeeded = timeNeeded;
	}

	private void setcAddress(String cAddress) {
		this.cAddress = cAddress;
	}

	private void setcZipCode(String cZipCode) {
		this.cZipCode = cZipCode;
	}

	private void setCity(String city) {
		this.city = city;
	}

	private void setState(String state) {
		this.state = state;
	}

	public void setTimeAccepted(Date timeAccepted) {
		this.timeAccepted = timeAccepted;
	}

	public void setTimeCompleted(Date timeCompleted) {
		this.timeCompleted = timeCompleted;
	}

	public void setTimesFlagged(int timesFlagged) {
		this.timesFlagged = timesFlagged;
		if(this.timesFlagged >= 5){
			this.setStatus("Voided");
		}
	}

	public int getRunId() {
		return runId;
	}

	public int getClntId() {
		return clntId;
	}

	public int getRnerId() {
		return rnerId;
	}

	public String getTitle() {
		return title;
	}

	public String getDescription() {
		return description;
	}

	public String getStatus() {
		return status;
	}

	public String getMoneyReqd() {
		return moneyReqd;
	}

	public String getRunType() {
		return runType;
	}

	public double getAmount() {
		return amount;
	}

	public String getTimeNeeded() {
		return timeNeeded;
	}

	public String getcAddress() {
		return cAddress;
	}

	public String getcZipCode() {
		return cZipCode;
	}

	public String getCity() {
		return city;
	}

	public String getState() {
		return state;
	}

	public Date getTimePosted() {
		return timePosted;
	}

	public Date getTimeAccepted() {
		return timeAccepted;
	}

	public Date getTimeCompleted() {
		return timeCompleted;
	}

	public int getTimesFlagged() {
		return timesFlagged;
	}

	public LinkedHashMap<String, String> getErrors() {
		return errors;
	}
	
	public void addError(String key, String value){
		errors.put(key, value);
	}
	
	public void clearErrors(){
		errors.clear();
	}
	
	public void setTimePosted(Date timePosted) {
		this.timePosted = timePosted;
	}
	
	public String getVersion() {
		return version;
	}

	public void setVersion(String version) {
		this.version = version;
	}
	
	public int getRReviewed() {
		return rReviewed;
	}

	public void setRReviewed(int rReviewed) {
		this.rReviewed = rReviewed;
	}
	
	public int getCReviewed() {
		return cReviewed;
	}

	public void setCReviewed(int cReviewed) {
		this.cReviewed = cReviewed;
	}
}
