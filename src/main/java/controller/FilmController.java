package controller;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.sun.istack.logging.Logger;

import dto.FilmDTO;
import dto.PaginatedFilmsDTO;
import service.FilmService;
import service.FilmServiceImpl;
import util.ContentSerializationContext;
import util.Parser;

@WebServlet("/Films/*")
public class FilmController extends HttpServlet {
	private static final long serialVersionUID = 1L;
    private final FilmService filmService;   
    private static final Logger logger = Logger.getLogger(FilmController.class);
    
    public FilmController() {
        filmService = new FilmServiceImpl(); 
    }

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		var serializer = new ContentSerializationContext(request, response);
		var filmID = serializer.getPathIntegerValue();
		if (filmID.isPresent()) {
			var film = filmService.getFilmByID(filmID.get());
			serializer.serializeResponseObject(film);
			return;
		}
		var pageNumber = serializer.getQueryParamInteger("page-number");
		var pageSize = serializer.getQueryParamInteger("page-size");
		var startYear = serializer.getQueryParamInteger("start-year");
		var endYear = serializer.getQueryParamInteger("end-year");
		var stars = serializer.getQueryParamString("stars");
		var searchStr = serializer.getQueryParamString("search");
		var director = serializer.getQueryParamString("director");
		
		var films = filmService.filterOutFilms(searchStr, director, startYear, endYear, stars, pageNumber, pageSize);
		serializer.serializeResponseObject(films);
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		var serializer = new ContentSerializationContext(request, response);
		var filmDTO = serializer.SerializeRequestObject(FilmDTO.class);
		var film = filmService.createNewFilm(filmDTO);
		serializer.serializeResponseObject(film);
	}

	protected void doPut(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		var serializer = new ContentSerializationContext(request, response);
		var filmID = serializer.getPathIntegerValue();
		if (filmID.isPresent()) {
			var filmDTO = serializer.SerializeRequestObject(FilmDTO.class);
			var film = filmService.updateFilm(filmID.get(), filmDTO);
			serializer.serializeResponseObject(film);
		}
	}
	
	protected void doDelete(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		var serializer = new ContentSerializationContext(request, response);
		var filmID = serializer.getPathIntegerValue();
		if (filmID.isPresent()) {
			var film = filmService.deleteFilmByID(filmID.get());
			serializer.serializeResponseObject(film);
		}
	}
}
