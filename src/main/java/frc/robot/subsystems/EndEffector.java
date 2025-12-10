package frc.robot.subsystems;

import edu.wpi.first.wpilibj2.command.SubsystemBase;
import com.revrobotics.spark.SparkMax;
import com.revrobotics.spark.SparkLowLevel.MotorType;;

public class EndEffector extends SubsystemBase {
private SparkMax endeffectormotor = new SparkMax(12,MotorType.kBrushless);

public EndEffector() {

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


    

