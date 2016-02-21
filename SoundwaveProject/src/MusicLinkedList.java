import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * Multi-linked list that implements MusicList interface
 * @author RonZapp
 *
 */
public class MusicLinkedList implements MusicList{
	private int numChannels;
	private float sampleRate;
	private int numSamples;
	private Sample head;
	
	public MusicLinkedList(float sampleRate, int numChannels) {
		this.sampleRate = sampleRate;
		this.numChannels = numChannels;
		this.numSamples = 0;
		this.head = null;
	}

	/**
	 * The number of channels in the SoundList
	 * @return The number f channels in the SoundList
	 */
	public int getNumChannels() {
		return numChannels;
	}

	
	/**
	 * Returns the sample rate, in samples per second
	 * @return The sample rate, in samples per second
	 */
	public float getSampleRate() {
		return sampleRate;
	}

	
	/**
	 * Returns the number of samples in the MusicList
	 * @return The number of samples in the MusicList.
	 */
	public int getNumSamples() {
		return numSamples;
	}

	
	/**
	 * Returns The duration of the sound, in seconds.
	 * @return  the duration of the sound, in seconds.
	 */
	public float getDuration() {
		return numSamples * sampleRate;
	}

	
	/**
	 * Add an echo effect to the SoundList.  
	 * @param delay The time (in seconds) before the echo starts
	 * @param percent The percent falloff of the echo (0.5 is 50 percent volume, 0.25 is
	 *        25 percent volume, and so on.  All samples should be clipped to the range -1 .. 1
	 */
	public void addEcho(float delay, float percent) {
		// TODO Auto-generated method stub
		
	}

	
	/**
	 * Reverse the SoundList.  
	 */
	public void reverse() {
		// TODO Auto-generated method stub
		
	}

	
	/**
	 * Change the speed of the sound.   
	 * @param percentChange  How much to change the speed.  1.0 is no change, 2.0 doubles the speed (and the pitch), 0.5 
	 * cuts the speed in half (and lowers the pitch)
	 */
	public void changeSpeed(float percentChange) {
		// TODO Auto-generated method stub
		
	}

	
	/**
	 * Change the sample rate of the SoundList.  This will increase (or decrease) the number of samples in the list, based on
	 * the new rate.  The total time (and pitch) of the sound should remain the same. (Though of course you will lose information
	 * if the new sample rate is lower than the old sample rate)
	 * @param newRate the new sampling rate
	 */
	public void changeSampleRate(float sampleRate) {
		this.sampleRate = sampleRate;
	}

	
	/**
	 * Add a single sample to the end of the SoundList.  Throws an exception if the soundlist has more than 1 channel 
	 * @param sample The sample to add
	 */
	public void addSample(float audio) {
		head = new Sample(audio, head, null);
		numSamples++;
	}

	
	/**
	 * Adds a single sample for each channel to the end of the SoundList.  Throws an exception if the size of the sample 
	 * array is not the same as the number of channels in the sound list
	 * @param sample Array of samples (one for each channel) to add to the end of the SoundList
	 */
	public void addSample(float[] audio) {
		Sample currentSample = null;
		for (int i = audio.length - 1; i >= 0; i--) {
			Sample nextSample = head;
			
			if (nextSample != null) { //if this is not the first sample of this channel
				for (int j = 0; j < i; j++) {
					nextSample = nextSample.nextChannel;
				}
			}
			currentSample = new Sample(audio[0], nextSample, currentSample);
		}
		head = currentSample;
		numSamples++;
		
		// TODO erase debugging stuff
//		if (numSamples >= 9990) {
//			System.out.println("addSample: " + numSamples + " " + audio[0]);
//		}
	}

	
	/**
	 * Return an iterator that traverses the entire sample, returning an array floats (one for each channel)
	 * @return iterator
	 */
	public Iterator<float[]> iterator() {
		return new MultiChannelIterator();
	}

	
	/**
	 * Return an iterator that traverses a single channel of the list
	 * @param channel The channel to traverse
	 * @return the iterator to traverse the list
	 */
	public Iterator<Float> iterator(int channel) {
		if (channel > this.numChannels) {
			throw new IndexOutOfBoundsException("Cannot create iterator because channel " + channel + " does not exist");
		} else {
			return new SingleChannelIterator(channel);
		}
	}

	
	/**
	 * Trim the Soundlist, by removing all samples before the startTime, and all samples past the duration.
	 * Note that if a SoundList represents an 8 second sound, and we call clip(4,2), the new SoundList will be
	 * a 2-second sound (from seconds 4-6 in the old SoundList)
	 * @param startTime Time to start (in seconds)
	 * @param duration Duration (in seconds)
	 */
	public void clip(float startTime, float duration) {
		// TODO Auto-generated method stub
		
	}

	
	/**
	 * Splice a new SoundList into this soundList.  Both SoundLists will be modified.  If the sampleRate of the
	 * clipToSplice is not the same as this sampleList, it will be modified to match the current soundList.
	 * @param startSpliceTime Time to start the splice
	 * @param clipToSplice The other SoundClip to splice in.  
	 */
	public void spliceIn(float startSpliceTime, MusicList clipToSplice) {
		// TODO Auto-generated method stub
		
	}

	
	/**
	 * Combine all channels into a single channel, by adding together all channels into a single channel.
	 * @param allowClipping If allowClipping is true, then values greater than 1.0 or less than -1.0 after the 
	 * addition are clipped to fit in the range.  If allowClipping is false, then if any values are greater than 1.0
	 * or less than -1.0, the entire sample is rescaled  to fit in the range.
	 */
	public void makeMono(boolean allowClipping) {
		// TODO Auto-generated method stub
		
	}

	
	/**
	 * Combines this SoundList with a new soundlist, by adding the samples together.  This SoundList
	 * is modified. 
	 * @param clipToCombine  The clip to combine with this clip
	 * @param allowClipping  If allowClipping is true, then values greater than 1.0 or less than -1.0 after the 
	 * addition are clipped to fit in the range.  If allowClipping is false, then the entire sample is rescaled  
	 */
	public void combine(MusicList clipToCombine, boolean allowClipping) {
		// TODO Auto-generated method stub
		
	}

	
	/**
	 * Returns a clone of this SoundList
	 * @return The cloned SoundList
	 */
	public MusicList clone() {
		// TODO Auto-generated method stub
		return null;
	}
	
