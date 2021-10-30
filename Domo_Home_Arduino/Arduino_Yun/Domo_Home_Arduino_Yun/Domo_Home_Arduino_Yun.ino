#include <Bridge.h>
#include <HttpClient.h>
#include <Servo.h>

Servo servoBox,servoCancello;
HttpClient client;
String URL;
int red=0;
int green=0;
int blue=0;
int intervallo=0;
bool on=true;
volatile bool onAllarme=false; //controlla se scatta l'allarme
bool statoAllarme=false; //controlla se l'allarme è attivo
float tempAttuale;

int pinVentola=2, pinVerde=3, pinLed=4, pinBlu=5, pinRosso=6, pinAllarme=7,pinIntrusione=8, pinBox=9,pinCancello=10;

void setup() 
{
  digitalWrite(pinIntrusione,LOW);
  Bridge.begin();
  pinMode(pinVentola,OUTPUT);           //ventola
  pinMode(pinLed,OUTPUT);               //led sala
  pinMode(pinVerde,OUTPUT);             //pin Verde RGB
  pinMode(pinBlu,OUTPUT);               //pin Blue RGB
  pinMode(pinRosso,OUTPUT);             //pin Red RGB
  pinMode(pinIntrusione,OUTPUT);        //pin per comunicare con Arduino Uno e scrivere su lcd
  servoBox.attach(pinBox);              //porta su cui è collegato il servo box
  servoCancello.attach(pinCancello);    //porta su cui è collegato il servo cancello  
  attachInterrupt(digitalPinToInterrupt(pinAllarme), interruptAllarme, RISING);
}

void loop() 
{  
  if(onAllarme)
  {
    digitalWrite(pinIntrusione,HIGH);
    setSuonato();
    onAllarme=false;   
  }
  else
  {
    if((millis()/30000>intervallo))  //ogni 30 secondi
    {
      intervallo++;
      on=true;
    }
  
    if(on)
    {
      tempAttuale=temperatura();
      on=false;
    }
  
    comandiTemperatura();
    comandiLed();
    comandiRGB();
    comandiServoBox();
    comandiServoCancello();
  }    
  comandiAllarme();
  comandiSuonoAllarme();
}

void setSuonato()
{
  URL="http://lucacolombo.altervista.org/Domo_Home/set_comandi_allarme_arduino.php";  //set suonato in tabella allarme
  client.get(URL);
  
  URL="http://lucacolombo.altervista.org/Domo_Home/notifica_push.php";   //invia notifica ad android
  client.get(URL);
}

void comandiSuonoAllarme()
{
  URL="http://lucacolombo.altervista.org/Domo_Home/lettura_comandi_allarme_suonato.php";
  client.get(URL);

  if(!client.available())
  {
    digitalWrite(pinIntrusione,LOW);
  }
  else
  {
    while(client.available())
    {
      digitalWrite(pinIntrusione,HIGH);
      client.read();
      client.get(URL);
    }
  }
}

void comandiAllarme()
{
  URL="http://lucacolombo.altervista.org/Domo_Home/lettura_comandi_allarme.php";
  client.get(URL);

  if(!client.available())
  {
    statoAllarme=false;
  }
  else
  {
    statoAllarme=true;
    client.read();    
  }
}

void comandiLed()
{
  URL="http://lucacolombo.altervista.org/Domo_Home/lettura_comandi_led.php";
  client.get(URL);
  
  if(!client.available())
  {
      digitalWrite(pinLed,LOW);
  }
  else
  {
      digitalWrite(pinLed,HIGH);
      client.read();
  }
}

