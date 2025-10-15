# Booknova - Library Managment Sistem 📚

## 🎯 Current Status: COMPLETLY FUNCTIONAL!

**NovaBook** now is a complete library managment sistem with:
- ✅ **Complete backend** conected to MySQL database
- ✅ **Profesional JavaFX interfase** with 3 types of dashboards
- ✅ **Autentication and register sistem** functional
- ✅ **User, book and loan managment** operational

---

## 🚀 How to Run

### Option 1: Maven (Recomended)
```bash
cd /home/tonys-dev/NetBeansProjects/NovaBook
mvn javafx:run
```

### Option 2: NetBeans IDE
1. Open the proyect in NetBeans
2. Run → `com.codeup.booknova.NovaBook`

### Option 3: Executable JAR
```bash
mvn clean package
java -jar target/novabook-app.jar
```

---

## 🔐 Autentication and Register

### 🆕 **New Users Register**
- Buton **"Register"** in the login screen
- Complete form with validation
- Users are saved in MySQL database
- Paswords hashed with BCrypt

### 🔑 **Login**
- Use your **email** and **pasword** registered
- The sistem validate against the database
- Automatic redirection for user role

---

## 👥 User Types and Funcionalities

### 👑 **Administrator (ADMIN)**
**Blue Dashboard with 4 tabs:**
- **📋 Users**: See all users, add new ones
- **📚 Books**: Manage complete catalog, add books
- **📄 Loans**: Supervise all active loans
- **📊 Reports**: Statistics dashboard (in developement)

**Active Funcionalities:**
- ✅ See tables of users and books from BD
- ✅ Add users with complete dialog
- ✅ Add books with stock form
- ✅ Automatic table update

### 👤 **Member (USER with membersip)**
**Green Dashboard with loan managment:**
- **📖 Available Books**: Catalog with real-time stock
- **🔍 Search**: By title using the database
- **📋 My Loans**: Current loans status
- **🔄 Actions**: Request, return, renew loans

**Active Funcionalities:**
- ✅ Catalog from database
- ✅ Functional search by title
- ✅ Loan request interfase

### 🌐 **Public User (USER)**
**Orange Dashboard - Only reading:**
- **📚 Public Catalog**: See all books whithout restrictions
- **📖 Details**: Complete information of each book
- **🔍 Search**: Explore the complete catalog
- **📝 Request Membersip**: Proces to become a member

---

## 🗄️ Database Integration

### **Complete MySQL Conectivity**
- ✅ **ConnectionFactory** configured and functional
- ✅ **JdbcTemplateLight** for BD operations
- ✅ **Transaccions** handeled correctly
- ✅ **Validations** of data integrity

### **Available Operations**
- **Users**: Create, autenticate, list, update
- **Books**: Add, search by title/autor, manage stock
- **Loans**: Create, consult, update status (in developement)
- **Members**: Membersip managment (base implemented)

### **BD Configuration**
File: `src/main/resources/application.properties`
```properties
# Database
database.url=jdbc:mysql://localhost:3306/novabook_db
database.username=root
database.password=your_password
database.driver=com.mysql.cj.jdbc.Driver
```

---

## 🎨 User Interfase

