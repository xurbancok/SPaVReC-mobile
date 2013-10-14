package sk.fiit.remotefiit.interfaces;

import java.util.ArrayList;

import sk.fiit.remotefiit.emun.Movement;
import sk.fiit.remotefiit.obj.PositionData;

public interface DataTransformation {
	
	ArrayList<Movement> rawData(PositionData inputData, boolean extendedFunction, boolean reset);
	
}