void comandiRGB()
{
  URL="http://lucacolombo.altervista.org/Domo_Home/lettura_comandi_RGB.php";
  client.get(URL);
  
  int letturaR[3];
  int letturaG[3];
  int letturaB[3];
  int r=-1;
  int g=-1;
  int b=-1;
  bool continua=true;
  
  char c;
  if(!client.available())
  {
    analogWrite(pinVerde,0);
    analogWrite(pinBlu,0);
    analogWrite(pinRosso,0);
  }
  else
  {
    while(client.available())
    {
      while((r<2)&&(continua))
      {
        c=client.read()-48;
        if(c!=-1)
        {
          r++;
          letturaR[r]=c;
        }
        else
        {
          continua=false;  
        }
      } 
      if(continua)
        c=client.read();
        
      continua=true; 
           
      while((g<2)&&(continua))
      {
        c=client.read()-48;
        if(c!=-1)
        {
          g++;
          letturaG[g]=c;
        }
        else
        {
          continua=false;
        }
      }
      if(continua)
        c=client.read();
        
      continua=true;      
      while((b<2)&&(continua))
      {
        c=client.read()-48;
        if(c!=-1)
        {
          b++;
          letturaB[b]=c;
        }
        else
        {
          continua=false;
        }
      }
      if(continua)
        c=client.read();
        
      continua=true;

      red=convertRGB(letturaR,r);
      green=convertRGB(letturaG,g);
      blue=convertRGB(letturaB,b);

      analogWrite(pinRosso,red);
      analogWrite(pinVerde,green);
      analogWrite(pinBlu,blue);
     }
  }
}

int convertRGB(int lettura[],int r)
{
  int numero;
  
    if(r==2) //es. 255
    {
      numero=lettura[0]*100 + lettura[1]*10 + lettura[2];
    }
    else if(r==1) //es. 50
    {
      numero=lettura[0]*10 + lettura[1];
    }
    else if(r==0) //es. 5
    {
      numero=lettura[0];   
    }    
    return numero; 
}

void comandiServoBox()
{

  URL="http://lucacolombo.altervista.org/Domo_Home/lettura_comandi_motore.php?id=1";
  client.get(URL);
  
  if (client.available())
  {
    if(client.read()=='0')
    {      
      for(int i=85;i>=8;i--)
      {
        servoBox.write(i);  
        delay(40);
      }
    }
    else
    {
      for(int i=8;i<=85;i++)
      {
        servoBox.write(i);  
        delay(40);
      }
    }
  }
}

void comandiServoCancello()
{

  URL="http://lucacolombo.altervista.org/Domo_Home/lettura_comandi_motore.php?id=2";
  client.get(URL);
  
  if (client.available())
  {
    if(client.read()=='0')
    {
      for(int i=80;i>=5;i--)
      {
        servoCancello.write(i);  
        delay(40);
      }
    }
    else
    {
      for(int i=5;i<=80;i++)
      {
        servoCancello.write(i);  
        delay(40);
      }
    }
  }
}

void comandiTemperatura()
{
  URL="http://lucacolombo.altervista.org/Domo_Home/lettura_temperatura_arduino.php";
  client.get(URL);
  int lettura[4];
  float numero;
  
  if(!client.available())
  {
    digitalWrite(pinVentola,LOW);
  }
  else
  {
    while (client.available())
    {
      for(int i=0;i<4;i++)
      {
        char c = client.read();
        lettura[i]=c - 48;
      }    
    
      numero=convertTemperatura(lettura);

      if(numero<tempAttuale)
      {
        digitalWrite(pinVentola,HIGH);   
      }
      else
      {
        digitalWrite(pinVentola,LOW);
      }
    }
  }
}

float convertTemperatura(int lettura[])
{
  float numero;
  
  if(lettura[2] == (-2)) //es. 18.5
    {
      numero=lettura[0]*10 + lettura[1] + lettura[3]* .1;
    }
    else if((lettura[2] == (-49)) & (lettura[3] == (-49))) //es. 18 gradi
    {
      numero=lettura[0]*10 + lettura[1];
    }
    else if(lettura[1] == (-2)) //es. 8.5 gradi
    {
      numero=lettura[0] + lettura[2]* .1;   
    }
    else  //es 8 gradi
    {
      numero=lettura[0];
    }   
    return numero; 
}

float temperatura()
{
  int valore = analogRead(A0);
  float voltaggio = (valore / 1024.0) * 5.0;
  float tempAttuale = (voltaggio - .5) * 100;
  if(tempAttuale>0)
  {
    aggiornaTemperatura(tempAttuale);
  }
  return tempAttuale;
}

void aggiornaTemperatura(float temperatura)
{
  URL="http://lucacolombo.altervista.org/Domo_Home/aggiorna_temperatura.php?temperatura=";
  URL+=temperatura;
  client.get(URL);
}

void interruptAllarme()
{
  Serial.println(digitalRead(pinAllarme));
  if((statoAllarme)&&(digitalRead(pinAllarme)==HIGH))
  {   
    digitalWrite(pinIntrusione,HIGH);
    onAllarme=true;    
  }
}

