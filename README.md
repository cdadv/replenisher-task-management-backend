# replenisher-task-management-backend
## How to use:

## How to deploy:

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
| name     | String     | "TASK_NAME"                                 |
| taskStatusString | String     | Four TaskStatus: "CREATED", "ASSIGNED", "IN_PROGRESS", "FINISHED"        |
| taskPriorityString      | String     | Three TaskPriority: "LOW", "MEDIUM", "HIGH"                                       |
| description           | String     | "PASSWORD"                                       |
| note                  | String     | "select"                                          |
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
| task_id              | Long       | 1                                         |

- request body:
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


5. get template list:
- request url: `/task/template`
- request method: `GET`
- request parameters:

6. create a task template:
- request url: `/task/template`
- request method: `POST`
- request parameters:

7. update a task template:
- request url: `/task/template`
- request method: `PUT`
- request parameters:

8. delete a task template:
- request url: `/task/template`
- request method: `DELETE`
- request parameters:
