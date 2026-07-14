DROP DATABASE IF EXISTS alumni_project;

CREATE DATABASE alumni_project;
USE alumni_project;

CREATE TABLE users (
    user_id INT AUTO_INCREMENT PRIMARY KEY,
    email VARCHAR(100) UNIQUE NOT NULL,
    password VARCHAR(100)  NOT NULL,
    role ENUM(
        'student',
        'alumni',
        'IT_ADMIN',
        'PROFESSOR',
        'PLACEMENT',
        'HOD',
        'APO_ADMIN'
    ) NOT NULL
);

CREATE TABLE students (
    student_id INT AUTO_INCREMENT PRIMARY KEY,
    user_id INT,
    name VARCHAR(100),
    sap_id VARCHAR(20) UNIQUE NOT NULL,
    course VARCHAR(100),
    batch VARCHAR(20),
    specialization ENUM(
        'core',
        'big data',
        'cyber security',
        'AIML',
        'Devops',
        'Frontend development',
        'Data science'
    ),
    FOREIGN KEY (user_id) REFERENCES users(user_id)
);

CREATE TABLE alumni (
    alumni_id INT AUTO_INCREMENT PRIMARY KEY,
    user_id INT,
    name VARCHAR(100),
    old_sap_id VARCHAR(20) UNIQUE,
    current_email VARCHAR(100) UNIQUE NOT NULL,
    current_company VARCHAR(100),
    job_role VARCHAR(100),
    address VARCHAR(200),
    specialization ENUM(
        'core',
        'big data',
        'cyber security',
        'AIML',
        'Devops',
        'Frontend development',
        'Data science'
    ),
    FOREIGN KEY (user_id) REFERENCES users(user_id)
);

CREATE TABLE admins (
    admin_id INT AUTO_INCREMENT PRIMARY KEY,
    user_id INT,
    name VARCHAR(100),
    employee_id VARCHAR(30) UNIQUE,
    department VARCHAR(100),
    FOREIGN KEY (user_id) REFERENCES users(user_id)
);


