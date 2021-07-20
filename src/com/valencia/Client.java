package com.valencia;

import java.io.*;
import java.net.*;
import java.sql.Timestamp;

import javafx.application.*;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.*;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;

/**
 * The main class for the Client application. Contains the main method and the start method. The client program takes an integer as an input and sends the value to the server. The client then displays the results from the server
 */
public class Client extends Application{
	
	DataOutputStream output = null;
	DataInputStream input = null;

	/**
	 * The main method
	 * 
	 * @param args
	 * @throws IOException 
	 */
	public static void main(String[] args) {
		
		launch(args);
		System.exit(0);
	}

	@Override
	public void start(Stage stage) throws Exception {
		
		stage.setTitle("Prime Numbers Client");
		
		TextField numberField = new TextField();
		Label inputLabel = new Label("Type an integer:");
		TextArea outputArea = new TextArea();
		
		numberField.setPrefColumnCount(20);
		numberField.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent event) {


				try {
					
					int inputNumber = Integer.parseInt(numberField.getText().trim());
					
					outputArea.appendText(formatText("Input: " + String.valueOf(inputNumber)));
					output.writeInt(inputNumber);
					output.flush();
					
					Boolean isPrime = input.readBoolean();
					
					if(isPrime) 
						outputArea.appendText(formatText(inputNumber + " is prime"));
					else
						outputArea.appendText(formatText(inputNumber + " is not prime"));
						
				} catch (NumberFormatException nfE) {
					
					outputArea.appendText(formatText(nfE.getMessage()));
				} catch (IOException ioE) {
					
					outputArea.appendText(formatText(ioE.getMessage()));
				}
			}
		});
		
		
		inputLabel.setStyle("-fx-font-weight: bold;");

		outputArea.setEditable(false);
		outputArea.appendText(formatText("Prime Numbers Client started"));
		
		HBox hb = new HBox();
		hb.getChildren().addAll(inputLabel,numberField);
		hb.setSpacing(20);
		hb.setAlignment(Pos.CENTER);
		hb.setStyle("-fx-background-color: #00B300;");
		hb.setPrefHeight(40);
		
		BorderPane bp = new BorderPane();
		bp.setTop(hb);
		bp.setCenter(outputArea);
		
		Scene scene = new Scene(bp,480,360);
		
		stage.setScene(scene);
		stage.show();
		
		try {

			outputArea.appendText(formatText("Connecting to server..."));
			Socket socket = new Socket("localhost", 8000);
			outputArea.appendText(formatText("Connected to server"));

			output = new DataOutputStream(socket.getOutputStream());
			input = new DataInputStream(socket.getInputStream());

		} catch (IOException ioE) {

			outputArea.appendText(formatText(ioE.getMessage()));
		}
	}
	
	/**
	 * Helper method to format text outputted to the the outputArea element of the application. Adds a timestamp at the beginning of the string and skips a line at the end
	 * 
	 * @param text The string to format
	 * @return Returns the formatted string
	 */
	public static String formatText(String text) {
		
		StringBuilder sb = new StringBuilder(text);
		
		sb.insert(0, "[" + new Timestamp(System.currentTimeMillis()) + "]: ");
		sb.append("\n");
		
		return sb.toString();
	}
}
