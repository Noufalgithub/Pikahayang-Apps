package pikahayang.pikahayang.Model;

public class ServiceKurirModel {
    String service, etd, value;

    public ServiceKurirModel(String service, String etd, String value){
        this.service = service;
        this.etd = etd;
        this.value = value;
    }

    public ServiceKurirModel(){

    }

    public String getService() {
        return service;
    }

    public void setService(String service) {
        this.service = service;
    }

    public String getEtd() {
        return etd;
    }

    public void setEtd(String etd) {
        this.etd = etd;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
