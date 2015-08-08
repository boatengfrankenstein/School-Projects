package examples;

import java.io.InputStream;
import java.util.HashMap;
import java.util.Scanner;

import db.UserFunctions;
import models.User;

public class Register {

	public static void main(String[] args) {
		
		Scanner in = new Scanner(System.in);
		InputStream input = null;
		User user = null;					//initialize User
		HashMap<String, String> map = new HashMap<String, String>();//HashMap for errors
		
		int registerAttempts = 5; //number of register attempts allowed
		
		do{

			System.out.println("Register");
			System.out.println("Username: ");
			String username = in.nextLine();	//user enters user name
			System.out.println("Password: ");
			String password = in.nextLine();	//user enters password
			System.out.println("Retype Password: ");
			String retyped = in.nextLine();	//user enters retyped password
			System.out.println("First Name: ");
			String firstname = in.nextLine();	//user enters first name
			System.out.println("Last Name: ");
			String lastname = in.nextLine();	//user enters last name
			System.out.println("Phone Number: ");
			String phone = in.nextLine();	//user enters phone number
			System.out.println("Email: ");
			String email = in.nextLine();	//user enters email
			System.out.println("City: ");
			String city = in.nextLine();	//user enters city
			System.out.println("State: ");
			String state = in.nextLine();	//user enters state
			System.out.println("Zip Code: ");
			String zipcode = in.nextLine();	//user enters zipcode
			System.out.println("Birthday: ");
			String birthday = in.nextLine();	//user enters birthday
			System.out.println("Role (Client/Runner): ");
			String role = in.nextLine();	//user enters role
			
			//create User from input
			user = new User(username, password, retyped, firstname, lastname, role, phone, email, city, state, zipcode, birthday);
			
			map = user.getErrors();	//put any errors into map
			
			if(map.size()>0){ //if there are errors in User model
				System.out.println("\nInvalid User:");
				
				if(map.get("username")!=null){					//if user name error occurred
					System.out.println(map.get("username")); 	//print out error
				}
				
				if(map.get("password")!=null){					//if password error occurred
					System.out.println(map.get("password")); 	//print out error
				}
				
				if(map.get("fName")!=null){						//if first name error occurred
					System.out.println(map.get("fName")); 		//print out error
				}
				
				if(map.get("lName")!=null){						//if last name error occurred
					System.out.println(map.get("lName")); 		//print out error
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
				
				if(map.get("zipcode")!=null){						//if zipcode error occurred
					System.out.println(map.get("zipcode")); 		//print out error
				}
				
				if(map.get("birthday")!=null){					//if birthday error occurred
					System.out.println(map.get("birthday")); 	//print out error
				}
				
				if(map.get("role")!=null){						//if role error occurred
					System.out.println(map.get("role")); 		//print out error
				}
				
				registerAttempts--;
			}else{ //valid User object created
				System.out.println("\nUser Creation Successful!");
				System.out.println("\nRegistering with database...");
				UserFunctions.AppAddUser(user, input);   //attempt to register with database
				if(user.getUserID()!=0){		  //if registration was successful
					System.out.println("Register Successful!");
					System.out.println("User Id from database: " + user.getUserID());
				}else{								//if registration was unsuccessful
					System.out.println("Register Unsuccessful!");
					System.out.println(user.getErrors().get("database"));
					System.out.println(user.getErrors().get("username"));
					System.out.println(user.getErrors().get("email"));
					System.out.println();
				}
			}
			
		}while(map.size()>0 && registerAttempts!=0);
		
		if(registerAttempts==0){
			System.out.println("\nRegister attempts exceeded");
		}
		
		do{
			System.out.println("Please enter verification code:");
			String v = in.nextLine(); //user enter verification
			if(v.equalsIgnoreCase(user.getVerification())){
				UserFunctions.AppVerifyUser(user);
			}
		}while(!user.getVerification().equals("Yes"));

		in.close();
	}

}
