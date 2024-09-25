package Entities;

public class Material extends Component {
    private double unitCost;
    private double quantity;
    private double transportCost;
    private double qualityCoefficient;

    public Material(String name, String componentType, double vatRate, Project project, double unitCost, double quantity, double transportCost, double qualityCoefficient) {
        super(name, componentType, vatRate, project);
        this.unitCost = unitCost;
        this.quantity = quantity;
        this.transportCost = transportCost;
        this.qualityCoefficient = qualityCoefficient;
    }

    public Material(Long id, String name, String componentType, double vatRate, Project project, double unitCost, double quantity, double transportCost, double qualityCoefficient) {
        super(id, name, componentType, vatRate, project);
        this.unitCost = unitCost;
        this.quantity = quantity;
        this.transportCost = transportCost;
        this.qualityCoefficient = qualityCoefficient;
    }

    public double getUnitCost() {
        return unitCost;
    }

    public void setUnitCost(double unitCost) {
        this.unitCost = unitCost;
    }

    public double getQuantity() {
        return quantity;
    }

    public void setQuantity(double quantity) {
        this.quantity = quantity;
    }

    public double getTransportCost() {
        return transportCost;
    }

    public void setTransportCost(double transportCost) {
        this.transportCost = transportCost;
    }

    public double getQualityCoefficient() {
        return qualityCoefficient;
    }

    public void setQualityCoefficient(double qualityCoefficient) {
        this.qualityCoefficient = qualityCoefficient;
    }

    @Override
    public String toString() {
        return super.toString() + "Material{" +
                "unitCost=" + unitCost +
                ", quantity=" + quantity +
                ", transportCost=" + transportCost +
                ", qualityCoefficient=" + qualityCoefficient +
                '}';
    }
}
