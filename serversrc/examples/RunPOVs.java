package examples;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;

import models.Rating;
import models.Run;
import models.User;
import db.RatingFunctions;
import db.RunFunctions;
import db.UserFunctions;

public class RunPOVs {

	public static void main(String[] args) {

		Scanner in = new Scanner(System.in); // to receive input from user
		InputStream input = null;
		User user = null; // initialize User
		HashMap<String, String> map = new HashMap<String, String>();// HashMap
																	// for
																	// errors

		do {

			System.out.println("Login");
			System.out.println("Username: ");
			String username = in.nextLine(); // user enters user name
			System.out.println("Password: ");
			String password = in.nextLine(); // user enters password

			System.out.println("\nProcessing...");
			user = UserFunctions.AppLogin(username, password, input); // Login attempted 
			map = user.getErrors(); // put any errors into map
			if (map.get("database") != null) { // if database error occurred,
											   // e.g. bad user name or password
				System.out.println(map.get("database") + "\n"); // print out error
			}

		} while (map.size() > 0); // loop will go until loginAttempts is exceeded or
									// no errors are returned from database

		// successful login
		System.out.println("Login Successful");
		System.out.println(user.printUser());

		System.out.println("\nRetrieving Runs...");

		ArrayList<Run> qRuns = null; // queued runs
		ArrayList<Run> aRuns = null; // accepted runs
		ArrayList<Run> cRuns = null; // complete runs
		ArrayList<Run> pRuns = null; // pending runs

		if (user.getRole().equalsIgnoreCase("client")) {
			RunFunctions.ClientPOV(user.getUserID()); // gets runs associated with client
		} else if (user.getRole().equalsIgnoreCase("runner")) {
			RunFunctions.RunnerPOV(user.getUserID()); // gets runs associated runner
		} else if (user.getRole().equalsIgnoreCase("both")) {
			RunFunctions.BothPOV(user.getUserID()); // gets runs associated with both client and runner
		} else if (user.getRole().equalsIgnoreCase("admin")) {
			RunFunctions.AdminPOV(user.getUserID()); // gets all runs
		}

		qRuns = RunFunctions.getqRuns(); // fill queued runs
		aRuns = RunFunctions.getaRuns(); // fill accepted runs
		cRuns = RunFunctions.getcRuns(); // fill completed runs
		pRuns = RunFunctions.getpRuns(); // fill completed runs

		printRuns(qRuns, pRuns, aRuns, cRuns);

		if (user.getRole().equalsIgnoreCase("client")) {
			String cont = null;
			do {
				System.out.println("\nWhat would you like to do (post/view/rate)? ");
				String c = in.nextLine();
				if (c.equalsIgnoreCase("post")) {
					Run run = null;
					do {
						System.out.println("\nEnter Run Info");
						System.out.println("Title: ");
						String title = in.nextLine(); // user enters title
						System.out.println("Description: ");
						String description = in.nextLine(); // user description
						System.out.println("RunType: ");
						String runType = in.nextLine(); // user description
						System.out.println("Amount: ");
						String amount = in.nextLine(); // user enters amount
						System.out.println("Address: ");
						String address = in.nextLine(); // user enters address
						System.out.println("City: ");
						String city = in.nextLine(); // user enters city
						System.out.println("State: ");
						String state = in.nextLine(); // user enters state
						System.out.println("Zip Code: ");
						String zipcode = in.nextLine(); // user enters zip code

						run = new Run(user.getUserID(), title, description, runType, amount, address, city, state, zipcode);

						map = run.getErrors(); // put any errors into map

						if (map.size() > 0) { // if there are errors in Run model
							System.out.println("\nInvalid Run:");

							if (map.get("title") != null) {
								System.out.println(map.get("title")); // print out error
							}

							if (map.get("description") != null) {
								System.out.println(map.get("description")); // print error
							}

							if (map.get("amount") != null) {
								System.out.println(map.get("amount")); // print error
							}

							if (map.get("address") != null) {
								System.out.println(map.get("address")); // print out error
							}

							if (map.get("city") != null) { // if city error occurred
								System.out.println(map.get("city")); // print out error
							}

							if (map.get("state") != null) { // if state error
															// occurred
								System.out.println(map.get("state")); // print out error
							}

							if (map.get("zipcode") != null) { // if zipcode error occurred
								System.out.println(map.get("zipcode")); // print out error
							}

						} else { // valid Run object created
							System.out.println("\nRun Creation Successful!");
							System.out.println("\nAdding to database...");
							RunFunctions.AppAddRun(run); // attempt to add to database
							if (run.getRunId() != 0) { // if adding was successful
								System.out.println("Adding Successful!");
								System.out.println("Run Id from database: " + run.getRunId());
								qRuns.add(0, run); // add to queued runs
							} else { // if unsuccessful
								System.out.println("Adding Unsuccessful!");
								System.out.println(user.getErrors().get("database"));
								System.out.println();
							}
						}
					} while (map.size() > 0);

					printRuns(qRuns, pRuns, aRuns, cRuns);
				} else if (c.equalsIgnoreCase("view")) {
					System.out.println("Which run would you like to see? Enter Run Id:");
					int id = in.nextInt();
					in.nextLine();
					Run r = Run.getRun(id, pRuns);
					if (r != null) {

						System.out.println("Run Id = " + r.getRunId()
								+ "\nRunner Id = " + r.getRnerId()
								+ "\nTitle = " + r.getTitle()
								+ "\nDescription =  " + r.getDescription()
								+ "\nAmount = " + r.getAmount()
								+ "\nTime Posted = " + r.getTimePosted());
						System.out.println("-------------------------------");

						System.out.println("Would like to see the runner's ratings? (yes/no)");
						String ss = in.nextLine();
						if (ss.equalsIgnoreCase("Yes")) {
							User runner = UserFunctions.getRunner(r.getRnerId());
							System.out.println("Email: " + runner.getEmail()
									+ "\nPhone: " + runner.getPhone()
									+ "\nCompleted Runs: "
									+ runner.getComRunsRunner()
									+ " Average Rating: "
									+ runner.getRunnerRating() + "\n");
							for (Rating rating : runner.getRatings()) {
								System.out.println("Reviewer: "+ rating.getPostedName() + "| Rating: "
										+ rating.getRating() + "\nReview: "+ rating.getReview());
							}
						}

						System.out.println("\nWhat would you like to do? (refuse/accept):");
						String str = in.nextLine();

						if (str.equals("refuse")) {
							r.refused(pRuns, qRuns);
							RunFunctions.AppUpdateRun(r);
							System.out.println("Run Refused");
						} else if (str.equals("accept")) {
							r.accepted(pRuns, aRuns);
							RunFunctions.AppUpdateRun(r);
							System.out.println("Run Accepted!");
						}

						printRuns(qRuns, pRuns, aRuns, cRuns);
					}

				} else if (c.equalsIgnoreCase("rate")) {
					System.out.println("Which run would you like to rate? Enter Run Id:");
					int id = in.nextInt();
					in.nextLine();
					Run r = Run.getRun(id, cRuns);
					if (r != null && r.getCReviewed() == 0) {
						Rating rating = null;

						do {
							System.out.println("\nPlease enter information for review");
							System.out.println("Rating: ");
							Double ra = in.nextDouble(); // user enters rating
							in.nextLine();
							System.out.println("Review: ");
							String review = in.nextLine(); // user enters review

							rating = new Rating(r.getRunId(), r.getRnerId(),
									user.getUserName(), ra, review);

							map = rating.getErrors();

							if (map.size() > 0) { // if there are errors in Run
													// model

								System.out.println("\nInvalid Rating:");

								if (map.get("rating") != null) {
									System.out.println(map.get("rating")); // print out error
								}

								if (map.get("review") != null) {
									System.out.println(map.get("review")); // print out error
								}
							} else { // valid Run object created
								System.out.println("\nRating Creation Successful!");
								System.out.println("\nAdding to database...");
								RatingFunctions.AppAddRunnerRating(rating); // attempt to add to database
								if (rating.getErrors().get("database") == null) { // if adding was successful
									System.out.println("Adding Successful!");
									System.out.println(rating);
									r.setCReviewed(1);
								} else { // if unsuccessful
									System.out.println("Adding Unsuccessful!");
									System.out.println(rating.getErrors().get("database"));
									System.out.println();
								}
							}

						} while (map.size() > 0);
					} else if (r.getCReviewed() == 1) {
						System.out.println("Run has already been reviewed");
					}

				}

				System.out.println("Do something else? (yes/no)");
				cont = in.nextLine();

			} while (cont.equalsIgnoreCase("yes"));

		} else if (user.getRole().equalsIgnoreCase("runner")) {
			String cont = null;
			do {
				System.out
						.println("\nWould you like to do? (view/complete/rate) ");
				String c = in.nextLine();
				if (c.equalsIgnoreCase("view")) {
					System.out.println("Which run would you like to view? Enter Run Id:");
					int id = in.nextInt();
					Run r = Run.getRun(id, qRuns);
					if (r != null) {
						System.out.println("Would like to see the client's ratings? (yes/no)");
						in.nextLine();
						String ss = in.nextLine();
						if (ss.equalsIgnoreCase("Yes")) {
							User client = UserFunctions.getClient(r.getClntId());
							System.out.println("Email: " + client.getEmail()
									+ "\nPhone: " + client.getPhone()
									+ "\nCompleted Runs: "
									+ client.getComRunsClient()
									+ " Average Rating: "
									+ client.getClientRating() + "\n");
							for (Rating rating : client.getRatings()) {
								System.out.println("Reviewer: "+ rating.getPostedName() + "| Rating: "
										+ rating.getRating() + "\nReview: "+ rating.getReview());
							}
						}

						System.out.println("\nWould you like to accept the run? (yes/no) ");
						String cc = in.nextLine();

						if (cc.equalsIgnoreCase("yes")) {

							String ver = RunFunctions.getVersion(r);
							if (!ver.equals(r.getVersion())) {
								System.out.println("Run is out of date, please refresh");
							} else {
								r.pending(user.getUserID(), qRuns, pRuns);
								RunFunctions.AppUpdateRun(r);
								System.out.println("Run moved to Pending!");
							}
							printRuns(qRuns, pRuns, aRuns, cRuns);
						}

					}
				} else if (c.equalsIgnoreCase("complete")) {
					System.out.println("Which run would you like to complete? Enter Run Id:");
					int id = in.nextInt();
					Run r = Run.getRun(id, aRuns);
					if (r != null) {

						String ver = RunFunctions.getVersion(r);
						if (!ver.equals(r.getVersion())) {
							System.out.println("Run is out of date, please refresh");
						} else {
							r.completed(aRuns, cRuns);
							RunFunctions.AppUpdateRun(r);
							System.out.println("Run Completed!");
						}

						printRuns(qRuns, pRuns, aRuns, cRuns);
					}
				} else if (c.equalsIgnoreCase("rate")) {
					System.out.println("Which run would you like to rate? Enter Run Id:");
					int id = in.nextInt();
					in.nextLine();
					Run r = Run.getRun(id, cRuns);
					if (r != null && r.getRReviewed() == 0) {
						Rating rating = null;

						do {
							System.out.println("\nPlease enter information for review");
							System.out.println("Rating: ");
							Double ra = in.nextDouble(); // user enters rating
							in.nextLine();
							System.out.println("Review: ");
							String review = in.nextLine(); // user enters review

							rating = new Rating(r.getRunId(), r.getClntId(),user.getUserName(), ra, review);

							map = rating.getErrors();

							if (map.size() > 0) { // if there are errors in Run model

								System.out.println("\nInvalid Rating:");

								if (map.get("rating") != null) {
									System.out.println(map.get("rating")); // print out error
								}

								if (map.get("review") != null) {
									System.out.println(map.get("review")); // print out error
								}
							} else { // valid Run object created
								System.out.println("\nRating Creation Successful!");
								System.out.println("\nAdding to database...");
								RatingFunctions.AppAddClientRating(rating); // attempt to add to database
								if (rating.getErrors().get("database") == null) { // if adding was successful
									System.out.println("Adding Successful!");
									System.out.println(rating);
									r.setRReviewed(1);
								} else { // if unsuccessful
									System.out.println("Adding Unsuccessful!");
									System.out.println(rating.getErrors().get("database"));
									System.out.println();
								}
							}

						} while (map.size() > 0);
					} else if (r.getRReviewed() == 1) {
						System.out.println("Run has already been reviewed");
					}
				}
				System.out.println("Do something else? (yes/no)");
				cont = in.nextLine();
			} while (cont.equalsIgnoreCase("yes"));
		} else if (user.getRole().equalsIgnoreCase("both") || user.getRole().equalsIgnoreCase("admin")) {
			System.out.println("What would you like to be? (client/runner)");
			String c = in.nextLine();
			if (c.equalsIgnoreCase("client")) {
				System.out.println("End of the line");
			} else if (c.equalsIgnoreCase("runner")) {
				System.out.println("End of the line");
			}
		}

		in.close();

	}

