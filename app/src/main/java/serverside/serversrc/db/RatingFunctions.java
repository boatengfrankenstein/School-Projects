package serverside.serversrc.db;

import serverside.serversrc.madmarcos.CryptoStuff;
import serverside.serversrc.madmarcos.HTTPMethod;
import serverside.serversrc.madmarcos.WSStuff;
import serverside.serversrc.models.Rating;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class RatingFunctions {

    private static long sessionId;
    private static String sessionSalt = null;
    private static WSStuff ws = null;

    public static void AppAddClientRating(Rating newRating) throws JSONException {
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
        obj3.put("query" , "INSERT INTO ClientRatings(rId, clientId, runnerName, rating, review) "
                + "VALUES (:rId, :clientId, :runnerName, :rating, :review);");
        arr.put(obj3);

        JSONObject rId = new JSONObject(); //run id arg
        rId.put(":rId", newRating.getRunId());
        JSONObject clientId = new JSONObject(); //client id arg
        clientId.put(":clientId", newRating.getRatedId());
        JSONObject runnerName = new JSONObject(); //username arg
        runnerName.put(":runnerName", newRating.getPostedName());
        JSONObject rating = new JSONObject(); //amount arg
        rating.put(":rating", newRating.getRating());
        JSONObject review = new JSONObject(); //address arg
        review.put(":review", newRating.getReview());

        JSONArray args = new JSONArray();    //args object
        args.put(rId);
        args.put(clientId);
        args.put(runnerName);
        args.put(rating);
        args.put(review);

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
            //database error, user name or email taken or other database error
            JSONObject message = CryptoStuff.getJSONObjectFromKey(arr, "message");
            String error = message.getString("message");
            newRating.addError("database", error);

        }

    }

    public static void AppAddRunnerRating(Rating newRating) throws JSONException {
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
        obj3.put("query" , "INSERT INTO RunnerRatings(rId, runnerId, clientName, rating, review) "
                + "VALUES (:rId, :runnerId, :clientName, :rating, :review);");
        arr.put(obj3);

        JSONObject rId = new JSONObject(); //run id arg
        rId.put(":rId", newRating.getRunId());
        JSONObject runnerId = new JSONObject(); //runner id arg
        runnerId.put(":runnerId", newRating.getRatedId());
        JSONObject clientName = new JSONObject(); //username arg
        clientName.put(":clientName", newRating.getPostedName());
        JSONObject rating = new JSONObject(); //amount arg
        rating.put(":rating", newRating.getRating());
        JSONObject review = new JSONObject(); //address arg
        review.put(":review", newRating.getReview());

        JSONArray args = new JSONArray();    //args object
        args.put(rId);
        args.put(runnerId);
        args.put(clientName);
        args.put(rating);
        args.put(review);

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
            //database error, user name or email taken or other database error
            JSONObject message = CryptoStuff.getJSONObjectFromKey(arr, "message");
            String error = message.getString("message");
            newRating.addError("database", error);

        }

    }

}