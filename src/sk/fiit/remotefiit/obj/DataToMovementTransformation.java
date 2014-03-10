package sk.fiit.remotefiit.obj;

import java.util.ArrayList;

import android.util.Log;

import sk.fiit.remotefiit.emun.Movement;
import sk.fiit.remotefiit.interfaces.DataTransformation;


public class DataToMovementTransformation implements DataTransformation{
	private static PositionData startPosition;
	
	@Override
	public ArrayList<Movement> rawData(PositionData inputData, boolean extendedFunction, boolean reset) {

		ArrayList<Movement> output = new ArrayList<Movement>();
		if (reset==true) startPosition=null;
		if(startPosition == null){
			startPosition = new PositionData();
			startPosition.setOrientationX(inputData.getOrientationX());
			startPosition.setOrientationY(inputData.getOrientationY());
		}
		
		
		if(inputData.getAccelerometerY() < -1){
			if (extendedFunction == false)output.add(Movement.MOVE_FORWARDS);
			else output.add(Movement.MOVE_FORWARDS);
		}
		if(inputData.getAccelerometerY() > 4){
			if (extendedFunction == false)output.add(Movement.MOVE_BACKWARDS);
			else output.add(Movement.MOVE_UP);
		}
		if(inputData.getAccelerometerX() > 3){
			output.add(Movement.MOVE_LEFT);
		}
		if(inputData.getAccelerometerX() < -3){
			output.add(Movement.MOVE_RIGHT);
		}	
		if(startPosition.getOrientationX()<=60 || startPosition.getOrientationX()>=300){
			
			if(inputData.getOrientationX()>60 && inputData.getOrientationX()<300 ){
				return null;
			}
			
			if(startPosition.getOrientationX()>=0 && startPosition.getOrientationX()<=60){
				if(inputData.getOrientationX()>=300){
					inputData.setOrientationX(inputData.getOrientationX()-360);
				}
				if(inputData.getOrientationX() > (startPosition.getOrientationX()+20) && 
						inputData.getAccelerometerX() <=3 && inputData.getAccelerometerX() >= -3){
					output.add(Movement.LOOK_RIGHT);
				}
				if(inputData.getOrientationX() < (startPosition.getOrientationX()-20) && 
						inputData.getAccelerometerX() <=3 && inputData.getAccelerometerX() >= -3){
					output.add(Movement.LOOK_LEFT);
				}
			}else{
				if(inputData.getOrientationX()<=60)inputData.setOrientationX(inputData.getOrientationX()+360);
				if(inputData.getOrientationX() < (startPosition.getOrientationX()-15) && 
						inputData.getAccelerometerX() <=3 && inputData.getAccelerometerX() >= -3){
					output.add(Movement.LOOK_LEFT);
				}

				if(inputData.getOrientationX() > (startPosition.getOrientationX()+15) && 
						inputData.getAccelerometerX() <=3 && inputData.getAccelerometerX() >= -3){
					output.add(Movement.LOOK_RIGHT);
					}
			}
		}else{
			if(inputData.getOrientationX() > (startPosition.getOrientationX()+15) && 
					inputData.getAccelerometerX() <=3 && inputData.getAccelerometerX() >= -3){
				output.add(Movement.LOOK_RIGHT);
			}
			if(inputData.getOrientationX() < (startPosition.getOrientationX()-15) && 
					inputData.getAccelerometerX() <=3 && inputData.getAccelerometerX() >= -3){
				output.add(Movement.LOOK_LEFT);
			}
		}
		return output;
	}

