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

<h2>Endpoints</h2>
<h4>Currency</h4>
<h6>You need to take a look NBP api for currency ids. All the currencies are provided using by Bank Polski Api: https://api.nbp.pl/ </h6>
<ul>
  <li>http://localhost:8080/v1/currency/table</li>
   <li>http://localhost:8080/v1/currency/id/{id}</li>
   <li>http://localhost:8080/v1/currency/table/period/{id}</li>
</ul>  
<h4>Account</h4>
<ul>
  <li>http://localhost:8080/v1/account/{id}</li>
   <li>http://localhost:8080/v1/account/user</li>
   Post <li>http://localhost:8080/v1/account</li>
</ul>  



###

<h2 align="center"> Dedvelopment </h3>

###

<h4 align="left"> Database Object Diagram:</h4>

###

![Database Schema](https://raw.githubusercontent.com/CANWIA00/exchangeBankAPI/master/DB.png)

###

<h4 align="left">Business Logic: </h4>

###

<h5 align="left">Class Diagram: </h5>

###

![Exchange Operation Class Diagram](https://raw.githubusercontent.com/CANWIA00/exchangeBankAPI/master/class.png)

###

<h5 align="left"> Class Diagram / RabbitMQ Process Logic:</h5>

###


![RabbitMQ Queue Logic](https://raw.githubusercontent.com/CANWIA00/exchangeBankAPI/master/class2.png)


