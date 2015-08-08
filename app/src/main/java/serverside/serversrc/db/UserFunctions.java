package serverside.serversrc.db;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Random;

import serverside.serversrc.madmarcos.CryptoStuff;
import serverside.serversrc.madmarcos.HTTPMethod;
import serverside.serversrc.madmarcos.WSStuff;
import serverside.serversrc.models.Rating;
import serverside.serversrc.models.User;

public class UserFunctions {
	
	private static String login = "ws_team08"; //login user name
	private static String password = CryptoStuff.hashSha256("FHpx832nvyqJ7duW"); //hash of login password
	private static String appCode = "tzvcjFK4Zr";
	private static File certFile = new File("./serversrc/fulgentcorp.crt"); //certification file
	
	private static WSStuff ws = new WSStuff("https://devcloud.fulgentcorp.com/bifrost/ws.php");
	private static long sessionID;
	private static String sessionSalt;
	
	public static void WSLogin(InputStream input) throws JSONException { //uses the login web service
		//ws.initSSLContext(certFile);
		ws.initKeyStore(input);
		
		JSONArray array1 = new JSONArray();
		
		JSONObject obj1 = new JSONObject(); //login object
		obj1.put("action", "login");
		array1.put(obj1);
		
		obj1 = new JSONObject();
		obj1.put("login", login);
		array1.put(obj1);
		
		obj1 = new JSONObject();
		obj1.put("password", password);
		array1.put(obj1);
		
		obj1 = new JSONObject();
		obj1.put("app_code", appCode);
		array1.put(obj1);
		
		String s = CryptoStuff.addCheckSum(array1);
		array1 = new JSONArray(s);
		
		//System.out.println("Login request being sent to web service:");
		//System.out.println(array1.toString());
		
		String r = ws.sendRequest(HTTPMethod.GET, array1.toString()); //sends login request
		JSONArray arr = new JSONArray(r); //converts returned array into JSONArray
		//System.out.println("\nWeb service response to login request:");
		//System.out.println(arr.toString());
		
		JSONObject o = CryptoStuff.getJSONObjectFromKey(arr, "session_id");
		String temp = o.get("session_id").toString();
		sessionID = Integer.parseInt(temp);
		sessionSalt = CryptoStuff.getJSONObjectFromKey(arr, "session_salt").getString("session_salt");
	}
	
	public static void WSHello() throws JSONException { //uses the hello web service
		JSONArray arr2 = new JSONArray();
		
		JSONObject obj2 = new JSONObject(); //hello object
		obj2.put("action", "hello");
		arr2.put(obj2); //puts current json object into array
		
		obj2 = new JSONObject(); //hello object
		obj2.put("session_id", sessionID); //puts session id from object
		arr2.put(obj2); //puts current json object into array
		
		String s = CryptoStuff.addCheckSum(arr2, sessionSalt); //calculates and adds checksum to JSONArray using salt
		arr2 = new JSONArray(s); //converts string into JSONArray
		//System.out.println("\nHello request being sent to the web service:");
		//System.out.println(arr2.toString());
		
		s = ws.sendRequest(HTTPMethod.GET, arr2.toString()); //sends hello request
		arr2 = new JSONArray(s); //converts returned array into JSONArray
		//System.out.println("\nWeb service response to hello request:");
		//System.out.println(arr2.toString());
	}
	