	@Override
	public ArrayList<Movement> pedestrianData(PositionData inputData) {
		ArrayList<Movement> output = new ArrayList<Movement>();
		
		if(inputData.getAccelerometerY() < CalibrationData.getTiltForwards()){
			output.add(Movement.MOVE_FORWARDS);
		}
		else if(inputData.getAccelerometerY() > CalibrationData.getTiltBackwards()){
			output.add(Movement.MOVE_BACKWARDS);
		}
		if(inputData.getAccelerometerX() > CalibrationData.getTiltLeft()){
			output.add(Movement.MOVE_LEFT);
		}
		else if(inputData.getAccelerometerX() < CalibrationData.getTiltRight()){
			output.add(Movement.MOVE_RIGHT);
		}
/*
		if(inputData.getAccelerometerY() < 4 && inputData.getAccelerometerY() >= 3){
			output.add(Movement.MOVE_FORWARDS_2);
		}
		else if(inputData.getAccelerometerY() < 3 && inputData.getAccelerometerY() >= 2){
			output.add(Movement.MOVE_FORWARDS_1);
		}
		else if(inputData.getAccelerometerY() < 2){
			output.add(Movement.MOVE_FORWARDS);
		}
		else if(inputData.getAccelerometerY() > 7 && inputData.getAccelerometerY() <= 8){
			output.add(Movement.MOVE_BACKWARDS_2);
		}
		else if(inputData.getAccelerometerY() > 8 && inputData.getAccelerometerY() <= 9){
			output.add(Movement.MOVE_BACKWARDS_1);
		}
		else if(inputData.getAccelerometerY() > 9){
			output.add(Movement.MOVE_BACKWARDS);
		}

		
		if(inputData.getAccelerometerX() > 3 && inputData.getAccelerometerX() <= 4.5){
			output.add(Movement.MOVE_LEFT_2);
		}
		else if(inputData.getAccelerometerX() > 4.5 && inputData.getAccelerometerX() <= 6){
			output.add(Movement.MOVE_LEFT_1);
		}	
		else if(inputData.getAccelerometerX() > 6){
			output.add(Movement.MOVE_LEFT);
		}
		else if(inputData.getAccelerometerX() < -3 && inputData.getAccelerometerX() >= -4.5){
			output.add(Movement.MOVE_RIGHT_2);
		}
		else if(inputData.getAccelerometerX() < -4.5 && inputData.getAccelerometerX() >= -6){
			output.add(Movement.MOVE_RIGHT_1);
		}
		else if(inputData.getAccelerometerX() < -6){
			output.add(Movement.MOVE_RIGHT);
		}	
*/
		
		if(inputData.isJoystickDown()){
			output.add(Movement.LOOK_DOWN);
		}
		else if(inputData.isJoystickUp()){
			output.add(Movement.LOOK_UP);
		}
		if(inputData.isJoystickLeft()){
			output.add(Movement.LOOK_LEFT);
		}
		else if(inputData.isJoystickRight()){
			output.add(Movement.LOOK_RIGHT);
		}
		return output;
	}

	@Override
	public ArrayList<Movement> helicopterData(PositionData inputData) {
		ArrayList<Movement> output = new ArrayList<Movement>();
		
		if(inputData.getAccelerometerY() < CalibrationData.getTiltForwards()){
			output.add(Movement.MOVE_FORWARDS);
		}
		else if(inputData.getAccelerometerY() > CalibrationData.getTiltBackwards()){
			output.add(Movement.MOVE_BACKWARDS);
		}
		if(inputData.getAccelerometerX() > CalibrationData.getTiltLeft()){
			output.add(Movement.MOVE_LEFT);
		}
		else if(inputData.getAccelerometerX() < CalibrationData.getTiltRight()){
			output.add(Movement.MOVE_RIGHT);
		}
		
/*
		if(inputData.getAccelerometerY() < 4 && inputData.getAccelerometerY() >= 3){
			output.add(Movement.MOVE_FORWARDS_2);
		}
		else if(inputData.getAccelerometerY() < 3 && inputData.getAccelerometerY() >= 2){
			output.add(Movement.MOVE_FORWARDS_1);
		}
		else if(inputData.getAccelerometerY() < 2){
			output.add(Movement.MOVE_FORWARDS);
		}
		else if(inputData.getAccelerometerY() > 7 && inputData.getAccelerometerY() <= 8){
			output.add(Movement.MOVE_BACKWARDS_2);
		}
		else if(inputData.getAccelerometerY() > 8 && inputData.getAccelerometerY() <= 9){
			output.add(Movement.MOVE_BACKWARDS_1);
		}
		else if(inputData.getAccelerometerY() > 9){
			output.add(Movement.MOVE_BACKWARDS);
		}

		
		if(inputData.getAccelerometerX() > 3 && inputData.getAccelerometerX() <= 4.5){
			output.add(Movement.MOVE_LEFT_2);
		}
		else if(inputData.getAccelerometerX() > 4.5 && inputData.getAccelerometerX() <= 6){
			output.add(Movement.MOVE_LEFT_1);
		}	
		else if(inputData.getAccelerometerX() > 6){
			output.add(Movement.MOVE_LEFT);
		}
		else if(inputData.getAccelerometerX() < -3 && inputData.getAccelerometerX() >= -4.5){
			output.add(Movement.MOVE_RIGHT_2);
		}
		else if(inputData.getAccelerometerX() < -4.5 && inputData.getAccelerometerX() >= -6){
			output.add(Movement.MOVE_RIGHT_1);
		}
		else if(inputData.getAccelerometerX() < -6){
			output.add(Movement.MOVE_RIGHT);
		}
*/
		
		
		if(inputData.isJoystickDown()){
			output.add(Movement.LOOK_DOWN);
		}
		else if(inputData.isJoystickUp()){
			output.add(Movement.LOOK_UP);
		}
		if(inputData.isJoystickLeft()){
			output.add(Movement.LOOK_LEFT);
		}
		else if(inputData.isJoystickRight()){
			output.add(Movement.LOOK_RIGHT);
		}
		if(inputData.getVerticalMovement() == 1){
			output.add(Movement.MOVE_UP);
		}else if(inputData.getVerticalMovement() == -1){
			output.add(Movement.MOVE_DOWN);
		}
		
		
		return output;

	}

