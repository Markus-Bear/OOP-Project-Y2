# Media Equipment Management System (MEMS)

**Object-Oriented Software Development Project**  
by Markus Mukiiza – C00166672

![Java](https://img.shields.io/badge/Java-17-blue)
![MySQL](https://img.shields.io/badge/MySQL-8.0-orange)
![Swing](https://img.shields.io/badge/GUI-Java_Swing-lightgrey)

## Description
MEMS is a Java application for managing media equipment (cameras, projectors, microphones) in academic settings. It handles reservations, check-outs, and returns while tracking equipment conditions. Built with core OOSD principles (encapsulation, inheritance, aggregation) and MVC architecture, featuring a MySQL database backend and Java Swing GUI.

## Features
### Core Functionality
- 🔍 User-friendly search/filter for available equipment
- ⚠️ Reservation system with time-slot conflict prevention
- 📦 Check-out/return system with condition tracking
- 📊 Maintenance logging for equipment

### Role-Based Access
| Role                | Permissions                                  |
|---------------------|---------------------------------------------|
| **Lecturers/Students** | Search, reserve, view history             |
| **Media Staff**       | Manage check-outs/returns, maintenance logs |
| **Administrators**    | Full inventory/user control, access levels  |

## Technical Implementation
### OO Principles
- **Inheritance**: Abstract `User` class → `Lecturer`, `Student`, `Staff`
- **Aggregation**: 
  - Compositional: Equipment ↔ Condition/Reservation history 
  - Basic: Users ↔ Access profiles
- **Polymorphism**: Dynamic role-based behavior handling

### Architecture
- **MVC** pattern separation
- **RBAC** (Role-Based Access Control)
- Input validation with **custom error handling**
- Activity logging for anomalies

## Technologies
| Component       | Technology Stack           |
|----------------|----------------------------|
| **Backend**    | Java 17, MySQL 8.0         |
| **Frontend**   | Java Swing                 |
| **Tools**      | NetBeans IDE, Git          |

## How to Run
### Prerequisites
- JDK 17+
- MySQL Server 8.0+

### Setup Steps
1. Clone repository:  
   ```bash
   git clone https://github.com/Markus-Bear/OOP-Project-Y2.git
2. Import SQL dump to MySQL  [📁 Project(DB_Dump).sql](out/artifacts/Media_Equipment_jar)
3. Open project in NetBeans/IDE
4. Run Media-Equipment.jar [📁 Media-Equipment.jar](out/artifacts/Media_Equipment_jar)

## Future Roadmap
🖥️ Admin dashboard for bulk management

📈 Equipment condition reporting with graphs

✉️ Email notifications for overdue returns

🔍 Audit trails for reservations/maintenance

## Documentation Includes
- ER Diagram
- Database table designs
- Code snippets
- Test plans

## Author
Markus Mukiiza C00166672 
- Email: [mark.mukiiza@gmail.com]
- College Email: [c00166672@setu.ie] 

Based on Systems Analysis, Design, and Testing coursework
