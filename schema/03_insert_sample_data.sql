INSERT INTO Organization (OrgName, RegistrationNumber, OrgEmail, ContactPerson, OrgAddress, OrgType, Password) 
VALUES 
('Kelab Sukarelawan UTM', 'REG001', 'ksu@utm.my', 'Dr. Ahmad Bin Abdullah', 'Universiti Teknologi Malaysia, 81310 Skudai, Johor', 'Club', 'password123'),
('NGO Masyarakat Hijau', 'REG002', 'info@hijau.my', 'Pn. Sarah Binti Omar', 'No. 45, Jalan SS2/24, 47300 Petaling Jaya, Selangor', 'NGO', 'green123'),
('Persatuan Kebajikan Mahasiswa', 'REG003', 'pkm@utm.my', 'En. Razif Bin Ismail', 'Kolej 4, UTM Johor', 'Club', 'welfare456');

SELECT '✅ Organization data inserted!' AS Status;

INSERT INTO Volunteer (StudentID, FullName, Volunteer_Email, PhoneNumber, VolunteerAddress, Course, Password) 
VALUES 
('A20CS001', 'Ahmad Bin Ali', 'ahmad.ali@live.utm.my', '012-3456789', 'Kolej 1, UTM Johor', 'Computer Science', 'student123'),
('A20CS002', 'Siti Binti Hassan', 'siti.hassan@live.utm.my', '012-9876543', 'Kolej 2, UTM Johor', 'Software Engineering', 'siti456'),
('A20CS003', 'Muhammad Bin Rosli', 'muhammad.rosli@live.utm.my', '013-4567890', 'Kolej 3, UTM Johor', 'Data Science', 'muhammad789'),
('A20CS004', 'Nurul Binti Ahmad', 'nurul.ahmad@live.utm.my', '014-5678901', 'Kolej 4, UTM Johor', 'Information Security', 'nurul123');

SELECT '✅ Volunteer data inserted!' AS Status;

INSERT INTO Event (OrganizationID, EventName, EventDate, Location, RequiredVolunteers, TaskDescription, EventVolunteerHour, SecretCode) 
VALUES 
(1, 'Program Gotong Royong Perdana', '2026-07-15', 'Dewan Sultan Iskandar, UTM', 50, 'Membersihkan kawasan sekitar dewan, mengecat bangunan, dan landskap', 3.00, 'GOTONG2026'),
(1, 'Kempen Derma Darah', '2026-07-25', 'Fakulti Kejuruteraan, UTM', 30, 'Membantu pengurusan pendaftaran penderma dan memberi taklimat', 4.00, 'DARAH2026'),
(2, 'Kempen Kitar Semula Komuniti', '2026-07-20', 'Taman Perbandaran Johor', 40, 'Mengumpul bahan kitar semula dan memberi kesedaran kepada komuniti', 4.50, 'KITAR2026'),
(3, 'Program Bantuan Pelajar', '2026-08-01', 'Dewan Kolej 4, UTM', 20, 'Mengagihkan bantuan kepada pelajar yang memerlukan', 2.50, 'BANTU2026');

SELECT '✅ Event data inserted!' AS Status;

INSERT INTO Registration (VolunteerID, EventID, AttendanceStatus) 
VALUES 
(1, 1, 'Verified'),      -- Ahmad joined Gotong Royong - Verified
(2, 2, 'Pending'),        -- Siti joined Derma Darah - Pending
(3, 3, 'Verified'),      -- Muhammad joined Kitar Semula - Verified
(4, 1, 'Pending'),        -- Nurul joined Gotong Royong - Pending
(1, 3, 'No-Show'),       -- Ahmad joined Kitar Semula - No-Show
(2, 4, 'Verified');       -- Siti joined Bantuan Pelajar - Verified

SELECT '✅ Registration data inserted!' AS Status;

SELECT '📊 Organization Table' AS '';
SELECT * FROM Organization;

SELECT '📊 Volunteer Table' AS '';
SELECT * FROM Volunteer;

SELECT '📊 Event Table' AS '';
SELECT * FROM Event;

SELECT '📊 Registration Table' AS '';
SELECT * FROM Registration;

SELECT '🎉 All sample data inserted successfully!' AS Status;
