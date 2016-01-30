package org.usfirst.frc.team4664.robot;

import edu.wpi.first.wpilibj.Victor;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.RobotDrive;
import edu.wpi.first.wpilibj.RobotDrive.MotorType;
import edu.wpi.first.wpilibj.SampleRobot;
import edu.wpi.first.wpilibj.Timer;

/**
 * This is a demo program showing how to use Mecanum control with the RobotDrive class.
 */
public class RobotMain extends SampleRobot {
	
    RobotDrive robotDrive;
    Joystick stickDrive, joyController;

    // Channels for the wheels
    final int frontLeftChannel	= 2;
    final int rearLeftChannel	= 3;
    final int frontRightChannel	= 0;
    final int rearRightChannel	= 1;
    final int armLiftChannel = 4;
    final int armBiteChannel = 5;
    
    // The channel on the driver station that the joystick is connected to
    //final int joystickDrive	= 0;
    final int joyPad = 0;
    
    Victor armLift;
    
    public RobotMain() {
        robotDrive = new RobotDrive(frontLeftChannel, rearLeftChannel, frontRightChannel, rearRightChannel);
    	robotDrive.setInvertedMotor(MotorType.kFrontRight, true);	// invert the right side motors
    	robotDrive.setInvertedMotor(MotorType.kRearRight, true);		// you may need to change or remove this to match your robot
        robotDrive.setExpiration(0.1);
        //stickDrive = new Joystick(joystickDrive);
        joyController = new Joystick(joyPad);
        armLift = new Victor(armLiftChannel);
    }
    /**
     * Runs the motors with Mecanum drive.
     */
    
    public void operatorControl() {
        robotDrive.setSafetyEnabled(true);

        while (isOperatorControl() && isEnabled()) {
        	if(joyController.getRawButton(1)){
        		robotDrive.mecanumDrive_Cartesian(joyController.getX(), joyController.getY(), joyController.getZ(), 0);
        	}
        	else {
        		robotDrive.mecanumDrive_Cartesian(0.5*joyController.getX(), 0.5*joyController.getY(), 0.5*joyController.getZ(), 0);
        	}
        	// Use the joystick X axis for lateral movement, Y axis for forward movement, and Z axis for rotation.
        	// This sample does not use field-oriented drive, so the gyro input is set to zero.
            //robotDrive.mecanumDrive_Cartesian(stickDrive.getX(), stickDrive.getY(), 0, 0);
                        
            if(joyController.getRawButton(8)) {
            	armLift.set(-0.75);
            }
            else if(joyController.getRawButton(7)) {
            	armLift.set(0.50);
            }
            else{
            	armLift.set(0.0);
            }
            
            Timer.delay(0.005);	// wait 5ms to avoid hogging CPU cycles
        }
    }
    
}
