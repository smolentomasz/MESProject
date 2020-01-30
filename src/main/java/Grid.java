import java.util.Arrays;

public class Grid {
    private Node[] nodes;
    private Element[] elements;

    private double[] PG;
    private double[][] HG;
    private double[][] CG;

    int elementsAmount;
    int nodesAmount;
    private double alfa;
    private double t_oo;
    private UniversalElement universalElement;

    public double[] getPG() {
        return PG;
    }

    public double[][] getHG() {
        return HG;
    }

    public double[][] getCG() {
        return CG;
    }

    public Element[] getElements() {
        return elements;
    }

    public Node[] getNodes() {
        return nodes;
    }

    Grid(GlobalData globalData){
        double height = globalData.getHeight();
        double width = globalData.getWidth();

        int nHeight = globalData.getnHeight();
        int nWidth = globalData.getnWidth();

        this.elementsAmount = globalData.getElementsAmount();
        this.nodesAmount = globalData.getNodesAmount();
        this.alfa = globalData.getAlfa();
        double conductivity = globalData.getConductivity();
        double density = globalData.getDensity();
        this.t_oo = globalData.getT_oo();

        double initialTemperature = globalData.getInitialTemperature();
        double specific_heat = globalData.getSpecific_heat();
        double simulation_time = globalData.getSimulation_time();
        double step_tiem = globalData.getStep_time();

        this.universalElement = new UniversalElement();
        this.nodes = new Node[nodesAmount];
        this.elements = new Element[elementsAmount];

        for(int i=0;i<nodesAmount;i++){
            nodes[i] = new Node();
        }
        for(int i=0;i<elementsAmount;i++){
            elements[i] = new Element();
        }

        PG = new double[nodesAmount];
        HG = new double[nodesAmount][nodesAmount];
        CG = new double[nodesAmount][nodesAmount];
        //ustawienie wezlow

        double x = width/(nWidth - 1);
        double y = height/(nHeight - 1);

        int n = 0;
        for(int i=1;i<=nWidth;i++){
            for(int j=1;j<=nHeight;j++){
                nodes[n].setX((i-1)*x);
                nodes[n].setY((j-1)*y);
                nodes[n].setBC(height, width);
                nodes[n].setT(initialTemperature);
                n++;
            }
        }

        //ustawienie elementów

        int el = 1;
        for(int i=0;i<elementsAmount;i++){
            elements[i].setID(el, nHeight);
            elements[i].setConductivity(conductivity);
            elements[i].setDensity(density);
            elements[i].setSpecific_heat(specific_heat);
            el++;
            if(el % nHeight == 0)
                el++;
        }
    }
    public void getInformationAboutElement(int number){
        for(int i=0;i<4;i++){
            System.out.println("ID: " + elements[number - 1].getID()[i] + ", wspolrzedne: X: " + nodes[elements[number - 1].getID()[i] - 1].getX() + " Y: " + nodes[elements[number - 1].getID()[i] - 1].getY() + "\n");
        }
    }
    public Node[] getElementNodes(int number){
        int ID1 = elements[number - 1].getID()[0];
        int ID2 = elements[number - 1].getID()[1];
        int ID3 = elements[number - 1].getID()[2];
        int ID4 = elements[number - 1].getID()[3];

        Node[] selectedNodes = new Node[4];

        selectedNodes[0] = nodes[ID1 - 1];
        selectedNodes[1] = nodes[ID2 - 1];
        selectedNodes[2] = nodes[ID3 - 1];
        selectedNodes[3] = nodes[ID4 - 1];

        return selectedNodes;
    }
    public void createLocalMatrixes(){
        Node[] localNodes = new Node[4];
        for(int i=0;i<elementsAmount;i++){
            for(int j=0;j<4;j++){
                localNodes[j] = nodes[elements[i].getID()[j] - 1];
            }
            elements[i].setHL(universalElement.H_HBcAggregate(localNodes, elements[i].getConductivity(), alfa));
            elements[i].setCL(universalElement.C(localNodes, elements[i].getSpecific_heat(), elements[i].getDensity()));
            elements[i].setPL(universalElement.vectorP(localNodes, alfa, t_oo));
        }

    }
    public void aggregate(){
        for(int i=0;i<elementsAmount;i++){
            int[] ID = new int[4];

            for(int j=0;j<4;j++){
                ID[j] = elements[i].getID()[j];
            }
            for(int k=0;k<4;k++){
                PG[ID[k] - 1] += elements[i].PL[k];
                for(int j =0;j<4;j++){
                    HG[ID[j] - 1][ID[k] - 1] += elements[i].HL[j][k];
                    CG[ID[j] - 1][ID[k] - 1] += elements[i].CL[j][k];
                }
            }
        }
    }
}
