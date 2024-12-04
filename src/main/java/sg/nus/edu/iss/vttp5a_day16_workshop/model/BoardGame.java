package sg.nus.edu.iss.vttp5a_day16_workshop.model;

public class BoardGame {
    private Integer gameID;
    private String name;
    private Integer year;
    private Integer ranking;
    private Integer usersRated;
    private String url;
    private String imageLink;

    public BoardGame(){

    }

    public BoardGame(Integer gameID, String name, Integer year, Integer ranking, Integer usersRated, String url,
            String imageLink) {
        this.gameID = gameID;
        this.name = name;
        this.year = year;
        this.ranking = ranking;
        this.usersRated = usersRated;
        this.url = url;
        this.imageLink = imageLink;
    }

    public Integer getGameID() {
        return gameID;
    }

    public void setGameID(Integer gameID) {
        this.gameID = gameID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getYear() {
        return year;
    }

    public void setYear(Integer year) {
        this.year = year;
    }

    public Integer getRanking() {
        return ranking;
    }

    public void setRanking(Integer ranking) {
        this.ranking = ranking;
    }

    public Integer getUsersRated() {
        return usersRated;
    }

    public void setUsersRated(Integer usersRated) {
        this.usersRated = usersRated;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getImageLink() {
        return imageLink;
    }

    public void setImageLink(String imageLink) {
        this.imageLink = imageLink;
    }

    @Override
    public String toString(){
        return "gid/=" + gameID + ";name/=" + name + ";year/=" + year + ";ranking/=" + ranking + ";usersRated/=" + usersRated + ";url/=" + url + ";imageLink/=" + imageLink;
    }

    

}
