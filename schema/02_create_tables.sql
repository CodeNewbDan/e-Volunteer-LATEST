CREATE TABLE IF NOT EXISTS Organization (
    OrganizationID INT PRIMARY KEY AUTO_INCREMENT,
    OrgName VARCHAR(100) NOT NULL,
    RegistrationNumber VARCHAR(50) UNIQUE NOT NULL,
    OrgEmail VARCHAR(100) UNIQUE NOT NULL,
    ContactPerson VARCHAR(100),
    OrgAddress TEXT,
    OrgType VARCHAR(50) NOT NULL,  -- 'Club' or 'NGO'
    Password VARCHAR(255) NOT NULL,
    CreatedAt TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    UpdatedAt TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

SELECT '✅ Table Organization created!' AS Status;

CREATE TABLE IF NOT EXISTS Volunteer (
    VolunteerID INT PRIMARY KEY AUTO_INCREMENT,
    StudentID VARCHAR(50) UNIQUE NOT NULL,
    FullName VARCHAR(100) NOT NULL,
    Volunteer_Email VARCHAR(100) UNIQUE NOT NULL,
    PhoneNumber VARCHAR(20),
    VolunteerAddress TEXT,
    Course VARCHAR(100),
    TotalHours DECIMAL(10,2) DEFAULT 0.00,
    Password VARCHAR(255) NOT NULL,
    CreatedAt TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    UpdatedAt TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

SELECT '✅ Table Volunteer created!' AS Status;

CREATE TABLE IF NOT EXISTS Event (
    EventID INT PRIMARY KEY AUTO_INCREMENT,
    OrganizationID INT NOT NULL,
    EventName VARCHAR(150) NOT NULL,
    EventDate DATE NOT NULL,
    Location VARCHAR(150),
    EventStatus VARCHAR(50) DEFAULT 'Active',
    RequiredVolunteers INT,
    TaskDescription TEXT,
    EventVolunteerHour DECIMAL(5,2) NOT NULL,
    SecretCode VARCHAR(50) NOT NULL,
    CreatedAt TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    UpdatedAt TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (OrganizationID) REFERENCES Organization(OrganizationID) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

SELECT '✅ Table Event created!' AS Status;

CREATE TABLE IF NOT EXISTS Registration (
    RegistrationID INT PRIMARY KEY AUTO_INCREMENT,
    VolunteerID INT NOT NULL,
    EventID INT NOT NULL,
    RegistrationDate TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    AttendanceDate TIMESTAMP NULL,
    VerificationDate TIMESTAMP NULL,
    AttendanceStatus VARCHAR(50) DEFAULT 'Pending',  -- 'Pending', 'Verified', 'No-Show'
    FOREIGN KEY (VolunteerID) REFERENCES Volunteer(VolunteerID) ON DELETE CASCADE,
    FOREIGN KEY (EventID) REFERENCES Event(EventID) ON DELETE CASCADE,
    CONSTRAINT unique_volunteer_event UNIQUE (VolunteerID, EventID)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

SELECT '✅ Table Registration created!' AS Status;

SET FOREIGN_KEY_CHECKS = 1;

SHOW TABLES;

SELECT '🎉 All tables created successfully!' AS Status;
