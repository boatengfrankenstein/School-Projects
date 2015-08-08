package examples;

import java.io.InputStream;
import java.util.HashMap;
import java.util.Scanner;

import models.User;
import db.UserFunctions;

public class Login {

	public static void main(String[] args) {
		
		Scanner in = new Scanner(System.in); //to receive input from user
		InputStream input = null;
		User user = null;					//initialize User
		HashMap<String, String> map = new HashMap<String, String>();//HashMap for errors
		
		int loginAttempts = 3; 			//number of login attempts user can have
		
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
				loginAttempts--;
			}
			
		} while(map.size()>0 && loginAttempts!=0); 			//loop will go until loginAttempts is exceeded or
															//no errors are returned from database

		if(loginAttempts==0){	//too many login attempts
			System.out.println("Number of login attempts exceeded.");
		}else{					//successful login
			System.out.println("Login Successful");
			System.out.println(user.printUser());
		}
		in.close();
	}

}
