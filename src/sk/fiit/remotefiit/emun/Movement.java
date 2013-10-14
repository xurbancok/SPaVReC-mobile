package sk.fiit.remotefiit.emun;

public enum Movement {
	 MOVE_LEFT(1), MOVE_RIGHT(2), MOVE_UP(3), MOVE_DOWN(4), MOVE_FORWARDS(5), MOVE_BACKWARDS(6),
	 LOOK_LEFT(7), LOOK_RIGHT(8), LOOK_UP(9), LOOK_DOWN(10), LOOK_FORWARDS(11), LOOK_BACKWARDS(12);
	 
	 private int code;
	 
	 private Movement(int c) {
	   code = c;
	 }
	 
	 public int getCode() {
	   return code;
	 }
}