	/**
	 * Basic node for linkedlist
	 * @author RonZapp
	 *
	 */
	public class Sample {
		public float audio;
		public Sample next;
		public Sample nextChannel;
		
		
		/**
		 * Constructor for Sample
		 * @param sample audio data for this sample
		 * @param next next sample of the song for this channel
		 * @param nextChannel sample of the next channel at same time as this sample
		 */
		public Sample(float audio, Sample next, Sample nextChannel) {
			this.audio = audio;
			this.next = next;
			this.nextChannel = nextChannel;
			
			// TODO erase debugging stuff
//			if (numSamples < 10) {
//				System.out.println("Sample audio: " + (numSamples + 1) + " " + this.audio); 
//			}
		}
	}
	
	
	/**
	 * Iterator for single channel
	 * @author RonZapp
	 * I got inspiration for the implementation of this class from
	 * http://codereview.stackexchange.com/questions/48109/simple-example-of-an-iterable-and-an-iterator-in-java
	 *
	 */
	public class SingleChannelIterator implements Iterator<Float> {
		private Sample currentSample;
		
		public SingleChannelIterator(int channel) {
			currentSample = head;
			for (int i = 1; i < channel; i++) {
				currentSample = currentSample.nextChannel;
			}
		}

		@Override
		public boolean hasNext() {
			return currentSample != null;
		}

		@Override
		public Float next() {
			if (hasNext()) {
				Float returnData = currentSample.audio;
				currentSample = currentSample.next;
				return returnData;
			}
			throw new NoSuchElementException("There is no next element");
		}
	}
	
	
	/**
	 * Iterator for all channels
	 * @author RonZapp
	 * I got inspiration for the implementation of this class from
	 * http://codereview.stackexchange.com/questions/48109/simple-example-of-an-iterable-and-an-iterator-in-java
	 * 
	 */
	public class MultiChannelIterator implements Iterator<float[]> {
		private Sample[] currentSamples;
		private int sampleNumber = 0;
		
		public MultiChannelIterator() {
			currentSamples = new Sample[numChannels];
			currentSamples[0] = head;
			for (int i = 1; i < numChannels; i++) {
				currentSamples[i] = currentSamples[i-1].nextChannel;
			}
		}
		
		@Override
		public boolean hasNext() {
			return currentSamples[0].next != null;
		}

		@Override
		public float[] next() {
			if (hasNext()) {
				float[] returnData = new float[currentSamples.length];
				for (int i = 0; i < currentSamples.length; i++) {
					returnData[i] = currentSamples[i].audio;
					currentSamples[i] = currentSamples[i].next;
				}
				// TODO erase debugging stuff
//				sampleNumber++;
//				if (sampleNumber <= 10) {
//					System.out.println("iterator: " + sampleNumber + " " + returnData[0]);
//				}
				return returnData;
			}
			throw new NoSuchElementException("There is no next element");
		}
	}
}
