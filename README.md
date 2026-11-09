Employee Management System

A Java console application for managing employees of different pay types. Employees are stored in an SQLite database, so your changes are still there the next time you run the program.

Demo video: [PASTE YOUR YOUTUBE LINK HERE]

Features
Three employee types: Hourly (with overtime), Salaried, and Commission
Add new employees (with input checking)
Display all employees, only one type, or a single employee by ID
Update any field of an employee
Remove an employee (with confirmation)
Payroll report showing how each employee's pay was figured and the total
Saved data: everything is stored in an SQLite database file
Runs from a menu until the user chooses Quit
Technologies
Java
SQLite, using the sqlite-jdbc driver (JDBC)
Console (command-line) interface
Getting Started
What you need
A Java JDK (version 11 or newer)
The SQLite driver jar, sqlite-jdbc-3.42.0.0.jar, in the lib folder (it is already there if you cloned this repository with the lib folder; otherwise download version 3.42.0.0 from Maven Central and put it in lib)
Run it from a terminal

From the project folder:

Windows:

javac -cp "lib/*" -d out src/*.java
java -cp "out;lib/*" App

Mac / Linux:

javac -cp "lib/*" -d out src/*.java
java -cp "out:lib/*" App
Run it in VS Code

Make sure the jar is listed under Referenced Libraries in the Java Projects panel, then open src/App.java and click the Run link above main. (The Code Runner button does not load the library.)

The first run creates employees.db and fills it with five sample employees. Delete employees.db to start over with fresh sample data.

Project Structure
employee-management-system/
  lib/                      SQLite JDBC driver (jar)
  src/
    App.java                Entry point: banner, database setup, starts the menu
    EmployeeMenu.java       Main menu loop and all menu choices
    EmployeeDatabase.java   All SQL: create, read, update, delete
    InputReader.java        Reads and validates what the user types
    ConsoleUI.java          All console output (tables, menus, reports)
    Payable.java            Interface: anything that can be paid
    PayrollProcessor.java   Totals pay for a list of Payable objects
    Employee.java           Abstract base class for all employees
    HourlyEmployee.java     Child class: hourly pay with overtime
    SalariedEmployee.java   Child class: annual salary
    CommissionEmployee.java Child class: base pay plus commission
    Address.java            Employee's address (used by composition)
Object-Oriented Design
Concept	Where it is used
Inheritance	HourlyEmployee, SalariedEmployee, and CommissionEmployee extend Employee
Abstract class	Employee is abstract. getType(), calculatePay(), and getPayDescription() are abstract methods
Interface	Payable defines getName(), calculatePay(), and getPayDescription(); Employee implements it
Composition	Each Employee has an Address; EmployeeMenu has a ConsoleUI, an InputReader, and an EmployeeDatabase
Polymorphism	Employees are handled as Employee and Payable references; each type runs its own pay code. Database rows become the correct child class
Constructors	Default, parameterized, copy, and chained (this(...) / super(...)) constructors, for example the four Address constructors
Access specifiers	Data is private, helpers shared with child classes are protected, and only methods other classes need are public
Database

Data is stored in two tables:

addresses (address_id, street, city, state, zip)
employees (employee_id, employee_type, name, address_id, plus the pay columns for each type; columns that do not apply to a type are NULL)

Each employee row points to one address row. Adding, updating, and removing an employee each change both tables in a single transaction, and every statement uses a PreparedStatement.

Course Project Requirements
Requirement	Met by
At least 3 employee types	Hourly, Salaried, Commission
Add and remove employees	Menu choices 1 and 6
Display all employees	Menu choice 2
Display employees of a specified type	Menu choice 3
Update an employee's information	Menu choice 5
Documented with in-code comments	Header and inline comments in every file
Interface class	Payable
Abstract class	Employee
Composition	Employee has an Address
Polymorphism	Payable and Employee references, PayrollProcessor
Classes with constructors	All model classes
Appropriate access specifiers	private, protected, public by rule
Data stored in SQLite	EmployeeDatabase and employees.db
Runs until the user quits	EmployeeMenu loop ends only when the user confirms Quit
Project Summary

[Write your own version of this paragraph. The draft below is based on the project as built; change anything that is not true for you.]

This project is a console-based Employee Management System written in Java. It lets a user add, display, update, and remove employees of three types (Hourly, Salaried, and Commission), view a payroll report, and keep all of the data in an SQLite database so that it is saved between runs. I built it over five weeks, starting with a user interaction document and a software design, and then adding inheritance and composition, an interface and polymorphism, an abstract base class with constructors and access specifiers, and finally database storage with full create, read, update, and delete operations.

The biggest challenge was connecting the object-oriented design to a database without rewriting it. I solved this by keeping all of the SQL in one class, EmployeeDatabase, which accepts and returns Employee objects, so the rest of the program never needs to know how the data is stored. Getting the JDBC driver set up in VS Code also took troubleshooting.

I learned how abstraction, interfaces, and access specifiers make a program easier to extend. Adding a new employee type means writing one new child class. I also learned the value of planning the design first, testing one feature at a time, and validating user input so the program does not crash. In the future I would add searching by name, a graphical interface, and automated tests.
