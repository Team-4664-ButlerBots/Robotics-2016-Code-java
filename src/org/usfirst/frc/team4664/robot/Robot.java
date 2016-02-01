package org.usfirst.frc.team4664.robot;

import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.RobotDrive;
import edu.wpi.first.wpilibj.RobotDrive.MotorType;
import edu.wpi.first.wpilibj.SampleRobot;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj.Victor;



 //This is a demo program showing how to use Mecanum control with the RobotDrive class.
 
public class Robot extends SampleRobot {
	
    RobotDrive RobotDrive;
    Joystick StickController;
    Joystick Jstick;
    
    int i;
    
    DigitalInput LSArm;
    DigitalInput LSClawUp;
    DigitalInput LSClawBot;
    
     //Channels for the wheels
    final int frontLeftChannel	= 2;
    final int rearLeftChannel	= 3;
    final int frontRightChannel	= 1;
    final int rearRightChannel	= 0;
    
    final int LSArmPort			= 0;
    final int LSClawUpPort		= 1;
    final int LSClawBotPort		= 2;
    
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
        Jstick 			= new Joystick(joystickChannel1);
        
        armLift  		= new Victor(4);
        clawTote 		= new Victor(5);
        
        LSArm     		= new DigitalInput(0);
        LSClawUp  		= new DigitalInput(1);
        LSClawBot 		= new DigitalInput(2);
    	}
    
      //Runs the motors with mecanum drive.
    public void Test() {
    	}
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
        			SmartDashboard.putNumber("Arm Up Speed", armLift.get());

            	}
            	else if(StickController.getRawButton(8)) {
            		armLift.set(.8);
        			SmartDashboard.putNumber("Arm Down Speed", armLift.get());

            	}
            	else if(StickController.getRawButton(6)) {
                	clawTote.set(.8);
        			SmartDashboard.putNumber("Claw Extend Speed", clawTote.get());

            	}
                else if(StickController.getRawButton(5)) {
                	clawTote.set(-.9);
        			SmartDashboard.putNumber("Claw Retract Speed", clawTote.get());
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
        			SmartDashboard.putNumber("Arm Up Speed", armLift.get());
            	}
            	else if(StickController.getRawButton(8)) {
            		armLift.set(.2);
        			SmartDashboard.putNumber("Arm Down Speed", armLift.get());
            	}
            	else if(StickController.getRawButton(6)) {
            		clawTote.set(.2);
        			SmartDashboard.putNumber("Claw Extend Speed", clawTote.get());
            	}
            	else if(StickController.getRawButton(5)) {
            		clawTote.set(-.3);
        			SmartDashboard.putNumber("Claw Retract Speed", clawTote.get());
            	}
            	else {
            		armLift.set(0);
            		clawTote.set(0);
            	}
            }
            Timer.delay(0.005);	// wait 5ms to avoid hogging CPU cycles
			SmartDashboard.putNumber("Robot Forward Speed", DeadBand(StickController.getX()));
			SmartDashboard.putNumber("Robot Backward Speed", DeadBand(StickController.getX()));
			SmartDashboard.putNumber("Robot Sideways Speed", DeadBand(StickController.getY()));

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
