package org.usfirst.frc.team4664.robot;

import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.RobotDrive;
import edu.wpi.first.wpilibj.SampleRobot;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj.Victor;



public class Robot extends SampleRobot {
	
    RobotDrive RobotDrive;
    Joystick StickController;
    Joystick Jstick;
    
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
    
    double clock = 0;
    
    //The channel on the driver station that the joystick is connected to
    final int joystickChannel	= 0;
    final int joystickChannel1 	= 1;

    public Robot() {
        RobotDrive = new RobotDrive(frontLeftChannel, rearLeftChannel, frontRightChannel, rearRightChannel);
    	//RobotDrive.setInvertedMotor(MotorType.kFrontRight, true);	 		//invert the left side motors
    	//RobotDrive.setInvertedMotor(MotorType.kRearRight, true);			//you may need to change or remove this to match your robot
        RobotDrive.setExpiration(0.1);

        StickController = new Joystick(joystickChannel);
        Jstick 			= new Joystick(joystickChannel1);
        
        armLift  		= new Victor(4);
        clawTote 		= new Victor(5);
        
        LSArm     		= new DigitalInput(LSArmPort);
        LSClawUp  		= new DigitalInput(LSClawUpPort);
        LSClawBot 		= new DigitalInput(LSClawBotPort);
    	
    
    	}
    
      //Runs the motors with mecanum drive.
    
    public void Test {
    	while(isEnabled) {
    		SmartDashboard.putString("Testing", "Active");
    		SmartDashboard.putNumber("Timer", clock);
    		SmartDashboard.putBoolean("Testing", isEnabled);
    		clock = clock = .05;
    		Timer.delay(.05);
    	}
    }
    
    public void operatorControl() {
        RobotDrive.setSafetyEnabled(true);
        while (isOperatorControl() && isEnabled()) {

            if(speedOverride = true) {
            	RobotDrive.mecanumDrive_Cartesian(DeadBand(StickController.getX()), DeadBand(StickController.getZ()), DeadBand(-StickController.getY()), 0);
                if(StickController.getRawButton(4)) {
            		speedOverride = false;
                	Timer.delay(.1);
            	}
                
            	if(LSArm.get()) {																			//Limit Switches
            		SmartDashboard.putString("Arm LS", "Reached");				
            		armLift.set(.8);
            		Timer.delay(.2);
            		armLift.set(0);
            	}
            	else if(LSClawUp.get()) {
            		SmartDashboard.putString("Claw Up LS", "Reached");
            		clawTote.set(-.8);
            		Timer.delay(.2);
            		clawTote.set(0);
            	}
            	else if(LSClawBot.get()) {
            		SmartDashboard.putString("Claw Bot LS", "Reached");
            		clawTote.set(.8);
            		Timer.delay(.2);
            		clawTote.set(0);
            	}
            	else {
            		SmartDashboard.putString("Arm LS", "Not Reached");
            		SmartDashboard.putString("Claw Up LS", "Not Reached");
            		SmartDashboard.putString("Claw Bot LS", "Not Reached");
            	}
            	
            	
            	if(StickController.getRawButton(7)) {														//Arm Code
                	armLift.set(-.9);
        			SmartDashboard.putNumber("Arm Up Speed", armLift.get());
            	}
            	else if(StickController.getRawButton(8)) {
            		armLift.set(.8);
        			SmartDashboard.putNumber("Arm Down Speed", armLift.get());
            	}
            	else {
            		armLift.set(0);
        			SmartDashboard.putString("Arm Status", "Stopped");
            	}
            	
            	if(StickController.getRawButton(6)) {														//Claw Code
                	clawTote.set(.8);
        			SmartDashboard.putNumber("Claw Extend Speed", clawTote.get());

            	}
                else if(StickController.getRawButton(5)) {
                	clawTote.set(-.9);
        			SmartDashboard.putNumber("Claw Retract Speed", clawTote.get());
            	}
                else {
            		clawTote.set(0);
        			SmartDashboard.putString("Claw Status", "Stopped");
                } 
            }
            else {
            	RobotDrive.mecanumDrive_Cartesian(DeadBand(StickController.getX()*.5), DeadBand(StickController.getZ()*.5), DeadBand(StickController.getY()*.5), 0);
            	if(StickController.getRawButton(4)) {
            		speedOverride = true;
            		Timer.delay(.1);
            	}
            	
            	if(LSArm.get()) {																		//Limit Switches
            		SmartDashboard.putString("Arm LS", "Reached");
            		armLift.set(.8);
            		Timer.delay(.2);
            		armLift.set(0);
            	}
            	else if(LSClawUp.get()) {
            		SmartDashboard.putString("Claw Up LS", "Reached");
            		clawTote.set(-.8);
            		Timer.delay(.2);
            		clawTote.set(0);
            	}
            	else if(LSClawBot.get()) {
            		SmartDashboard.putString("Claw Bot LS", "Reached");
            		clawTote.set(.8);
            		Timer.delay(.2);
            		clawTote.set(0);
            	}
            	else {
            		SmartDashboard.putString("Arm LS", "Not Reached");
            		SmartDashboard.putString("Claw Up LS", "Not Reached");
            		SmartDashboard.putString("Claw Bot LS", "Not Reached");
            	}
            	
            	
            	if(StickController.getRawButton(7)) {														//Arm Code
            		armLift.set(-.3);
        			SmartDashboard.putNumber("Arm Up Speed", armLift.get());
            	}
            	else if(StickController.getRawButton(8)) {
            		armLift.set(.2);
        			SmartDashboard.putNumber("Arm Down Speed", armLift.get());
            	}
            	else {
            		armLift.set(0);
        			SmartDashboard.putString("Arm Status", "Stopped");
            	}
            	
            	if(StickController.getRawButton(6)) {														//Claw Code
            		clawTote.set(.2);
        			SmartDashboard.putNumber("Claw Extend Speed", clawTote.get());
            	}
            	else if(StickController.getRawButton(5)) {
            		clawTote.set(-.3);
        			SmartDashboard.putNumber("Claw Retract Speed", clawTote.get());
            	}
            	else {
            		armLift.set(0);
        			SmartDashboard.putString("Arm Status", "Stopped");

            		clawTote.set(0);
        			SmartDashboard.putString("Claw Status", "Stopped");
            	}
            }
            
            Timer.delay(0.005);	// wait 5ms to avoid hogging CPU cycles
            
            
			if (StickController.getX() > 0) {																//Displays Robot Speed
				SmartDashboard.putNumber("Robot Forward Speed", DeadBand(StickController.getX()));	
			}
			else if (StickController.getX() < 0) {
				SmartDashboard.putNumber("Robot Backward Speed", DeadBand(StickController.getX()));
			}
			else {
				SmartDashboard.putString("Robot Lateral Status", "Stopped");
			}
			
			if (StickController.getY() > 0) {
				SmartDashboard.putNumber("Robot Right Speed", DeadBand(StickController.getY()));
			}
			else if (StickController.getY() < 0) {
				SmartDashboard.putNumber("Robot Right Speed", DeadBand(StickController.getY()));
			}
			else {
				SmartDashboard.putString("Robot Horizontal Status", "Stopped");
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