	public static void printRuns(ArrayList<Run> queued, ArrayList<Run> pending, ArrayList<Run> accepted, ArrayList<Run> completed) {
		System.out.println("\nQueued Runs\n");
		for (Run run : queued) {
			System.out.println("Run Id = " + run.getRunId() + "\nTitle = "
					+ run.getTitle() + "\nDescription =  "
					+ run.getDescription() + "\nAmount = " + run.getAmount()
					+ "\nTime Posted = " + run.getTimePosted());
			System.out.println("-------------------------------");
		}

		System.out.println("\nPending Runs\n");
		for (Run run : pending) {
			System.out.println("Run Id = " + run.getRunId() + "\nRunner Id = "
					+ run.getRnerId() + "\nTitle = " + run.getTitle()
					+ "\nDescription =  " + run.getDescription()
					+ "\nAmount = " + run.getAmount() + "\nTime Posted = "
					+ run.getTimePosted());
			System.out.println("-------------------------------");
		}

		System.out.println("\nAccepted Runs\n");
		for (Run run : accepted) {
			System.out.println("Run Id = " + run.getRunId() + "\nRunner Id = "
					+ run.getRnerId() + "\nTitle = " + run.getTitle()
					+ "\nDescription =  " + run.getDescription()
					+ "\nAmount = " + run.getAmount() + "\nTime Posted = "
					+ run.getTimePosted() + "\nTime Accepted = "
					+ run.getTimeAccepted());
			System.out.println("-------------------------------");
		}

		System.out.println("\nCompleted Runs\n");
		for (Run run : completed) {
			System.out.println("Run Id = " + run.getRunId() + "\nRunner Id = "
					+ run.getRnerId() + "\nTitle = " + run.getTitle()
					+ "\nDescription =  " + run.getDescription()
					+ "\nAmount = " + run.getAmount() + "\nTime Posted = "
					+ run.getTimePosted() + "\nTime Accepted = "
					+ run.getTimeAccepted() + "\nTime Completed = "
					+ run.getTimeCompleted());
			System.out.println("-------------------------------");
		}
	}

}
