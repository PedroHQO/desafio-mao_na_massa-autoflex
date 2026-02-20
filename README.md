# Autoflex - Production Optimization System 🏭

This repository contains the full-stack solution for the Autoflex Technical Challenge. The system is designed to manage the inventory of raw materials and products for a manufacturing plant, providing an intelligent algorithm to suggest production batches that maximize overall revenue.

## 🚀 Features

* **Raw Material Management:** Full CRUD operations to track inventory levels of individual ingredients/components.
* **Product & Recipe Management:** Full CRUD operations to define products, their selling prices, and their specific bill of materials (recipes).
* **Production Suggestion (Greedy Algorithm):** An intelligent calculation engine that analyzes current raw material stock and suggests the optimal quantity of products to manufacture. The algorithm prioritizes products with the highest selling price to maximize potential profit without exceeding available inventory.
* **Responsive Dashboard:** A modern, clean, and responsive UI built with Next.js and Tailwind CSS.
* **Automated Testing:** Comprehensive test suite covering backend business logic and frontend end-to-end (E2E) user flows.

## 🛠️ Technologies Used

### Backend (API)
* **Java 21** & **Spring Boot 3**
* **PostgreSQL** (Relational Database)
* **Lombok** (Boilerplate reduction)
* **JUnit 5 & Mockito** (Unit Testing)

### Frontend (Web UI)
* **React & Next.js** (App Router)
* **Tailwind CSS** (Styling & Responsiveness)
* **React Hook Form** (Form state management and validation)
* **React Hot Toast** (User-friendly notifications)
* **Cypress** (E2E Testing)
* **Lucide React** (Iconography)

## 🧠 Architecture & Business Logic

The core feature of this application is the **Production Suggestion Engine**. 
It utilizes a **Greedy Algorithm** approach:
1.  Fetches all registered products ordered by highest price descending.
2.  Iterates through the products, calculating the "bottleneck" (limiting factor) based on the current available stock of raw materials.
3.  Determines the maximum producible quantity for the most expensive product.
4.  Virtually deducts the used materials from the stock and moves to the next most expensive product.
5.  Returns a detailed production plan and the total potential revenue.

## ⚙️ Getting Started

### Prerequisites
* Java 21 or higher
* Node.js (v18+)
* PostgreSQL running locally (or adjust the `application.properties` to use an H2 in-memory database)

### Running the Backend
1. Navigate to the backend directory.
2. Ensure your database credentials match the `src/main/resources/application.properties` file.
3. Run the Spring Boot application:
   ```bash
   ./mvnw spring-boot:run
   ```

The API will be available at http://localhost:8080/api.

### Running the Frontend
1. Navigate to the frontend directory.

2. Install the dependencies:
   ```bash
   npm install
   ```

3. Run the Next.js development server:
   ```bash
   npm run dev
   ```

4. Access the application at http://localhost:3000.

## 🏗️ Running the Tests

This project includes automated tests to ensure reliability and code quality.

### Backend (Unit Tests)
The business logic, specifically the production suggestion algorithm, is isolated and tested using JUnit 5 and Mockito.

1. Navigate to the backend directory.
2. Run the tests:
   ```bash
   ./mvnw test
   ``` 

### Frontend (E2E Tests)
Cypress is configured to simulate user flows, such as navigating the dashboard and ensuring critical UI components render correctly.

1. Navigate to the frontend directory.
2. Ensure the frontend is running first:
   ```bash
   npm run dev
   ```
3. Run the tests:
   ```bash
   npx cypress run
   ```

Developed by Pedro Henrique Oliveira for the Autoflex selection process.

