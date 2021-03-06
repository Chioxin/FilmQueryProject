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
		String query = "SELECT * FROM film WHERE id = ?";

		try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
			PreparedStatement statement = conn.prepareStatement(query);){
			
			statement.setInt(1, filmId);
			ResultSet rs = statement.executeQuery();

			if (rs.next()) {
				myFilm = createFilm(rs);
			} else {
				System.out.println("Could not find a film with ID(" + filmId + ").");
			}

		} catch (SQLException e) {
			System.err.println("Something went wrong in findFilmByID method.");
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
		List<Actor> cast = findActorsByFilmId(id);
		String language = findLanguagesByFilmId(id);
		myFilm = new Film(id, title, description, releaseYear, languageId, rentalDuration, rentalRate, length,
				replacementCost, rating, specialFeatures, cast, language);
		
		return myFilm;
	}
	
	private String findLanguagesByFilmId(int filmId) {
		String myLanguage = "N/A";
		String query = "SELECT film.title, language.name " + 
					   "FROM language JOIN film ON film.language_id = language.id " + 
					   "WHERE film.id = ?;";
		
		try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
			PreparedStatement statement = conn.prepareStatement(query);){
			
			statement.setInt(1, filmId);
			ResultSet rs = statement.executeQuery();
			
			if (rs.next()) {
				myLanguage = rs.getString("language.name");
			} 
			
		} catch (SQLException e) {
			System.err.println("Something went wrong in findLanguagesByFilmId method.");
			e.printStackTrace();
		}
		
		return myLanguage;
	}

	@Override
	public Actor findActorById(int actorId) {

		Actor myActor = null;
		String query = "SELECT * FROM actor WHERE id = ?";

		try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
			PreparedStatement statement = conn.prepareStatement(query);){
			
			statement.setInt(1, actorId);
			ResultSet rs = statement.executeQuery();

			if (rs.next()) {
				
				myActor = createActor(rs);
				
			} else {
				System.out.println("Could not find an actor with ID(" + actorId + ").");
			}
			
			rs.close();
			statement.close();
			conn.close();
			
		} catch (SQLException e) {
			System.err.println("Something went wrong in findActorByID method.");
			e.printStackTrace();
		}
		return myActor;
	}

	@Override
	public List<Actor> findActorsByFilmId(int filmId) {
		List<Actor> myList = new ArrayList<Actor>();
		String query = "SELECT actor.id, actor.first_name, actor.last_name " + 
				"FROM film JOIN film_actor " + 
				"ON film.id = film_actor.film_id " + 
				"JOIN actor " + 
				"ON film_actor.actor_id = actor.id " + 
				"WHERE film.id = ?;";
		
		try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
			PreparedStatement statement = conn.prepareStatement(query);){
			
			statement.setInt(1, filmId);
			ResultSet rs = statement.executeQuery();
			
			while (rs.next()) {
				myList.add(createActor(rs));
			}
			
		} catch (SQLException e) {
			System.err.println("Something went wrong in findActorByFilm method.");
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
	
	@Override
	public List<Film> findFilmByKeyWord(String key) {
		List<Film> myList = new ArrayList<Film>();
		String query = "SELECT * " + 
					   "FROM film " + 
					   "WHERE title LIKE ? OR description LIKE ?;";
		
		try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
			PreparedStatement statement = conn.prepareStatement(query);){
		    
			statement.setString(1, "%" + key + "%");
		    statement.setString(2, "%" + key + "%");
			ResultSet rs = statement.executeQuery();
			
			while (rs.next()) {
				myList.add(createFilm(rs));
			}
				
				
				
		} catch (SQLException e){
			System.err.println("Something went wrong in findFilmByKeyWord method");
			e.printStackTrace();
		}
		
		return myList;
	}

}
