package sk.fiit.remotefiit.emun;

public enum Movement {
	 MOVE_LEFT(1), MOVE_RIGHT(2), MOVE_UP(3), MOVE_DOWN(4), MOVE_FORWARDS(5), MOVE_BACKWARDS(6),
	 LOOK_LEFT(7), LOOK_RIGHT(8), LOOK_UP(9), LOOK_DOWN(10), LOOK_FORWARDS(11), LOOK_BACKWARDS(12),
	 
	 MOVE_LEFT_1(13), MOVE_RIGHT_1(14), MOVE_UP_1(15), MOVE_DOWN_1(16), MOVE_FORWARDS_1(17), MOVE_BACKWARDS_1(18),
	 MOVE_LEFT_2(19), MOVE_RIGHT_2(20), MOVE_UP_2(21), MOVE_DOWN_2(22), MOVE_FORWARDS_2(23), MOVE_BACKWARDS_2(24),
	 LOOK_LEFT_1(25), LOOK_LEFT_2(27), LOOK_RIGHT_1(26), LOOK_RIGHT_2(28);
	 
	 private int code;
	 
	 private Movement(int c) {
	   code = c;
	 }
	 
	 public int getCode() {
	   return code;
	 }
}
