import org.la4j.LinearAlgebra;
import org.la4j.Matrix;
import org.la4j.Vector;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.sql.SQLOutput;
import java.util.ArrayList;

public class BasicMain {
    private static ArrayList<Double> startData = new ArrayList<>();
    public static void main(String[] args) {
        BufferedReader reader;
        try {
            reader = new BufferedReader(new FileReader(
                    "src/main/resources/data.txt"));
            String line = reader.readLine();
            while (line != null) {
                startData.add(Double.parseDouble(line.split(" ")[0]));
                line = reader.readLine();
            }
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        double height = startData.get(7);
        double width = startData.get(8);

        GlobalData data = new GlobalData(startData.get(5),startData.get(6), (int) height, (int) width, startData.get(0), startData.get(4), startData.get(3),
                startData.get(10), startData.get(11), startData.get(9), startData.get(1), startData.get(2));
        Grid grid = new Grid(data);

        grid.createLocalMatrixes();
        grid.aggregate();


        int finalSize = data.getnWidth() * data.getnHeight();

        double[][] finalH = new double[finalSize][finalSize];
        double[][] finalC = new double[finalSize][finalSize];
        double[] t0Tab = new double[finalSize];
        for(int i=0;i<finalSize;i++){
            for(int j=0;j<finalSize;j++){
                finalH[i][j] = grid.getHG()[i][j] + grid.getCG()[i][j]/data.getStep_time();
                finalC[i][j] = grid.getCG()[i][j]/data.getStep_time();
            }

        }
        System.out.println("Time[s]\tMinTemp[C]\tMaxTemp[C]");
        for(int i=0;i<10;i++){
            for(int j=0;j<finalSize;j++){
                t0Tab[j] = grid.getNodes()[j].getT();
            }
            Matrix H = Matrix.from2DArray(finalH);
            Matrix C = Matrix.from2DArray(finalC);
            Vector temperature = Vector.fromArray(t0Tab);
            Vector P = Vector.fromArray(grid.getPG());

            Vector C_t = C.multiply(temperature);
            C_t = C_t.subtract(P);
            Vector temperatureNextStep = C_t.multiply(H.withInverter(LinearAlgebra.InverterFactory.GAUSS_JORDAN).inverse());
            for(int j=0;j<finalSize;j++){
                grid.getNodes()[j].setT(temperatureNextStep.get(j));
            }
            System.out.println((i+1)*data.getStep_time() + "\t \t" + Math.floor(temperatureNextStep.min()*1000)/1000 + "\t \t" + Math.floor(temperatureNextStep.max() * 1000)/1000);
        }

    }
}
