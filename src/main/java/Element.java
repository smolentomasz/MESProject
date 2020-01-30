import java.util.Arrays;

public class Element {
    private int[] ID = new int[4];
    double[][] HL;
    double[][] CL;
    double[] PL;
    private double conductivity;
    private double density;
    private double specific_heat;

    public Element() {
        Arrays.fill(ID, 0);
    }

    public int[] getID() {
        return ID;
    }

    public void setID(int i, int nHeight) {
        ID[0] = i;
        ID[1] = ID[0] + nHeight;
        ID[2] = ID[1] + 1;
        ID[3] = ID[0] + 1;
    }

    public double getConductivity() {
        return conductivity;
    }

    public void setConductivity(double conductivity) {
        this.conductivity = conductivity;
    }

    public double getDensity() {
        return density;
    }

    public void setDensity(double density) {
        this.density = density;
    }

    public double getSpecific_heat() {
        return specific_heat;
    }

    public void setSpecific_heat(double specific_heat) {
        this.specific_heat = specific_heat;
    }

    public void setHL(double[][] HL) {
        this.HL = HL;
    }

    public void setCL(double[][] CL) {
        this.CL = CL;
    }

    public void setPL(double[] PL) {
        this.PL = PL;
    }
}
