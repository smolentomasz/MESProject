public class GlobalData {
    private double height;
    private double width;
    private int nHeight; //ilosc wezlow w pionie
    private int nWidth; //ilosc wezlow w poziomie
    private int elementsAmount; // liczba elementow
    private int nodesAmount; // liczba wezlow
    private double alfa;
    private double t_oo;
    private double conductivity;
    private double density;
    private double initialTemperature;
    private double specific_heat;
    private double simulation_time;
    private double step_time;

    public GlobalData(double height, double width, int nHeight, int nWidth, double initialTemperature, double alfa, double t_oo, double conductivity, double density, double specific_heat, double simulation_time, double step_time) {
        this.height = height;
        this.width = width;
        this.nHeight = nHeight;
        this.nWidth = nWidth;
        this.elementsAmount = (nHeight - 1) * (nWidth - 1);
        this.nodesAmount = nHeight * nWidth;
        this.alfa = alfa;
        this.t_oo = t_oo;
        this.conductivity = conductivity;
        this.density = density;
        this.initialTemperature = initialTemperature;
        this.specific_heat = specific_heat;
        this.simulation_time = simulation_time;
        this.step_time = step_time;
    }

    public double getHeight() {
        return height;
    }

    public double getWidth() {
        return width;
    }

    public int getnHeight() {
        return nHeight;
    }

    public int getnWidth() {
        return nWidth;
    }

    public int getElementsAmount() {
        return elementsAmount;
    }

    public int getNodesAmount() {
        return nodesAmount;
    }

    public double getAlfa() {
        return alfa;
    }

    public double getT_oo() {
        return t_oo;
    }

    public double getConductivity() {
        return conductivity;
    }

    public double getDensity() {
        return density;
    }

    public double getInitialTemperature() {
        return initialTemperature;
    }

    public double getSpecific_heat() {
        return specific_heat;
    }

    public double getSimulation_time() {
        return simulation_time;
    }

    public double getStep_time() {
        return step_time;
    }
}