	/**
	 * Single query login, method takes a user name and password and returns a User Object.
	 * If error, User.getErrors() will not be empty
	 * @param username
	 * @param password
	 * @return
	 */
	public static User AppLogin(String username, String password, InputStream input) throws JSONException {
		WSLogin(input);
		
		JSONArray arr = new JSONArray();
		JSONObject obj3 = new JSONObject(); //run sql object
		obj3.put("action", "run_sql");
		arr.put(obj3);
		
		obj3 = new JSONObject();
		obj3.put("session_id", sessionID); //puts session id from object
		arr.put(obj3);
		
		obj3 = new JSONObject();
		obj3.put("query" , "SELECT * FROM Users "
				+ "WHERE username = :username AND passHash = :password;");
		arr.put(obj3);
		
		JSONObject uName = new JSONObject(); //username arg
		uName.put(":username", username);
		JSONObject pWord = new JSONObject(); //password arg
		pWord.put(":password", CryptoStuff.hashSha256(password));
		JSONArray args = new JSONArray();    //args object
		args.put(uName);
		args.put(pWord);
		
		obj3 = new JSONObject();
		obj3.put("args", args); //args
		arr.put(obj3);
		
		String s = CryptoStuff.addCheckSum(arr, sessionSalt); //adds checksum w/ session salt
		//System.out.println("\nSQL request being sent to the web service:");
		//System.out.println(arr.toString());
		
		s = ws.sendRequest(HTTPMethod.GET, arr.toString()); //request sent
		arr = new JSONArray(s);
		//System.out.println("\nWeb service response to SQL request:");
		//System.out.println(arr.toString());
		
		User user = null;
		
		JSONObject result = CryptoStuff.getJSONObjectFromKey(arr, "result"); //gets result object from array
		if(result.getString("result").equals("success")){					 //if result is successful
			JSONObject message = CryptoStuff.getJSONObjectFromKey(arr, "message"); //get message object from array
			if(message.get("message") instanceof JSONArray){				 //checks to see if the message returned a JSONArray
				JSONArray rowSet = message.getJSONArray("message"); //get rowSet array from message Object
				JSONArray userArr = rowSet.getJSONArray(0); //get the first and only row in the RowSet array
			
				int uId = CryptoStuff.getJSONObjectFromKey(userArr, "userId").getInt("userId");				//user id
				String fn = CryptoStuff.getJSONObjectFromKey(userArr, "firstName").getString("firstName");	//first name
				String ln = CryptoStuff.getJSONObjectFromKey(userArr, "lastName").getString("lastName");	//last name
				String r = CryptoStuff.getJSONObjectFromKey(userArr, "role").getString("role");				//role
				String e = CryptoStuff.getJSONObjectFromKey(userArr, "email").getString("email");			//email
				String p = CryptoStuff.getJSONObjectFromKey(userArr, "phone").getString("phone");			//phone
				String a = CryptoStuff.getJSONObjectFromKey(userArr, "address").getString("address");		//address
				String z = CryptoStuff.getJSONObjectFromKey(userArr, "zipCode").getString("zipCode");		//zip code
				String c = CryptoStuff.getJSONObjectFromKey(userArr, "city").getString("city");				//city
				String st = CryptoStuff.getJSONObjectFromKey(userArr, "state").getString("state");			//state
				String b = CryptoStuff.getJSONObjectFromKey(userArr, "birthday").getString("birthday");		//birthday
				double rr = CryptoStuff.getJSONObjectFromKey(userArr, "ratingR").getDouble("ratingR");		//runner rating
				double cr = CryptoStuff.getJSONObjectFromKey(userArr, "ratingC").getDouble("ratingC");		//client rating
				int crr = CryptoStuff.getJSONObjectFromKey(userArr, "completeRunsR").getInt("completeRunsR");	//completed runs as runner
				int crc = CryptoStuff.getJSONObjectFromKey(userArr, "completeRunsC").getInt("completeRunsC");	//completed runs as client
				String un = CryptoStuff.getJSONObjectFromKey(userArr, "username").getString("username");	//user name
				String pa = CryptoStuff.getJSONObjectFromKey(userArr, "passHash").getString("passHash");	//password
				String v = CryptoStuff.getJSONObjectFromKey(userArr, "isVerified").getString("isVerified");		//verification
				user = new User(un,pa, pa, fn, ln, r, p, e, c, st, z, b);											//creates User
				user.setAddress(a);			//put address info into User
				user.setVerification(v);
				user.initializeStats(uId, cr, rr, crr,crc);										//puts statistics into User
				user.clearErrors();
			}else{	//message is instance of String, no rowset returned
				user = User.initializeErrorUser();
				user.addError("database", "Invalid username or password");	
			}
		}else{ //database error
			JSONObject message = CryptoStuff.getJSONObjectFromKey(arr, "message");
			user = User.initializeErrorUser();
			user.addError("database", message.getString("message"));
		}
		return user;
	}
	
