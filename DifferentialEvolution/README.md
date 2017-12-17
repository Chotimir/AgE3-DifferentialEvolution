# AgE 3 Differential Evolution / Differential Evolution
This project contains an [AgE3](https://gitlab.com/age-agh/age3)-compatible implementation of the Differential 
Evolution Algorithm for optimization of common test functions.

## Installation and Commissioning
This repository is compatible with the `0.5` version of the [AgE3](https://gitlab.com/age-agh/age3) project, which is
not deployed yet. In order to run this repository, follow these steps:
 1. Clone the latest version of the [AgE3 develop branch](https://gitlab.com/age-agh/age3/tree/develop).
 2. Enter the main directory of cloned repository.
 3. Navigate to the `settings.gradle` file and append the following line:
    ```
    include 'AgE3-DifferentialEvolution/Commons'
    include 'AgE3-DifferentialEvolution/DifferentialEvolution'
    ```
     Then close the text editor.
 4. Add this repository as a submodule and navigate to it:
    ```
    git submodule add https://github.com/bgrochal/AgE3-DifferentialEvolution.git
    cd AgE3-DifferentialEvolution/DifferentialEvolution
    ```
 5. Build and run the code included in this project by executing:
    ```
    gradle node -PappArgs="['pl/edu/agh/age/de/differential/differential-evolution-config.xml,pl/edu/agh/age/de/differential/differential-evolution-config.properties,pl/edu/agh/age/de/common/common-config.properties']"
    ```
    Please note that the algorithm configuration is defined in `*.properties` and `*.xml` files given by the program 
    arguments (see the command above).

## Visualisation
In order to evaluate multiple `*.log` files produced by the Differential Evolution algorithm, run the Python script 
placed under `Commons/src/main/resources/visualisation`. Install all required dependencies by typing:
```
pip3 install -r requiremments.txt
```
Then, run the evaluation script by:
```
python3 visualisation.py [BASE_DIR]
```
where `[BASE_DIR]` is the absolute path to this (i.e. DifferentialEvolution) repository.
