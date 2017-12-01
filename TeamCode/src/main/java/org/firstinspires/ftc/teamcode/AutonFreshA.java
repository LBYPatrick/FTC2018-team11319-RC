package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.teamcode.actuators.DriveTrain;
import org.firstinspires.ftc.teamcode.actuators.ServoControl;
import org.firstinspires.ftc.teamcode.databases.Statics;

/**
 * Created by LBYPatrick on 11/14/2017.
 */
@Autonomous(name = "FRESH_POS_A",group = "Freshman")
public class AutonFreshA extends LinearOpMode {

    //Initialize objects
    private     DriveTrain      mWheel;

    private     Servo           GGrabberLObj;
    private     Servo           GGrabberRObj;

    // Declare OpMode members.
    final private ElapsedTime globalTime = new ElapsedTime();
    final private ElapsedTime stageTime = new ElapsedTime();

    private void initialize() {

        DcMotor BL = hardwareMap.get(DcMotor.class, Statics.Freshman.LWheel);
        DcMotor BR = hardwareMap.get(DcMotor.class, Statics.Freshman.RWheel);

        mWheel = new DriveTrain(BL,BR);

        //Glyph Grabbers
        GGrabberLObj = hardwareMap.get(Servo.class, Statics.Sophomore.Servos.left_glyphGrabber);
        GGrabberRObj = hardwareMap.get(Servo.class, Statics.Sophomore.Servos.right_glyphGrabber);

    }

    @Override
    public void runOpMode() {

        initialize();

        //Move Jewels

        //Move Forward
        stageTime.reset();
        mWheel.tankDrive(-1,0);
        while(stageTime.seconds() <= 1.0) {
            telemetry.addData("Current Stage: ", "Moving forward");
            telemetry.addData( "Global Time: ", globalTime.seconds());
            telemetry.addData("Stage Time: ", stageTime.seconds());
            telemetry.update();
        }
        mWheel.tankDrive(0,0);

        //Stop the bot for a second
        stageTime.reset();
        while(stageTime.seconds() <= 1.0) {
            telemetry.addData("Current Stage: ", "Idle");
            telemetry.addData( "Global Time: ", globalTime.seconds());
            telemetry.addData("Stage Time: ", stageTime.seconds());
            telemetry.update();
        }

        //Turn left
        stageTime.reset();
        mWheel.tankDrive(0,-0.5);
        while(stageTime.seconds() <= 0.5){
            telemetry.addData("Current Stage", "Turning Left");
            telemetry.addData("Global Time",globalTime.seconds());
            telemetry.addData("Stage Time",stageTime.seconds());
        }
        mWheel.tankDrive(0,0);

        //Stop the bot for another second
        stageTime.reset();
        while(stageTime.seconds() <= 1.0) {
            telemetry.addData("Current Stage: ", "Idle");
            telemetry.addData( "Global Time: ", globalTime.seconds());
            telemetry.addData("Stage Time: ", stageTime.seconds());
            telemetry.update();
        }

        stageTime.reset();
        mWheel.tankDrive(-1,0);
        while(stageTime.seconds() <= 0.3) {
            telemetry.addData("Current Stage: ", "Moving forward");
            telemetry.addData( "Global Time: ", globalTime.seconds());
            telemetry.addData("Stage Time: ", stageTime.seconds());
            telemetry.update();
        }
        mWheel.tankDrive(0,0);

    }
}
