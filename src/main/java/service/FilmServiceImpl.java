package service;
import java.util.logging.Logger;
import dao.FilmDAO;
import dao.FilmDAOImpl;
import dto.FilmDTO;
import dto.PaginatedFilmsDTO;
import model.Film;

public class FilmServiceImpl implements FilmService{
	private final FilmDAO filmDAO;
	private final Logger logger = Logger.getLogger(FilmServiceImpl.class.getName());
	
	public FilmServiceImpl() {
		filmDAO = new FilmDAOImpl();
	}

	@Override
	public PaginatedFilmsDTO getAllFilms(int pageNo, int pageSize) {
		pageNo = pageNo > 0 ? pageNo : 1;
		pageSize = pageSize > 0 ? pageSize : 10;
		return filmDAO.getAll(pageNo, pageSize);
	}

	@Override
	public FilmDTO getFilmByID(int id) {
		var film = filmDAO.get(id);
		if (film.isEmpty()) {
			var message = String.format("Film with ID '%s' not found", id);
			logger.severe(() -> message);
//			throw new FilmNotFoundException(message);
		}
		return FilmDTO.toFilmDTO(film.get());
	}

	@Override
	public FilmDTO createNewFilm(FilmDTO filmDTO) {
		var film = Film.builder()
				.title(filmDTO.getTitle())
				.director(filmDTO.getDirector())
				.review(filmDTO.getReview())
				.stars(filmDTO.getStars())
				.year(filmDTO.getYear())
				.build();
		filmDAO.insert(film);
		return FilmDTO.toFilmDTO(film);
	}

	@Override
	public FilmDTO updateFilm(int id, FilmDTO filmDTO) {
		var film = filmDAO.get(id);
		if (film.isEmpty()) {
			var message = String.format("Film with ID '%s' not found", id);
//			throw new FilmNotFoundException(message);
		}
		film.get().setTitle(filmDTO.getTitle());
		film.get().setDirector(filmDTO.getDirector());
		film.get().setYear(filmDTO.getYear());
		film.get().setReview(filmDTO.getReview());
		film.get().setStars(filmDTO.getStars());
		filmDAO.update(film.get());
		return FilmDTO.toFilmDTO(film.get());
		
	}

	@Override
	public FilmDTO deleteFilmByID(int id) {
		var film = filmDAO.get(id);
		if (film.isEmpty()) {
			var message = String.format("Film with ID '%s' not found", id);
			logger.severe(() -> message);
//			throw new FilmNotFoundException(message);
		}
		filmDAO.delete(id);
		return FilmDTO.toFilmDTO(film.get());	
	}

	@Override
	public PaginatedFilmsDTO filterOutFilms(String searchStr, String director, int startYear, int endYear, String stars, int pageNumber, int pageSize) {
		pageNumber = pageNumber > 0 ? pageNumber : 1;
		pageSize = pageSize > 0 ? pageSize : 10;
		return filmDAO.getAll(searchStr, director, startYear, endYear, stars, pageNumber, pageSize);
	}
}