	/**
	 * Single query addUser method, method takes a User object as parameter.
	 * If successful, UserId is set
	 * 
	 * If error, User.getErrors() will not be empty
	 * @param newUser
	 */
	public static void AppAddUser(User newUser, InputStream input) throws JSONException {
		WSLogin(input);
		
		JSONArray arr = new JSONArray();
		JSONObject obj3 = new JSONObject(); //run sql object
		obj3.put("action", "run_sql");
		arr.put(obj3);
		
		obj3 = new JSONObject();
		obj3.put("session_id", sessionID); //puts session id from object
		arr.put(obj3);

		obj3 = new JSONObject();
		obj3.put("query" , "INSERT INTO Users(username, passHash, email, firstName, lastName, role, phone, city, state, zipCode, birthday, isVerified) "
				+ "VALUES (:username, :password, :email, :firstName, :lastName, :role, :phone, :city, :state, :zipCode, :birthday, :vString);");
		arr.put(obj3);
		
		JSONObject uName = new JSONObject(); //username arg
		uName.put(":username", newUser.getUserName());
		JSONObject pWord = new JSONObject(); //password arg
		pWord.put(":password", CryptoStuff.hashSha256(newUser.getPasswd()));
		JSONObject em = new JSONObject(); //email arg
		em.put(":email", newUser.getEmail());
		JSONObject fn = new JSONObject(); //first name arg
		fn.put(":firstName", newUser.getfName());
		JSONObject ln = new JSONObject(); //last name arg
		ln.put(":lastName", newUser.getlName());
		JSONObject r = new JSONObject(); //role arg
		r.put(":role", newUser.getRole());
		JSONObject p = new JSONObject(); //phone arg
		p.put(":phone", newUser.getPhone());
		JSONObject c = new JSONObject(); //city arg
		c.put(":city", newUser.getCity());
		JSONObject st = new JSONObject(); //state arg
		st.put(":state", newUser.getState());
		JSONObject z = new JSONObject(); //zipcode arg
		z.put(":zipCode", newUser.getZipCode());
		JSONObject b = new JSONObject(); //birthday arg
		b.put(":birthday", newUser.getBirthday());
		
		String vs = generateVString();
		
		JSONObject v = new JSONObject();
		v.put("vString", vs);
		JSONArray args = new JSONArray();    //args object
		args.put(fn);
		args.put(ln);
		args.put(r);
		args.put(p);
		args.put(c);
		args.put(st);
		args.put(z);
		args.put(b);
		args.put(v);
		args.put(uName);
		args.put(pWord);
		args.put(em);
		
		obj3 = new JSONObject();
		obj3.put("args", args); //args
		arr.put(obj3);
		
		String s = CryptoStuff.addCheckSum(arr, sessionSalt); //adds checksum w/ session salt
		//System.out.println("\nSQL request being sent to the web service:");
		//System.out.println(arr.toString());
				
		s = ws.sendRequest(HTTPMethod.GET, arr.toString()); //request sent
		arr = new JSONArray(s);
		//System.out.println("\nWeb service response to SQL request:");
		//System.out.println(arr.toString());
		
		JSONObject result = CryptoStuff.getJSONObjectFromKey(arr, "result"); //gets result object from array
		
		if(result.getString("result").equals("success")){					 //if result is successful
			JSONObject message = CryptoStuff.getJSONObjectFromKey(arr, "message"); //get message object from array
			JSONObject m = message.getJSONObject("message"); //get message from message Object
			String uId = m.getString("insert_id");			 //puts inserted id into string
			newUser.setUserID(Integer.parseInt(uId));
			newUser.setVerification(vs);

		}else{ //database error, user name or email taken or other database error
			JSONObject message = CryptoStuff.getJSONObjectFromKey(arr, "message");
			String error = message.getString("message");
			
			if(error.contains("username")){
				newUser.addError("username", "Username is taken");
			}else if(error.contains("email")){
				newUser.addError("email", "Email is already in use");
			}else{
				newUser.addError("database", error);
			}
			
		}

	}
	
