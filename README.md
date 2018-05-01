# replenisher-task-management-backend
## How to use:
**I deployed backend and frontend to AWS server.** You can try the app by using ip address and port number http://54.213.41.232:4200/ (I am aslo planning to setup DNS domain mapping for this ip address use Nginx in my AWS server. But use ip address to access for now.)    

You can test backend project by importing **Postman file (under /libs directory) and send testing requests.**

I also **wrote a few unit tests in backend codes for simple testings.** 

I pre-setup some demo account for logging in and play around in http://54.213.41.232:4200/:     

- Admin account: 
	- id: 1, username: admin_user, password: admin_password     

- Manager account: 
	- id: 5, username: manager_user_1, password: manager_password_1 
	- id: 6, username: manager_user_2, password: manager_password_2    

- User account: 
	- id: 2, username: staff_user_1, password: staff_password_1
	- id: 3, username: staff_user_2, password: staff_password_2 
	- id: 4, username: staff_user_3, password: staff_password_3    


The endpoints of backend listed below are exposed (authentication is permitted for all user. Task and TaskTemplate endpoints are protected by authentication, you will need access_token to request.)

## Tech stack I used:
- For Backend:
	- **Java** with **Spring** framework
	- **MySql** for main data storage
	- **Redis** for token with expiration date storage 
- For Frontend:
	- **TypeScript** with **Angular 5** framework
	- **Angular Metrial Design** for styling and UI design
	
## Project goal illustration:
I listed all the requirements from the spec sheet I got from WalmartLabs and demostrated how did I achieved these features.

1. "individuals to create independent tasks to be added to their personalized task list.":   

I implemented user-role based oauth authentication in backend. All CRUD services for Task and TaskTemplate is 
Different individual will be able to see different tasks for them or related to them. Which means a manager will be able to see all their staff's task, a staff can only see their own project and won't be able to see other staff's tasks.

2. "it should also be exposed as so that other tasks can be created on behalf of the user.":

All the endpoints are exposed. If you have a proper access_token you will be able to request CRUD services for tasks and task templates.

3. "Keep in mind the desire to capture metrics and usage patterns to drive future enhancements"

I created global expcetion handler in backend to handle all expected and unexpected expections and in the future if I have more time I may store all the exceptions along with user log in and operation event to database.

4. "management of template tasks by experienced group, and assignment of these templates as recurring tasks to individuals.  "

I created endpoints and services for managing task and task templates separately. Task Template have the ability to create Task periodically. User can select if the template will recurringly create a task and use cron job expression to setup recurring periods.

5. "Display sorted list of pending tasks":

I finished this by created an endpoints in backend for sorting using the "Rank" algorithm mentioned in the Specs. And the basic sort for string and date are done in frontend.




## How to deploy: 
(I am also planning to improve deployment steps of this project with Docker if I have more time...)
### [Ubuntu] backend
1. Java 8   

`sudo add-apt-repository ppa:webupd8team/java`  

`sudo apt-get update`	

`sudo apt-get install oracle-java8-installer`	


2. Gradle 4.6

`wget https://services.gradle.org/distributions/gradle-4.6-all.zip`	

`sudo mkdir /opt/gradle`	

`sudo unzip -d /opt/gradle gradle-4.6-all.zip`	

`export PATH=$PATH:/opt/gradle/gradle-4.6-all/bin`	


3. Mysql	

`sudo apt-get update`	

`sudo apt-get install mysql-server`	


**during the installation enter the user as 'root' and password as '' (empty)**

**After installation need to set password of root user to null**

Step # 1: Stop the MySQL server process.	

`sudo /etc/init.d/mysql stop`	

Step # 2: Start the MySQL (mysqld) server/daemon process with the --skip-grant-tables option so that it will not prompt for password.	

`sudo mysqld_safe --skip-grant-tables &`	

(you may encounter directory does not exist error: mysqld_safe directory '/var/run/mysqld' for unix socket file don't exists.	

`mkdir -p /var/run/mysqld`	

`chown mysql:mysql /var/run/mysqld`)


