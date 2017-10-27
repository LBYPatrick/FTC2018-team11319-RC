/* Copyright (c) 2017 FIRST. All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without modification,
 * are permitted (subject to the limitations in the disclaimer below) provided that
 * the following conditions are met:
 *
 * Redistributions of source code must retain the above copyright notice, this list
 * of conditions and the following disclaimer.
 *
 * Redistributions in binary form must reproduce the above copyright notice, this
 * list of conditions and the following disclaimer in the documentation and/or
 * other materials provided with the distribution.
 *
 * Neither the name of FIRST nor the names of its contributors may be used to endorse or
 * promote products derived from this software without specific prior written permission.
 *
 * NO EXPRESS OR IMPLIED LICENSES TO ANY PARTY'S PATENT RIGHTS ARE GRANTED BY THIS
 * LICENSE. THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS
 * "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO,
 * THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE LIABLE
 * FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL
 * DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR
 * SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER
 * CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY,
 * OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE
 * OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */

package org.firstinspires.ftc.teamcode;

//import com.qualcomm.ftccommon.configuration.ScannedDevices;
//import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
//import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.ElapsedTime;
import com.qualcomm.robotcore.util.Range;

//import com.qualcomm.robotcore.util.Range;
import org.firstinspires.ftc.teamcode.databases.*;
import org.firstinspires.ftc.teamcode.actuators.*;

/**
 * This file contains an minimal example of a Linear "OpMode". An OpMode is a 'program' that runs in either
 * the autonomous or the teleop period of an FTC match. The names of OpModes appear on the menu
 * of the FTC Driver Station. When an selection is made from the menu, the corresponding OpMode
 * class is instantiated on the Robot Controller and executed.
 *
 * This particular OpMode just executes a basic Tank Drive Teleop for a two wheeled robot
 * It includes all the skeletal structure that all linear OpModes contain.
 *
 * Use Android Studios to Copy this Class, and Paste it into your team's code folder with a new name.
 * Remove or comment out the @Disabled line to add this opmode to the Driver Station OpMode list
 */

@TeleOp(name="Basic: Linear OpMode", group="Linear Opmode")
public class LinearOp extends LinearOpMode {

    //final private Servo S1 = hardwareMap.get(Servo.class, Statics.Servos.left_glyphGrabber);

    //Initialize objects
    private MecanumDrive mWheel;
    private ServoControl jArm;
    private GamepadSpace previous;

    // Declare OpMode members.
    final private ElapsedTime runtime = new ElapsedTime();
    private boolean isDPadUpChanged = false;
    private boolean isDPadDownChanged = false;
    private boolean isJleftXChanged = false;
    private boolean isJleftYChanged = false;
    private boolean isJrightXChanged = false;
    private boolean isLBChanged = false;


    private void detectGPChange() {
        isDPadUpChanged   = gamepad1.dpad_up != previous.DPadUp;
        isDPadDownChanged = gamepad1.dpad_down != previous.DPadDown;
        isLBChanged       = gamepad1.left_bumper != previous.LB;
        isJleftXChanged   = gamepad1.left_stick_x != previous.JleftX;
        isJleftYChanged   = gamepad1.left_stick_y != previous.JleftY;
        isJrightXChanged  = gamepad1.right_stick_x != previous.JrightX;
        boolean isLTChanged = gamepad1.left_trigger != previous.LT;
        boolean isRTChanged = gamepad1.right_trigger != previous.RT;
    }

    private void saveGPData() {
        previous.DPadDown = gamepad1.dpad_down;
        previous.DPadUp = gamepad1.dpad_up;
        previous.LB = gamepad1.left_bumper;
        previous.JleftX = gamepad1.left_stick_x;
        previous.JleftY = gamepad1.left_stick_y;
        previous.JrightX = gamepad1.right_stick_x;
        previous.LT = gamepad1.left_trigger;
        previous.RT = gamepad1.right_trigger;
    }

    private void initialize(){
        DcMotor FL = hardwareMap.get(DcMotor.class, Statics.MecanumWheel.Front.left);
        DcMotor FR = hardwareMap.get(DcMotor.class, Statics.MecanumWheel.Front.right);
        DcMotor BL = hardwareMap.get(DcMotor.class, Statics.MecanumWheel.Back.left);
        DcMotor BR = hardwareMap.get(DcMotor.class, Statics.MecanumWheel.Back.right);
        Servo s0 = hardwareMap.get(Servo.class, Statics.Servos.jewel);
        mWheel = new MecanumDrive(FL, FR, BL, BR);
        jArm = new ServoControl(s0, true, 0.13, 0.7);
        previous = new GamepadSpace();

    }

    @Override
    public void runOpMode() {

        telemetry.addData("Status", "Initialized");
        telemetry.update();

        initialize();
        waitForStart(); // Wait for the game to start (driver presses PLAY)
        runtime.reset();

        // run until the end of the match (driver presses STOP)
        while (opModeIsActive()) {

            detectGPChange();

            if (isLBChanged) { //Sniping Mode Switch
                // Change the speed of Mecanum Wheels if Y key got pressed
                if (gamepad1.left_bumper) {mWheel.speed = 0.3;jArm.speed = 0.3;}
                else {mWheel.speed = 1.0;jArm.speed = 1.0;}
            }


            if (isJleftXChanged || isJleftYChanged || isJrightXChanged) {
                mWheel.move(gamepad1.left_stick_x, gamepad1.left_stick_y, gamepad1.right_stick_x);
            } //Drive the bot if any joystick moved


            if (isDPadDownChanged || isDPadUpChanged)
                jArm.move_jewelArm(gamepad1.dpad_up ? true : false, gamepad1.dpad_down ? true : false); //Move the arm if triggered DPAD Up or Down

            //Save Data for next loop
            saveGPData();

            //Start putting information on the Driver Station
            //telemetry.addData("VuMark", vumark.getPos()); // Get VuMark informations
            telemetry.addData("Status           ", "Run Time: " + runtime.toString());// Show the elapsed game time and wheel power.
            telemetry.addData("Mecanum Wheels   ", " ");
            telemetry.addData("Left Front Wheel ", mWheel.leftFrontPower);
            telemetry.addData("Right Front Wheel", mWheel.rightFrontPower);
            telemetry.addData("Left Back Wheel  ", mWheel.leftBackPower);
            telemetry.addData("Right Back Wheel ", mWheel.leftBackPower);
            telemetry.addData("Gamepad          ", " ");
            telemetry.addData("Left Joystick    ", "(" + previous.JleftX + ", " + previous.JleftY + ")");
            telemetry.addData("Jewel Arm        ", jArm.servoPos);
            telemetry.update();
        }
    }
}
