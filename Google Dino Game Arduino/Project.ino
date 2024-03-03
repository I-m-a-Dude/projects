#include <Wire.h>
#include <LiquidCrystal_I2C.h>
#include <TimerOne.h>
#include "bitmaps.h"

LiquidCrystal_I2C lcd(0X3F, 20, 4);

boolean dinoCheck = true;

int joystickXPin = A0;  // Connect X-axis of joystick to analog pin A0
int joystickYPin= A1;
int joystickButtonPin = 22;  // Connect button of joystick to digital pin 2


int highScore = 0;

boolean startCheck = false;


int d1 = 0;
int d2 = 0;
int scor = 0;

unsigned long previousMillis = 0;  // will store last time the function was called
const unsigned long interval = 5000;  // default interval in milliseconds

void customDelay(unsigned long duration) {
  unsigned long currentMillis = millis();

  while (millis() - currentMillis < duration) {
    // Wait for the specified duration without blocking the program
  }
}

void setup() {
  lcd.init();
  lcd.init();
  lcd.backlight();
  lcd.begin(16, 2);
  lcd.createChar(7, dino);
  lcd.createChar(6, cacti);
  lcd.createChar(5, bird);
  lcd.setCursor(0, 1);
  lcd.write(7);
  pinMode(joystickXPin, INPUT);
  pinMode(joystickYPin, INPUT);
  pinMode(joystickButtonPin, INPUT_PULLUP);
  Serial.begin(9600);

  //Timer1.initialize(10000000); // Set a timer of length 1000000 microseconds (or 1 second)

  //Timer1.attachInterrupt(callback); // Attach the service routine here
}

void loop() {
  lcd.clear();

  if (!startCheck) {
    lcd.setCursor(0, 0);
    lcd.print("Press to start");

    if (digitalRead(joystickButtonPin) == LOW) {
      startCheck = true;
    }
  }

  if (startCheck) {
    startGame();
  }

  //delay(100);
  customDelay(100);
 //Timer1.initialize(500000);  // Timer period in microseconds (500 milliseconds)
}

void endGame() {
  lcd.setCursor(0, 1);
  lcd.print("HIGH: ");
  lcd.print(highScore);
  startCheck = false;
  //delay(500);
  customDelay(interval);
  //time(5000);
 // Timer1.initialize(500000);  // Timer period in microseconds (500 milliseconds)
  
  scor = 0;
}

void startGame() {
  lcd.clear();
  d1 = random(4, 9);
  d2 = random(4, 9);

  for (int i = 15; i >= -(d1 + d2); i--) {
    lcd.setCursor(13, 0);
    lcd.print(scor);

    int joystickXValue = analogRead(joystickXPin);
    int joystickYValue = analogRead(joystickYPin);
    int joystickButtonState = digitalRead(joystickButtonPin);

    if (joystickButtonState == LOW || joystickXValue == LOW || joystickYValue == LOW) {
 
      if (dinoCheck) {
        lcd.setCursor(1, 0);
        lcd.write(7);
        lcd.setCursor(1, 1);
        lcd.print(" ");
        dinoCheck = false;
      }
    } else {
      if (!dinoCheck) {
        lcd.setCursor(1, 1);
        lcd.write(7);
        lcd.setCursor(1, 0);
        lcd.print(" ");
        dinoCheck = true;
      }
    }

    lcd.setCursor(i, 1);
    lcd.write(6);
    lcd.setCursor(i + 1, 1);
    lcd.print(" ");

    lcd.setCursor(i + d1, 1);
    lcd.write(6);
    lcd.setCursor(i + d1 + 1, 1);
    lcd.print(" ");

    int x = i + (d2 - d1);
    if (abs(x - i) < 2 || abs(x - (i + d1)) < 2 || abs(x - (i + d1 + d2)) < 2) {
      x += 2;
    }

    lcd.setCursor(x, 0);
    lcd.write(5);
    lcd.setCursor(x + 1, 0);
    lcd.print(" ");

    lcd.setCursor(i+d1+d2, 1);
    lcd.write(6);
    lcd.setCursor(i+d1+d2+1, 1);
    lcd.print(" ");

    if ((i + d1 + d2) == -1) {
      i = 15;
    }

    if (i == 1 && (dinoCheck == true)) {

      lcd.clear();
      lcd.print("Aia e.....");
      if (scor > highScore) {
        highScore = scor;
      }
      endGame();
      break;
    } else if (i + d1 == 1 && (dinoCheck == true)) {

      lcd.clear();
      lcd.print("Aia e.....");
      if (scor > highScore) {
        highScore = scor;
      }
      endGame();
      break;
    } else if (i + d1 + d2 == 1 && (dinoCheck == true)) {

      lcd.clear();
      lcd.print("Aia e.....");
      if (scor > highScore) {
        highScore = scor;
      }
      endGame();
      break;
    } else if (x == 1 && (dinoCheck == false)) {
  
      lcd.clear();
      lcd.print("Aia e.....");
      if (scor > highScore) {
        highScore = scor;
      }
      endGame();
      break;
    }

    /*if (flag) {
    // Code that should be executed after the delay
    scor++;
    flag = false; // Reset the flag
  } */
  scor++;
  //delay(500);
  customDelay(500);
 // time(5000);
  }
}

