package uf1.infobosccoma.mymapllocsdinteres.Controller;

/**
 * Created by Gerard on 06/03/2015.
 */
public class LlocInteres {
    private int id;
    private double latitude, longitude;
    private String name, city;
    public LlocInteres(){

    }

    public LlocInteres(int id, double latitude, String name, String city, double longitude) {
        this.id = id;
        this.latitude = latitude;
        this.name = name;
        this.city = city;
        this.longitude = longitude;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }
}