	/**
	 * Single query UpdateUser method, method takes a User object as parameter.
	 * If successful, User is updated
	 * 
	 * If error, User.getErrors() will not be empty
	 * @param user
	 */
	public static void AppUpdateUser(User user) throws JSONException {
		
		JSONArray arr = new JSONArray();
		JSONObject obj3 = new JSONObject(); //run sql object
		obj3.put("action", "run_sql");
		arr.put(obj3);
		
		obj3 = new JSONObject();
		obj3.put("session_id", sessionID); //puts session id from object
		arr.put(obj3);

		obj3 = new JSONObject();
		obj3.put("query" , "UPDATE Users "
				+ "SET address = :address, email = :email, role = :role, phone = :phone, city = :city, state = :state, zipCode = :zipCode, "
				+ "ratingC = :cRating, ratingR = :rating, completeRunsR = :ccr, completeRunsC = :crc "
				+ "WHERE userId = :userId");
		arr.put(obj3);
		
		JSONObject a = new JSONObject(); //address arg
		a.put(":address", user.getAddress());
		JSONObject em = new JSONObject(); //email arg
		em.put(":email", user.getEmail());
		JSONObject r = new JSONObject(); //role arg
		r.put(":role", user.getRole());
		JSONObject p = new JSONObject(); //phone arg
		p.put(":phone", user.getPhone());
		JSONObject c = new JSONObject(); //city arg
		c.put(":city", user.getCity());
		JSONObject st = new JSONObject(); //state arg
		st.put(":state", user.getState());
		JSONObject z = new JSONObject(); //zipcode arg
		z.put(":zipCode", user.getZipCode());
		JSONObject rat = new JSONObject(); //runner rating arg
		rat.put(":rating", user.getRunnerRating());
		JSONObject cRat = new JSONObject(); //client rating arg
		cRat.put(":cRating", user.getClientRating());
		JSONObject ccr = new JSONObject(); //complete runs as runner arg
		ccr.put(":ccr", user.getComRunsRunner());
		JSONObject crc = new JSONObject(); //complete runs as client arg
		crc.put(":crc", user.getComRunsClient());
		JSONObject u = new JSONObject(); //user id arg
		u.put(":userId", user.getUserID());
		
		JSONArray args = new JSONArray();    //args object
		args.put(r);
		args.put(p);
		args.put(c);
		args.put(st);
		args.put(z);
		args.put(a);
		args.put(em);
		args.put(rat);
		args.put(cRat);
		args.put(ccr);
		args.put(crc);
		args.put(u);
		
		obj3 = new JSONObject();
		obj3.put("args", args); //args
		arr.put(obj3);
		
		String s = CryptoStuff.addCheckSum(arr, sessionSalt); //adds checksum w/ session salt
		//System.out.println("\nSQL request being sent to the web service:");
		//System.out.println(arr.toString());
				
		s = ws.sendRequest(HTTPMethod.GET, arr.toString()); //request sent
		arr = new JSONArray(s);
		//System.out.println("\nWeb service response to SQL request:");
		//System.out.println(arr.toString());
		
		JSONObject result = CryptoStuff.getJSONObjectFromKey(arr, "result"); //gets result object from array
		
		if(!result.getString("result").equals("success")){					 //if result is successful

			JSONObject message = CryptoStuff.getJSONObjectFromKey(arr, "message");
			String error = message.getString("message");
			
			if(error.contains("email")){
				user.addError("email", "Email is already in use");
			}else{
				user.addError("database", error);
			}
			
		}
	}
	
