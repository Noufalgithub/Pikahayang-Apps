package pikahayang.pikahayang.Model;

/**
 * Created by KUNCORO on 23/04/2017.
 */

public class SizeModel {
    private String id, size, stock, berat;

    public SizeModel(String id, String size, String stock, String berat) {
        this.id = id;
        this.size = size;
        this.stock = stock;
        this.berat = berat;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }

    public String getStock() {
        return stock;
    }

    public void setStock(String stock) {
        this.stock = stock;
    }

    public String getBerat() {
        return berat;
    }

    public void setBerat(String berat) {
        this.berat = berat;
    }
}