Step # 3: Connect to mysql server as the root user.

`mysql -u root`	

Step # 4: Setup new mysql root account password i.e. reset mysql password.

`mysql> use mysql;`	

`mysql> update user set authentication_string=password(''), plugin='mysql_native_password' where user='root';`	

`mysql> flush privileges;`	

`mysql> quit`	

Step # 5: Exit and restart the MySQL server.	

`sudo /etc/init.d/mysql stop`	

To start mysql server use command `sudo /etc/init.d/mysql start`

create a new database in mysql called "task_management_db_mysql" by logging into mysql `mysql -u root` and `CREATE DATABASE task_management_db_mysql;`


4. redis 

`wget http://download.redis.io/releases/redis-4.0.9.tar.gz`

`tar xzf redis-4.0.9.tar.gz`	

`cd redis-4.0.9`	

`make`	

(you may need to install make: use command `sudo apt-get install build-essential`)

To start redis server use command `src/redis-server --daemonize yes`	


### [Ubuntu] frontend
1. Install npm

`cd ~`	

`curl -sL https://deb.nodesource.com/setup_8.x -o nodesource_setup.sh`	

`sudo bash nodesource_setup.sh`	

`sudo apt-get install nodejs`	


2. Install angular cli

`npm install -g @angular `	

## How to build:
### [Ubuntu] backend
cd to backend directory and run `gradle build` command.	
To run the application use command `gradle bootRun`
### [Ubuntu] frontend
cd to frontend director and run `npm install` command.
To run the application use command `ng serve --host 0.0.0.0`

## Endpoints:
### Authentication
1. request access token with password:username
- request url: `/oauth/token`
- request method: `POST`
- request parameters:

| params name   | type       | example                                   |
| ------------  | ---------- | ------------------------------------------|
| client_id     | String     | "YOUR_CLIENT_ID"                                 |
| client_secret | String     | "YOUR_CLIENT_SECRET"                             |
| username      | String     | "USERNAME"                                       |
| password      | String     | "PASSWORD"                                       |
| scope         | String     | "select"                                          |
| grant_type    | String     | Support two modes: "password", "refresh_token"    |

- request parameters sample:

`localhost:8181/oauth/token?grant_type=password&username=admin_user&client_id=clientId&client_secret=clientSecret&password=admin_password&scope=select`

- response sample:
```json
{
    "access_token": "cc1acd80-0588-43f7-a9a6-bbcf5d4526e8",
    "token_type": "bearer",
    "refresh_token": "4f36ab75-7ed9-416e-9562-0bd2e1eb01af",
    "expires_in": 599,
    "scope": "select"
}
```

2. request to refresh access token with refresh_token

- request url: `/oauth/token`
- request method: `POST`
- request parameters:

| params name   | type       | example                                   |
| ------------  | ---------- | ------------------------------------------|
| client_id     | String     | "YOUR_CLIENT_ID"                                 |
| client_secret | String     | "YOUR_CLIENT_SECRET"                             |
| refresh_token | String     | "xxxxxxxx-xxxx-xxxx-xxxx-xxxxxxxxxxxx"                             |
| scope         | String     | "select"                                          |
| grant_type    | String     | Support two modes: "password", "refresh_token" , use "refresh_token" here |

- response sample:
```json
{
    "access_token": "cc1acd80-0588-43f7-a9a6-bbcf5d4526e8",
    "token_type": "bearer",
    "refresh_token": "4f36ab75-7ed9-416e-9562-0bd2e1eb01af",
    "expires_in": 599,
    "scope": "select"
}
```

3. request to check and see if an access_token is valid or not
- request url: `/oauth/check_token`
- request method: `POST`
- request parameters:

| params name  | type       | example                                   |
| ------------ | ---------- | ------------------------------------------|
| access_token | String     | "xxxxxxxx-xxxx-xxxx-xxxx-xxxxxxxxxxxx"    |

