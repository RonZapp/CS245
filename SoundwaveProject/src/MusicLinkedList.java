import java.util.Iterator;

/**
 * Multi-linked list that implements MusicList interface
 * @author RonZapp
 *
 */
public class MusicLinkedList implements MusicList{

	/**
	 * The number of channels in the SoundList
	 * @return The number f channels in the SoundList
	 */
	public int getNumChannels() {
		// TODO Auto-generated method stub
		return 0;
	}

	
	/**
	 * Returns the sample rate, in samples per second
	 * @return The sample rate, in samples per second
	 */
	public float getSampleRate() {
		// TODO Auto-generated method stub
		return 0;
	}

	
	
	public int getNumSamples() {
		// TODO Auto-generated method stub
		return 0;
	}

	
	
	public float getDuration() {
		// TODO Auto-generated method stub
		return 0;
	}

	
	
	public void addEcho(float delay, float percent) {
		// TODO Auto-generated method stub
		
	}

	
	
	public void reverse() {
		// TODO Auto-generated method stub
		
	}

	
	
	public void changeSpeed(float percentChange) {
		// TODO Auto-generated method stub
		
	}

	
	
	public void changeSampleRate(float newRate) {
		// TODO Auto-generated method stub
		
	}

	
	
	public void addSample(float sample) {
		// TODO Auto-generated method stub
		
	}

	
	
	public void addSample(float[] sample) {
		// TODO Auto-generated method stub
		
	}

	
	
	public Iterator<float[]> iterator() {
		// TODO Auto-generated method stub
		return null;
	}

	
	
	public Iterator<Float> iterator(int channel) {
		// TODO Auto-generated method stub
		return null;
	}

	
	
	public void clip(float startTime, float duration) {
		// TODO Auto-generated method stub
		
	}

	
	
	public void spliceIn(float startSpliceTime, MusicList clipToSplice) {
		// TODO Auto-generated method stub
		
	}

	
	
	public void makeMono(boolean allowClipping) {
		// TODO Auto-generated method stub
		
	}

	
	
	public void combine(MusicList clipToCombine, boolean allowClipping) {
		// TODO Auto-generated method stub
		
	}

	
	
	public MusicList clone() {
		// TODO Auto-generated method stub
		return null;
	}

}
