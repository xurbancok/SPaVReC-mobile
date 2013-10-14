package sk.fiit.remotefiit.obj;

import java.util.ArrayList;

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

}
