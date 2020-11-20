package pikahayang.pikahayang.Model;

public class PaymentModel {
    String id_cart, id_produk, id_users, nama_produk, quantity, harga, berat, size;

    public PaymentModel(String id_cart, String id_produk, String id_users, String nama_produk,
                        String harga, String quantity, String berat, String size){
        this.id_cart = id_cart;
        this.id_produk = id_produk;
        this.id_users = id_users;
        this.nama_produk = nama_produk;
        this.quantity = quantity;
        this.berat = berat;
        this.harga = harga;
        this.size = size;
    }

    public String getId_cart() {
        return id_cart;
    }

    public void setId_cart(String id_cart) {
        this.id_cart = id_cart;
    }

    public String getId_produk() {
        return id_produk;
    }

    public void setId_produk(String id_produk) {
        this.id_produk = id_produk;
    }

    public String getId_users() {
        return id_users;
    }

    public void setId_users(String id_users) {
        this.id_users = id_users;
    }

    public String getNama_produk() {
        return nama_produk;
    }

    public void setNama_produk(String nama_produk) {
        this.nama_produk = nama_produk;
    }

    public String getHarga() {
        return harga;
    }

    public void setHarga(String harga) {
        this.harga = harga;
    }

    public String getQuantity() {
        return quantity;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }

    public String getBerat() {
        return berat;
    }

    public void setBerat(String berat) {
        this.berat = berat;
    }

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }
}
