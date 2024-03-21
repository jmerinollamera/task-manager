# task-manager

The task-manager project is a basic Spring Boot 2 project with the functionality to create, list, change status and
delete tasks.
Each task can have a list of subtasks.
Each subtasks can only have one parent task.

It is not implemented to add a subtask to a task. All the subtasks must be provided when the task is created.

Also, in the project, there is implemented a **basic authentication** and authorization using Spring Boot Security.

## Basic logic required

* **Create task:** with fields name, description, expiry date, state

* **Retrieve all tasks:** in the list all the tasks are listed. If a task has subtasks, the list will list all the
  tasks (subtasks included) and the subtasks under the parent task. Example:

```json
[
  {
    "id": 1,
    "description": "Task 1",
    "expiryDate": "2024-06-30",
    "status": "PENDING",
    "subtasks": [
      {
        "id": 2,
        "description": "Subtask 1 of Task 1",
        "expiryDate": "2024-06-01",
        "status": "PENDING",
        "subtasks": [],
        "parentTask": {
          "id": 1,
          "description": "Task 1",
          "expiryDate": "2024-06-30",
          "status": "PENDING",
          "subtasks": null,
          "parentTask": null
        }
      }
    ],
    "parentTask": null
  },
  {
    "id": 2,
    "description": "Subtask 1 of Task 1",
    "expiryDate": "2024-06-01",
    "status": "PENDING",
    "subtasks": [],
    "parentTask": {
      "id": 1,
      "description": "Task 1",
      "expiryDate": "2024-06-30",
      "status": "PENDING",
      "subtasks": null,
      "parentTask": null
    }
  }
]
```

* **Set task as implemented:**
    * If a parent task is marked as **IMPLEMENTED**, all the subtasks must be set as **IMPLEMENTED**.
    * If all the subtasks are in state **IMPLEMENTED**, the parent task must be set as **IMPLEMENTED**.
* **Delete task:**
    * Only **Authorized** users can delete tasks (user with the role ADMIN).
    * If a parent task is deleted, all the subtasks must be deleted.

## Technical solution

### Technologies/Libraries:

* **Spring Boot 2:** the version configuration must be done manually due that Spring initializrfor version 2 had been
  deprecated.
* **Lombock:** used to reduce the code for the getters/setters/constructors.
* **spring-boot-starter-data-jpa:** persistence library to persist tasks in db using the JpaRepository interface.
* **spring-boot-starter-data-jpa:**
    * used to configure basic security in the application.
    * implement the basic authorization.
    * create users
    * avoid DELETE requests for some users.
* **mapstruct:** used to automatize mappings between Dtos and Entities
* **commons-collections4:** library with some functions for collections
* **h2:** in memory database to store the tasks and operate with them.

### Architecture: Hexagonal Architecture

To structure the packages for the application,
it has been chosen an _**Hexagonal Architecture**_ pattern where the domain logic and the rest of the application
components
are loosely coupled.

The business logic is placed in the **com.jmll.taskmanager.domain** package and all related with the application
infrastructure (security, endpoints, persistence) is placed in the **com.jmll.taskmanager.infrastructure**.

Non of the domain classes have any reference to the infrastructure package but, the infrastructure package implement
interfaces defined in the domain package.

## How test task-manager

### Steps to download and run the project: ###

* **Download:** the project from the public repository https://github.com/jmerinollamera/task-manager
* **Compile and generate the .jar:** inside the task-manager project directory, execute: **mvn clean install**
* **Execute project:** inside the task-manager project directory, execute: **java -jar
  target/task-manager-0.0.1-SNAPSHOT.jar**

### Test the project: ###

* **Postman collection:** the project can be tested with the postman collection **task-manager.postman_collection.json**
  located in the project directory.
* **Import collection:**
    * Select File-import in Postman menu:

      <img width=60% src="https://github.com/jmerinollamera/task-manager/blob/main/img/import_in_postman.png">

    * Select Files

      <img width=60% src="https://github.com/jmerinollamera/task-manager/blob/main/img/select_files.png">

    * Select collections

      <img width=60% src="https://github.com/jmerinollamera/task-manager/blob/main/img/select_collection.png">

* **Configuration:** the collection has two configurations:
    * Authorization: can be used to set the user and password to send for authentication and authorization.

      <img width=60% src="https://github.com/jmerinollamera/task-manager/blob/main/img/Authorization_screen.png">

    * Variables: it is set the variable url-task-manager to point to the url and port set for the application.

      <img width=60% src="https://github.com/jmerinollamera/task-manager/blob/main/img/Variables_screen.png">

  There are two users defined to test the project:
    * Basic user:
        * Username: user
        * Password: basic
    * Admin user (can delete task):
        * Username: jorge
        * Password: merino

* **Requests:** there are six request for test:

    <img width=60% src="https://github.com/jmerinollamera/task-manager/blob/main/img/test_requests.png">

    * **Add task (POST request):** add task without subtasks. Status PENDING by default.
    * **Add task with subtasks (POST request):** add a task with a list of subtasks
    * **Get all (GET request):** retrieve all the tasks in the database
    * **Set task as implemented by id (POST request):** set task as IMPLEMENTED
    * **Set task as Pending by id (POST request):** (NOT REQUIRED in the cases definition) but useful to test the
      project.
    * **Delete task by id (DELETE request):** delete a task only if the user is ADMIN role 