- request sample:
`localhost:8181/oauth/check_token?token=f2ac504b-ee9b-446d-adbc-8c9e7a862fd6`


### Task

1. query task list:
- request url: `/task`
- request method: `GET`
- request parameters:

| params name  | type       | example                                   |
| ------------ | ---------- | ------------------------------------------|
| access_token | String     | "xxxxxxxx-xxxx-xxxx-xxxx-xxxxxxxxxxxx"    |

- request body:

- response sample:
```json
{
    "messages": [
        "Get task list by user successfully!"
    ],
    "success": true,
    "result": [
        {
            "taskId": 1,
            "name": "task_cleaning_bathroom",
            "taskStatusString": "CREATED",
            "taskPriorityString": "LOW",
            "description": "",
            "note": "Anne needs to clean the bathroom.",
            "feedback": "bathroom is not clean enough.",
            "timeInput": "2018-04-17T09:27:03.000+0000",
            "timeEstimatedFinish": "2022-09-05T06:10:23.000+0000",
            "corporationId": 1,
            "assignedStaffIdSet": [
                2,
                3
            ],
            "managerIdSet": [
                5
            ],
            "assignedStaffUserDTOSet": [
                {
                    "userId": 2,
                    "corporationId": 1,
                    "username": "staff_user_1",
                    "fullName": "staff1",
                    "emailAddress": "staff1@staff1.com",
                    "enabled": true,
                    "deleted": false
                },
                {
                    "userId": 3,
                    "corporationId": 1,
                    "username": "staff_user_2",
                    "fullName": "staff2",
                    "emailAddress": "staff2@staff2.com",
                    "enabled": true,
                    "deleted": false
                }
            ],
            "managerUserDTOSet": [
                {
                    "userId": 5,
                    "corporationId": 1,
                    "username": "manager_user_1",
                    "fullName": "manager1",
                    "emailAddress": "manager1@manager1.com",
                    "enabled": true,
                    "deleted": false
                }
            ]
        },
        {
            "taskId": 2,
            "name": "task_cook_vegetable",
            "taskStatusString": "CREATED",
            "taskPriorityString": "LOW",
            "description": "",
            "note": "Anne needs to cook some vegetable.",
            "feedback": "vegetables are good.",
            "timeInput": "2018-04-17T09:27:03.000+0000",
            "timeEstimatedFinish": "2034-03-14T14:27:03.000+0000",
            "corporationId": 1,
            "assignedStaffIdSet": [
                4
            ],
            "managerIdSet": [
                6
            ],
            "assignedStaffUserDTOSet": [
                {
                    "userId": 4,
                    "corporationId": 1,
                    "username": "staff_user_3",
                    "fullName": "staff3",
                    "emailAddress": "staff3@staff3.com",
                    "enabled": true,
                    "deleted": false
                }
            ],
            "managerUserDTOSet": [
                {
                    "userId": 6,
                    "corporationId": 1,
                    "username": "manager_user_2",
                    "fullName": "manager2",
                    "emailAddress": "manager2@manager2.com",
                    "enabled": true,
                    "deleted": false
                }
            ]
        }
    ]
}
```

2. create a task:
- request url: `/task`
- request method: `POST`
- request parameters:

| params name  | type       | example                                   |
| ------------ | ---------- | ------------------------------------------|
| access_token | String     | "xxxxxxxx-xxxx-xxxx-xxxx-xxxxxxxxxxxx"    |

- request body:

| params name   | type       | example                                   |
| ------------  | ---------- | ------------------------------------------|
| name     		| String     | "TASK_NAME"                                 |
| taskStatusString 	| String     | Four TaskStatus: "CREATED", "ASSIGNED", "IN_PROGRESS", "FINISHED"        |
| taskPriorityString    | String     | Three TaskPriority: "LOW", "MEDIUM", "HIGH"                                       |
| description           | String     | "TASK_DESCRIPTION"                                       |
| note                  | String     | "TASK_NOTE"                                          |
| feedback              | String     | "TASK_FEEDBACK"                                          |
| timeInput                    | Date     | time when the task was created    |
| timeEstimatedFinish          | Date     | time when the task was estimated to be finished   |
| corporationId                  | Long     |  1                                          |
| assignedStaffIdSet                    | List<Long>     | [2, 3]    |
| managerIdSet          | List<Long>     |   [5, 6]  |


