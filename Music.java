package firmament;
import java.io.File;
import java.io.IOException;

import javax.sound.midi.InvalidMidiDataException;
import javax.sound.midi.MidiSystem;
import javax.sound.midi.MidiUnavailableException;
import javax.sound.midi.Sequence;
import javax.sound.midi.Sequencer;

import org.newdawn.slick.openal.Audio;
import org.newdawn.slick.openal.AudioLoader;
import org.newdawn.slick.util.ResourceLoader;


public class Music {

	private Sequence mainTheme;
	private Sequencer sequencer;
	private Audio enter;
	
	public Music(){
		try{

			mainTheme = MidiSystem.getSequence(new File("./res/DaybreakFInal.mid"));
			enter = AudioLoader.getAudio("WAV", ResourceLoader.getResourceAsStream("./res/enter.wav"));
			// Create a sequencer for the sequence
			sequencer = MidiSystem.getSequencer();
			sequencer.open();
			sequencer.setSequence(mainTheme);
		}catch(IOException e){
			e.printStackTrace();}
		catch (InvalidMidiDataException e) {
			e.printStackTrace();} 
		catch (MidiUnavailableException e) {
			e.printStackTrace();
		}

	}

	public void start(){

		sequencer.start();
	}
	
	public void stop(){
		enter.playAsSoundEffect(.2f, 2.5f, false);
		sequencer.stop();
	}

}
