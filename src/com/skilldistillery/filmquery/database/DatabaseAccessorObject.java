package com.skilldistillery.filmquery.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.skilldistillery.filmquery.entities.Actor;
import com.skilldistillery.filmquery.entities.Film;

public class DatabaseAccessorObject implements DatabaseAccessor {
	private static final String URL = "jdbc:mysql://localhost:3306/sdvid?useSSL=false";
	private static final String USER = "student";
	private static final String PASSWORD = "student";

	static {
		try {
			Class.forName("com.mysql.jdbc.Driver");
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
	}

	@Override
	public Film findFilmById(int filmId) {

		Film myFilm = null;

		try {
			Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
			String querry = "SELECT * FROM film WHERE id = ?";
			PreparedStatement statement = conn.prepareStatement(querry);
			statement.setInt(1, filmId);
			ResultSet rs = statement.executeQuery();

			if (rs.next()) {
				myFilm = createFilm(rs);
			} else {
				System.out.println("Could not find a film with ID(" + filmId + ".");
			}

			rs.close();
			statement.close();
			conn.close();

		} catch (SQLException e) {
			System.err.println("Something went wrong in FindFilmByID method.");
			e.printStackTrace();
		}
		return myFilm;
	}
	
	private Film createFilm(ResultSet rs) throws SQLException {
		Film myFilm = null;
		
		int id = rs.getInt("film.id");
		String title = rs.getString("film.title");
		String description = rs.getString("film.description");
		String releaseYear = rs.getString("film.release_year");
		int languageId = rs.getInt("film.language_id");
		int rentalDuration = rs.getInt("film.rental_duration");
		double rentalRate = rs.getDouble("film.rental_rate");
		int length = rs.getInt("film.length");
		double replacementCost = rs.getDouble("film.replacement_cost");
		String rating = rs.getString("film.rating");
		String specialFeatures = rs.getString("film.special_features");
		myFilm = new Film(id, title, description, releaseYear, languageId, rentalDuration, rentalRate, length,
				replacementCost, rating, specialFeatures);
		
		return myFilm;
	}

	@Override
	public Actor findActorById(int actorId) {

		Actor myActor = null;

		try {
			Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
			String querry = "SELECT * FROM actor WHERE id = ?";
			PreparedStatement statement = conn.prepareStatement(querry);
			statement.setInt(1, actorId);
			ResultSet rs = statement.executeQuery();

			if (rs.next()) {
				
				myActor = createActor(rs);
				
//				int id = rs.getInt("id");
//				String firstName = rs.getString("first_name");
//				String lastName = rs.getString("last_name");
//				myActor = new Actor(id, firstName, lastName);
			}
		} catch (SQLException e) {
			System.err.println("Something went wrong in FindActorByID method.");
			e.printStackTrace();
		}
		return myActor;
	}

	@Override
	public List<Actor> findActorsByFilmId(int filmId) {
		List<Actor> myList = new ArrayList<Actor>();
		
		try {
			Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
			String querry = "SELECT actor.id, actor.first_name, actor.last_name " + 
							"FROM film JOIN film_actor " + 
							"ON film.id = film_actor.film_id " + 
							"JOIN actor " + 
							"ON film_actor.actor_id = actor.id " + 
							"WHERE film.id = ?;";
			PreparedStatement statement = conn.prepareStatement(querry);
			statement.setInt(1, filmId);
			ResultSet rs = statement.executeQuery();
			
			while (rs.next()) {
				myList.add(createActor(rs));
			}
			
		} catch (SQLException e) {
			System.err.println("Something went wrong in FindActorByFilm method.");
			e.printStackTrace();
		}

		return myList;
	}
	
	private Actor createActor(ResultSet rs) throws SQLException {
		Actor myActor = null;
		
		int id = rs.getInt("actor.id");
		String firstName = rs.getString("actor.first_name");
		String lastName = rs.getString("actor.last_name");
		myActor = new Actor(id, firstName, lastName);
		
		return myActor;
	}

}
