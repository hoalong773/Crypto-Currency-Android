# Crypto Currencies Android App
From CH Resource VietNam we want to improve the current digital currencies wallet and for that
purpose we want to build a new mobile application to help users to enjoy quick and secure
access to the wallet

# Demo

![home](https://user-images.githubusercontent.com/7643183/120780927-c51e0080-c552-11eb-999c-1a411dfc24d0.jpg)

# Getting Started

- Clone or download this repository
- Run and feel free to use this app without any account

# Functionalities

- Add update price real time (each 30s)
- Add search functions for names to find currencies.
- Check Internet and call api when internet access.

# Libraries and Frameworks

- Presentation module
   - Data-binding
   - MVVM Architecture.(is Requirement)
-  Network
    - Retrofit
    - Okhttp3
    - Moshi
- Dependence Injection: coroutines & koin. (is Requirement)
- Kotlin coroutines
- Layout
    - ConstraintLayout
- Image: Coil

# Base Architecture Diagram (Model-View-ViewModel)

![architecture_diagram](https://user-images.githubusercontent.com/7643183/119255903-4e931180-bbe8-11eb-95cd-4cd15ed0c3d7.png)

### View

- Activity, Fragment, Views
- Binding data from ViewModel
- Handle UI logic

### View-Model

- Live Data
- Code logic

### Domain use-case

Define all functions to use-case

# Test-Case

| ID | Action | Description | Input | Expected Output | Actual Output | Test Result |
| ------------- | ------------- | ------------- |  ------------- | ------------- |  ------------- | ------------- |
| TC01 | Open app |  Check default list of currency | Has network | ![home](https://user-images.githubusercontent.com/7643183/120779555-5db38100-c551-11eb-8158-96a94c4526f6.jpg) | ![home](https://user-images.githubusercontent.com/7643183/120779555-5db38100-c551-11eb-8158-96a94c4526f6.jpg) | Passed | 
| TC02 | Open app |  Check list of currency when don't have network | Disconnected network | ![no_connection](https://user-images.githubusercontent.com/7643183/120779569-60ae7180-c551-11eb-8d88-3b97fcd57890.jpg) | ![no_connection](https://user-images.githubusercontent.com/7643183/120779569-60ae7180-c551-11eb-8d88-3b97fcd57890.jpg) | Passed |
| TC03 | Open app |  Check list of currency when have network again | Connected network | ![home](https://user-images.githubusercontent.com/7643183/120779555-5db38100-c551-11eb-8158-96a94c4526f6.jpg) | ![home](https://user-images.githubusercontent.com/7643183/120779555-5db38100-c551-11eb-8158-96a94c4526f6.jpg) | Passed |
| TC04 | Open app > In Search screen, input query to search|  Check list of result after searching | Input key: "neo" |![search](https://user-images.githubusercontent.com/7643183/120779576-63a96200-c551-11eb-82d1-10f071f51e69.jpg) | ![search](https://user-images.githubusercontent.com/7643183/120779576-63a96200-c551-11eb-82d1-10f071f51e69.jpg) | Passed |
| TC05 | Open app > In Search screen, input query to search|  Check empty list after searching | Input key: "lll" | ![no_data](https://user-images.githubusercontent.com/7643183/120779591-66a45280-c551-11eb-9c7b-1491afacdfef.jpg) | ![no_data](https://user-images.githubusercontent.com/7643183/120779591-66a45280-c551-11eb-9c7b-1491afacdfef.jpg) | Passed |

- Get data from the repository

### Model - Data layer | Repository

All data needed for the application comes from this

Receive a request to get data. Switch data between remote and local to return a value 

- Local data source: Room for complexly functions in the feature
- Remote data source: web service API
# Crypto Currencies Android App
