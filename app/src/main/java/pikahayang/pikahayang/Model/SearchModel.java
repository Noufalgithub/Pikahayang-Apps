package pikahayang.pikahayang.Model;

public class SearchModel {
    String id, id_kategori, nama_produk, kode_produk, deskripsi, jenis_batik, jenis_kelamin, harga, image;

    public SearchModel(){
        this.id = id;
        this.id_kategori = id_kategori;
        this.nama_produk = nama_produk;
        this.kode_produk = kode_produk;
        this.deskripsi = deskripsi;
        this.jenis_batik = jenis_batik;
        this.jenis_kelamin = jenis_kelamin;
        this.harga = harga;
        this.image = image;
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
