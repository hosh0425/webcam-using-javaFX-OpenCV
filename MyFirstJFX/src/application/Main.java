package application;

import java.io.ByteArrayInputStream;


import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.MatOfByte;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.videoio.VideoCapture;


public class Main extends Application {

	private static final int SCENE_W = 740;
	private static final int SCENE_H = 580;

	VideoCapture videoCapture;
	Stage stage;
	AnimationTimer timer;
	boolean play=false;

	@Override
	public void start(Stage stage) {

		this.stage = stage;
		Button button=new Button("play/pause");
		ImageView imageView=new ImageView();

		initOpenCv();

		VBox group = new VBox(10);
		group.setAlignment(Pos.CENTER);

		Scene scene = new Scene(group, SCENE_W, SCENE_H);
		group.getChildren().add(imageView);
		group.getChildren().add(button);
		
		stage.setScene(scene);
		stage.setResizable(false);
		stage.show();

		timer = new AnimationTimer() {

			Mat mat = new Mat();

			@Override
			public void handle(long now) {

				videoCapture.read(mat);
				Image image = mat2Image(mat);
				imageView.setImage(image);
			}
		};
		
		button.setOnAction(new EventHandler<ActionEvent>() {
			
			@Override
			public void handle(ActionEvent event) {
				// TODO Auto-generated method stub
				play=!play;
				if(play) 
					timer.start();
				else
					timer.stop();
			}
		});
	}
	

	private void initOpenCv() {

		System.loadLibrary(Core.NATIVE_LIBRARY_NAME);

		videoCapture = new VideoCapture();
		videoCapture.open(0);

		System.out.println("Camera open: " + videoCapture.isOpened());

		stage.setOnCloseRequest(new EventHandler<WindowEvent>() {
			public void handle(WindowEvent we) {

				timer.stop();
				videoCapture.release();

				System.out.println("Camera released");

			}
		});

	}

	public static Image mat2Image(Mat mat) {
		MatOfByte buffer = new MatOfByte();
		Imgcodecs.imencode(".png", mat, buffer);
		return new Image(new ByteArrayInputStream(buffer.toArray()));
	}


	public static void main(String[] args) {
		launch(args);
	}
}


