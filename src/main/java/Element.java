import java.util.Arrays;

public class Element {
    private int[] ID = new int[4];
    double[][] HL;
    double[][] CL;
    double[] PL;
    public Element() {
        Arrays.fill(ID, 0);
    }

    public int[] getID() {
        return ID;
    }

    public void setID(int i, int nHeight) {
        ID[0] = i;
        ID[1] = ID[0] + (int) nHeight;
        ID[2] = ID[1] + 1;
        ID[3] = ID[0] + 1;
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
