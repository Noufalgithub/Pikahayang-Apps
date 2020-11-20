package pikahayang.pikahayang.Model;

/**
 * Created by KUNCORO on 23/04/2017.
 */

public class CityModel {
    private String city_id, province_id, province,type, city_name,postal_code;

    public CityModel(String city_id, String province_id, String province, String type, String city_name, String postal_code) {
        this.city_id = city_id;
        this.province_id = province_id;
        this.province = province;
        this.type = type;
        this.city_name = city_name;
        this.postal_code = postal_code;
    }

    public String getCity_id() {
        return city_id;
    }

    public void setCity_id(String city_id) {
        this.city_id = city_id;
    }

    public String getCity_name() {
        return city_name;
    }

    public void setCity_name(String city_name) {
        this.city_name = city_name;
    }

    public String getProvince_id() {
        return province_id;
    }

    public void setProvince_id(String province_id) {
        this.province_id = province_id;
    }

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getPostal_code() {
        return postal_code;
    }

    public void setPostal_code(String postal_code) {
        this.postal_code = postal_code;
    }
}
