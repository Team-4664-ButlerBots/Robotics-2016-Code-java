package org.usfirst.frc.team4664.robot;

import edu.wpi.first.wpilibj.Counter;
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
    
    Counter LSCArm;
    Counter LSCClawUp;
    Counter LSCClawBot;
    
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
        
        LSCArm 			= new Counter(LSArm);
        LSCClawUp 		= new Counter(LSClawUp);
        LSCClawBot 		= new Counter(LSClawBot);
        
    	}
    
      //Runs the motors with mecanum drive.
    public void Test() {
    	
    	for(i = 0; i < 50; i++) {
    		if(LSCArm = 1) {
    			SmartDashboard.putString("Arm Limit Switch","Active");
    			Timer.delay(.5);
    		}
    		if(LSCClawUp = 1) {
    			SmartDashboard.putString("Claw Up Limit Switch","Active");
    			Timer.delay(.5);
    		}
    		if(LSCClawBot = 1) {
    			SmartDashboard.putString("Claw Bot Limit Switch","Active");
    			Timer.delay(.5);
    		}
    		if(StickController.getRawButton(7)) {
    			SmartDashboard.putString("Arm Goes Up","");
    			Timer.delay(.5);
    		}
    		if(StickController.getRawButton(8)) {
    			SmartDashboard.putString("Arm Goes Down","");
    			Timer.delay(.5);
    		}
    		if(StickController.getRawButton(6)) {
    			SmartDashboard.putString("Claw Goes Out","");
    			Timer.delay(.5);
    		}
    		if(StickController.getRawButton(5)) {
    			SmartDashboard.putString("Claw Goes In","");
    			Timer.delay(.5);
    		}
    		if(StickController.getX() > .8) {
    			SmartDashboard.putString("Robot Moves Forward","");
    			Timer.delay(.5);
    		}
    		if(StickController.getX() > -.8) {
    			SmartDashboard.putString("Robot Moves Backward","");
    			Timer.delay(.5);
    		}
    		if(Math.abs(StickController.getY()) > .8) {
    			SmartDashboard.putString("Robot Moves Sideways","");
    			Timer.delay(.5);
    		}
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
