💇‍♀️ Sulochana Salon Mobile App

A comprehensive mobile application for managing salon operations, client appointments, services, and staff activities. This Android app streamlines salon management and enhances the customer experience.

📌 Table of Contents

About the App

Features

User Roles

Technology Stack

App Screens & UI Overview

Database & Backend

Installation

Testing

Future Enhancements

License

About the App

The Sulochana Salon Mobile App is an Android-based solution designed for salon owners, staff, and customers to efficiently manage daily operations. The app allows customers to book appointments, view services, and track their appointment history while providing salon admins and staff tools to manage services, appointments, notifications, and payments in a centralized system.

This mobile application is ideal for enhancing operational efficiency, improving client satisfaction, and digitizing salon workflows.

Features
Customer Features

Browse salon services with detailed descriptions and images.

Book and cancel appointments easily.

View past appointments and payment history.

Receive notifications for appointments and promotions.

Manage personal profile information.

Admin Features

Dashboard for managing appointments, payments, and notifications.

Manage salon services, including adding, editing, and removing services.

View and track client appointments and payment history.

Handle notifications and announcements for customers.

Manage staff access and roles.

Staff Features

View and manage assigned appointments.

Confirm, modify, or cancel customer bookings.

Process service-related tasks efficiently.

User Roles
Role	Access & Permissions
Admin	Full access: manage users, services, appointments, payments, and notifications.
Operational Staff	Manage and process appointments, confirm or modify bookings, and update services.
Customer	View services, book appointments, view history, and receive notifications.
Technology Stack

Platform: Android

Language: Java

Database: SQLite (local)

UI/UX: Android XML layouts, Material Design components

Gradle Build System: Kotlin DSL

Testing: JUnit (unit tests), Instrumented tests for UI

App Screens & UI Overview

The application provides a clean and user-friendly interface, including:

Splash Screen

Login & Sign-Up Screens

Customer Dashboard & Service Listings

Admin Dashboard & Management Screens

Appointment Booking & History

Payment History & Billing Screens

Notifications & Feedback Management

UI Design Decisions:

Material design principles for consistent UI/UX.

Separate layouts for Admin, Staff, and Customer roles for clarity.

Intuitive navigation using bottom navigation and side menus.

Responsive layouts for multiple screen sizes and densities.

Database & Backend

Database Helper: SQLite database to store user info, appointments, services, and notifications.

Tables:

Users – stores customer, staff, and admin details.

Services – stores service names, descriptions, and pricing.

Appointments – stores bookings, status, and payment details.

Notifications – stores announcements and alerts.

Payments – tracks payment records and invoices.

Backend Logic: Implemented in Java using MVC architecture to separate UI and data operations.

Installation

Clone the repository:

git clone https://github.com/upethalaksiluni/salon-app.git


Open the project in Android Studio.

Sync Gradle to download all dependencies.

Run the app on an emulator or physical Android device (API 21+ recommended).

Testing

Unit tests for database and business logic.

Instrumented tests for activity navigation and UI components.

Manual testing for:

Booking appointments

Adding/editing services

Admin notifications

User login/sign-up

Feedback evaluation from test users to improve usability.

Future Enhancements

Cloud database integration for multi-device access.

Push notifications for appointments and promotions.

Integration with online payment gateways.

Advanced analytics for admin dashboard.

Multi-language support.

📬 Contact

Maintainer: Upetha Laksiluni
GitHub: https://github.com/upethalaksiluni
Linkedin: 
Email: upethalaksiluni@gmail.com

License

This project is licensed under the MIT License.
