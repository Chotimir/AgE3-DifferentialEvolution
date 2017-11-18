# AgE 3 Differential Evolution / Classical Evolution
This project contains an [AgE3](https://gitlab.com/age-agh/age3)-compatible implementations of the Classical Algorithm
for optimization of common test functions. The algorithm employs standard EMAS approach.

## Installation and Commissioning
This repository is compatible with the `0.5` version of the [AgE3](https://gitlab.com/age-agh/age3) project, which is
not deployed yet. In order to run this repository, follow these steps:
 1. Clone the latest version of the [AgE3 develop branch](https://gitlab.com/age-agh/age3/tree/develop).
 2. Enter the main directory of cloned repository.
 3. Navigate to the `settings.gradle` file and append the following line:
    ```
    include 'AgE3-DifferentialEvolution/Commons'
    include 'AgE3-DifferentialEvolution/HybridEvolution'
    ```
     Then close the text editor.
 4. Add this repository as a submodule and navigate to it:
    ```
    git submodule add https://github.com/bgrochal/AgE3-DifferentialEvolution.git
    cd AgE3-DifferentialEvolution/ClassicalEvolution
    ```
 5. Build and run the code included in this project by executing:
    ```
    gradle node -PappArgs="['pl/edu/agh/age/de/classical/classical-evolution-config.xml,pl/edu/agh/age/de/classical/classical-evolution-config.properties,pl/edu/agh/age/de/common/common-config.properties']"
    ```
    Please note that the algorithm configuration is defined in `*.properties` and `*.xml` files given by the program 
    arguments (see the command above).