CREATE TABLE comments (
    comment_id INT AUTO_INCREMENT PRIMARY KEY,
    post_author VARCHAR(100),
    comment_text VARCHAR(500),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

INSERT INTO users (email, password, role) VALUES
('naina.19001@stu.upes.ac.in','stuPass101','student'),
('dev.19002@stu.upes.ac.in','stuPass102','student'),
('tara.19003@stu.upes.ac.in','stuPass103','student'),
('kanishka.19004@stu.upes.ac.in','stuPass104','student'),
('isha.19005@stu.upes.ac.in','stuPass105','student'),
('vishal.19006@stu.upes.ac.in','stuPasxs106','student'),
('misha.19007@stu.upes.ac.in','stuPass107','student'),
('aryan.19008@stu.upes.ac.in','stuPass108','student'),
('sanya.19009@stu.upes.ac.in','stuPass109','student'),
('rudra.19010@stu.upes.ac.in','stuPass110','student');

INSERT INTO students 
(user_id, name, sap_id, course, batch, specialization) VALUES
(1, 'Naina', '610019001', 'BTech CSE', '2026', 'AIML'),
(2, 'Dev', '610019002', 'BTech CSE', '2026', 'core'),
(3, 'Tara', '610019003', 'BTech CSE', '2026', 'big data'),
(4, 'Kanishka', '610019004', 'BTech CSE', '2026', 'cyber security'),
(5, 'Isha', '610019005', 'BTech CSE', '2026', 'Frontend development'),
(6, 'Vishal', '610019006', 'BTech CSE', '2026', 'Devops'),
(7, 'Misha', '610019007', 'BTech CSE', '2026', 'Data science'),
(8, 'Aryan', '610019008', 'BTech CSE', '2026', 'AIML'),
(9, 'Sanya', '610019009', 'BTech CSE', '2026', 'cyber security'),
(10, 'Rudra', '610019010', 'BTech CSE', '2026', 'Frontend development');

INSERT INTO users (email, password, role) VALUES
('aditya.18001@alumni.upes.ac.in','alumPass201','alumni'),
('kiara.18002@alumni.upes.ac.in','alumPass202','alumni'),
('vihaan.18003@alumni.upes.ac.in','alumPass203','alumni'),
('myra.18004@alumni.upes.ac.in','alumPass204','alumni'),
('reyan.18005@alumni.upes.ac.in','alumPass205','alumni'),
('anika.18006@alumni.upes.ac.in','alumPass206','alumni'),
('ishaan.18007@alumni.upes.ac.in','alumPass207','alumni'),
('navya.18008@alumni.upes.ac.in','alumPass208','alumni'),
('daksh.18009@alumni.upes.ac.in','alumPass209','alumni'),
('zoya.18010@alumni.upes.ac.in','alumPass210','alumni');

INSERT INTO alumni 
(user_id, name, old_sap_id, current_email, current_company, job_role, address, specialization) VALUES
(11, 'Aditya', '610018001', 'aditya.work@gmail.com', 'TCS', 'Software Developer', 'Delhi', 'core'),
(12, 'Kiara', '610018002', 'kiara.work@gmail.com', 'Infosys', 'Data Analyst', 'Mumbai', 'Data science'),
(13, 'Vihaan', '610018003', 'vihaan.work@gmail.com', 'Wipro', 'Cyber Security Analyst', 'Bangalore', 'cyber security'),
(14, 'Myra', '610018004', 'myra.work@gmail.com', 'HCL', 'Frontend Developer', 'Noida', 'Frontend development'),
(15, 'Reyan', '610018005', 'reyan.work@gmail.com', 'Accenture', 'DevOps Engineer', 'Pune', 'Devops'),
(16, 'Anika', '610018006', 'anika.work@gmail.com', 'IBM', 'Big Data Engineer', 'Hyderabad', 'big data'),
(17, 'Ishaan', '610018007', 'ishaan.work@gmail.com', 'Amazon', 'AI Engineer', 'Chennai', 'AIML'),
(18, 'Navya', '610018008', 'navya.work@gmail.com', 'Deloitte', 'Business Analyst', 'Gurgaon', 'Data science'),
(19, 'Daksh', '610018009', 'daksh.work@gmail.com', 'Capgemini', 'Backend Developer', 'Jaipur', 'core'),
(20, 'Zoya', '610018010', 'zoya.work@gmail.com', 'Cognizant', 'Frontend Engineer', 'Kolkata', 'Frontend development');

INSERT INTO users (email, password, role) VALUES
('mahesh@ddn.upes.ac.in','adminPass301','IT_ADMIN'),
('ridhima@ddn.upes.ac.in','adminPass302','APO_ADMIN'),
('suresh@ddn.upes.ac.in','adminPass303','PROFESSOR'),
('latika@ddn.upes.ac.in','adminPass304','PLACEMENT'),
('omkar@ddn.upes.ac.in','adminPass305','HOD'),
('prerna@ddn.upes.ac.in','adminPass306','PROFESSOR'),
('tarun@ddn.upes.ac.in','adminPass307','IT_ADMIN'),
('meenal@ddn.upes.ac.in','adminPass308','PROFESSOR'),
('rakesh@ddn.upes.ac.in','adminPass309','APO_ADMIN'),
('nandini@ddn.upes.ac.in','adminPass310','PLACEMENT');

INSERT INTO admins 
(user_id, name, employee_id, department) VALUES
(21, 'Mahesh', 'EMP201', 'IT Department'),
(22, 'Ridhima', 'EMP202', 'APO Office'),
(23, 'Suresh', 'EMP203', 'CSE Department'),
(24, 'Latika', 'EMP204', 'Placement Cell'),
(25, 'Omkar', 'EMP205', 'HOD Office'),
(26, 'Prerna', 'EMP206', 'CSE Department'),
(27, 'Tarun', 'EMP207', 'IT Department'),
(28, 'Meenal', 'EMP208', 'CSE Department'),
(29, 'Rakesh', 'EMP209', 'APO Office'),
(30, 'Nandini', 'EMP210', 'Placement Cell');