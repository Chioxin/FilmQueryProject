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
    Film film = db.findFilmById(-1);
    System.out.println(film);
    Actor actor = db.findActorById(-1);
    System.out.println(actor);
    List<Actor> actorList = db.findActorsByFilmId(-1);
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
		  }
		  
	  } while (input != 3);
	  
	  System.out.println("Goodbye.");
    
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

}
