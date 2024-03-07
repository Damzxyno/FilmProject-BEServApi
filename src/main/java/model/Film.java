package model;


public class Film {
	private int id;
	private String title;
	private int year;
	private String director;
	private String stars;
	private String review;
	
   public Film(int id, String title, int year, String director, String stars, String review) {
		super();
		this.id = id;
		this.title = title;
		this.year = year;
		this.director = director;
		this.stars = stars;
		this.review = review;
	}
   
   
   public Film() {}

   private Film(Builder builder) {
       this.id = builder.id;
       this.title = builder.title;
       this.year = builder.year;
       this.director = builder.director;
       this.stars = builder.stars;
       this.review = builder.review;
   }



	public int getId() {
		return id;
	}
	public void setId(int id) {
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
		return "Film [id=" + id + ", title=" + title + ", year=" + year
				+ ", director=" + director + ", stars=" + stars + ", review="
				+ review + "]";
	}  
	
	public static Builder builder(){
		return new Builder();
	}
	
	public static class Builder {
        private int id;
        private String title;
        private int year;
        private String director;
        private String stars;
        private String review;

        public Builder() {
            // default constructor
        }

        public Builder id(int id) {
            this.id = id;
            return this;
        }

        public Builder title(String title) {
            this.title = title;
            return this;
        }

        public Builder year(int year) {
            this.year = year;
            return this;
        }

        public Builder director(String director) {
            this.director = director;
            return this;
        }

        public Builder stars(String stars) {
            this.stars = stars;
            return this;
        }

        public Builder review(String review) {
            this.review = review;
            return this;
        }

        public Film build() {
            return new Film(this);
        }
	}
}
