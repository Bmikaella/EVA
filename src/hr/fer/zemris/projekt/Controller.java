package hr.fer.zemris.projekt;

import javafx.embed.swing.SwingFXUtils;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.Slider;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import org.bytedeco.javacv.FrameGrabber;
import org.jcodec.api.JCodecException;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class Controller implements Initializable {
	private Scene scene;
	private EvaluationMain evaluationMainApp;
	private static final int FRAME_HOP = 15;
	@FXML
	private Label frameNumberField;
	@FXML
	private Slider frameSlider;
	@FXML
	private ImageView footballFieldImage;
	@FXML
	private ListView markedFramesList;
	@FXML
	private Label recallValue;
	@FXML
	private Label precisionValue;
	@FXML
	private Label f1Value;

	public void setUp(Scene scene, EvaluationMain evaluationMain) {
		this.scene = scene;
		this.evaluationMainApp = evaluationMain;
	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {

	}


	//TODO add marking on the frames

	@FXML
	public void newFrameSelected(Event event) throws IOException, JCodecException {
		Slider slider = (Slider) event.getSource();
		long frame = Math.round(slider.getValue());
		setSelectedFrame((int) frame);
	}

	public void setSelectedFrame(int number) throws IOException, JCodecException {
		int frameNumber = FRAME_HOP * number;
		frameNumberField.setText(String.valueOf(frameNumber));

		File frameFile = evaluationMainApp.getDumpDir().toPath().resolve(frameNumber + ".png").toFile();
		BufferedImage fieldImage;
		if (frameFile.exists()) {
			fieldImage = ImageIO.read(frameFile);
		} else {
			fieldImage = VideoUtil.getFrame(evaluationMainApp.getVideoPath(), frameNumber);
			ImageIO.write(fieldImage, "png", frameFile);
		}

		Image image = SwingFXUtils.toFXImage(fieldImage, null);
		footballFieldImage.setImage(image);

		//TODO: add marked rectangles
	}

	@FXML
	public void setVideoAndSetUp(ActionEvent actionEvent) throws FrameGrabber.Exception, IOException, JCodecException {
		//TODO : dodajte jos neke ekstenzije ako mislite da je potrebno
		FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("Video", "*.mp4", "*.avi", "*.mkv");
		FileChooser fileChooser = new FileChooser();
		fileChooser.getExtensionFilters().add(extFilter);
		fileChooser.setTitle("Odaberi video");
		File file = fileChooser.showOpenDialog(scene.getWindow());

		evaluationMainApp.setVideoPath(file.getPath());
		int numberOfFrames = VideoUtil.getNumberOfFrames(file.getPath());

		frameSlider.setMax(numberOfFrames / FRAME_HOP);
		frameSlider.setMin(0);
		frameSlider.setBlockIncrement(50);

		evaluationMainApp.setEvaluationFile(null);

		if(evaluationMainApp.isDumpFolderSet()){
			setSelectedFrame(0);
			frameSlider.setDisable(false);
		}
	}

	@FXML
	public void setImageDumpDir(ActionEvent actionEvent) throws IOException, JCodecException {
		DirectoryChooser fileChooser = new DirectoryChooser();
		fileChooser.setTitle("Odaberi direktorij");
		File file = fileChooser.showDialog(scene.getWindow());
		file = file.toPath().resolve("images").toFile();
		file.mkdir();
		evaluationMainApp.setDumpDir(file);
		if(evaluationMainApp.isVideoDirSet()){
			setSelectedFrame(0);
			frameSlider.setDisable(false);
		}
	}



	@FXML
	public void setEvaluationFile(ActionEvent actionEvent) {
		FileChooser fileChooser = new FileChooser();
		fileChooser.setTitle("Odaberi datoteku za evaluaciju");
		File file = fileChooser.showOpenDialog(scene.getWindow());
		evaluationMainApp.setEvaluationFile(file);
	}


	@FXML
	public void evaluate(ActionEvent actionEvent) {
		//TODO check first if you can evaluate
		if(evaluationMainApp.getMarkedFrames().size() == 0){
			//TODO throw warning that there should be frames that are marked
		}
	}

	public void saveCurrentFrame(ActionEvent actionEvent) {
	}

	public void saveFileWithMarks(ActionEvent actionEvent) {
	}

	public void saveCurrentFrameWithMarks(ActionEvent actionEvent) {
	}

	public void saveMarks(ActionEvent actionEvent) {
	}
}
