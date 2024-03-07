package dto;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlRootElement;
import model.Film;

/**
 * This class help to transmit a pagination metadata along with the films content.
 * @author damzxyno
 *
 */
@XmlRootElement(name="PaginatedFilmsDTO")
@XmlAccessorType(XmlAccessType.NONE)
public class PaginatedFilmsDTO implements  CustomPlainTextSerializable<PaginatedFilmsDTO>{
    @XmlElement
	private int pageNumber;
    @XmlElement
    private int pageSize;
    @XmlElement
    private int currentFilmCount;
    @XmlElement
    private int totalFilmsCount;
    @XmlElement
    private int totalFilmsPages;
    @XmlElement
    private boolean hasNext;
    @XmlElement
    private boolean hasPrevious;
    @XmlElement
    private List<Film> films;
    
    public PaginatedFilmsDTO() {}

    public PaginatedFilmsDTO(int pageNumber, int pageSize, int totalFilmsCount, List<Film> films) {
        this.pageNumber = pageNumber;
        this.pageSize = pageSize;
        this.totalFilmsCount = totalFilmsCount;
        this.films = films;
        this.currentFilmCount = films.size();
        this.totalFilmsPages = (int) Math.ceil((double) totalFilmsCount / pageSize);
        this.hasNext = pageNumber < totalFilmsPages;
        this.hasPrevious = pageNumber > 1;
    }

    private void setPageNumber(int pageNumber) {
    	this.pageNumber = pageNumber;
    }
    private void setPageSize(int pageSize) {
    	this.pageSize = pageSize;
    }
    private void setFilms(List<Film> films) {
		this.films = films;
	}
    public int getPageNumber() {
        return pageNumber;
    }

    public int getPageSize() {
        return pageSize;
    }

    public int getCurrentFilmCount() {
        return currentFilmCount;
    }

    public int getTotalFilmsCount() {
        return totalFilmsCount;
    }

    public int getTotalFilmsPages() {
        return totalFilmsPages;
    }

    public boolean hasNext() {
        return hasNext;
    }

    public boolean hasPrevious() {
        return hasPrevious;
    }

    public List<Film> getFilms() {
        return films;
    }
    
    @Override
    public String toString() {
        return "FilmContainer{" +
                "pageNumber=" + pageNumber +
                ", pageSize=" + pageSize +
                ", currentFilmCount=" + currentFilmCount +
                ", totalFilmsCount=" + totalFilmsCount +
                ", totalFilmsPages=" + totalFilmsPages +
                ", hasNext=" + hasNext +
                ", hasPrevious=" + hasPrevious +
                ", films=" + films +
                '}';
    }

	@Override
	public String toPlainText() {
		var sb = new StringBuilder();
		sb.append('[');
		sb.append("<page-number>").append('[').append(this.pageNumber).append(']');
		sb.append("<page-size>").append('[').append(this.pageSize).append(']');
		sb.append("<current-film-count>").append('[').append(this.currentFilmCount).append(']');
		sb.append("<total-films-count>").append('[').append(this.totalFilmsCount).append(']');
		sb.append("<total-films-pages>").append('[').append(this.totalFilmsPages).append(']');
		sb.append("<has-next>").append('[').append(this.hasNext).append(']');
		sb.append("<has-Previous>").append('[').append(this.hasPrevious).append(']');
		sb.append("<films>").append('[');
			for (var film : films) {
				sb.append("|");
				sb.append("<id>").append('[').append(film.getId()).append(']');
				sb.append("<director>").append('[').append(film.getDirector()).append(']');
				sb.append("<title>").append('[').append(film.getTitle()).append(']');
				sb.append("<year>").append('[').append(film.getYear()).append(']');
				sb.append("<stars>").append('[').append(film.getStars()).append(']');
				sb.append("<review>").append('[').append(film.getReview()).append(']');
			}
			if (!films.isEmpty()) {
				sb.append("|");
			}
		sb.append(']');
		sb.append(']');
		return sb.toString();
	}

	 @Override
	    public PaginatedFilmsDTO fromPlainText(String plainText) {
	        var paginatedFilmsDTO = new PaginatedFilmsDTO();
	        var pageNumberPattern = Pattern.compile("<page-number>\\[(\\d+)]");
	        var pageSizePattern = Pattern.compile("<page-size>\\[(\\d+)]");
	        var matcher = pageNumberPattern.matcher(plainText);
	        if (matcher.find()) {
	            paginatedFilmsDTO.setPageNumber(Integer.parseInt(matcher.group(1)));
	        }
	        matcher = pageSizePattern.matcher(plainText);
	        if (matcher.find()) {
	            paginatedFilmsDTO.setPageSize(Integer.parseInt(matcher.group(1)));
	        }
	        var filmsPattern = Pattern.compile("<films>\\[.*?\\]");
	        matcher = filmsPattern.matcher(plainText);
	        if (matcher.find()) {
	            String filmsText = matcher.group();
	            var films = parseFilms(filmsText);
	            paginatedFilmsDTO.setFilms(films);
	        }

	        return paginatedFilmsDTO;
	    }


		private List<Film> parseFilms(String filmsText) {
	        var films = new ArrayList<Film>();
	        var filmPattern = Pattern.compile("\\|<id>\\[(\\d+)]<director>\\[(.*?)\\]<title>\\[(.*?)\\]<year>\\[(\\d+)]<stars>\\[(.*?)\\]<review>\\[(.*?)\\]");
	        var matcher = filmPattern.matcher(filmsText);
	        while (matcher.find()) {
	            Film film = new Film();
	            film.setId(Integer.parseInt(matcher.group(1)));
	            film.setDirector(matcher.group(2));
	            film.setTitle(matcher.group(3));
	            film.setYear(Integer.parseInt(matcher.group(4)));
	            film.setStars(matcher.group(5));
	            film.setReview(matcher.group(6));

	            films.add(film);
	        }
	        return films;
	    }

}
