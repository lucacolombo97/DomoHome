#include<LiquidCrystal.h>
LiquidCrystal lcd(12,11,6,5,4,3);

int valoreFotoresistenza=0;
const int pinFotoresistenza=A0;
const int valoreMin=550;        //valore oltre il quale si accende il led
int intervallo=0;               //intervallo di tempo per le scritte schermo LCD 
int intervallo2=0;              //intervallo per sirena allarme
int suono[2]={500,700};         //frequenza suoni per sirena allarme
bool on=true,on2=true;          //inversione scritta e suoni
bool attivo=false;

int pinIntrusione=2, pinSirena=7, pinContrasto=10,ledGiardino=13;

void setup() {
  lcd.begin(16,2);             //16 caratteri, 2 righe
  pinMode(pinContrasto,OUTPUT);
  pinMode(ledGiardino,OUTPUT);
  analogWrite(pinContrasto,60);
  noTone(pinSirena);
  attachInterrupt(digitalPinToInterrupt(pinIntrusione), interruptAllarme, CHANGE);
}

void loop() 
{
  if(!attivo)
  {
    if((millis()/10000>intervallo))
    {
      intervallo++;
      on=!on;
    }
    if(on)
    {     
      lcd.setCursor(0,0);             
      lcd.print("   Domo Home   ");
      lcd.setCursor(0,1);               
      lcd.print("Benvenuti a casa");
      noTone(pinSirena);
    }
    else
    {  
      noTone(pinSirena);
      lcd.setCursor(0,0);
      lcd.print("  Progetto di:  ");
      lcd.setCursor(0,1);
      lcd.print("  Luca Colombo  ");
    }
  }
  else
  {
    if(on2)
    {  
      tone(pinSirena,suono[0]);
      lcd.setCursor(0,0);             
      lcd.print("  INTRUSIONE!!  ");
      lcd.setCursor(0,1);             
      lcd.print("  INTRUSIONE!!  ");
    }
    else
    {
      tone(pinSirena,suono[1]);
      lcd.setCursor(0,0);             
      lcd.print("                ");
      lcd.setCursor(0,1);             
      lcd.print("                ");
    }
    
    if(millis()/350>intervallo2)
    {
      intervallo2++;
      on2=!on2;
    }
    delay(350);    
  }
  fotoresistenza();
}

void fotoresistenza()
{
  valoreFotoresistenza=analogRead(pinFotoresistenza);
  if(valoreFotoresistenza<valoreMin)
  {
    digitalWrite(ledGiardino,HIGH);
  }
  else
  {
    digitalWrite(ledGiardino,LOW);
  }
}

void interruptAllarme()
{
  if(digitalRead(pinIntrusione)==HIGH)
    attivo=true;
  else
    attivo=false;
}


