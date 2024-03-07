package dao;

import java.util.Optional;
import dto.PaginatedFilmsDTO;
import model.Film;

/**
 * Interface for the film data access object
 * @author damzxyno
 *
 */
public interface FilmDAO {
	PaginatedFilmsDTO getAll(int pageNumber, int pageSize);
	PaginatedFilmsDTO getAll(String searchStr, String director, int startYear, int endYear, String stars, int pageNumber, int PageSize);
	Optional<Film> get(int id);
	void insert(Film film);
	void update (Film film);
	String delete (int id);
}
