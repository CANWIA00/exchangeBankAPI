###


# 💸 Exchange Bank App 💸

The Exchange Banking API is a feature-rich and real-time currency exchange platform. It enables users to create custom accounts, perform currency exchanges, and experience seamless banking operations powered by real-time data from the Narodowy Bank Polski API (NBP).


## 🚀 Functional Requirements

- User authentication (login/logout)

- Account Management: Create and manage personalized accounts for different currencies.

- Money exchange transactions: Buy or Sell currency.

- Deposit money: Deposit money to your account.

- Exchange history: Get your exchange history and details about exchange transactions

- Real-Time Currency Rates: Get live exchange rates from the NBP API for accurate currency conversions.

## 🛠️ Technologies

- Programming Language: Java 21
- Framework: Spring Boot
- Database: MySQL with JPA
- HTTP Client: RestTemplate for consuming external APIs
- JSON Parsing: Jackson dependency
- Messaging Queue: RabbitMQ for error management and notifications
- Security: JWT Authorization and Spring Security
- Currency Rates API: Narodowy Bank Polski API (NBP API)
- Deployment: Docker for containerization and environment management

###


### Clone the Exchange Bank API repository
- git clone https://github.com/CANWIA00/exchangeBankAPI.git

### Navigate into the project directory
- cd exchangeBankAPI

### Run the file using Docker
- docker-compose up --build

### Swagger and full documentation
- http://localhost:8080/swagger-ui/index.html#/

### Endpoints
- Base url: http://localhost:8080/v1/
- All the endponints got secured with jwt token except Login and Register endpoints. So need to send also jwt token all the time via header.


| Rquest Type | Path     | Description                |
| :-------- | :------- | :------------------------- |
| `GET` | `exchange` | The length of the response |
| `POST` | `exchange` | The amount of pages|
| `GET` | `exchange/{id}` | Link to the next page (if it exists)|
| `GET` | `exchange/account/{id}` | Link to the previous page (if it exists)|


###

<h1 align="left"> Dedvelopment </h1>

###

<h4 align="center"> Database Object Diagram:</h4>

###

![Database Schema](https://raw.githubusercontent.com/CANWIA00/exchangeBankAPI/master/DB.png)

###

<h3 align="center">Business Logic </h3>

###

<h4 align="center">Class Diagram: </h4>

###

![Exchange Operation Class Diagram](https://raw.githubusercontent.com/CANWIA00/exchangeBankAPI/master/class.png)

###

<h4 align="center"> Class Diagram / RabbitMQ Process Logic:</h4>

###


![RabbitMQ Queue Logic](https://raw.githubusercontent.com/CANWIA00/exchangeBankAPI/master/class2.png)


