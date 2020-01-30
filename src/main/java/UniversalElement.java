public class UniversalElement {
    private int size = 4;
    private LocalPoint[] gridPoints = new LocalPoint[size];
    private double[][] tabKsi = new double[size][size];
    private double[][] tabEta = new double[size][size];
    private double[][] tabN = new double[size][size];

    public UniversalElement() {
        gridPoints[0] = new LocalPoint(-1.0 / Math.sqrt(3), -1.0 / Math.sqrt(3));
        gridPoints[1] = new LocalPoint(1.0 / Math.sqrt(3), -1.0 / Math.sqrt(3));
        gridPoints[2] = new LocalPoint(1.0 / Math.sqrt(3), 1.0 / Math.sqrt(3));
        gridPoints[3] = new LocalPoint(-1.0 / Math.sqrt(3), 1.0 / Math.sqrt(3));

        createArrays();
    }

    public double[][] getTabKsi() {
        return tabKsi;
    }

    public double[][] getTabEta() {
        return tabEta;
    }

    public double[][] getTabN() {
        return tabN;
    }

    private void createArrays(){
        for(int i=0;i<size;i++){
            tabKsi[i][0] = dN1dKsi(gridPoints[i].getEta());
            tabKsi[i][1] = dN2dKsi(gridPoints[i].getEta());
            tabKsi[i][2] = dN3dKsi(gridPoints[i].getEta());
            tabKsi[i][3] = dN4dKsi(gridPoints[i].getEta());

            tabEta[i][0] = dN1dEta(gridPoints[i].getKsi());
            tabEta[i][1] = dN2dEta(gridPoints[i].getKsi());
            tabEta[i][2] = dN3dEta(gridPoints[i].getKsi());
            tabEta[i][3] = dN4dEta(gridPoints[i].getKsi());

            tabN[i][0] = N1(gridPoints[i]);
            tabN[i][1] = N2(gridPoints[i]);
            tabN[i][2] = N3(gridPoints[i]);
            tabN[i][3] = N4(gridPoints[i]);
        }
    }
    public double dXdKsi(Node[] nodes, LocalPoint localPoint){
        return dN1dKsi(localPoint.getEta()) * nodes[0].getX() + dN2dKsi(localPoint.getEta()) * nodes[1].getX() +
                dN3dKsi(localPoint.getEta()) * nodes[2].getX() + dN4dKsi(localPoint.getEta()) * nodes[3].getX();
    }
    private double dXdEta(Node[] nodes, LocalPoint localPoint){
        return dN1dEta(localPoint.getKsi()) * nodes[0].getX() + dN2dEta(localPoint.getKsi()) * nodes[1].getX() +
                dN3dEta(localPoint.getKsi()) * nodes[2].getX() + dN4dEta(localPoint.getKsi()) * nodes[3].getX();
    }
    private double dYdKsi(Node[] nodes, LocalPoint localPoint){
        return dN1dKsi(localPoint.getEta()) * nodes[0].getY() + dN2dKsi(localPoint.getEta()) * nodes[1].getY() +
                dN3dKsi(localPoint.getEta()) * nodes[2].getY() + dN4dKsi(localPoint.getEta()) * nodes[3].getY();
    }
    private double dYdEta(Node[] nodes, LocalPoint localPoint){
        return dN1dEta(localPoint.getKsi()) * nodes[0].getY() + dN2dEta(localPoint.getKsi()) * nodes[1].getY() +
                dN3dEta(localPoint.getKsi()) * nodes[2].getY() + dN4dEta(localPoint.getKsi()) * nodes[3].getY();
    }
    private double dN1dKsi(double eta){
        return -0.25 * (1 - eta);
    }
    private double dN2dKsi(double eta){
        return 0.25 * (1 - eta);
    }
    private double dN3dKsi(double eta){
        return 0.25 * (1 + eta);
    }
    private double dN4dKsi(double eta){
        return -0.25 * (1 + eta);
    }
    private double dN1dEta(double ksi){
        return -0.25 * (1 - ksi);
    }
    private double dN2dEta(double ksi){
        return -0.25 * (1 + ksi);
    }
    private double dN3dEta(double ksi){
        return 0.25 * (1 + ksi);
    }
    private double dN4dEta(double ksi){
        return 0.25 * (1 - ksi);
    }
    private double N1(LocalPoint localPoint){
        return 0.25 * (1 - localPoint.getKsi()) * (1 - localPoint.getEta());
    }
    private double N2(LocalPoint localPoint){
        return 0.25 * (1 + localPoint.getKsi()) * (1 - localPoint.getEta());
    }
    private double N3(LocalPoint localPoint){
        return 0.25 * (1 + localPoint.getKsi()) * (1 + localPoint.getEta());
    }
    private double N4(LocalPoint localPoint){
        return 0.25 * (1 - localPoint.getKsi()) * (1 + localPoint.getEta());
    }
    private double  dNidX(Node[] nodes, int pointNumber, int shapeNumber){
        LocalPoint integrationPoint = gridPoints[pointNumber - 1];
        return 1.0/detJ(nodes, pointNumber) * ((dYdEta(nodes, integrationPoint) * tabKsi[pointNumber - 1][shapeNumber - 1]) -
                (dYdKsi(nodes, integrationPoint) * tabEta[pointNumber - 1][shapeNumber - 1]));
    }
    private double dNidY(Node[] nodes, int pointNumber, int shapeNumber){
        LocalPoint integrationPoint = gridPoints[pointNumber - 1];
        return 1.0/detJ(nodes, pointNumber) * ((dXdKsi(nodes, integrationPoint) * tabEta[pointNumber - 1][shapeNumber - 1]) -
                (dXdEta(nodes, integrationPoint) * tabKsi[pointNumber - 1][shapeNumber - 1]));
    }
    private double[][] Hi(Node[] nodes, int pointNumber){
        int size = 4;
        double[][] tabHi = new double[size][size];

        for(int i=0;i<size;i++){
            for(int j=0;j<size;j++){
                tabHi[i][j] = dNidX(nodes, pointNumber, j+1) * dNidX(nodes, pointNumber, i+1) +
                        dNidY(nodes, pointNumber, j+1) * dNidY(nodes, pointNumber, i+1);
            }
        }
        return tabHi;
    }
    public double detJ(Node[] nodes, int pointNumber){
        LocalPoint integrationPoint = gridPoints[pointNumber - 1];
        return dXdKsi(nodes, integrationPoint) * dYdEta(nodes, integrationPoint) - dYdKsi(nodes, integrationPoint) * dXdEta(nodes, integrationPoint);
    }
    public double[][] H(Node[] nodes, double k_t){
        int size = 4;
        double w = 1;
        double[][] tabH = new double[size][size];

        for(int i=0;i<size;i++){
            for(int j=0;j<size;j++){
                tabH[i][j] = 0;
            }
        }
        for(int i=0;i<size;i++){
            for(int j=0;j<size;j++){
                tabH[i][j] = k_t * (Hi(nodes, 1)[i][j] * w * w * detJ(nodes, 1) + Hi(nodes,2)[i][j] * w * w * detJ(nodes, 2) +
                        Hi(nodes, 3)[i][j] * w * w * detJ(nodes, 3) + Hi(nodes, 4)[i][j] * w * w * detJ(nodes, 4));
            }
        }

        return tabH;
    }
    public double[][] HBc(Node[] nodes, double alfa){
        int size = 4;
        double w = 1;
        double detJ1D;
        double[][] tabHBc = new double[size][size];

        for(int i=0;i<size;i++){
            for(int j=0;j<size;j++){
                tabHBc[i][j] = 0;
            }
        }

        if(nodes[0].isBC() && nodes[1].isBC()){
            LocalPoint pkt1 = new LocalPoint(-1.0 / Math.sqrt(3), -1);
            LocalPoint pkt2 = new LocalPoint(1.0 / Math.sqrt(3), -1);

            detJ1D = 0.5 * Math.sqrt(Math.pow(nodes[1].getX() - nodes[0].getX(),2) + Math.pow(nodes[1].getY() - nodes[0].getY(),2));

            double[] shapeVector1 = new double[size];
            shapeVector1 = fillVector(shapeVector1, pkt1);

            double[] shapeVector2 = new double[size];
            shapeVector2 = fillVector(shapeVector2, pkt2);

            for(int i=0;i<size;i++){
                for(int j=0;j<size;j++){
                    tabHBc[i][j] += (shapeVector1[i] * shapeVector1[j] * detJ1D * w + shapeVector2[i] * shapeVector2[j] * detJ1D * w);
                }
            }

        }// krawedz dolna
        if(nodes[1].isBC() && nodes[2].isBC()){
            LocalPoint pkt1 = new LocalPoint(1, -1.0 / Math.sqrt(3));
            LocalPoint pkt2 = new LocalPoint(1, 1.0 / Math.sqrt(3));

            detJ1D = 0.5 * Math.sqrt(Math.pow(nodes[2].getX() - nodes[1].getX(),2) + Math.pow(nodes[2].getY() - nodes[1].getY(),2));

            double[] shapeVector1 = new double[size];
            shapeVector1 = fillVector(shapeVector1, pkt1);

            double[] shapeVector2 = new double[size];
            shapeVector2 = fillVector(shapeVector2, pkt2);

            for(int i=0;i<size;i++){
                for(int j=0;j<size;j++){
                    tabHBc[i][j] += (shapeVector1[i] * shapeVector1[j] * detJ1D * w + shapeVector2[i] * shapeVector2[j] * detJ1D * w);
                }
            }
        }// krawedz prawa
        if(nodes[2].isBC() && nodes[3].isBC()){
            LocalPoint pkt1 = new LocalPoint(1.0 / Math.sqrt(3), 1);
            LocalPoint pkt2 = new LocalPoint(-1.0 / Math.sqrt(3), 1);

            detJ1D = 0.5 * Math.sqrt(Math.pow(nodes[2].getX() - nodes[3].getX(),2) + Math.pow(nodes[2].getY() - nodes[3].getY(),2));

            double[] shapeVector1 = new double[size];
            shapeVector1 = fillVector(shapeVector1, pkt1);

            double[] shapeVector2 = new double[size];
            shapeVector2 = fillVector(shapeVector2, pkt2);

            for(int i=0;i<size;i++){
                for(int j=0;j<size;j++){
                    tabHBc[i][j] += (shapeVector1[i] * shapeVector1[j] * detJ1D * w + shapeVector2[i] * shapeVector2[j] * detJ1D * w);
                }
            }

        }// krawedz gorna
        if(nodes[3].isBC() && nodes[0].isBC()){
            LocalPoint pkt1 = new LocalPoint(-1, 1.0 / Math.sqrt(3));
            LocalPoint pkt2 = new LocalPoint(-1, -1.0 / Math.sqrt(3));

            detJ1D = 0.5 * Math.sqrt(Math.pow(nodes[3].getX() - nodes[0].getX(),2) + Math.pow(nodes[3].getY() - nodes[0].getY(),2));

            double[] shapeVector1 = new double[size];
            shapeVector1 = fillVector(shapeVector1, pkt1);

            double[] shapeVector2 = new double[size];
            shapeVector2 = fillVector(shapeVector2, pkt2);

            for(int i=0;i<size;i++){
                for(int j=0;j<size;j++){
                    tabHBc[i][j] += (shapeVector1[i] * shapeVector1[j] * detJ1D * w + shapeVector2[i] * shapeVector2[j] * detJ1D * w);
                }
            }

        }//krawedz lewa

        for(int i=0;i<size;i++){
            for(int j=0;j<size;j++){
                tabHBc[i][j] *= alfa;
            }
        }

        return tabHBc;
    }
    private double[] fillVector(double[] vector, LocalPoint pkt){
        vector[0] = N1(pkt);
        vector[1] = N2(pkt);
        vector[2] = N3(pkt);
        vector[3] = N4(pkt);

        return vector;
    }
    public double[][] C(Node[] nodes, double conductivity, double density){
        int size = 4;
        double w = 1;
        double[][] tabC = new double[size][size];

        for(int i=0;i<size;i++){
            for(int j=0;j<size;j++){
                tabC[i][j] = 0;
            }
        }
        for(int i=0;i<size;i++){
            for(int j=0;j<size;j++){
                tabC[i][j] = tabN[0][i] * tabN[0][j] * w * w * detJ(nodes, 1) + tabN[1][i] * tabN[1][j] * w * w * detJ(nodes, 2) +
                        tabN[2][i] * tabN[2][j] * w * w * detJ(nodes, 3) + tabN[3][i] * tabN[3][j] * w * w * detJ(nodes, 4);
            }
        }

        for(int i=0;i<size;i++){
            for(int j=0;j<size;j++){
                tabC[i][j] *= (conductivity * density);
            }
        }
        return tabC;
    }
    public double[] vectorP(Node[] nodes, double alfa, double t_oo){
        int size = 4;
        double w = 1;
        double detJ1D;
        double[] tabP = new double[size];

        for(int i=0;i<size;i++){
            tabP[i] = 0;
        }
        if(nodes[0].isBC() && nodes[1].isBC()){
            LocalPoint pkt1 = new LocalPoint(-1.0 / Math.sqrt(3), -1);
            LocalPoint pkt2 = new LocalPoint(1.0 / Math.sqrt(3), -1);
            detJ1D = 0.5 * Math.sqrt(Math.pow(nodes[1].getX() - nodes[0].getX(),2) + Math.pow(nodes[1].getY() - nodes[0].getY(),2));

            double[] shapeVector1 = new double[size];
            shapeVector1 = fillVector(shapeVector1, pkt1);

            double[] shapeVector2 = new double[size];
            shapeVector2 = fillVector(shapeVector2, pkt2);

            for(int i=0;i<size;i++){
                tabP[i] += ((shapeVector1[i] + shapeVector2[i]) * detJ1D * w);
            }
        }
        if(nodes[1].isBC() && nodes[2].isBC()){
            LocalPoint pkt1 = new LocalPoint(1, -1.0 / Math.sqrt(3));
            LocalPoint pkt2 = new LocalPoint(1, 1.0 / Math.sqrt(3));

            detJ1D = 0.5 * Math.sqrt(Math.pow(nodes[2].getX() - nodes[1].getX(),2) + Math.pow(nodes[2].getY() - nodes[1].getY(),2));

            double[] shapeVector1 = new double[size];
            shapeVector1 = fillVector(shapeVector1, pkt1);

            double[] shapeVector2 = new double[size];
            shapeVector2 = fillVector(shapeVector2, pkt2);

            for(int i=0;i<size;i++){
                tabP[i] += ((shapeVector1[i] + shapeVector2[i]) * detJ1D * w);
            }
        }
        if(nodes[2].isBC() && nodes[3].isBC()){
            LocalPoint pkt1 = new LocalPoint(1.0 / Math.sqrt(3), 1);
            LocalPoint pkt2 = new LocalPoint(-1.0 / Math.sqrt(3), 1);

            detJ1D = 0.5 * Math.sqrt(Math.pow(nodes[2].getX() - nodes[3].getX(),2) + Math.pow(nodes[2].getY() - nodes[3].getY(),2));

            double[] shapeVector1 = new double[size];
            shapeVector1 = fillVector(shapeVector1, pkt1);

            double[] shapeVector2 = new double[size];
            shapeVector2 = fillVector(shapeVector2, pkt2);

            for(int i=0;i<size;i++){
                tabP[i] += ((shapeVector1[i] + shapeVector2[i]) * detJ1D * w);
            }

        }
        if(nodes[3].isBC() && nodes[0].isBC()){
            LocalPoint pkt1 = new LocalPoint(-1, 1.0 / Math.sqrt(3));
            LocalPoint pkt2 = new LocalPoint(-1, -1.0 / Math.sqrt(3));

            detJ1D = 0.5 * Math.sqrt(Math.pow(nodes[3].getX() - nodes[0].getX(),2) + Math.pow(nodes[3].getY() - nodes[0].getY(),2));

            double[] shapeVector1 = new double[size];
            shapeVector1 = fillVector(shapeVector1, pkt1);

            double[] shapeVector2 = new double[size];
            shapeVector2 = fillVector(shapeVector2, pkt2);

            for(int i=0;i<size;i++){
                tabP[i] += ((shapeVector1[i] + shapeVector2[i]) * detJ1D * w);
            }
        }
        for(int i=0;i<size;i++){
            tabP[i] *= -alfa * t_oo;
        }
        return tabP;
    }
    public double[][] H_HBcAggregate(Node[] nodes, double conductivity, double alfa){
        double[][] H = H(nodes, conductivity);
        double[][] HBc = HBc(nodes, alfa);
        for(int i=0;i<size;i++){
            for(int j=0;j<size;j++){
                H[i][j] += HBc[i][j];
            }
        }
        return H;
    }
}