- request body sample:

```json
{
	"name": "task_cleaning_bathroom",
	"taskStatusString": "CREATED",
	"taskPriorityString": "LOW",
	"description": "",
	"note": "Anne needs to clean the bathroom.",
	"feedback": "bathroom is not clean enough.",
	"timeInput": 1523957223008,
	"timeEstimatedFinish": 1662358223008,
	"isRecurring": false,
	"recurringPeriodCronExpression": "will be ignored",
	"corporationId": 1,
	"assignedStaffIdSet": [2, 3],
	"managerIdSet": [5]
}
```

- resonse sample:

```json

```

3. update a task:
- request url: `/task`
- request method: `PUT`
- request parameters:

| request params name  | type       | example                                   |
| ------------         | ---------- | ------------------------------------------|
| access_token         | String     | "xxxxxxxx-xxxx-xxxx-xxxx-xxxxxxxxxxxx"    |

- request body:

| params name   | type       | example                                   |
| ------------  | ---------- | ------------------------------------------|
| name     		| String     | "TASK_NAME"                                 |
| taskStatusString 	| String     | Four TaskStatus: "CREATED", "ASSIGNED", "IN_PROGRESS", "FINISHED"        |
| taskPriorityString    | String     | Three TaskPriority: "LOW", "MEDIUM", "HIGH"                                       |
| description           | String     | "TASK_DESCRIPTION"                                       |
| note                  | String     | "TASK_NOTE"                                          |
| feedback              | String     | "TASK_FEEDBACK"                                          |
| timeInput                    | Date     | time when the task was created    |
| timeEstimatedFinish          | Date     | time when the task was estimated to be finished   |
| corporationId                  | Long     |  1                                          |
| assignedStaffIdSet                    | List<Long>     | [2, 3]    |
| managerIdSet          | List<Long>     |   [5, 6]  |

- request body sample:
```json
{
	"taskId": 2,
	"name": "task_cleaning_bathroom",
	"taskStatusString": "FINISHED",
	"taskPriorityString": "MEDIUM",
	"description": "",
	"note": "Anne needs to clean the bathroom.",
	"feedback": "bathroom is not clean enough.",
	"timeInput": 1523957223008,
	"timeEstimatedFinish": 1523958223008,
	"isRecurring": false,
	"recurringPeriodCronExpression": "will be ignored",
	"corporationId": 1,
	"assignedStaffIdSet": [2, 3, 4],
	"managerIdSet": [6]
}
```


4. delete a task:
- request url: `/task`
- request method: `DELETE`
- request parameters:

| request params name  | type       | example                                   |
| ------------         | ---------- | ------------------------------------------|
| access_token         | String     | "xxxxxxxx-xxxx-xxxx-xxxx-xxxxxxxxxxxx"    |
| task_id              | Long       | 1                                         |

- request body:


5. query template list:
- request url: `/task/template`
- request method: `GET`
- request parameters:

| params name  | type       | example                                   |
| ------------ | ---------- | ------------------------------------------|
| access_token | String     | "xxxxxxxx-xxxx-xxxx-xxxx-xxxxxxxxxxxx"    |

- request sample:
`localhost:8181/task/template?access_token=fe4d3900-c5aa-4797-bfef-a29563df453d`

- response sample:

6. create a task template:
- request url: `/task/template`
- request method: `POST`
- request parameters:

| params name  | type       | example                                   |
| ------------ | ---------- | ------------------------------------------|
| access_token | String     | "xxxxxxxx-xxxx-xxxx-xxxx-xxxxxxxxxxxx"    |

