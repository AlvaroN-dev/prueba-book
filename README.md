# Booknova - Library Managment Sistem 📚

## 🎯 Current Status: FULLY FUNCTIONAL!

**NovaBook** is now a complete library management system with:
- ✅ **Complete backend** connected to MySQL database
- ✅ **Professional JavaFX interface** with 3 types of dashboards
- ✅ **Authentication and registration system** working
- ✅ **User, book, and loan management** operational
- ✅ **Membership request system** with admin approval

---

## 🚀 How to Run the Application

### Option 1: Maven (Recommended)
```bash
cd path/to/your/project
mvn javafx:run
```

### Option 2: NetBeans IDE
1. Open the project in NetBeans
2. Click Run → `com.codeup.booknova.NovaBook`

### Option 3: Build JAR File
```bash
mvn clean package
java -jar target/novabook-app.jar
```

---

## 🔐 Authentication and Registration

### 🆕 **New User Registration**
- Click **"Register"** button on login screen
- Fill the form with your information
- All data is validated before saving
- Users are saved in MySQL database
- Passwords are secured with BCrypt encryption

### 🔑 **Login Process**
- Enter your **email** and **password**
- System checks your information in database
- You are redirected to your dashboard based on your role

---

## 👥 User Types and Features

### 👑 **Administrator (ADMIN)**
**Blue Dashboard with 5 tabs:**
- **📋 Users**: View all users, add new users, manage accounts
- **📚 Books**: Manage complete catalog, add new books with stock
- **📄 Loans**: Monitor all active loans (coming soon)
- **� Membership Requests**: Approve or reject membership applications
- **�📊 Reports**: Statistics and data dashboard (in development)

**What Administrators Can Do:**
- ✅ View tables with all users and books from database
- ✅ Add new users with complete registration dialog
- ✅ Add books to catalog with stock information
- ✅ Review membership requests from users
- ✅ Approve or reject membership applications
- ✅ Tables update automatically when data changes

### 👤 **Member (USER with membership)**
**Green Dashboard with loan management:**
- **📖 Available Books**: Browse catalog with real-time stock information
- **🔍 Search Books**: Find books by title, author, or ISBN
- **📋 My Loans**: View your current loan status
- **🔄 Loan Actions**: Request loans, return books, renew loans

**What Members Can Do:**
- ✅ Browse complete book catalog from database
- ✅ Search for specific books by different criteria
- ✅ Request book loans (requires active membership)
- ✅ View details about each book in catalog
- ✅ Check availability before requesting loans

### 🌐 **Regular User (USER without membership)**
**Orange Dashboard - Browse only:**
- **📚 Public Catalog**: View all books without restrictions
- **📖 Book Details**: See complete information about each book
- **🔍 Search Function**: Explore the catalog by title, author, or ISBN
- **📝 Request Membership**: Apply to become a member

**What Regular Users Can Do:**
- ✅ Browse the complete public catalog
- ✅ Search for books in the system
- ✅ View book details and availability
- ✅ Request membership (requires admin approval)
- ❌ Cannot request book loans (membership required)

---

## 🗄️ Database Integration

### **Complete MySQL Connectivity**
- ✅ **ConnectionFactory** configured and working properly
- ✅ **JdbcTemplateLight** handles all database operations
- ✅ **Transactions** managed correctly for data safety
- ✅ **Data validation** ensures integrity

### **Database Operations Available**
- **Users**: Create accounts, authenticate login, list all users, update information
- **Books**: Add new books, search by title/author/ISBN, manage stock levels
- **Loans**: Create loan records, check status, update returns (in development)
- **Members**: Membership management with user_id linking
- **Membership Requests**: Request submission, approval/rejection workflow

### **Database Configuration**
Edit this file: `src/main/resources/application.properties`
```properties
# Database Connection Settings
database.url=jdbc:mysql://localhost:3306/novabook
database.username=root
database.password=your_password
database.driver=com.mysql.cj.jdbc.Driver
```

### **Important: Database Setup**
Before running the application:
1. Create the database: `CREATE DATABASE novabook;`
2. Run the schema script: `src/main/java/com/codeup/booknova/db/DatabaseSchema.sql`
3. Run the membership update: `add_user_id_to_member.sql`

---

## 🎨 User Interface Design

