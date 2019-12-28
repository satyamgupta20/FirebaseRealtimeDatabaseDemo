package app.track.firebaserealtimedatabasedemo;

public class Artist {
    String artistId;
    String artistName;
    String artistEmail;
    String artistPassword;
    String artistConfirmPassword;
    String artistMobile;
    String artistGenre;
    String artistGender;


    public Artist(String id, String name, String genre) {
    }

    public Artist(String artistId, String artistName, String artistEmail, String artistPassword, String artistConfirmPassword, String artistMobile, String artistGenre, String artistGender) {
        this.artistId = artistId;
        this.artistName = artistName;
        this.artistEmail = artistEmail;
        this.artistPassword = artistPassword;
        this.artistConfirmPassword = artistConfirmPassword;
        this.artistMobile = artistMobile;
        this.artistGenre = artistGenre;
        this.artistGender = artistGender;
    }

    public String getArtistId() {
        return artistId;
    }

    public String getArtistName() {
        return artistName;
    }

    public String getArtistEmail() {
        return artistEmail;
    }

    public String getArtistPassword() {
        return artistPassword;
    }

    public String getArtistConfirmPassword() {
        return artistConfirmPassword;
    }

    public String getArtistMobile() {
        return artistMobile;
    }

    public String getArtistGenre() {
        return artistGenre;
    }

    public String getArtistGender() {
        return artistGender;
    }
}
