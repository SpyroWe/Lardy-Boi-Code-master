package frc.robot.Commands;

import java.util.function.BooleanSupplier;
import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.subsystems.ClimberSubsystem;

public class ClimberCommand extends Command {
    private BooleanSupplier xBut;
    private BooleanSupplier aBut;
    private ClimberSubsystem ClimberSub;

    public ClimberCommand(ClimberSubsystem CS, BooleanSupplier x, BooleanSupplier a){
        xBut = x;
        aBut = a;
        ClimberSub = CS;
        addRequirements(CS);

    }
    @Override
    public void initialize() {}

    @Override
    public void execute() {
        ClimberSub.move(xBut.getAsBoolean(),aBut.getAsBoolean());
    }
    @Override
    public void end(boolean interrupted) {}

    @Override
    public boolean isFinished(){
        return false;
    }
    
}
