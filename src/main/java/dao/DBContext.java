package dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import config.Configuration;
import util.Constant;

/**
 * This class help manage and create connection to the database.
 * @author damzxyno
 *
 */
public class DBContext {
	private Connection connection;
	private final String user;
    private final String password;
    private final String url;
    private Logger logger = Logger.getLogger(DBContext.class.getName());
    
    public DBContext() {
    	Configuration config = Configuration.getInstance();
		url = config.getPropertyByName(Constant.DB_URL) + config.getPropertyByName(Constant.DB_USERNAME);
		user = config.getPropertyByName(Constant.DB_USERNAME);
		password = config.getPropertyByName(Constant.DB_PASSWORD);
    }
    
    /**
     * This method help create connection to the database using the provided credentials.
     * @return 
     * @throws SQLException
     */
    public Connection openConnection() throws SQLException{
    	logger.info("Opening DB connection");
		try{
		     Class.forName(Constant.MYSQL_CLASS_PATH).getDeclaredConstructor().newInstance();
		} catch(Exception e) {
			System.out.println(e); 
		}
		try{
			connection = DriverManager.getConnection(url, user, password);
			logger.info("DB connection established successfully");
		    return connection;
		} catch(SQLException se) { 
			logger.log(Level.SEVERE, "DB connection not established successfully.", se);
			throw se;
		}	   
    }
}
