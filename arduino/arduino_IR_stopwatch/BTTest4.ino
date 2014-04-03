/*
  Multicolor Lamp (works with Amarino and the MultiColorLamp Android app)
  
  - based on the Amarino Multicolor Lamp tutorial
  - receives custom events from Amarino changing color accordingly
  
  author: Bonifaz Kaufmann - December 2009
*/
 
#include <MeetAndroid.h>

float p[5];
int i = 0;
int Threshold = 150;
int LEDpin = 12;

// declare MeetAndroid so that you can call functions with it
MeetAndroid meetAndroid;
int startflag = 0;
const int irReceiver = 2;

// we need 3 PWM pins to control the leds

void setup()  
{
  // use the baud rate your bluetooth module is configured to 
  // not all baud rates are working well, i.e. ATMEGA168 works best with 57600
  Serial.begin(9600); 
  
  // register callback functions, which will be called when an associated event occurs.
  meetAndroid.registerFunction(StopCount, 'o');
  meetAndroid.registerFunction(ArdSetting,'s');

  pinMode(LEDpin, OUTPUT);
}

void loop()
{
  meetAndroid.receive(); // you need to keep this in your loop() to receive events
  int dist = Count_distnce();
  //int avg = average_filter(dist);
  //Serial.println(avg);
    if(dist < Threshold)
    {
      digitalWrite(LEDpin, HIGH);
      meetAndroid.send(dist);
    }
    else
      digitalWrite(LEDpin, LOW);    
}

/*
 * Whenever the multicolor lamp app changes the red value
 * this function will be called
 */
void StopCount(byte flag, byte numOfValues)
{
   int received = meetAndroid.getInt();  
}

void ArdSetting(byte flag, byte numOfValues)
{
   Threshold = meetAndroid.getInt();  
}

int Count_distnce()
{
  float volts = analogRead(A0)*0.0048828125;   // value from sensor * (5/1024) - if running 3.3.volts then change 5 to 3.3
  int distance = (int)(65*pow(volts, -1.10));          // worked out from graph 65 = theretical distance / (1/Volts)S - luckylarry.co.
  return distance;
  delay(100); 
}  

int average_filter(int average)
{
  p[i] = average;
  i++;
  if(i >= 5)
    i = 0;
  
  average = (p[0] + p[1] + p[2] + p[3] + p[4])/5;
  
  return (int)average;
}


