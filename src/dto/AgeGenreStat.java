package dto;

public class AgeGenreStat {
    private String ageGroup;
    private String genreName;
    private int count;

    public AgeGenreStat(String ageGroup, String genreName, int count) {
        this.ageGroup = ageGroup;
        this.genreName = genreName;
        this.count = count;
    }

    public String getAgeGroup() {
        return ageGroup;
    }

    public String getGenreName() {
        return genreName;
    }

    public int getCount() {
        return count;
    }
}