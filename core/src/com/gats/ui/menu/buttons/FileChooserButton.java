package com.gats.ui.menu.buttons;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;

import javax.swing.*;
import javax.swing.filechooser.FileFilter;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;

/**
 * Class that starts a FileChooser to select a File, when pressed.
 * Not the fanciest way but works rn
 */
public class FileChooserButton extends TextButton {

	private final String labelText = "Selected Replay: ";
	private final JFileChooser chooser;
	private final FileDialog fileDialog;
	private String selectedFilePath = "";

public FileChooserButton(Skin skin) {
	super("Select a Replay to start", skin);
	chooser = new JFileChooser();
	FileNameExtensionFilter filter = new FileNameExtensionFilter(
			"Gadsen Replay Files: .replay", "replay");
	chooser.setFileFilter(filter);

	JFrame frame = new JFrame();
	fileDialog = new FileDialog(frame, "Choose a file", FileDialog.LOAD);


	this.addListener(new ChangeListener() {
		@Override
		public void changed(ChangeEvent event, Actor actor) {
			SwingUtilities.invokeLater(new ChooseFile());
		}
	});
}


class ChooseFile implements Runnable{

	@Override
	public void run() {
		//selectFile();
		dialogSelect();

	}

	private void dialogSelect(){
		fileDialog.setVisible(true);
		String filename = fileDialog.getFile();
		if (filename == null)
			System.out.println("You cancelled the choice");
		else
			System.out.println("You chose " + filename);
	}
	private JFrame setupJFrame(){
		JFrame frame = new JFrame();
		frame.setVisible(true);
		frame.toFront();
		frame.setVisible(false);

		return frame;
	}
	private void selectFile() {
		//opens filechooser

		JFrame frame = setupJFrame();
		int returnVal = chooser.showOpenDialog(frame);
		frame.dispose();

		if (returnVal == JFileChooser.APPROVE_OPTION) {
			System.out.println("You chose to open this file: " +
									   chooser.getSelectedFile().getName());

			setSelected(selectedFilePath);
		}
	}
}

private void setSelected(String selected){
	this.selectedFilePath = selected;
}

	@Override
	public boolean remove() {

		return super.remove();
	}
}
