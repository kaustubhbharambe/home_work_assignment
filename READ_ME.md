RetailerApp :- Spring Boot Web Application for getting reward points report json respective customer name and total months. For this spring boot application I have used mongo db for creating customer, order.

1. If we dont want to create customer and order by url then we can go ahead with two sample files named 'customer_tbl.csv' and 'order_tbl.csv'. These files need to import in mongo db and you can generate reward report.
2. Second approach is to create manually create customer and order with help of following url.

Apis:- 
1. Url :- http://localhost:8081/createCustomer
Method :- POST
Parameter :- 
{
    "customerName":"XYZ",
    "date" : "06-12-2024 00:00:00"
}
Response :- 
Customer Created Successfully.

Validation Added for this API.
Url :- http://localhost:8081/createCustomer
Method :- POST
Parameter :- 
{
    "customerName":"",
    "date" : ""
}
Response :- 
Please provide date
Please provide customer Name

Url :- http://localhost:8081/createCustomer
Method :- POST
Parameter :- 
{
    "customerName":"XYZ",
    "date" : ""
}
Response :- 
Please provide date


Url :- http://localhost:8081/createCustomer
Method :- POST
Parameter :- 
{
    "customerName":"",
    "date" : "06-12-2024 00:00:00"
}
Response :- 
Please provide customer Name


Url :- http://localhost:8081/createCustomer
Method :- POST
Parameter :- 
{
    "customerName":"XYZ",
    "date" : "07-12-2024 00:00:00"
}
Response :- 
Future Date is not possible for customer creation.

Url :- http://localhost:8081/createCustomer
Method :- POST
Parameter :- 
{
    "customerName":"XYZ",
    "date" : "06-12-2024 00:00:00"
}
Response :-
Customer name is already present. Please use different name.

2. Url :- http://localhost:8081/createOrder
Method :- POST
Parameter :- 
{
    "customerName":"XYZ",
    "orderDate" : "06-12-2024 00:00:00",
    "amount" : "300.0"
}
Response:-
Order Created Successfully.

Validation Added for this API.
Url :- http://localhost:8081/createOrder
Method :- POST
Parameter :- 
{
    "customerName":"",
    "orderDate" : "",
    "amount" : ""
}
Response:-
Please provide amount
Please provide customer Name
Please provide order date

Url :- http://localhost:8081/createOrder
Method :- POST
Parameter :- 
{
    "customerName":"XYZ",
    "orderDate" : "",
    "amount" : ""
}
Response:-
Please provide order date
Please provide amount


Url :- http://localhost:8081/createOrder
Method :- POST
Parameter :- 
{
    "customerName":"",
    "orderDate" : "06-12-2024 00:00:00",
    "amount" : ""
}
Response:-
Please provide customer Name
Please provide amount

Url :- http://localhost:8081/createOrder
Method :- POST
Parameter :- 
{
    "customerName":"",
    "orderDate" : "06-12-2024 00:00:00",
    "amount" : "200.0"
}
Response:-	
Please provide customer Name

Url :- http://localhost:8081/createOrder
Method :- POST
Parameter :- 
{
    "customerName":"XYZ",
    "orderDate" : "06-12-2024 00:",
    "amount" : "200.0"
}
Response :-
Please enter date format in dd-MM-yyyy HH:mm:ss

Url :- http://localhost:8081/createOrder
Method :- POST
Parameter :- 
{
    "customerName":"XYZ",
    "orderDate" : "06-12-2024 00:00:00",
    "amount" : "abc"
}
Response :-
Please enter valid amount value

Url :- http://localhost:8081/createOrder
Method :- POST
Parameter :- 
{
    "customerName":"XYZ",
    "orderDate" : "06-12-2024 00:00:00",
    "amount" : "-200.0"
}
Response :-
Negative amount is not valid for order creation.

