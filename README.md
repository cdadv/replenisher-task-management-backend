# replenisher-task-management-backend

## Endpoints:
### Task
1. query task list:
- request url: `/task`
- request method: `GET`
- request parameters:

| params name  | type  | example  |
| ------------ | ---   | --- |
| access_token | String   | "xxxxxxxx-xxxx-xxxx-xxxx-xxxxxxxxxxxx" |

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