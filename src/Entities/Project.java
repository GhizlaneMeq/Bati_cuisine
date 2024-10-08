package Entities;

import Entities.Enum.ProjectStatus;

import java.util.ArrayList;
import java.util.List;

public class Project {
    private Long id;
    private String name;
    private double profitMargin;
    private double totalCost;
    private ProjectStatus projectStatus;
    private Client client;
    private List<Component> components;

    public Project(String name, double profitMargin, double totalCost, ProjectStatus projectStatus, Client client) {
        this.name = name;
        this.profitMargin = profitMargin;
        this.totalCost = totalCost;
        this.projectStatus = projectStatus;
        this.client = client;
        this.components = new ArrayList<>();
    }

    public Project(Long projectId) {
    }

    public Project(Long id, String name, double profitMargin, double totalCost, ProjectStatus projectStatus, Client client) {
        this.id = id;
        this.name = name;
        this.profitMargin = profitMargin;
        this.totalCost = totalCost;
        this.projectStatus = projectStatus;
        this.client = client;
    }

    public Project(Long id, String name, double newTotalCost, ProjectStatus projectStatus, Client client) {

    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getProfitMargin() {
        return profitMargin;
    }

    public void setProfitMargin(double profitMargin) {
        this.profitMargin = profitMargin;
    }

    public double getTotalCost() {
        return totalCost;
    }

    public void setTotalCost(double totalCost) {
        this.totalCost = totalCost;
    }

    public ProjectStatus getProjectStatus() {
        return projectStatus;
    }

    public void setProjectStatus(ProjectStatus projectStatus) {
        this.projectStatus = projectStatus;
    }

    public Client getClient() {
        return client;
    }

    public void setClient(Client client) {
        this.client = client;
    }

    public List<Component> getComponents() {
        return components;
    }

    public void setComponents(List<Component> components) {
        this.components = components;
    }

    public void addComponent(Component component) {
        components.add(component);
    }

    public void removeComponent(Component component) {
        components.remove(component);
    }

    @Override
    public String toString() {
        return "Project{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", profitMargin=" + profitMargin +
                ", totalCost=" + totalCost +
                ", projectStatus=" + projectStatus +
                ", client=" + client +
                ", components=" + components +
                '}';
    }
}
