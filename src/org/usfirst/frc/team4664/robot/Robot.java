package org.usfirst.frc.team4664.robot;


import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.Encoder;
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
    
    Encoder armHeight;
    
     //Channels for the wheels
    final int frontLeftChannel	= 2;
    final int rearLeftChannel	= 3;
    final int frontRightChannel	= 1;
    final int rearRightChannel	= 0;
    
    final int LSArmPort			= 0;
    final int LSClawUpPort		= 1;
    final int LSClawBotPort		= 2;
    
    final int Enc1				= 6;
    final int Enc2				= 7;
    
    Victor armLift;
    Victor clawTote;
    
    double speedL = .4;
    double speedH = .8;
    
    
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
    	
        armHeight 		= new Encoder(Enc1, Enc2, true, Encoder.EncodingType.k4X);
        armHeight.setMaxPeriod(.1);
        armHeight.setMinRate(10);
        armHeight.setDistancePerPulse(5);
        armHeight.setReverseDirection(true);
        armHeight.setSamplesToAverage(7);
    	}
    
      //Runs the motors with mecanum drive.
    
    public void Test() {
    	while(isEnabled()) {
    		int count = armHeight.get();
    		SmartDashboard.putString("Testing", "Active");
    		SmartDashboard.putBoolean("LS Arm", LSArm.get());
    		SmartDashboard.putNumber("Arm Height", count);
    		SmartDashboard.putBoolean("LS Claw Up", LSClawUp.get());
    		SmartDashboard.putBoolean("LS Claw Bot", LSClawBot.get());
    		SmartDashboard.putNumber("Timer", clock);
    		clock += .05;
    		Timer.delay(.05);
    	}
    }
    
    public void operatorControl() {
        RobotDrive.setSafetyEnabled(true);
        while (isOperatorControl() && isEnabled()) {
        		
        		double speed = StickController.getRawButton(4)?speedL:speedH;
        		
            	RobotDrive.mecanumDrive_Cartesian(DeadBand(StickController.getX() * speed), DeadBand(StickController.getZ() * speed), 
            			DeadBand(-StickController.getY() * speed), 0);
            	
            	
            	String ArmState;
            	if(StickController.getRawButton(7) && LSArm.get() != true) {									//Arm Code
                	armLift.set(-.9);
                	ArmState = "Up";
            	}
            	else if(StickController.getRawButton(8)) {
            		armLift.set(.8);
            		ArmState = "Down";
            	}
            	else {
            		armLift.set(0);
            		ArmState = "False";
            	}
            	
            	String ClawState;
            	if(StickController.getRawButton(6) && LSClawBot.get() != true) {								//Claw Code
                	clawTote.set(.8);
                	ClawState = "Up";
            	}
                else if(StickController.getRawButton(5) && LSClawUp.get() != true) {
                	clawTote.set(-.9);
                	ClawState = "Down";
            	}
                else {
            		clawTote.set(0);
            		ClawState = "False";
                } 
            
            	Timer.delay(0.005);	// wait 5ms to avoid hogging CPU cycles

            	if (StickController.getY() > 0) {	
            		SmartDashboard.putNumber("Robot Forward Speed", DeadBand(StickController.getY()));			//Robot Speed
            	}
            	else{
            		SmartDashboard.putNumber("Robot Backward Speed", DeadBand(StickController.getY()));
            	}
			
            	if (StickController.getX() > 0 ) {
            		SmartDashboard.putNumber("Robot Left Speed", DeadBand(StickController.getX()));
            	}
            	else {
            		SmartDashboard.putNumber("Robot Right Speed", DeadBand(StickController.getX()));
            	}
			
            	if (LSArm.get()) {
            		SmartDashboard.putString("Arm LS", "Reached");												//Limit Switches
            	}
            	else {
        			SmartDashboard.putString("Arm LS", "Not Reached");
            	}
            	
            	if (LSClawUp.get()) {
            		SmartDashboard.putString("Claw Upper LS", "Reached");
            	}
            	else {
            		SmartDashboard.putString("Claw Upper LS", "Not Reached");
            	}
            	
            	if (LSClawBot.get()) {
            		SmartDashboard.putString("Claw Bottom LS", "Reached");
            	}
            	else {
            		SmartDashboard.putString("Claw Bottom LS", "Not Reached");
            	}
            	SmartDashboard.putString("Claw Moving", ClawState);												//Claw Status
    			SmartDashboard.putString("Arm Moving", ArmState);												//Arm Status
        }
    }
        
        //Useful functions
        	double DeadBand(double input) {
        		if(Math.abs(input) < 0.3) {
        			return 0;
        		}
        		else {
        			return input;
        		}
        }
}
