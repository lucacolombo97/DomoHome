# DomoHome 
![4586710193](https://user-images.githubusercontent.com/48442855/139596263-c2ff57d9-0af2-4a88-81d9-fef767e7cd86.png)

*High School final examination project at I.I.S A.Volta - Lodi*

![DomoHome](https://user-images.githubusercontent.com/48442855/139595711-375a10b8-3cad-4816-8ceb-cab4f910f6f6.png)

## General Overview

Home automation is a recent discipline that studies technologies to improve the quality of life inside the house and is achieved through the use of three major technical disciplines: computer science, electronics and mechanics.  
Home automation can be thought of as a central system capable of managing and controlling the various automations implemented within the house, such as the automatic opening of a door, control over the lighting system and management of the security system.  
This technology uses "bus systems", a data line that connects the different devices and transmits all the control information to the central system, which is informed about everything that happens inside the house.  
The use of this recent technology brings with it many advantages, such as the possibility of an accurate monitoring of the conditions of the house, significant energy savings and the ability to control the systems wherever you are.

![HomeAutomation](https://user-images.githubusercontent.com/48442855/139595717-cd9e740f-401f-43ad-ba08-d786408aaf38.jpg)

## The Microcontrollers

A microcontroller is a small computer built on a single integrated circuit containing a CPU, a memory chip and several programmable input/output peripherals.
An example of a microcontroller is the Arduino board.  
Arduino, the "brain" of the project, is an electronic board controlled by an ATmega microcontroller and is equipped with Input/Output peripherals, thanks to which it receives the signals collected by the sensors, and after processing them interacts with the outside thanks to the actuators driven by the program loaded on it.  
In the "Domo Home" project two Arduino boards were used: 
1. "Arduino Uno", a standard board with basic functionality and
2. "Arduino Yun", which in addition implements an ethernet board and a wifi sensor that allow the microcontroller to communicate with Internet.

## Sensors and Actuators

List of sensors and actuators.  
Garden:
- A Photoresistor, a particular resistance whose value varies with the intensity of light
- A series of white LEDs, that simulate the outdoor lighting
- A servomotor, used to open and close the gate.

Living Room: 
- A temperature sensor, used to detect the temperature inside the house
- A fan, that simulates the cooling system
- A series of white LEDs, that simulate the lighting of the room.

Bedroom:
- An RGB led, a particular led that emits a colored light

Box:
- A servomotor, used for opening and closing the shutter.

Finally, the anti-intrusion system is realized through the use of three reed sensors for the detection of magnetic fields, placed on the windows and on the door, and a piezo, which simulates the alarm siren.

*Arduino Uno Wiring Diagram:*

![ArduinoUno](https://user-images.githubusercontent.com/48442855/139596258-d81bb0d0-add0-4d36-b8d8-e6ab5e71d821.jpg)

*Arduino Yun Wiring Diagram:*

![ArduinoYun](https://user-images.githubusercontent.com/48442855/139596261-cb38114d-8ee7-4f45-9e70-35d7be90bb29.jpg)










