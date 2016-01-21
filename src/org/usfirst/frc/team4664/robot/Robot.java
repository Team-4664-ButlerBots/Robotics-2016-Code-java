package org.usfirst.frc.team4664.robot;


import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.RobotDrive;
import edu.wpi.first.wpilibj.RobotDrive.MotorType;
import edu.wpi.first.wpilibj.SampleRobot;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.Victor;

/**
 * This is a demo program showing how to use Mecanum control with the RobotDrive class.
 */
public class Robot extends SampleRobot {
	
    RobotDrive robotDrive;
    Victor armMotor;
    Victor clawMotor;
    
	double scaleArm= -0.7, scaleClaw= -1.0, scaleJoy= 0.6;
	double dbdArm= 0.2, dbdClaw= 0.3, dbdJoy= 0.3;
    
    Joystick stickDrive;
    Joystick stickArm;

    // Channels for the wheels
    final int frontLeftChannel	= 2;
    final int rearLeftChannel	= 3;
    final int frontRightChannel	= 0;
    final int rearRightChannel	= 1;
    
    final int armLift = 4;
    final int armBite = 5;
    
    // The channel on the driver station that the joystick is connected to
    final int joystickChannel1	= 0;
    final int joystickChannel2  = 1;

    public Robot() {
        robotDrive = new RobotDrive(frontLeftChannel, rearLeftChannel, frontRightChannel, rearRightChannel);
        armMotor = new Victor(armLift);
        clawMotor = new Victor(armBite);
        
        robotDrive.setInvertedMotor(MotorType.kFrontRight, true);
        robotDrive.setInvertedMotor(MotorType.kRearRight, true);
    	//robotDrive.setInvertedMotor(MotorType.kFrontLeft, true);	// invert the left side motors
    	//robotDrive.setInvertedMotor(MotorType.kRearLeft, true);   // you may need to change or remove this to match your robot
        robotDrive.setExpiration(0.1);

        stickDrive = new Joystick(joystickChannel1);
        stickArm   = new Joystick(joystickChannel2);
    }
        

    /**
     * Runs the motors with Mecanum drive.
     */
    public void operatorControl() {
        robotDrive.setSafetyEnabled(true);
        while (isOperatorControl() && isEnabled()) {
        	
        	// Use the joystick X axis for lateral movement, Y axis for forward movement, and Z axis for rotation.
        	// This sample does not use field-oriented drive, so the gyro input is set to zero.
            robotDrive.mecanumDrive_Cartesian(scaleJoy*ApplyDeadband(stickDrive.getX(), dbdJoy),
            						  scaleJoy*ApplyDeadband(stickDrive.getY(), dbdJoy), 0, 0);
            //robotDrive.arcadeDrive(stick); 
            
            armMotor.set(ApplyDeadband(stickArm.getX(), dbdJoy));
            clawMotor.set(ApplyDeadband(stickArm.getY(), dbdJoy));
            
            Timer.delay(0.005);	// wait 5ms to avoid hogging CPU cycles
        }
    }

	double ApplyDeadband(double rawValue, double deadband) {
		rawValue = Limit(rawValue);
		if(Math.abs(rawValue) < deadband) return 0.0;
		if(rawValue > 0)			      return (rawValue - deadband) / (1.0 - deadband);
									      return (rawValue + deadband) / (1.0 - deadband);
	}
	
	double Limit(double value) {
		if(value > 1.0)  return 1.0;
		if(value < -1.0) return -1.0;
						 return value;
	}
	
}