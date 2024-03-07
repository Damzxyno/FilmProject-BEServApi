package service;

import dto.FilmDTO;
import dto.PaginatedFilmsDTO;
import model.Film;

public interface FilmService {
	PaginatedFilmsDTO getAllFilms(int pageNo, int pageSize);
	FilmDTO getFilmByID(int id);
	FilmDTO createNewFilm(FilmDTO filmDTO);
	FilmDTO updateFilm(int id, FilmDTO filmDTO);
	FilmDTO deleteFilmByID(int id);
	PaginatedFilmsDTO filterOutFilms(String searchStr, String director, int startYear, int endYear, String stars, int pageNumber, int pageSize);
}

