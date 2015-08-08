package examples;

import java.io.InputStream;
import java.util.HashMap;
import java.util.Scanner;

import models.User;
import db.UserFunctions;

public class EditUser {

	public static void main(String[] args) {

		Scanner in = new Scanner(System.in); //to receive input from user
		InputStream input = null;
		User user = null;					//initialize User
		HashMap<String, String> map = new HashMap<String, String>();//HashMap for errors
		
		do{
		
			System.out.println("Login");
			System.out.println("Username: ");
			String username = in.nextLine();	//user enters user name
			System.out.println("Password: ");
			String password = in.nextLine();	//user enters password
		
			System.out.println("\nProcessing...");
			user = UserFunctions.AppLogin(username, password, input); //Login attempted through database
			map = user.getErrors();							//put any errors into map
			if(map.get("database")!=null){					//if database error occurred, e.g. bad username or password
				System.out.println(map.get("database")+"\n"); //print out error
			}
			
		} while(map.size()>0); 			//loop will go until loginAttempts is exceeded or
															//no errors are returned from database

		System.out.println("Login Successful");
		System.out.println(user.toString());
		
		do{
			System.out.println("\nEdit Values");
			System.out.println("Address: " + user.getAddress());
			String address = in.nextLine();	//user edits address
			System.out.println("Phone Number: " + user.getPhone());
			String phone = in.nextLine();	//user edits phone number
			System.out.println("Email: " + user.getEmail());
			String email = in.nextLine();	//user edits email
			System.out.println("City: " + user.getCity());
			String city = in.nextLine();	//user edits city
			System.out.println("State: " + user.getState());
			String state = in.nextLine();	//user edits state
			System.out.println("Zip Code: " + user.getZipCode());
			String zipcode = in.nextLine();	//user edits zipcode
			System.out.println("Role: " + user.getRole());
			String role = in.nextLine();	//user edits role
			
			//create User from input
			user.editUser(role, phone, email, address, city, state, zipcode);
			map = user.getErrors();	//put any errors into map
			
			if(map.size()>0){ //if there are errors in User model
				System.out.println("\nInvalid Values:");
				
				if(map.get("address")!=null){						//if address error occurred
					System.out.println(map.get("address")); 		//print out error
				}
				
				if(map.get("phone")!=null){						//if phone error occurred
					System.out.println(map.get("phone")); 		//print out error
				}
				
				if(map.get("email")!=null){						//if email error occurred
					System.out.println(map.get("email")); 		//print out error
				}
				
				if(map.get("city")!=null){						//if city error occurred
					System.out.println(map.get("city")); 		//print out error
				}
				
				if(map.get("state")!=null){						//if state error occurred
					System.out.println(map.get("state")); 		//print out error
				}
				
				if(map.get("zipcode")!=null){						//if city error occurred
					System.out.println(map.get("zipcode")); 		//print out error
				}
				
				if(map.get("role")!=null){						//if role error occurred
					System.out.println(map.get("role")); 		//print out error
				}

			}else{ //valid User object edited
				System.out.println("\nUser Edit Successful!");
				System.out.println(user.toString());
				System.out.println("\nUpdating values in database...");
				UserFunctions.AppUpdateUser(user);   //attempt to edit with database
				if(user.getErrors().size()==0){		  //if registration was successful
					System.out.println("Update Successful!");
				}else{								//if registration was unsuccessful
					System.out.println("Update Unsuccessful!");
					System.out.println(user.getErrors().get("database"));
					System.out.println(user.getErrors().get("email"));
					System.out.println();
				}
			}
		}while(map.size()>0);
		
		in.close();

	}

}
