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

###

#### Authentication Controller

| Request Type | Path     | Description                |
| :-------- | :------- | :------------------------- |
| `POST` | `/auth/register` | To register to app  |
| `POST` | `/exchange/login` | To login to app |

#### User Controller

| Request Type | Path     | Description                |
| :-------- | :------- | :------------------------- |
| `GET` | `/user` | Get user details  |

#### Account Controller

| Request Type | Path     | Description                |
| :-------- | :------- | :------------------------- |
| `POST` | `/acount/pln` | To create pln account when you register to app first |
| `POST` | `/account` | Create an account with foreign currency |
| `GET` | `/account/{id}` | Get account using by id |
| `GET` | `/account/user` | Get all accounts using by user id via jwt token|
| `DELETE` | `/account/{id}` | Delete account using by account id |
| `PATCH` | `/account/{id}` | Add money to account using by account id |


#### Exchange Controller

| Request Type | Path     | Description                |
| :-------- | :------- | :------------------------- |
| `GET` | `/exchange` | Get all exchanges using by user id via jwt token [History]|
| `POST` | `/exchange` | Make an exchange between two diferent account |
| `GET` | `/exchange/{id}` | Get exchange details using by id |
| `GET` | `/exchange/account/{id}` | Get all exchange details using by account id [History]|

#### Currency Controller

<h6>All currency rates are up to date. // https://api.nbp.pl/</h6>

| Request Type | Path     | Description                |
| :-------- | :------- | :------------------------- |
| `GET` | `/currency/table` | Get all currency rates|
| `GET` | `/currency/table/period/{id}` | Get currency rates for 1 month |
| `GET` | `/currency/id/{id}` | Get currency using by id|

###

<h4>Launch the API on your device and navigate to the Swagger documentation endpoint to explore the available requests and responses.</h4>

 http://localhost:8080/swagger-ui/index.html#/


###

<h1 align="left"> Development </h1>

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


