package Entities;

public class Labor extends Component {
    private Long id;
    private double hourlyRate;
    private double hoursWorked;
    private double workerProductivity;

    public Labor(String name, String componentType, double vatRate, Project project, double hourlyRate, double hoursWorked, double workerProductivity) {
        super(name, componentType, vatRate, project);
        this.hourlyRate = hourlyRate;
        this.hoursWorked = hoursWorked;
        this.workerProductivity = workerProductivity;
    }

    public Labor(Long id, String name, String componentType, double vatRate, Project project, double hourlyRate, double hoursWorked, double workerProductivity) {
        super(id, name, componentType, vatRate, project);
        this.id = id;
        this.hourlyRate = hourlyRate;
        this.hoursWorked = hoursWorked;
        this.workerProductivity = workerProductivity;
    }

    @Override
    public Long getId() {
        return id;
    }

    @Override
    public void setId(Long id) {
        this.id = id;
    }

    public double getHourlyRate() {
        return hourlyRate;
    }

    public void setHourlyRate(double hourlyRate) {
        this.hourlyRate = hourlyRate;
    }

    public double getHoursWorked() {
        return hoursWorked;
    }

    public void setHoursWorked(double hoursWorked) {
        this.hoursWorked = hoursWorked;
    }

    public double getWorkerProductivity() {
        return workerProductivity;
    }

    public void setWorkerProductivity(double workerProductivity) {
        this.workerProductivity = workerProductivity;
    }
}
