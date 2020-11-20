package pikahayang.pikahayang.Model;

public class DesainPlusKainModel {
    String id, id_kategori, nama_produk, rate, kode_produk, jenis_batik, jenis_kelamin, deskripsi, harga, image, size;

    public DesainPlusKainModel(String id, String id_kategori, String nama_produk,
                               String kode_produk, String jenis_batik, String rate, String jenis_kelamin, String deskripsi, String harga, String size, String image){
        this.id = id;
        this.id_kategori = id_kategori;
        this.nama_produk = nama_produk;
        this.kode_produk = kode_produk;
        this.jenis_batik = jenis_batik;
        this.jenis_kelamin = jenis_kelamin;
        this.size = size;
        this.harga = harga;
        this.deskripsi = deskripsi;
        this.image = image;
        this.rate = rate;
    }

    public String getRate() {
        return rate;
    }

    public void setRate(String rate) {
        this.rate = rate;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getId_kategori() {
        return id_kategori;
    }

    public void setId_kategori(String id_kategori) {
        this.id_kategori = id_kategori;
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

    public String getJenis_batik() {
        return jenis_batik;
    }

    public void setJenis_batik(String jenis_batik) {
        this.jenis_batik = jenis_batik;
    }

    public String getHarga() {
        return harga;
    }

    public void setHarga(String harga) {
        this.harga = harga;
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

    public String getDeskripsi() {
        return deskripsi;
    }

    public void setDeskripsi(String deskripsi) {
        this.deskripsi = deskripsi;
    }

    public String getJenis_kelamin() {
        return jenis_kelamin;
    }

    public void setJenis_kelamin(String jenis_kelamin) {
        this.jenis_kelamin = jenis_kelamin;
    }
}
