package org.usfirst.frc.team4664.robot;
import edu.wpi.first.wpilibj.CameraServer;
import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.RobotDrive;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj.SampleRobot;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.Victor;
public class Robot extends SampleRobot {
	//Systems
    RobotDrive driveTrain;
    Joystick joy1, joy2;
    //Motors
    Victor rightSide, leftSide;//Drive train motors
    Victor lattice, winch;//The Scissor lift & winch respectively
    Victor armSpeed, armTorqueLeft, armTorqueRight;//armSpeed spins the intake wheels; armTorque moves input in out
    //Camera
    CameraServer eyeOfProvidence;
    //SmartDashboard Stuff
    String ArmStatus;
    String CaptureStatus;
    //Limit Switch
    DigitalInput LSArm;
    final int LSArmPort = 0;
    //Ports
    final int lsMotor	  = 0; //Left Side Motor
    final int rsMotor	  = 1; //Right Side Motor
    final int armTPortL   = 2; //Arm Left Port
    final int armTPortR   = 3; //Arm Right Port
    final int armCPort	  = 4; //Arm Capture Port
    final int latPort     = 5; //Lattice (scissor lift) port
    final int winchPort   = 6; //Winch Port
    //Joystick 2 buttons
    final int armCaptureB  = 6;
    final int armReleaseB  = 7;
    final int latticeUpB   = 3;
    final int latticeDownB = 2;
    final int winchOutB    = 4;
    final int winchInB     = 5;
    //Speed variables
    final double armCSpeedVal  = -1.0;
    final double armRSpeedVal  = 1.0;
    final double winchOut      = 1.0;
    final double winchIn       = -.7;
    final double latticeUp     = 0.8;
    final double latticeDown   = -.5;
    //Dead band variables
    final double driveXDb    = 0.3;
    final double driveYDb    = 0.3;
    final double armTorqueDb = 0.2;
    //Laptop ports
    final int joy1Port	= 1;
    final int joy2Port  = 2;
    
    public Robot() {
    	//Drive systems
    	rightSide      = new Victor(rsMotor);
    	leftSide       = new Victor(lsMotor);
    	armSpeed       = new Victor(armCPort);
    	armTorqueLeft  = new Victor(armTPortL);
    	armTorqueRight = new Victor(armTPortR);
    	lattice        = new Victor(latPort);
    	winch          = new Victor(winchPort);
        driveTrain     = new RobotDrive(leftSide, rightSide);
        //Joysticks
        joy1   = new Joystick(joy1Port);
        joy2   = new Joystick(joy2Port);
        //Camera
        eyeOfProvidence = CameraServer.getInstance();
        eyeOfProvidence.setQuality(25);
        eyeOfProvidence.startAutomaticCapture("cam0");
        //Limit Switch
        LSArm = new DigitalInput(LSArmPort);
    	}
    
    public void operatorControl() {
        driveTrain.setSafetyEnabled(true);
        while (isOperatorControl() && isEnabled()) {
        	//drive train
        	driveTrain.arcadeDrive(joy1); //joy1 is drive
        	//Arm Code
        	if(joy2.getY() >= armTorqueDb && LSArm.get() == true) {
        		armTorqueLeft.set(Deadband(joy2.getY(), armTorqueDb));   //armTorqueLeft
            	armTorqueRight.set(Deadband(-joy2.getY(), armTorqueDb)); //armTouqueRight
            	ArmStatus = "Raising";
        	}else if (joy2.getY() < -armTorqueDb) {
        		armTorqueLeft.set(Deadband(joy2.getY(), armTorqueDb));   //armTorqueLeft
            	armTorqueRight.set(Deadband(-joy2.getY(), armTorqueDb)); //armTouqueRight
            	ArmStatus = "Lowering";
        	}
        	else {
        		armTorqueLeft.set(0);
        		armTorqueRight.set(0);
        		ArmStatus = "False";
        	}
        	if(joy2.getRawButton(armCaptureB)){					//armSpeed
        		armSpeed.set(armCSpeedVal);
        	}else if(joy2.getRawButton(armReleaseB)){
        		armSpeed.set(armRSpeedVal);
        	}else{
        		armSpeed.set(0.0);
        	}
        	
        	if(joy2.getRawButton(winchOutB)){					//winch
        		winch.set(winchOut);
        	}else if(joy2.getRawButton(winchInB)){
        		winch.set(winchIn);
        	}else{
        		winch.set(0.0);
        	}
        	SmartDashboard.putString("Arm Status", ArmStatus);
        	SmartDashboard.putBoolean("Arm Limit Switch", LSArm.get());
        	Timer.delay(0.005);	// wait 5ms to avoid hogging CPU cycles
        }
    }
    
    void Autonomous(){
    }
    
    void Test(){
	}

	double Deadband(double rawValue, double deadband) {
		rawValue = Limit(rawValue);
		if(Math.abs(rawValue) < deadband) return 0.0;
		if(rawValue > 0)			  return (rawValue - deadband) / (1.0 - deadband);
									  return (rawValue + deadband) / (1.0 - deadband);
	}

	double Limit(double value) {
		if(value > 1.0)  return 1.0;
		if(value < -1.0) return -1.0;
						 return value;
	}
}