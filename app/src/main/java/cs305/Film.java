package cs305;

public class Film {
    public String title;
    public String description;
    public Integer length;

    public Film() {
    }

    @Override
    public String toString() {
        return "Film(\"" + this.title + "\", length: " + this.length + ")";
    }
}
