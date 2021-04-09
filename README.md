# <h1 align="center"><b>Elevator Simulation</b></h1>
<p align="center">
   <img src="https://cdn.discordapp.com/attachments/695010007808475147/830083364345479195/unknown.png" alt="Logo" width="540" height="80">
</p>

## About the Project
The project has been developed by 2 undergraduate students (Parminder Saini and Jims Nacaya) at Aston University, as part of the first-year module "Java Program Development".
Link to GitHub profiles:
<ul>
<li><a href = "https://github.com/Parmo7"   > Parminder </a></li>
<li><a href = "https://github.com/PochitaOP"> Jims  </a></li>
</ul>

### The Task
The task is to develop a simulation of an elevator within an office building. Different types of users will randomly enter the building and may attempt to use the elevator. The goal of the simulation is to see how much internal and external activity can be handled by the elevator. The types of users that may queue up to take the elevator are:
<ul>
<li>Non-Developer Employees : they attempt to go to any other floor with the same probability.</li>
<li>Developers: they only work in the top half of the building, and may randomly decide to move to another floor in the top half.</li>
<li>Maintenance crews: they always go to the top floor and requires 4 spaces in the elevator. </li>
<li>Clients: they enter the building and go to one of the floors in the bottom half of the building. After a few ticks, they return to the ground floor and leave. </li>
</ul>


## System Description
### Launchers
The system provides 3 different ways to run the program:
<ul>
<li>TextViewLauncher > Non-interactive text-based view. At each tick, it displays the status of the elevator and the floors on the console.</li>
<li>GUILauncher > Interactive graphical interface with two scenes: the first allows to set some parameters before running the simulation; the latter provides buttons to run the simulation (+1 tick, +10 ticks, +100 ticks) and shows the building accordingly.</li>
<li>CaseStudyLauncher > Takes different values of seed, p and q and runs an 8-hour simulation for each combination. After successfully running a simulation, it displays the combination that was employed, number of complaints, average waiting, etc.</li>
</ul>

### Setup
To start a simulation, the Simulation class needs to be provided with the following parameters: seed, p, q, probability of MaintenanceCrew, total Goggles, Mugtomes and NonDevelopers, number of floors and elevator capacity. To avoid a public constructor with too many parameters, a Builder class was employed - thus allowing the caller to create a simulation without specifying all parameters, if he is happy with the defaults.
When a Simulation object is built, it sets up the ArrivalSimulator, the WaitingStatistics and the Building. The Building, in turn, configures the floors and the elevator. Each floor contains two ArrayLists, respectively for users waiting in queue and staying on the floor.

### Ticking
When asked to tick, the simulation will delegate the ticking to:
<ul>
<li>ArrivalSimulator : in the first tick, it generates all the employees and passes them to the simulation so they can enter the building. At each tick, it also generates non-employees according to a probability. </li>
<li> Building : propagates the ticking to its entities, first the floors and then the elevator:
  <ul>
  <li>Floor : propagates the tick to each user on it
  <li>Elevator : at each tick, it checks for requests and starts moving accordingly. Behaves differently depending on whether the doors are open or closed, which lead to the creation of two private methods in it: tickIfOpen() and tickIfClosed().
  </ul>
</li>
</ul>
