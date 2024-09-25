import Menus.*;
import Repositories.*;
import Services.*;


public class Main {
    private static ClientService clientService;
    private static ProjectService projectService;
    private static ClientRepository clientRepository;
    private static ProjectRepository projectRepository;
    private static MaterialService materialService;
    private static MaterialRepository materialRepository;
    private static LaborRepository laborRepository;
    private static LaborService laborService;
    private static QuoteRepository quoteRepository;
    private static QuoteService quoteService;
    public static void main(String[] args) {
        clientRepository = new ClientRepository();
        projectRepository = new ProjectRepository();
        materialRepository = new MaterialRepository();
        laborRepository = new LaborRepository();
        quoteRepository = new QuoteRepository();

        materialService = new MaterialService(materialRepository);
        projectService = new ProjectService(materialService,new LaborService(new LaborRepository()),projectRepository);
        clientService = new ClientService(clientRepository,projectService);
        laborService = new LaborService(laborRepository);
        quoteService = new QuoteService(quoteRepository);


        ManageClient manageClient = new ManageClient(clientService);
        ManageMaterial manageMaterial = new ManageMaterial(materialService,projectService);
        ManageLabor manageLabor = new ManageLabor(laborService,projectService);
        ManageQuote manageQuote = new ManageQuote(quoteService,projectService);
        ManageProject manageProject = new ManageProject(projectService,manageClient,manageMaterial,manageLabor,manageQuote);


        MainMenu mainMenu = new MainMenu(manageClient ,manageMaterial,manageLabor,manageProject,manageQuote);
        mainMenu.displayMainMenu();
    }
}





