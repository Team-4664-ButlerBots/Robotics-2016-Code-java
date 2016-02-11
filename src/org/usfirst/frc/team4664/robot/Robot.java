package org.usfirst.frc.team4664.robot;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.RobotDrive;
import edu.wpi.first.wpilibj.SampleRobot;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.Victor; 
public class Robot extends SampleRobot {
	//Systems
    RobotDrive TankDrive;
    Joystick StickController;
    //Motors
    Victor rightSide;
    Victor leftSide;
    Victor arm;
    Victor capture;
    
    final int lsMotor	  = 0;
    final int rsMotor	  = 1;
    final int armPort     = 2;
    final int capturePort = 3;
    
    //The channel on the driver station that the joystick is connected to
    final int joystickChannel	= 0;

    public Robot() {
    	rightSide = new Victor(rsMotor);
    	leftSide  = new Victor(lsMotor);
    	arm       = new Victor(armPort);
    	capture   = new Victor(capturePort);
    	
        TankDrive = new RobotDrive(leftSide, rightSide);
    	
        StickController = new Joystick(joystickChannel);
    	}
    
    public void operatorControl() {
        TankDrive.setSafetyEnabled(true);
        while (isOperatorControl() && isEnabled()) {
        	TankDrive.tankDrive(DeadBand(-StickController.getY()), DeadBand(-StickController.getRawAxis(3)));
        	if(StickController.getRawButton(6)) {
        		capture.set(1.0);
        	}
        	else if(StickController.getRawButton(5)) {
        		capture.set(-1.0);
        	}
        	else {
        		capture.set(0);
        	}
        	
        	Timer.delay(0.005);	// wait 5ms to avoid hogging CPU cycles   
        }
    }
    //Useful functions
        double DeadBand(double input) {
        	if(Math.abs(input) < 0.1) {
        		return 0;
        	}
        	else {
        		return input;
        	}
        }
}