#include <SoftwareSerial.h>
// rotary encoders - read wheel speed
int MOTOR_FL =7; //Front Left Motor
int MOTOR_FR =4; //Front Right Motor
int MOTOR_BL =6; //Back Left Motor
int MOTOR_BR =5; //Back Right Motor

SoftwareSerial mySerial(10, 11);
int values[8];
int i;
boolean a, b, c, d, e, f, g, h;
boolean forward_mode, backward_mode, 
        strafe_left_mode, strafe_right_mode, 
        rotate_CCW_mode,rotate_CW_mode,
        forward_diagonal_left_mode, forward_diagonal_right_mode,
        backward_diagonal_left_mode, backward_diagonal_right_mode,
        serial_mode;
int Lx, Ly;
int Rx, Ry;
int DIRECTION_FL=24;
int DIRECTION_FR=26;
int DIRECTION_BL=25;
int DIRECTION_BR=27;

void setup ()
{
  Serial.begin(38400);
  mySerial.begin(38400);
  serial_mode = false;
  pinMode (MOTOR_FL,OUTPUT);
  pinMode (DIRECTION_FL,OUTPUT);
  pinMode (MOTOR_FR,OUTPUT);
  pinMode (DIRECTION_FR,OUTPUT);
  pinMode (MOTOR_BL,OUTPUT);
  pinMode (DIRECTION_BL,OUTPUT);
  pinMode (MOTOR_BR,OUTPUT);
  pinMode (DIRECTION_BR,OUTPUT);
}

void loop ()
{
  i = 0;
  // work out speed over 10th second
   
   if(mySerial.available()){
    resetButtons();
    while(mySerial.available()){
        values[i] = mySerial.read();
        i++;
    }
    getButtons(values[5]);
    getLeftJoystick(values[4], values[3]);
    getRightJoystick(values[2], values[1]);
    Serial.print(a);
    Serial.print(b);
    Serial.print(c);
    Serial.print(d);
    Serial.print(e);
    Serial.print(f);
    Serial.print(g);
    Serial.print(h);
    Serial.print(" : ");
    Serial.print(Lx);
    Serial.print(":");
    Serial.print(Ly);
    Serial.print(" : ");
    Serial.print(Rx);
    Serial.print(":");
    Serial.print(Ry);
    Serial.println();
    if((Ly == 0 && Lx == 0) && (Ry == 1 && Rx == -4)){
        MotorBrake();
    } else{
       Drive();
       Drive2();
    }
   } else{
       resetButtons();
   }
 }
  
void getButtons(int n){
    if(n >= 128){
        a = true;
        n -= 128;
    }
    if(n >= 64){
        b = true;
        n -= 64;
    }
    if(n >= 32){
        c = true;
        n -= 32;
    }
    if(n >= 16){
        d = true;
        n -= 16;
    }
    if(n >= 8){
        e = true;
        n -= 8;
    }
    if(n >= 4){
        f = true;
        n -= 4;
    }
    if(n >= 2){
        g = true;
        n -= 2;
    }
    if(n >= 1){
        h = true;
        n -= 1;
    }
}

void resetButtons(){
    a = false;
    b = false;
    c = false;
    d = false;
    e = false;
    f = false;
    g = false;
    h = false;
    Lx = 0;
    Ly = 0;
    Rx = 0;
    Ry = 0; 
}

void getLeftJoystick(int x, int y){
    Lx = x - 128;
    Ly = y - 128;
}

void getRightJoystick(int x, int y){
    Rx = x - 128;
    Ry = y - 128;
}

void Drive(){
   if(Lx > 50  && Ly > 50){
        DiagonalFR(50);
   }else if(Lx < -50  && Ly < -50){
        DiagonalBL(50);
   }else if(Lx < -50 && Ly > 50){
        DiagonalFL(50);
   }else if(Lx > 50 && Ly < -50){
        DiagonalBR(50);
   }else if(Lx < -50){
        StrafeLeft(50);
    }
    else if(Lx > 50){
        StrafeRight(50);
    }
    else if(Ly < -50){
        Backward(50);
    }
    else if(Ly > 50){
        Forward(50);
    } else{
      //MotorBrake();
    }
}

void Drive2(){
  if(Rx < -50){
        RotateCCW(50);
    }
    else if(Rx > 50){
        RotateCW(50);
    }
    else{
      //MotorBrake();
    }
}
void SetSpeed(){
  
}
void Forward(int PWM_val){
  analogWrite(MOTOR_FL,PWM_val);
  analogWrite(MOTOR_FR,PWM_val);
  analogWrite(MOTOR_BL,PWM_val);
  analogWrite(MOTOR_BR,PWM_val);
 
  digitalWrite(DIRECTION_FL,LOW);
  digitalWrite(DIRECTION_FR,LOW);
  digitalWrite(DIRECTION_BL,HIGH);
  digitalWrite(DIRECTION_BR,HIGH);
}

