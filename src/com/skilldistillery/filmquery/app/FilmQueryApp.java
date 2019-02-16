package com.skilldistillery.filmquery.app;

import java.util.List;
import java.util.Scanner;

import com.skilldistillery.filmquery.database.DatabaseAccessor;
import com.skilldistillery.filmquery.database.DatabaseAccessorObject;
import com.skilldistillery.filmquery.entities.Actor;
import com.skilldistillery.filmquery.entities.Film;

public class FilmQueryApp {

	DatabaseAccessor db = new DatabaseAccessorObject();

	public static void main(String[] args) {
		FilmQueryApp app = new FilmQueryApp();
//    app.test();
		app.launch();
	}

	private void test() {
		Film film = db.findFilmById(1);
		System.out.println(film);
		Actor actor = db.findActorById(1);
		System.out.println(actor);
		List<Actor> actorList = db.findActorsByFilmId(1);
		System.out.println(actorList);
		System.out.println(film.getCast());
	}

	private void launch() {
		Scanner kb = new Scanner(System.in);

		startUserInterface(kb);

		kb.close();
	}

	private void startUserInterface(Scanner kb) {
		int input;
		do {
			System.out.println();
			System.out.println("1.) Look up a film by its ID");
			System.out.println("2.) Look up a film by a search keyword");
			System.out.println("3.) Exit Application");
			System.out.print(">> ");
			input = getIntFromUser(kb);

			if (input < 1 || input > 3) {
				System.out.println();
				System.out.println("\t!!!*** You must enter a number between 1 and 3. ***!!!");
			} else {
				switch (input) {
				case 1:
					searchFilmByID(kb);
					break;
				case 2:
					searchByKeyword(kb);
					break;
				default:
					break;
				}
			}

		} while (input != 3);

		System.out.println("Goodbye.");

	}

	private void searchFilmByID(Scanner kb) {
		int input;

		do {
			System.out.println();
			System.out.print("Please enter a film ID >> ");
			input = getIntFromUser(kb);

			if (input == -1) {
				System.out.println();
				System.out.println("\t!!!*** Film ID's are a 4 digit number ***!!!");
			}
		} while (input == -1);
		Film myFilm = db.findFilmById(input);

		if (myFilm == null) {
			System.out.println();
			System.out.println("Film of ID(" + input + ") could not be found in our database.");
		} else {
			System.out.println();
			System.out.println("Your film has been found.");
			displayFilm(myFilm);
		}
	}

	private void searchByKeyword(Scanner kb) {
		String input = null;

		do {

			System.out.println();
			System.out.print("Please enter a (1) keyword >> ");
			input = getStringFromUser(kb);

			if (input == null) {
				System.out.println();
				System.out.println("\t!!!*** Input invalid, please enter 1 word. ***!!!");
			}

		} while (input == null);

		List<Film> myFilmList = db.findFilmByKeyWord(input);

		System.out.println();
		if (myFilmList.size() > 0) {
			for (Film film : myFilmList) {
				displayFilm(film);
			}
		} else {
			System.out.println("\tThere were no films with the keyword \"" + input + "\".");
		}

	}

	private int getIntFromUser(Scanner kb) {
		int input = -1;

		try {
			input = Integer.parseInt(kb.next());
		} catch (NumberFormatException e) {
//		  System.err.println("Number Format Exception, an integer is required here.");
		}

		return input;
	}

	private String getStringFromUser(Scanner kb) {
		String input = null;

		kb.nextLine();
		input = kb.nextLine();

		int index = input.indexOf(' ');

		if (index > 0) { // we are trying to get the first word from the user, even if they type in 10
							// words.
			input = input.substring(0, input.indexOf(' '));
		}

		return input;
	}

	private void displayFilm(Film myFilm) {

		printDivider();
		System.out.println("Title: " + myFilm.getTitle() + "(" + myFilm.getRating() + ") Released in "
				+ myFilm.getReleaseYear().substring(0, 4));
		System.out.println("Epic Description:");
		System.out.println(myFilm.getDescription());
		printDivider();

	}

	private void printDivider() {
		System.out.println(
				"********************************************************************************************************************************");
	}

}
