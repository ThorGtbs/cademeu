package Classes;



public class MarkerSets {

    private String Pais,Cidade,Estado;
    private double Lgt,Ltd;


    public MarkerSets(){

    }

    public String getPais() {
        return Pais;
    }

    public void setPais(String Pais) {
        this.Pais = Pais;
    }

    public String getCidade() {
        return Cidade;
    }

    public void setCidade(String Cidade) {
        this.Cidade = Cidade;
    }

    public String getEstado() {
        return Estado;
    }

    public void setEstado(String Estado) {
        this.Estado= Estado;
    }

    public double getLgt() {
        return Lgt;
    }

    public void setLgt(float Longitude) {
        this.Lgt = Longitude;
    }

    public double getLtd() {
        return Ltd;
    }

    public void setLtd(float Latitude) {
        this.Ltd = Latitude;
    }
}
