package pikahayang.pikahayang.Model;

public class CartItemsModel {
    String id_cart, id_produk, id_users, nama_produk, kode_produk, jenis_batik, jenis_kelamin, quantity, harga, image, size, stock, berat, subtotal;

    public CartItemsModel(String id_cart, String id_produk, String id_users, String nama_produk,
                          String kode_produk, String harga, String jenis_batik,String jenis_kelamin, String quantity, String size, String stock, String berat, String image, String subtotal){
        this.id_cart = id_cart;
        this.id_produk = id_produk;
        this.id_users = id_users;
        this.nama_produk = nama_produk;
        this.kode_produk = kode_produk;
        this.quantity = quantity;
        this.size = size;
        this.stock = stock;
        this.jenis_batik = jenis_batik;
        this.jenis_kelamin = jenis_kelamin;
        this.berat = berat;
        this.harga = harga;
        this.image = image;
        this.subtotal = subtotal;
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

    public String getKode_produk() {
        return kode_produk;
    }

    public void setKode_produk(String kode_produk) {
        this.kode_produk = kode_produk;
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

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }

    public String getSubtotal() {
        return subtotal;
    }

    public void setSubtotal(String subtotal) {
        this.subtotal = subtotal;
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

    public String getJenis_batik() {
        return jenis_batik;
    }

    public void setJenis_batik(String jenis_batik) {
        this.jenis_batik = jenis_batik;
    }

    public String getJenis_kelamin() {
        return jenis_kelamin;
    }

    public void setJenis_kelamin(String jenis_kelamin) {
        this.jenis_kelamin = jenis_kelamin;
    }
}
