# Hotel Billing System (HBS) - Android App

## Overview
**Hotel Billing System (HBS)** is an Android application built with **Kotlin** and **Firebase** to manage hotel operations such as customer management, branch management, billing, and invoicing. The system is designed for hotel staff and administrators to streamline daily operations and automate billing, making it easier to manage customers, track orders, and generate invoices efficiently.

The app provides a user-friendly interface and a clean workflow, allowing hotel staff to focus on providing services while the system handles billing and record-keeping.

---

## Why it’s called a Hotel Billing System
This project is called a **Hotel Billing System** because its main functionality revolves around **tracking hotel customers, managing hotel branches, and generating bills/invoices**. Key features include:  
- **Customer Management**: Add, update, and view customer details.  
- **Branch Management**: Manage multiple hotel branches.  
- **Billing**: Generate and save bills for hotel services or products.  
- **Invoices**: View and manage invoices for transparency and record-keeping.  

The system ensures that all transactions are **stored in Firestore**, providing **real-time updates**, secure data storage, and easy retrieval.

---

## Project Workflow

### 1. **Authentication**
- Staff/Admin users log in using Firebase Authentication.
- Login validation ensures that only authorized personnel can access sensitive operations like billing and branch management.

### 2. **Admin Dashboard**
- Admin users can:
  - Manage hotel branches (`ManageBranchesActivity` and `AddBranchActivity`).
  - View all registered customers (`CustomerListActivity`).
  - Generate bills and invoices for services (`BillActivity` and `InvoiceActivity`).

### 3. **Customer Management**
- Staff can add new customers (`AddCustomerActivity`) with details like:
  - Name
  - Phone
  - Email
  - Address
- All customer data is stored in Firebase Firestore under a `customers` collection.
- Customers can be listed and managed through `CustomerListActivity`.

### 4. **Branch Management**
- Admin can add or edit hotel branches.
- Branch data includes branch name, location, and other relevant information.
- Each branch is stored in Firestore under a `branches` collection.

### 5. **Billing**
- Staff can generate bills for customers through `BillActivity`.
- Bills can include items/services, prices, and taxes.
- All billing records are stored in Firestore for auditing and reporting.

### 6. **Invoice Management**
- Invoices are generated from bills.
- Admin and staff can view invoices through `InvoiceActivity`.

---

## Firebase Structure
The project uses **Firebase Firestore** to store data. Collections include:  

- **users**: Stores authenticated user data (admin/staff).  
- **customers**: Stores all customer details.  
- **branches**: Stores hotel branch information.  
- **bills**: Stores billing records and details for each transaction.  

---

## Key Features
- **Secure authentication** using Firebase Authentication.  
- **Real-time database** with Firebase Firestore.  
- **Clean and modern UI** with Material Design components.  
- **Multi-branch support** for hotels with multiple locations.  
- **Easy billing and invoice generation**.  

---

## Project Architecture
