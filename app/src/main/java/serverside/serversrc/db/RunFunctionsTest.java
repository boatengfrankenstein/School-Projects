package serverside.serversrc.db;

import org.json.JSONException;

import java.io.InputStream;
import java.util.ArrayList;

import serverside.serversrc.models.Run;

public class RunFunctionsTest {

	public static void main(String[] args) throws JSONException {
		
		InputStream input = null;
		UserFunctions.WSLogin(input);
		
		/*Run run = new Run(1, "title", "description", "5", 
				"1 utsa cirle", "sa", "tx", "78249");
		
		System.out.println(run.toString());
		RunFunctions.AppAddRun(run);*/
		RunFunctions.BothPOV(2);
		ArrayList<Run> qRuns = RunFunctions.getqRuns();
		ArrayList<Run> aRuns = RunFunctions.getaRuns();
		ArrayList<Run> cRuns = RunFunctions.getcRuns();
		
		System.out.println(qRuns);
		System.out.println(aRuns);
		System.out.println(cRuns);
		
		/*qRuns.add(0,run);
		
		run.EditRun("title2", "desc2", "25", "34 address", "austin", "texas", "72111");
		//run.accepted(2, qRuns, aRuns);
		//run.completed(aRuns, cRuns);
		System.out.println(run.toString());
		RunFunctions.AppUpdateRun(run);*/

	}

}