	@Override
	public ArrayList<Movement> carData(PositionData inputData) {
		ArrayList<Movement> output = new ArrayList<Movement>();
		
		//pri vertikalnej polohe su znamienka vlavo a vpravo naopak 
		if(inputData.getAccelerometerY() < -CalibrationData.getTiltLeft()){
			output.add(Movement.LOOK_LEFT);
		}
		else if(inputData.getAccelerometerY() > -CalibrationData.getTiltRight()){
			output.add(Movement.LOOK_RIGHT);
		}
		if(inputData.getAccelerometerX() < CalibrationData.getTiltForwards()){
			output.add(Movement.MOVE_FORWARDS);
		}
		else if(inputData.getAccelerometerX() > CalibrationData.getTiltBackwards()){
			output.add(Movement.MOVE_BACKWARDS);
		}	

/*
		if(inputData.getAccelerometerX() < 4 && inputData.getAccelerometerX() >= 3){
			output.add(Movement.MOVE_FORWARDS_2);
		}
		else if(inputData.getAccelerometerX() < 3 && inputData.getAccelerometerX() >= 2){
			output.add(Movement.MOVE_FORWARDS_1);
		}
		else if(inputData.getAccelerometerX() < 2){
			output.add(Movement.MOVE_FORWARDS);
		}
		else if(inputData.getAccelerometerX() > 7 && inputData.getAccelerometerX() <= 8){
			output.add(Movement.MOVE_BACKWARDS_2);
		}
		else if(inputData.getAccelerometerX() > 8 && inputData.getAccelerometerX() <= 9){
			output.add(Movement.MOVE_BACKWARDS_1);
		}
		else if(inputData.getAccelerometerX() > 9){
			output.add(Movement.MOVE_BACKWARDS);
		}

		if(inputData.getAccelerometerY() < -1.5 && inputData.getAccelerometerY() >= -2.5){
			output.add(Movement.LOOK_LEFT_2);
		}
		else if(inputData.getAccelerometerY() < -2.5 && inputData.getAccelerometerY() >= -3.5){
			output.add(Movement.LOOK_LEFT_1);
		}	
		else if(inputData.getAccelerometerY() < -3.5){
			output.add(Movement.LOOK_LEFT);
		}
		else if(inputData.getAccelerometerY() > 1.5 && inputData.getAccelerometerY() <= 2.5){
			output.add(Movement.LOOK_RIGHT_2);
		}
		else if(inputData.getAccelerometerY() > 2.5 && inputData.getAccelerometerY() <= 3.5){
			output.add(Movement.LOOK_RIGHT_1);
		}
		else if(inputData.getAccelerometerY() > 3.5){
			output.add(Movement.LOOK_RIGHT);
		}
		
*/
		if(inputData.isJoystickDown()){
			output.add(Movement.LOOK_DOWN);
		}
		else if(inputData.isJoystickUp()){
			output.add(Movement.LOOK_UP);
		}
		if(inputData.isJoystickLeft()){
			output.add(Movement.LOOK_LEFT);
		}
		else if(inputData.isJoystickRight()){
			output.add(Movement.LOOK_RIGHT);
		}
		return output;
	}

}
