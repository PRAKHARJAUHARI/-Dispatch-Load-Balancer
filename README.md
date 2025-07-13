
Dispatch Load Balancer – API & Setup Doc


Author: Prakhar Jauhari
GitHub: https://github.com/PRAKHARJAUHARI/-Dispatch-Load-Balancer

---

📌 PROJECT OVERVIEW:
--------------------
This Spring Boot project handles delivery order assignments to available vehicles based on geolocation and delivery priority. It includes endpoints to upload orders, register vehicles, and generate a dispatch plan.

---

🚀 HOW TO RUN THE PROJECT:
---------------------------

1. Clone the repository:
   git clone https://github.com/PRAKHARJAUHARI/-Dispatch-Load-Balancer.git

2. Navigate into the project directory:
   cd -Dispatch-Load-Balancer

3. Open the project in your favorite IDE (e.g., IntelliJ, VSCode).

4. Build the project using Maven:
   mvn clean install

5. Run the application:
   You can run the main class:
   `DispatchLoadBalancerApplication.java`

   OR use the command:
   mvn spring-boot:run

6. Access the application:
   The app runs at: http://localhost:8080

---

📚 API DOCUMENTATION:
----------------------

1. **Add Orders**
   - **Endpoint:** `POST /orders`
   - **Description:** Upload delivery orders.
   - **Payload (JSON):**
     ```json
     [
       {
         "orderId": "ORD001",
         "priority": "HIGH",
         "packageWeight": 5.0,
         "address": "221B Baker Street",
         "longitude": 77.5946,
         "latitude": 12.9716
       }
     ]
     ```
   - **Response:** `200 OK`

2. **Add Vehicles**
   - **Endpoint:** `POST /vehicles`
   - **Description:** Register delivery vehicles with current location.
   - **Payload (JSON):**
     ```json
     [
       {
         "vehicleId": "VEH001",
         "capacity": 10.0,
         "currentAddress": "MG Road",
         "currentLongitude": 77.6033,
         "currentLatitude": 12.9752
       }
     ]
     ```
   - **Response:** `200 OK`

3. **Generate Dispatch Plan**
   - **Endpoint:** `GET /dispatch`
   - **Description:** Returns optimal dispatch plan based on location and capacity.
   - **Response:**
     ```json
     {
       "VEH001": ["ORD001", "ORD002"]
     }
     ```

---

🧪 TESTING:
-----------
- Run all unit and integration tests using:
  `mvn test`

---

🛠 DEPENDENCIES:
-----------------
- Java 17+
- Spring Boot 3.2.5
- Spring Data JPA
- H2 Database (in-memory)
- Lombok

---

📌 NOTES:
---------
- Make sure Lombok plugin is installed in your IDE.
- H2 Console (for testing DB): http://localhost:8080/h2-console

---

✅ Best Practices Followed:
----------------------------
- RESTful API Design
- Proper Entity Constraints & Validation
- Custom Exception Handling
- Modular Codebase