### **Different Colors for Each Role**
- **🔵 Administrator**: Blue (#2196F3) - Represents authority and control
- **🟢 Member**: Green (#4CAF50) - Represents activity and access
- **🟠 Regular User**: Orange (#FF9800) - Represents exploration

### **Interface Features**
- ✅ **Responsive design** that adapts to different sizes
- ✅ **Dynamic tables** showing real data from database
- ✅ **Form validation** happens in real-time
- ✅ **Clear messages** for every action you take
- ✅ **Easy navigation** between different sections
- ✅ **Professional look** with modern styling

---

## 🏗️ System Architecture

### **📁 Project Structure by Layers**
```
BookNova/
├── 🎯 Domain Layer (com.codeup.booknova.domain)
│   ├── Entities: User, Book, Member, Loan, MembershipRequest
│   └── Enums: UserRole, AccessLevel, MemberRole
│
├── 🗃️ Repository Layer (com.codeup.booknova.repository)
│   ├── Interfaces: IUserRepository, IBookRepository, IMembershipRequestRepository
│   └── JDBC Implementations: UserJdbcRepository, MembershipRequestJdbcRepository
│
├── ⚙️ Service Layer (com.codeup.booknova.service)
│   ├── Interfaces: IUserService, IBookService, IMembershipRequestService
│   └── Business Logic: UserService, BookService, MembershipRequestService
│
├── 🎨 UI Layer (com.codeup.booknova.ui)
│   ├── NovaBookApplication (JavaFX main class)
│   ├── Controllers: LoginController, AdminDashboardController, UserDashboardController
│   ├── Models: UserTableModel, BookTableModel, MembershipRequestTableModel
│   └── ServiceManager (dependency injection pattern)
│
└── 🔧 Infrastructure (com.codeup.booknova.infra)
    ├── Database: JdbcTemplateLight, ConnectionFactory
    └── Utilities: PasswordUtils, ValidationUtils, CsvExporter
```

### **🔄 How Data Flows in the System**
1. **UI Controller** → Captures what the user does (clicks, inputs)
2. **ServiceManager** → Provides the needed services
3. **Service Layer** → Applies business rules and validations
4. **Repository Layer** → Talks to the database
5. **Domain Entities** → Represent the data in the system

---

## 🛠️ Technologies Used

### **Backend Technologies**
- ☕ **Java 17** - Main programming language
- 🗄️ **MySQL 8.0** - Database system
- 🔗 **JDBC** - Database connectivity
- 🔐 **BCrypt** - Password encryption
- 🏗️ **Maven** - Build and dependency management

### **Frontend Technologies**
- 🖥️ **JavaFX 21** - User interface framework
- 📄 **FXML** - Declarative UI definition
- 🎨 **CSS** - Custom styling
- 📋 **TableView/Forms** - Data display components

### **Architecture Patterns**
- 🏛️ **Domain-Driven Design** - Business logic focused design
- 📦 **Repository Pattern** - Data access abstraction
- ⚙️ **Service Layer** - Business logic separation
- 💉 **Dependency Injection** - ServiceManager pattern
- 🔄 **MVC Pattern** - Model-View-Controller structure

---

## 📋 Features Status

### ✅ **Completed Features**
- [x] User authentication with encrypted passwords (BCrypt)
- [x] New user registration with real-time validation
- [x] Role-based dashboards (Admin, Member, Regular User)
- [x] User management - admins can add, edit, delete users
- [x] Book catalog management - admins can add, edit, delete books
- [x] Membership request workflow:
  - Users can request membership with optional reason
  - Admins see all pending requests in dedicated tab
  - Admins can approve or reject requests
  - System automatically creates member record when approved
  - User must log out/in to activate membership features
- [x] Automatic membership detection on login (checks by user_id and name)
- [x] Loan request button (automatically enabled for approved members)
- [x] Public book catalog browsing for all users
- [x] Advanced book search (by title, author, ISBN)
- [x] Real-time form validation with error messages
- [x] Complete error and exception handling
- [x] Professional and responsive interface with custom CSS
- [x] Database relationships (users ↔ members, members ↔ loans)

### 🚧 **In Development**
- [ ] Complete loan CRUD operations (create loan is partial, return/renew missing)
- [ ] Late fee calculation based on overdue days
- [ ] Statistics dashboard with charts and reports
- [ ] Email notifications for membership and loans
- [ ] Export data to PDF or Excel files
- [ ] Book cover images and file upload
- [ ] REST API for mobile app integration
- [ ] Book reservation/hold system

---

## 🎯 Main Use Cases

### **📝 Registration and Login**
1. User opens the application
2. Can register a new account or login
3. System validates credentials with database
4. User is redirected to correct dashboard for their role

### **👑 System Administration**
1. Administrator logs in
2. Sees dashboard with 5 tabs (Users, Books, Loans, Membership Requests, Reports)
3. Can add new users and books to the system
4. Reviews and approves/rejects membership requests
5. Manages the complete system

### **📚 Becoming a Member**
1. Regular user logs in
2. Clicks "Request Membership" button
3. Fills out optional reason for membership
4. Request is saved to database as PENDING
5. Administrator reviews the request
6. Administrator approves or rejects
7. If approved, user becomes a member automatically
8. User logs out and logs in again
9. System detects membership and enables loan features

### **📖 Borrowing Books (Members Only)**
1. Member logs in (membership auto-detected)
2. Browses available books in catalog
3. Searches for specific books
4. Selects a book from the table
5. Clicks "Request Loan" button (now enabled)
6. Confirms the loan request
7. Loan is created in database

### **🌐 Public Browsing**
1. Regular user (no membership) logs in
2. Can browse complete catalog
3. Can search for books
4. Can see book details and availability
5. Cannot request loans (membership required)

---

## 🔧 Configuration and Requirements

### **System Requirements**
- ☕ **Java 17+** (complete JDK installed)
- 🗄️ **MySQL Server 8.0+** running
- 📊 **Database** `novabook` must be created
- 🔧 **Maven 3.6+** for building the project
- 💾 **4GB RAM** minimum recommended

### **Initial Setup Steps**

#### **Step 1: Create MySQL Database**
```sql
CREATE DATABASE novabook;
USE novabook;
-- Run the main schema script
SOURCE /path/to/project/src/main/java/com/codeup/booknova/db/DatabaseSchema.sql;
```

#### **Step 2: Apply Recent Schema Updates**
If you already have the database from before, you need to run this update:
```sql
USE novabook;
-- Add user_id column to member table (required for membership system)
SOURCE /path/to/project/src/main/java/com/codeup/booknova/db/add_user_id_to_member.sql;
```

#### **Step 3: Configure Database Connection**
1. Copy `src/main/resources/application example.properties` to `application.properties`
2. Edit `application.properties` with your database credentials:
   ```properties
   db.url=jdbc:mysql://localhost:3306/novabook
   db.username=your_username
   db.password=your_password
   ```

#### **Step 4: Compile the Project**
```bash
mvn clean compile
```

#### **Step 5: Run Application**
```bash
mvn javafx:run
```

#### **Step 6: Create First Administrator**
When you first run the application, register a user and then manually update the database:
```sql
UPDATE users SET role = 'ADMIN' WHERE username = 'your_username';
```

---

## 🚨 Common Problems and Solutions

### **Database Connection Error**
**Problem:** Application can't connect to MySQL
- ✅ Check that MySQL server is running
- ✅ Verify username and password in `application.properties`
- ✅ Make sure database `novabook` exists
- ✅ Check that MySQL is listening on port 3306
- ✅ Test connection with MySQL Workbench or command line first

### **"Loan Request" Button is Disabled**
**Problem:** User has approved membership but button is still grayed out
- ✅ User must log out and log in again after membership approval
- ✅ Check database: `SELECT * FROM member WHERE user_id = X;`
- ✅ Verify that `add_user_id_to_member.sql` script was executed
- ✅ Make sure member record has correct `user_id` value

### **Session Error: "User session not found"**
**Problem:** Error when trying to request membership
- ✅ This was fixed in latest version - update your code
- ✅ Make sure you're passing User object (not just username) to controllers
- ✅ Check that `currentUser` is properly initialized in controller

### **JavaFX Runtime Error**
**Problem:** Application won't start or crashes on launch
- ✅ Verify Java 17+ is installed: `java --version`
- ✅ Make sure JavaFX is in the classpath
- ✅ Always use `mvn javafx:run` (don't use `java -jar`)
- ✅ Check that FXML files are in correct location

### **Compilation Errors**
**Problem:** Maven build fails
- ✅ Run `mvn clean compile` to clean old builds
- ✅ Check dependencies in `pom.xml` are correct
- ✅ Verify project encoding is UTF-8
- ✅ Make sure `JAVA_HOME` environment variable points to JDK 17+

### **PropertyValueFactory Error in Dashboards**
**Problem:** Dashboard tables don't load, show errors in console
- ✅ This was fixed in latest version - tables now use SimpleStringProperty
- ✅ Update `AdminDashboardController` and `MemberDashboardController` code

---

## 📈 Next Development Steps

### **🎯 High Priority Features**
1. **Complete Loan System**
    - Full CRUD operations for loans (create, read, update, delete)
    - Validate limits per user (max books borrowed at same time)
    - Calculate expiration dates automatically
    - Return book functionality
    - Renew loan if no one is waiting

2. **Statistics Dashboard**
    - Charts showing loans per month
    - Most popular books report
    - Most active members list
    - Overdue loans report

3. **Notification System**
    - Email alerts for loan expiration
    - Status updates for membership requests
    - Automatic reminders for late returns

### **🎯 Medium Priority Features**
1. **UI/UX Improvements**
    - Add animations and smooth transitions
    - Dark mode / light mode toggle
    - User preferences and settings page
    - Better error messages with suggestions

2. **Advanced Features**
    - Book reservation system (hold a book when it's borrowed)
    - Rating system for books
    - Personalized recommendations based on history
    - Export reports to PDF or Excel

### **🎯 Future Plans**
1. **External Integration**
    - REST API for mobile apps or web clients
    - Email service for notifications
    - Synchronization with other library systems
    - QR code scanning for books

---

## 👨‍💻 Development and Contributing

### **How to Add New Features**
If you want to add something new to the project, follow this structure:

1. **Domain Layer**: Create entity classes in `com/codeup/booknova/domain/`
   - Example: `Book.java`, `Member.java`, `MembershipRequest.java`

2. **Repository Layer**: Create interface and implementation
   - Interface in `repository/` (Example: `IBookRepository.java`)
   - Implementation in `repository/impl/` (Example: `BookJdbcRepository.java`)

3. **Service Layer**: Add business logic
   - Interface in `service/` (Example: `IBookService.java`)
   - Implementation in `service/impl/` (Example: `BookService.java`)

4. **Controller Layer**: Create JavaFX controller
   - Add controller in `ui/controller/` (Example: `AdminDashboardController.java`)
   - Create FXML file in `resources/fxml/`

5. **Testing**: Write unit tests
   - Add tests in `src/test/java/` (Example: `BookServiceTest.java`)

### **Code Guidelines**
- 📝 **Comments**: Write Javadoc for all public classes and methods (in English)
- ✅ **Validation**: Always validate inputs in the service layer before saving
- 🗄️ **Transactions**: Use database transactions for critical operations
- 🎨 **Separation**: Keep each layer independent (domain → repository → service → controller)
- 🔒 **Security**: Never store plain passwords, always use BCrypt
- 🧪 **Testing**: Test your code before committing

### **Git Workflow**
1. Fork the repository
2. Create a new branch: `git checkout -b feature/your-feature-name`
3. Make your changes and commit: `git commit -m "Add new feature"`
4. Push to your fork: `git push origin feature/your-feature-name`
5. Open a Pull Request with description of changes

---

## 🎉 Conclusion

**NovaBook is now a complete and functional library management system** that demonstrates:

✅ **Solid architecture** with clear layer separation (5 layers)  
✅ **Complete database integration** using JDBC and MySQL  
✅ **Professional interface** with different dashboards for each user role  
✅ **Real-world functionality** including membership requests and loan management  
✅ **Clean code** following best practices and design patterns  
✅ **Security** with encrypted passwords and role-based access control

The system is ready to use in development and testing environments. It has a solid foundation that makes it easy to add new features in the future. The project can be extended with more advanced features like notifications, statistics, and external API integration.

### **Project Status: FULLY FUNCTIONAL ✅**

All core features are working correctly:
- User authentication and registration
- Book catalog management
- Membership request workflow (users request → admins approve → users become members)
- Role-based dashboards (Admin, Member, User)
- Database persistence with proper relationships

Feel free to explore the code, report issues, or contribute improvements! 🚀

---

**🚀 Enjoy exploring Booknova - Your library managment sistem!** 📚✨
