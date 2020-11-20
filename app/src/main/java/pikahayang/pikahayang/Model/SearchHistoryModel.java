package pikahayang.pikahayang.Model;

public class SearchHistoryModel {
    String  kd_transaksi, tanggal, status_transaksi;

    public SearchHistoryModel(){
        this.kd_transaksi = kd_transaksi;
        this.tanggal = tanggal;
        this.status_transaksi = status_transaksi;
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
