package serverside.serversrc.models;

import java.util.LinkedHashMap;

public class Rating {
	
	private int runId;
	private int ratedId; //the user id of the rated user
	private String postedName; //the user name of the reviewing user
	private double rating;
	private String review;
	
	private LinkedHashMap<String, String> errors;
	
	public Rating(int runId, int ratedId, String postedName, double rating, String review){
		
		this.runId = runId;
		this.ratedId = ratedId;
		this.postedName = postedName;
		errors = new LinkedHashMap<String, String>(); //initialize error map
		
		//Pattern notnumbers = Pattern.compile("[^0-9.]+");  //checks for no numbers
		
		if(rating < 0 || rating > 5){
			errors.put("rating", "invalid rating");
		}/*else if(notnumbers.matcher(rating).find()){
			errors.put("rating", "Invalid rating");
			rating = "0";
		}*/
		this.rating = rating;
		
		if(review.length()>100){
			errors.put("review", "review is too long");
		}
		this.review = review;
		
	}
	
	public String toString(){
		return "Run Id: " + this.runId + ", Rated Id: " + this.ratedId + ", Posted Name: " + this.postedName +
				"\nRating: " + this.rating + ", Review: " + this.review + "\n";
	}

	//getters and setters
	
	public int getRunId() {
		return runId;
	}

	public int getRatedId() {
		return ratedId;
	}

	public String getPostedName() {
		return postedName;
	}

	public double getRating() {
		return rating;
	}

	public String getReview() {
		return review;
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
	
	

}
