package simulator;



import javafx.animation.AnimationTimer;

public class Simulator {

	private class AnimationController extends AnimationTimer {

		@Override
		public void handle(long arg0) {
			
		}
		
	}
	
	
	AnimationController simulator;
	private boolean isPaused;
	private boolean isStopped;
	
	public void pause() {
		simulator.stop();
		isPaused = true;
		isStopped = false;
	}
	
	
	public void play() {
		simulator.start();
		isPaused = false;
		isStopped = true;
	}
	

}
