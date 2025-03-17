# Managing Online Exams Platform

EduPlatform is a Java-based web application designed to streamline educational course management, testing, and scoring. It supports three user roles—Administrators, Teachers, and Students—offering a robust system for registration, course management, test creation, and evaluation. Built with scalability and flexibility in mind, it adheres to SOLID design principles for future extensibility.

## Features

### Phase A: User & Course Management
- **Registration**: Users can sign up as a Teacher or Student, entering "Awaiting Approval" status.
- **Admin Approval**: Admins can view, approve, edit, or change roles for all registered users.
- **Search & Filter**: Admins can filter users by role, name, or other criteria.
- **Course Definition**: Admins create courses with details like title, unique ID, start/end dates.
- **Course Assignment**: Admins assign one teacher per course and add students; teachers can teach multiple courses.

### Phase B: Test Creation
- **Teacher Dashboard**: Teachers view their assigned courses and manage tests.
- **Test Management**: Create, edit, or delete tests with title, description, and duration (in minutes).

### Phase C: Question Design
- **Question Types**:
  - *Multiple-Choice*: Unlimited options with a specified correct answer.
  - *Essay*: Text-based questions with student response fields.
- **Question Bank**: Teachers access and reuse past questions; new questions auto-save to the bank.
- **Scoring**: Set default scores per question; total test score auto-calculated.
- **Extensibility**: Designed for easy addition of new question types in the future.

### Phase D: Student Experience
- **Course View**: Students see their enrolled courses and available tests.
- **Test-Taking**: Participate in tests with a countdown timer, navigate questions, and submit answers.
- **Resilience**: Temporary answer storage for reconnects; no reattempts after completion or timeout.

### Phase E: Scoring & Results
- **Teacher Review**: View test participants and results.
- **Auto-Scoring**: Multiple-choice questions scored instantly; essay answers manually scored by teachers (within max limits).

## Installation

### Prerequisites
- Java 17
- Maven (for dependency management)
- [Database : PostgreSQL]
- Web server (embedded Spring Boot)

### Steps
1. Clone the repository:
   ```bash
   git clone https://github.com/miladfrj/managingonlineexams.git
