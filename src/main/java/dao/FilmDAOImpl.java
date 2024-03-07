package dao;
import java.util.ArrayList;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;
import dto.PaginatedFilmsDTO;
import model.Film;
import java.sql.*;

/**
 * This class helps to perform CRUD functionalities on the FILM table of the database.
 * @author damzxyno
 *
 */

public class FilmDAOImpl implements FilmDAO {
	private DBContext dbContext;
	private final Logger logger = Logger.getLogger(FilmDAOImpl.class.getName());
	
	public FilmDAOImpl() {
		dbContext = new DBContext();
	}
	
	/**
	 * This method gets all paginated films
	 */
	@Override
    public PaginatedFilmsDTO getAll(int pageNumber, int pageSize) {
        var films = new ArrayList<Film>();
        var filmsCount = 0;
        try {
            var dbConnection = dbContext.openConnection();
            var selectSQL = "SELECT * FROM films LIMIT ?, ?";
            var dbStatement = dbConnection.prepareStatement(selectSQL);
            int offset = (pageNumber - 1) * pageSize;
            dbStatement.setInt(1, offset);
            dbStatement.setInt(2, pageSize);

            var rs1 = dbStatement.executeQuery();
            while (rs1.next()) {
                Optional<Film> film = getNextFilm(rs1);
                film.ifPresent(films::add);
            }
            
            filmsCount =  getTotalFilmsCount(dbConnection, null);

            dbStatement.close();
            dbConnection.close();
        } catch (SQLException se) {
            logger.log(Level.SEVERE, "An error occurred while fetching all films", se);
        }
        return new PaginatedFilmsDTO(pageNumber, pageSize, filmsCount, films);
    }
	   
	@Override
	public Optional<Film> get(int id) {
	       Optional<Film> newFilm = Optional.empty();
	       try {
	           var dbConnection = dbContext.openConnection();
	           var selectSQL = "SELECT * FROM films WHERE id = ?"; 
	           var preparedStatement = dbConnection.prepareStatement(selectSQL);
	           preparedStatement.setInt(1, id);

	           var resultSet = preparedStatement.executeQuery();
	           if (resultSet.next()) {
	               newFilm = getNextFilm(resultSet);
	           }
	           preparedStatement.close();
	           dbConnection.close();
	       } catch (SQLException se) {
	           logger.log(Level.SEVERE, "An error occurred while fetching a film by ID", se);
	       }

	       return newFilm;
	}
	 
	@Override
    public void insert(Film film) {
        try {
            var dbConnection = dbContext.openConnection();
            var insertSQL = "INSERT INTO films (title, year, director, stars, review) VALUES (?, ?, ?, ?, ?)";
            var preparedStatement = dbConnection.prepareStatement(insertSQL);

            preparedStatement.setString(1, film.getTitle());
            preparedStatement.setInt(2, film.getYear());
            preparedStatement.setString(3, film.getDirector());
            preparedStatement.setString(4, film.getStars());
            preparedStatement.setString(5, film.getReview());

            preparedStatement.executeUpdate();

            preparedStatement.close();
            dbConnection.close();
        } catch (SQLException se) {
            logger.log(Level.SEVERE, "An error occurred while inserting a film", se);
        }
    }

    @Override
    public void update(Film film) {
        try {
            var dbConnection = dbContext.openConnection();
            var updateSQL = "UPDATE films SET title=?, year=?, director=?, stars=?, review=? WHERE id=?";
            var preparedStatement = dbConnection.prepareStatement(updateSQL);

            preparedStatement.setString(1, film.getTitle());
            preparedStatement.setInt(2, film.getYear());
            preparedStatement.setString(3, film.getDirector());
            preparedStatement.setString(4, film.getStars());
            preparedStatement.setString(5, film.getReview());
            preparedStatement.setInt(6, film.getId());

            preparedStatement.executeUpdate();

            preparedStatement.close();
            dbConnection.close();
        } catch (SQLException se) {
            logger.log(Level.SEVERE, "An error occurred while updating a film", se);
        }
    }

    @Override
    public String delete(int id) {
    	String title = null;
        try {
        	var dbConnection = dbContext.openConnection();
            String deleteSQL = "DELETE FROM films WHERE id=?";
            var deleteStatement = dbConnection.prepareStatement(deleteSQL);
            deleteStatement.setInt(1, id);
            deleteStatement.executeUpdate();
            deleteStatement.close();
            dbConnection.close();
        } catch (SQLException se) {
            logger.log(Level.SEVERE, "An error occurred while deleting a film", se);
        }
        return title;
    }
	

