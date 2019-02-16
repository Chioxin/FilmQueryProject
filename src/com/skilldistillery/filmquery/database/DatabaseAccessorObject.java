package com.skilldistillery.filmquery.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
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
				int id = rs.getInt("id");
				String title = rs.getString("title");
				String description = rs.getString("description");
				String releaseYear = rs.getString("release_year");
				int languageId = rs.getInt("language_id");
				int rentalDuration = rs.getInt("rental_duration");
				double rentalRate = rs.getDouble("rental_rate");
				int length = rs.getInt("length");
				double replacementCost = rs.getDouble("replacement_cost");
				String rating = rs.getString("rating");
				String specialFeatures = rs.getString("special_features");
				myFilm = new Film(id, title, description, releaseYear, languageId, rentalDuration, rentalRate, length, replacementCost, rating, specialFeatures);
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
				int id = rs.getInt("id");
				String firstName = rs.getString("first_name");
				String lastName = rs.getString("last_name");
				myActor = new Actor(id, firstName, lastName);
			}
		} catch (SQLException e) {
			System.err.println("Something went wrong in FindActorByID method.");
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public List<Actor> findActorsByFilmId(int filmId) {
		// TODO Auto-generated method stub
		return null;
	}

}