	public static User getClient(int clientId) throws JSONException {
		JSONArray arr = new JSONArray();
		JSONObject obj3 = new JSONObject(); //run sql object
		obj3.put("action", "run_sql");
		arr.put(obj3);
		
		obj3 = new JSONObject();
		obj3.put("session_id", sessionID); //puts session id from object
		arr.put(obj3);
		
		obj3 = new JSONObject();
		obj3.put("query" , "SELECT * "
				+ "FROM `Users` t1 "
				+ "LEFT JOIN `ClientRatings` t2 ON t1.userId = t2.clientId  "
				+ "WHERE t2.clientId IS NULL AND t1.userId = :clientId OR t1.userId = :clientId");
		arr.put(obj3);
		
		JSONObject cId = new JSONObject(); //clientId arg
		cId.put(":clientId", clientId);
		JSONArray args = new JSONArray();    //args object
		args.put(cId);
		
		obj3 = new JSONObject();
		obj3.put("args", args); //args
		arr.put(obj3);
		
		String s = CryptoStuff.addCheckSum(arr, sessionSalt); //adds checksum w/ session salt
		//System.out.println("\nSQL request being sent to the web service:");
		//System.out.println(arr.toString());
		
		s = ws.sendRequest(HTTPMethod.GET, arr.toString()); //request sent
		arr = new JSONArray(s);
		//System.out.println("\nWeb service response to SQL request:");
		//System.out.println(arr.toString());
		
		User user = null;
		
		JSONObject result = CryptoStuff.getJSONObjectFromKey(arr, "result"); //gets result object from array
		if(result.getString("result").equals("success")){					 //if result is successful
			JSONObject message = CryptoStuff.getJSONObjectFromKey(arr, "message"); //get message object from array
			if(message.get("message") instanceof JSONArray){				 //checks to see if the message returned a JSONArray
				JSONArray rowSet = message.getJSONArray("message"); //get rowSet array from message Object
				ArrayList<Rating> ratings = new ArrayList<Rating>();
				int firsttime = 0;
				for (int i = 0; i < rowSet.length();i++){
					JSONArray userArr = rowSet.getJSONArray(i); 
			
					if(firsttime == 0){
						int uId = CryptoStuff.getJSONObjectFromKey(userArr, "userId").getInt("userId");				//user id
						String fn = CryptoStuff.getJSONObjectFromKey(userArr, "firstName").getString("firstName");	//first name
						String ln = CryptoStuff.getJSONObjectFromKey(userArr, "lastName").getString("lastName");	//last name
						String r = CryptoStuff.getJSONObjectFromKey(userArr, "role").getString("role");				//role
						String e = CryptoStuff.getJSONObjectFromKey(userArr, "email").getString("email");			//email
						String p = CryptoStuff.getJSONObjectFromKey(userArr, "phone").getString("phone");			//phone
						String z = CryptoStuff.getJSONObjectFromKey(userArr, "zipCode").getString("zipCode");		//zip code
						String c = CryptoStuff.getJSONObjectFromKey(userArr, "city").getString("city");				//city
						String st = CryptoStuff.getJSONObjectFromKey(userArr, "state").getString("state");			//state
						String b = CryptoStuff.getJSONObjectFromKey(userArr, "birthday").getString("birthday");		//birthday
						double rr = CryptoStuff.getJSONObjectFromKey(userArr, "ratingR").getDouble("ratingR");		//runner rating
						double cr = CryptoStuff.getJSONObjectFromKey(userArr, "ratingC").getDouble("ratingC");		//client rating
						int crr = CryptoStuff.getJSONObjectFromKey(userArr, "completeRunsR").getInt("completeRunsR");	//completed runs as runner
						int crc = CryptoStuff.getJSONObjectFromKey(userArr, "completeRunsC").getInt("completeRunsC");	//completed runs as client
						String un = CryptoStuff.getJSONObjectFromKey(userArr, "username").getString("username");	//user name
						String pa = CryptoStuff.getJSONObjectFromKey(userArr, "passHash").getString("passHash");	//password
						String v = CryptoStuff.getJSONObjectFromKey(userArr, "isVerified").getString("isVerified");		//verification
						user = new User(un,pa, pa, fn, ln, r, p, e, c, st, z, b);											//creates User
						user.initializeStats(uId, cr, rr, crr,crc);										//puts statistics into User
						user.setVerification(v);
						user.clearErrors();
						firsttime = 1;
					}
					if(!CryptoStuff.getJSONObjectFromKey(userArr, "rId").isNull("rId")){
						int rId = CryptoStuff.getJSONObjectFromKey(userArr, "rId").getInt("rId");
						String runnerName = CryptoStuff.getJSONObjectFromKey(userArr, "runnerName").getString("runnerName");
						double rating = CryptoStuff.getJSONObjectFromKey(userArr, "rating").getDouble("rating");
						String review = CryptoStuff.getJSONObjectFromKey(userArr, "review").getString("review");
						
						Rating r = new Rating(rId, clientId, runnerName, rating, review);
						ratings.add(0,r);
						
					}else{
						break;
					}
				}
				user.setRatings(ratings);
			}else{	//message is instance of String, no rowset returned
				user = User.initializeErrorUser();
				user.addError("database", "Invalid userId");	
			}
		}else{ //database error
			JSONObject message = CryptoStuff.getJSONObjectFromKey(arr, "message");
			user = User.initializeErrorUser();
			user.addError("database", message.getString("message"));
		}
		return user;
	}
	