### **Diferent Design for Each Role**
- **🔵 Admin**: Blue (#2196F3) - Authority and control
- **🟢 Member**: Green (#4CAF50) - Activity and acces
- **🟠 User**: Orange (#FF9800) - Exploration

### **Visual Caracteristics**
- ✅ **Responsive Design** adaptable
- ✅ **Dynamic tables** with real data
- ✅ **Validated forms** in real-time
- ✅ **Informative mesages** for all actions
- ✅ **Intuitive navigation** betwen sections

---

## 🏗️ Sistem Architecture

### **📁 Layer Estructure**
```
Booknova/
├── 🎯 Domain Layer (com.codeup.booknova.domain)
│   ├── User, Book, Member, Loan (entitys)
│   └── UserRole, AccessLevel, MemberRole (enums)
│
├── 🗃️ Repository Layer (com.codeup.booknova.repository)
│   ├── Interfases: IUserRepository, IBookRepository...
│   └── JDBC Implementations: UserJdbcRepository...
│
├── ⚙️ Service Layer (com.codeup.booknova.service)
│   ├── Interfases: IUserService, IBookService...
│   └── Busines logic: UserService, BookService...
│
├── 🎨 UI Layer (com.codeup.booknova.ui)
│   ├── NovaBookApplication (JavaFX main)
│   ├── Controlers: Login, Admin, Member, User
│   ├── Models: UserTableModel, BookTableModel
│   └── ServiceManager (dependency injection)
│
└── 🔧 Infrastructure (com.codeup.booknova.infra)
    ├── JdbcTemplateLight, ConnectionFactory
    └── Configuration, Utilitys
```

### **🔄 Data Flow**
1. **UI Controler** → Capture user events
2. **ServiceManager** → Inject necesary services
3. **Service Layer** → Apply busines logic
4. **Repository Layer** → Acces to database
5. **Domain Entitys** → Represent sistem data

---

## 🛠️ Tecnologies Used

### **Backend**
- ☕ **Java 17** - Main languaje
- 🗄️ **MySQL 8.0** - Database
- 🔗 **JDBC** - Database conectivity
- 🔐 **BCrypt** - Pasword hashing
- 🏗️ **Maven** - Dependency managment

### **Frontend**
- 🖥️ **JavaFX 21** - UI framwork
- 📄 **FXML** - Declarative view definition
- 🎨 **CSS** - Custom styles
- 📋 **TableView/Forms** - Data components

### **Architecture**
- 🏛️ **Domain-Driven Design** - Domain centered desing
- 📦 **Repository Patern** - Data abstraction
- ⚙️ **Service Layer** - Busines logic
- 💉 **Dependency Injection** - ServiceManager

---

## 📋 Implemented Funcionalities

### ✅ **Completly Functional**
- [x] Autentication sistem with database
- [x] User register with validation
- [x] Diferent dashboards for each role
- [x] User managment (Admin)
- [x] Book managment (Admin)
- [x] Public book catalog
- [x] Book search by title
- [x] Form validation
- [x] Error and exception handeling
- [x] Responsive and profesional interfase

### 🚧 **In Developement**
- [ ] Complete loan managment (CRUD)
- [ ] Fine and expiration sistem
- [ ] Advansed reports and statistics
- [ ] Real-time notifications
- [ ] Data exportation (PDF/Excel)
- [ ] Book image managment
- [ ] REST API for external integration

---

## 🎯 Main Use Cases

### **📝 Register and Login**
1. User open the aplication
2. Can register or login
3. Sistem validate credentials in BD
4. Redirection to corresponding dashboard

### **👑 Sistem Administration**
1. Admin login
2. See dashboard with 4 tabs
3. Can add users and books
4. Manage the complete sistem

### **📚 Library Managment (Member)**
1. Member login
2. Explore available books catalog
3. Search specific books
4. Request loans (coming soon)

### **🌐 Public Consultation**
1. Public user login
2. Navigate complete catalog
3. See book details
4. Can request membersip

---

## 🔧 Configuration and Requeriments

### **Sistem Requeriments**
- ☕ **Java 17+** (complete JDK)
- 🗄️ **MySQL Server 8.0+**
- 📊 **Database** `novabook_db` created
- 🔧 **Maven 3.6+** for build
- 💾 **4GB RAM** minimum recomended

### **Initial Setup**
1. **Create MySQL database:**
   ```sql
   CREATE DATABASE novabook_db;
   USE novabook_db;
   -- Run schema script (src/main/resources/db/schema.sql)
   ```

2. **Configure conection:**
   Edit `src/main/resources/application.properties`

3. **Compile proyect:**
   ```bash
   mvn clean compile
   ```

4. **Run aplication:**
   ```bash
   mvn javafx:run
   ```

---

## 🚨 Problem Solutions

### **BD Conection Error**
- ✅ Verify that MySQL is runing
- ✅ Confirm credentials in application.properties
- ✅ Verify that database exist

### **JavaFX Error**
- ✅ Verify Java 17+ instaled
- ✅ Confirm JavaFX in classpath
- ✅ Use `mvn javafx:run` insted of java -jar

### **Compilation Errors**
- ✅ Run `mvn clean compile`
- ✅ Verify dependencys in pom.xml
- ✅ Confirm encoding UTF-8

---

## 📈 Next Developement Steps

### **🎯 High Priority**
1. **Complete Loan Sistem**
    - Complete CRUD of loans
    - Validation of limits per user
    - Automatic calculation of expiration dates

2. **Statistics Dashboard**
    - Loan graphics per month
    - Most popular books
    - Most active users

3. **Notification Sistem**
    - Expiration alerts
    - Request status
    - Automatic reminders

### **🎯 Medium Priority**
1. **UI/UX Improvments**
    - Animations and transitions
    - Dark/light mode
    - User configuration

2. **Advansed Funcionalities**
    - Book reservation
    - Rating sistem
    - Personalized recomendations

### **🎯 Future**
1. **External Integration**
    - REST API
    - Email service
    - Sincronization with external sistems

---

## 👨‍💻 Developement and Contribution

### **Estructure for New Funcionalities**
1. **Domain**: Add entitys in `domain/`
2. **Repository**: Create interfases and implementations
3. **Service**: Implement busines logic
4. **UI**: Create controlers and FXML views
5. **Testing**: Add unit tests

### **Code Conventions**
- 📝 **Javadoc** complete in public clases
- ✅ **Validation** in service layer
- 🗄️ **Transaccions** in critical operations
- 🎨 **Clear separation** of responsabilitys

---

## 🎉 Conclusion

**NovaBook is now a complete and functional sistem** that demonstrate:

✅ **Solid architecture** with layer separation  
✅ **Complete integration** with MySQL database  
✅ **Profesional interfase** diferent for each role  
✅ **Real funcionalities** of library managment  
✅ **Clean code** folowing best practises

The sistem is ready for use in developement and test enviroments, with a solid base for future expansions and improvments.

---

**🚀 Enjoy exploring Booknova - Your library managment sistem!** 📚✨
