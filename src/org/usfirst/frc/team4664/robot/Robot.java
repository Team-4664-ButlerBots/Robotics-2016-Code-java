package org.usfirst.frc.team4664.robot;

import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.RobotDrive;
import edu.wpi.first.wpilibj.RobotDrive.MotorType;
import edu.wpi.first.wpilibj.SampleRobot;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.Victor;


 //This is a demo program showing how to use Mecanum control with the RobotDrive class.
 
public class Robot extends SampleRobot {
	
    RobotDrive RobotDrive;
    Joystick StickController;
    Joystick Jstick;
    //DigitalInput LimitSwitchArm;
    //DigitalInput LimitSwitchClaw;
    
     //Channels for the wheels
    final int frontLeftChannel	= 2;
    final int rearLeftChannel	= 3;
    final int frontRightChannel	= 1;
    final int rearRightChannel	= 0;
    
    Victor armLift;
    Victor clawTote;
    
    boolean speedOverride = false;
    
    //The channel on the driver station that the joystick is connected to
    final int joystickChannel	= 0;
    final int joystickChannel1 	= 1;

    public Robot() {
        RobotDrive = new RobotDrive(frontLeftChannel, rearLeftChannel, frontRightChannel, rearRightChannel);
    	//RobotDrive.setInvertedMotor(MotorType.kFrontRight, true);	 //invert the left side motors
    	//RobotDrive.setInvertedMotor(MotorType.kRearRight, true);		 //you may need to change or remove this to match your robot
        RobotDrive.setExpiration(0.1);

        StickController = new Joystick(joystickChannel);
        Jstick = new Joystick(joystickChannel1);
        
        armLift = new Victor(4);
        clawTote = new Victor(5);
        //LimitSwitchArm = new DigitalInput(8);
        //LimitSwitchClaw = new DigitalInput(9);
    	}
    
      //Runs the motors with mecanum drive.
    public void autonomousPeriodic() {
    	RobotDrive.mecanumDrive_Cartesian(.25,0,0,0);
    	Timer.delay(1.5);
    	RobotDrive.mecanumDrive_Cartesian(0, 0, 0, 0);
    	armLift.set(-0.5);
    	Timer.delay(2.0);
    	armLift.set(0);
    }
    
    public void operatorControl() {
        RobotDrive.setSafetyEnabled(true);
        while (isOperatorControl() && isEnabled()) {
        	
        	// Use the joystick X axis for lateral movement, Y axis for forward movement, and Z axis for rotation.
        	// This sample does not use field-oriented drive, so the gyro input is set to zero.
            if(speedOverride = true) {
            	RobotDrive.mecanumDrive_Cartesian(DeadBand(StickController.getX()), DeadBand(StickController.getZ()), DeadBand(-StickController.getY()), 0);
                if(StickController.getRawButton(4)) {
            		speedOverride = false;
                	Timer.delay(.0625);
            	}
            	if(StickController.getRawButton(7)) {
                	armLift.set(-.9);
            	}
            	else if(StickController.getRawButton(8)) {
            		armLift.set(.8);
            	}
            	else if(StickController.getRawButton(6)) {
                	clawTote.set(.8);
            	}
                else if(StickController.getRawButton(5)) {
                	clawTote.set(-.9);
            	}
                else {
            		armLift.set(0);
            		clawTote.set(0);
                }  
            }
            else {
            	RobotDrive.mecanumDrive_Cartesian(DeadBand(StickController.getX()*.5), DeadBand(StickController.getZ()*.5), DeadBand(StickController.getY()*.5), 0);
            	if(StickController.getRawButton(4)) {
            		speedOverride = true;
            		Timer.delay(.0625);
            	}
            	if(StickController.getRawButton(7)) {
            		armLift.set(-.3);
            	}
            	else if(StickController.getRawButton(8)) {
            		armLift.set(.2);
            	}
            	else if(StickController.getRawButton(6)) {
            		clawTote.set(.2);
            	}
            	else if(StickController.getRawButton(5)) {
            		clawTote.set(-.3);
            	}
            	else {
            		armLift.set(0);
            		clawTote.set(0);
            	}
            Timer.delay(0.005);	// wait 5ms to avoid hogging CPU cycles
            }
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