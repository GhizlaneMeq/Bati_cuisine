CREATE TYPE project_status AS ENUM ('InProgress', 'Completed', 'Cancelled');

-- Table clients
CREATE TABLE clients (
                         id SERIAL PRIMARY KEY,
                         name VARCHAR(100) NOT NULL,
                         address VARCHAR(255),
                         phone VARCHAR(50),
                         isProfessional BOOLEAN
);

-- Table projects
CREATE TABLE projects (
                          id SERIAL PRIMARY KEY,
                          name VARCHAR(255) NOT NULL,
                          profitMargin DOUBLE PRECISION,
                          totalCost DOUBLE PRECISION,
                          projectStatus project_status,
                          client_id INTEGER REFERENCES clients(id) ON DELETE SET NULL
);

-- Table des composants générique (matériaux et main-d'œuvre)
CREATE TABLE components (
                            id SERIAL PRIMARY KEY,
                            name VARCHAR(100) NOT NULL,
                            componentType VARCHAR(255),
                            vatRate DOUBLE PRECISION NOT NULL,
                            project_id INTEGER REFERENCES projects(id) ON DELETE CASCADE
);

-- Table
CREATE TABLE materials (
                           unitCost DOUBLE PRECISION NOT NULL,
                           quantity DOUBLE PRECISION NOT NULL,
                           transportCost DOUBLE PRECISION NOT NULL,
                           qualityCoefficient DOUBLE PRECISION NOT NULL
) INHERITS (components);

-- Table main-d'oeuvre
CREATE TABLE labor (
                       hourlyRate DOUBLE PRECISION NOT NULL,
                       hoursWorked DOUBLE PRECISION NOT NULL,
                       workerProductivity DOUBLE PRECISION NOT NULL
) INHERITS (components);

-- Table des devis
CREATE TABLE quotes (
                        id SERIAL PRIMARY KEY,
                        estimatedAmount DOUBLE PRECISION NOT NULL,
                        issueDate DATE NOT NULL,
                        validityDate DATE NOT NULL,
                        isAccepted BOOLEAN,
                        project_id INTEGER REFERENCES projects (id) ON DELETE CASCADE
);
