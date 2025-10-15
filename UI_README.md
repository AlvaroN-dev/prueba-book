# Booknova - JavaFX User Interfase

## 🎯 Descripcion

Modern and profesional user interfase maked in JavaFX for the NovaBook library managment sistem. It give diferent views for each user role with especific funcionalities.

## 🚀 Main Caracteristics

### 🔐 Login Sistem
- Simple autentication with demo credentials
- Automatic redirection for user role
- Field validation and error/succes mesages
- Clean and profesional interfase

### 👑 Administrator Panel
- **User Managment**: Create, edit and manage user acounts
- **Book Managment**: Manage the complete book catalog
- **Loan Managment**: Supervise and manage all the loans
- **Reports**: Dashboard with statistics and sistem reports

### 👤 Member Panel
- **Book Catalog**: Explore books availables for loan
- **Request Loans**: Make loan request of books
- **My Loans**: Manage active loans
- **Renovations**: Renew loans when is posible
- **Search**: Find specific books in the catalog

### 🌐 Public User Panel
- **Public Catalog**: Explore the complete catalog whithout restrictions
- **Book Details**: See detailed information of each book
- **Advansed Search**: Search by title, autor, genre, etc.
- **Request Membersip**: Proces to become a member

## 🎮 Demo Credentials

| Role | User | Pasword | Description |
|-----|---------|------------|-------------|
| Admin | `admin` | `admin123` | Complete acces to the sistem |
| Member | `member` | `member123` | Member acces with loans |
| User | `user` | `user123` | Public acces to catalog |

## 🎨 Design and Styles

### Color Palet
- **Admin**: Blue (#2196F3) - Represent authority and control
- **Member**: Green (#4CAF50) - Represent activity and acces
- **User**: Orange (#FF9800) - Represent exploration and discoveri
- **Accent**: Red (#FF5722) - For importants actions like logout

### Visual Caracteristics
- Responsive and modern desing
- Descriptive and user-friendly icons
- Diferent colors for each role
- Hover effects and smooth transitions
- Tables and forms well organized

## 📁 Files Estructure

```
src/main/
├── java/com/codeup/booknova/ui/
│   ├── NovaBookApplication.java          # Main JavaFX clas
│   └── controller/
│       ├── LoginController.java          # Login controler
│       ├── AdminDashboardController.java # Admin controler
│       ├── MemberDashboardController.java # Member controler
│       └── UserDashboardController.java  # User controler
└── resources/
    ├── fxml/
    │   ├── login.fxml                    # Login view
    │   ├── admin-dashboard.fxml          # Admin dashboard
    │   ├── member-dashboard.fxml         # Member dashboard
    │   └── user-dashboard.fxml           # User dashboard
    └── styles/
        └── application.css               # Global CSS styles
```

## 🛠️ Tecnologies Used

- **JavaFX 21**: User interfase framwork
- **FXML**: Declarative interfase definition
- **CSS**: Custom styles and themes
- **Maven**: Dependencys managment

## 🚀 How to Run

1. **From IDE:**
   ```bash
   Run the main clas: com.codeup.booknova.NovaBook
   ```

2. **From Maven:**
   ```bash
   mvn clean javafx:run
   ```

3. **Executable JAR:**
   ```bash
   mvn clean package
   java -jar target/novabook-app.jar
   ```

## 🔧 Developement Configuration

### Requeriments
- Java 17 or superior
- JavaFX 21
- Maven 3.6+

### JavaFX Enviroment Variables
If you run from comand line, maybe you need to configure the module path:
```bash
--module-path /path/to/javafx/lib --add-modules javafx.controls,javafx.fxml
```

## 📋 Implemented Funcionalities

### ✅ Completed
- [x] Autentication sistem with roles
- [x] Navigation betwen dashboards
- [x] Diferent interfases for each role
- [x] Tables with simulated data
- [x] Search forms
- [x] Action butons with informative alerts
- [x] Profesional CSS styles
- [x] Basic responsivity

### 🔄 Pending of Integration
- [ ] Conection with backend services
- [ ] Functional forms for CRUD
- [ ] Real-time data validation
- [ ] Table pagination
- [ ] Advansed filters
- [ ] Report exportation
- [ ] Book image managment
- [ ] Real-time notifications

## 🎯 Next Steps

1. **Backend Integration**: Conect controlers with implemented services
2. **Functional Forms**: Create dialogs for add/edit data
3. **Advansed Validation**: Implement client-side validation
4. **UX Improvments**: Add loading spiners, confirmations, etc.
5. **Internacionalization**: Multi-language suport
6. **Themes**: Light/dark mode

## 📞 Suport

For any question about the user interfase:
- Review the documentation in the controlers
- Verify the FXML files for views estructure
- Check the CSS styles for visual customization

---

**BookNova UI** - A modern and profesional interfase for library managment 📚✨