void Backward(int PWM_val){
  analogWrite(MOTOR_FL,PWM_val);
  analogWrite(MOTOR_FR,PWM_val);
  analogWrite(MOTOR_BL,PWM_val);
  analogWrite(MOTOR_BR,PWM_val);
 
  digitalWrite(DIRECTION_FL,HIGH);
  digitalWrite(DIRECTION_FR,HIGH);
  digitalWrite(DIRECTION_BL,LOW);
  digitalWrite(DIRECTION_BR,LOW);
}

void StrafeLeft(int PWM_val){

  analogWrite(MOTOR_FL,PWM_val);
  analogWrite(MOTOR_FR,PWM_val);
  analogWrite(MOTOR_BL,PWM_val);
  analogWrite(MOTOR_BR,PWM_val);

  digitalWrite(DIRECTION_FL,HIGH);
  digitalWrite(DIRECTION_FR,LOW);
  digitalWrite(DIRECTION_BL,HIGH);
  digitalWrite(DIRECTION_BR,LOW);
}

void StrafeRight(int PWM_val){
  analogWrite(MOTOR_FL,PWM_val);
  analogWrite(MOTOR_FR,PWM_val);
  analogWrite(MOTOR_BL,PWM_val);
  analogWrite(MOTOR_BR,PWM_val);

  digitalWrite(DIRECTION_FL,LOW);
  digitalWrite(DIRECTION_FR,HIGH);
  digitalWrite(DIRECTION_BL,LOW);
  digitalWrite(DIRECTION_BR,HIGH);
  
}

void RotateCW(int PWM_val){
  analogWrite(MOTOR_FL,PWM_val);
  analogWrite(MOTOR_FR,PWM_val);
  analogWrite(MOTOR_BL,PWM_val);
  analogWrite(MOTOR_BR,PWM_val);

  digitalWrite(DIRECTION_FL,LOW);
  digitalWrite(DIRECTION_FR,HIGH);
  digitalWrite(DIRECTION_BL,HIGH);
  digitalWrite(DIRECTION_BR,LOW);
}

void RotateCCW(int PWM_val){
  analogWrite(MOTOR_FL,PWM_val);
  analogWrite(MOTOR_FR,PWM_val);
  analogWrite(MOTOR_BL,PWM_val);
  analogWrite(MOTOR_BR,PWM_val);

  digitalWrite(DIRECTION_FL,HIGH);
  digitalWrite(DIRECTION_FR,LOW);
  digitalWrite(DIRECTION_BL,LOW);
  digitalWrite(DIRECTION_BR,HIGH);
}

void DiagonalBL(int PWM_val){
  analogWrite(MOTOR_FL,PWM_val);
  analogWrite(MOTOR_FR,0);
  analogWrite(MOTOR_BL,0);
  analogWrite(MOTOR_BR,PWM_val);

  digitalWrite(DIRECTION_FL,HIGH);
  digitalWrite(DIRECTION_BR,LOW);
}

void DiagonalBR(int PWM_val){
  analogWrite(MOTOR_FL,0);
  analogWrite(MOTOR_FR,PWM_val);
  analogWrite(MOTOR_BL,PWM_val);
  analogWrite(MOTOR_BR,0);

  digitalWrite(DIRECTION_FR,HIGH);
  digitalWrite(DIRECTION_BL,LOW);
}

void DiagonalFL(int PWM_val){
  analogWrite(MOTOR_FL,0);
  analogWrite(MOTOR_FR,PWM_val);
  analogWrite(MOTOR_BL,PWM_val);
  analogWrite(MOTOR_BR,0);

  digitalWrite(DIRECTION_FR,LOW);
  digitalWrite(DIRECTION_BL,HIGH);
}

void DiagonalFR(int PWM_val){
  analogWrite(MOTOR_FL,PWM_val);
  analogWrite(MOTOR_FR,0);
  analogWrite(MOTOR_BL,0);
  analogWrite(MOTOR_BR,PWM_val);

  digitalWrite(DIRECTION_FR,HIGH);
  digitalWrite(DIRECTION_BL,LOW);
}

void MotorBrake(){
  analogWrite(MOTOR_FL,0);
  analogWrite(MOTOR_FR,0);
  analogWrite(MOTOR_BL,0 );
  analogWrite(MOTOR_BR,0);
}
