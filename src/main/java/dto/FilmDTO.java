package dto;

import java.util.HashMap;

import model.Film;

public class FilmDTO implements CustomPlainTextSerializable<FilmDTO> {
	private int id;
	private String title;
	private int year;
	private String director;
	private String stars;
	private String review;
   
   
   public FilmDTO() {}
   
   public int getID() {
	   return id;
   }
   public void setID(int id) {
	   this.id = id;
   }

	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public int getYear() {
		return year;
	}
	public void setYear(int year) {
		this.year = year;
	}
	public String getDirector() {
		return director;
	}
	public void setDirector(String director) {
		this.director = director;
	}
	public String getStars() {
		return stars;
	}
	public void setStars(String stars) {
		this.stars = stars;
	}
	public String getReview() {
		return review;
	}
	public void setReview(String review) {
		this.review = review;
	}
	@Override
	public String toString() {
		return "Film [title=" + title + ", year=" + year
				+ ", director=" + director + ", stars=" + stars + ", review="
				+ review + "]";
	}  
	
	
	public static FilmDTO toFilmDTO (Film film) {
		var res = new FilmDTO();
		res.setID(film.getId());
		res.setDirector(film.getDirector());
		res.setTitle(film.getTitle());
		res.setStars(film.getStars());
		res.setReview(film.getReview());
		return res;
	}
	
    @Override
    public String toPlainText() {
    	var sb = new StringBuilder();
    	sb.append('[');
    	sb.append("<id>").append('[').append(this.id).append(']');
		sb.append("<director>").append('[').append(director).append(']');
		sb.append("<title>").append('[').append(title).append(']');
		sb.append("<year>").append('[').append(year).append(']');
		sb.append("<stars>").append('[').append(stars).append(']');
		sb.append("<review>").append('[').append(review).append(']');
		sb.append(']');
		return sb.toString();
    }

    @Override
    public FilmDTO fromPlainText(String plainText) {
    	FilmDTO filmDTO = new FilmDTO();
    	var map = new HashMap<String, String>();
    	var sbKey = new StringBuilder();
    	var sbValue = new StringBuilder();
    	var keyLevel = 0;
    	for(var item : plainText.toCharArray()) {
    		if(item == '<' && keyLevel == 0) {
    			keyLevel = 1;
    			continue;
    		}
    		if(item == '>' && keyLevel == 1) {
    			keyLevel = 2;
    			continue;
    		}
    		if (keyLevel == 1) {
    			sbKey.append(item);
    		}
    		
    		if(item == '[' && keyLevel == 2) {
    			keyLevel = 3;
    			continue;
    		}
    		if(item == ']' && keyLevel == 3) {
    			keyLevel = 0;
    			map.put(sbKey.toString(), sbValue.toString());
    			sbKey = new StringBuilder();
    			sbValue = new StringBuilder();
    			
    		}
    		if (keyLevel == 3) {
    			sbValue.append(item);
    		}
    		
    	}
        if (map.containsKey("id")) {
        	filmDTO.setID(Integer.parseInt(map.get("id")));
        }
        if (map.containsKey("director")) {
        	filmDTO.setDirector(map.get("director"));
        }
        if (map.containsKey("title")) {
        	filmDTO.setTitle(map.get("title"));
        }
        if (map.containsKey("year")) {
        	filmDTO.setYear(Integer.parseInt(map.get("year")));
        }
        if (map.containsKey("stars")) {
        	filmDTO.setStars(map.get("stars"));
        }
        if (map.containsKey("review")) {
        	filmDTO.setReview(map.get("review"));
        }
        return filmDTO;
    }
}
