package pikahayang.pikahayang.Model;

public class HistoryModel {
    String id_transaksi, kd_transaksi, tanggal, status_transaksi;

    public HistoryModel(String id_transaksi, String kd_transaksi, String tanggal, String status_transaksi){
        this.id_transaksi = id_transaksi;
        this.kd_transaksi = kd_transaksi;
        this.tanggal = tanggal;
        this.status_transaksi = status_transaksi;
    }

    public String getId_transaksi() {
        return id_transaksi;
    }

    public void setId_transaksi(String id_transaksi) {
        this.id_transaksi = id_transaksi;
    }

    public String getKd_transaksi() {
        return kd_transaksi;
    }

    public void setKd_transaksi(String kd_transaksi) {
        this.kd_transaksi = kd_transaksi;
    }


    public String getStatus_transaksi() {
        return status_transaksi;
    }

    public void setStatus_transaksi(String status_transaksi) {
        this.status_transaksi = status_transaksi;
    }

    public String getTanggal() {
        return tanggal;
    }

    public void setTanggal(String tanggal) {
        this.tanggal = tanggal;
    }
}