	public static User getRunner(int runnerId) throws JSONException {
		JSONArray arr = new JSONArray();
		JSONObject obj3 = new JSONObject(); //run sql object
		obj3.put("action", "run_sql");
		arr.put(obj3);
		
		obj3 = new JSONObject();
		obj3.put("session_id", sessionID); //puts session id from object
		arr.put(obj3);
		
		obj3 = new JSONObject();
		obj3.put("query" , "SELECT * "
				+ "FROM `Users` t1 "
				+ "LEFT JOIN `RunnerRatings` t2 ON t1.userId = t2.runnerId  "
				+ "WHERE t2.runnerId IS NULL AND t1.userId = :runnerId OR t1.userId = :runnerId");
		arr.put(obj3);
		
		JSONObject cId = new JSONObject(); //runnerId arg
		cId.put(":runnerId", runnerId);
		JSONArray args = new JSONArray();    //args object
		args.put(cId);
		
		obj3 = new JSONObject();
		obj3.put("args", args); //args
		arr.put(obj3);
		
		String s = CryptoStuff.addCheckSum(arr, sessionSalt); //adds checksum w/ session salt
		//System.out.println("\nSQL request being sent to the web service:");
		//System.out.println(arr.toString());
		
		s = ws.sendRequest(HTTPMethod.GET, arr.toString()); //request sent
		arr = new JSONArray(s);
		//System.out.println("\nWeb service response to SQL request:");
		//System.out.println(arr.toString());
		
		User user = null;
		
		JSONObject result = CryptoStuff.getJSONObjectFromKey(arr, "result"); //gets result object from array
		if(result.getString("result").equals("success")){					 //if result is successful
			JSONObject message = CryptoStuff.getJSONObjectFromKey(arr, "message"); //get message object from array
			if(message.get("message") instanceof JSONArray){				 //checks to see if the message returned a JSONArray
				JSONArray rowSet = message.getJSONArray("message"); //get rowSet array from message Object
				ArrayList<Rating> ratings = new ArrayList<Rating>();
				int firsttime = 0;
				for (int i = 0; i < rowSet.length();i++){
					JSONArray userArr = rowSet.getJSONArray(i); 
			
					if(firsttime == 0){
						int uId = CryptoStuff.getJSONObjectFromKey(userArr, "userId").getInt("userId");				//user id
						String fn = CryptoStuff.getJSONObjectFromKey(userArr, "firstName").getString("firstName");	//first name
						String ln = CryptoStuff.getJSONObjectFromKey(userArr, "lastName").getString("lastName");	//last name
						String r = CryptoStuff.getJSONObjectFromKey(userArr, "role").getString("role");				//role
						String e = CryptoStuff.getJSONObjectFromKey(userArr, "email").getString("email");			//email
						String p = CryptoStuff.getJSONObjectFromKey(userArr, "phone").getString("phone");			//phone
						String z = CryptoStuff.getJSONObjectFromKey(userArr, "zipCode").getString("zipCode");		//zip code
						String c = CryptoStuff.getJSONObjectFromKey(userArr, "city").getString("city");				//city
						String st = CryptoStuff.getJSONObjectFromKey(userArr, "state").getString("state");			//state
						String b = CryptoStuff.getJSONObjectFromKey(userArr, "birthday").getString("birthday");		//birthday
						double rr = CryptoStuff.getJSONObjectFromKey(userArr, "ratingR").getDouble("ratingR");		//runner rating
						double cr = CryptoStuff.getJSONObjectFromKey(userArr, "ratingC").getDouble("ratingC");		//client rating
						int crr = CryptoStuff.getJSONObjectFromKey(userArr, "completeRunsR").getInt("completeRunsR");	//completed runs as runner
						int crc = CryptoStuff.getJSONObjectFromKey(userArr, "completeRunsC").getInt("completeRunsC");	//completed runs as client
						String un = CryptoStuff.getJSONObjectFromKey(userArr, "username").getString("username");	//user name
						String pa = CryptoStuff.getJSONObjectFromKey(userArr, "passHash").getString("passHash");	//password
						String v = CryptoStuff.getJSONObjectFromKey(userArr, "isVerified").getString("isVerified");		//verification
						user = new User(un,pa, pa, fn, ln, r, p, e, c, st, z, b);											//creates User
						user.initializeStats(uId, cr, rr, crr,crc);										//puts statistics into User
						user.setVerification(v);
						user.clearErrors();
						firsttime = 1;
					}
					if(!CryptoStuff.getJSONObjectFromKey(userArr, "rId").isNull("rId")){
						int rId = CryptoStuff.getJSONObjectFromKey(userArr, "rId").getInt("rId");
						String clientName = CryptoStuff.getJSONObjectFromKey(userArr, "clientName").getString("clientName");
						double rating = CryptoStuff.getJSONObjectFromKey(userArr, "rating").getDouble("rating");
						String review = CryptoStuff.getJSONObjectFromKey(userArr, "review").getString("review");
						
						Rating r = new Rating(rId, runnerId, clientName, rating, review);
						ratings.add(0,r);
						
					}else{
						break;
					}
				}
				user.setRatings(ratings);
			}else{	//message is instance of String, no rowset returned
				user = User.initializeErrorUser();
				user.addError("database", "Invalid userId");	
			}
		}else{ //database error
			JSONObject message = CryptoStuff.getJSONObjectFromKey(arr, "message");
			user = User.initializeErrorUser();
			user.addError("database", message.getString("message"));
		}
		return user;
	}
	
