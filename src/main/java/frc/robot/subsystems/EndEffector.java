package frc.robot.subsystems;

import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.Commands;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import com.revrobotics.spark.SparkMax;
import com.revrobotics.spark.SparkLowLevel.MotorType;;

public class EndEffector extends SubsystemBase {
private SparkMax endeffectormotor = new SparkMax(12,MotorType.kBrushless);

public EndEffector() {




}
public Command automove(){
  return Commands.run(() -> endeffectormotor.set(-3), this).withTimeout(1);

}
public Command stopautomove(){
    return Commands.run(() -> endeffectormotor.set(0), this);
}




public void moveEffector (boolean leftTrigger,boolean rightTrigger){
    if(true){
    if(leftTrigger){
        endeffectormotor.set(13);//2

    }else if(rightTrigger){
        endeffectormotor.set(-10);//2
    }else{
        endeffectormotor.stopMotor();
    }

    }

    }
}


    

