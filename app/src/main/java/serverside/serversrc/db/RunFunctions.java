package serverside.serversrc.db;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.TimeZone;

import serverside.serversrc.madmarcos.CryptoStuff;
import serverside.serversrc.madmarcos.HTTPMethod;
import serverside.serversrc.madmarcos.WSStuff;
import serverside.serversrc.models.Run;

public class RunFunctions {
	
	private static long sessionId;
	private static String sessionSalt = null;
	private static WSStuff ws = null;
	
	private static SimpleDateFormat ft = new SimpleDateFormat ("yyyy.MM.dd HH:mm:ss");
	private static TimeZone timezone = TimeZone.getTimeZone("US/Central");
	
	private static ArrayList<Run> qRuns;
	private static ArrayList<Run> pRuns;
	private static ArrayList<Run> aRuns;
	private static ArrayList<Run> cRuns;
	
	/**
	 * add a run to the database
	 * 
	 * if errors, Run.getErrors() will not be empty
	 * @param newRun
	 */
	public static void AppAddRun(Run newRun) throws JSONException {
		sessionId = UserFunctions.getSessionId();
		sessionSalt = UserFunctions.getSessionSalt();
		ws = UserFunctions.getWS();
		
		JSONArray arr = new JSONArray();
		JSONObject obj3 = new JSONObject(); //run sql object
		obj3.put("action", "run_sql");
		arr.put(obj3);
		
		obj3 = new JSONObject();
		obj3.put("session_id", sessionId); //puts session id from object
		arr.put(obj3);

		obj3 = new JSONObject();
		obj3.put("query" , "INSERT INTO RunsFinal(clntId, title, description, runType, amount, cAddress, city, state, cZipCode, timePosted, status, version) "
				+ "VALUES (:clientId, :title, :description, :runType, :amount, :cAddress, :city, :state, :cZipCode, :timePosted, :status, :version);");
		arr.put(obj3);
		
		ft.setTimeZone(timezone);
		String timePosted = ft.format(newRun.getTimePosted());
		String version = ft.format(new Date());
		
		JSONObject cid = new JSONObject(); //client id arg
		cid.put(":clientId", newRun.getClntId());
		JSONObject title = new JSONObject(); //title arg
		title.put(":title", newRun.getTitle());
		JSONObject des = new JSONObject(); //description arg
		des.put(":description", newRun.getDescription());
		JSONObject runType = new JSONObject(); //runtype arg
		runType.put(":runType", newRun.getRunType());
		JSONObject am = new JSONObject(); //amount arg
		am.put(":amount", newRun.getAmount());
		JSONObject ad = new JSONObject(); //address arg
		ad.put(":cAddress", newRun.getcAddress());
		JSONObject c = new JSONObject(); //city arg
		c.put(":city", newRun.getCity());
		JSONObject state = new JSONObject(); //state arg
		state.put(":state", newRun.getState());
		JSONObject z = new JSONObject(); //zipcode arg
		z.put(":cZipCode", newRun.getcZipCode());
		JSONObject tp = new JSONObject(); //time posted arg
		tp.put(":timePosted", timePosted);
		JSONObject st = new JSONObject(); //status arg
		st.put(":status", newRun.getStatus());
		JSONObject v = new JSONObject();
		v.put(":version", version);
		JSONArray args = new JSONArray();    //args object
		args.put(cid);
		args.put(title);
		args.put(des);
		args.put(runType);
		args.put(am);
		args.put(ad);
		args.put(c);
		args.put(state);
		args.put(z);
		args.put(tp);
		args.put(st);
		args.put(v);
		
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
			String rId = m.getString("insert_id");			 //puts inserted id into string
			newRun.setRunId(Integer.parseInt(rId));
			newRun.setVersion(version);

		}else{ //database error, user name or email taken or other database error
			JSONObject message = CryptoStuff.getJSONObjectFromKey(arr, "message");
			String error = message.getString("message");
			newRun.addError("database", error);
			
		}

	}
	
	/**
	 * Single query UpdateRun method, method takes a Run object as parameter.
	 * If successful, Run is updated
	 * 
	 * If error, Run.getErrors() will not be empty
	 * @param user
	 */
	public static void AppUpdateRun(Run run) throws JSONException {
		sessionId = UserFunctions.getSessionId();
		sessionSalt = UserFunctions.getSessionSalt();
		ws = UserFunctions.getWS();
		
		JSONArray arr = new JSONArray();
		JSONObject obj3 = new JSONObject(); //run sql object
		obj3.put("action", "run_sql");
		arr.put(obj3);
		
		obj3 = new JSONObject();
		obj3.put("session_id", sessionId); //puts session id from object
		arr.put(obj3);

		obj3 = new JSONObject();
		if(run.getRnerId()==0){
			obj3.put("query" , "UPDATE RunsFinal "
				+ "SET title = :title, description = :description, amount = :amount, cAddress = :address, city = :city, state = :state, cZipCode = :zipCode, "
				+ "status = :status, timeAccepted = :ta, timeCompleted = :tc, timesFlagged = :tf, version = :newversion "
				+ "WHERE runId = :runId AND version = :version");
		}else if(run.getRnerId()==-1){
			obj3.put("query" , "UPDATE RunsFinal "
					+ "SET title = :title, description = :description, amount = :amount, cAddress = :address, city = :city, state = :state, cZipCode = :zipCode, "
					+ "status = :status, timeAccepted = :ta, timeCompleted = :tc, rnerId = NULL, timesFlagged = :tf, version = :newversion "
					+ "WHERE runId = :runId AND version = :version");
		}else{
			obj3.put("query" , "UPDATE RunsFinal "
				+ "SET title = :title, description = :description, amount = :amount, cAddress = :address, city = :city, state = :state, cZipCode = :zipCode, "
				+ "status = :status, timeAccepted = :ta, timeCompleted = :tc, rnerId = :runnerId, timesFlagged = :tf, version = :newversion "
				+ "WHERE runId = :runId AND version = :version");
		}
		arr.put(obj3);
		
		ft.setTimeZone(timezone);
		String timeAccepted = null;
		String timeCompleted = null;
		if(run.getTimeAccepted() != null){
			timeAccepted = ft.format(run.getTimeAccepted());
		}else{
			timeAccepted = "";
		}
		if(run.getTimeCompleted() != null){
			timeCompleted = ft.format(run.getTimeCompleted());
		}else{
			timeCompleted = "";
		}
		
		String newVersion = ft.format(new Date());
		
		JSONObject title = new JSONObject(); //title arg
		title.put(":title", run.getTitle());
		JSONObject des = new JSONObject(); //description arg
		des.put(":description", run.getDescription());
		JSONObject am = new JSONObject(); //amount arg
		am.put(":amount", run.getAmount());
		JSONObject ad = new JSONObject(); //address arg
		ad.put(":address", run.getcAddress());
		JSONObject c = new JSONObject(); //city arg
		c.put(":city", run.getCity());
		JSONObject st = new JSONObject(); //state arg
		st.put(":state", run.getState());
		JSONObject z = new JSONObject(); //zipcode arg
		z.put(":zipCode", run.getcZipCode());
		JSONObject stat = new JSONObject(); //status arg
		stat.put(":status", run.getStatus());
		JSONObject ta = new JSONObject(); //time accepted arg
		ta.put(":ta", timeAccepted);
		JSONObject tc = new JSONObject(); //time completedr arg
		tc.put(":tc", timeCompleted);
		JSONObject runnerId = new JSONObject(); //runner id arg
		runnerId.put(":runnerId", run.getRnerId());
		JSONObject tf = new JSONObject(); //times flagged arg
		tf.put(":tf", run.getTimesFlagged());
		JSONObject nv = new JSONObject();//new version arg
		nv.put(":newversion", newVersion);
		JSONObject r = new JSONObject(); //run id arg
		r.put(":runId", run.getRunId());
		JSONObject v = new JSONObject(); //original version arg
		v.put(":version", run.getVersion());
		
		JSONArray args = new JSONArray();    //args object
		args.put(title);
		args.put(des);
		args.put(am);
		args.put(ad);
		args.put(c);
		args.put(st);
		args.put(z);
		args.put(stat);
		args.put(ta);
		args.put(tc);
		if(run.getRnerId()!=0 && run.getRnerId()!=-1) args.put(runnerId);
		args.put(tf);
		args.put(nv);
		args.put(r);
		args.put(v);
		
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
			
			run.addError("database", error);
			
		}else{
			run.setRnerId(0);
			run.setVersion(newVersion);
		}
	}
	
	/**
	 * get runs associated with a client
	 * @param clientId
	 */
	public static void ClientPOV(int clientId) throws JSONException {
		sessionId = UserFunctions.getSessionId();
		sessionSalt = UserFunctions.getSessionSalt();
		ws = UserFunctions.getWS();
		
		JSONArray arr = new JSONArray();
		JSONObject obj3 = new JSONObject(); //run sql object
		obj3.put("action", "run_sql");
		arr.put(obj3);
		
		obj3 = new JSONObject();
		obj3.put("session_id", sessionId); //puts session id from object
		arr.put(obj3);
		
		obj3 = new JSONObject();
		obj3.put("query" , "SELECT * FROM RunsFinal "
				+ "WHERE clntId = :clientId");
		arr.put(obj3);
		
		JSONObject clntId = new JSONObject(); //clientId arg
		clntId.put(":clientId", clientId);
		JSONArray args = new JSONArray();    //args object
		args.put(clntId);
		
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
		
		qRuns = new ArrayList<Run>();
		aRuns = new ArrayList<Run>();
		cRuns = new ArrayList<Run>();
		pRuns = new ArrayList<Run>();
		
		Run run = null;
		
		JSONObject result = CryptoStuff.getJSONObjectFromKey(arr, "result"); //gets result object from array
		if(result.getString("result").equals("success")){					 //if result is successful
			JSONObject message = CryptoStuff.getJSONObjectFromKey(arr, "message"); //get message object from array
			if(message.get("message") instanceof JSONArray){				 //checks to see if the message returned a JSONArray
				JSONArray rowSet = message.getJSONArray("message"); //get rowSet array from message Object
				for (int i = 0; i < rowSet.length();i++){
					JSONArray runArr = rowSet.getJSONArray(i); //get the first and only row in the RowSet array
			
					int runId = CryptoStuff.getJSONObjectFromKey(runArr, "runId").getInt("runId");			//run id
					String t = CryptoStuff.getJSONObjectFromKey(runArr, "title").getString("title");		//title
					String d = CryptoStuff.getJSONObjectFromKey(runArr, "description").getString("description");	//description
					String rt = CryptoStuff.getJSONObjectFromKey(runArr, "runType").getString("runType");	//runType
					double a = CryptoStuff.getJSONObjectFromKey(runArr, "amount").getDouble("amount");		//amount
					String ad = CryptoStuff.getJSONObjectFromKey(runArr, "cAddress").getString("cAddress");	//address
					String z = CryptoStuff.getJSONObjectFromKey(runArr, "cZipCode").getString("cZipCode");		//zip code
					String c = CryptoStuff.getJSONObjectFromKey(runArr, "city").getString("city");				//city
					String st = CryptoStuff.getJSONObjectFromKey(runArr, "state").getString("state");			//state
					String tp = CryptoStuff.getJSONObjectFromKey(runArr, "timePosted").getString("timePosted");	//time posted
					int rr = CryptoStuff.getJSONObjectFromKey(runArr, "rReviewed").getInt("rReviewed"); 		//runner reviewed
					int cr = CryptoStuff.getJSONObjectFromKey(runArr, "cReviewed").getInt("cReviewed"); 		//client reviewed
					int tf = CryptoStuff.getJSONObjectFromKey(runArr, "timesFlagged").getInt("timesFlagged");	//times flagged
					String status = CryptoStuff.getJSONObjectFromKey(runArr, "status").getString("status");		//status
					String version = CryptoStuff.getJSONObjectFromKey(runArr, "version").getString("version");   //version
					
					Date tpDate = null;
					try {
						tpDate = ft.parse(tp);
					} catch (ParseException e) {
						// never should happen
					}
					String amount = Double.toString(a);
					
					run = new Run(clientId,t,d,rt,amount,ad,c,st,z);
					run.setRunId(runId);
					run.setStatus(status);
					run.setTimePosted(tpDate);
					run.setTimesFlagged(tf);
					run.setCReviewed(cr);
					run.setRReviewed(rr);
					run.setVersion(version);
					
					if(status.equals("Queued")){
						
						qRuns.add(0,run);
						
					}else if(status.equals("Pending")){
						int runnerId = CryptoStuff.getJSONObjectFromKey(runArr, "rnerId").getInt("rnerId");	//runner Id
						run.setRnerId(runnerId);
						pRuns.add(0,run);
					}else if(status.equals("Accepted")||status.equals("Completed")){
						int runnerId = CryptoStuff.getJSONObjectFromKey(runArr, "rnerId").getInt("rnerId");	//runner Id
						String ta = CryptoStuff.getJSONObjectFromKey(runArr, "timeAccepted").getString("timeAccepted");	//time accepted
						Date taDate = null;
						try {
							taDate = ft.parse(ta);
						} catch (ParseException e) {
							// never should happen
						}
						run.setRnerId(runnerId);
						run.setTimeAccepted(taDate);
						if(status.equals("Accepted")){
							aRuns.add(0,run);
						}else if(status.equals("Completed")){
							String tc = CryptoStuff.getJSONObjectFromKey(runArr, "timeCompleted").getString("timeCompleted");	//time completed
							Date tcDate = null;
							try {
								tcDate = ft.parse(tc);
							} catch (ParseException e) {
								// never should happen
							}
							run.setTimeCompleted(tcDate);
							cRuns.add(0,run);
						}
						
					}
					
					run.clearErrors();
				}
			}else{	//message is instance of String, no rowset returned
				System.out.println("No runs in database");	
			}
		}else{ //database error
			JSONObject message = CryptoStuff.getJSONObjectFromKey(arr, "message");
			System.out.println(message.getString("message"));
		}
		
	}
	
	/**
	 * gets runs associated with a runner
	 * @param runnerId
	 */
	public static void RunnerPOV(int runnerId) throws JSONException {
		sessionId = UserFunctions.getSessionId();
		sessionSalt = UserFunctions.getSessionSalt();
		ws = UserFunctions.getWS();
		
		JSONArray arr = new JSONArray();
		JSONObject obj3 = new JSONObject(); //run sql object
		obj3.put("action", "run_sql");
		arr.put(obj3);
		
		obj3 = new JSONObject();
		obj3.put("session_id", sessionId); //puts session id from object
		arr.put(obj3);
		
		obj3 = new JSONObject();
		obj3.put("query" , "SELECT * FROM RunsFinal "
				+ "WHERE rnerId = :runnerId OR timeAccepted = ''");
		arr.put(obj3);
		
		JSONObject rnerId = new JSONObject(); //runnerId arg
		rnerId.put(":runnerId", runnerId);
		JSONArray args = new JSONArray();    //args object
		args.put(rnerId);
		
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
		
		qRuns = new ArrayList<Run>();
		aRuns = new ArrayList<Run>();
		cRuns = new ArrayList<Run>();
		pRuns = new ArrayList<Run>();
		
		Run run = null;
		
		JSONObject result = CryptoStuff.getJSONObjectFromKey(arr, "result"); //gets result object from array
		if(result.getString("result").equals("success")){					 //if result is successful
			JSONObject message = CryptoStuff.getJSONObjectFromKey(arr, "message"); //get message object from array
			if(message.get("message") instanceof JSONArray){				 //checks to see if the message returned a JSONArray
				JSONArray rowSet = message.getJSONArray("message"); //get rowSet array from message Object
				for (int i = 0; i < rowSet.length();i++){
					JSONArray runArr = rowSet.getJSONArray(i); //get the first and only row in the RowSet array
			
					int runId = CryptoStuff.getJSONObjectFromKey(runArr, "runId").getInt("runId");			//run id
					int clientId = CryptoStuff.getJSONObjectFromKey(runArr, "clntId").getInt("clntId");		//client id
					String t = CryptoStuff.getJSONObjectFromKey(runArr, "title").getString("title");		//title
					String d = CryptoStuff.getJSONObjectFromKey(runArr, "description").getString("description");	//description
					String rt = CryptoStuff.getJSONObjectFromKey(runArr, "runType").getString("runType");	//runType
					double a = CryptoStuff.getJSONObjectFromKey(runArr, "amount").getDouble("amount");		//amount
					String ad = CryptoStuff.getJSONObjectFromKey(runArr, "cAddress").getString("cAddress");	//address
					String z = CryptoStuff.getJSONObjectFromKey(runArr, "cZipCode").getString("cZipCode");		//zip code
					String c = CryptoStuff.getJSONObjectFromKey(runArr, "city").getString("city");				//city
					String st = CryptoStuff.getJSONObjectFromKey(runArr, "state").getString("state");			//state
					String tp = CryptoStuff.getJSONObjectFromKey(runArr, "timePosted").getString("timePosted");	//time posted
					int tf = CryptoStuff.getJSONObjectFromKey(runArr, "timesFlagged").getInt("timesFlagged");	//times flagged
					int rr = CryptoStuff.getJSONObjectFromKey(runArr, "rReviewed").getInt("rReviewed"); 		//runner reviewed
					int cr = CryptoStuff.getJSONObjectFromKey(runArr, "cReviewed").getInt("cReviewed"); 		//client reviewed
					String status = CryptoStuff.getJSONObjectFromKey(runArr, "status").getString("status");		//status
					String version = CryptoStuff.getJSONObjectFromKey(runArr, "version").getString("version");   //version
					
					Date tpDate = null;
					try {
						tpDate = ft.parse(tp);
					} catch (ParseException e) {
						// never should happen
					}
					String amount = Double.toString(a);
					
					if(runnerId == clientId){
						continue;
					}
					
					run = new Run(clientId,t,d,rt,amount,ad,c,st,z);
					run.setRunId(runId);
					run.setStatus(status);
					run.setTimePosted(tpDate);
					run.setTimesFlagged(tf);
					run.setCReviewed(cr);
					run.setRReviewed(rr);
					run.setVersion(version);
					
					if(status.equals("Queued")){
						
						qRuns.add(0,run);
						
					}else if(status.equals("Pending")){
						int rnnerId = CryptoStuff.getJSONObjectFromKey(runArr, "rnerId").getInt("rnerId");		//runner id
						if(rnnerId==runnerId){
							run.setRnerId(runnerId);
							pRuns.add(0,run);
						}
					}else if(status.equals("Accepted")||status.equals("Completed")){
						String ta = CryptoStuff.getJSONObjectFromKey(runArr, "timeAccepted").getString("timeAccepted");	//time accepted
						Date taDate = null;
						try {
							taDate = ft.parse(ta);
						} catch (ParseException e) {
							// never should happen
						}
						run.setRnerId(runnerId);
						run.setTimeAccepted(taDate);
						if(status.equals("Accepted")){
							aRuns.add(0,run);
						}else if(status.equals("Completed")){
							String tc = CryptoStuff.getJSONObjectFromKey(runArr, "timeCompleted").getString("timeCompleted");	//time completed
							Date tcDate = null;
							try {
								tcDate = ft.parse(tc);
							} catch (ParseException e) {
								// never should happen
							}
							run.setTimeCompleted(tcDate);
							cRuns.add(0,run);
						}
						
					}
					
					run.clearErrors();
				}
			}else{	//message is instance of String, no rowset returned
				System.out.println("No runs in database");	
			}
		}else{ //database error
			JSONObject message = CryptoStuff.getJSONObjectFromKey(arr, "message");
			System.out.println(message.getString("message"));
		}
		
	}
	
	/**
	 * gets run associated with a client and a runner
	 * @param userId
	 */
	public static void BothPOV(int userId) throws JSONException {
		sessionId = UserFunctions.getSessionId();
		sessionSalt = UserFunctions.getSessionSalt();
		ws = UserFunctions.getWS();
		
		JSONArray arr = new JSONArray();
		JSONObject obj3 = new JSONObject(); //run sql object
		obj3.put("action", "run_sql");
		arr.put(obj3);
		
		obj3 = new JSONObject();
		obj3.put("session_id", sessionId); //puts session id from object
		arr.put(obj3);
		
		obj3 = new JSONObject();
		obj3.put("query" , "SELECT * FROM RunsFinal "
				+ "WHERE rnerId = :runnerId OR timeAccepted = '' OR clntId = :clientId");
		arr.put(obj3);
		
		JSONObject rnerId = new JSONObject(); //runnerId arg
		rnerId.put(":runnerId", userId);
		JSONObject clntId = new JSONObject(); //runnerId arg
		clntId.put(":clientId", userId);
		JSONArray args = new JSONArray();    //args object
		args.put(rnerId);
		args.put(clntId);
		
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
		
		qRuns = new ArrayList<Run>();
		aRuns = new ArrayList<Run>();
		cRuns = new ArrayList<Run>();
		pRuns = new ArrayList<Run>();
		
		Run run = null;
		
		JSONObject result = CryptoStuff.getJSONObjectFromKey(arr, "result"); //gets result object from array
		if(result.getString("result").equals("success")){					 //if result is successful
			JSONObject message = CryptoStuff.getJSONObjectFromKey(arr, "message"); //get message object from array
			if(message.get("message") instanceof JSONArray){				 //checks to see if the message returned a JSONArray
				JSONArray rowSet = message.getJSONArray("message"); //get rowSet array from message Object
				for (int i = 0; i < rowSet.length();i++){
					JSONArray runArr = rowSet.getJSONArray(i); //get the first and only row in the RowSet array
			
					int runId = CryptoStuff.getJSONObjectFromKey(runArr, "runId").getInt("runId");			//run id
					int clientId = CryptoStuff.getJSONObjectFromKey(runArr, "clntId").getInt("clntId");		//client id
					String t = CryptoStuff.getJSONObjectFromKey(runArr, "title").getString("title");		//title
					String d = CryptoStuff.getJSONObjectFromKey(runArr, "description").getString("description");	//description
					String rt = CryptoStuff.getJSONObjectFromKey(runArr, "runType").getString("runType");	//runType
					double a = CryptoStuff.getJSONObjectFromKey(runArr, "amount").getDouble("amount");		//amount
					String ad = CryptoStuff.getJSONObjectFromKey(runArr, "cAddress").getString("cAddress");	//address
					String z = CryptoStuff.getJSONObjectFromKey(runArr, "cZipCode").getString("cZipCode");		//zip code
					String c = CryptoStuff.getJSONObjectFromKey(runArr, "city").getString("city");				//city
					String st = CryptoStuff.getJSONObjectFromKey(runArr, "state").getString("state");			//state
					String tp = CryptoStuff.getJSONObjectFromKey(runArr, "timePosted").getString("timePosted");	//time posted
					int tf = CryptoStuff.getJSONObjectFromKey(runArr, "timesFlagged").getInt("timesFlagged");	//times flagged
					int rr = CryptoStuff.getJSONObjectFromKey(runArr, "rReviewed").getInt("rReviewed"); 		//runner reviewed
					int cr = CryptoStuff.getJSONObjectFromKey(runArr, "cReviewed").getInt("cReviewed"); 		//client reviewed
					String status = CryptoStuff.getJSONObjectFromKey(runArr, "status").getString("status");		//status
					String version = CryptoStuff.getJSONObjectFromKey(runArr, "version").getString("version");   //version
					
					Date tpDate = null;
					try {
						tpDate = ft.parse(tp);
					} catch (ParseException e) {
						// never should happen
					}
					String amount = Double.toString(a);
					
					run = new Run(clientId,t,d,rt,amount,ad,c,st,z);
					run.setRunId(runId);
					run.setStatus(status);
					run.setTimePosted(tpDate);
					run.setTimesFlagged(tf);
					run.setCReviewed(cr);
					run.setRReviewed(rr);
					run.setVersion(version);
					
					if(status.equals("Queued")){
						
						qRuns.add(0, run);
						
					}else if(status.equals("Pending")){
						int runnerId = CryptoStuff.getJSONObjectFromKey(runArr, "rnerId").getInt("rnerId");	//runner Id
						run.setRnerId(runnerId);
						pRuns.add(0,run);
					}else if(status.equals("Accepted")||status.equals("Completed")){
						int runnerId = CryptoStuff.getJSONObjectFromKey(runArr, "rnerId").getInt("rnerId");	//runner Id
						String ta = CryptoStuff.getJSONObjectFromKey(runArr, "timeAccepted").getString("timeAccepted");	//time accepted
						Date taDate = null;
						try {
							taDate = ft.parse(ta);
						} catch (ParseException e) {
							// never should happen
						}
						run.setRnerId(runnerId);
						run.setTimeAccepted(taDate);
						if(status.equals("Accepted")){
							aRuns.add(0, run);
						}else if(status.equals("Completed")){
							String tc = CryptoStuff.getJSONObjectFromKey(runArr, "timeCompleted").getString("timeCompleted");	//time completed
							Date tcDate = null;
							try {
								tcDate = ft.parse(tc);
							} catch (ParseException e) {
								// never should happen
							}
							run.setTimeCompleted(tcDate);
							cRuns.add(0, run);
						}
						
					}
					
					run.clearErrors();
				}
			}else{	//message is instance of String, no rowset returned
				System.out.println("No runs in database");	
			}
		}else{ //database error
			JSONObject message = CryptoStuff.getJSONObjectFromKey(arr, "message");
			System.out.println(message.getString("message"));
		}
		
	}
	
	public static void AdminPOV(int userId) throws JSONException {
		sessionId = UserFunctions.getSessionId();
		sessionSalt = UserFunctions.getSessionSalt();
		ws = UserFunctions.getWS();
		
		JSONArray arr = new JSONArray();
		JSONObject obj3 = new JSONObject(); //run sql object
		obj3.put("action", "run_sql");
		arr.put(obj3);
		
		obj3 = new JSONObject();
		obj3.put("session_id", sessionId); //puts session id from object
		arr.put(obj3);
		
		obj3 = new JSONObject();
		obj3.put("query" , "SELECT * FROM RunsFinal ");
		arr.put(obj3);
		
		String s = CryptoStuff.addCheckSum(arr, sessionSalt); //adds checksum w/ session salt
		//System.out.println("\nSQL request being sent to the web service:");
		//System.out.println(arr.toString());
		
		s = ws.sendRequest(HTTPMethod.GET, arr.toString()); //request sent
		arr = new JSONArray(s);
		//System.out.println("\nWeb service response to SQL request:");
		//System.out.println(arr.toString());
		
		qRuns = new ArrayList<Run>();
		aRuns = new ArrayList<Run>();
		cRuns = new ArrayList<Run>();
		pRuns = new ArrayList<Run>();
		
		Run run = null;
		
		JSONObject result = CryptoStuff.getJSONObjectFromKey(arr, "result"); //gets result object from array
		if(result.getString("result").equals("success")){					 //if result is successful
			JSONObject message = CryptoStuff.getJSONObjectFromKey(arr, "message"); //get message object from array
			if(message.get("message") instanceof JSONArray){				 //checks to see if the message returned a JSONArray
				JSONArray rowSet = message.getJSONArray("message"); //get rowSet array from message Object
				for (int i = 0; i < rowSet.length();i++){
					JSONArray runArr = rowSet.getJSONArray(i); //get the first and only row in the RowSet array
			
					int runId = CryptoStuff.getJSONObjectFromKey(runArr, "runId").getInt("runId");			//run id
					int clientId = CryptoStuff.getJSONObjectFromKey(runArr, "clntId").getInt("clntId");		//client id
					String t = CryptoStuff.getJSONObjectFromKey(runArr, "title").getString("title");		//title
					String d = CryptoStuff.getJSONObjectFromKey(runArr, "description").getString("description");	//description
					String rt = CryptoStuff.getJSONObjectFromKey(runArr, "runType").getString("runType");	//runType
					double a = CryptoStuff.getJSONObjectFromKey(runArr, "amount").getDouble("amount");		//amount
					String ad = CryptoStuff.getJSONObjectFromKey(runArr, "cAddress").getString("cAddress");	//address
					String z = CryptoStuff.getJSONObjectFromKey(runArr, "cZipCode").getString("cZipCode");		//zip code
					String c = CryptoStuff.getJSONObjectFromKey(runArr, "city").getString("city");				//city
					String st = CryptoStuff.getJSONObjectFromKey(runArr, "state").getString("state");			//state
					String tp = CryptoStuff.getJSONObjectFromKey(runArr, "timePosted").getString("timePosted");	//time posted
					int tf = CryptoStuff.getJSONObjectFromKey(runArr, "timesFlagged").getInt("timesFlagged");	//times flagged
					int rr = CryptoStuff.getJSONObjectFromKey(runArr, "rReviewed").getInt("rReviewed"); 		//runner reviewed
					int cr = CryptoStuff.getJSONObjectFromKey(runArr, "cReviewed").getInt("cReviewed"); 		//client reviewed
					String status = CryptoStuff.getJSONObjectFromKey(runArr, "status").getString("status");		//status
					String version = CryptoStuff.getJSONObjectFromKey(runArr, "version").getString("version");   //version
					
					Date tpDate = null;
					try {
						tpDate = ft.parse(tp);
					} catch (ParseException e) {
						// never should happen
					}
					String amount = Double.toString(a);
					
					run = new Run(clientId,t,d,rt,amount,ad,c,st,z);
					run.setRunId(runId);
					run.setStatus(status);
					run.setTimePosted(tpDate);
					run.setTimesFlagged(tf);
					run.setCReviewed(cr);
					run.setRReviewed(rr);
					run.setVersion(version);
					
					if(status.equals("Queued")){
						
						qRuns.add(0, run);
						
					}else if(status.equals("Pending")){
						int runnerId = CryptoStuff.getJSONObjectFromKey(runArr, "rnerId").getInt("rnerId");	//runner Id
						run.setRnerId(runnerId);
						pRuns.add(0,run);
					}else if(status.equals("Accepted")||status.equals("Completed")){
						int runnerId = CryptoStuff.getJSONObjectFromKey(runArr, "rnerId").getInt("rnerId");	//runner Id
						String ta = CryptoStuff.getJSONObjectFromKey(runArr, "timeAccepted").getString("timeAccepted");	//time accepted
						Date taDate = null;
						try {
							taDate = ft.parse(ta);
						} catch (ParseException e) {
							// never should happen
						}
						run.setRnerId(runnerId);
						run.setTimeAccepted(taDate);
						if(status.equals("Accepted")){
							aRuns.add(0, run);
						}else if(status.equals("Completed")){
							String tc = CryptoStuff.getJSONObjectFromKey(runArr, "timeCompleted").getString("timeCompleted");	//time completed
							Date tcDate = null;
							try {
								tcDate = ft.parse(tc);
							} catch (ParseException e) {
								// never should happen
							}
							run.setTimeCompleted(tcDate);
							cRuns.add(0, run);
						}
						
					}
					
					run.clearErrors();
				}
			}else{	//message is instance of String, no rowset returned
				System.out.println("No runs in database");	
			}
		}else{ //database error
			JSONObject message = CryptoStuff.getJSONObjectFromKey(arr, "message");
			System.out.println(message.getString("message"));
		}
		
	}
	
	public static String getVersion(Run run) throws JSONException {
		sessionId = UserFunctions.getSessionId();
		sessionSalt = UserFunctions.getSessionSalt();
		ws = UserFunctions.getWS();
		
		String version = null;
		String status = null;
		
		JSONArray arr = new JSONArray();
		JSONObject obj3 = new JSONObject(); //run sql object
		obj3.put("action", "run_sql");
		arr.put(obj3);
		
		obj3 = new JSONObject();
		obj3.put("session_id", sessionId); //puts session id from object
		arr.put(obj3);
		
		obj3 = new JSONObject();
		obj3.put("query" , "SELECT * FROM RunsFinal "
				+ "WHERE runId = :runId");
		arr.put(obj3);
		
		JSONObject rId = new JSONObject(); //runnerId arg
		rId.put(":runId", run.getRunId());
		JSONArray args = new JSONArray();    //args object
		args.put(rId);
		
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
			if(message.get("message") instanceof JSONArray){				 //checks to see if the message returned a JSONArray
				JSONArray rowSet = message.getJSONArray("message"); //get rowSet array from message Object
				JSONArray rowArr = rowSet.getJSONArray(0); //get the first and only row in the RowSet array
				
				version = CryptoStuff.getJSONObjectFromKey(rowArr, "version").getString("version");	//version
				status = CryptoStuff.getJSONObjectFromKey(rowArr, "status").getString("status");	//version
			}
		}
		
		if(status.equalsIgnoreCase("Voided")){
			return null;
		}else{
			return version;
		}
	}

	/**
	 * get the list of queued runs, a POV function must be called first
	 * @return
	 */
	public static ArrayList<Run> getqRuns() {
		return qRuns;
	}

	/**
	 * get the list of accepted runs, a POV function must be called first
	 * @return
	 */
	public static ArrayList<Run> getaRuns() {
		return aRuns;
	}

	/**
	 * get the list of completed runs, a POV function must be called first
	 * @return
	 */
	public static ArrayList<Run> getcRuns() {
		return cRuns;
	}
	
	/**
	 * get the list of pending runs, a POV function must be called first
	 * @return
	 */
	public static ArrayList<Run> getpRuns() {
		return pRuns;
	}

}
