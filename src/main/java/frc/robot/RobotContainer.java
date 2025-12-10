// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot;

import static edu.wpi.first.units.Units.*;

import com.ctre.phoenix6.swerve.SwerveModule.DriveRequestType;
import com.ctre.phoenix6.swerve.SwerveRequest;

import com.pathplanner.lib.auto.AutoBuilder;
import com.pathplanner.lib.auto.NamedCommands;
import com.pathplanner.lib.commands.FollowPathCommand;

import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.Commands;
import edu.wpi.first.wpilibj2.command.button.CommandXboxController;
import edu.wpi.first.wpilibj2.command.button.RobotModeTriggers;
import edu.wpi.first.wpilibj2.command.sysid.SysIdRoutine.Direction;
import frc.robot.Commands.EffectorCommand;
import frc.robot.Commands.ElevatorCommand;
import frc.robot.generated.TunerConstants;
import frc.robot.subsystems.CommandSwerveDrivetrain;
import frc.robot.subsystems.ElevatorSubsystem;
import frc.robot.subsystems.EndEffector;
import frc.robot.subsystems.ClimberSubsystem;
import frc.robot.Commands.ClimberCommand;

public class RobotContainer {
    private double MaxSpeed = TunerConstants.kSpeedAt12Volts.in(MetersPerSecond); // kSpeedAt12Volts desired top speed
    private double MaxAngularRate = RotationsPerSecond.of(0.75).in(RadiansPerSecond); // 3/4 of a rotation per second max angular velocity
    
    /* Setting up bindings for necessary control of the swerve drive platform */
    private final SwerveRequest.FieldCentric drive = new SwerveRequest.FieldCentric()
            .withDeadband(MaxSpeed * 0.1).withRotationalDeadband(MaxAngularRate * 0.1) // Add a 10% deadband
            .withDriveRequestType(DriveRequestType.OpenLoopVoltage); // Use open-loop control for drive motors
    private final SwerveRequest.SwerveDriveBrake brake = new SwerveRequest.SwerveDriveBrake();
    private final SwerveRequest.PointWheelsAt point = new SwerveRequest.PointWheelsAt();
    private final ElevatorSubsystem elevatorbase = new ElevatorSubsystem();
    private final CommandXboxController m_driverController = new CommandXboxController(0);
    private final CommandXboxController m_coDriverController = new CommandXboxController(1);
    private final EndEffector effectorbase = new EndEffector();
    private final ClimberSubsystem ClimberBase = new ClimberSubsystem();
;
    private final Telemetry logger = new Telemetry(MaxSpeed);

    private final CommandXboxController joystick = new CommandXboxController(0);

    public final CommandSwerveDrivetrain drivetrain = TunerConstants.createDrivetrain();







private final SendableChooser<Command> autoChooser;
    
    public RobotContainer() {
    autoChooser = AutoBuilder.buildAutoChooser("First Auto");
        SmartDashboard.putData("Auto Mode", autoChooser);
        
        
        NamedCommands.registerCommand("score",
            new EffectorCommand(effectorbase,
            () -> false,
            () -> true).withTimeout(0.5));
        

        configureBindings();

        // Warmup PathPlanner to avoid Java pauses
        FollowPathCommand.warmupCommand().schedule();

    }

    private void configureBindings() {
        // Note that X is defined as forward according to WPILib convention,
        // and Y is defined as to the left according to WPILib convention.
        drivetrain.setDefaultCommand(
            // Drivetrain will execute this command periodically
            drivetrain.applyRequest(() ->
                drive.withVelocityX(-joystick.getLeftY() * MaxSpeed/2) // Drive forward with negative Y (forward) Change speed here
                    .withVelocityY(-joystick.getLeftX() * MaxSpeed/2) // Drive left with negative X (left)
                    .withRotationalRate(-joystick.getRightX() * MaxAngularRate/2) // Drive counterclockwise with negative X (left)
            )
        );
        //elevator
        elevatorbase.setDefaultCommand(new ElevatorCommand(elevatorbase,
        () -> m_driverController.y().getAsBoolean(),
        () -> m_driverController.b().getAsBoolean(),
        ()-> m_coDriverController.a().getAsBoolean(),
        () -> m_coDriverController.y().getAsBoolean(),
        () -> m_coDriverController.b().getAsBoolean()));

        //effector
        effectorbase.setDefaultCommand(new EffectorCommand(effectorbase,
        () ->m_driverController.leftTrigger().getAsBoolean(),
        () ->m_driverController.rightTrigger().getAsBoolean()
        ));

        //climber
        ClimberBase.setDefaultCommand(new ClimberCommand(ClimberBase,
        ()-> m_coDriverController.leftBumper().getAsBoolean(),
        ()-> m_coDriverController.rightBumper().getAsBoolean()));



        // Idle while the robot is disabled. This ensures the configured
        // neutral mode is applied to the drive motors while disabled.
        /*final var idle = new SwerveRequest.Idle();
        RobotModeTriggers.disabled().whileTrue(
            drivetrain.applyRequest(() -> idle).ignoringDisable(true)
        );

        joystick.a().whileTrue(drivetrain.applyRequest(() -> brake));
        joystick.b().whileTrue(drivetrain.applyRequest(() ->
            point.withModuleDirection(new Rotation2d(-joystick.getLeftY(), -joystick.getLeftX()))
        ));*/

        // Run SysId routines when holding back/start and X/Y.
        // Note that each routine should be run exactly once in a single log.
        joystick.back().and(joystick.y()).whileTrue(drivetrain.sysIdDynamic(Direction.kForward));
        joystick.back().and(joystick.x()).whileTrue(drivetrain.sysIdDynamic(Direction.kReverse));
        joystick.start().and(joystick.y()).whileTrue(drivetrain.sysIdQuasistatic(Direction.kForward));
        joystick.start().and(joystick.x()).whileTrue(drivetrain.sysIdQuasistatic(Direction.kReverse));

        // reset the field-centric heading on left bumper press
        joystick.leftBumper().onTrue(drivetrain.runOnce(() -> drivetrain.seedFieldCentric()));//previously joystick.leftBumper

        drivetrain.registerTelemetry(logger::telemeterize);
    }

    public Command getAutonomousCommand() {
        //return Commands.print("No autonomous command configured");
        return autoChooser.getSelected();
    }
}
