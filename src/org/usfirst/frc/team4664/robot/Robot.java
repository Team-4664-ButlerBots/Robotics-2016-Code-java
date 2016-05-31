package org.usfirst.frc.team4664.robot;
import edu.wpi.first.wpilibj.CameraServer;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.RobotDrive;
import edu.wpi.first.wpilibj.SampleRobot;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.Victor;
import edu.wpi.first.wpilibj.DigitalImput;
public class Robot extends SampleRobot {
	//Systems
    RobotDrive driveTrain;
    Joystick joy1, joy2;
    //Motors
    Victor rightSide, leftSide;//Drive train motors
    Victor lattice, winch;//The Scissor lift & winch respectively
    Victor armCapture, armTorque;//armSpeed spins the intake wheels; armTorque moves input in out
    //Ports
    final int lsMotor	  = 0;
    final int rsMotor	  = 1;
    final int armCPort    = 9;
    final int armTPort	  = 2;
    final int latPort     = 4;
    final int winchPort   = 5;
    //joystick 2 buttons
    final int armCaptureB  = 6;
    final int armReleaseB  = 8;
    final int latticeUpB   = 3;
    final int latticeDownB = 2;
    final int winchOutB    = 4;
    final int winchInB     = 5;
    //speed variables
    final double armCaptureVal = 1.0;
    final double winchOut      = 1.0;
    final double winchIn       = -.7;
    final double latticeUp     = 0.8;
    final double latticeDown   = -.5;
    //dead band variables
    final double driveXDb    = 0.3;
    final double driveYDb    = 0.3;
    final double armTorqueDb = 0.2;
    //Laptop ports
    final int joy1Port	= 0;
    final int joy2Port  = 1;
    //Camera
    CameraServer eyeOfProvidence;
    //Limit Switches
    DigitalInput limUp;
    DigitalInput limDown;
    //Limit Switch Ports
    final int limUpPort = 0;
    final int limDownPort = 1;
    public Robot() {
    	rightSide  = new Victor(rsMotor);
    	leftSide   = new Victor(lsMotor);
    	armCapture = new Victor(armCPort);
    	armTorque  = new Victor(armTPort);
    	lattice    = new Victor(latPort);
    	winch      = new Victor(winchPort);
        driveTrain = new RobotDrive(leftSide, rightSide);
        joy1       = new Joystick(joy1Port);
        joy2       = new Joystick(joy2Port);
        eyeOfProvidence = CameraServer.getInstance();
        eyeOfProvidence.setQuality(25);
        eyeOfProvidence.startAutomaticCapture("cam0");
        limUp      = new DigitalInput(limUpPort);
        limDown    = new DigitalInput(limDownPort);
    	}
    
    public void operatorControl() {
        driveTrain.setSafetyEnabled(true);
        while (isOperatorControl() && isEnabled()) {
        	//Drivetrain
        	//driveTrain.arcadeDrive(Deadband(joy1.getX(), driveXDb), Deadband(joy1.getY(), driveYDb)); //joy1 is drive
        	driveTrain.tankDrive(-joy1.getY(), -joy1.getRawAxis(3)); //tankdrive
        	//Arm Code
        	if(!limUp.get && Deadband(-joy2.getY(), armTorqueDb) < 0){
        		armTorque.set(0);
        	}
        	else if(!limDown.get()&& Deadband(-joy2.getY(), armTorqueDb) > 0){
        		armTorque.set(0);
        	}
        	else{
        		armTorque.set(Deadband(-joy2.getY(), armTorqueDb));  //armTorque
        	}
        	if(joy2.getRawButton(6)){					//armSpeed
        		armCapture.set(armCaptureVal);
        	}else if(joy2.getRawButton(7)){
        		armCapture.set(-armCaptureVal);
        	}else{
        		armCapture.set(0.0);
        	}
        	if(joy1.getRawButton(9)){
                eyeOfProvidence = CameraServer.getInstance();
                eyeOfProvidence.setQuality(25);
                eyeOfProvidence.startAutomaticCapture("cam0");
        	}/*
        	//lift system code
        	if(joy2.getRawButton(latticeUpB)){					//lattice
        		lattice.set(latticeUp);
        	}else if(joy2.getRawButton(latticeDownB)){
        		lattice.set(latticeDown);
        	}else{
        		lattice.set(0);
        	}
        	if(joy2.getRawButton(winchOutB)){					//winch
        		winch.set(winchOut);
        	}else if(joy2.getRawButton(winchInB)){
        		winch.set(winchIn);
        	}else{
        		winch.set(0.0);
        	}*/
        	Timer.delay(0.005);	// wait 5ms to avoid hogging CPU cycles   
        }
    }
    
    public void autonomous(){
    	driveTrain.setSafetyEnabled(false);
    	armTorque.set(0.6);
    	Timer.delay(0.6);
    	armTorque.set(0.0);
    	driveTrain.tankDrive(-0.75, -0.75);
    	Timer.delay(3.0);
    	driveTrain.tankDrive(0.00, 0.00);
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