	public static void AppVerifyUser(User user) throws JSONException {
		
		JSONArray arr = new JSONArray();
		JSONObject obj3 = new JSONObject(); //run sql object
		obj3.put("action", "run_sql");
		arr.put(obj3);
		
		obj3 = new JSONObject();
		obj3.put("session_id", sessionID); //puts session id from object
		arr.put(obj3);

		obj3 = new JSONObject();
		obj3.put("query" , "UPDATE Users "
				+ "SET isVerified = 'Yes' "
				+ "WHERE userId = :userId");
		arr.put(obj3);
		
		JSONObject u = new JSONObject(); //user id arg
		u.put(":userId", user.getUserID());
		
		JSONArray args = new JSONArray();    //args object
		args.put(u);
		
		obj3 = new JSONObject();
		obj3.put("args", args); //args
		arr.put(obj3);
		
		String s = CryptoStuff.addCheckSum(arr, sessionSalt); //adds checksum w/ session salt
		//System.out.println("\nSQL request being sent to the web service:");
		//System.out.println(arr.toString());
				
		s = ws.sendRequest(HTTPMethod.GET, arr.toString()); //request sent
		arr = new JSONArray(s);
		//System.out.println("\nWeb service response to SQL request:");
		//System.out.println(arr.toString());
		
		JSONObject result = CryptoStuff.getJSONObjectFromKey(arr, "result"); //gets result object from array
		
		if(!result.getString("result").equals("success")){					 //if result is successful

			JSONObject message = CryptoStuff.getJSONObjectFromKey(arr, "message");
			String error = message.getString("message");
			user.addError("database", error);
			
		}else{
			user.setVerification("Yes");
		}
	}
	
	
	public static String generateVString() {
		char[] chars1 = "ABCDEF012GHIJKL345MNOPQR678STUVWXYZ9".toCharArray();
	    StringBuilder sb1 = new StringBuilder();
	    Random random1 = new Random();
	    for (int i = 0; i < 5; i++){
	    	char c1 = chars1[random1.nextInt(chars1.length)];
	        sb1.append(c1);
	    }
	    return sb1.toString();
	}
	
	public static WSStuff getWS(){
		return ws;
	}
	
	public static long getSessionId(){
		return sessionID;
	}
	
	public static String getSessionSalt(){
		return sessionSalt;
	}

}
