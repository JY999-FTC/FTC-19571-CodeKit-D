package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.ElapsedTime;

/**
 * This file contains an example of a Linear "OpMode".
 * An OpMode is a 'program' that runs in either the autonomous or the teleop period of an FTC match.
 * The names of OpModes appear on the menu of the FTC Driver Station.
 * When a selection is made from the menu, the corresponding OpMode is executed.
 *
 * This particular OpMode illustrates driving a 4-motor Omni-Directional (or Holonomic) robot.
 * This code will work with either a Mecanum-Drive or an X-Drive train.
 * Both of these drives are illustrated at https://gm0.org/en/latest/docs/robot-design/drivetrains/holonomic.html
 * Note that a Mecanum drive must display an X roller-pattern when viewed from above.
 *
 * Also note that it is critical to set the correct rotation direction for each motor.  See details below.
 *
 * Holonomic drives provide the ability for the robot to move in three axes (directions) simultaneously.
 * Each motion axis is controlled by one Joystick axis.
 *
 * 1) Axial:    Driving forward and backward               Left-joystick Forward/Backward
 * 2) Lateral:  Strafing right and left                     Left-joystick Right and Left
 * 3) Yaw:      Rotating Clockwise and counter clockwise    Right-joystick Right and Left
 *
 * This code is written assuming that the right-side motors need to be reversed for the robot to drive forward.
 * When you first test your robot, if it moves backward when you push the left stick forward, then you must flip
 * the direction of all 4 motors (see code below).
 *
 * Use Android Studio to Copy this Class, and Paste it into your team's code folder with a new name.
 * Remove or comment out the @Disabled line to add this opmode to the Driver Station OpMode list
 */

@TeleOp(name="Center Stage Gamepad", group="Linear Opmode")
public class CenterStageGamepad extends LinearOpMode {

    // Declare OpMode members for each of the 4 motors.
    private ElapsedTime runtime = new ElapsedTime();
    private DcMotor leftFrontDrive = null;
    private DcMotor leftBackDrive = null;
    private DcMotor rightFrontDrive = null;
    private DcMotor rightBackDrive = null;
    private DcMotor carWashMotor = null;
    private Servo droneServo = null;
    //private DigitalChannel touch;

    //declare servos for clasper, as looking down on robot from behind, servoCW is the servo on the
    // left that moves clockwise to clasp, servoCCW is on the right and moves counter-clockwise to clasp
    private Servo servoCW;
    private Servo servoCCW;


    @Override
    public void runOpMode() {

        leftFrontDrive  = hardwareMap.get(DcMotor.class, "leftFront");
        leftBackDrive  = hardwareMap.get(DcMotor.class, "leftBack");
        rightFrontDrive = hardwareMap.get(DcMotor.class, "rightFront");
        rightBackDrive = hardwareMap.get(DcMotor.class, "rightBack");
        carWashMotor = hardwareMap.get(DcMotor.class, "liftMotor");
        droneServo = hardwareMap.get(Servo.class, "droneServo");

        leftFrontDrive.setDirection(DcMotor.Direction.REVERSE);
        leftBackDrive.setDirection(DcMotor.Direction.REVERSE);
        rightFrontDrive.setDirection(DcMotor.Direction.FORWARD);
        rightBackDrive.setDirection(DcMotor.Direction.FORWARD);

        telemetry.addData("Status", "Initialized");
        telemetry.update();

        waitForStart();
        runtime.reset();

        double slow = 1.0;
        double strafeSlow = 1.0;
        int targetEncoderValue = 0;
        double carWashPower = 1.0;

        while (opModeIsActive()) {
            double max;
            if (gamepad1.right_trigger > 0.7) {
                slow = 0.25;
                strafeSlow = 0.3525;
            } else {
                slow = 0.5;
                strafeSlow = 0.5;
            }

            double axial   = -gamepad1.left_stick_y;  // Note: pushing stick forward gives negative value
            double lateral =  gamepad1.left_stick_x;
            double yaw     =  gamepad1.right_stick_x;

            axial = -gamepad1.left_stick_y * slow;
            lateral = gamepad1.left_stick_x * 0.75 * slow;
            yaw = gamepad1.right_stick_x * strafeSlow;

            double leftFrontPower  = axial + lateral + yaw;
            double rightFrontPower = axial - lateral - yaw;
            double leftBackPower   = axial - lateral + yaw;
            double rightBackPower  = axial + lateral - yaw;

            max = Math.max(Math.abs(leftFrontPower), Math.abs(rightFrontPower));
            max = Math.max(max, Math.abs(leftBackPower));
            max = Math.max(max, Math.abs(rightBackPower));

            if (max > 1.0) {
                leftFrontPower  /= max;
                rightFrontPower /= max;
                leftBackPower   /= max;
                rightBackPower  /= max;
            }

            // Send calculated power to wheels
            leftFrontDrive.setPower(leftFrontPower);
            rightFrontDrive.setPower(rightFrontPower);
            leftBackDrive.setPower(leftBackPower);
            rightBackDrive.setPower(rightBackPower);

            if (gamepad2.a) {
                carWashMotor.setPower(carWashPower);
            }


            if (gamepad2.b) {
                carWashMotor.setPower(-carWashPower);
            }

            if ((gamepad2.left_trigger > 0.75) && (gamepad2.right_trigger > 0.75)) {
                droneServo.setPosition(1);
            }

            if (!gamepad2.a && !gamepad2.b){
                carWashMotor.setPower(0);
            }
            telemetry.addData("Status", "Run Time: " + runtime.toString());
            if (gamepad2.left_trigger > 0.75){
                telemetry.addData("Left Trigger", "Activated - " + gamepad2.left_trigger);
            } else{
                telemetry.addData("Left Trigger", gamepad2.left_trigger);
            }
            if (gamepad2.right_trigger > 0.75){
                telemetry.addData("Right Trigger", "Activated - " + gamepad2.right_trigger);
            }else {
                telemetry.addData("Right Trigger", gamepad2.right_trigger);
            }
            telemetry.update();

            sleep(250);
        }

            }
}
