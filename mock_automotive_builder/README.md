# Mock Automotive Builder

## Description

Welcome to the Mock Automotive Builder repository! This project aims to simulate the processes of ordering, manufacturing, and producing cars in a car manufacturer. It provides an organization of several Java classes that work together to replicate the operations of a car manufacturer.

## Table of Contents

- [Installation](#installation)
- [Usage](#usage)
- [Classes](#classes)
- [Running Demo.java](#running-demojava)
- [Contributing](#contributing)
- [Contact](#contact)

## Installation

To get started with the Mock Automotive Builder, follow these steps:

1. Download the mock_automotive_builder directory
2. If files get zipped, unzip them
3. See [Running Demo.java](#running-demojava) to run the project

Ensure that you have Java installed on your system before proceeding with the installation.

## Usage

The Mock Automotive Builder provides a Java implementation of car manufacturing processes. To utilize this project, you can explore the available classes and utilize their functionalities. The primary class with a public API for customer interactions is the `Manufacturer` class.

## Classes

The project consists of the following classes:

- `Manufacturer`: This class serves as the central component of the car manufacturing process. It coordinates the factories and part suppliers to produce cars based on customer orders.
- `Factory`: Represents a car manufacturing factory. It receives the necessary parts from the part suppliers and assembles the final cars.
- `PartSupplier`: This class is responsible for building the required parts for the cars. It utilizes other classes like `Options`, `Part`, and `PartSpecification` to define and create the parts.
- `Options`: Represents different available options for car customization.
- `Part`: Represents a specific part of a car.
- `PartSpecification`: Defines the specifications of a particular car part.
- `RequestedVehicle`: Used to specify the details of the vehicle a customer would like to order.
- `Util`: Contains static utility methods used by other classes.
- `Vehicle`: Describes an actual/specific vehicle that would be sold to a customer.
- `VehicleModel`: Describes a model/product line of a vehicle.

Please refer to the source code for more detailed information on the remaining classes.

## Running Demo.java

To run the `Demo.java` class, follow these steps:

1. Download all the Java classes from this repository.
2. Open a command-line interface or an integrated development environment (IDE) of your choice.
3. Navigate to the directory where you saved the Java classes.
4. Compile the classes using the following command:

   ```shell
   javac *.java
5. Once the classes are compiled, execute the Demo class by running the following command:

   ```shell
   java Demo
6. You will now be able to interact with the simulation and explore the car manufacturing processes.

## Contributing
Contributions to the Mock Automotive Builder project are welcome! If you would like to contribute, please follow these guidelines:

1. Fork the repository.
2. Create a new branch.
3. Make your changes.
4. Test your changes.
5. Submit a pull request.
Please ensure that your contributions align with the project's coding standards and guidelines.

## Contact
If you have any questions or suggestions regarding the Mock Automotive Builder project, please feel free to reach out to me:

- Ari Schlacht
- arischlacht@gmail.com
- [LinkedIn](https://www.linkedin.com/in/ari-schlacht/)

I appreciate your interest in my project and look forward to your feedback!   