- request body: 

| params name   | type       | example                                   |
| ------------  | ---------- | ------------------------------------------|
| name     		| String     | "TASK_NAME"                                 |
| taskStatusString 	| String     | Four TaskStatus: "CREATED", "ASSIGNED", "IN_PROGRESS", "FINISHED"        |
| taskPriorityString    | String     | Three TaskPriority: "LOW", "MEDIUM", "HIGH"                                       |
| description           | String     | "TASK_DESCRIPTION"                                       |
| note                  | String     | "TASK_NOTE"                                          |
| estimatedDuration                    | long     | duration in milliseconds  |
| corporationId                  | Long     |  1                                          |
| isRecurring                    | boolean     | true or false    |
| recurringPeriodCronExpression          | String     |  six digit cron job expression. For example: "*/5 * * * * ?"  |
| assignedStaffIdSet                    | List<Long>     | [2, 3]    |
| managerIdSet          | List<Long>     |   [5, 6]  |

- request body sample:

```json
{
	"name": "task_cleaning_bathroom",
	"taskStatusString": "CREATED",
	"taskPriorityString": "MEDIUM",
	"description": "",
	"note": "Anne needs to clean the bathroom.",
	"feedback": "bathroom is not clean enough.",
	"timeInput": 1523957223008,
	"timeEstimatedFinish": 1523958223008,
	"isRecurring": true,
	"recurringPeriodCronExpression": "*/5 * * * * ?",
	"corporationId": 1,
	"assignedStaffIdSet": [2, 3],
	"managerIdSet": [5]
}
```

7. update a task template:
- request url: `/task/template`
- request method: `PUT`
- request parameters:

| params name  | type       | example                                   |
| ------------ | ---------- | ------------------------------------------|
| access_token | String     | "xxxxxxxx-xxxx-xxxx-xxxx-xxxxxxxxxxxx"    |

- request body:

| params name   | type       | example                                   |
| ------------  | ---------- | ------------------------------------------|
| taskTemplateId     		| Long     | 1                           |
| name     		| String     | "TASK_NAME"                                 |
| taskStatusString 	| String     | Four TaskStatus: "CREATED", "ASSIGNED", "IN_PROGRESS", "FINISHED"        |
| taskPriorityString    | String     | Three TaskPriority: "LOW", "MEDIUM", "HIGH"                                       |
| description           | String     | "TASK_DESCRIPTION"                                       |
| note                  | String     | "TASK_NOTE"                                          |
| estimatedDuration                    | long     | duration in milliseconds  |
| corporationId                  | Long     |  1                                          |
| isRecurring                    | boolean     | true or false    |
| recurringPeriodCronExpression          | String     |  six digit cron job expression. For example: "*/5 * * * * ?"  |
| assignedStaffIdSet                    | List<Long>     | [2, 3]    |
| managerIdSet          | List<Long>     |   [5, 6]  |

- request body sample:
```json
{
	"taskTemplateId": 1,
	"name": "task_cleaning_bathroom",
	"taskPriorityString": "HIGH",
	"description": "",
	"note": "Anne needs to clean the bathroom.",
	"feedback": "bathroom is not clean enough.",
	"estimatedDuration": 123456789,
	"isRecurring": false,
	"recurringPeriodCronExpression": "*/5 * * * * ?",
	"corporationId": 1,
	"assignedStaffIdSet": [2],
	"managerIdSet": [5, 6]
}
```
8. delete a task template:
- request url: `/task/template`
- request method: `DELETE`
- request parameters:

| request params name  | type       | example                                   |
| ------------         | ---------- | ------------------------------------------|
| access_token         | String     | "xxxxxxxx-xxxx-xxxx-xxxx-xxxxxxxxxxxx"    |
| task_id              | Long       | 1                                         |

- request sample:
`localhost:8181/task/template?access_token=dfc6dbfb-84d2-42a5-9fb9-a903e84986bd&taskTemplateId=1`
