# Exam Cove

Exam Cove is a Java-based web application designed to manage educational courses, create and administer online tests, and evaluate student performance. Built with Spring Boot and Maven, it supports three roles—Administrators, Teachers, and Students—with a focus on scalability and SOLID design principles.

## Features

### Phase A: User & Course Management
- **Registration**: Users register as Teachers or Students, entering "Awaiting Approval" status.
- **Admin Approval**: Admins view, approve, edit, or change roles for all users.
- **Search & Filter**: Admins filter users by role, name, etc.
- **Course Definition**: Admins create courses with title, unique ID, start/end dates.
- **Course Assignment**: Admins assign one teacher per course and add students; teachers can teach multiple courses.

### Phase B: Test Creation
- **Teacher Dashboard**: Teachers manage tests for their assigned courses.
- **Test Management**: Create, edit, or delete tests with title, description, and duration.

### Phase C: Question Design
- **Question Types**: Multiple-choice (unlimited options) and essay questions.
- **Question Bank**: Teachers reuse and auto-save questions.
- **Scoring**: Set default scores per question; total test score calculated.

### Phase D: Student Experience
- **Course View**: Students see enrolled courses and available tests.
- **Test-Taking**: Timed tests with navigation, resilience for reconnects, and auto-end on timeout.

### Phase E: Scoring & Results
- **Teacher Review**: View participants and results; auto-score multiple-choice, manual score for essays.

## Installation

### Prerequisites
- Java 17
- Maven
- Postgresql

### Steps
1. Clone the repository:
   ```bash
   git clone https://github.com/miladfrji/Managing-online-exams.git