3. Url :- http://localhost:8081/getRewardReport
Method :- GET
Parameter :-
{
    "customerName":"AMAZON",
    "totalMonths" : "5"
}
Response :- 
{
    "errors": [],
    "customerOrder": {
        "customerName": "AMAZON",
        "totalAmount": "9500.0",
        "totalRewardPoints": "240.0",
        "orders": [
            {
                "orderDate": "2024-08-01T00:00Z",
                "amount": "210"
            },
            {
                "orderDate": "2024-08-02T00:00Z",
                "amount": "310"
            },
            {
                "orderDate": "2024-08-03T00:00Z",
                "amount": "410"
            },
            {
                "orderDate": "2024-09-01T00:00Z",
                "amount": "110"
            },
            {
                "orderDate": "2024-09-06T00:00Z",
                "amount": "120"
            },
            {
                "orderDate": "2024-09-08T00:00Z",
                "amount": "130"
            },
            {
                "orderDate": "2024-10-10T00:00Z",
                "amount": "910"
            },
            {
                "orderDate": "2024-10-11T00:00Z",
                "amount": "620"
            },
            {
                "orderDate": "2024-10-12T00:00Z",
                "amount": "830"
            },
            {
                "orderDate": "2024-11-06T00:00Z",
                "amount": "100"
            },
            {
                "orderDate": "2024-11-08T00:00Z",
                "amount": "200"
            },
            {
                "orderDate": "2024-11-09T00:00Z",
                "amount": "300"
            },
            {
                "orderDate": "2024-12-06T00:00Z",
                "amount": "500"
            },
            {
                "orderDate": "2024-08-01T00:00Z",
                "amount": "210"
            },
            {
                "orderDate": "2024-08-02T00:00Z",
                "amount": "310"
            },
            {
                "orderDate": "2024-08-03T00:00Z",
                "amount": "410"
            },
            {
                "orderDate": "2024-09-01T00:00Z",
                "amount": "110"
            },
            {
                "orderDate": "2024-09-06T00:00Z",
                "amount": "120"
            },
            {
                "orderDate": "2024-09-08T00:00Z",
                "amount": "130"
            },
            {
                "orderDate": "2024-10-10T00:00Z",
                "amount": "910"
            },
            {
                "orderDate": "2024-10-11T00:00Z",
                "amount": "620"
            },
            {
                "orderDate": "2024-10-12T00:00Z",
                "amount": "830"
            },
            {
                "orderDate": "2024-11-06T00:00Z",
                "amount": "100"
            },
            {
                "orderDate": "2024-11-08T00:00Z",
                "amount": "200"
            },
            {
                "orderDate": "2024-11-09T00:00Z",
                "amount": "300"
            },
            {
                "orderDate": "2024-12-06T00:00Z",
                "amount": "500"
            }
        ]
    }
}

Validation Added for this API.
Url :- http://localhost:8081/getRewardReport
Method :- GET
Parameter :-
{
    "customerName":"",
    "totalMonths" : "5"
}
Response :- 
{
    "errors": [
        "Please provide customer Name"
    ],
    "customerOrder": {
        "customerName": null,
        "totalAmount": null,
        "totalRewardPoints": null,
        "orders": []
    }
}

Url :- http://localhost:8081/getRewardReport
Method :- GET
Parameter :-
{
    "customerName":"AMAZON",
    "totalMonths" : ""
}
Response :- 
{
    "errors": [
        "Please provide total months"
    ],
    "customerOrder": {
        "customerName": null,
        "totalAmount": null,
        "totalRewardPoints": null,
        "orders": []
    }
}

Url :- http://localhost:8081/getRewardReport
Method :- GET
Parameter :-
{
    "customerName":"",
    "totalMonths" : ""
}
Response :- 
{
    "errors": [
        "Please provide customer Name",
        "Please provide total months"
    ],
    "customerOrder": {
        "customerName": null,
        "totalAmount": null,
        "totalRewardPoints": null,
        "orders": []
    }
}

Url :- http://localhost:8081/getRewardReport
Method :- GET
Parameter :-
{
    "customerName":"AMAZON",
    "totalMonths" : "abc"
}
Response :- 
{
    "errors": [
        "Please enter number in total months."
    ],
    "customerOrder": {
        "customerName": "AMAZON",
        "totalAmount": "0.0",
        "totalRewardPoints": "0.0",
        "orders": []
    }
}

Url :- http://localhost:8081/getRewardReport
Method :- GET
Parameter :-
{
    "customerName":"AMAZON",
    "totalMonths" : "-6"
}
Response :- 
{
    "errors": [
        "Negative total months is not valid to generate reward report."
    ],
    "customerOrder": {
        "customerName": "AMAZON",
        "totalAmount": "0.0",
        "totalRewardPoints": "0.0",
        "orders": []
    }
}