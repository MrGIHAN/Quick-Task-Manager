
# Quick Task Manager

**Quick Task Manager** is a simple and efficient task management web app that allows users to add, manage, and track their tasks with ease. The application supports task filtering, a summary dashboard with visual stats (using Chart.js), and user-specific task tracking via backend API integration.

## 🌟 Features

* ✅ Add and manage tasks
* 📋 Filter tasks by **All**, **Pending**, or **Completed**
* 📊 Task summary dashboard with bar chart (Chart.js)
* 🔐 User-specific task loading based on `userId` stored in `localStorage`
* ⚡ Backend integration using REST API (Spring Boot)

## 🔧 Technologies Used

### Frontend

* HTML, CSS, JavaScript
* Chart.js for data visualization

### Backend

* Java Spring Boot REST API (running on `http://localhost:8080`)

### Others

* LocalStorage for storing userId

## 📁 Project Structure

```
quick-task-manager/
│
├── index.html          # Main task list page
├── summary.html        # Summary dashboard with chart
├── main.js             # Handles task logic and chart rendering
├── style.css           # UI styling
└── README.md           # Project documentation
```

---

## 🧪 Testing

- Backend tested using JUnit and Spring Boot Test
- Integration tests for API endpoints
- Validation on user inputs for both frontend and backend

---


### Backend (Spring Boot)
1. Clone the repository:
   ```bash
   git clone https://github.com/IT21826740/quick-task-manager.git
   cd quick-task-manager/backend

---

## 🛠️ Steps to Run the Project

1️⃣ **Start Docker and Database Connection**

* Make sure Docker is running.
* Start the database container (e.g., MySQL).

2️⃣ **Open the Project in IntelliJ IDEA**

* Import the project as a Maven project.

3️⃣ **Navigate to Project Path**

```bash
cd /path/to/your/project
```

4️⃣ **Start the Database Container**

```bash
docker exec -it <container_name> /bin/bash
mysql -u root -p
# Enter your password when prompted
```

5️⃣ **Run the Spring Boot Application**

```bash
mvn spring-boot:run
```

6️⃣ **Run Test Cases (if needed)**

* Find the `test` directory in your project structure.
* Run all test cases from IntelliJ or use Maven:

```bash
mvn test
```

7️⃣ **Generate Code Coverage Report**

```bash
mvn clean test
# Check the `target` directory. If `site` folder is missing, create it manually.
```

8️⃣ **Create JaCoCo Coverage Report**

```bash
mvn jacoco:report
```

9️⃣ **View Code Coverage**

* Open the following file in your browser:

```
target/site/jacoco/index.html
```

* You can also find the XML version:

```
target/site/jacoco/jacoco.xml
```

🔟 **Run Static Code Analysis with SonarQube**

* Start SonarQube locally using Docker:

```bash
docker run -d --name sonarqube -p 9000:9000 sonarqube:latest
```

* Visit: [http://localhost:9000](http://localhost:9000)
  Default login: `admin / admin`
  *(Change password in Security page)*

1️⃣1️⃣ **Run Sonar Analysis**

```bash
mvn sonar:sonar \
  -Dsonar.projectKey=quick-task-manager \
  -Dsonar.host.url=http://localhost:9000 \
  -Dsonar.login=<YOUR_SONARQUBE_TOKEN> \
  -Dsonar.coverage.jacoco.xmlReportPaths=target/site/jacoco/jacoco.xml
```

1️⃣2️⃣ **View Sonar Results**

* Go to: [http://localhost:9000](http://localhost:9000)
* View:

  * ✅ Code Coverage %
  * 🚨 Code Smells
  * 🐞 Bugs
  * 🔐 Vulnerabilities
  * 🔁 Duplications

1️⃣3️⃣ **Run the Frontend**

```bash
cd /path/to/your/project
google-chrome http://localhost:8080/login.html
```

1️⃣4️⃣ **Find Frontend Code**

* Navigate to:

```
src -> main -> resources -> static
```

---

## 📌 Screenshots

![Screenshot From 2025-05-01 17-10-34](https://github.com/user-attachments/assets/93154035-af71-43c5-bf01-cbcca28979cf)
![Screenshot From 2025-05-01 17-18-14](https://github.com/user-attachments/assets/8c59727e-c651-4b2d-8f7b-6ec5f7a561ae)

## 📬 Feedback & Contributions

Feel free to fork the repo, raise issues, or submit pull requests.
Your feedback is always welcome!

---

