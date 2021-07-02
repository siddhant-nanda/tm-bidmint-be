# BidMint - A Reverse Auction Platform

## A MVP developed for Hackathon 2.0 organised by [Turtlemint](https://www.turtlemint.com/)

### This repo consists of backend implementation code for the **BidMint MVP**

> ## About the product

- The idea of this product is to provide a platform for the corporate clients who have the requirements to buy corporate policies like Group Health Insurance, Cyber Insurance, etc.

- This platform will provide the corporate clients (i.e. buyers) a digital market place to add a proposal for their insurance through a proposal form.

- The platform will trigger notifications to all the insurance companies (i.e. sellers) registered on the platform.

- The insurance companies will have the opportunity to share their response through the platform which in turn will be visible to the corporate clients.

- The response from the sellers is in the form of bid which will include the response along with a quotation value will be attached.

- Based on the reponse, a bid score will be calculated and based on which the bids will be listed in the descending order.

- After selecting 3 bids from the sellers, the buyer can have a meeting for their agreement and discussion.

- Finally the buyer will be able to move ahead with their purchase of policies.

> ## Features

- A digital platform to add proposal based on your requirements for buyer.

- Notification system is triggered to have real time update for buyer and sellers.

- A response in the form of bid is captured to respond the proposals for sellers.

- Merging facility is available to merge multiple bids into one for providing buyer better options.

> ## About the Backend Implementation

- Tech Stack - Java Spring Boot, MongoDB, Notification Service, Template Generator

- To run the project

1. Clone the repo
2. Change the MongoDB master and slave host url in BidMint.java (will parameterise later)
3. Change the notification service url and dashboard url in application.properties
4. ```./gradlew compileJava```  (to check the build)
5. ```./gradlew bootrun``` (to run the project) will run in 1212 port

> ## Collaborators

- [Bhanu Pratap Chauhan](https://github.com/bhanuAtWork)

> ## Conclusion

We have finally won the Best Project in the hackathon along with Best Idea Award among our organisation.

![Hackathon Certificate](./hackathon-certificate/Hackathon-Certificate.png?raw=true "Winner")
