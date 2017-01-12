package hr.fer.zemris.projekt;

import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import org.jcodec.api.JCodecException;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class EvaluationMain extends Application {
	private final String APLICATION_NAME = "Aplikacija za evaluaciju";
	private String videoPath = null;
	private File dumpDir = null;
	private File evaluationFile = null;
	private int rectangleSizeMultiplyer = 1;
	Controller controller;
	Stage primaryStage;
	private Map<Integer, List<javafx.scene.shape.Rectangle>> markedFrames;

	@Override
	public void start(Stage primaryStage) throws Exception {
		this.primaryStage = primaryStage;
		markedFrames = new HashMap<>();

		FXMLLoader loader = new FXMLLoader(getClass().getResource("main.fxml"));
		Parent root = loader.load();
		primaryStage.setTitle(APLICATION_NAME);

		Scene scene = new Scene(root);
		primaryStage.setScene(scene);

		controller = loader.getController();
		controller.setUp(scene, this);

		primaryStage.setOnCloseRequest(new EventHandler<WindowEvent>() {
			@Override
			public void handle(WindowEvent event) {
				if (dumpDir != null) {
					removeDumpDir();
				}
			}
		});

		primaryStage.show();
	}


	public int getRectangleSizeMultiplyer() {
		return rectangleSizeMultiplyer;
	}

	public static void main(String[] args) {
		launch(args);
	}

	public void setVideoPath(String videoPath) throws IOException, JCodecException {
		this.videoPath = videoPath;
		BufferedImage bufferedImage = VideoUtil.getFrame(videoPath,0);
		rectangleSizeMultiplyer = 900/bufferedImage.getWidth();
		if (dumpDir != null) {
			cleanDumpFolder();
		}
	}

	public void removeDumpDir() {
		cleanDumpFolder();
		dumpDir.delete();

	}

	public void cleanDumpFolder() {
		for (File file : dumpDir.listFiles()) {
			if (!file.isDirectory()) {
				file.delete();
			}
		}
	}

	public String getVideoPath() {
		return videoPath;
	}

	public File getDumpDir() {
		return dumpDir;
	}

	public void setDumpDir(File dumpDir) {
		if (this.dumpDir != null) {
			removeDumpDir();
		}
		this.dumpDir = dumpDir;
	}

	public boolean isDumpFolderSet() {
		return dumpDir != null;
	}

	public boolean isVideoDirSet() {
		return videoPath != null;
	}

	public File getEvaluationFile() {
		return evaluationFile;
	}

	public Map<Integer, List<javafx.scene.shape.Rectangle>> getMarkedFrames() {
		return markedFrames;
	}

	public List<javafx.scene.shape.Rectangle> getMarkedFrame(int frameNumber){
		return markedFrames.get(frameNumber);
	}

	public void setEvaluationFile(File evaluationFile) {
		this.evaluationFile = evaluationFile;
	}

}

