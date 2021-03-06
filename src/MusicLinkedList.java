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
	private Sample tail;
	
	public MusicLinkedList(float sampleRate, int numChannels) {
		this.sampleRate = sampleRate;
		this.numChannels = numChannels;
		this.numSamples = 0;
		this.head = null;
		this.tail = null;
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
		return (numSamples - 1) / sampleRate;
	}

	
	/**
	 * Add an echo effect to the SoundList.  
	 * @param delay The time (in seconds) before the echo starts
	 * @param percent The percent falloff of the echo (0.5 is 50 percent volume, 0.25 is
	 *        25 percent volume, and so on.  All samples should be clipped to the range -1 .. 1
	 */
	public void addEcho(float delay, float percent) {
		Sample fromPointer = head;
		Sample toPointer = head;
		for (int i = 0 ; i < delay*this.sampleRate; i++) {
			toPointer = toPointer.next;
		}
		while (toPointer != null) {
			Sample toPointerChannelIterator = toPointer;
			Sample fromPointerChannelIterator = fromPointer;
			for (int i = 0; i < numChannels; i++) {
				toPointerChannelIterator.audio += fromPointerChannelIterator.audio*percent;
				toPointerChannelIterator = toPointerChannelIterator.nextChannel;
				fromPointerChannelIterator = fromPointerChannelIterator.nextChannel;
			}
			toPointer = toPointer.next;
			fromPointer = fromPointer.next;
		}
	}

	
	/**
	 * Reverse the SoundList.  
	 */
	public void reverse() {
		Sample sample1 = head;
		Sample sample2 = sample1.next;
		Sample sample3 = sample2.next;
		
		Sample channelCrawler1 = sample1;
		
		//set first samples to point to null
		for (int i = 0; i < numChannels; i++) {
			channelCrawler1.next = null;
			channelCrawler1 = channelCrawler1.nextChannel;
		}
		
		channelCrawler1 = sample1;
		Sample channelCrawler2 = sample2;
		
		//set all samples to switch direction they point in
		for (int j = 0; j < numSamples - 3; j++) {
			channelCrawler1 = sample1;
			channelCrawler2 = sample2;
			
			for (int i = 0; i < numChannels; i++) {
				channelCrawler2.next = channelCrawler1;
				channelCrawler1 = channelCrawler1.nextChannel;
				channelCrawler2 = channelCrawler2.nextChannel;
				
			}
			sample1 = sample2;
			sample2 = sample3;
			sample3 = sample3.next;
		}
		
		channelCrawler1 = sample1;
		channelCrawler2 = sample2;
		Sample channelCrawler3 = sample3;
		
		//set last samples to switch direction they point in
		for (int i = 0; i < numChannels; i++) {
			channelCrawler3.next = channelCrawler2;
			channelCrawler2.next = channelCrawler1;
			channelCrawler1 = channelCrawler1.nextChannel;
			channelCrawler2 = channelCrawler2.nextChannel;
			channelCrawler3 = channelCrawler3.nextChannel;
		}
		
		Sample temp = tail;
		tail = head;
		head = temp;
	}

	
	/**
	 * Change the speed of the sound.   
	 * @param percentChange  How much to change the speed.  1.0 is no change, 2.0 doubles the speed (and the pitch), 0.5 
	 * cuts the speed in half (and lowers the pitch)
	 */
	public void changeSpeed(float percentChange) {
		this.sampleRate = this.sampleRate*percentChange;
	}

	
	/**
	 * Change the sample rate of the SoundList.  This will increase (or decrease) the number of samples in the list, based on
	 * the new rate.  The total time (and pitch) of the sound should remain the same. (Though of course you will lose information
	 * if the new sample rate is lower than the old sample rate)
	 * @param newRate the new sampling rate
	 */
	public void changeSampleRate(float sampleRate) {
		Double oldSampleRateDouble = (double) this.sampleRate;
		Double newSampleRateDouble = (double) sampleRate;
		MusicLinkedList newList = new MusicLinkedList(sampleRate, this.numChannels);
		Iterator<float[]> itLeading = iterator();
		Iterator<float[]> itFollowing = iterator();
		float[] audioLeading = itLeading.next();
		float[] audioFollowing = itFollowing.next();
		double oldTimeLeading = 0;
		double oldTimeFollowing = 0;
		double newTime = 0;
		
		//first data point
		audioLeading = itLeading.next();
		oldTimeLeading += 1/oldSampleRateDouble;
		newList.addSample(audioFollowing);
		newTime += 1/sampleRate;
		
		//rest of data points
		while (newTime <= this.getDuration()) {
			while (oldTimeLeading < newTime) {
				audioLeading = itLeading.next();
				oldTimeLeading += 1/oldSampleRateDouble;
			}
			while (oldTimeFollowing < newTime - 1/oldSampleRateDouble) {
				audioFollowing = itFollowing.next();
				oldTimeFollowing += 1/oldSampleRateDouble;
			}
			
			double ratio = (newTime - oldTimeFollowing)/(oldTimeLeading - oldTimeFollowing);
			float[] newSample = new float[numChannels];
			for (int c = 0; c < numChannels; c++) {
				newSample[c] = (float) (audioFollowing[c] + (audioLeading[c] - audioFollowing[c])*ratio);
			}
			
			newList.addSample(newSample);
			newTime += 1/sampleRate;
		}
		this.head = newList.head;
		this.tail = newList.tail;
		this.numSamples = newList.numSamples;
		this.sampleRate = newList.sampleRate;
	}

	
	/**
	 * Add a single sample to the end of the SoundList.  Throws an exception if the soundlist has more than 1 channel 
	 * @param sample The sample to add
	 */
	public void addSample(float audio) {
		if (tail == null) {
			tail = new Sample(audio, null, null);
			head = tail;
		} else {
			Sample newSample = new Sample(audio, null, null);
			tail.next = newSample;
			tail = newSample;
		}
		numSamples++;
	}

	
	/**
	 * Adds a single sample for each channel to the end of the SoundList.  Throws an exception if the size of the sample 
	 * array is not the same as the number of channels in the sound list
	 * @param sample Array of samples (one for each channel) to add to the end of the SoundList
	 */
	public void addSample(float[] audio) {
		if (audio.length != numChannels) {
			throw new IllegalArgumentException("Audio being added does not have the correct number of channels");
		}
		if (tail == null) {
			for (int i = audio.length - 1; i >= 0; i--) {
				tail = new Sample(audio[i], null, tail);
			}
			head = tail;
		} else {
			Sample temp = tail;
			Sample newSample = null;
			
			//create new Samples for each channel
			for (int i = audio.length - 1; i >= 0; i--) {
				newSample = new Sample(audio[i], null, newSample);
			}
			tail = newSample;
			
			//iterate over new samples and assign them to tail sample's next variable
			while (temp != null) {
				temp.next = newSample;
				temp = temp.nextChannel;
				newSample = newSample.nextChannel;
			}
		}
		numSamples++;
		

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
		Sample pointer = head;
		for (int i = 0; i < startTime*this.sampleRate; i++) {
			pointer = pointer.next;
		}
		head = pointer;
		for (int i = 0; i < duration*this.sampleRate; i++) {
			pointer = pointer.next;
		}
		tail = pointer;
		
		Sample tailChannelIterator = tail;
		for (int i = 0; i < numChannels; i++) {
			tailChannelIterator.next = null;
			tailChannelIterator = tailChannelIterator.nextChannel;
		}
	}

	
	/**
	 * Splice a new SoundList into this soundList.  Both SoundLists will be modified.  If the sampleRate of the
	 * clipToSplice is not the same as this sampleList, it will be modified to match the current soundList.
	 * @param startSpliceTime Time to start the splice
	 * @param clipToSplice The other SoundClip to splice in.  
	 */
	public void spliceIn(float startSpliceTime, MusicList clipToSplice) {
		if (clipToSplice.getSampleRate() != this.sampleRate) {
			clipToSplice.changeSampleRate(this.sampleRate);
		}
		
		//find point where clip will be spliced in
		Sample pointer = head;
		for (int i = 0; i < startSpliceTime*this.sampleRate; i++) {
			pointer = pointer.next;
		}
		
		//create temporary holder for part of clip to go after splice
		Sample tempHead = pointer.next;
		Sample tempTail = this.tail;
		this.tail = pointer; //set this clip to end at point where splice will start
		
		//add clipToSplice
		Iterator<float[]> iterator = clipToSplice.iterator();
		while (iterator.hasNext()) {
			addSample(iterator.next());
		}
		
		//connect tail of this clip (which is first part of original clip + cliptoSplice) with the head of the temp clip (which is second part of original clip)
		Sample tailChannelIterator = tail;
		Sample tempHeadChannelIterator = tempHead;
		for (int i = 0; i < numChannels; i++) {
			tailChannelIterator.next = tempHeadChannelIterator;
			tailChannelIterator = tailChannelIterator.nextChannel;
			tempHeadChannelIterator = tempHeadChannelIterator.nextChannel;
		}
		
		//set tail to end of second part of original clip
		this.tail = tempTail;
	}

	
	/**
	 * Combine all channels into a single channel, by adding together all channels into a single channel.
	 * @param allowClipping If allowClipping is true, then values greater than 1.0 or less than -1.0 after the 
	 * addition are clipped to fit in the range.  If allowClipping is false, then if any values are greater than 1.0
	 * or less than -1.0, the entire sample is rescaled  to fit in the range.
	 */
	public void makeMono(boolean allowClipping) {
		MusicLinkedList newList = new MusicLinkedList(this.numSamples, this.numChannels);
		Iterator<float[]> iterator = iterator();
		float biggestWave = 0;
		
		for (int t = 0; t < numSamples; t++) {
			float[] seperateAudio = iterator.next();
			float combinedAudio = 0;
			
			for (float audio: seperateAudio) {
				combinedAudio += audio;
			}
			
			if (allowClipping) {
				if (combinedAudio > 1) {
					combinedAudio = 1;
				} else if (combinedAudio < -1) {
					combinedAudio = -1;
				}
			} else {
				if (combinedAudio > biggestWave) {
					biggestWave = combinedAudio;
				} else if (combinedAudio*-1 > biggestWave) {
					biggestWave = combinedAudio*-1;
				}
			}
			
			float[] newAudio = new float[numChannels];
			for (int i = 0; i < newAudio.length; i++) {
				newAudio[i] = combinedAudio;
			}
			
			newList.addSample(newAudio);
		}
		
		this.head = newList.head;
		this.tail = newList.tail;
		this.numSamples = newList.numSamples;
		
		if (!allowClipping) {
			if (biggestWave > 1) {
				MusicLinkedList rescaledList = new MusicLinkedList(this.sampleRate, this.numChannels);
				Iterator<float[]> preScalingIterator = iterator();
				
				for (int t = 0; t < numSamples; t++) {
					float[] preScalingSamples = preScalingIterator.next();
					float[] postScalingSamples = new float[numChannels];
					
					for (int i = 0; i < numChannels; i++) {
						postScalingSamples[i] = preScalingSamples[i] / biggestWave;
					}
					rescaledList.addSample(postScalingSamples);
				}
				
				
				this.head = rescaledList.head;
				this.tail = rescaledList.tail;
				this.numSamples = rescaledList.numSamples;
			}
		}
	}

	
	/**
	 * Combines this SoundList with a new soundlist, by adding the samples together.  This SoundList
	 * is modified. 
	 * @param clipToCombine  The clip to combine with this clip
	 * @param allowClipping  If allowClipping is true, then values greater than 1.0 or less than -1.0 after the 
	 * addition are clipped to fit in the range.  If allowClipping is false, then the entire sample is rescaled  
	 */
	public void combine(MusicList clipToCombine, boolean allowClipping) {
		Iterator<float[]> ogClipIterator = iterator();
		Iterator<float[]> newClipIterator = clipToCombine.iterator();
		float biggestWave = 0;
		MusicLinkedList newList = new MusicLinkedList(this.numSamples, this.numChannels);
		for (int t = 0; t < numSamples; t++) {
			float[] ogSamples = ogClipIterator.next();
			float[] newSamples = newClipIterator.next();
			float[] combinedSamples = new float[numChannels];
			for (int i = 0; i < numChannels; i++) {
				combinedSamples[i] = ogSamples[i] + newSamples[i];
				
				if (allowClipping) {
					if (combinedSamples[i] > 1.0) {
						combinedSamples[i] = 1;
					} else if (combinedSamples[i] < -1.0) {
						combinedSamples[i] = -1;
					}
				} else {
					if (combinedSamples[i] > biggestWave) {
						biggestWave = combinedSamples[i];
					} else if (combinedSamples[i]*-1 > biggestWave) {
						biggestWave = combinedSamples[i]*-1;
					}
				}
			}
			
			newList.addSample(combinedSamples);
		}
		this.head = newList.head;
		this.tail = newList.tail;
		this.numSamples = newList.numSamples;
		
		if (!allowClipping) {
			if (biggestWave > 1) {
				MusicLinkedList rescaledList = new MusicLinkedList(this.sampleRate, this.numChannels);
				Iterator<float[]> preScalingIterator = iterator();
				
				for (int t = 0; t < numSamples; t++) {
					float[] preScalingSamples = preScalingIterator.next();
					float[] postScalingSamples = new float[numChannels];
					
					for (int i = 0; i < numChannels; i++) {
						postScalingSamples[i] = preScalingSamples[i] / biggestWave;
					}
					rescaledList.addSample(postScalingSamples);
				}
				
				
				this.head = rescaledList.head;
				this.tail = rescaledList.tail;
				this.numSamples = rescaledList.numSamples;
			}
		}
	}

	
	/**
	 * Returns a clone of this SoundList
	 * @return The cloned SoundList
	 */
	public MusicList clone() {
		MusicLinkedList newList = new MusicLinkedList(this.sampleRate, this.numChannels);
		Iterator<float[]> iterator = iterator();
		int samplesAdded = 0;
		while (iterator.hasNext()) {
			newList.addSample(iterator.next());
		}
		return newList;
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
			for (int i = 0; i < channel; i++) {
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
		
		public MultiChannelIterator() {
			currentSamples = new Sample[numChannels];
			currentSamples[0] = head;
			for (int i = 1; i < numChannels; i++) {
				currentSamples[i] = currentSamples[i-1].nextChannel;
			}
		}
		
		@Override
		public boolean hasNext() {
			return currentSamples[0] != null;
		}

		@Override
		public float[] next() {
			if (hasNext()) {
				float[] returnData = new float[currentSamples.length];
				for (int i = 0; i < currentSamples.length; i++) {
					returnData[i] = currentSamples[i].audio;
					currentSamples[i] = currentSamples[i].next;
				}
				return returnData;
			}
			throw new NoSuchElementException("Iterator: There is no next element");
		}
	}
}
