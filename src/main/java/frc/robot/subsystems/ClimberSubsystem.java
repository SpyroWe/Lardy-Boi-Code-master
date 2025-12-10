package frc.robot.subsystems;

import com.ctre.phoenix6.hardware.TalonFX;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.SubsystemBase;

public class ClimberSubsystem extends SubsystemBase {
    private boolean xbutton = false;
    private TalonFX m_climber = new TalonFX(17); //put device id
    public ClimberSubsystem() {}

    public void move(Boolean xBut, boolean aBut){

        if(xBut){
            xbutton=true;
            m_climber.set(-.5);

        } else if(xbutton&&aBut){
            m_climber.set(.5);
        } else{
            m_climber.set(0);
        }
     }
    

    public double getClimbEncoder(){
        return m_climber.getRotorPosition().getValueAsDouble();
    }

    @Override
    public void periodic() {

    }

    
}
