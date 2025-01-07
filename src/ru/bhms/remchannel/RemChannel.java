package ru.bhms.remchannel;

import javax.sound.midi.*;
import java.io.*;

public class RemChannel {
	public static Sequence midi;
	public static int channel;
	
	public static final void main(String[] args) throws Exception {
		if(args.length < 2) {
			System.err.println("Usage:\n"
					+ "		java -jar RemChannel.jar <MIDI file> <channel to delete> [...]\n"
					+ "\n"
					+ "Input and output files example:\n"
					+ "		Input: \"Jingle Bells.mid\"\n"
					+ "		Output: \"Jingle Bells_output.mid\"\n"
					+ "\n"
					+ "Deleting drums from MIDI example:\n"
					+ "		java -jar RemChannel.jar \"C:/Users/User/Desktop/Jingle Bells.mid\" 10\n");
			return;
		}
		midi = MidiSystem.getSequence(new File(args[0]));
		channel = Integer.parseInt(args[1]);
		
		deleteChannel(new File(removeExtension(args[0]) + "_output.mid"));
	}
	
	public static final void deleteChannel(File out) throws Exception {
		Track[] tracks = midi.getTracks();
		int trackNum = tracks.length;
		System.out.println("Track num: " + trackNum + "\n");
		for(int trackIndex = 0;trackIndex < trackNum;trackIndex++) {
			Track track = tracks[trackIndex];
			int size = track.size();
			for(int i = 0;i < size;i++) {
				MidiEvent event = track.get(i);
				MidiMessage message = event.getMessage();
				if(message instanceof ShortMessage) {
					if(((ShortMessage)(message)).getChannel() == (channel - 1)) {
						track.remove(event);
						i--;
						size--;
					}
				}
			}
			System.out.println("All events with specified channel removed from " + trackIndex + " track");
		}
		System.out.println("\nWriting...");
		MidiSystem.write(midi,1,out);
		System.out.println("Wrote " + out.length() + " bytes");
	}
	
	public static String removeExtension(String file) {
		StringBuilder name = new StringBuilder();
		char ch = 0;
		int index = file.length() - 1;

		for(;(ch = file.charAt(index)) != '.';index--) {
		}
		index--;
		for(;index >= 0;index--) {
			ch = file.charAt(index);
			name.append(ch);
		}

		return name.reverse().toString();
	}
}
