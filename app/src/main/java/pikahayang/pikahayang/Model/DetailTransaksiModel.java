package pikahayang.pikahayang.Model;

public class DetailTransaksiModel {
    String nama_produk, quantity, size, harga;

    public DetailTransaksiModel(String nama_produk, String quantity, String size, String harga){
        this.nama_produk = nama_produk;
        this.quantity = quantity;
        this.size = size;
        this.harga = harga;
    }

    public String getNama_produk() {
        return nama_produk;
    }

    public void setNama_produk(String nama_produk) {
        this.nama_produk = nama_produk;
    }

    public String getQuantity() {
        return quantity;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }

    public String getHarga() {
        return harga;
    }

    public void setHarga(String harga) {
        this.harga = harga;
    }
}
