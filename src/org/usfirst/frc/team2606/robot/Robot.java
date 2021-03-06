/*----------------------------------------------------------------------------*/
/* Copyright (c) 2017-2018 FIRST. All Rights Reserved.                        */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package org.usfirst.frc.team2606.robot;

import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.AnalogGyro;
import edu.wpi.first.wpilibj.TimedRobot;
import edu.wpi.first.wpilibj.Ultrasonic;
import edu.wpi.first.wpilibj.command.Scheduler;
import edu.wpi.first.wpilibj.interfaces.Gyro;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import org.usfirst.frc.team2606.robot.subsystems.Drive;
import org.usfirst.frc.team2606.robot.commands.autonomous.BreakPlane;
import org.usfirst.frc.team2606.robot.commands.autonomous.SwitchPlace;


/**
 * The VM is configured to automatically run this class, and to call the
 * functions corresponding to each mode, as described in the TimedRobot
 * documentation. If you change the name of this class or the package after
 * creating this project, you must also update the build.properties file in the
 * project.
 */
public class Robot extends TimedRobot {

	private SendableChooser<String> autoChooser = new SendableChooser<>();
	private Command autonomousCommand;

	public static OI oi;
	public static Drive drive;
	public static double scale;
	public static double orientation;

	private Gyro gyro = new AnalogGyro(1);
	private Ultrasonic ultrasonic = new Ultrasonic(0,1);
	/**
	 * This function is run when the robot is first started up and should be
	 * used for any initialization code.
	 */
	@Override
	public void robotInit() {
		oi = new OI();
		drive = new Drive();

		//autoChooser.addObject("Break the Plane", new BreakPlane());
		SmartDashboard.putData("Auto mode", autoChooser);

		// Initialize global constants
		scale = 0.7;
		orientation = 1.0;
	}

	/**
	 * This function is called once each time the robot enters Disabled mode.
	 * You can use it to reset any subsystem information you want to clear when
	 * the robot is disabled.
	 */
	@Override
	public void disabledInit() {
	}

	@Override
	public void disabledPeriodic() {
		Scheduler.getInstance().run();
	}

	/**
	 * This autonomous (along with the chooser code above) shows how to select
	 * between different autonomous modes using the dashboard. The sendable
	 * chooser code works with the Java SmartDashboard. If you prefer the
	 * LabVIEW Dashboard, remove all of the chooser code and uncomment the
	 * getString code to get the auto name from the text box below the Gyro
	 *
	 * <p>You can add additional auto modes by adding additional commands to the
	 * chooser code above (like the commented example) or additional comparisons
	 * to the switch structure below with additional strings & commands.
	 */
	@Override
	public void autonomousInit() {

		String autoSelected = SmartDashboard.getString("Auto Selector", "Default");
		switch(autoSelected) {
			case "Place Cube on Switch":
				autonomousCommand = new SwitchPlace();
				break;
			case "Default":
			default:
				autonomousCommand = new BreakPlane();
				break;
		}

		// schedule the autonomous command (example)
		if (autonomousCommand != null) {
			autonomousCommand.start();
		}
	}

	/**
	 * This function is called periodically during autonomous.
	 */

	public void autonomousPeriodic() {
		Scheduler.getInstance().run();
	}

	@Override
	public void teleopInit() {
		if (autonomousCommand != null) {
			autonomousCommand.cancel();
		}
		gyro.reset();
		RobotMap.SUPER_GYRO.reset();
	}

	/**
	 * This function is called periodically during operator control.
	 */
	@Override
	public void teleopPeriodic() {
		double angle=gyro.getAngle();
		double angleSuper=RobotMap.SUPER_GYRO.getAngle();
		double range=ultrasonic.getRangeInches();
		SmartDashboard.putNumber("gyro angle:",angle);
		SmartDashboard.putNumber("angleSuper",angleSuper);
		SmartDashboard.putNumber("range?",range);
		Scheduler.getInstance().run();

	}

	/**
	 * This function is called periodically during test mode.
	 */
	@Override
	public void testPeriodic() {
	}

	public void log() {
		drive.log();
	}

	public void reset(){
		drive.reset();
	}
}
