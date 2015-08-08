package serverside.serversrc.db;

import org.json.JSONException;

import java.io.InputStream;
import java.util.HashMap;

import serverside.serversrc.models.User;

public class UserFunctionsTest {

	public static void main(String[] args) throws JSONException {
		
		//UserFunctions.WSLogin();
		//UserFunctions.WSHello();
		
		//initialize user
		/*User emptyuser = new User("","", "", "","","", "", "", "","");
		HashMap<String, String> map = emptyuser.getErrors();
		System.out.println(map.toString());*/
		
		//logging in
		InputStream input = null;
		User user = UserFunctions.AppLogin("testuser", "abc123", input);
		System.out.println(user.toString());
		HashMap<String, String> map = user.getErrors();
		System.out.println(map.toString());
		
		//registering a user
		/*User user2 = User.initializeBaseUser();
		System.out.println("User Id before adding: " + user2.getUserID());
		user2.setUserName(user2.getUserName());
		user2.setEmail(user2.getEmail());
		UserFunctions.AppAddUser(user2);
		System.out.println("User Id after attempted adding: " + user2.getUserID());
		HashMap<String, String> map = user2.getErrors();
		System.out.println(map.toString());*/

	}

}