   private Optional<Film> getNextFilm(ResultSet rs){
		try {
			var film = Film.builder()
					.id(rs.getInt("id"))
					.title(rs.getString("title"))
					.year(rs.getInt("year"))
					.director(rs.getString("director"))
					.stars(rs.getString("stars"))
					.review(rs.getString("review"))
					.build();
			return Optional.of(film);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return Optional.empty();		
   }   

	@Override
	public PaginatedFilmsDTO getAll(String searchStr, String director, int startYear, int endYear, String stars,
			int pageNumber, int pageSize) {
	var whereClause = deriveWhereClause(searchStr, director, startYear, endYear, stars);
	    
	    var films = new ArrayList<Film>();
	    var filmsCount = 0;
	    try {
	        var dbConnection = dbContext.openConnection();
	        var selectSQL = "SELECT * FROM films " + whereClause + "LIMIT ?, ?";
	        var dbStatement = dbConnection.prepareStatement(selectSQL);
	        
	        // Set parameters based on the generated WHERE clause
	        int paramIndex = 1;
	        if (searchStr != null) {
	            dbStatement.setString(paramIndex++, "%" + searchStr + "%");
	        }
	        if (director != null) {
	            dbStatement.setString(paramIndex++, "%" + director + "%");
	        }
	        if (startYear > 0 && endYear > 0) {
	            dbStatement.setInt(paramIndex++, startYear);
	            dbStatement.setInt(paramIndex++, endYear);
	        }
	        if (stars != null) {
	            dbStatement.setString(paramIndex++, "%" + stars + "%");
	        }
	        logger.info(dbStatement.toString());
	        int offset = (pageNumber - 1) * pageSize;
	        dbStatement.setInt(paramIndex++, offset);
	        dbStatement.setInt(paramIndex++, pageSize);
	        logger.info(dbStatement.toString());
	        var rs1 = dbStatement.executeQuery();
	        while (rs1.next()) {
	            Optional<Film> film = getNextFilm(rs1);
	            film.ifPresent(films::add);
	        }
	        filmsCount = getTotalFilmsCount(dbConnection, whereClause, searchStr, director, startYear, endYear, stars);
	        dbStatement.close();
	        dbConnection.close();
	    } catch (SQLException se) {
	        logger.log(Level.SEVERE, "An error occurred while fetching all films", se);
	    }
	    return new PaginatedFilmsDTO(pageNumber, pageSize, filmsCount, films);
	}

	private String deriveWhereClause(String searchStr, String director, int startYear, int endYear, String stars) {
	    var conditions = new ArrayList<String>();
	    if (searchStr != null) {
	        conditions.add("title LIKE ?");
	    }
	    if (director != null) {
	        conditions.add("director LIKE ?");
	    }
	    if (startYear > 0 && endYear > 0) {
	        conditions.add("year BETWEEN ? AND ?");
	    }
	    if (stars != null) {
	        conditions.add("stars LIKE ?");
	    }
	    return conditions.isEmpty() ? "" : "WHERE " + String.join(" AND ", conditions) +  " ";
	}
	
	private int getTotalFilmsCount(Connection connection, String whereClause, String searchStr, String director, int startYear, int endYear, String stars) throws SQLException {
	    var countSQL = "SELECT COUNT(*) AS count FROM films " + whereClause;
	    var countStatement = connection.prepareStatement(countSQL);

	    // Set parameters based on the generated WHERE clause
	    int paramIndex = 1;
	    if (searchStr != null) {
	        countStatement.setString(paramIndex++, "%" + searchStr + "%");
	    }
	    if (director != null) {
	        countStatement.setString(paramIndex++, "%" + director + "%");
	    }
	    if (startYear > 0 && endYear > 0) {
	        countStatement.setInt(paramIndex++, startYear);
	        countStatement.setInt(paramIndex++, endYear);
	    }
	    if (stars != null) {
	        countStatement.setString(paramIndex++, "%" + stars + "%");
	    }

	    var rs = countStatement.executeQuery();
	    if (rs.next()) {
	        return rs.getInt("count");
	    }

	    // Return 0 if no count is obtained
	    return 0;
	}
	
	private int getTotalFilmsCount(Connection connection, String whereClause) throws SQLException {
	       var countQuery = whereClause == null ? "SELECT COUNT(*) as total FROM films" : "SELECT COUNT(*) " + whereClause + " as total FROM films";
	       var countStatement = connection.createStatement();
	       var countResultSet = countStatement.executeQuery(countQuery);

	       int totalFilmsCount = 0;
	       if (countResultSet.next()) {
	           totalFilmsCount = countResultSet.getInt("total");
	       }

	       countStatement.close();
	       return totalFilmsCount;
	 }
}